package javaweb.ElectronicStore.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import javaweb.ElectronicStore.models.ForgotPasswordToken;

@Service
public class ForgotPasswordService {
	
	@Autowired
	JavaMailSender javaMailSender;
	
    private final int MINUTES = 10;
	
	public String generateToken() {
		return UUID.randomUUID().toString();
	}
	
	public LocalDateTime expireTimeRange() {
		return LocalDateTime.now().plusMinutes(MINUTES);
	}
	
	public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String emailContent = "<p>Xin chào</p>"
				              + "Nhấp vào liên kết bên dưới để đặt lại mật khẩu"
				              + "<p><a href=\""+ emailLink + "\">Thay đổi mật khẩu</a></p>"
				              + "<br>"
				              + "Bỏ qua Email này nếu bạn không thực hiện yêu cầu";
		helper.setText(emailContent, true);
		helper.setFrom("kichhoattktest@gmail.com", "Admin");
		helper.setSubject(subject);
		helper.setTo(to);
		javaMailSender.send(message);
	}
	
	public boolean isExpired (ForgotPasswordToken forgotPasswordToken) {
		return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
	}
	
	public String checkValidity (ForgotPasswordToken forgotPasswordToken, Model model) {
		
		if (forgotPasswordToken == null) {
			model.addAttribute("error", "Mã không hợp lệ");
			return "error-page";
		}
		
		else if (forgotPasswordToken.isUsed()) {
			model.addAttribute("error", "mã thông báo đã được sử dụng");
			return "error-page";
		}
		
		else if (isExpired(forgotPasswordToken)) {
			model.addAttribute("error", "mã thông báo đã hết hạn");
			return "error-page";
		}
		else {
			return "reset-password";
		}
		
		
	}
	
}
