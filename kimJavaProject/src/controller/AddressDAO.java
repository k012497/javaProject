package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddressDAO {
   // 00구 찾기
   public ObservableList<String> getGu() {
      ObservableList<String> list = FXCollections.observableArrayList();
      String dml = "select gu from addresstbl group by gu";

      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

      try {
         con = DBUtil.getConnection();
         pstmt = con.prepareStatement(dml);
         rs = pstmt.executeQuery();
         while (rs.next()) { // 다음 레코드가 있을 동안
            list.add(rs.getString(1));
         }
      } catch (SQLException se) {
         System.out.println(se);
      } catch (Exception e) {
         System.out.println(e);
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (SQLException se) {
         }
      }
      return list;
   }
   
   // 특정 구에 속한 동 가져오기 
   public ObservableList<String> getDong(String gu) {
      ObservableList<String> list = FXCollections.observableArrayList();
      String dml = "select dong from addresstbl where gu = ?";

      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

      try {
         con = DBUtil.getConnection();
         pstmt = con.prepareStatement(dml);
         pstmt.setString(1, gu);
         rs = pstmt.executeQuery();
         while (rs.next()) { // 다음 레코드가 있을 동안
            list.add(rs.getString(1));
         }
      } catch (SQLException se) {
         System.out.println(se);
      } catch (Exception e) {
         System.out.println(e);
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (SQLException se) {
         }
      }
      return list;
   }

}