package javaweb.ElectronicStore.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javaweb.ElectronicStore.models.Category;

public interface CategoryService {
	Category getCategoryByName(String name);
	
	Category getCategoryByAlias(String alias);

	Category saveCategory(Category category);
	
	List<Category> getAllcategories();
	
	Category getCategoryById(Long categoryId);
	
	void deleteCategory(Long categoryId);
	
	Page<Category> getCategoriesPage(Pageable pageable);
	
	Page<Category> searchCategoriesByName(String name, Pageable pageable);
}
