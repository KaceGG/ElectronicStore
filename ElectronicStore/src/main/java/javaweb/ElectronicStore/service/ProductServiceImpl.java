	package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.Product;
import javaweb.ElectronicStore.models.ProductImage;
import javaweb.ElectronicStore.repository.ProductImageRepository;
import javaweb.ElectronicStore.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
    private ProductImageRepository productImageRepository;

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProduct() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public ProductImage saveProductImage(ProductImage productImage) {
		// TODO Auto-generated method stub
		return productImageRepository.save(productImage);
	}

	@Override
	public Page<Product> getProductPage(Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> searchProductByName(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.searchProductByName(name, pageable);
	}

	@Override
	public Product getProductById(Long productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public List<ProductImage> getAllProductImgById(Long productid) {
		return productImageRepository.findAllByProductId(productid);
	}

	@Override
	public void deleteProduct(Long productId) {
		productRepository.deleteById(productId);
	}

	@Override
	public void deleteProductImg(Long productId) {
		productImageRepository.deleteAllByProductId(productId);
		
	}

	@Override
	public void deleteProductImage(Integer imageId) {
		productImageRepository.deleteById(imageId);
		
	}

	@Override
	public Page<Product> getProductsByBrandAndCategory(Long brandId, Long categoryId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandIdAndCategoryId(brandId, categoryId, pageable);
	}

	@Override
	public Page<Product> getProductsByBrand(Long brandId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandId(brandId, pageable);
	}

	@Override
	public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryId(categoryId, pageable);
	}
	@Override
	public List<Product> getProductsByImage(Integer imageId) {
        return productRepository.getProductsByImage(imageId);
    }
	@Override
	public List<Product> getProductsByMainImage(String mainImage) {
        return productRepository.findProductsByMainImage(mainImage);
    }
	@Override
    public List<Product> getProductsByAdditionalImageName(String imageName) {
        return productImageRepository.findProductsByAdditionalImageName(imageName);
    }


}
