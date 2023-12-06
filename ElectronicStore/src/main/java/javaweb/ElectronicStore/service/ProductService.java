package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.ProductImage;

public interface ProductService {
	
	Product saveProduct(Product product);
	
	List<Product> getAllProduct();
	
	Product getProductById(Long productId);
	
	
	Page<Product> getProductPage(Pageable pageable);
	
	Page<Product> searchProductByName(String name, Pageable pageable);
	
	void deleteProduct(Long productId);
	
	
	Page<Product> getProductsByBrandAndCategory(Long brandId, Long categoryId, Pageable pageable);

    Page<Product> getProductsByBrand(Long brandId, Pageable pageable);

    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);
	
	
	
	ProductImage saveProductImage(ProductImage productImage);
	
	List<ProductImage> getAllProductImgById(Long productid);
	
	void deleteProductImg(Long productId);
	
	void deleteProductImage(Integer integer);
	
	List<Product> getProductsByImage(Integer imageId);

	List<Product> getProductsByMainImage(String mainImage);

	List<Product> getProductsByAdditionalImageName(String imageName);
	
}
