package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.Review;

public interface ReviewService {
	
	Review saveReview(Review review);
	
	List<Review> getReviewsByProduct(Product product);
	
	Review getReviewById(Long reviewId);
	
	Page<Review> getReviewsByProductPaged(Product product, int page, int size);
	
	Page<Review> getReviewsByProductIdPaged(Long productId, Pageable pageable);

}
