package de.instinct.api.social.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
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
import de.instinct.api.social.service.SocialInterface;

public class Social extends BaseService implements SocialInterface {

	public Social() {
		super("social");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public UserCreationResponse init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UserCreationResponse.class);
	}

	@Override
	public SocialPushData pushData(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("pushdata")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, SocialPushData.class);
	}

	@Override
	public PlayerSocialData playerdata(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("playerdata")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerSocialData.class);
	}

	@Override
	public Group getGroup(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("group")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, Group.class);
	}

	@Override
	public FriendRequestSendResponse sendFriendRequest(String token, String targetName) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("friendsend")
				.pathVariable(token + "/" + targetName)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, FriendRequestSendResponse.class);
	}

	@Override
	public FriendRequestRespondResponse respondToFriendRequest(String token, String requesterName, boolean accept) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("friendrespond")
				.pathVariable(token + "/" + requesterName + "/" + accept)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, FriendRequestRespondResponse.class);
	}

	@Override
	public FriendDeleteResponse deleteFriend(String token, String friendName) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("frienddelete")
				.pathVariable(token + "/" + friendName)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, FriendDeleteResponse.class);
	}

	@Override
	public GroupCreateResponse createGroup(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("groupcreate")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, GroupCreateResponse.class);
	}

	@Override
	public GroupInviteResponse inviteToGroup(String token, String targetName) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("groupinvite")
				.pathVariable(token + "/" + targetName)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, GroupInviteResponse.class);
	}

	@Override
	public GroupInviteRespondResponse respondToGroupInvite(String token, String groupToken, boolean accept) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("groupinviterespond")
				.pathVariable(token + "/" + groupToken + "/" + accept)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, GroupInviteRespondResponse.class);
	}

	@Override
	public GroupLeaveResponse leaveGroup(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("groupleave")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, GroupLeaveResponse.class);
	}

}
