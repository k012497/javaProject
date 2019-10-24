package model;

public class ReviewJoinRestaurantVO {

	private String restaurantName; 
	private Double stars; 
	private String registeDate;
	
	public ReviewJoinRestaurantVO(String restaurantName, Double stars, String registeDate) {
		super();
		this.restaurantName = restaurantName;
		this.stars = stars;
		this.registeDate = registeDate;
	}

	public String getRestaurantName() {
		return restaurantName;
	}


	public Double getStars() {
		return stars;
	}


	public String getRegisteDate() {
		return registeDate;
	}

}
