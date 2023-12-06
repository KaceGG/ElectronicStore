package javaweb.ElectronicStore.api.payload.request;

import java.util.Date;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {	
	
	@Size(max = 30)
	private String lastname;
	
	@Size(max = 30)
	private String firstname;
	
	@Size(max = 10)
	private String phone;
	
	private int sex;
	
	private Date birthday;
	
	private String picture;
	
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
