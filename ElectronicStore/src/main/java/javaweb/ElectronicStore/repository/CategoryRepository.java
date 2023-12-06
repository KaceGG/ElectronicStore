package javaweb.ElectronicStore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaweb.ElectronicStore.models.Category;


public interface CategoryRepository extends JpaRepository<Category,Long> {
	Category getCategoryByName(String name);
	Category getCategoryByAlias(String alias);

	@Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%'))")
	Page<Category> searchCategoriesByName(@Param("name") String name, Pageable pageable);
}
