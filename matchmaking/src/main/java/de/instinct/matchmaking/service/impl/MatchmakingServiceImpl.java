package de.instinct.matchmaking.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.UserTeamData;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.FinishGameData;
import de.instinct.api.matchmaking.dto.GameResult;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyLeaveResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusCode;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStopResponseCode;
import de.instinct.api.matchmaking.dto.PlayerReward;
import de.instinct.api.matchmaking.model.GameMode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.Invite;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.GalaxyData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarsystemData;
import de.instinct.matchmaking.service.MatchmakingMapper;
import de.instinct.matchmaking.service.MatchmakingService;
import de.instinct.matchmaking.service.model.GameserverInfo;
import de.instinct.matchmaking.service.model.GameserverStatus;
import de.instinct.matchmaking.service.model.Lobby;

@Service
public class MatchmakingServiceImpl implements MatchmakingService {
	
	private List<Lobby> lobbies;
	private List<Invite> invites;
	private Map<String, GameResult> postGameResults;
	private final MatchmakingMapper mapper;
	
	public MatchmakingServiceImpl() {
		lobbies = new ArrayList<>();
		invites = new ArrayList<>();
		postGameResults = new HashMap<>();
		mapper = new MatchmakingMapperImpl();
	}
	
	@Override
	public LobbyCreationResponse createLobby(String authToken) {
		Lobby lobby = getPlayerLobby(authToken);
		if (lobby == null) {
			lobby = createLobby();
			lobby.getUserUUIDs().add(authToken);
			lobbies.add(lobby);
		}
		return LobbyCreationResponse.builder()
				.lobbyUUID(lobby.getLobbyUUID())
				.build();
	}
	
	@Override
	public LobbyLeaveResponse leaveLobby(String authToken) {
		Lobby lobby = getPlayerLobby(authToken);
		if (lobby == null) return LobbyLeaveResponse.NOT_IN_LOBBY;
		lobby.getUserUUIDs().remove(authToken);
		return LobbyLeaveResponse.SUCCESS;
	}
	
	@Override
	public String getUserLobby(String authToken) {
		Lobby userLobby = getPlayerLobby(authToken);
		if (userLobby == null) return null;
		return userLobby.getLobbyUUID();
	}
	
	@Override
	public LobbyTypeSetResponse setType(String authToken, String lobbyUUID, GameType selectedGameType) {
		Lobby lobby = getLobby(lobbyUUID);
		if (lobby == null) return LobbyTypeSetResponse.LOBBY_DOESNT_EXIST;
		lobby.setType(selectedGameType);
		return LobbyTypeSetResponse.SUCCESS;
	}
	
	@Override
	public InviteResponse invite(String authToken, String username) {
		String usertoken = API.meta().token(username);
		if (usertoken.contentEquals("")) return InviteResponse.USERNAME_DOESNT_EXIST;
		Lobby lobby = getPlayerLobby(authToken);
		if (alreadyInvited(lobby, usertoken)) return InviteResponse.ALREADY_INVITED;
		invites.add(Invite.builder()
				.lobbyUUID(lobby.getLobbyUUID())
				.fromUsername(API.meta().profile(lobby.getUserUUIDs().get(0)).getUsername())
				.toUsername(username)
				.toUUID(usertoken)
				.selectedGameType(lobby.getType())
				.build());
		return InviteResponse.INVITED;
	}

	private boolean alreadyInvited(Lobby lobby, String usertoken) {
		return invites.stream()
				.anyMatch(invite -> invite.getLobbyUUID().contentEquals(lobby.getLobbyUUID())
								 && invite.getToUUID().contentEquals(usertoken));
	}

	@Override
	public String respond(String authToken, String lobbyUUID, boolean accepted) {
		Invite invite = getInviteByLobbyUUID(lobbyUUID);
		Lobby lobby = getLobby(lobbyUUID);
		if (invite == null) return null;
		if (accepted) {
			lobby.getUserUUIDs().add(authToken);
		}
		invites.remove(invite);
		return lobby.getLobbyUUID();
	}

