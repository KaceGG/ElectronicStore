package javaweb.ElectronicStore.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javaweb.ElectronicStore.models.CartItem;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.order.PaymentMethod;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;
import javaweb.ElectronicStore.service.CartItemService;

@Controller
@RequestMapping("/user/pay")
public class payController {
	
	//	trang thanh toán 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
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
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String pay(@RequestParam(name = "selectedIds", required = false) Long[] selectedIds,
                      @ModelAttribute("user") User user, Model model) {
        List<CartItem> items = new ArrayList<>();
        float subtotal = 0;
        if (selectedIds != null) {
            for (Long id : selectedIds) {
                CartItem cartItem = cartItemService.getCartItemById(id);
                subtotal = subtotal + cartItem.getQuantity() * cartItem.getProduct().getPrice();
                items.add(cartItem);
            }
        }

        model.addAttribute("selectedItems", items);
        model.addAttribute("length", items.size());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("paymentType", PaymentMethod.values());

        return "pay";
    }
	
	
	
	
	
	
}
