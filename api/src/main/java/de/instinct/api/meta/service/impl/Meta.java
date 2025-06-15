package de.instinct.api.meta.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.api.meta.dto.modules.ModuleData;
import de.instinct.api.meta.dto.modules.ModuleRegistrationResponseCode;
import de.instinct.api.meta.service.MetaInterface;

public class Meta extends BaseService implements MetaInterface {

	public Meta() {
		super("meta");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public NameRegisterResponseCode registerName(String username) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("register")
				.pathVariable(username)
				.build());
		return ObjectJSONMapper.mapJSON(response, NameRegisterResponseCode.class);
	}

	@Override
	public ProfileData profile(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("profile")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ProfileData.class);
	}
	
	@Override
	public ModuleData modules(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("modules")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ModuleData.class);
	}
	
	@Override
	public ResourceData resources(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("resources")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ResourceData.class);
	}
	
	@Override
	public ModuleRegistrationResponseCode registerModule(String token, MenuModule module) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("modules")
				.payload(module)
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ModuleRegistrationResponseCode.class);
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("initialize")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, RegisterResponseCode.class);
	}

	@Override
	public LoadoutData loadout(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("loadout")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, LoadoutData.class);
	}

	@Override
	public String token(String username) {
		if (!isConnected()) return null;
		return super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("token")
				.pathVariable(username)
				.build());
	}

	@Override
	public ResourceUpdateResponseCode addResources(String token, ResourceData resourceUpdate) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("resources")
				.pathVariable(token)
				.payload(resourceUpdate)
				.build());
		return ObjectJSONMapper.mapJSON(response, ResourceUpdateResponseCode.class);
	}
	
	@Override
	public ExperienceUpdateResponseCode experience(String token, long experience) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("experience")
				.pathVariable(token + "/" + experience)
				.build());
		return ObjectJSONMapper.mapJSON(response, ExperienceUpdateResponseCode.class);
	}

}
