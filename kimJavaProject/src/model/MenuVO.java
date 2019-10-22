package model;

public class MenuVO {
	private int menuID;
	private int restaurantID;
	private String menuName;
	private int menuPrice;
	
	public MenuVO(int menuID, int restaurantID, String menuName, int menuPrice) {
		super();
		this.menuID = menuID;
		this.restaurantID = restaurantID;
		this.menuName = menuName;
		this.menuPrice = menuPrice;
	}
	
	public MenuVO() {
		
	}
	

	public MenuVO(String menuName, int menuPrice) {
		super();
		this.menuName = menuName;
		this.menuPrice = menuPrice;
	}

	public MenuVO(String menuName, int menuPrice, int menuID) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuID = menuID;
	}

	public int getMenuID() {
		return menuID;
	}

	public void setMenuID(int menuID) {
		this.menuID = menuID;
	}

	public int getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}
	
	
}
