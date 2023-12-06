package javaweb.ElectronicStore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.models.UserDTO;
import javaweb.ElectronicStore.service.UserService;


//@RestController
//@RequestMapping("/auth")
//@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private UserService userService;
    
//    @Autowired
//    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
    	
    	User existingUser = userService.getEmail(userDTO.getEmail());
    	if(existingUser !=  null) {
//            return ResponseEntity.ok(existingUser.getEmail());
    		return ResponseEntity.badRequest().body("a");
    	}else {
    		User user = new User();
	        user.setPhoneNumber(userDTO.getPhoneNumber());
	        user.setPassword(userDTO.getPassword());
	        user.setEmail(userDTO.getEmail());
	        user.setFirstName(userDTO.getFirstName());
	        user.setLastName(userDTO.getLastName());
	        user.setRoles(userDTO.getRoles());
	        User registeredUser = userService.RegisterUser(user, "http://localhost:8070");
	
	        if (registeredUser != null) {
	            // Trả về trường verfivationcode của đối tượng registeredUser
	            return ResponseEntity.ok(registeredUser.getVerfivationcode());
	        } else {
	            return ResponseEntity.badRequest().body("Registration failed");
	        }
    	}
        
    }
    
//    @PostMapping("/login")
//    public LoginResponseDTO loginUser(@RequestBody UserDTO body){
//        return authenticationService.loginUser(body.getEmail(), body.getPassword());
//    }
}   