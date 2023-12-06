package javaweb.ElectronicStore.service;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import javaweb.ElectronicStore.models.Role;
import javaweb.ElectronicStore.models.User;
import javaweb.ElectronicStore.repository.RoleRepository;
import javaweb.ElectronicStore.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public User RegisterUser(User user, String url) {
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		
		Role userRole = roleRepository.findByName("USER");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        
        user.setEnabled(false);
        
        user.setVerfivationcode(UUID.randomUUID().toString());
		
		User newuser = userRepository.save(user);
		
		
		if(newuser != null) {
			sendEmail(newuser , url);
		}
		return newuser;
	}

	@Override
	public void removeMessage() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");
		
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User getUserId(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	
	@Override
	public void sendEmail(User user, String url) {
		String from = "kichhoattktest@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Admin";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Admin");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getLastName());
			String siteUrl = url + "/register/verify?code=" + user.getVerfivationcode();

			System.out.println(siteUrl);

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean verifyAccount(String verifycationCode) {	
		User user = userRepository.findByVerfivationcode(verifycationCode);
		if(user == null) {
			return false;
		}else {
			user.setEnabled(true);
			user.setVerfivationcode(null);
			userRepository.save(user);
			
			return true;
		}
	}
}
