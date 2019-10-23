package model;

public class OpenVO {
	private String monOpen;		private String monClose = "20:00";
	private String tueOpen;		private String tueClose = "20:00";
	private String wedOpen;		private String wedClose = "20:00";
	private String thuOpen;		private String thuClose = "15:00";
	private String friOpen;		private String friClose = "20:00";
	private String satOpen; 	private String satClose = "20:00";
	private String sunOpen;		private String sunClose = "20:00";

	public OpenVO(String monOpen, String monClose, String tueOpen, String tueClose, String wedOpen, String wedClose,
			String thuOpen, String thuClose, String friOpen, String friClose, String satOpen, String satClose,
			String sunOpen, String sunClose) {
		super();
		this.monOpen = monOpen;
		this.monClose = monClose;
		this.tueOpen = tueOpen;
		this.tueClose = tueClose;
		this.wedOpen = wedOpen;
		this.wedClose = wedClose;
		this.thuOpen = thuOpen;
		this.thuClose = thuClose;
		this.friOpen = friOpen;
		this.friClose = friClose;
		this.satOpen = satOpen;
		this.satClose = satClose;
		this.sunOpen = sunOpen;
		this.sunClose = sunClose;
	}

	public OpenVO() {
	}

	public String getMonOpen() {
		return monOpen;
	}

	public String getMonClose() {
		return monClose;
	}



	public String getTueOpen() {
		return tueOpen;
	}

	public String getTueClose() {
		return tueClose;
	}

	public String getWedOpen() {
		return wedOpen;
	}

	public String getWedClose() {
		return wedClose;
	}

	public String getThuOpen() {
		return thuOpen;
	}


	public String getThuClose() {
		return thuClose;
	}

	public String getFriOpen() {
		return friOpen;
	}

	public String getFriClose() {
		return friClose;
	}

	public String getSatOpen() {
		return satOpen;
	}

	public String getSatClose() {
		return satClose;
	}

	public String getSunOpen() {
		return sunOpen;
	}

	public String getSunClose() {
		return sunClose;
	}
	
	
}
