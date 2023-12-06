package javaweb.ElectronicStore.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.CartItem;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.CartItemRepository;


@Service
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	
	@Override
	public CartItem AddToCart(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
		
	}


	@Override
	public List<CartItem> getCartItemsByUser(User user) {
		return cartItemRepository.findByUser(user);
	}


	@Override
	public void removeFromCart(Long cartItemId) {
		cartItemRepository.deleteById(cartItemId);
		
	}


	@Override
	public CartItem getCartItemById(Long cartItemId) {
		return cartItemRepository.findById(cartItemId).orElse(null);
	}


	

}
