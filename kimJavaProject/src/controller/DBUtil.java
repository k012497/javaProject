package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//드라이버를 적재, 아이디 패스워드 mysql 데이터 베이스 연결요청 -> DB를 연동하는 객체참조변수를 줌
public class DBUtil {
   //1.드라이버를 적재한다
   private static String driver = "com.mysql.jdbc.Driver";
   //2.데이터베이스 url저장. 접근하려는 데이터베이스주소
   private static String url = "jdbc:mysql://localhost/mapDB"; 
   
   //2.드라이버를 적재하고 데이터베이스를 연결하는 함수
   public static Connection getConnection() throws ClassNotFoundException, SQLException {
      //1.드라이버를 적재
      Class.forName(driver);
      //2. 데이터베이스연결 , 주소, 루트, 비번순
      //Connection은 java.sql.connection을 사용
      Connection con = DriverManager.getConnection(url,"root","01240124");
      
      return con;
   }
   
}