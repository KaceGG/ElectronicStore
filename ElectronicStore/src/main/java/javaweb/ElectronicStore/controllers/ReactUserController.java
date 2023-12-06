package javaweb.ElectronicStore.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class ReactUserController {
	 
	@GetMapping("/user")
	public String user() {
		return "trang nguoi dung";
	}
}
