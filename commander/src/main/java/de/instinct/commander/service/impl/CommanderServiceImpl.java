package de.instinct.commander.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.commander.dto.BaseCommanderData;
import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.commander.dto.CommanderInitializationResponseCode;
import de.instinct.api.commander.dto.CommanderRankUpResponseCode;
import de.instinct.api.commander.dto.CommanderUpgrade;
import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.base.file.FileManager;
import de.instinct.commander.service.CommanderService;

@Service
public class CommanderServiceImpl implements CommanderService {
	
	private BaseCommanderData baseData;
	private Map<String, CommanderData> commanders;
	
	public CommanderServiceImpl() {
		commanders = new HashMap<>();
		baseData = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), BaseCommanderData.class);
	}
	
	public CommanderInitializationResponseCode initialize(String token) {
		if (commanders.containsKey(token)) return CommanderInitializationResponseCode.ALREADY_INITIALIZED;
		CommanderData newCommander = new CommanderData();
		newCommander.setStartResources(baseData.getBaseStartResources());
		newCommander.setMaxResources(baseData.getBaseMaxResources());
		newCommander.setResourceGenerationSpeed(baseData.getBaseResourceGenerationSpeed());
		commanders.put(token, newCommander);
		return CommanderInitializationResponseCode.SUCCESS;
	}

	@Override
	public CommanderData data(String token) {
		CommanderData commander = commanders.get(token);
		if (commander == null) {
			initialize(token);
			commander = commanders.get(token);
		}
		return commander;
	}

	@Override
	public CommanderRankUpResponseCode rankup(String token, PlayerRank rank) {
		CommanderData commander = commanders.get(token);
		if (commander == null) return CommanderRankUpResponseCode.TOKEN_NOT_FOUND;
		RankUpCommanderUpgrade upgrade = upgrade(rank);
		for (CommanderUpgrade comUpgrade : upgrade.getUpgrades()) {
			switch (comUpgrade.getStat()) {
			case MAX_RES:
				commander.setMaxResources(commander.getMaxResources() + comUpgrade.getValue());
				break;
			case START_RES:
				commander.setStartResources(commander.getStartResources() + comUpgrade.getValue());
				break;
			case RES_PER_SECOND:
				commander.setResourceGenerationSpeed(commander.getResourceGenerationSpeed() + comUpgrade.getValue());
				break;
			}
		}
		return CommanderRankUpResponseCode.SUCCESS;
	}

	@Override
	public RankUpCommanderUpgrade upgrade(PlayerRank rank) {
		for (RankUpCommanderUpgrade upgrade : baseData.getRankUpData()) {
			if (upgrade.getPlayerRank() == rank) {
				return upgrade;
			}
		}	
		return null;
	}

}
