package javaweb.ElectronicStore.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.order.Order;
import javaweb.ElectronicStore.models.order.OrderStatus;
import javaweb.ElectronicStore.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<Order> findOrdersByDateRange(Date startDate, Date endDate) {
		return orderRepository.findByOrderTimeBetween(startDate, endDate);
	}

	@Override
	public Order saveOrder(Order order) {
		// TODO Auto-generated method stub
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrder() {
		return orderRepository.findAll();
	}

	@Override
	public Order getOrderById(Long orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}


	@Override
	public List<Order> getOrdersByYear(int selectedYear) {
		return orderRepository.findOrdersByYear(selectedYear);
	}

	@Override
	public List<Order> getOrdersByUser(User user) {
		return orderRepository.findByUser(user);
	}

	@Override
	public List<Order> getOrdersByUserAndStatus(User user, OrderStatus orderStatus) {
		return orderRepository.findByUserAndStatus(user, orderStatus);
	}

	@Override
	public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
		return orderRepository.getOrdersByStatus(orderStatus);
	}

}
