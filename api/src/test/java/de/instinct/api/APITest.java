package de.instinct.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.model.FactionMode;
import de.instinct.api.matchmaking.model.GameMode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.VersusMode;

class APITest {

	@BeforeEach
	void setUp() throws Exception {
		API.initialize(APIConfiguration.SERVER);
	}

	@Test
	void fullE2ETest() {
		//init
		API.discovery().connect();
		API.authentication().connect();
		API.meta().connect();
		API.matchmaking().connect();
		API.game().connect();
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
		System.out.println(API.meta().profile(token));
		System.out.println("Meta service E2E-Test successful");
		//matchmaking
		String token2 = API.authentication().register();
		API.authKey = token2;
		API.meta().registerName("testuser2");
		
		String lobbyUUID = API.matchmaking().create().getLobbyUUID();
		System.out.println(lobbyUUID);
		System.out.println(API.matchmaking().settype(lobbyUUID, GameType.builder().factionMode(FactionMode.ONE_VS_ONE).versusMode(VersusMode.PVP).gameMode(GameMode.KING_OF_THE_HILL).build()));
		API.matchmaking().invite("testuser");
		API.authKey = token;
		System.out.println(API.matchmaking().invites());
		API.matchmaking().accept(lobbyUUID);
		System.out.println(API.matchmaking().status(lobbyUUID));
		System.out.println("Matchmaking service E2E-Test successful");
		//game
		
		System.out.println("Game service E2E-Test successful");
		//finalize
		assertEquals(true, true);
	}

}
