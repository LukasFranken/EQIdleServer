package de.instinct.matchmaker.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.matchmaker.controller.dto.CallbackCode;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponseCode;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponseCode;
import de.instinct.matchmaker.service.MatchmakingMapper;
import de.instinct.matchmaker.service.MatchmakingService;
import de.instinct.matchmaker.service.model.GameType;
import de.instinct.matchmaker.service.model.GameserverInfo;
import de.instinct.matchmaker.service.model.Lobby;
import de.instinct.matchmaker.service.model.enums.GameserverStatus;
import de.instinct.matchmaker.service.model.enums.VersusMode;

@Service
public class MatchmakingServiceImpl implements MatchmakingService {
	
	private List<Lobby> lobbies;
	private List<Lobby> disposedLobbies;
	private final MatchmakingMapper mapper;
	
	public MatchmakingServiceImpl() {
		lobbies = new ArrayList<>();
		disposedLobbies = new ArrayList<>();
		mapper = new MatchmakingMapperImpl();
	}
	
	@Override
	public MatchmakingRegistrationResponse register(String playerAuthToken, MatchmakingRegistrationRequest request) {
		GameType gameType = mapper.map(request);
		if (alreadyInLobby(playerAuthToken)) return MatchmakingRegistrationResponse.builder()
				.code(MatchmakingRegistrationResponseCode.ALREADY_IN_LOBBY)
				.build();
		//TODO disabled gametype check
		//TODO invalid gametype check
		//TODO invalid auth token check
		Lobby existingLobby = getValidLobby(gameType);
		if (existingLobby == null) {
			existingLobby = createLobby(gameType);
			lobbies.add(existingLobby);
		}
		existingLobby.getPlayerUUIDs().add(playerAuthToken);
		return MatchmakingRegistrationResponse.builder()
				.code(MatchmakingRegistrationResponseCode.SUCCESS)
				.lobbyUUID(existingLobby.getLobbyUUID())
				.build();
	}
	
	@Override
	public MatchmakingStatusResponse getStatus(String lobbyToken) {
		Lobby existingLobby = getLobby(lobbyToken);
		if (existingLobby == null) {
			Lobby disposedLobby = getDisposedLobby(lobbyToken);
			if (disposedLobby == null) return MatchmakingStatusResponse.builder()
					.code(MatchmakingStatusResponseCode.LOBBY_DOESNT_EXIST)
					.build();
			
			return MatchmakingStatusResponse.builder()
					.code(MatchmakingStatusResponseCode.LOBBY_DISPOSED)
					.build();
		}
		
		int foundPlayers = existingLobby.getPlayerUUIDs().size();
		int requiredPlayers = calculateRequiredPlayers(existingLobby.getType());
		
		if (foundPlayers == requiredPlayers && existingLobby.getGameserverInfo().getStatus() == GameserverStatus.NOT_CREATED) {
			existingLobby.getGameserverInfo().setStatus(GameserverStatus.IN_CREATION);
			//TODO init gameserver
		}
		
		MatchmakingStatusResponseCode responseCode = MatchmakingStatusResponseCode.MATCHING;
		MatchmakingStatusResponse response =  MatchmakingStatusResponse.builder()
				.foundPlayers(foundPlayers)
				.requiredPlayers(requiredPlayers)
				.code(responseCode)
				.build();
		
		return mapper.mapGameserverInfo(response, existingLobby.getGameserverInfo());
	}
	
	@Override
	public void callback(String lobbyToken, CallbackCode code) {
		Lobby existingLobby = getLobby(lobbyToken);
		if (existingLobby == null) return;
		
		switch (code) {
		case READY:
			existingLobby.getGameserverInfo().setStatus(GameserverStatus.READY);
			existingLobby.getGameserverInfo().setAddress("eqgame.dev");
			existingLobby.getGameserverInfo().setPort(9005);
			break;
		case ERROR:
			existingLobby.getGameserverInfo().setStatus(GameserverStatus.ERROR);
			break;
		case FULL:
			//TODO recreation fallback
			existingLobby.getGameserverInfo().setStatus(GameserverStatus.ERROR);
			break;
		}
	}
	
	@Override
	public void dispose(String lobbyToken) {
		Lobby disposeLobby = getLobby(lobbyToken);
		if (disposeLobby == null) return;
		
		disposeLobby.getGameserverInfo().setStatus(GameserverStatus.CLOSED);
		lobbies.remove(disposeLobby);
		disposedLobbies.add(disposeLobby);
	}

	private int calculateRequiredPlayers(GameType type) {
		return type.getFactionMode().teamPlayerCount 
				* (type.getVersusMode() == VersusMode.PVP ? 2 : 1);
	}

	public Lobby createLobby(GameType gameType) {
		return Lobby.builder()
				.lobbyUUID(UUID.randomUUID().toString())
				.type(gameType)
				.playerUUIDs(new ArrayList<>())
				.gameserverInfo(GameserverInfo.builder()
						.status(GameserverStatus.NOT_CREATED)
						.build())
				.build();
	}

	public Lobby getValidLobby(GameType gameType) {
		return lobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getType().matches(gameType) && lobby.getGameserverInfo().getStatus() == GameserverStatus.NOT_CREATED)
				.orElse(null);
	}
	
	public Lobby getLobby(String lobbyToken) {
		return lobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getLobbyUUID().contentEquals(lobbyToken))
				.orElse(null);
	}
	
	public Lobby getDisposedLobby(String lobbyToken) {
		return disposedLobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getLobbyUUID().contentEquals(lobbyToken))
				.orElse(null);
	}

	public boolean alreadyInLobby(String playerAuthToken) {
		return lobbies.stream()
				.anyMatch(lobby -> lobby.getPlayerUUIDs().contains(playerAuthToken));
	}

}
