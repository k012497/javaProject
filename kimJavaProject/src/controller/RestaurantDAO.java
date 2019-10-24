package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.RestaurantVO;

public class RestaurantDAO {

	// 신규 식당 등록
	public int getRestregiste(RestaurantVO rvo) throws Exception {
		String dml = "insert into restaurantTBL "
				+ "(restaurantID, restaurantName, address, telephone, kind, veganLevel, imageFileName, favCount, avgStars, registeDate, takeout, parking, reservation)"
				+ " values " + "(null, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
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

			// SQL문을 수행후 처리 결과를 얻어옴
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

	// 전체 식당 리스트 가져오기 - select
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

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String guToSearch = "%" + gu + "%";
			pstmt.setString(1, guToSearch);
			rs = pstmt.executeQuery();
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
		String dml = "select restaurantTBL.restaurantName, restaurantTBL.address, restaurantTBL.kind "
				+ "from restaurantTBL inner join favoriteTBL on restaurantTBL.restaurantID = favoriteTBL.restaurantID "
				+ "inner join memberTBL on memberTBL.memberID = favoriteTBL.memberID where memberTBL.memberID = ? group by favoriteTBL.restaurantID";

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
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from restaurantTBL where restaurantID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, no);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "식당 삭제 성공", "식당 삭제 성공", "식당을 성공적으로 삭제하였습니다.");

			} else {
				SharedMethod.alertDisplay(1, "식당 삭제 실패", "식당 삭제 실패", "식당 삭제에 실패하였습니다.");
			}

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

	}

	// 수정 기능
	public RestaurantVO getRestUpdate(RestaurantVO rvo, int no) throws Exception {
		String dml = "update restaurantTBL set "
				+ "restaurantName=?, address=?, telephone=?, kind=?, veganLevel=?, imageFileName=?, favCount=?, avgStars=?, takeout=?, parking=?, reservation=? where restaurantID=?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
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
			pstmt.setInt(12, no);
			
			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();
			if (i == 1) {
				SharedMethod.alertDisplay(1, " 식당 수정 ", "수정 성공", "정보를 성공적으로 수정하였습니다.");
			} else {
				SharedMethod.alertDisplay(1, " 식당 수정 실패 ", "식당 수정 실패", "식당 수정 실패");
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

	// favCount(즐겨찾기 개수) 수정
	public void getFavCountUpdate(int count, int restId) throws Exception {
		String dml = "update restaurantTBL set favCount = ? where restaurantID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			// 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, count);
			pstmt.setInt(2, restId);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "즐겨찾기 등록 ", "즐겨찾기에 추가되었습니다.",  "성공적으로 등록되었습니다!");
			} else {
				SharedMethod.alertDisplay(1, "즐겨찾기 등록 실패", "즐겨찾기 등록 실패", "즐겨찾기 등록 실패!");
			}

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
	}

	// avgStars(평균 별점) 업데이트
	public void getRestStarsUpdate(int restId) throws Exception {
		String dml = "update restaurantTBL set avgStars = (Select Round(Avg(stars), 2) from reviewTBL where restaurantId = ?) "
				+ "where restaurantID = ? ";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restId);
			pstmt.setInt(2, restId);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();
			if (i == 1) {
				SharedMethod.alertDisplay(5, "리뷰 등록 성공!", "리뷰 등록 성공!!", "리뷰를 정상적으로 등록하였습니다 ");
			} else {
				SharedMethod.alertDisplay(1, "리뷰 등록 실패 ", "리뷰 등록 실패", "리뷰 등록에 실패하였습니다.");
				return;
			}

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
		return;
	}

	// 이름으로 식당 검색 기능
	public ArrayList<RestaurantVO> getRestByName(String name) throws Exception {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select * from restaurantTBL where restaurantName like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RestaurantVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String addrToSearch = "%" + name + "%";
			pstmt.setString(1, addrToSearch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new RestaurantVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDouble(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
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

	// 지역별 식당 검색 기능
	public ArrayList<CustomThing> getRestByAddr(String gu, String dong) throws Exception {
		ArrayList<CustomThing> list = new ArrayList<CustomThing>();
		String dml = "select restaurantID, restaurantName, address, avgStars, imageFileName from restaurantTBL where address like ?";

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
				retval = new CustomThing(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
						rs.getString(5));
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

	// 지역 & 종류별 식당 검색 하기
	public ArrayList<CustomThing> getRestByAddrAndKind(String gu, String dong, String kind) throws Exception {
		ArrayList<CustomThing> list = new ArrayList<CustomThing>();
		String dml = "select restaurantID, restaurantName, address, avgStars, imageFileName from restaurantTBL where address like ? and kind = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CustomThing retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String addrToSearch = "%" + gu + "%" + dong + "%";
			pstmt.setString(1, addrToSearch);
			pstmt.setString(2, kind);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new CustomThing(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
						rs.getString(5));
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

	// 식당 아이디로 평균 별점 가져오기
	public double getAvgStarsbyId(int id) {
		double avgStars = 0;
		String dml = "select avgStars from restaurantTBL where restaurantID = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				avgStars = rs.getDouble(1);
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
		return avgStars;
	}

	// 연령대별 인기 식당(즐겨찾기 수 내림차순) 가져오기
	public ArrayList<RestaurantVO> getTopFavCounByAge(int ageGroup) {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		String dml = "select restaurantTBL.restaurantName, restaurantTBL.favCount from memberTBL INNER join favoriteTBL on memberTBL.memberID = favoriteTBL.memberID " + 
				"INNER join restaurantTBL on restaurantTBL.restaurantID = favoriteTBL.restaurantID " + 
				"where memberTBL.ageGroup = ? group by restaurantTBL.restaurantID order by count(restaurantTBL.restaurantID) desc limit 10";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RestaurantVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, ageGroup);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new RestaurantVO(rs.getString(1), rs.getInt(2));
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
