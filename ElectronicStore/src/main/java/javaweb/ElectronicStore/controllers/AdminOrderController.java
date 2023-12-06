package javaweb.ElectronicStore.controllers;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javaweb.ElectronicStore.models.Address;
import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.order.Order;
import javaweb.ElectronicStore.models.order.OrderDetail;
import javaweb.ElectronicStore.models.order.OrderStatus;
import javaweb.ElectronicStore.models.order.PaymentMethod;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;
import javaweb.ElectronicStore.service.CartItemService;
import javaweb.ElectronicStore.service.OrderService;
import javaweb.ElectronicStore.service.ProductService;
import javaweb.ElectronicStore.service.UserService;

@Controller
@RequestMapping
public class AdminOrderController {
		@Autowired
		private UserService userService;
		
		@Autowired
		private ProductService productService;
		
		@Autowired
		private OrderService orderService;

		@GetMapping("/admin/order")
		public String GetOrder(@RequestParam(name ="status" ,defaultValue = "all" ) String status, Model model) {
			List<Order> orders;
			
			if("all".equalsIgnoreCase(status)) {
				orders = orderService.getAllOrder();
			}else {
				OrderStatus orderStatus = OrderStatus.valueOf(status);
				orders = orderService.getOrdersByStatus(orderStatus);
			}
			
			model.addAttribute("orders" , orders);
			model.addAttribute("selectedStatus", status);
			
			return "AdminOrder";
		}
		
		
		@PostMapping("/admin/order/updateStatus/{orderId}")
		public String updateStatus(@PathVariable Long orderId, Model model) {
		    Order order = orderService.getOrderById(orderId);

		    order.setStatus(OrderStatus.SHIPPING);

		    orderService.saveOrder(order);

		    return "redirect:/admin/order";
		}
		
	
}
