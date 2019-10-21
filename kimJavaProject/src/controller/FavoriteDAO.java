package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FavoriteVO;
import model.MenuVO;

public class FavoriteDAO {

	// ① 신규 등록 (data 입력 부분 - insert)
	public int getFavregiste(FavoriteVO fvo, String memberId) throws Exception {

		String dml = "insert into favoriteTBL " + " values " + "(null, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, memberId);
			pstmt.setInt(2, fvo.getRestaurantID());

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

	// 모든 즐겨찾기 가져오기
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

	// 해당 사용자가 특정 식당을 즐찾 등록 했는지 확인. 했으면 1 안 했으면 0 리턴
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

	public int getFavCount(int restID) {
		ObservableList<String> list = FXCollections.observableArrayList();
		String dml = "select count(f.restaurantID)" + "	from favoriteTBL f, restaurantTBL r"
				+ "	where f.restaurantID = r.restaurantID and f.restaurantID = ?" + "	group by f.restaurantID";

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

	// data 삭제 기능 - delete
	public void getFavDelete(int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from favoriteTBL where favoriteID = ?";
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
				SharedMethod.alertDisplay(5, "delete menu", "delete completed", "SUCCESS!");

			} else {
				SharedMethod.alertDisplay(1, "delete menu", "delete not completed", "FAIL!");
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

	// 수정기능
	public FavoriteVO getFavUpdate(FavoriteVO fvo, int favId) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "update favoriteTBL set " + "memberID=?, restaurantID=? where favID=?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 수정한 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, fvo.getMemberID());
			pstmt.setInt(2, fvo.getRestaurantID());
			pstmt.setInt(3, favId);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(1, "favorite correction", "correction completed", "SUCCESS!");
			} else {
				SharedMethod.alertDisplay(1, "favorite correction error", "correction failed", "TRY AGAIN!");
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
		return fvo;
//		}
	}
}
