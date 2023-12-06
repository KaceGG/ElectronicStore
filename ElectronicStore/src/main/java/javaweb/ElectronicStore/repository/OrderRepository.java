package javaweb.ElectronicStore.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.order.Order;
import javaweb.ElectronicStore.models.order.OrderStatus;

public interface OrderRepository extends JpaRepository<Order,Long> {
	List<Order> getOrdersByStatus(OrderStatus orderStatus);
	
	List<Order> findByUser(User user);
    List<Order> findByUserAndStatus(User user, OrderStatus status);
	
	@Query("SELECT o FROM Order o WHERE o.orderTime >= :startDate AND o.orderTime <= :endDate")
	List<Order> findByOrderTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE YEAR(o.orderTime) = :year")
    List<Order> findOrdersByYear(@Param("year") int year);
}
