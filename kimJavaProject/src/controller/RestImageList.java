package controller;

public class RestImageList {
    private String name;
    private String address;
    private double avgStars;

    public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public double getAvgStars() {
		return avgStars;
	}

	public RestImageList(String name, String address, double avgStars) {
		super();
		this.name = name;
		this.address = address;
		this.avgStars = avgStars;
	}

}