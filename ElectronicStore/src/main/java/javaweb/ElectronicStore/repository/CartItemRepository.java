package javaweb.ElectronicStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import javaweb.ElectronicStore.models.CartItem;
import javaweb.ElectronicStore.models.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	List<CartItem> findByUser(User user);

}