	private Invite getInviteByLobbyUUID(String lobbyUUID) {
		return invites.stream()
				.findFirst()
				.filter(invite -> invite.getLobbyUUID().contentEquals(lobbyUUID))
				.orElse(null);
	}

	@Override
	public InvitesStatusResponse getInvites(String usertoken) {
		return InvitesStatusResponse.builder()
				.invites(invites.stream()
					    .filter(invite -> usertoken.equals(invite.getToUUID()))
					    .collect(Collectors.toList()))
				.build();
	}
	
	@Override
	public MatchmakingRegistrationResponseCode start(String playerAuthToken, String lobbyUUID) {
		Lobby lobby = getLobby(lobbyUUID);
		if (lobby == null) return MatchmakingRegistrationResponseCode.INVALID_LOBBY_TOKEN;
		if (lobby.getCode() == LobbyStatusCode.MATCHING) return MatchmakingRegistrationResponseCode.ALREADY_SEARCHING;
		if (lobby.getType() == null) return MatchmakingRegistrationResponseCode.NO_GAME_TYPE;
		
		lobby.setCode(LobbyStatusCode.MATCHING);
		
		if (calculateTotalPlayersFound(lobby.getType()) >= calculateRequiredPlayers(lobby.getType())) {
			match(lobby.getType());
		}
		
		return MatchmakingRegistrationResponseCode.SUCCESS;
	}
	
	@Override
	public MatchmakingStopResponseCode stop(String authToken, String lobbyUUID) {
		Lobby lobby = getLobby(lobbyUUID);
		if (lobby == null) return MatchmakingStopResponseCode.LOBBY_DOESNT_EXIST;
		if (lobby.getCode() != LobbyStatusCode.MATCHING) return MatchmakingStopResponseCode.NOT_MATCHING;
		
		lobby.setCode(LobbyStatusCode.IDLE);
		return MatchmakingStopResponseCode.SUCCESS;
	}
	
	@Override
	public LobbyStatusResponse getStatus(String lobbyToken) {
		Lobby lobby = getLobby(lobbyToken);
		if (lobby == null) return LobbyStatusResponse.builder().code(LobbyStatusCode.DOESNT_EXIST).build();
		
		return LobbyStatusResponse.builder()
				.code(lobby.getCode())
				.type(lobby.getType())
				.userNames(loadUsernames(lobby.getUserUUIDs()))
				.build();
	}

	private List<String> loadUsernames(List<String> userUUIDs) {
		return userUUIDs.stream()
				.map(uuid -> API.meta().profile(uuid).getUsername())
				.collect(Collectors.toList());
	}

	@Override
	public MatchmakingStatusResponse getMatchmakingStatus(String lobbyToken) {
		Lobby lobby = getLobby(lobbyToken);
		if (lobby == null) return MatchmakingStatusResponse.builder()
				.code(MatchmakingStatusResponseCode.LOBBY_DOESNT_EXIST)
				.build();
		if (lobby.getCode() != LobbyStatusCode.MATCHING && lobby.getCode() != LobbyStatusCode.IN_GAME) return MatchmakingStatusResponse.builder()
				.code(MatchmakingStatusResponseCode.NOT_IN_MATCHMAKING)
				.build();
		
		
		int requiredPlayers = calculateRequiredPlayers(lobby.getType());
		int foundPlayers = lobby.getCode() == LobbyStatusCode.MATCHING ? calculateTotalPlayersFound(lobby.getType()) : requiredPlayers;
		
		MatchmakingStatusResponseCode responseCode = MatchmakingStatusResponseCode.MATCHING;
		MatchmakingStatusResponse response =  MatchmakingStatusResponse.builder()
				.foundPlayers(foundPlayers)
				.requiredPlayers(requiredPlayers)
				.code(responseCode)
				.build();
		
		return mapper.mapGameserverInfo(response, lobby.getGameserverInfo());
	}
	
	private int calculateTotalPlayersFound(GameType type) {
		int count = 0;
		for (Lobby lobby : lobbies) {
			if (lobby.getType() != null && lobby.getType().matches(type) && lobby.getCode() == LobbyStatusCode.MATCHING) {
				count += lobby.getUserUUIDs().size();
			}
		}
		return count;
	}
	
