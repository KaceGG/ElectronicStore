package javaweb.ElectronicStore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.service.UserService;

@Controller
@RequestMapping
public class AdminProfileUserController {
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/admin/profileUser")
	public String AdminProfileUser(Model model){
		
		List<User> listUser = userService.getAllUser();
		
		model.addAttribute("listUser", listUser);
	
		return "AdminProfileUser";
	}
	
	
	@GetMapping("/admin/profileUser/detail/{userId}")
	public String AdminProfileUserDetail(@PathVariable Long userId, Model model){
		
		User user = userService.getUserId(userId);
		
		model.addAttribute("user" , user);
		
		return "AdminProfileUserDetail";
	}
	
	
	
	@PostMapping("/admin/profileUser/detail/disabled")
	public String AdminProfileUserDetailDisabled(@RequestParam("userId") Long userId , RedirectAttributes redirectAttributes) {
		User user = userService.getUserId(userId);
		
		user.setEnabled(false);
		
		userService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("successMessage", "Vô hiệu hóa tài khoản thành công.");
		
		return "redirect:/admin/profileUser";
	}
	
	
	@PostMapping("/admin/profileUser/detail/active")
	public String AdminProfileUserDetailActive(@RequestParam("userId") Long userId , RedirectAttributes redirectAttributes) {
		User user = userService.getUserId(userId);
		
		user.setEnabled(true);
		
		userService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("successMessage", "Đã kích hoạt tài khoản.");
		
		return "redirect:/admin/profileUser";
	}
}
