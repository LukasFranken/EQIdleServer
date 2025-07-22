package de.instinct.shop.service.model;

public interface ShopItemEffect {
	
	String preconditionMetMessage(String token, int stage);
	
	void applyEffect(String token, int stage);

}
