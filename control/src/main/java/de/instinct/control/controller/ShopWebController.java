package de.instinct.control.controller;

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
    
    @GetMapping("/modal/createshopitemmodal/{id}")
    public String createShopItemModal(Model model, @PathVariable("id") String id) {
    	System.out.println("id: " + id);
    	model.addAttribute("categoryId", id);
        return "content/modal/createshopitemmodal :: createshopitemmodal";
    }

}
