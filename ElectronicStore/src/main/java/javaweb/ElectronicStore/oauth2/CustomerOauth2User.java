package javaweb.ElectronicStore.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOauth2User implements OAuth2User {

	private OAuth2User oauth2User;
	
	
	public CustomerOauth2User(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    List<GrantedAuthority> authorities = new ArrayList<>(oauth2User.getAuthorities());
	    return authorities;
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");
	}
	
	public String getFullName() {
		// TODO Auto-generated method stub
		return oauth2User.getAttribute("name");
	}
	
	
	public String getEmail() {
		return oauth2User.getAttribute("email");
	}
	
}
