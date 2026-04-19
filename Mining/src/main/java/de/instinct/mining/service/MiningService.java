package de.instinct.mining.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.api.mining.service.MiningInterface;
import de.instinct.engine.mining.net.MiningKryoRegistrator;
import de.instinct.mining.config.ApplicationConfiguration;

@Service
public class MiningService implements MiningInterface {
	
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
		server = new Server(65536, 65536);
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

}
