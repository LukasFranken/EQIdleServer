package de.instinct.game.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.engine.net.KryoRegistrator;
import de.instinct.game.config.GameserverConfig;
import de.instinct.game.service.GameserverManagerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameserverManagerServiceImpl implements GameserverManagerService {
	
	private Server server;
	private ServerConnectionListener connectionListener;
	
	private final GameserverConfig gameserverConfig;

	@Override
	public void start() {
		connectionListener = new ServerConnectionListener();
		server = new Server();
		Kryo kryo = server.getKryo();
        KryoRegistrator.registerAll(kryo);
		server.addListener(connectionListener);
		server.start();
		try {
			server.bind(gameserverConfig.getTcpPort(), gameserverConfig.getUdpPort());
			System.out.println("Server successfully started for ports - TCP: " + gameserverConfig.getTcpPort() + ", UDP: " + gameserverConfig.getUdpPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		server.stop();
		connectionListener.dispose();
	}

	@Override
	public void createSession(GameSessionInitializationRequest request) {
		SessionManager.create(request);
	}

}
