package check_time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
	static Date now = new Date();
	static int nowHours = now.getHours();
	static int nowMinutes = now.getMinutes();
	static String nowTime;

	public static void main(String[] args) {
		//check now time
		System.out.println(nowHours);
		System.out.println(nowMinutes);
		
		if(nowHours < 10 && nowMinutes >= 10) {
			nowTime = "0" + nowHours + ":" + nowMinutes;
		}else if(nowHours >= 10 && nowMinutes < 10) {
			nowTime = nowHours + ":0" + nowMinutes;
		}else if(nowMinutes < 10 && nowHours < 10) {
			nowTime = "0" + nowHours + ":0" + nowMinutes;
		}else {
			nowTime = nowHours + ":" + nowMinutes;
		}
		
		System.out.println(nowTime);
		
		//get day of week
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		String monOpen = "11:00";	String monClose = "20:00";
		String tueOpen = "11:00";	String tueClose = "20:00";
		String wedOpen = "10:00";	String wedClose = "10:00";
		String thuOpen = "14:08";	String thuClose = "15:00";
		String friOpen = "09:00";	String friClose = "20:00";
		String satOpen = "10:00";	String satClose = "20:00";
		String sunOpen = "10:00";	String sunClose = "20:00";
		
		// switch case by dayOfWeek 
		switch (dayOfWeek) {
		case 1:
			checkOpenHour(sunOpen, sunClose);
			break;
		case 2:
			checkOpenHour(monOpen, monClose);
			break;
		case 3:
			checkOpenHour(tueOpen, tueClose);
			break;
		case 4:
			checkOpenHour(wedOpen, wedClose);
			break;
		case 5:
			checkOpenHour(thuOpen, thuClose);
			break;
		case 6:
			checkOpenHour(friOpen, friClose);
			break;
		case 7:
			checkOpenHour(satOpen, satClose);
			break;

		default:
			break;
		}
	}
	
	//해당 요일의 오픈/마감 시간 String을 받아와서 현재시간과 비교 
	public static void checkOpenHour(String open, String close) {
		try {		
			//time1.compareTo(time2) : time1 - time2를 반환. >> time1이 time2보다 이후 날짜이면 양수, 그 반대의 경우 (-), 같으면 0
			int result1 = nowTime.compareTo(open);
			int result2 = close.compareTo(nowTime);
			System.out.println("compare result = " + result1);
			System.out.println("compare result = " + result2);
			
			if(result1 >= 0 && result2 >= 0) {
				System.out.println("영업중");
			}else {
				System.out.println("closed");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
