package javaweb.ElectronicStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage , Integer> {
	List<ProductImage> findAllByProductId(Long productId);
	
	void deleteAllByProductId(Long productId);
	
	void deleteById(Long imageId);
	
	@Query("SELECT pi.product FROM ProductImage pi WHERE pi.name = :imageName")
    List<Product> findProductsByAdditionalImageName(@Param("imageName") String imageName);
	
	
}
