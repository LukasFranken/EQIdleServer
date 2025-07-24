package de.instinct.game.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Server;

import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;
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
		SessionManager.init();
		connectionListener = new ServerConnectionListener();
		Kryo customKryo = new Kryo() {
			  @Override
			  protected Serializer newDefaultSerializer (Class type) {
			    return new FieldSerializer(this, type);
			  }
			};
        KryoRegistrator.registerAll(customKryo);
        server = new Server(16384, 2048, new KryoSerialization(customKryo));
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
	public String createSession(GameSessionInitializationRequest request) {
		return SessionManager.create(request);
	}

	@Override
	public MapPreview preview(String map) {
		return SessionManager.preview(map);
	}

}
