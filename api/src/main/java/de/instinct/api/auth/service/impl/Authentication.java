package de.instinct.api.auth.service.impl;

import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.core.service.impl.BaseService;

public class Authentication extends BaseService implements AuthenticationInterface {
	
	public Authentication() {
		super("auth");
		super.loadURL();
		super.connect();
	}

}
