package de.instinct.social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.API;
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
import de.instinct.base.controller.BaseServiceController;
import de.instinct.social.service.SocialService;

@RestController
@RequestMapping("/social")
public class SocialController extends BaseServiceController {
	
	private final SocialService socialService;
	
	@Autowired
	public SocialController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version, SocialService socialService) {
	    super("social", serverPort, version);
	    this.socialService = socialService;
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<UserCreationResponse> init(@PathVariable String token) {
		return ResponseEntity.ok(socialService.init(token));
	}
	
	@GetMapping("/pushdata/{token}")
	public ResponseEntity<SocialPushData> pushdata(@PathVariable String token) {
		return ResponseEntity.ok(socialService.getPushData(token));
	}
	
	@GetMapping("/playerdata/{token}")
	public ResponseEntity<PlayerSocialData> playerdata(@PathVariable String token) {
		return ResponseEntity.ok(socialService.getPlayerSocialData(token));
	}
	
	@GetMapping("/group/{token}")
	public ResponseEntity<Group> group(@PathVariable String token) {
		return ResponseEntity.ok(socialService.getGroup(token));
	}
	
	@GetMapping("/friendsend/{token}/{targetName}")
	public ResponseEntity<FriendRequestSendResponse> friendsend(@PathVariable String token, @PathVariable String targetName) {
		return ResponseEntity.ok(socialService.sendFriendRequest(token, targetName));
	}
	
	@GetMapping("/friendrespond/{token}/{requesterName}/{accept}")
	public ResponseEntity<FriendRequestRespondResponse> friendrespond(@PathVariable String token, @PathVariable String requesterName, @PathVariable boolean accept) {
		return ResponseEntity.ok(socialService.respondToFriendRequest(token, requesterName, accept));
	}
	
	@GetMapping("/frienddelete/{token}/{friendName}")
	public ResponseEntity<FriendDeleteResponse> frienddelete(@PathVariable String token, @PathVariable String friendName) {
		return ResponseEntity.ok(socialService.deleteFriend(token, friendName));
	}
	
	@GetMapping("/groupcreate/{token}")
	public ResponseEntity<GroupCreateResponse> groupcreate(@PathVariable String token) {
		return ResponseEntity.ok(socialService.createGroup(token));
	}

	@GetMapping("/groupinvite/{token}/{targetName}")
	public ResponseEntity<GroupInviteResponse> groupinvite(@PathVariable String token, @PathVariable String targetName) {
		return ResponseEntity.ok(socialService.inviteToGroup(token, targetName));
	}
	
	@GetMapping("/groupinviterespond/{token}/{groupToken}/{accept}")
	public ResponseEntity<GroupInviteRespondResponse> groupinviterespond(@PathVariable String token, @PathVariable String groupToken, @PathVariable boolean accept) {
		return ResponseEntity.ok(socialService.respondToGroupInvite(token, groupToken, accept));
	}
	
	@GetMapping("/groupleave/{token}")
	public ResponseEntity<GroupLeaveResponse> groupleave(@PathVariable String token) {
		return ResponseEntity.ok(socialService.leaveGroup(token));
	}
	
}