	private void match(GameType type) {
	    int requiredPlayers = calculateRequiredPlayers(type);

	    List<Lobby> candidates = lobbies.stream()
	        .filter(l -> l.getCode() == LobbyStatusCode.MATCHING && l.getType().matches(type))
	        .collect(Collectors.toList());

	    List<Lobby> matched = new ArrayList<>();
	    if (findCombination(candidates, 0, requiredPlayers, matched)) {
	        List<UserTeamData> usersWithTeams = assignTeams(matched, type);
	        createGameSession(matched, usersWithTeams);
	    }
	}

	private List<UserTeamData> assignTeams(List<Lobby> matchedLobbies, GameType type) {
	    List<UserTeamData> result = new ArrayList<>();

	    if (type.getVersusMode() == VersusMode.AI) {
	        matchedLobbies.forEach(lobby ->
	            lobby.getUserUUIDs().forEach(uuid ->
	                result.add(new UserTeamData(uuid, 1))
	            )
	        );
	        return result;
	    }

	    int perTeam = type.getFactionMode().teamPlayerCount;
	    List<Lobby> team1Lobbies = new ArrayList<>();
	    
	    findCombination(matchedLobbies, 0, perTeam, team1Lobbies);

	    Set<String> team1Ids = team1Lobbies.stream()
	        .map(Lobby::getLobbyUUID)
	        .collect(Collectors.toSet());

	    for (Lobby lobby : matchedLobbies) {
	        int teamId = team1Ids.contains(lobby.getLobbyUUID()) ? 1 : 2;
	        lobby.getUserUUIDs().forEach(uuid ->
	            result.add(new UserTeamData(uuid, teamId))
	        );
	    }

	    return result;
	}

	private boolean findCombination(List<Lobby> candidates, int start, int remaining, List<Lobby> matched) {
	    if (remaining == 0) {
	        return true;
	    }
	    for (int i = start; i < candidates.size(); i++) {
	        Lobby lobby = candidates.get(i);
	        int sz = lobby.getUserUUIDs().size();
	        if (sz > remaining) {
	            continue;
	        }
	        matched.add(lobby);
	        if (findCombination(candidates, i + 1, remaining - sz, matched)) {
	            return true;
	        }
	        matched.remove(matched.size() - 1);
	    }
	    return false;
	}
	
	private void createGameSession(List<Lobby> matchedLobbies, List<UserTeamData> userData) {
		String gameSessionToken = API.game().create(GameSessionInitializationRequest.builder()
				.type(matchedLobbies.get(0).getType())
				.users(userData)
				.build());
		System.out.println("creating session on map " + "test");
		for (Lobby lobby : matchedLobbies) {
			lobby.getGameserverInfo().setStatus(GameserverStatus.IN_CREATION);
			lobby.getGameserverInfo().setGameSessionUUID(gameSessionToken);
		}
	}

	@Override
	public void callback(String gameSessionToken, CallbackCode code) {
		for (Lobby lobby : lobbies) {
			if (lobby.getGameserverInfo().getGameSessionUUID() != null && lobby.getGameserverInfo().getGameSessionUUID().contentEquals(gameSessionToken)) {
				switch (code) {
				case READY:
					lobby.getGameserverInfo().setStatus(GameserverStatus.READY);
					lobby.getGameserverInfo().setAddress("eqgame.dev");
					lobby.getGameserverInfo().setPort(9005);
					lobby.setCode(LobbyStatusCode.IN_GAME);
					break;
				case ERROR:
					lobby.getGameserverInfo().setStatus(GameserverStatus.ERROR);
					break;
				case FULL:
					lobby.getGameserverInfo().setStatus(GameserverStatus.ERROR);
					break;
				}
			}
		}
	}
	
