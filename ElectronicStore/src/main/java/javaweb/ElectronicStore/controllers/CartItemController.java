package javaweb.ElectronicStore.controllers;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javaweb.ElectronicStore.models.CartItem;
import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;
import javaweb.ElectronicStore.service.CartItemService;
import javaweb.ElectronicStore.service.ProductService;


@Controller
@RequestMapping
public class CartItemController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserRepository userRepository;
	
	private StaticClass staticClass;
	
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepository.findByEmail(email);
			if(user == null) {
				user = new User();
				user.setLastName(staticClass.name);
				user.setEmail(staticClass.email);
			}
			m.addAttribute("user", user);
		}
		
	}
	

		@GetMapping("/user/cartItemCount")
		@ResponseBody
		public int getCartItemCount(@ModelAttribute("user") User user) {
			if (user != null) {
				List<CartItem> cartItems = cartItemService.getCartItemsByUser(user);
				return cartItems.size();
			}
			return 0;
		}
	
		@GetMapping("/user/cart")
		public String Cart(Model model, @ModelAttribute("user") User user) {
			
			if (user != null) {
		        // Retrieve cart items for the current user
		        List<CartItem> cartItems = cartItemService.getCartItemsByUser(user);

		        model.addAttribute("cartItems", cartItems);
		        
		        model.addAttribute("cartItemCount", cartItems.size());
		        
		    }
			
			return "cart";
		}
		
		@PostMapping("/user/cart")
		public String AddToCart(@RequestParam("productId") Long productId, 
								@RequestParam("quantity") int quantity, 
								@ModelAttribute("user") User user) {
			
			Product product = productService.getProductById(productId);
			
			if(product !=null && user!=null) {
				
				List<CartItem> cartItems = cartItemService.getCartItemsByUser(user);
				boolean productFound = false;
					for(CartItem cartItem : cartItems) {
						if (cartItem.getProduct().getId() == productId) {
							int newQuantity = cartItem.getQuantity() + quantity;
							cartItem.setQuantity(newQuantity);
							cartItemService.AddToCart(cartItem);
							productFound = true;
			                break;	
						}

					}
									
					if (!productFound) {
			            CartItem cartItem = new CartItem();
			            cartItem.setCustomer(user);
			            cartItem.setProduct(product);
			            cartItem.setQuantity(quantity);
			            cartItemService.AddToCart(cartItem);
			        }
				
				
			}
			
			return "redirect:/shop";
		}
		@PostMapping("/user/cart/update")
	    public String updateCartItemQuantity(@RequestParam("cartItemId") Long cartItemId,
	                                         @RequestParam("quantity") int quantity) {
	        CartItem cartItem = cartItemService.getCartItemById(cartItemId);

	        if (cartItem != null) {
	            int newQuantity = Math.max(cartItem.getQuantity() + quantity, 1);
	            cartItem.setQuantity(newQuantity);
	            cartItemService.AddToCart(cartItem);
	        }

	        return "redirect:/user/cart";
	    }
		
		@PostMapping("/user/cart/delete")
		public String deleteCartItem(@RequestParam("cartItemId") Long cartItemId) {
		    cartItemService.removeFromCart(cartItemId);
		    return "redirect:/user/cart";
		}
		
		
	
}
