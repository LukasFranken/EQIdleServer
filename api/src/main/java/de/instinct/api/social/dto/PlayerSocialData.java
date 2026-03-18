package de.instinct.api.social.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerSocialData {
	
	private PlayerStatus status;
	private long lastRefreshTimestamp;
	private List<Friend> friends;
	private List<FriendRequest> friendRequests;
	private List<GroupInvite> groupInvites;
	private String groupToken;
	
	public boolean hasFriend(String friendName) {
		return friends.stream().anyMatch(f -> f.getName().equals(friendName));
	}
	
	public Friend getFriend(String friendName) {
		return friends.stream().filter(f -> f.getName().equals(friendName)).findFirst().orElse(null);
	}
	
	public boolean hasFriendRequest(String requesterName) {
		return friendRequests.stream().anyMatch(fr -> fr.getFromName().equals(requesterName));
	}
	
	public FriendRequest getFriendRequest(String requesterName) {
		return friendRequests.stream().filter(fr -> fr.getFromName().equals(requesterName)).findFirst().orElse(null);
	}

	public boolean hasGroupInvite(String groupToken) {
		return groupInvites.stream().anyMatch(gi -> gi.getGroupToken().equals(groupToken));
	}
	
	public GroupInvite getGroupInvite(String groupToken) {
		return groupInvites.stream().filter(gi -> gi.getGroupToken().equals(groupToken)).findFirst().orElse(null);
	}

}
