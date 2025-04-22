package de.instinct.matchmaking.service;

import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.matchmaking.service.model.GameserverInfo;

public interface MatchmakingMapper {

	MatchmakingStatusResponse mapGameserverInfo(MatchmakingStatusResponse response, GameserverInfo gameserverInfo);

}
