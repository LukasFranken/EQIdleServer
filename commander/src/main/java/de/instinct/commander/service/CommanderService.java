package de.instinct.commander.service;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.commander.dto.CommanderInitializationResponseCode;
import de.instinct.api.commander.dto.CommanderRankUpResponseCode;
import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import de.instinct.api.meta.dto.PlayerRank;

public interface CommanderService {

	CommanderInitializationResponseCode initialize(String token);

	CommanderData data(String token);

	CommanderRankUpResponseCode rankup(String token, PlayerRank rank);

	RankUpCommanderUpgrade upgrade(PlayerRank rank);

}
