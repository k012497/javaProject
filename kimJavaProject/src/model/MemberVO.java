package model;

public class MemberVO {
	private String memberID;
	private String password;
	private String name;
	private String phoneNumer;
	private String address;
	private String gender;
	private String ageGroup;
	
	public MemberVO() {
	}

	public MemberVO(String memberID, String password, String name, String phoneNumer, String address, String gender,
			String ageGroup) {
		super();
		this.memberID = memberID;
		this.password = password;
		this.name = name;
		this.phoneNumer = phoneNumer;
		this.address = address;
		this.gender = gender;
		this.ageGroup = ageGroup;
	}
	
	public MemberVO(String password, String name, String phoneNumer, String address,
			String ageGroup) {
		super();
		this.password = password;
		this.name = name;
		this.phoneNumer = phoneNumer;
		this.address = address;
		this.ageGroup = ageGroup;
	}

	public MemberVO(String password, String name, String phoneNumer, String address, String gender,
			String ageGroup) {
		super();
		this.password = password;
		this.name = name;
		this.phoneNumer = phoneNumer;
		this.address = address;
		this.gender = gender;
		this.ageGroup = ageGroup;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumer() {
		return phoneNumer;
	}

	public void setPhoneNumer(String phoneNumer) {
		this.phoneNumer = phoneNumer;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	

}
