package de.instinct.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;

class APITest {

	@BeforeEach
	void setUp() throws Exception {
		API.initialize(APIConfiguration.DEV);
	}

	@Test
	void test() {
		API.discovery().connect();
		API.printAPIStatus();
		assertEquals(true, true);
	}

}
