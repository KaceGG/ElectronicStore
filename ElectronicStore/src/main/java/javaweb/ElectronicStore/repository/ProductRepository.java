package javaweb.ElectronicStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javaweb.ElectronicStore.models.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
	@Query("SELECT c FROM Product c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%'))")
	Page<Product> searchProductByName(@Param("name") String name, Pageable pageable);
	
	Page<Product> findByBrandIdAndCategoryId(Long brandId, Long categoryId, Pageable pageable);

    Page<Product> findByBrandId(Long brandId, Pageable pageable);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.images i WHERE i.id = :imageId")
    List<Product> getProductsByImage(@Param("imageId") Integer imageId);
    
    @Query("SELECT p FROM Product p WHERE p.mainImage = :mainImage")
    List<Product> findProductsByMainImage(@Param("mainImage") String mainImage);
    
    Page<Product> findAll(Pageable pageable);

}
