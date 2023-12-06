package javaweb.ElectronicStore.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import javaweb.ElectronicStore.api.payload.request.LoginRequest;
import javaweb.ElectronicStore.api.payload.request.SignupRequest;
import javaweb.ElectronicStore.api.payload.request.UpdateProfileRequest;
import javaweb.ElectronicStore.api.payload.response.JwtResponse;
import javaweb.ElectronicStore.api.payload.response.MessageResponse;
import javaweb.ElectronicStore.api.payload.response.UserProfileResponse;
import javaweb.ElectronicStore.api.security.jwt.JwtUtils;
import javaweb.ElectronicStore.config.CustomUser;
import javaweb.ElectronicStore.models.Role;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.RoleRepository;
import javaweb.ElectronicStore.repository.UserRepository;

@Service
public class ApiUserServiceImpl implements ApiUserService{
	@Autowired private UserRepository userRepository;
	@Autowired private RoleRepository roleRepository;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private JwtUtils jwtUtils;
	@Autowired private PasswordEncoder encoder;
	
	@Autowired private JavaMailSender mailSender;
	
	@Override
	public ResponseEntity<?> getUserProfile(Long userId) {
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
		
		return ResponseEntity.ok(new UserProfileResponse(
									user.getEmail(),
									user.getLastName(), 
									user.getFirstName(),
									user.getPhoneNumber(),
									user.getSex(),
									user.getBirthday(), 
									user.getPicture()));
	}	
	
	@Override
	public ResponseEntity<?> updateUserProfile(Long userId, UpdateProfileRequest profileRequest) {
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with Id: " + userId));
		user.setFirstName(profileRequest.getFirstname());
		user.setLastName(profileRequest.getLastname());
		user.setBirthday(profileRequest.getBirthday());
		user.setPhoneNumber(profileRequest.getPhone());
		user.setSex(profileRequest.getSex());
		user.setPicture(profileRequest.getPicture());
		userRepository.save(user);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<?> signInApi(LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
			        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    String jwt = jwtUtils.generateJwtToken(authentication);
		    
		    CustomUser userDetails = (CustomUser) authentication.getPrincipal();    
		    List<String> roles = userDetails.getAuthorities().stream()
		        .map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		    
		    return ResponseEntity.ok(new JwtResponse(jwt, 
		                         userDetails.getId(), 
		                         userDetails.getUsername(),
		                         roles));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Tên tài khoản hoặc mật khẩu không chính xác");
		}
	}

	@Override
	public ResponseEntity<?> signUpApi(SignupRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity
	          .badRequest()
	          .body("Email đã được sử dụng");
	    }
	    try {
	    	User user = new User();
	    	user.setEmail(signUpRequest.getEmail());
	    	user.setPhoneNumber(signUpRequest.getPhone());
	    	user.setFirstName(signUpRequest.getFirstName());
	    	user.setPassword(encoder.encode(signUpRequest.getPassword()));
	    	user.setCreatedTime(new Date());

		    Set<Role> roles = new HashSet<>();
		    Role userRole = roleRepository.findByName("USER");
	        roles.add(userRole);
		    user.setRoles(roles);
		    
		    user.setEnabled(false);
	        user.setVerfivationcode(UUID.randomUUID().toString());
			userRepository.save(user);
			sendEmail(user , signUpRequest.getUrl());
		    return ResponseEntity.ok("Đăng ký thành công, vui lòng kiểm tra email!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Có lỗi xảy ra, vui lòng thử lại sau!");
		}
	}
	
	@Override
	public void sendEmail(User user, String url) {
		String from = "kichhoattktest@gmail.com";
		String to = user.getEmail();
		String subject = "Xác thực tài khoản";
		String content = "Chào [[name]],<br>" + "Vui lòng nhấn vào link dưới đây để kích hoạt tài khoản của bạn:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_blank\">Xác thực ngay</a></h3>" + "Cảm ơn bạn,<br>" + "Đội ngũ Admin!";
		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Admin");
			helper.setTo(to);
			helper.setSubject(subject);

			if(user.getFirstName() != null) {
				content = content.replace("[[name]]", user.getFirstName());
			}
			else {
				content = content.replace("[[name]]", "bạn");
			}
			String siteUrl = url + "/verify?code=" + user.getVerfivationcode();
			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
