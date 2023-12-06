package javaweb.ElectronicStore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaweb.ElectronicStore.models.Brand;


public interface BrandRepository extends JpaRepository<Brand,Long> {
	Brand getBrandByName(String name);
	
	@Query("SELECT c FROM Brand c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%'))")
	Page<Brand> searchBrandByName(@Param("name") String name, Pageable pageable);
}
