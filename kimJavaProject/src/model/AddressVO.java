package model;

public class AddressVO {
	private String gu;
	private String dong;
	
	public AddressVO() {
	}

	public AddressVO(String gu, String dong) {
		super();
		this.gu = gu;
		this.dong = dong;
	}

	public String getGu() {
		return gu;
	}

	public void setGu(String gu) {
		this.gu = gu;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}
	
	
}
