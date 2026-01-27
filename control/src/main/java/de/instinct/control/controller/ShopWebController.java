package de.instinct.control.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import de.instinct.control.service.base.BaseService;
import de.instinct.control.service.shop.ShopControlService;
import de.instinct.control.service.shop.model.CreateShopCategoryRequest;
import de.instinct.control.service.shop.model.CreateShopCategoryResponse;
import de.instinct.control.service.shop.model.CreateShopItemRequest;
import de.instinct.control.service.shop.model.CreateShopItemResponse;
import de.instinct.control.service.shop.model.CreateShopItemStageRequest;
import de.instinct.control.service.shop.model.CreateShopItemStageResponse;
import de.instinct.control.service.shop.model.DeleteShopItemStageRequest;
import de.instinct.control.service.shop.model.DeleteShopItemStageResponse;
import de.instinct.control.service.shop.model.UpdateShopItemStageRequest;
import de.instinct.control.service.shop.model.UpdateShopItemStageResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("shop")
public class ShopWebController {
	
	private final BaseService baseService;
	private final ShopControlService shopService;
	
	@GetMapping("")
    public String home(Model model) {
		baseService.setModel(model);
		shopService.setModel(model);
		model.addAttribute("panel", "shop");
		model.addAttribute("modal", "basemodal");
        return "home";
    }

    @GetMapping("/refresh")
    public String refresh(Model model) {
        return home(model);
    }
    
    @GetMapping("/module/{id}")
    public String selectTypeModule(Model model, @PathVariable("id") String id) {
    	shopService.prepareShopTable(model, id);
		return home(model);
    }
    
    @PostMapping("/create/category")
    public ResponseEntity<CreateShopCategoryResponse> createCategory(@RequestBody CreateShopCategoryRequest request) {
		return ResponseEntity.ok(shopService.createCategory(request));
	}
    
    @PostMapping("/create/item")
    public ResponseEntity<CreateShopItemResponse> createItem(@RequestBody CreateShopItemRequest request) {
		return ResponseEntity.ok(shopService.createItem(request));
	}
    
    @PostMapping("/create/itemstage")
    public ResponseEntity<CreateShopItemStageResponse> createItemStage(@RequestBody CreateShopItemStageRequest request) {
		return ResponseEntity.ok(shopService.createItemStage(request));
	}
    
    @PostMapping("/update/itemstage")
    public ResponseEntity<UpdateShopItemStageResponse> createItemStage(@RequestBody UpdateShopItemStageRequest request) {
		return ResponseEntity.ok(shopService.updateItemStage(request));
	}
    
    @PostMapping("/delete/itemstage")
    public ResponseEntity<DeleteShopItemStageResponse> createItemStage(@RequestBody DeleteShopItemStageRequest request) {
		return ResponseEntity.ok(shopService.deleteItemStage(request));
	}
    
    @GetMapping("/modal/createshopitemmodal/{id}")
    public String createShopItemModal(Model model, @PathVariable("id") String id) {
    	model.addAttribute("categoryId", id);
        return "content/modal/createshopitemmodal :: createshopitemmodal";
    }
    
    @GetMapping("/modal/shopitemmodal/{id}")
    public String getBuildCostModal(Model model, @PathVariable("id") String id) {
    	shopService.prepareItemModal(model, id);
        return "content/modal/shopitemmodal :: shopitemmodal";
    }
    
    @GetMapping("/effect-types")
    public ResponseEntity<List<String>> getEffectTypes() {
        return ResponseEntity.ok(shopService.getEffectTypes());
    }

}
