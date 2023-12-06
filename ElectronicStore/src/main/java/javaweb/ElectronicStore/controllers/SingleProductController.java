package javaweb.ElectronicStore.controllers;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.ProductImage;
import javaweb.ElectronicStore.models.Review;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;
import javaweb.ElectronicStore.service.ProductService;
import javaweb.ElectronicStore.service.ReviewService;
import javaweb.ElectronicStore.service.UserService;

@Controller
@RequestMapping
public class SingleProductController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
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
	
	
	@GetMapping("/singleproduct/{productId}")
	public String singleproduct(Model model,@PathVariable Long productId) {
		Product product = productService.getProductById(productId);
		
		List<ProductImage> productImages = productService.getAllProductImgById(productId);
		
		List<Review> reviews = reviewService.getReviewsByProduct(product);
				
		reviews.sort(Comparator.comparing(Review::getReviewTime).reversed());
		
		model.addAttribute("product", product);
		
		model.addAttribute("reviews", reviews);
		
		model.addAttribute("productImages",productImages);
				
		return "singleProduct";
	}
	
	@PostMapping("/user/reviews/{productId}")
	public String PostReviews(@PathVariable Long productId,@ModelAttribute("user") User user, @RequestParam int rating, @RequestParam String comment) {
		
		Product product = productService.getProductById(productId);
		
		Review review = new Review();
		review.setCustomer(user);
		review.setComment(comment);
		review.setProduct(product);
		review.setRating(rating);
		review.setReviewTime(new Date());
		review.setHeadline("");
		
		
		reviewService.saveReview(review);	
		
		return "redirect:/singleproduct/{productId}";
	}
	
	
	@GetMapping("/user/reviews/{productId}/like/{reviewId}")
	public String toggleLikeReview(@PathVariable Long productId, @PathVariable Long reviewId, Principal principal,Model model,RedirectAttributes redirectAttributes) {
	    User user = userService.getEmail(principal.getName());
	    
	    Review review = reviewService.getReviewById(reviewId);
	    	    

	    // Check if the user has already liked the review
	    if (review.getLikedByUsers().contains(user)) {
	        // Remove the user from the likedByUsers set
	        review.getLikedByUsers().remove(user);

	        // Remove the review from the likedReviews set
	        user.getLikedReviews().remove(review);

	        // Decrease the likeCount in the review
	        review.setLikeCount(review.getLikeCount() - 1);
	    } else {
	        // Add the user to the likedByUsers set
	        review.getLikedByUsers().add(user);

	        // Add the review to the likedReviews set
	        user.getLikedReviews().add(review);

	        // Increase the likeCount in the review
	        review.setLikeCount(review.getLikeCount() + 1);
	    }
	    
	    
	    // Save changes to the database
	    reviewService.saveReview(review);
	    userService.saveUser(user);
	    
	    
	    return "redirect:/singleproduct/{productId}";
	}

	
	
	
}