	@Override
	public void finish(String gameSessionToken, FinishGameData finishGameData) {
		List<PlayerReward> rewards = new ArrayList<>();
		for (Lobby lobby : lobbies) {
			if (lobby.getGameserverInfo().getGameSessionUUID() != null && lobby.getGameserverInfo().getGameSessionUUID().contentEquals(gameSessionToken)) {
				lobby.getGameserverInfo().setStatus(GameserverStatus.NOT_CREATED);
				lobby.getGameserverInfo().setAddress(null);
				lobby.getGameserverInfo().setPort(0);
				lobby.getGameserverInfo().setGameSessionUUID(null);
				lobby.setCode(LobbyStatusCode.IDLE);
				if (lobby.getType().getGameMode() == GameMode.CONQUEST) {
					int galaxyId = Integer.parseInt(lobby.getType().getMap().split("_")[1]);
					int systemId = Integer.parseInt(lobby.getType().getMap().split("_")[2]);
					SectorData sectorData = API.starmap().sector();
					StarsystemData currentSystem = null;
					for (GalaxyData galaxy : sectorData.getGalaxies()) {
						if (galaxy.getId() == galaxyId) {
							for (StarsystemData system : galaxy.getStarsystems()) {
								if (system.getId() == systemId) {
									currentSystem = system;
								}
							}
						}
					}
					if (currentSystem == null) break;
					
					for (String userUUID : lobby.getUserUUIDs()) {
						rewards.add(PlayerReward.builder()
								.uuid(userUUID)
								.experience(currentSystem.getExperience())
								.resources(currentSystem.getResourceRewards())
								.build());
						API.meta().experience(userUUID, currentSystem.getExperience());
						API.meta().addResources(userUUID, ResourceData.builder()
								.resources(currentSystem.getResourceRewards())
								.build());
						API.starmap().complete(CompletionRequest.builder()
								.userUUID(userUUID)
								.galaxyId(galaxyId)
								.systemId(systemId)
								.build());
					}
				}
				if (lobby.getType().getGameMode() == GameMode.KING_OF_THE_HILL) {
					long defaultExp = 200;
					for (String userUUID : lobby.getUserUUIDs()) {
						rewards.add(PlayerReward.builder()
								.uuid(userUUID)
								.experience(defaultExp)
								.resources(new ArrayList<>())
								.build());
						API.meta().experience(userUUID, defaultExp);
					}
				}
			}
		}
		System.out.println("finalized game: " + gameSessionToken);
		postGameResults.put(gameSessionToken, GameResult.builder()
				.playedMS(finishGameData.getPlayedMS())
				.rewards(rewards)
				.build());
	}

	@Override
	public void dispose(String lobbyToken) {
		Lobby disposeLobby = getLobby(lobbyToken);
		if (disposeLobby == null) return;
		disposeLobby.setCode(LobbyStatusCode.DISPOSED);
	}

	private int calculateRequiredPlayers(GameType type) {
		return type.getFactionMode().teamPlayerCount 
				* (type.getVersusMode() == VersusMode.PVP ? 2 : 1);
	}

	private Lobby createLobby() {
		return Lobby.builder()
				.code(LobbyStatusCode.IDLE)
				.lobbyUUID(UUID.randomUUID().toString())
				.userUUIDs(new ArrayList<>())
				.gameserverInfo(GameserverInfo.builder()
						.status(GameserverStatus.NOT_CREATED)
						.build())
				.invites(new ArrayList<>())
				.build();
	}
	
	private Lobby getLobby(String lobbyToken) {
		return lobbies.stream()
				.filter(lobby -> lobby.getLobbyUUID().contentEquals(lobbyToken))
				.findFirst()
				.orElse(null);
	}

	private Lobby getPlayerLobby(String playerAuthToken) {
		return lobbies.stream()
				.filter(lobby -> lobby.getUserUUIDs().contains(playerAuthToken))
				.findFirst()
				.orElse(null);
	}

	@Override
	public PlayerReward result(String authToken, String gamesessionUUID) {
		GameResult postGameResult = postGameResults.get(gamesessionUUID);
		if (postGameResult == null) return null;
		for (PlayerReward playerReward : postGameResult.getRewards()) {
			if (playerReward.getUuid().contentEquals(authToken)) {
				return playerReward;
			}
		}
		return null;
	}

}
