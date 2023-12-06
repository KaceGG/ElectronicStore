package javaweb.ElectronicStore.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javaweb.ElectronicStore.models.Category;
import javaweb.ElectronicStore.service.CategoryService;

@Controller
@RequestMapping
public class AdminCategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/admin/category")
	public String adminCategories(Model model, @RequestParam(defaultValue = "0") int page,@RequestParam(name = "name", required = false) String categoryName ) {
	    int pageSize = 4; // Số lượng danh mục mỗi trang
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<Category> categoryPage;

	    if (categoryName != null && !categoryName.isEmpty()) {
	        // Search for categories by name
	        categoryPage = categoryService.searchCategoriesByName(categoryName, pageable);
	    } else {
	        // Get all categories if no search query
	        categoryPage = categoryService.getCategoriesPage(pageable);
	    }
	    
	    if (categoryPage.isEmpty()) {
	        // No results found, add a message
	        model.addAttribute("noResultsMessage", "Không có dữ liệu");
	    }
	    
	    	

	    model.addAttribute("categories", categoryPage);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", categoryPage.getTotalPages());


	    return "AdminCategory";
	}


	
	@GetMapping("/admin/category/add")
	public String admincategoryadd() {
		return "AdminCategoryAdd";
	}
	
	@PostMapping("/admin/category/add")
	public String addCategory(@RequestParam MultipartFile image , @RequestParam String name,@RequestParam String alias,RedirectAttributes redirectAttributes, Model model) {
		
		if (image.isEmpty() || name.isEmpty() || alias.isEmpty()) {
	        model.addAttribute("errorMessage", "Hãy chọn file & nhập thông tin đầy đủ.");
	        return "AdminCategoryAdd";
	    } else {
	    	Category existingCategoryName = categoryService.getCategoryByName(name);
	    	Category existingCategoryAlias = categoryService.getCategoryByName(alias);
	    	if(existingCategoryName !=null || existingCategoryAlias!=null) {
	    		model.addAttribute("errorMessage", "Danh mục đã tồn tại : name hoặc alias đã trùng.");
	            return "AdminCategoryAdd";
	    	}
	    	Category category = new Category();
			category.setImage(image.getOriginalFilename());
			
			category.setName(name);
			category.setAlias(alias);
			
			Category upload = categoryService.saveCategory(category);
			if(upload!=null) {
				try {
					
					String absolutePath = "src/main/resources/static/assets/img/category";
					File imageDirectory = new File(absolutePath);
					Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + image.getOriginalFilename());
					Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
//					File saveCategory = new ClassPathResource("static/assets/img/category").getFile();
//					
//					Path path = Paths.get(saveCategory.getAbsolutePath()+File.separator+image.getOriginalFilename());
//					System.out.println(path);
//					
//					Files.copy(image.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//					
				} catch(Exception e) {
					e.printStackTrace();
				}
				redirectAttributes.addFlashAttribute("successMessage", "Danh mục đã được thêm thành công.");
			}
			
	    }
		
		return "redirect:/admin/category";
	}	
	
	
	@GetMapping("/admin/category/edit/{categoryId}")
	public String admincategoryedit(@PathVariable Long categoryId, Model model) {
		
		
	    Category category = categoryService.getCategoryById(categoryId);
	    model.addAttribute("category", category);
	    return "AdminCategoryEdit";
	}
	
		
	@PostMapping("/admin/category/update")
	public String updateCategory(@RequestParam MultipartFile image, @RequestParam String name, @RequestParam String alias, @RequestParam Long categoryId,RedirectAttributes redirectAttributes,Model model) {
	    Category category = categoryService.getCategoryById(categoryId);

	    if (category != null) {
	        category.setName(name);
	        category.setAlias(alias);

	        if (image != null && !image.isEmpty()) {
	            category.setImage(image.getOriginalFilename());

	            try {
	                File saveCategory = new ClassPathResource("static/assets/img/category").getFile();
	                Path path = Paths.get(saveCategory.getAbsolutePath() + File.separator + image.getOriginalFilename());
	                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            
	        }
	        categoryService.saveCategory(category);
	        redirectAttributes.addFlashAttribute("successMessage", "Danh mục đã được chỉnh sửa thành công.");
	    }

	    return "redirect:/admin/category";
	}
	
	
	@GetMapping("/admin/category/delete/{categoryId}")
	public String adminCategoryDelete(@PathVariable Long categoryId, Model model) {
	    Category category = categoryService.getCategoryById(categoryId);
	    model.addAttribute("category", category);
	    return "AdminCategoryDelete";
	}
	
	@PostMapping("/admin/category/delete/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId,RedirectAttributes redirectAttributes) {
	    categoryService.deleteCategory(categoryId);
	    redirectAttributes.addFlashAttribute("successMessage", "Danh mục đã được xóa.");
	    return "redirect:/admin/category";
	}
	

}
