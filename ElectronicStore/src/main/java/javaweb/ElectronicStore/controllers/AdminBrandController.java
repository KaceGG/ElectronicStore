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

import javaweb.ElectronicStore.models.Brand;
import javaweb.ElectronicStore.service.BrandService;

@Controller
@RequestMapping
public class AdminBrandController {
	
	@Autowired
	private BrandService brandService;
	
	
	@GetMapping("/admin/brand")
	public String adminBrand(Model model, @RequestParam(defaultValue = "0") int page,@RequestParam(name = "name", required = false) String brandName) {
	    int pageSize = 4; // Số lượng danh mục mỗi trang
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<Brand> brandPage;

	    if (brandName != null && !brandName.isEmpty()) {
	        // Search for categories by name
	    	brandPage = brandService.searchBrandByName(brandName, pageable);
	    } else {
	        // Get all categories if no search query
	    	brandPage = brandService.getBrandPage(pageable);
	    }
	    
	    if (brandPage.isEmpty()) {
	        // No results found, add a message
	        model.addAttribute("noResultsMessage", "Không có dữ liệu");
	    }
	    
	    	

	    model.addAttribute("brandlist", brandPage);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", brandPage.getTotalPages());


	    return "AdminBrand";
	}
	
	
	@GetMapping("/admin/brand/add")
	public String adminBrandAdd() {
		return "AdminBrandAdd";
	}
	
	@PostMapping("/admin/brand/add")
	public String addBrand(@RequestParam MultipartFile logo , @RequestParam String name,RedirectAttributes redirectAttributes, Model model) {
		
		if (logo.isEmpty() || name.isEmpty()) {
	        model.addAttribute("errorMessage", "Hãy chọn file & nhập thông tin đầy đủ.");
	        return "AdminBrandAdd";
	    } else {
	    	Brand brand = new Brand();
	    	brand.setLogo(logo.getOriginalFilename());
			
	    	brand.setName(name);
			
			Brand upload = brandService.saveBrand(brand);
			if(upload!=null) {
				try {
					String absolutePath = "src/main/resources/static/assets/img/brand";
					File imageDirectory = new File(absolutePath);
					Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + logo.getOriginalFilename());
					Files.copy(logo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					
//					File saveBrand = new ClassPathResource("static/assets/img/brand").getFile();	
//					Path path = Paths.get(saveBrand.getAbsolutePath()+File.separator+logo.getOriginalFilename());					
//					Files.copy(logo.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				redirectAttributes.addFlashAttribute("successMessage", "Thương hiệu đã được thêm thành công.");
			}
			
	    }
		
		return "redirect:/admin/brand";
	}	
	
	
	@GetMapping("/admin/brand/edit/{brandId}")
	public String adminbrandedit(@PathVariable Long brandId, Model model) {
	    Brand brand = brandService.getBrandById(brandId);
	    model.addAttribute("brand", brand);
	    return "AdminBrandEdit";
	}
	
	
	@PostMapping("/admin/brand/update")
	public String updateCategory(@RequestParam MultipartFile logo, @RequestParam String name, @RequestParam Long brandId,RedirectAttributes redirectAttributes,Model model) {
	    Brand brand = brandService.getBrandById(brandId);

	    if (brand != null) {
	    	brand.setName(name);

	        if (logo != null && !logo.isEmpty()) {
	        	brand.setLogo(logo.getOriginalFilename());

	            try {
	                File saveBrand = new ClassPathResource("static/assets/img/brand").getFile();
	                Path path = Paths.get(saveBrand.getAbsolutePath() + File.separator + logo.getOriginalFilename());
	                Files.copy(logo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            
	        }
	        brandService.saveBrand(brand);
	        redirectAttributes.addFlashAttribute("successMessage", "Thương Hiệu đã được chỉnh sửa thành công.");
	    }

	    return "redirect:/admin/brand";
	}
	
	
	@GetMapping("/admin/brand/delete/{brandId}")
	public String adminCategoryDelete(@PathVariable Long brandId, Model model) {
	    Brand brand = brandService.getBrandById(brandId);
	    model.addAttribute("brand", brand);
	    return "AdminBrandDelete";
	}
	
	@PostMapping("/admin/brand/delete/{brandId}")
	public String deleteCategory(@PathVariable Long brandId,RedirectAttributes redirectAttributes) {
		brandService.deleteBrand(brandId);
	    redirectAttributes.addFlashAttribute("successMessage", "Thương hiệu đã được xóa.");
	    return "redirect:/admin/brand";
	}
	
	
}
