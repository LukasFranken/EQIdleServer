package de.instinct.social.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.social.dto.Friend;
import de.instinct.api.social.dto.FriendDeleteResponse;
import de.instinct.api.social.dto.FriendRequest;
import de.instinct.api.social.dto.FriendRequestRespondResponse;
import de.instinct.api.social.dto.FriendRequestSendResponse;
import de.instinct.api.social.dto.Group;
import de.instinct.api.social.dto.GroupCreateResponse;
import de.instinct.api.social.dto.GroupInvite;
import de.instinct.api.social.dto.GroupInviteRespondResponse;
import de.instinct.api.social.dto.GroupInviteResponse;
import de.instinct.api.social.dto.GroupLeaveResponse;
import de.instinct.api.social.dto.PlayerSocialData;
import de.instinct.api.social.dto.PlayerStatus;
import de.instinct.api.social.dto.SocialPushData;
import de.instinct.api.social.dto.UserCreationResponse;
import de.instinct.social.service.SocialService;

@Service
public class SocialServiceImpl implements SocialService {

	private Map<String, PlayerSocialData> socialDatas;
	private Map<String, Group> groups;
	
	public SocialServiceImpl() {
		socialDatas = new HashMap<>();
		groups = new HashMap<>();
	}
	
	@Override
	public UserCreationResponse init(String token) {
		if (socialDatas.containsKey(token)) return UserCreationResponse.ALREADY_EXISTS;
		
		PlayerSocialData initPlayerData = new PlayerSocialData();
		initPlayerData.setName(API.meta().profile(token).getUsername());
		initPlayerData.setStatus(PlayerStatus.OFFLINE);
		initPlayerData.setFriends(new ArrayList<>());
		initPlayerData.setFriendRequests(new ArrayList<>());
		initPlayerData.setGroupInvites(new ArrayList<>());
		socialDatas.put(token, initPlayerData);
		return UserCreationResponse.SUCCESS;
	}
	
