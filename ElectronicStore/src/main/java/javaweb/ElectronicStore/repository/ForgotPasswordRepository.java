package javaweb.ElectronicStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javaweb.ElectronicStore.models.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken ,Long> {
	ForgotPasswordToken findByToken(String token);
}
