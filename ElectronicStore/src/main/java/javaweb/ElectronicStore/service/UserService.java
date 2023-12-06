package javaweb.ElectronicStore.service;

import java.util.List;
import javaweb.ElectronicStore.models.User;

public interface UserService {
	
	User saveUser(User user);
	
	User getEmail(String email);

	List<User> getAllUser();
	
	User getUserId(Long userId);
	
	public User RegisterUser(User user, String url);
	
	public void removeMessage();
	
	public void sendEmail(User user, String path);
	
	public 	boolean verifyAccount(String verifycationCode);
}
