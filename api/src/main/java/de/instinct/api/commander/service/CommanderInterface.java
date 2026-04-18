package de.instinct.api.commander.service;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.commander.dto.CommanderInitializationResponseCode;
import de.instinct.api.commander.dto.CommanderRankUpResponseCode;
import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import de.instinct.api.meta.dto.PlayerRank;

public interface CommanderInterface {
	
	CommanderInitializationResponseCode init(String token);
	
	CommanderData data(String token);
	
	CommanderRankUpResponseCode rankup(String token, PlayerRank rank);
	
	RankUpCommanderUpgrade upgrade(PlayerRank rank);

}
