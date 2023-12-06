package javaweb.ElectronicStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javaweb.ElectronicStore.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
	public User findByVerfivationcode(String code);
	
	Boolean existsByEmail(String email);
	
}
