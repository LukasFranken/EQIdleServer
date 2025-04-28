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
import de.instinct.api.game.dto.UserData;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusCode;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponseCode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.Invite;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.matchmaking.service.MatchmakingMapper;
import de.instinct.matchmaking.service.MatchmakingService;
import de.instinct.matchmaking.service.model.GameserverInfo;
import de.instinct.matchmaking.service.model.GameserverStatus;
import de.instinct.matchmaking.service.model.Lobby;

@Service
public class MatchmakingServiceImpl implements MatchmakingService {
	
	private Map<String, LobbyStatusCode> lobbyStati;
	private List<Lobby> lobbies;
	private List<Lobby> searchingLobbies;
	private List<Lobby> playingLobbies;
	private List<Lobby> disposedLobbies;
	private List<Invite> invites;
	private final MatchmakingMapper mapper;
	
	public MatchmakingServiceImpl() {
		lobbyStati = new HashMap<>();
		lobbies = new ArrayList<>();
		searchingLobbies = new ArrayList<>();
		disposedLobbies = new ArrayList<>();
		invites = new ArrayList<>();
		mapper = new MatchmakingMapperImpl();
	}
	
	@Override
	public LobbyCreationResponse createLobby(String authToken) {
		Lobby lobby = getPlayerLobby(authToken);
		if (lobby == null) {
			lobby = createLobby();
			lobby.getUserUUIDs().add(authToken);
			lobbies.add(lobby);
			lobbyStati.put(lobby.getLobbyUUID(), LobbyStatusCode.IDLE);
		}
		return LobbyCreationResponse.builder()
				.lobbyUUID(lobby.getLobbyUUID())
				.build();
	}
	
	@Override
	public LobbyTypeSetResponse setType(String authToken, String lobbyUUID, GameType selectedGameType) {
		Lobby lobby = getLobby(lobbyUUID, LobbyStatusCode.IDLE);
		if (lobby == null) return LobbyTypeSetResponse.LOBBY_DOESNT_EXIST;
		lobby.setType(selectedGameType);
		return LobbyTypeSetResponse.SUCCESS;
	}
	
