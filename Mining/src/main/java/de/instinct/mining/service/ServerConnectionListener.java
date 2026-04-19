package de.instinct.mining.service;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

public class ServerConnectionListener extends Listener {
	
	private SessionController sessionController;
	private ArrayList<Connection> connections;
	
	public ServerConnectionListener(SessionController sessionController) {
		connections = new ArrayList<>();
		this.sessionController = sessionController;
	}
	
	@Override
	public void connected(Connection c) {
		System.out.println("connected: " + c);
        connections.add(c);
    }

	@Override
    public void received(Connection c, Object o) {
		if (o instanceof FrameworkMessage) {
			
		} else {
			System.out.println("received: " + o);
			sessionController.processMessage(c, o);
		}
    }

	@Override
    public void disconnected(Connection c) {
		System.out.println("disconnected: " + c);
        connections.remove(c);
        sessionController.disconnect(c);
    }

	public void dispose() {
		System.out.println("Disposing connection listener, closing " + connections.size() + " connections.");
		for (Connection c : connections) {
			c.close();
		}
	}

}
