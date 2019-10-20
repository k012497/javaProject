package controller;

public class CustomThing {
    private String name;
    private String address;
    private double avgStars;
    private int restaurantID;
    
    public String getName() {
        return name;
    }
    public double getPrice() {
        return avgStars;
    }
    public String getAddress() {
    	return address;
    }
    
    public CustomThing(int restaurantID, String name, String address, double avgStars) {
        super();
        this.restaurantID = restaurantID;
        this.name = name;
        this.address = address;
        this.avgStars = avgStars;
    }
	public int getRestaurantID() {
		return restaurantID;
	}
}