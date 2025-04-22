package de.instinct.matchmaker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.model.FactionMode;
import de.instinct.api.matchmaking.model.GameMode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.matchmaking.service.impl.MatchmakingServiceImpl;

class MatchmakingServiceImplTest {
	
	MatchmakingServiceImpl cut;

	@BeforeEach
	void setUp() throws Exception {
		cut = new MatchmakingServiceImpl();
	}

	@Test
	void canCreateNewLobbyIfNotPresent() {
		String testUUID = "testuuid";
		MatchmakingRegistrationRequest testType = MatchmakingRegistrationRequest.builder()
				.gameType(GameType.builder()
						.versusMode(VersusMode.AI)
						.gameMode(GameMode.CONQUEST)
						.factionMode(FactionMode.ONE_VS_ONE)
						.build())
				.build();
		assertEquals(MatchmakingRegistrationResponseCode.SUCCESS, cut.register(testUUID, testType).getCode());
	}
	
	@Test
	void returnsUUIDOfExistingMatchingLobby() {
		String testUUID = "testuuid";
		MatchmakingRegistrationRequest testType = MatchmakingRegistrationRequest.builder()
				.gameType(GameType.builder()
						.versusMode(VersusMode.AI)
						.gameMode(GameMode.CONQUEST)
						.factionMode(FactionMode.ONE_VS_ONE)
						.build())
				.build();
		MatchmakingRegistrationResponse response = cut.register(testUUID, testType);
		assertEquals(response.getLobbyUUID(), cut.register("anothertestuuid", testType).getLobbyUUID());
	}
	
	@Test
	void returnsExistsCodeIfPlayerTokenAlreadyExistsInALobby() {
		String testUUID = "testuuid";
		MatchmakingRegistrationRequest testType = MatchmakingRegistrationRequest.builder()
				.gameType(GameType.builder()
						.versusMode(VersusMode.AI)
						.gameMode(GameMode.CONQUEST)
						.factionMode(FactionMode.ONE_VS_ONE)
						.build())
				.build();
		cut.register(testUUID, testType);
		assertEquals(MatchmakingRegistrationResponseCode.ALREADY_IN_LOBBY, cut.register(testUUID, testType).getCode());
	}

}
