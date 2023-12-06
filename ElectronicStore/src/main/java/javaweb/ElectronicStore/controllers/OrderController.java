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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javaweb.ElectronicStore.dto.OrderDto;
import javaweb.ElectronicStore.dto.PaymentResDTO;
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
public class OrderController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
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
	
	@GetMapping("/user/order")
	public String GetOrder(@RequestParam(name ="status" ,defaultValue = "all" ) String status, Model model, Principal principal) {
	    if (principal != null) {
	        String email = principal.getName();
	        User user = userRepository.findByEmail(email);
	        
	        List<Order> orders;
	        
	        if ("all".equalsIgnoreCase(status)) {
	            orders = orderService.getOrdersByUser(user);
	        } else {
	            OrderStatus orderStatus = OrderStatus.valueOf(status);
	            orders = orderService.getOrdersByUserAndStatus(user, orderStatus);
	        }
	        
	        model.addAttribute("orders", orders);
	        model.addAttribute("selectedStatus", status);
	    }
	    
	    return "order";
	}
	
	
	
	@PostMapping("/user/order")
	public String Order(@RequestParam("paymentType") PaymentMethod paymentType,
						@RequestParam("userId") Long userId,
						@RequestParam("subtotal") float subtotal,
						@RequestParam("productIds") List<Long> productIds,
						@RequestParam("quantities") List<Integer> quantities,
						@RequestParam("city") String city,
	                    @RequestParam("district") String district,
	                    @RequestParam("ward") String ward,
	                    @RequestParam("street") String street) {
		
		if(paymentType == PaymentMethod.COD ) {
			User user = userService.getUserId(userId);
			
			Address address = new Address();
			address.setCity(city);
		    address.setDistrict(district);
		    address.setWard(ward);
		    address.setStreet(street);
		    
		    address.setUser(user);
			
		    user.getAddresses().add(address);
		    
		    
		    
			
			
			Order order = new Order();
			order.setOrderTime(new Date());
			order.setShippingCost(0);
			order.setTax(0);
			order.setTotal(subtotal + order.getShippingCost() + order.getTax());
			order.setDeliverDays(3);
//			order.setDeliverDate(new Date());
			order.setPaymentMethod(paymentType);
			order.setStatus(OrderStatus.PROCESSING);
			order.setCustomer(user);
			
			Set<OrderDetail> orderDetails = new HashSet<>();
			for(int i =0 ;i< productIds.size();i++) {
				Long productId = productIds.get(i);
				Integer quantity = quantities.get(i);
				
				Product product = productService.getProductById(productId);
				
				OrderDetail orderDetail = new OrderDetail();	
				orderDetail.setProduct(product);
				orderDetail.setQuantity(quantity);
				orderDetail.setUnitPrice(product.getPrice());
				
				orderDetail.setOrder(order);
				orderDetails.add(orderDetail);
				
			}
			
			order.setOrderDetails(orderDetails);
			
			orderService.saveOrder(order);
		
			return "redirect:/user/order";
		} else {
			OrderDto.paymentType = paymentType;
			OrderDto.userId = userId;
			OrderDto.subtotal = subtotal;
			OrderDto.productIds = productIds;
			OrderDto.quantities = quantities;
			OrderDto.city = city;
			OrderDto.district = district;
			OrderDto.ward = ward;
			OrderDto.street = street;
			return "redirect:/api/payment/create_payment?amount=" + (int) subtotal;
		}
	}
	
	@GetMapping("/payment_result")
	public String payment_result(
			@RequestParam(value = "vnp_Amount") int vnp_Amount,
			@RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
			@RequestParam(value = "vnp_ResponseCode") String vnp_ResponseCode,
			Model model
	) {
		if (vnp_ResponseCode.equals("00")) {
			if(OrderDto.userId != null) {
				PaymentMethod paymentType = OrderDto.paymentType;
	        	Long userId = OrderDto.userId;
	        	float subtotal = OrderDto.subtotal;
	        	List<Long> productIds = OrderDto.productIds;
	        	List<Integer> quantities = OrderDto.quantities;
	        	String city = OrderDto.city;
	        	String district = OrderDto.district;
	        	String ward = OrderDto.ward;
	        	String street = OrderDto.street;
	        	
	        	User user = userService.getUserId(userId);
				
				Address address = new Address();
				address.setCity(city);
			    address.setDistrict(district);
			    address.setWard(ward);
			    address.setStreet(street);
			    
			    address.setUser(user);
				
			    user.getAddresses().add(address);
				
				Order order = new Order();
				order.setOrderTime(new Date());
				order.setShippingCost(0);
				order.setTax(0);
				order.setTotal(subtotal + order.getShippingCost() + order.getTax());
				order.setDeliverDays(3);
//				order.setDeliverDate(new Date());
				order.setPaymentMethod(paymentType);
				order.setStatus(OrderStatus.PROCESSING);
				order.setCustomer(user);
				
				Set<OrderDetail> orderDetails = new HashSet<>();
				for(int i =0 ;i< productIds.size();i++) {
					Long productId = productIds.get(i);
					Integer quantity = quantities.get(i);
					
					Product product = productService.getProductById(productId);
					
					OrderDetail orderDetail = new OrderDetail();	
					orderDetail.setProduct(product);
					orderDetail.setQuantity(quantity);
					orderDetail.setUnitPrice(product.getPrice());
					
					orderDetail.setOrder(order);
					orderDetails.add(orderDetail);
					
				}
				order.setOrderDetails(orderDetails);
				orderService.saveOrder(order);
				OrderDto.clear();

				model.addAttribute("amount", vnp_Amount);
				model.addAttribute("order_info", vnp_OrderInfo);
				model.addAttribute("status", 1);
			} else {
				return "redirect:/user/order";
			}
		}
		else {
			model.addAttribute("status", 0);
		}
			
		
		return "payResult";
	}
	
	
	@PostMapping("/user/order/updateStatus/{orderId}")
	public String updateStatus(@PathVariable Long orderId, @RequestParam("action") String action, Model model) {
	    Order order = orderService.getOrderById(orderId);

	    if ("cancel".equals(action)) {
	        order.setStatus(OrderStatus.CANCELLED);
	    } else if ("receive".equals(action)) {
	        order.setStatus(OrderStatus.DELIVERED);
	    }

	    orderService.saveOrder(order);

	    return "redirect:/user/order";
	}
	
}
