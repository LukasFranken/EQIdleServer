package de.instinct.shipyard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.API;
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
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateResponse;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.shipyard.service.ShipyardService;
import de.instinct.shipyard.service.impl.ShipyardServiceImpl;

@RestController
@RequestMapping("/shipyard")
public class ShipyardController extends BaseServiceController {
	
	private final ShipyardService service;

	public ShipyardController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("shipyard", serverPort, version);
		service = new ShipyardServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<ShipyardInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<PlayerShipyardData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.getShipyardData(token));
	}
	
	@GetMapping("/shipyard")
	public ResponseEntity<ShipyardData> shipyard() {
		return ResponseEntity.ok(service.getBaseData());
	}
	
	@GetMapping("/load")
	public ResponseEntity<ShipyardData> load() {
		service.loadBaseData();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/use/{token}/{shipUUID}")
	public ResponseEntity<UseShipResponseCode> use(@PathVariable String token, @PathVariable String shipUUID) {
		return ResponseEntity.ok(service.useShip(token, shipUUID));
	}
	
	@PostMapping("/unuse/{token}/{shipUUID}")
	public ResponseEntity<UnuseShipResponseCode> unuse(@PathVariable String token, @PathVariable String shipUUID) {
		return ResponseEntity.ok(service.unuseShip(token, shipUUID));
	}
	
	@PostMapping("/hangar/{token}/{count}")
	public ResponseEntity<StatChangeResponse> hangar(@PathVariable String token, @PathVariable int count) {
		return ResponseEntity.ok(service.changeHangarSpace(token, count));
	}
	
	@PostMapping("/active/{token}/{count}")
	public ResponseEntity<StatChangeResponse> active(@PathVariable String token, @PathVariable int count) {
		return ResponseEntity.ok(service.changeActiveShips(token, count));
	}
	
	@PostMapping("/build/{shiptoken}")
	public ResponseEntity<ShipBuildResponse> build(@RequestHeader String token, @PathVariable String shiptoken) {
		return ResponseEntity.ok(service.build(token, shiptoken));
	}
	
	@PostMapping("/upgrade/{shiptoken}")
	public ResponseEntity<ShipUpgradeResponse> upgrade(@RequestHeader String token, @PathVariable String shiptoken) {
		return ResponseEntity.ok(service.upgrade(token, shiptoken));
	}
	
	@PostMapping("/add/{token}/{shipid}")
	public ResponseEntity<ShipAddResponse> add(@PathVariable String token, @PathVariable int shipid) {
		return ResponseEntity.ok(service.addBlueprint(token, shipid));
	}
	
	//admin
	@PostMapping("/admin/ship/create")
	public ResponseEntity<ShipCreateResponse> createShip(@RequestBody ShipCreateRequest request) {
		return ResponseEntity.ok(service.createShip(request));
	}
	
	@PostMapping("/admin/component/create")
	public ResponseEntity<ComponentCreateResponse> createComponent(@RequestBody ComponentCreateRequest request) {
		return ResponseEntity.ok(service.createComponent(request));
	}
	
	@PostMapping("/admin/component/update")
	public ResponseEntity<ComponentUpdateResponse> updateComponent(@RequestBody ComponentUpdateRequest request) {
		return ResponseEntity.ok(service.updateComponent(request));
	}
	
	@PostMapping("/admin/component/delete")
	public ResponseEntity<ComponentDeleteResponse> deleteComponent(@RequestBody ComponentDeleteRequest request) {
		return ResponseEntity.ok(service.deleteComponent(request));
	}
	
}
