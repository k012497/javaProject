package controller;
/*
 * listView에 담길 타입
 */

public class CustomThing {
    private String name;
    private String address;
    private double avgStars;
    private int restaurantID;
    private String fileName;
    
    public String getName() {
        return name;
    }
    public double getAvgStars() {
        return avgStars;
    }
    public String getAddress() {
    	return address;
    }
    
	public int getRestaurantID() {
		return restaurantID;
	}
	
	public String getFileName() {
		return fileName;
	}

	public CustomThing(int restaurantID, String name, String address, Double avgStars, String fileName) {
		super();
		this.restaurantID = restaurantID;
		this.name = name;
		this.address = address;
		this.avgStars = avgStars;
		this.fileName = fileName;
	}
}