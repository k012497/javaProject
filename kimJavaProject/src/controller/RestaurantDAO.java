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

	// data(전체 리스트) 가져오기 - select
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
				emVo = new RestaurantVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDouble(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
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

	// 음식 종류별 식당 카운트 가져오기
	public int getCountbyKind(String kind) {
		int count = 0;
		String dml = "select count(*) from restaurantTBL where kind = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, kind);
			rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next()) { // 다음 레코드가 있을 동안
				count = rs.getInt(1);
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
		return count;
	}

	// 구별 식당 카운트 가져오기
	public int getCountbyGu(String gu) {
		int count = 0;
		String dml = "select count(*) from restaurantTBL where address like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		RestaurantVO retval = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String guToSearch = "%" + gu + "%";
			pstmt.setString(1, guToSearch);
			rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next()) { // 다음 레코드가 있을 동안
				count = rs.getInt(1);
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
		return count;
	}

	// 별점순 10개 가져오기
	public ArrayList<RestaurantVO> getRest10() {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select * from restaurantTBL order by avgStars desc limit 10";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		RestaurantVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				emVo = new RestaurantVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDouble(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
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

	// 특정 사용자가 즐겨찾기에 등록한 식당의 리스트 가져오기
	public ArrayList<RestaurantVO> getListForFav(String memberId) {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select r.restaurantName, r.address, r.kind\n"
				+ "from restaurantTBL r inner join favoriteTBL f on r.restaurantID = f.restaurantID\n"
				+ "inner join memberTBL m on m.memberID = f.memberID\n" + "where m.memberID = ?;";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		RestaurantVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				emVo = new RestaurantVO(rs.getString(1), rs.getString(2), rs.getString(3));
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

	// 수정 기능
	public RestaurantVO getRestUpdate(RestaurantVO rvo, int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "update restaurantTBL set "
				+ "restaurantName=?, address=?, telephone=?, kind=?, veganLevel=?, imageFileName=?, favCount=?, avgStars=?, takeout=?, parking=?, reservation=? where restaurantID=?";
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

	// 지역별 식당 검색 기능
	public ArrayList<CustomThing> getRestByAddr(String gu, String dong) throws Exception {
		ArrayList<CustomThing> list = new ArrayList<CustomThing>();
		String dml = "select restaurantName, address, avgStars from restaurantTBL where address like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CustomThing retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String addrToSearch = "%" + gu + "%" + dong + "%";
			pstmt.setString(1, addrToSearch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new CustomThing(rs.getString(1), rs.getString(2), rs.getDouble(3));
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
