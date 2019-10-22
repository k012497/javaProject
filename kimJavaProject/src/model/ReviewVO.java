package model;

public class ReviewVO {
	private int reviewID;
	private String memberID;
	private int restaurantID;
	private double stars;
	private String registeDate;
	
	
	public ReviewVO(int reviewID, String memberID, int restaurantID, double stars, String registeDate) {
		super();
		this.reviewID = reviewID;
		this.memberID = memberID;
		this.restaurantID = restaurantID;
		this.stars = stars;
		this.registeDate = registeDate;
	}
	
	public ReviewVO(String memberID, int restaurantID, double stars) {
		super();
		this.memberID = memberID;
		this.restaurantID = restaurantID;
		this.stars = stars;
	}
	
	public int getReviewID() {
		return reviewID;
	}
	public void setReview(int review) {
		this.reviewID = review;
	}
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	public int getRestaurantID() {
		return restaurantID;
	}
	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}
	public double getStars() {
		return stars;
	}
	public void setStars(double stars) {
		this.stars = stars;
	}
	public String getRegisteDate() {
		return registeDate;
	}
	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}
	
}
