package javaweb.ElectronicStore.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import javaweb.ElectronicStore.models.ForgotPasswordToken;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.ForgotPasswordRepository;
import javaweb.ElectronicStore.service.ForgotPasswordService;
import javaweb.ElectronicStore.service.UserService;


@Controller
@RequestMapping
public class ForgotPasswordController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@Autowired
	ForgotPasswordRepository forgotPasswordRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/password-request")
	public String passwordRequest() {
	
		return "password-request";
	}
		
	@PostMapping("/password-request")
	public String savePasswordRequest(@RequestParam("email") String email, Model model) {
		User user = userService.getEmail(email);
		if (user == null) {
			model.addAttribute("error", "Email này chưa được đăng ký");
			return "password-request";
		}
		
		ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
		forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
		forgotPasswordToken.setToken(forgotPasswordService.generateToken());
		forgotPasswordToken.setUser(user);
		forgotPasswordToken.setUsed(false);
		
		forgotPasswordRepository.save(forgotPasswordToken);
		
		String emailLink = "http://localhost:8070/reset-password?token=" + forgotPasswordToken.getToken();
		
		try {
			forgotPasswordService.sendEmail(user.getEmail(), "Password Reset Link", emailLink);
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Lỗi khi gửi email");
			return "password-request";
		}
		
		
		return "redirect:/password-request?success";
	}
	
	@GetMapping("/reset-password")
	public String resetPassword(@Param(value="token") String token, Model model, HttpSession session) {
		
		session.setAttribute("token", token);
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		return forgotPasswordService.checkValidity(forgotPasswordToken, model);
		
	}
	
	@PostMapping("/reset-password")
	public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
		String password = request.getParameter("password");
		String token = (String)session.getAttribute("token");
		
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		User user = forgotPasswordToken.getUser();
		user.setPassword(passwordEncoder.encode(password));
		forgotPasswordToken.setUsed(true);
		userService.saveUser(user);
		forgotPasswordRepository.save(forgotPasswordToken);
		
		model.addAttribute("message", "Bạn đã lấy lại mật khẩu thành công");
		
		return "reset-password";
	}

}
