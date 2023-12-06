package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javaweb.ElectronicStore.models.Brand;


public interface BrandService {
	
	Brand getBrandByName(String name);


	Brand saveBrand(Brand brand);
	
	List<Brand> getAllBrand();
	
	Brand getBrandById(Long brandId);
	
	void deleteBrand(Long brandId);
	
	Page<Brand> getBrandPage(Pageable pageable);
	
	Page<Brand> searchBrandByName(String name, Pageable pageable);
	
}
