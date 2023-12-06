package javaweb.ElectronicStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javaweb.ElectronicStore.models.Brand;
import javaweb.ElectronicStore.models.Category;
import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.Role;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.BrandRepository;
import javaweb.ElectronicStore.repository.CategoryRepository;
import javaweb.ElectronicStore.repository.ProductRepository;
import javaweb.ElectronicStore.repository.RoleRepository;
import javaweb.ElectronicStore.repository.UserRepository;


@SpringBootApplication
public class ElectronicStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository,
							BrandRepository brandRepository, CategoryRepository categoryRepository,
							ProductRepository productRepository,PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByName("ADMIN") != null) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			User admin = new User((long) 1, "1", passwordEncode.encode("admin"), "admin@gmail.com", "admin", "admin", roles, true, new Date());
			userRepository.save(admin);
			
			List<Brand> brands = new ArrayList<>();
			Brand brand1 = new Brand(1, "NOKIA", "brand1.png");
	        Brand brand2 = new Brand(2, "SAMSUNG", "brand2.png");
			
	        brands.add(brand1);
	        brands.add(brand2);
	        
	        brandRepository.saveAll(brands);
	        
	        List<Category> categories = new ArrayList<>();
	        Category category1 = new Category(1, "Điện Thoại", "ĐT", "dt.png", true);
	        Category category2 = new Category(2, "Máy Tính", "MT", "mt.png", true);
			
	        categories.add(category1);
	        categories.add(category2);
	        
	        categoryRepository.saveAll(categories);

	        List<Product> products = new ArrayList<>();
	        Product product1 = new Product(1, "tai nghe", "TN", "sản phẩm 1", "sản phẩm 1 khá ok", new Date(), new Date(), true, 1, 1, 1, "product-1.jpg", category1, brand1);
	        Product product2 = new Product(2, "bàn phím", "BP", "sản phẩm 2", "sản phẩm 2 khá ok", new Date(), new Date(), true, 2, 2, 2, "product-2.jpg", category2, brand2);
	        Product product3 = new Product(3, "bàn phím không dây", "BPKD", "sản phẩm 3", "sản phẩm 3 khá ok", new Date(), new Date(), true, 3, 3, 3, "product-3.jpg", category1, brand1);
	        Product product4 = new Product(4, "con chuột", "cc", "sản phẩm 4", "sản phẩm 4 khá ok", new Date(), new Date(), true, 4, 4, 4, "product-4.jpg", category2, brand2);
	        Product product5 = new Product(5, "cục sạc máy tính", "csmt", "sản phẩm 5", "sản phẩm 5 khá ok", new Date(), new Date(), true, 5, 5, 5, "product-5.jpg", category1, brand1);
	        Product product6 = new Product(6, "cục sạc điện thoại", "csdt", "sản phẩm 6", "sản phẩm 6 khá ok", new Date(), new Date(), true, 6, 6, 6, "product-1.jpg", category2, brand2);
	        Product product7 = new Product(7, "dây cắm", "dc", "sản phẩm 7", "sản phẩm 7 khá ok", new Date(), new Date(), true, 7, 7, 7, "product-2.jpg", category1, brand1);
	        Product product8 = new Product(8, "con chuột2", "cc2", "sản phẩm 8", "sản phẩm 8 khá ok", new Date(), new Date(), true, 8, 8, 8, "product-3.jpg", category2, brand2);
	        Product product9 = new Product(9, "con chuột3", "cc3", "sản phẩm 9", "sản phẩm 9 khá ok", new Date(), new Date(), true, 9, 9, 9, "product-4.jpg", category1, brand1);
	        Product product10 = new Product(10, "con chuột4", "cc4", "sản phẩm 10", "sản phẩm 10 khá ok", new Date(), new Date(), true, 10, 10, 10, "product-5.jpg", category2, brand2);
	        products.add(product1);
	        products.add(product2);
	        products.add(product3);
	        products.add(product4);
	        products.add(product5);
	        products.add(product6);
	        products.add(product7);
	        products.add(product8);
	        products.add(product9);
	        products.add(product10);
	        
	        productRepository.saveAll(products);
	        
		};
	}

}
