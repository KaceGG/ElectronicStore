package javaweb.ElectronicStore.api.payload.response;

import java.util.Date;

public class UserProfileResponse {
	
	private String email;
	private String lastname;
	private String firstname;
	private String phone;
	private int sex;
	private Date birthday;
	private String picture;
	
	public UserProfileResponse(String email, String lastname, String firstname, String phone, int sex,
			Date birthday, String picture) {
		super();
		this.email = email;
		this.lastname = lastname;
		this.firstname = firstname;
		this.phone = phone;
		this.sex = sex;
		this.birthday = birthday;
		this.picture = picture;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
		
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
