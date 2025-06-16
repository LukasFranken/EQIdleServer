package de.instinct.api.core.service;

public interface BaseServiceInterface {
	
	void connect();
	
	void disconnect();
	
	boolean isConnected();
	
	long ping();

}
