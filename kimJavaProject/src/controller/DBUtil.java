package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * 데이터베이스와 연결  
 */

public class DBUtil {
   // 드라이버
   private static String driver = "com.mysql.jdbc.Driver";
   
   // 데이터베이스 url
   private static String url = "jdbc:mysql://localhost/mapDB"; 
   
   // 드라이버를 적재하고 데이터베이스를 연결하는 함수 - DB를 연동하는 객체참조변수를 줌
   public static Connection getConnection() throws ClassNotFoundException, SQLException {
      // 1. 드라이버를 적재
      Class.forName(driver);
      
      // 2. 데이터베이스 연결 , 주소, 루트, 비밀번호 순
      //Connection은 java.sql.connection을 사용
      Connection con = DriverManager.getConnection(url,"root","01240124");
      
      return con;
   }
   
}