	@Override
	public SocialPushData getPushData(String token) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		playerSocialData.setLastRefreshTimestamp(System.currentTimeMillis());
		playerSocialData.setStatus(PlayerStatus.ONLINE);
		return null;
	}

	@Override
	public PlayerSocialData getPlayerSocialData(String token) {
		PlayerSocialData playerSocialData = socialDatas.get(token);
		if (playerSocialData == null) return null;
		if (System.currentTimeMillis() >= playerSocialData.getLastRefreshTimestamp() + (1000 * 60)) {
			playerSocialData.setStatus(PlayerStatus.OFFLINE);
		}
		return playerSocialData;
	}

	@Override
	public Group getGroup(String token) {
		return groups.get(token);
	}

	@Override
	public FriendRequestSendResponse sendFriendRequest(String token, String targetName) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return FriendRequestSendResponse.USER_NOT_FOUND;
		if (playerSocialData.getName().equals(targetName)) return FriendRequestSendResponse.TARGET_IS_SELF;
		PlayerSocialData targetSocialData = getSocialData(targetName);
		if (targetSocialData == null) return FriendRequestSendResponse.TARGET_USER_NOT_FOUND;
		if (targetSocialData.hasFriend(playerSocialData.getName())) return FriendRequestSendResponse.ALREADY_FRIENDS;
		if (targetSocialData.hasFriendRequest(playerSocialData.getName())) return FriendRequestSendResponse.ALREADY_REQUESTED;
		
		FriendRequest newFriendRequest = new FriendRequest();
		newFriendRequest.setFromName(playerSocialData.getName());
		newFriendRequest.setTimestamp(System.currentTimeMillis());
		targetSocialData.getFriendRequests().add(newFriendRequest);
		return FriendRequestSendResponse.SUCCESS;
	}

	@Override
	public FriendRequestRespondResponse respondToFriendRequest(String token, String requesterName, boolean accept) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return FriendRequestRespondResponse.USER_NOT_FOUND;
		FriendRequest request = playerSocialData.getFriendRequest(requesterName);
		if (request == null) return FriendRequestRespondResponse.REQUEST_NOT_FOUND;
		
		playerSocialData.getFriendRequests().remove(request);
		if (accept) {
			long timestamp = System.currentTimeMillis();
			Friend newFriend = new Friend();
			newFriend.setName(requesterName);
			newFriend.setSinceTimestamp(timestamp);
			playerSocialData.getFriends().add(newFriend);
			
			Friend newFriendForRequester = new Friend();
			newFriendForRequester.setName(playerSocialData.getName());
			newFriendForRequester.setSinceTimestamp(timestamp);
			getSocialData(requesterName).getFriends().add(newFriendForRequester);
		}
		return FriendRequestRespondResponse.SUCCESS;
	}

	@Override
	public FriendDeleteResponse deleteFriend(String token, String friendName) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return FriendDeleteResponse.USER_NOT_FOUND;
		Friend friend = playerSocialData.getFriend(friendName);
		if (friend == null) return FriendDeleteResponse.NOT_FRIENDS;
		
		playerSocialData.getFriends().remove(friend);
		PlayerSocialData friendSocialData = getSocialData(friendName);
		friendSocialData.getFriends().remove(friendSocialData.getFriend(playerSocialData.getName()));
		return FriendDeleteResponse.SUCCESS;
	}

	@Override
	public GroupCreateResponse createGroup(String token) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return GroupCreateResponse.USER_NOT_FOUND;
		if (playerSocialData.getGroupToken() != null) return GroupCreateResponse.ALREADY_IN_GROUP;
		
		Group newGroup = new Group();
		newGroup.setMembers(new ArrayList<>());
		newGroup.getMembers().add(playerSocialData.getName());
		String groupToken = UUID.randomUUID().toString();
		groups.put(groupToken, newGroup);
		playerSocialData.setGroupToken(groupToken);
		return GroupCreateResponse.SUCCESS;
	}

	@Override
	public GroupInviteResponse inviteToGroup(String token, String targetName) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return GroupInviteResponse.USER_NOT_FOUND;
		if (playerSocialData.getGroupToken() == null) return GroupInviteResponse.NOT_IN_GROUP;
		PlayerSocialData targetSocialData = getSocialData(targetName);
		if (targetSocialData == null) return GroupInviteResponse.TARGET_USER_NOT_FOUND;
		if (targetSocialData.getGroupToken() != null) return GroupInviteResponse.TARGET_USER_ALREADY_IN_GROUP;
		if (targetSocialData.getStatus() == PlayerStatus.OFFLINE) return GroupInviteResponse.TARGET_USER_OFFLINE;
		if (targetSocialData.hasGroupInvite(playerSocialData.getGroupToken())) return GroupInviteResponse.ALREADY_INVITED;
		if (!targetSocialData.hasFriend(playerSocialData.getName())) return GroupInviteResponse.NOT_FRIENDS;
		
		GroupInvite newInvite = new GroupInvite();
		newInvite.setFromName(playerSocialData.getName());
		newInvite.setGroupToken(playerSocialData.getGroupToken());
		newInvite.setTimestamp(System.currentTimeMillis());
		targetSocialData.getGroupInvites().add(newInvite);
		return GroupInviteResponse.SUCCESS;
	}

	@Override
	public GroupInviteRespondResponse respondToGroupInvite(String token, String groupToken, boolean accept) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return GroupInviteRespondResponse.USER_NOT_FOUND;
		if (playerSocialData.getGroupToken() != null) return GroupInviteRespondResponse.ALREADY_IN_GROUP;
		GroupInvite invite = playerSocialData.getGroupInvite(groupToken);
		if (invite == null) return GroupInviteRespondResponse.NOT_INVITED;
		Group group = groups.get(groupToken);
		if (group == null) return GroupInviteRespondResponse.GROUP_NOT_FOUND;
		playerSocialData.getGroupInvites().remove(invite);
		if (accept) {
			group.getMembers().add(playerSocialData.getName());
			playerSocialData.setGroupToken(groupToken);
		}
		return GroupInviteRespondResponse.SUCCESS;
	}

	@Override
	public GroupLeaveResponse leaveGroup(String token) {
		PlayerSocialData playerSocialData = getPlayerSocialData(token);
		if (playerSocialData == null) return GroupLeaveResponse.USER_NOT_FOUND;
		if (playerSocialData.getGroupToken() == null) return GroupLeaveResponse.NOT_IN_GROUP;
		 Group group = groups.get(playerSocialData.getGroupToken());
		 if (group == null) return GroupLeaveResponse.GROUP_NOT_FOUND;
		 group.getMembers().remove(playerSocialData.getName());
		 if (group.getMembers().isEmpty()) {
			 groups.remove(playerSocialData.getGroupToken());
		 }
		 playerSocialData.setGroupToken(null);
		return GroupLeaveResponse.SUCCESS;
	}
	
	private PlayerSocialData getSocialData(String username) {
		PlayerSocialData socialData = null;
		for (PlayerSocialData currentData : socialDatas.values()) {
			if (currentData.getName().equals(username)) {
				socialData = currentData;
				if (System.currentTimeMillis() >= socialData.getLastRefreshTimestamp() + (1000 * 60)) {
					socialData.setStatus(PlayerStatus.OFFLINE);
				}
				break;
			}
		}
		return socialData;
	}

}
