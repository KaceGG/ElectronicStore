package javaweb.ElectronicStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javaweb.ElectronicStore.models.Brand;
import javaweb.ElectronicStore.repository.BrandRepository;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private BrandRepository brandRepository;

	@Override
	public Brand saveBrand(Brand brand) {
		return brandRepository.save(brand);
	}

	@Override
	public Brand getBrandByName(String name) {
		return brandRepository.getBrandByName(name);
	}

	@Override
	public List<Brand> getAllBrand() {
		return brandRepository.findAll();
	}

	@Override
	public Brand getBrandById(Long brandId) {
		return brandRepository.findById(brandId).orElse(null);
	}

	@Override
	public void deleteBrand(Long brandId) {
		brandRepository.deleteById(brandId);
	}

	@Override
	public Page<Brand> getBrandPage(Pageable pageable) {
		return brandRepository.findAll(pageable);
	}

	@Override
	public Page<Brand> searchBrandByName(String name, Pageable pageable) {
		return brandRepository.searchBrandByName(name, pageable);
	}

}
