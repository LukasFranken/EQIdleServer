package de.instinct.matchmaker.service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.service.model.GameType;
import de.instinct.matchmaker.service.model.GameserverInfo;

public interface MatchmakingMapper {

	GameType map(MatchmakingRegistrationRequest request);

	MatchmakingStatusResponse mapGameserverInfo(MatchmakingStatusResponse response, GameserverInfo gameserverInfo);

}
