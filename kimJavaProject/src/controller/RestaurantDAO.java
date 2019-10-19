package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.RestaurantVO;

public class RestaurantDAO {

	// ① 신규 학생 점수 등록 (data 입력 부분 - insert)
	public int getRestregiste(RestaurantVO rvo) throws Exception {

		String dml = "insert into restaurantTBL "
				+ "(restaurantID, restaurantName, address, telephone, kind, veganLevel, imageFileName, favCount, avgStars, registeDate, takeout, parking, reservation)"
				+ " values " + "(null, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, rvo.getRestaurantName());
			pstmt.setString(2, rvo.getAddress());
			pstmt.setString(3, rvo.getTelephone());
			pstmt.setString(4, rvo.getKind());
			pstmt.setString(5, rvo.getVeganLevel());
			pstmt.setString(6, rvo.getImageFileName());
			pstmt.setInt(7, rvo.getFavCount());
			pstmt.setDouble(8, rvo.getAvgStars());
			
			pstmt.setString(9, rvo.getTakeout());
			pstmt.setString(10, rvo.getParking());
			pstmt.setString(11, rvo.getReservation());

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			count = pstmt.executeUpdate(); // workbench에서 번개 누르는 것. 몇 문장을 실행했는지를 리턴

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
		return count;
	}

	// data(학생 전체 리스트) 가져오기 - select
	public ArrayList<RestaurantVO> getRestTotal() {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select * from restaurantTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		RestaurantVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				emVo = new RestaurantVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
						rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(emVo);
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
	

	// data 삭제 기능 - delete
	public void getRestDelete(int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from restaurantTBL where restaurantID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "delete restaurant", "delete completed", "SUCCESS!");

			} else {
				SharedMethod.alertDisplay(1, "delete restaurant", "delete not completed", "FAIL!");
			}

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

	}

	// 수정기능. UPDATE table SET //리턴 왜 함?
	public RestaurantVO getRestUpdate(RestaurantVO rvo, int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "update restaurantTBL set "
				+"restaurantName=?, address=?, telephone=?, kind=?, veganLevel=?, imageFileName=?, favCount=?, avgStars=?, takeout=?, parking=?, reservation=? where restaurantID=?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt.setString(1, rvo.getRestaurantName());
			pstmt.setString(2, rvo.getAddress());
			pstmt.setString(3, rvo.getTelephone());
			pstmt.setString(4, rvo.getKind());
			pstmt.setString(5, rvo.getVeganLevel());
			pstmt.setString(6, rvo.getImageFileName());
			pstmt.setInt(7, rvo.getFavCount());
			pstmt.setDouble(8, rvo.getAvgStars());
			
			pstmt.setString(9, rvo.getTakeout());
			pstmt.setString(10, rvo.getParking());
			pstmt.setString(11, rvo.getReservation());

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(1, "score correction", "correction completed", "SUCCESS!");
			} else {
				SharedMethod.alertDisplay(1, "score correction error", "correction failed", "TRY AGAIN!");
				return null;
			}

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
		return rvo;
	}

	// 이름 검색 기능
	public ArrayList<RestaurantVO> getRestCheck(String name) throws Exception {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select * from restaurantTBL where name like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RestaurantVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String nameToSearch = "%" + name + "%";
			pstmt.setString(1, nameToSearch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new RestaurantVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
						rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(retval);
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
