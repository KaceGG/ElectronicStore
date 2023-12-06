package javaweb.ElectronicStore.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@Column(length = 128, nullable = false)
	private String headline;

	@Column(length = 300, nullable = false)
	private String comment;

	private int rating;

	@Column(nullable = false)
	private Date reviewTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(nullable = false)
    private int likeCount;
	
	@ManyToMany(mappedBy = "likedReviews", cascade = CascadeType.ALL)
	private Set<User> likedByUsers = new HashSet<>();

	public Set<User> getLikedByUsers() {
	    return likedByUsers;
	}

	public void setLikedByUsers(Set<User> likedByUsers) {
	    this.likedByUsers = likedByUsers;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getCustomer() {
		return user;
	}

	public void setCustomer(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Review [headline=" + headline + ", rating=" + rating + ", reviewTime=" + reviewTime + "]";
	}

}