	@Override
	public InviteResponse invite(String authToken, String username) {
		String usertoken = API.meta().token("username");
		if (usertoken == null) return InviteResponse.USERNAME_DOESNT_EXIST;
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
	public void respond(String authToken, String lobbyUUID, boolean accepted) {
		Invite invite = getInviteByLobbyUUID(lobbyUUID);
		Lobby lobby = getLobby(lobbyUUID, LobbyStatusCode.IDLE);
		if (invite == null) return;
		if(accepted) {
			lobby.getUserUUIDs().add(authToken);
		}
		invites.remove(invite);
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
	public MatchmakingRegistrationResponseCode start(String playerAuthToken, MatchmakingRegistrationRequest request) {
		if (isSearching(request.getLobbyUUID())) return MatchmakingRegistrationResponseCode.ALREADY_SEARCHING;
		Lobby lobby = getLobby(request.getLobbyUUID(), LobbyStatusCode.IDLE);
		if (lobby == null) return MatchmakingRegistrationResponseCode.INVALID_LOBBY_TOKEN;
		if (lobby.getType() == null) return MatchmakingRegistrationResponseCode.NO_GAME_TYPE;
		searchingLobbies.add(lobby);
		lobbies.remove(lobby);
		lobbyStati.put(lobby.getLobbyUUID(), LobbyStatusCode.MATCHING);
		
		if (calculateTotalPlayersFound(lobby.getType(), searchingLobbies) >= calculateRequiredPlayers(lobby.getType())) {
			match(lobby.getType());
		}
		
		return MatchmakingRegistrationResponseCode.SUCCESS;
	}
	
	private boolean isSearching(String lobbyUUID) {
		return searchingLobbies.stream()
				.anyMatch(lobby -> lobby.getLobbyUUID().contentEquals(lobbyUUID));
	}
	
	@Override
	public LobbyStatusResponse getStatus(String lobbyToken) {
		LobbyStatusCode code = lobbyStati.get(lobbyToken);
		if (code == null) return LobbyStatusResponse.builder().code(LobbyStatusCode.DOESNT_EXIST).build();
		
		Lobby lobby = getLobby(lobbyToken, code);
		if (lobby == null) return LobbyStatusResponse.builder().code(LobbyStatusCode.DOESNT_EXIST).build();
		
		return LobbyStatusResponse.builder()
				.code(code)
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
		Lobby existingLobby = getLobby(lobbyToken, LobbyStatusCode.MATCHING);
		if (existingLobby == null) return MatchmakingStatusResponse.builder()
				.code(MatchmakingStatusResponseCode.LOBBY_DOESNT_EXIST)
				.build();
		
		
		int foundPlayers = calculateTotalPlayersFound(existingLobby.getType(), searchingLobbies);
		int requiredPlayers = calculateRequiredPlayers(existingLobby.getType());
		
		MatchmakingStatusResponseCode responseCode = MatchmakingStatusResponseCode.MATCHING;
		MatchmakingStatusResponse response =  MatchmakingStatusResponse.builder()
				.foundPlayers(foundPlayers)
				.requiredPlayers(requiredPlayers)
				.code(responseCode)
				.build();
		
		return mapper.mapGameserverInfo(response, existingLobby.getGameserverInfo());
	}
	
	private int calculateTotalPlayersFound(GameType type, List<Lobby> lobbies) {
		int count = 0;
		for (Lobby lobby : lobbies) {
			if (lobby.getType().matches(type)) {
				count += lobby.getUserUUIDs().size();
			}
		}
		return count;
	}
	
	private void match(GameType type) {
	    int requiredPlayers = calculateRequiredPlayers(type);

	    List<Lobby> candidates = searchingLobbies.stream()
	        .filter(l -> l.getType().matches(type))
	        .collect(Collectors.toList());

	    List<Lobby> matched = new ArrayList<>();
	    if (findCombination(candidates, 0, requiredPlayers, matched)) {
	    	searchingLobbies.removeAll(matched);
	    	playingLobbies.addAll(matched);
	        List<UserData> usersWithTeams = assignTeams(matched, type);
	        createGameSession(matched, usersWithTeams);
	    }
	}

	private List<UserData> assignTeams(List<Lobby> matchedLobbies, GameType type) {
	    List<UserData> result = new ArrayList<>();

	    if (type.getVersusMode() == VersusMode.AI) {
	        matchedLobbies.forEach(lobby ->
	            lobby.getUserUUIDs().forEach(uuid ->
	                result.add(new UserData(uuid, 1))
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
	            result.add(new UserData(uuid, teamId))
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
	
	private void createGameSession(List<Lobby> matchedLobbies, List<UserData> userData) {
		String gameSessionToken = API.game().create(GameSessionInitializationRequest.builder()
				.type(matchedLobbies.get(0).getType())
				.users(userData)
				.build());
		
		for (Lobby lobby : matchedLobbies) {
			lobby.getGameserverInfo().setStatus(GameserverStatus.IN_CREATION);
			lobby.getGameserverInfo().setGameSessionUUID(gameSessionToken);
		}
	}

	@Override
	public void callback(String gameSessionToken, CallbackCode code) {
		for (Lobby lobby : playingLobbies) {
			if (lobby.getGameserverInfo().getGameSessionUUID().contentEquals(gameSessionToken)) {
				switch (code) {
				case READY:
					lobby.getGameserverInfo().setStatus(GameserverStatus.READY);
					lobby.getGameserverInfo().setAddress("eqgame.dev");
					lobby.getGameserverInfo().setPort(9005);
					lobbyStati.put(lobby.getLobbyUUID(), LobbyStatusCode.IN_GAME);
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
	public void finish(String gameSessionToken) {
		for (Lobby lobby : playingLobbies) {
			if (lobby.getGameserverInfo().getGameSessionUUID().contentEquals(gameSessionToken)) {
				lobby.getGameserverInfo().setStatus(GameserverStatus.NOT_CREATED);
				lobby.getGameserverInfo().setAddress(null);
				lobby.getGameserverInfo().setPort(0);
				lobby.getGameserverInfo().setGameSessionUUID(null);
				playingLobbies.remove(lobby);
				lobbies.add(lobby);
				lobbyStati.put(lobby.getLobbyUUID(), LobbyStatusCode.IDLE);
			}
		}
	}
	
	@Override
	public void dispose(String lobbyToken) {
		Lobby disposeLobby = getLobby(lobbyToken, lobbyStati.get(lobbyToken));
		if (disposeLobby == null) return;
		
		lobbies.remove(disposeLobby);
		disposedLobbies.add(disposeLobby);
		lobbyStati.put(lobbyToken, LobbyStatusCode.DISPOSED);
	}

	private int calculateRequiredPlayers(GameType type) {
		return type.getFactionMode().teamPlayerCount 
				* (type.getVersusMode() == VersusMode.PVP ? 2 : 1);
	}

	private Lobby createLobby() {
		return Lobby.builder()
				.lobbyUUID(UUID.randomUUID().toString())
				.userUUIDs(new ArrayList<>())
				.gameserverInfo(GameserverInfo.builder()
						.status(GameserverStatus.NOT_CREATED)
						.build())
				.invites(new ArrayList<>())
				.build();
	}
	
	private Lobby getLobby(String lobbyToken, LobbyStatusCode code) {
		return getLobbies(code).stream()
				.findFirst()
				.filter(lobby -> lobby.getLobbyUUID().contentEquals(lobbyToken))
				.orElse(null);
	}

	private List<Lobby> getLobbies(LobbyStatusCode code) {
		switch (code) {
			case IDLE: return lobbies;
			case DISPOSED: return disposedLobbies;
			case MATCHING: return searchingLobbies;
			case IN_GAME: return playingLobbies;
			default: return new ArrayList<>();
		}
	}

	private Lobby getPlayerLobby(String playerAuthToken) {
		return lobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getUserUUIDs().contains(playerAuthToken))
				.orElse(null);
	}

}
