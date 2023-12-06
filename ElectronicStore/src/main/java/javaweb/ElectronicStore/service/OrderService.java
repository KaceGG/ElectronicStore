package javaweb.ElectronicStore.service;

import java.util.Date;
import java.util.List;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.order.Order;
import javaweb.ElectronicStore.models.order.OrderStatus;

public interface OrderService {
	Order saveOrder(Order order);
	
	List<Order> findOrdersByDateRange(Date startDate, Date endDate);
	
	List<Order> getAllOrder();
	
	Order getOrderById(Long orderId);

	List<Order> getOrdersByYear(int selectedYear);

	List<Order> getOrdersByUser(User user);

	List<Order> getOrdersByUserAndStatus(User user, OrderStatus orderStatus);

	List<Order> getOrdersByStatus(OrderStatus orderStatus);
}
