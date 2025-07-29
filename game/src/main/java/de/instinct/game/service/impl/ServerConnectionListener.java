package de.instinct.game.service.impl;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import de.instinct.engine.net.message.types.FleetMovementMessage;
import de.instinct.engine.net.message.types.GamePauseMessage;
import de.instinct.engine.net.message.types.JoinMessage;
import de.instinct.engine.net.message.types.LoadedMessage;
import de.instinct.engine.net.message.types.SurrenderMessage;

public class ServerConnectionListener extends Listener {
	
	private ArrayList<Connection> connections;
	
	public ServerConnectionListener() {
		connections = new ArrayList<>();
	}
	
	@Override
	public void connected(Connection c) {
        connections.add(c);
    }

	@Override
    public void received(Connection c, Object o) {
		if (o instanceof FrameworkMessage) {
			
		} else {
			System.out.println("received: " + o);
			if (o instanceof JoinMessage) {
	    		JoinMessage joinMessage = (JoinMessage) o;
	            SessionManager.join(joinMessage, c);
	        }
	    	if (o instanceof LoadedMessage) {
	    		LoadedMessage loadedMessage = (LoadedMessage) o;
	            SessionManager.loaded(loadedMessage, c);
	        }
	        if (o instanceof FleetMovementMessage) {
	            FleetMovementMessage fleetMovement = (FleetMovementMessage) o;
	            SessionManager.process(fleetMovement);
	        }
	        if (o instanceof SurrenderMessage) {
	        	SurrenderMessage surrender = (SurrenderMessage) o;
	            SessionManager.process(surrender);
	        }
	        if (o instanceof GamePauseMessage) {
	        	GamePauseMessage gamePause = (GamePauseMessage) o;
	            SessionManager.process(gamePause);
	        }
		}
    }

	@Override
    public void disconnected(Connection c) {
        connections.remove(c);
        SessionManager.disconnect(c);
    }

	public void dispose() {
		
	}

}
