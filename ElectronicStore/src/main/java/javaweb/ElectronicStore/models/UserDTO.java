package javaweb.ElectronicStore.models;

import java.util.Set;


public class UserDTO {
	private String phoneNumber;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    // Các trường khác nếu cần thiết
    
    // Constructors, Getters, Setters
    

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    // Các getters và setters cho các trường khác nếu cần
}
