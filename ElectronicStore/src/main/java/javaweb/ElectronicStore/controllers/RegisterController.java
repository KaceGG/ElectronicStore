package javaweb.ElectronicStore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	 @GetMapping
	    public String showRegisterPage(Model model) {
		 model.addAttribute("user", new User());
	        return "login&register";
	    }
	 
	 @PostMapping("/saveUser")
	 public String registerUser(@ModelAttribute("user") User user,HttpSession session ,HttpServletRequest request) {
//		 if (user.getFirstName() == null || user.getFirstName().isEmpty() ||
//				 user.getLastName() == null || user.getLastName().isEmpty() ||
//					user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty() ||
// 			        user.getPassword() == null || user.getPassword().isEmpty() ||
//			        user.getEmail() == null || user.getEmail().isEmpty()) {
//			        session.setAttribute("msg", "Vui lòng điền đầy đủ thông tin.");
//			        return "redirect:/register";
//			    }
//		 
		 if (user == null) {
		        // Xử lý khi user là null, có thể redirect hoặc trả về trang lỗi
		        return "redirect:/error"; // Thay đổi đường dẫn tùy theo yêu cầu của bạn
		    }
		 var abc =  userService.getEmail(user.getEmail());
		 if(abc !=  null) {
			 session.setAttribute("msg", "email đã được đăng ký , vui lòng sử dụng email khác");
			 return "redirect:/register";
		 }
		 
		 
		 String url  = request.getRequestURL().toString();
		 url = url.replace(request.getServletPath(), "");
		 User u = userService.RegisterUser(user,url);
		 if(u != null) {
			 session.setAttribute("msg", "Hãy vào Email để xác minh tài khoản");
		 }else {
			 session.setAttribute("msg", "Đăng Ký Không Thành Công");
		 }
		  
		 return "redirect:/register";
	 }
	 
	 
	 @GetMapping("/verify")
	 public String verifyAccount(@Param("code") String code, Model model) {
		 
		 System.out.println(code);
		 
		 boolean f = userService.verifyAccount(code);
		 
		 if(f) {
			 model.addAttribute("msg" ,"Bạn Đã xác thực tài khoản thành công");
		 } else {
			 model.addAttribute("msg" ,"Tài khoản đã được xác thực");
		 }
		 
		 return "registermessage";
	 }
	 
}
