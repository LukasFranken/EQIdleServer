package de.instinct.matchmaker.service;

import org.junit.jupiter.api.BeforeEach;

import de.instinct.matchmaking.service.impl.MatchmakingServiceImpl;

class MatchmakingServiceImplTest {
	
	MatchmakingServiceImpl cut;

	@BeforeEach
	void setUp() throws Exception {
		cut = new MatchmakingServiceImpl();
	}

}
