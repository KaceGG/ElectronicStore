package javaweb.ElectronicStore.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import javaweb.ElectronicStore.api.payload.request.LoginRequest;
import javaweb.ElectronicStore.api.payload.request.SignupRequest;
import javaweb.ElectronicStore.service.ApiUserService;
import javaweb.ElectronicStore.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private ApiUserService apiUserService;
  @Autowired private UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	  	return apiUserService.signInApi(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    return apiUserService.signUpApi(signUpRequest);
  }
  
  @PostMapping("/verify")
  public ResponseEntity<?> verifyAccount(@RequestBody String code){
	  boolean result = userService.verifyAccount(code);
	  if(result) {
		  return ResponseEntity.ok().build();
	  }
	  else {
		  return ResponseEntity.badRequest().build();
	  }
  }
}
