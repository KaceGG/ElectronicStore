package javaweb.ElectronicStore.controllers;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.oauth2.StaticClass;
import javaweb.ElectronicStore.repository.UserRepository;

@Controller
@RequestMapping
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	private StaticClass staticClass;
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepository.findByEmail(email);
			if(user == null) {
				user = new User();
				user.setLastName(staticClass.name);
				user.setEmail(staticClass.email);
			}
			m.addAttribute("user", user);
		}
		
	}
	
	 @GetMapping("/")
	 public String Index() {
	     return "index";
	 }
	 
	 
	 @GetMapping("/signin")
	   public String Signin(Model model) {
		 model.addAttribute("user", new User());
	      return "login&register";
	 }
	 
	 @GetMapping("/user/profile")
		public String userprofile() {
			return "profile";
		}
}
