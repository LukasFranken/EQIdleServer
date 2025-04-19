package de.instinct.matchmaker.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponseCode;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponseCode;
import de.instinct.matchmaker.service.MatchmakingMapper;
import de.instinct.matchmaker.service.MatchmakingService;
import de.instinct.matchmaker.service.model.GameType;
import de.instinct.matchmaker.service.model.Lobby;
import de.instinct.matchmaker.service.model.enums.VersusMode;

@Service
public class MatchmakingServiceImpl implements MatchmakingService {
	
	private List<Lobby> lobbies;
	private final MatchmakingMapper mapper;
	
	public MatchmakingServiceImpl() {
		lobbies = new ArrayList<>();
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
		Lobby existingLobby = getLobby(gameType);
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
		if (existingLobby == null) return MatchmakingStatusResponse.builder()
				.code(MatchmakingStatusResponseCode.LOBBY_DOESNT_EXIST)
				.build();
		
		MatchmakingStatusResponse response =  MatchmakingStatusResponse.builder()
				.foundPlayers(existingLobby.getPlayerUUIDs().size())
				.requiredPlayers(calculateRequiredPlayers(existingLobby.getType()))
				.build();
		
		if (response.getFoundPlayers() == response.getRequiredPlayers() && response.getCode() == MatchmakingStatusResponseCode.SUCCESS) {
			response.setCode(MatchmakingStatusResponseCode.IN_CREATION);
			//init gameserver and create callback request in matchmakign server controller
			//create server base interceptor that blocks all non-localhost ips
		}
		return response;
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
				.build();
	}

	public Lobby getLobby(GameType gameType) {
		return lobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getType().matches(gameType))
				.orElse(null);
	}
	
	public Lobby getLobby(String lobbyToken) {
		return lobbies.stream()
				.findFirst()
				.filter(lobby -> lobby.getLobbyUUID().contentEquals(lobbyToken))
				.orElse(null);
	}

	public boolean alreadyInLobby(String playerAuthToken) {
		return lobbies.stream()
				.anyMatch(lobby -> lobby.getPlayerUUIDs().contains(playerAuthToken));
	}

}
