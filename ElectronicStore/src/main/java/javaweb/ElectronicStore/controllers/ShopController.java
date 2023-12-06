package javaweb.ElectronicStore.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javaweb.ElectronicStore.models.Brand;
import javaweb.ElectronicStore.models.Category;
import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;
import javaweb.ElectronicStore.service.BrandService;
import javaweb.ElectronicStore.service.CategoryService;
import javaweb.ElectronicStore.service.ProductService;

@Controller
@RequestMapping
public class ShopController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandService;
	
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
	
	@GetMapping("/shop")
	public String shop(Model model,@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "name" ,required =false) String productName,
			@RequestParam(name = "categoryId",required =false) Long categoryId,
			@RequestParam(name = "brandId",required =false) Long brandId) {
		
		int pageSize = 8;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Product> productPage;

	    if (productName != null && !productName.isEmpty()) {
	        // Search for products by name
	        productPage = productService.searchProductByName(productName, pageable);
	    } else if (brandId != null && categoryId != null) {
	        // Filter products by both brand and category
	        productPage = productService.getProductsByBrandAndCategory(brandId, categoryId, pageable);
	    } else if (brandId != null) {
	        // Filter products by brand
	        productPage = productService.getProductsByBrand(brandId, pageable);
	    } else if (categoryId != null) {
	        // Filter products by category
	        productPage = productService.getProductsByCategory(categoryId, pageable);
	    } else {
	        // Get all products if no search query or filter
	        productPage = productService.getProductPage(pageable);
	    }

	    if (productPage.isEmpty()) {
	        // No results found, add a message
	        model.addAttribute("noResultsMessage", "Không có dữ liệu");
	    }
		
		List<Category> categories = categoryService.getAllcategories();
		
		List<Brand> brands =brandService.getAllBrand();
		
		model.addAttribute("products" , productPage);
		model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    model.addAttribute("brands" , brands);
		model.addAttribute("categories" , categories);
		
		
		return "shop";
	}
}
