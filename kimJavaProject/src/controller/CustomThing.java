package controller;

public class CustomThing {
    private String name;
    private String address;
    private double avgStars;
    public String getName() {
        return name;
    }
    public double getPrice() {
        return avgStars;
    }
    public String getAddress() {
    	return address;
    }
    public CustomThing(String name, String address, double avgStars) {
        super();
        this.name = name;
        this.address = address;
        this.avgStars = avgStars;
    }
}