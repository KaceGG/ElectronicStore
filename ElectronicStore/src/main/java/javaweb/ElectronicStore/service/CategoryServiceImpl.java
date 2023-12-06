package javaweb.ElectronicStore.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.Category;
import javaweb.ElectronicStore.repository.CategoryRepository;


@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllcategories() {	
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId).orElse(null);
	}

	@Override
	public void deleteCategory(Long categoryId) {
		categoryRepository.deleteById(categoryId);
		
	}

	@Override
	public Page<Category> getCategoriesPage(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	@Override
	public Page<Category> searchCategoriesByName(String name, Pageable pageable) {
		return categoryRepository.searchCategoriesByName(name, pageable);
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.getCategoryByName(name);
	}

	@Override
	public Category getCategoryByAlias(String alias) {
		return categoryRepository.getCategoryByAlias(alias);
	}



 
}
