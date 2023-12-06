package javaweb.ElectronicStore.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import javaweb.ElectronicStore.models.order.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(name = "first_name", nullable = true, length = 45)
	private String firstName;

	@Column(name = "last_name", nullable = true, length = 45)
	private String lastName;
	
	private int sex;
	
	private Date birthday;
	
	private String picture;
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//	private Set<Role> roles = new HashSet<Role>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "user_roles", 
//		joinColumns = @JoinColumn(name = "user_id"),
//		inverseJoinColumns = @JoinColumn(name = "role_id"))
//	
//	private Set<Role> roles = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Set<Review> reviews = new HashSet<Review>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_liked_reviews",
	        joinColumns = @JoinColumn(name = "user_id"),
	        inverseJoinColumns = @JoinColumn(name = "review_id"))
	private Set<Review> likedReviews = new HashSet<>();

	public Set<Review> getLikedReviews() {
	    return likedReviews;
	}

	public void setLikedReviews(Set<Review> likedReviews) {
	    this.likedReviews = likedReviews;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Set<Order> orders = new HashSet<Order>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Set<Address> addresses = new HashSet<Address>();

	private boolean enabled;
	
	private String verfivationcode;

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public String getVerfivationcode() {
		return verfivationcode;
	}

	public void setVerfivationcode(String verfivationcode) {
		this.verfivationcode = verfivationcode;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Column(name = "created_time")
	private Date createdTime = new Date();

	public User(Long id, String phoneNumber, String password, String email, String firstName, String lastName,
			Set<Role> roles, boolean enabled, Date createdTime) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
		this.enabled = enabled;
		this.createdTime = createdTime;
	}

	public User() {
		
	}
	
	public User(String phoneNumber, String password, String email, String firstName, String lastName, int sex,
			Date birthday, String picture, Set<Role> roles, boolean enabled, Date createdTime) {
		super();
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.birthday = birthday;
		this.picture = picture;
		this.roles = roles;
		this.enabled = enabled;
		this.createdTime = createdTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public String getPhoneNumber() {
		return phoneNumber;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	@Override
	public String toString() {
		String format = "Customer [id=%d, email=%s, firstName=%s, lastName=%s";
		return format.formatted(this.getId(), this.getEmail(), this.getFirstName(), this.getLastName());
	}
}
