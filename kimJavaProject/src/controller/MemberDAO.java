package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.MemberVO;

public class MemberDAO {
	// insert
	public int getMemberRegiste(MemberVO mvo) throws Exception {
		String dml = "insert into memberTBL " + "(memberID, password, name, phoneNumber, address, gender, ageGroup)" + " values "
				+ "(?, ?, ?, ?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // for security
			pstmt.setString(1, mvo.getMemberID());
			pstmt.setString(2, mvo.getPassword());
			pstmt.setString(3, mvo.getName());
			pstmt.setString(4, mvo.getPhoneNumer());
			pstmt.setString(5, mvo.getAddress());
			pstmt.setString(6, mvo.getGender());
			pstmt.setString(7, mvo.getAgeGroup());

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

	// select *
	public ArrayList<MemberVO> getTotalMember() {
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		String dml = "select * from memberTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // db에서 가져올 때 임시 보관 장소
		MemberVO mVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();
			while (rs.next()) { // 다음 레코드가 있을 동안
				mVO = new MemberVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6),rs.getString(7));
				list.add(mVO);
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

	// select - 이름 검색 기능
	public ArrayList<MemberVO> getMemberCheck(String name) throws Exception {
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		String dml = "select * from memberTBL where name like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String nameToSearch = "%" + name + "%";
			pstmt.setString(1, nameToSearch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new MemberVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),
						rs.getString(7));
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

	// update
	public MemberVO getMemberUpdate(MemberVO mvo, String id) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "update memberTBL set "
				+ " memberID=?, password=?, name=?, phoneNumber=?, address=?, gender=?, ageGroup=? where memberID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, mvo.getMemberID());
			pstmt.setString(2, mvo.getPassword());
			pstmt.setString(3, mvo.getName());
			pstmt.setString(4, mvo.getPhoneNumer());
			pstmt.setString(5, mvo.getAddress());
			pstmt.setString(6, mvo.getGender());
			pstmt.setString(7, mvo.getAgeGroup());
			pstmt.setString(8, mvo.getMemberID());

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(1, "member info correction", "correction completed", "SUCCESS!");
			} else {
				SharedMethod.alertDisplay(1, "member info correction error", "correction failed", "TRY AGAIN!");
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
		return mvo;
	}

	// delete
	public void getMemberDelete(String id) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from memberTBL where memberID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "delete student", "delete completed", "SUCCESS!");

			} else {
				SharedMethod.alertDisplay(1, "delete student", "delete not completed", "FAIL!");
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
	
	public int getUserIdSearch(String id) {

	      String saveId = null;
	      ArrayList<String> list = new ArrayList<String>();
	      String dml = "select memberID from memberTBL where memberID = ?";
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         con = DBUtil.getConnection();
	         pstmt = con.prepareStatement(dml);
	         pstmt.setString(1, id);
	         rs = pstmt.executeQuery();
	         if (id.equals("")) {
	            SharedMethod.alertDisplay(1, "id 사용 가능", ".", ".");
	            return 0;
	         }
	         while (rs.next()) {
	            saveId = rs.getString(1);
	            list.add(saveId);
	         }
	         for (int i = 0; i < list.size(); i++) {
	            if (list.get(i).equals(id)) {
	               return 1;
	            }
	         }
	         return -1;
	      } catch (Exception e2) {
	         e2.printStackTrace();
	      } finally {
	         try {
	            if (pstmt != null)
	               pstmt.close();
	            if (con != null)
	               con.close();
	         } catch (SQLException e3) {
	            e3.printStackTrace();
	         }
	      }
	      return 0;
	   }

	   // Search踰꾪듉-> 以묐났 �솗�씤�쓽 踰꾪듉�쓣 �닃�윭�빞留� 媛��엯�씠 �릺�뒗 �븿�닔
	   public static void insertMemberDataSearch(MemberVO member) {
	      
	   }

}