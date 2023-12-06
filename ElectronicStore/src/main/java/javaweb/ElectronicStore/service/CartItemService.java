package javaweb.ElectronicStore.service;

import java.util.List;

import javaweb.ElectronicStore.models.CartItem;
import javaweb.ElectronicStore.models.User;

public interface CartItemService {
	CartItem AddToCart(CartItem cartItem);
	
	void removeFromCart(Long cartItemId);
	
	CartItem getCartItemById(Long cartItemId);
	
	List<CartItem> getCartItemsByUser(User user);
	
}
