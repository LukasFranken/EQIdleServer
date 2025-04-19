package de.instinct.matchmaker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponseCode;
import de.instinct.matchmaker.service.impl.MatchmakingServiceImpl;
import de.instinct.matchmaker.service.model.enums.FactionMode;
import de.instinct.matchmaker.service.model.enums.GameMode;
import de.instinct.matchmaker.service.model.enums.VersusMode;

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
				.versusMode(VersusMode.AI)
				.gameMode(GameMode.CONQUEST)
				.factionMode(FactionMode.ONE_VS_ONE)
				.build();
		assertEquals(MatchmakingRegistrationResponseCode.SUCCESS, cut.register(testUUID, testType).getCode());
	}
	
	@Test
	void returnsUUIDOfExistingMatchingLobby() {
		String testUUID = "testuuid";
		MatchmakingRegistrationRequest testType = MatchmakingRegistrationRequest.builder()
				.versusMode(VersusMode.AI)
				.gameMode(GameMode.CONQUEST)
				.factionMode(FactionMode.ONE_VS_ONE)
				.build();
		MatchmakingRegistrationResponse response = cut.register(testUUID, testType);
		assertEquals(response.getLobbyUUID(), cut.register("anothertestuuid", testType).getLobbyUUID());
	}
	
	@Test
	void returnsExistsCodeIfPlayerTokenAlreadyExistsInALobby() {
		String testUUID = "testuuid";
		MatchmakingRegistrationRequest testType = MatchmakingRegistrationRequest.builder()
				.versusMode(VersusMode.AI)
				.gameMode(GameMode.CONQUEST)
				.factionMode(FactionMode.ONE_VS_ONE)
				.build();
		cut.register(testUUID, testType);
		assertEquals(MatchmakingRegistrationResponseCode.ALREADY_IN_LOBBY, cut.register(testUUID, testType).getCode());
	}

}
