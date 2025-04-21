package de.instinct.api.meta.service.impl;

import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.service.MetaInterface;

public class Meta extends BaseService implements MetaInterface {

	public Meta(String tag) {
		super("meta");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public NameRegisterResponseCode registerName(String username) {
		return null;
	}

	@Override
	public ProfileData profile() {
		return null;
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		return null;
	}

}
