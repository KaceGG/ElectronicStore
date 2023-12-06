package javaweb.ElectronicStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.Review;

public interface ReviewRepository extends JpaRepository<Review,Long> {
	List<Review> findByProduct(Product product);
	
	Page<Review> findByProduct(Product product, Pageable pageable);
	
	Page<Review> findByProductId(Long productId, Pageable pageable);
}
