package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.MemberVO;
import model.OpenVO;

public class OpenDAO {
	
	public ArrayList<OpenVO> getTotalMember(int restId) {
		ArrayList<OpenVO> list = new ArrayList<OpenVO>();
		String dml = "select * from openTBL where restaurantID = ? ";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		OpenVO ovo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restId);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				ovo = new OpenVO(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
						rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), 
						rs.getString(13), rs.getString(14), rs.getString(15));
				list.add(ovo);
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
	
	
	public OpenVO getMemberUpdate(OpenVO ovo, String id) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "UPDATE openTBL " + 
				"SET " + 
				"monOpen = ?, monClose = ?,tueOpen = ?,tueClose = ?, wedOpen = ?,wedClose = ?, thuOpen = ?, " + 
				"thuClose = ?, friOpen = ?,friClose = ?,satOpen= ?,satClose = ?, sunOpen= ?, sunClose = ? " + 
				"WHERE restaurantID = ? ";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, ovo.getMonOpen());



			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();
			System.out.println("11111");

			if (i == 1) {
				SharedMethod.alertDisplay(1, "member info correction", "correction completed", "SUCCESS!");
			} else {
				SharedMethod.alertDisplay(1, "member info correction error", "correction failed", "TRY AGAIN!");
				return null;
			}
			System.out.println("33333");

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return ovo;
	}
	

}
