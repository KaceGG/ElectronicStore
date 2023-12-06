package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.Review;
import javaweb.ElectronicStore.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public Review saveReview(Review review) {

		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getReviewsByProduct(Product product) {
		return reviewRepository.findByProduct(product);
	}

	@Override
	public Review getReviewById(Long reviewId) {
		// TODO Auto-generated method stub
		return reviewRepository.findById(reviewId).orElse(null);
	}
	
	@Override
	public Page<Review> getReviewsByProductPaged(Product product, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("reviewTime").descending());
        return reviewRepository.findByProduct(product, pageable);
    }
	@Override
	public Page<Review> getReviewsByProductIdPaged(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }
}
