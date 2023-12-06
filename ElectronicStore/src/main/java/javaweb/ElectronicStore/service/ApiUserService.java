package javaweb.ElectronicStore.service;

import org.springframework.http.ResponseEntity;

import javaweb.ElectronicStore.api.payload.request.LoginRequest;
import javaweb.ElectronicStore.api.payload.request.SignupRequest;
import javaweb.ElectronicStore.api.payload.request.UpdateProfileRequest;
import javaweb.ElectronicStore.models.User;

public interface ApiUserService {
	ResponseEntity<?> getUserProfile(Long userId);
	
	ResponseEntity<?> updateUserProfile(Long userId, UpdateProfileRequest profileRequest);
	
	ResponseEntity<?> signUpApi(SignupRequest signUpRequest);
	
	ResponseEntity<?> signInApi(LoginRequest loginRequest);
	
	void sendEmail(User user, String url);
}
