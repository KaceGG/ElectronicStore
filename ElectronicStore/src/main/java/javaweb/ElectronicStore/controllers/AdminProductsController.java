package javaweb.ElectronicStore.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javaweb.ElectronicStore.models.Category;
import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.ProductImage;
import javaweb.ElectronicStore.service.BrandService;
import javaweb.ElectronicStore.service.CategoryService;
import javaweb.ElectronicStore.service.ProductService;

@Controller
@RequestMapping
public class AdminProductsController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandyService;
	
	@Autowired
    private ProductService productService;
	
	@GetMapping("/admin/product")
	public String AdminProducts(Model model, @RequestParam(defaultValue = "0") int page, 
	    @RequestParam(name = "name", required = false) String productName,
	    @RequestParam(name = "brand_id", required = false) Long brandId,
	    @RequestParam(name = "category_id", required = false) Long categoryId) {
	    int pageSize = 4; // Số lượng danh mục mỗi trang
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<Product> productPage;

	    if (productName != null && !productName.isEmpty()) {
	        // Search for products by name
	        productPage = productService.searchProductByName(productName, pageable);
	    } else if (brandId != null && categoryId != null) {
	        // Filter products by both brand and category
	        productPage = productService.getProductsByBrandAndCategory(brandId, categoryId, pageable);
	    } else if (brandId != null) {
	        // Filter products by brand
	        productPage = productService.getProductsByBrand(brandId, pageable);
	    } else if (categoryId != null) {
	        // Filter products by category
	        productPage = productService.getProductsByCategory(categoryId, pageable);
	    } else {
	        // Get all products if no search query or filter
	        productPage = productService.getProductPage(pageable);
	    }

	    if (productPage.isEmpty()) {
	        // No results found, add a message
	        model.addAttribute("noResultsMessage", "Không có dữ liệu");
	    }

	    var category = categoryService.getAllcategories();
	    var brand = brandyService.getAllBrand();

	    model.addAttribute("listcategory", category);
	    model.addAttribute("listbrand", brand);

	    model.addAttribute("productlist", productPage);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", productPage.getTotalPages());

	    return "AdminProducts";
	}

	
	
	@GetMapping("/admin/product/add")
	public String AdminProductsAdd(Model model) {
		var category = categoryService.getAllcategories();
		var brand = brandyService.getAllBrand();
		
		model.addAttribute("listcategory" , category);
		model.addAttribute("listbrand" , brand);
		
		return "AdminProductAdd";
	}
	
	@PostMapping("/admin/product/add")
	public String addProduct(
	    @RequestParam("name") String name,
	    @RequestParam("alias") String alias,
	    @RequestParam("mainImage") MultipartFile mainImage,
	    @RequestParam("category_id") Long categoryId,
	    @RequestParam("brand_id") Long brandId,
	    @RequestParam("price") float price,
	    @RequestParam("cost") float cost,
	    @RequestParam("discountPercent") float discountPercent,
	    @RequestParam("short_description") String shortDescription,
	    @RequestParam("full_description") String fullDescription,
	    @RequestParam("additionalImages") MultipartFile[] additionalImages,
	    RedirectAttributes redirectAttributes,
	    Model model) {
	    
	    Category category = categoryService.getCategoryById(categoryId);
	    Brand brand = brandyService.getBrandById(brandId);
	    
	    try {
	        Product product = new Product();
	        product.setName(name);
	        product.setAlias(alias);
	        product.setPrice(price);
	        product.setShortDescription(shortDescription);
	        product.setFullDescription(fullDescription);
	        product.setCategory(category);
	        product.setBrand(brand);
	        product.setCost(cost);
	        product.setDiscountPercent(discountPercent);
	        product.setEnabled(true);
	        
	        // Gán giá trị cho thuộc tính mainImage
	        if (!mainImage.isEmpty()) {
	            String imageName = mainImage.getOriginalFilename();
	            product.setMainImage(imageName);
	            
	            // Lưu hình ảnh sản phẩm
	            
	            String absolutePath = "src/main/resources/static/assets/img/products";
				File imageDirectory = new File(absolutePath);
				Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + mainImage.getOriginalFilename());
				Files.copy(mainImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
//	            File saveProduct = new ClassPathResource("static/assets/img/products").getFile();
//	            Path path = Paths.get(saveProduct.getAbsolutePath() + File.separator + imageName);
//	            Files.copy(mainImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        }

	        // Lưu sản phẩm vào cơ sở dữ liệu
	        Product savedProduct = productService.saveProduct(product);
	        
	        for (MultipartFile image : additionalImages) {
	            if (!image.isEmpty()) {
	                try {
	                    ProductImage productImage = new ProductImage();
	                    productImage.setName(image.getOriginalFilename());
	                    productImage.setProduct(savedProduct);
	                
	                    productService.saveProductImage(productImage);
	                    
	                    // Lưu hình ảnh sản phẩm
	                    String absolutePath = "src/main/resources/static/assets/img/products";
	    				File imageDirectory = new File(absolutePath);
	    				Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + image.getOriginalFilename());
	    				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	                    
//	                    File saveProductImage = new ClassPathResource("static/assets/img/products").getFile();
//	                    Path imagePath = Paths.get(saveProductImage.getAbsolutePath() + File.separator + image.getOriginalFilename());
//	                    Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
	    				
	                } catch (Exception e) {
	                    // Xử lý lỗi (nếu có)
	                    model.addAttribute("errorMessage", "Error adding product images: " + e.getMessage());
	                    return "AdminProductAdd";
	                }
	                
	            }
	        }
				redirectAttributes.addFlashAttribute("successMessage", "Danh mục đã được thêm thành công.");
	        // Chuyển hướng người dùng đến trang danh sách sản phẩm sau khi thêm sản phẩm thành công
	        return "redirect:/admin/product";
	    } catch (Exception e) {
	        // Nếu có lỗi, hiển thị thông báo lỗi
	        model.addAttribute("errorMessage", "Error adding the product: " + e.getMessage());
	        return "AdminProductAdd";
	    }
	    
	}

	
	
	@GetMapping("/admin/product/edit/{productId}")
	public String adminbrandedit(@PathVariable Long productId, Model model) {
	    Product product = productService.getProductById(productId);
	    
	    List<Category> categories = categoryService.getAllcategories();
	    List<Brand> brands = brandyService.getAllBrand();
	    List<ProductImage> productImages = productService.getAllProductImgById(productId);
	    

	    model.addAttribute("categories", categories);
	    model.addAttribute("brands", brands);
	    model.addAttribute("product", product);
	    model.addAttribute("productImages", productImages);
	    
	    return "AdminProductEdit";
	}
	
	
		
	@PostMapping("/admin/product/update")
	public String updateProduct(
	    @RequestParam("productId") Long productId,
	    @RequestParam("name") String name,
	    @RequestParam("alias") String alias,
	    @RequestParam("mainImage") MultipartFile mainImage,
	    @RequestParam("category_id") Long categoryId,
	    @RequestParam("brand_id") Long brandId,
	    @RequestParam("price") float price,
	    @RequestParam("cost") float cost,
	    @RequestParam("discountPercent") float discountPercent,
	    @RequestParam("short_description") String shortDescription,
	    @RequestParam("full_description") String fullDescription,
	    @RequestParam("additionalImages") MultipartFile[] additionalImages,
	    RedirectAttributes redirectAttributes,
	    Model model) {

	    Category category = categoryService.getCategoryById(categoryId);
	    Brand brand = brandyService.getBrandById(brandId);

	    try {
	        // Get the existing product by productId
	        Product product = productService.getProductById(productId);
	        
	        
	        
	        if (!mainImage.isEmpty()) {
	        	// Lấy danh sách các sản phẩm sử dụng ảnh cũ làm mainImage
	            List<Product> productsUsingOldMainImage = productService.getProductsByMainImage(product.getMainImage());

	            // Lấy danh sách các sản phẩm sử dụng ảnh cũ làm ảnh phụ
	            List<Product> productsUsingOldAdditionalImage = productService.getProductsByAdditionalImageName(product.getMainImage());

	            // Kiểm tra xem ảnh cũ có phải là ảnh chính của các sản phẩm khác hay không
	            boolean isOldMainImageUsed = productsUsingOldMainImage.size() > 1 || (productsUsingOldMainImage.size() == 1 && !productsUsingOldMainImage.get(0).getId().equals(productId));

	            // Kiểm tra xem ảnh cũ có nằm trong danh sách ảnh phụ của các sản phẩm khác hay không
	            boolean isOldMainImageUsedAsAdditionalImage = productsUsingOldAdditionalImage.size() > 0;

	            if (!isOldMainImageUsed && !isOldMainImageUsedAsAdditionalImage) {
	                // Xóa ảnh cũ khỏi folder
	                try {    	
	                	
	                	String absolutePath = "src/main/resources/static/assets/img/products";
	                	File deleteFile = new File(absolutePath);
	                	Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + product.getMainImage());
	                	Files.deleteIfExists(deletePath);
	                	
	                	
//	                    File deleteFile = new ClassPathResource("static/assets/img/products").getFile();
//	                    Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + product.getMainImage());
//	                    Files.deleteIfExists(deletePath);
	                    
	                } catch (IOException e) {
	                    // Xử lý lỗi khi xóa ảnh
	                    model.addAttribute("errorMessage", "Error deleting old main image: " + e.getMessage());
	                    return "AdminProductEdit";
	                }
	            }
	        	//xử lý lưu ảnh mới 
	            String imageName = mainImage.getOriginalFilename();
	            product.setMainImage(imageName);

	            //lưu ảnh mới vào folder
	            String absolutePath = "src/main/resources/static/assets/img/products";
				File imageDirectory = new File(absolutePath);
				Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + mainImage.getOriginalFilename());
				Files.copy(mainImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            
//	            File saveMainImage = new ClassPathResource("static/assets/img/products").getFile();
//	            Path mainImagePath = Paths.get(saveMainImage.getAbsolutePath() + File.separator + imageName);
//	            Files.copy(mainImage.getInputStream(), mainImagePath, StandardCopyOption.REPLACE_EXISTING);
	            
	            
	        }
	        
	        
	        
	        if (additionalImages != null && additionalImages.length > 0) {
	            // Lưu danh sách ảnh phụ cũ của sản phẩm
	            List<String> oldAdditionalImages = new ArrayList<>();
	            for (ProductImage oldImage : product.getImages()) {
	                oldAdditionalImages.add(oldImage.getName());
	            }

	            // Xóa tất cả ảnh phụ cũ của sản phẩm
	            product.getImages().clear();

	            for (String oldAdditionalImageName : oldAdditionalImages) {
	                // Check if the old additional image is used in any product as mainImage or additionalImage
	                boolean isOldAdditionalImageUsed = false;
	                for (Product p : productService.getAllProduct()) {
	                    if (p.getMainImage().equals(oldAdditionalImageName)) {
	                        isOldAdditionalImageUsed = true;
	                        break;
	                    }
	                    for (ProductImage additionalImage : p.getImages()) {
	                        if (additionalImage.getName().equals(oldAdditionalImageName)) {
	                            isOldAdditionalImageUsed = true;
	                            break;
	                        }
	                    }
	                }

	                if (!isOldAdditionalImageUsed) {
	                    // Delete old additional image from folder
	                    try {
	                    	
	                    	String absolutePath = "src/main/resources/static/assets/img/products";
		                	File deleteFile = new File(absolutePath);
		                	Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + oldAdditionalImageName);
		                	Files.deleteIfExists(deletePath);
	                    	
//	                        File deleteFile = new ClassPathResource("static/assets/img/products").getFile();
//	                        Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + oldAdditionalImageName);
//	                        Files.deleteIfExists(deletePath);
	                        
	                        
	                    } catch (IOException e) {
	                        // Handle error when deleting old additional image
	                        model.addAttribute("errorMessage", "Error deleting old additional image: " + e.getMessage());
	                        return "AdminProductEdit";
	                    }
	                }
	            }

	            // Tiếp tục xử lý các ảnh mới
	            for (MultipartFile additionalImage : additionalImages) {
	                if (!additionalImage.isEmpty()) {
	                    String additionalImageName = additionalImage.getOriginalFilename();

	                    // Save the new additional image
	                    try {
	                    	String absolutePath = "src/main/resources/static/assets/img/products";
	        				File imageDirectory = new File(absolutePath);
	        				Path path = Paths.get(imageDirectory.getAbsolutePath() + File.separator + additionalImageName);
	        				Files.copy(additionalImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        				
	        				
//	                        File saveAdditionalImage = new ClassPathResource("static/assets/img/products").getFile();
//	                        Path additionalImagePath = Paths.get(saveAdditionalImage.getAbsolutePath() + File.separator + additionalImageName);
//	                        Files.copy(additionalImage.getInputStream(), additionalImagePath, StandardCopyOption.REPLACE_EXISTING);

	                        // Update or save the product image information
	                        ProductImage productImage = new ProductImage();
	                        productImage.setName(additionalImageName);
	                        productImage.setProduct(product);
	                        product.getImages().add(productImage);
	                    } catch (IOException e) {
	                        // Handle error when saving new additional image
	                        model.addAttribute("errorMessage", "Error saving new additional image: " + e.getMessage());
	                        return "AdminProductEdit";
	                    }
	                }
	            }
	        }






	        


	        // Cập nhật thông tin sản phẩm
	        product.setName(name);
	        product.setAlias(alias);
	        product.setPrice(price);
	        product.setShortDescription(shortDescription);
	        product.setFullDescription(fullDescription);
	        product.setCategory(category);
	        product.setBrand(brand);
	        product.setCost(cost);
	        product.setDiscountPercent(discountPercent);
	        product.setEnabled(true);

	        // Lưu sản phẩm vào cơ sở dữ liệu
	        productService.saveProduct(product);

	        redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được cập nhật thành công.");
	        // Redirect the user to the product list page after successful update
	        return "redirect:/admin/product";
	    } catch (Exception e) {
	        // If there is an error, display an error message
	        model.addAttribute("errorMessage", "Error updating the product: " + e.getMessage());
	        return "AdminProductEdit";
	    }
	}
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/admin/product/delete/{productId}")
	public String adminproductdelete(@PathVariable Long productId, Model model) {
		Product product = productService.getProductById(productId);
		List<ProductImage> productImages = productService.getAllProductImgById(productId);
		
		model.addAttribute("product", product);
		model.addAttribute("productImages", productImages);	
		return "AdminProductDelete";
	}
	
	
	@PostMapping("/admin/product/delete/{productId}")
	public String adminproductdeleteconfim(@PathVariable Long productId, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.getProductById(productId);
				
			List<Product> productsUsingMainImage = productService.getProductsByMainImage(product.getMainImage());
			List<Product> productsUsingOldAdditionalImage = productService.getProductsByAdditionalImageName(product.getMainImage());
			
			if(productsUsingMainImage.size() <=1 && productsUsingOldAdditionalImage.isEmpty()) {
				try {
					
					String absolutePath = "src/main/resources/static/assets/img/products";
                	File deleteFile = new File(absolutePath);
                	Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + product.getMainImage());
                	Files.deleteIfExists(deletePath);
					
					
//			        File deleteFile = new ClassPathResource("static/assets/img/products").getFile();
//			        Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + product.getMainImage());
//			        Files.deleteIfExists(deletePath);
			        
			        
			    } catch (IOException e) {
			        throw new IOException("Error deleting image: " + e.getMessage());
			    }
			}
			
			for(ProductImage additionalImage : product.getImages()) {
				List<Product> productsUsingOddMainImage = productService.getProductsByMainImage(additionalImage.getName());
				List<Product> productsUsingOldAddImage = productService.getProductsByAdditionalImageName(additionalImage.getName());
				
				if(productsUsingOddMainImage.size() == 0 && productsUsingOldAddImage.size() == 1) {
					try {
						String absolutePath = "src/main/resources/static/assets/img/products";
	                	File deleteFile = new File(absolutePath);
	                	Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + additionalImage.getName());
	                	Files.deleteIfExists(deletePath);
						
//				        File deleteFile = new ClassPathResource("static/assets/img/products").getFile();
//				        Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + additionalImage.getName());
//				        Files.deleteIfExists(deletePath);
				    } catch (IOException e) {
				        throw new IOException("Error deleting image: " + e.getMessage());
				    }
				}
			}
					
			productService.deleteProduct(productId);
			redirectAttributes.addFlashAttribute("successMessage", "Danh mục đã được xóa.");
			return "redirect:/admin/product";
		}catch (Exception e) {
	        // Handle error
	        model.addAttribute("errorMessage", "Error deleting the product: " + e.getMessage());
	        return "AdminProductDelete";
	    }
		
	}
}
