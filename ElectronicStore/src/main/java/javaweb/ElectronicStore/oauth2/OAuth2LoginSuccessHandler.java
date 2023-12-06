package javaweb.ElectronicStore.oauth2;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import javaweb.ElectronicStore.models.Role;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.RoleRepository;
import javaweb.ElectronicStore.service.UserService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private UserService userService; // Service để quản lý người dùng

    @Autowired
    private RoleRepository roleRepository; // Repository để quản lý vai trò (role)
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    
    private StaticClass staticClass;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
			CustomerOauth2User oauth2User = (CustomerOauth2User) authentication.getPrincipal();
			
			// Lấy thông tin từ OAuth2
			String email = oauth2User.getEmail();
			staticClass.email = email;
			String name = oauth2User.getName();
			staticClass.name = name;
			
			// Kiểm tra xem người dùng có tồn tại trong hệ thống không
			User user = userService.getEmail(email);
			if (user == null) {
			// Nếu người dùng chưa tồn tại, tạo mới và gán quyền hạn USER
			user = new User();
			user.setEmail(email);
			user.setFirstName(name);
			user.setLastName(name);
			
			// Tạo mật khẩu mặc định hoặc có thể sử dụng mã hóa ngẫu nhiên
			String defaultPassword = "password123"; // Mật khẩu mặc định
			String encodedPassword = passwordEncoder.encode(defaultPassword); // Mã hóa mật khẩu
			
			user.setPassword(encodedPassword);
			
			user.setPhoneNumber("0123456789");
			
			user.setEnabled(true);
			
			Role userRole = roleRepository.findByName("USER");
			user.setRoles(new HashSet<>(Collections.singletonList(userRole))); // Gán quyền USER
			userService.saveUser(user);
			}
			
			super.onAuthenticationSuccess(request, response, authentication);
			}
}