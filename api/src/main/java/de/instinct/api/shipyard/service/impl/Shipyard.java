package de.instinct.api.shipyard.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelUpdateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeUpdateResponse;
import de.instinct.api.shipyard.dto.ship.ShipStatisticReportRequest;
import de.instinct.api.shipyard.dto.ship.ShipStatisticReportResponse;
import de.instinct.api.shipyard.service.ShipyardInterface;

public class Shipyard extends BaseService implements ShipyardInterface {

	public Shipyard() {
		super("shipyard");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public ShipyardInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipyardInitializationResponseCode.class);
	}
	
	@Override
	public PlayerShipyardData data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerShipyardData.class);
	}
	
	@Override
	public ShipyardData shipyard() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("shipyard")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipyardData.class);
	}

	@Override
	public UseShipResponseCode use(String token, String shipUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("use")
				.pathVariable(token + "/" + shipUUID)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UseShipResponseCode.class);
	}

	@Override
	public UnuseShipResponseCode unuse(String token, String shipUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("unuse")
				.pathVariable(token + "/" + shipUUID)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UnuseShipResponseCode.class);
	}

	@Override
	public StatChangeResponse hangar(String token, int count) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("hangar")
				.pathVariable(token + "/" + count)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, StatChangeResponse.class);
	}

	@Override
	public StatChangeResponse active(String token, int count) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("active")
				.pathVariable(token + "/" + count)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, StatChangeResponse.class);
	}

	@Override
	public ShipBuildResponse build(String shiptoken) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("build")
				.pathVariable(shiptoken)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipBuildResponse.class);
	}

	@Override
	public ShipUpgradeResponse upgrade(String shiptoken) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("upgrade")
				.pathVariable(shiptoken)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipUpgradeResponse.class);
	}
	
	@Override
	public ShipAddResponse add(String token, int shipid) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("add")
				.pathVariable(token + "/" + shipid)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipAddResponse.class);
	}
	
	@Override
	public ShipStatisticReportResponse statistic(ShipStatisticReportRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("statistic")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipStatisticReportResponse.class);
	}

	@Override
	public ShipCreateResponse createShip(ShipCreateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/ship/create")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipCreateResponse.class);
	}
	
	@Override
	public ComponentCreateResponse createComponent(ComponentCreateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/component/create")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentCreateResponse.class);
	}
	
	@Override
	public ComponentUpdateResponse updateComponent(ComponentUpdateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/component/update")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentUpdateResponse.class);
	}

	@Override
	public ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/component/delete")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentDeleteResponse.class);
	}

	@Override
	public ComponentLevelCreateResponse createComponentLevel(ComponentLevelCreateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/componentlevel/create")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentLevelCreateResponse.class);
	}

	@Override
	public ComponentLevelUpdateResponse updateComponentLevel(ComponentLevelUpdateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/componentlevel/update")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentLevelUpdateResponse.class);
	}

	@Override
	public ComponentLevelDeleteResponse deleteComponentLevel(ComponentLevelDeleteRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/componentlevel/delete")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ComponentLevelDeleteResponse.class);
	}

	@Override
	public LevelAttributeCreateResponse createLevelAttribute(LevelAttributeCreateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/levelattribute/create")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, LevelAttributeCreateResponse.class);
	}

	@Override
	public LevelAttributeUpdateResponse updateLevelAttribute(LevelAttributeUpdateRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/levelattribute/update")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, LevelAttributeUpdateResponse.class);
	}

	@Override
	public LevelAttributeDeleteResponse deleteLevelAttribute(LevelAttributeDeleteRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("admin/levelattribute/delete")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, LevelAttributeDeleteResponse.class);
	}

}
