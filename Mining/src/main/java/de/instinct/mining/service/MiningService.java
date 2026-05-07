package de.instinct.mining.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.api.mining.dto.Maps;
import de.instinct.api.mining.dto.player.MiningPlayerMissionData;
import de.instinct.engine.mining.net.MiningKryoRegistrator;
import de.instinct.engine_api.mining.model.MiningPlayerInventoryData;
import de.instinct.engine_api.mining.service.MiningEngineInterface;
import de.instinct.engine_api.mining.service.model.MiningMissionOverview;
import de.instinct.mining.config.ApplicationConfiguration;

@Service
public class MiningService implements MiningEngineInterface {
	
	private Server server;
	private ServerConnectionListener connectionListener;
	private MiningKryoRegistrator kryoRegistrator;
	
	private SessionController sessionController;
	private ApplicationConfiguration config;

	public MiningService(ApplicationConfiguration config) {
		this.config = config;
	}
	
	@Override
	public void start() {
		sessionController = new SessionController();
		connectionListener = new ServerConnectionListener(sessionController);
		server = new Server(8096, 8096);
		Kryo kryo = server.getKryo();
		kryoRegistrator = new MiningKryoRegistrator();
		kryoRegistrator.registerClasses(kryo);
		server.addListener(connectionListener);
		server.start();
		try {
			server.bind(config.getGameTcpPort(), config.getGameUdpPort());
			System.out.println("Server successfully started for ports - TCP: " + config.getGameTcpPort() + ", UDP: " + config.getGameUdpPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CreateSessionResponse createSession(CreateSessionRequest request) {
		return sessionController.createSession(request);
	}

	@Override
	public MiningPlayerInventoryData inventory(String token) {
		return sessionController.getPlayerInventory(token);
	}

	@Override
	public MiningPlayerMissionData missiondata(String token) {
		return sessionController.getPlayerMissionData(token);
	}

	@Override
	public MiningMissionOverview mission(String missionName) {
		return sessionController.getMissionOverview(missionName);
	}

	@Override
	public Maps maps() {
		return sessionController.getMaps();
	}

}
