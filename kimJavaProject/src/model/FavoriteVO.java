package model;

public class FavoriteVO {
	private int favID;
	private String memberID;
	private int restaurantID;
	
	public FavoriteVO(){
	}
	
	public FavoriteVO(int favID, String memberID, int restaurantID) {
		super();
		this.favID = favID;
		this.memberID = memberID;
		this.restaurantID = restaurantID;
	}
	
	public FavoriteVO(String memberID, int restaurantID) {
		super();
		this.memberID = memberID;
		this.restaurantID = restaurantID;
	}

	public int getFavID() {
		return favID;
	}

	public void setFavID(int favID) {
		this.favID = favID;
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

	public void setShopID(int restaurantID) {
		this.restaurantID = restaurantID;
	}
	
	
}
