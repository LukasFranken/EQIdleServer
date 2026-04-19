package de.instinct.mining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.mining.service.model.Session;

public class SessionController {
	
	private List<Session> sessions;
	
	public SessionController() {
		this.sessions = new ArrayList<>();
	}

	public void processMessage(Connection c, Object o) {
		
	}

	public void disconnect(Connection c) {
		
	}

	public CreateSessionResponse createSession(CreateSessionRequest request) {
		Session session = new Session();
		session.setSessionUUID(UUID.randomUUID().toString());
		session.setClients(new ArrayList<>());
		//session.setState();
		sessions.add(session);
		return CreateSessionResponse.SUCCESS;
	}

}
