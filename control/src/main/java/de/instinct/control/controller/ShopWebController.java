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

import de.instinct.api.control.requests.CreateShopCategoryRequest;
import de.instinct.api.control.requests.CreateShopCategoryResponse;
import de.instinct.api.control.requests.CreateShopItemRequest;
import de.instinct.api.control.requests.CreateShopItemResponse;
import de.instinct.api.control.requests.CreateShopItemStageRequest;
import de.instinct.api.control.requests.CreateShopItemStageResponse;
import de.instinct.api.control.requests.DeleteShopCategoryResponse;
import de.instinct.api.control.requests.DeleteShopItemResponse;
import de.instinct.api.control.requests.DeleteShopItemStageRequest;
import de.instinct.api.control.requests.DeleteShopItemStageResponse;
import de.instinct.api.control.requests.UpdateShopItemStageRequest;
import de.instinct.api.control.requests.UpdateShopItemStageResponse;
import de.instinct.control.service.base.BaseService;
import de.instinct.control.service.shop.ShopControlService;
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
        return "home";
    }

    @GetMapping("/refresh")
    public String refresh(Model model) {
        return home(model);
    }
    
    @GetMapping("/module/{id}")
    public String selectTypeModule(Model model, @PathVariable("id") String id) {
    	shopService.prepareShopTable(model, id);
    	model.addAttribute("activeShopCategory", id);
		return home(model);
    }
    
    @PostMapping("/create/category")
    public ResponseEntity<CreateShopCategoryResponse> createCategory(@RequestBody CreateShopCategoryRequest request) {
		return ResponseEntity.ok(shopService.createCategory(request));
	}
    
    @PostMapping("/delete/category/{id}")
    public ResponseEntity<DeleteShopCategoryResponse> deleteShopCategory(@PathVariable("id") String id) {
		return ResponseEntity.ok(shopService.deleteCategory(id));
	}
    
    @PostMapping("/create/item")
    public ResponseEntity<CreateShopItemResponse> createItem(@RequestBody CreateShopItemRequest request) {
		return ResponseEntity.ok(shopService.createItem(request));
	}
    
    @PostMapping("/delete/item/{id}")
    public ResponseEntity<DeleteShopItemResponse> deleteShopItem(@PathVariable("id") String id) {
		return ResponseEntity.ok(shopService.deleteItem(id));
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
