package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ReviewJoinRestaurantVO;
import model.ReviewVO;

public class ReviewDAO {
	
	// insert - 리뷰 등록
	public int getReviewRegiste(ReviewVO rvo) throws Exception {
		String dml = "insert into reviewTBL " + "(reviewID, memberID, restaurantID, stars, registeDate)" + " values "
				+ "(null, ?, ?, ?, now())";

		Connection con = null;
		PreparedStatement pstmt = null;

		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // for security
			pstmt.setString(1, rvo.getMemberID());
			pstmt.setInt(2, rvo.getRestaurantID());
			pstmt.setDouble(3, rvo.getStars());

			// SQL문을 수행 후 처리 결과를 얻어옴
			count = pstmt.executeUpdate(); // workbench에서 번개 누르는 것. 몇 문장을 실행했는지를 리턴

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return count;
	}

	// 특정 사용자의 리뷰 가져오기
	public ArrayList<ReviewJoinRestaurantVO> getReveiw(String memberID) {
		ArrayList<ReviewJoinRestaurantVO> list = new ArrayList<ReviewJoinRestaurantVO>();
		String dml = "select restaurantTBL.restaurantName, reviewTBL.stars, reviewTBL.registeDate "
				+ "from restaurantTBL inner join reviewTBL on restaurantTBL.restaurantID = reviewTBL.restaurantID " + 
				"where reviewTBL.memberId = ? order by reviewTBL.registeDate desc";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, memberID);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				list.add(new ReviewJoinRestaurantVO(rs.getString(1), rs.getDouble(2), rs.getString(3)));
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
