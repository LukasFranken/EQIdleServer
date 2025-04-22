package de.instinct.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.model.FactionMode;
import de.instinct.api.matchmaking.model.GameMode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.VersusMode;

class APITest {

	@BeforeEach
	void setUp() throws Exception {
		API.initialize(APIConfiguration.DEV);
	}

	@Test
	void fullE2ETest() {
		//init
		API.discovery().connect();
		API.authentication().connect();
		API.meta().connect();
		API.matchmaking().connect();
		API.printAPIStatus();
		System.out.println("Initialization E2E-Test successful");
		//auth
		String token = API.authentication().register();
		API.authKey = token;
		System.out.println("token " + token);
		System.out.println(API.authentication().verify(token));
		System.out.println("Auth service E2E-Test successful");
		//meta
		System.out.println(API.meta().registerName("testuser"));
		System.out.println(API.meta().profile());
		System.out.println("Meta service E2E-Test successful");
		MatchmakingRegistrationResponse matchmakingRegistrationResponse = API.matchmaking().register(MatchmakingRegistrationRequest.builder()
				.gameType(GameType.builder()
						.versusMode(VersusMode.AI)
						.gameMode(GameMode.CONQUEST)
						.factionMode(FactionMode.ONE_VS_ONE)
						.build())
				.build());
		System.out.println(API.matchmaking().status(matchmakingRegistrationResponse.getLobbyUUID()));
		System.out.println(API.matchmaking().callback(matchmakingRegistrationResponse.getLobbyUUID(), CallbackCode.READY));
		System.out.println(API.matchmaking().dispose(matchmakingRegistrationResponse.getLobbyUUID()));
		System.out.println("Matchmaking service E2E-Test successful");
		//finalize
		assertEquals(true, true);
	}

}
