package model;

public class RestaurantVO {

	private int restaurantID;
	private String restaurantName;
	private String address;
	private String telephone;
	private String kind;
	private String veganLevel;
	private String imageFileName;
	private int favCount;
	private double avgStars;
	private String registeDate;
	private String takeout;
	private String parking;
	private String reservation;
	
	public RestaurantVO(int restaurantID, String restaurantName, String address, String telephone, String kind,
			String veganLevel, String imageFileName, int favCount, double avgStars, String registeDate, String takeout,
			String parking, String reservation) {
		super();
		this.restaurantID = restaurantID;
		this.restaurantName = restaurantName;
		this.address = address;
		this.telephone = telephone;
		this.kind = kind;
		this.veganLevel = veganLevel;
		this.imageFileName = imageFileName;
		this.favCount = favCount;
		this.avgStars = avgStars;
		this.registeDate = registeDate;
		this.takeout = takeout;
		this.parking = parking;
		this.reservation = reservation;
	}

	public RestaurantVO(String restaurantName, String address, String telephone, String kind, String veganLevel,
			String imageFileName, int favCount, double avgStars, String registeDate, String takeout, String parking,
			String reservation) {
		super();
		this.restaurantName = restaurantName;
		this.address = address;
		this.telephone = telephone;
		this.kind = kind;
		this.veganLevel = veganLevel;
		this.imageFileName = imageFileName;
		this.favCount = favCount;
		this.avgStars = avgStars;
		this.registeDate = registeDate;
		this.takeout = takeout;
		this.parking = parking;
		this.reservation = reservation;
	}

	public RestaurantVO(String restaurantName, String address, String kind) {
		this.restaurantName = restaurantName;
		this.address = address;
		this.kind = kind;
	}
	
	public RestaurantVO(String restaurantName, int favCount) {
		this.restaurantName = restaurantName;
		this.favCount = favCount;
	}

	public RestaurantVO() {
		
	}


	public int getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getVeganLevel() {
		return veganLevel;
	}

	public void setVeganLevel(String veganLevel) {
		this.veganLevel = veganLevel;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public int getFavCount() {
		return favCount;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}

	public double getAvgStars() {
		return avgStars;
	}

	public void setAvgStars(double avgStars) {
		this.avgStars = avgStars;
	}

	public String getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}

	public String getTakeout() {
		return takeout;
	}

	public void setTakeout(String takeout) {
		this.takeout = takeout;
	}

	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public String getReservation() {
		return reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

}
