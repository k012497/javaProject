package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FavoriteVO;

public class FavoriteDAO {

	// insert
	public int getFavregiste(FavoriteVO fvo, String memberId) throws Exception {

		String dml = "insert into favoriteTBL " + " values " + "(null, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, memberId);
			pstmt.setInt(2, fvo.getRestaurantID());

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

	// select - 모든 즐겨찾기 가져오기
	public ArrayList<FavoriteVO> getFavTotal(int restId) {
		ArrayList<FavoriteVO> list = new ArrayList<FavoriteVO>();
		String dml = "select * from favoriteTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		FavoriteVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				emVo = new FavoriteVO(rs.getInt(1), rs.getString(2), rs.getInt(3));
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

	// 해당 사용자가 특정 식당을 즐겨찾기에 등록 했는지 확인
	// 했으면 1, 안 했으면 0을 리턴
	public int getFavFlag(int restId, String memberId) {
		boolean flag = false;
		ObservableList<FavoriteVO> list = FXCollections.observableArrayList();
		String dml = "select * from favoriteTBL where restaurantID = ? and memberID = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restId);
			pstmt.setString(2, memberId);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				list.add(new FavoriteVO(rs.getString(2), rs.getInt(1)));
				flag = true;
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

		if (flag == false) {
			return 0;
		}

		return 1;

	}

	// select - 즐겨찾기에 등록된 횟수
	public int getFavCount(int restID) {
		String dml = "select count(f.restaurantID)" + "	from favoriteTBL f, restaurantTBL r"
				+ "	where f.restaurantID = r.restaurantID and f.restaurantID = ?" + " group by f.restaurantID";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		int count = 0;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restID);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				count = rs.getInt(1);
			}
		} catch (SQLException se) {
		} catch (Exception e) {
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

	// delete - 해당 식당의 즐겨찾기 레코드를 삭제
	public void getFavDelete(int restId, String memId) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from favoriteTBL where restaurantID = ? and memberID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restId);
			pstmt.setString(2, memId);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "삭제 성공", "삭제 성공", "삭제를 성공했습니다!");

			} else {
				SharedMethod.alertDisplay(1, "삭제 실패", "삭제 실패", "삭제에 실패했습니다");
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

//	// update - 해당 id의 즐겨찾기 레코드를 수정
//	public FavoriteVO getFavUpdate(FavoriteVO fvo, int favId) throws Exception {
//		// 데이터 처리를 위한 SQL 문
//		String dml = "update favoriteTBL set " + "memberID=?, restaurantID=? where favID=?";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			con = DBUtil.getConnection();
//
//			// 수정한 정보를 수정하기 위하여 SQL문장을 생성
//			pstmt = con.prepareStatement(dml);
//			pstmt.setString(1, fvo.getMemberID());
//			pstmt.setInt(2, fvo.getRestaurantID());
//			pstmt.setInt(3, favId);
//
//			// SQL문을 수행후 처리 결과를 얻어옴
//			int i = pstmt.executeUpdate();
//
//			if (i == 1) {
//				SharedMethod.alertDisplay(1, "즐겨찾기 수정 성 correction", "correction completed", "SUCCESS!");
//			} else {
//				SharedMethod.alertDisplay(1, "favorite correction error", "correction failed", "TRY AGAIN!");
//				return null;
//			}
//
//		} catch (SQLException e) {
//			System.out.println("e=[" + e + "]");
//		} catch (Exception e) {
//			System.out.println("e=[" + e + "]");
//		} finally {
//			try {
//				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
//				if (pstmt != null)
//					pstmt.close();
//				if (con != null)
//					con.close();
//			} catch (SQLException e) {
//			}
//		}
//		return fvo;
//	}
}
