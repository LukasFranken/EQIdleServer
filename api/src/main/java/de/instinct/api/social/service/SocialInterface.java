package de.instinct.api.social.service;

import de.instinct.api.social.dto.FriendDeleteResponse;
import de.instinct.api.social.dto.FriendRequestRespondResponse;
import de.instinct.api.social.dto.FriendRequestSendResponse;
import de.instinct.api.social.dto.Group;
import de.instinct.api.social.dto.GroupCreateResponse;
import de.instinct.api.social.dto.GroupInviteRespondResponse;
import de.instinct.api.social.dto.GroupInviteResponse;
import de.instinct.api.social.dto.GroupLeaveResponse;
import de.instinct.api.social.dto.PlayerSocialData;
import de.instinct.api.social.dto.SocialPushData;
import de.instinct.api.social.dto.UserCreationResponse;

public interface SocialInterface {
	
	UserCreationResponse init(String token);
	
	SocialPushData pushData(String token);
	
	PlayerSocialData playerdata(String token);
	
	Group getGroup(String token);
	
	FriendRequestSendResponse sendFriendRequest(String token, String targetName);
	
	FriendRequestRespondResponse respondToFriendRequest(String token, String requesterName, boolean accept);
	
	FriendDeleteResponse deleteFriend(String token, String friendName);
	
	GroupCreateResponse createGroup(String token);
	
	GroupInviteResponse inviteToGroup(String token, String targetName);
	
	GroupInviteRespondResponse respondToGroupInvite(String token, String groupToken, boolean accept);
	
	GroupLeaveResponse leaveGroup(String token);

}
