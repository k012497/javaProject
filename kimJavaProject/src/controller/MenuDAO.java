package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MenuVO;

public class MenuDAO {
	// insert - 멤버 등록
	public int getMenuregiste(MenuVO mvo, int restId) throws Exception {

		String dml = "insert into menuTBL " + "(menuID, restaurantID, menuName, menuPrice)" + " values "
				+ "(null, ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, String.valueOf(restId));
			pstmt.setString(2, mvo.getMenuName());
			pstmt.setString(3, String.valueOf(mvo.getMenuPrice()));

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

	// select - 모든 메뉴 가져오기
	public ArrayList<MenuVO> getMenuTotal(int restId) {
		ArrayList<MenuVO> list = new ArrayList<MenuVO>();
		String dml = "select * from menuTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		MenuVO emVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				emVo = new MenuVO(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4));
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
	
	// select - 특정 식당의 메뉴 가져오기 
	public ObservableList<MenuVO> getMenu(int restId) {
		ObservableList<MenuVO> list = FXCollections.observableArrayList();
		String dml = "select menuName, menuPrice, menuId from menuTBL where restaurantID = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, restId);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				list.add(new MenuVO(rs.getString(1), rs.getInt(2), rs.getInt(3)));
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

	// delete 
	public void getMenuDelete(int no) throws Exception {
		String dml = "delete from menuTBL where menuID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setInt(1, no);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "메뉴 삭제 성공", "메뉴 삭제 성공", "메뉴 삭제에 성공하였습니다.");

			} else {
				SharedMethod.alertDisplay(1, "메뉴 삭제 실패", "메뉴 삭제 실패", "메뉴 삭제에 실패하였습니다.");
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

	// update 
	public MenuVO getMenuUpdate(MenuVO mvo, int menuId) throws Exception {
		String dml = "update menuTBL set " 
		+ "menuName=?, menuPrice=? where menuID=?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 수정한 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, mvo.getMenuName());
			pstmt.setInt(2, mvo.getMenuPrice());
			pstmt.setInt(3, menuId);
			
			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(1, "메뉴 수정 성공", "메뉴 수정 성공", "메뉴 수정에 성공하였습니다.");
			} else {
				SharedMethod.alertDisplay(1, "메뉴 수정 실패", "메뉴 수정 실패", "메뉴 수정에 실패하였습니다.");
				return null;
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
		return mvo;
	}
}
