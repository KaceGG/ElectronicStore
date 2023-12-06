package javaweb.ElectronicStore.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import javaweb.ElectronicStore.api.payload.request.UpdateProfileRequest;
import javaweb.ElectronicStore.config.CustomUser;
import javaweb.ElectronicStore.service.ApiUserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
	@Autowired private ApiUserService userService;

	@GetMapping("/getProfile")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUser userDetails){
		return userService.getUserProfile(userDetails.getId());
	}
	
	@PutMapping("/updateProfile")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUser userDetails, @Valid @RequestBody UpdateProfileRequest profileRequest){
		return userService.updateUserProfile(userDetails.getId(), profileRequest);
		
	}
	
//	@PutMapping("/updateProfile")
//	@PreAuthorize("hasAuthority('USER')")
//	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("file") MultipartFile file){
//		try {
//            File convertedFile = new File("/img/user/"+ userDetails.getId() + "/" + file.getOriginalFilename());
//            FileOutputStream fos = new FileOutputStream(convertedFile);
//            fos.write(file.getBytes());
//            fos.close();
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//        	return ResponseEntity.badRequest().build();
//        }
//		
//	}
}
