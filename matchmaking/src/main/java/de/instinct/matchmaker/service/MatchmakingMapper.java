package de.instinct.matchmaker.service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.service.model.GameType;

public interface MatchmakingMapper {

	GameType map(MatchmakingRegistrationRequest request);

}
