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
		String dml = "insert into memberTBL " + "(memberID, password, name, phoneNumber, address, gender, ageGroup)"
				+ " values " + "(?, ?, ?, ?, ?, ?, ?)";

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
						rs.getString(6), rs.getString(7));
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
				retval = new MemberVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
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

	// select - 아이디로 멤버 검색
	public ArrayList<MemberVO> getMemberInfoUsingId(String id) throws Exception {
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		String dml = "select * from memberTBL where memberID like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			String nameToSearch = "%" + id + "%";
			pstmt.setString(1, nameToSearch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new MemberVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
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
				SharedMethod.alertDisplay(1, "멤버 정보 수정 성공", "멤버 정보 수정 성공", "정보를 성공적으로 수정하였습니다.");
			} else {
				SharedMethod.alertDisplay(1, "멤버 정보 수정 실패", "멤버 정보 수정 실패", "멤버 정보 수정 실패하였습니다.");
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

	// delete
	public void getMemberDelete(String id) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "delete from memberTBL where memberID = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(5, "멤버 삭제 성공", "멤버 삭제 성공", "멤버를 성공적으로 삭제하였습니다.");

			} else {
				SharedMethod.alertDisplay(1, "멤버 삭제 성공", "멤버 삭제 성공", "멤버 삭제에 실패하였습니다.");
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

	// ID중복 검사를 위해  해당 아이디가 존재하는지를 검사하는 메소드 
	public int getMemeberIdSearch(String id) {
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
				SharedMethod.alertDisplay(1, "ID 중복검사 실패", "아이디 입력 요망 ", "아이디를 입력해주세요");
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

	// 아이디를 찾기 위해 이름과 전화번호를 DB에서 가져와 확인 하는 메소드
	
	public static String findIDByPhone(String phone, String name) throws Exception {
		StringBuffer checkTchID = new StringBuffer("select memberID from memberTBL where name = ? and phoneNumber= ?");
		String resultString = null;
		Connection con = null;
		PreparedStatement psmt = null;

		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			psmt = con.prepareStatement(checkTchID.toString());
			// 첫번째 물음표 자리 -> studentID 매치 시켜주는 작업
			psmt.setString(1, name);
			psmt.setString(2, phone);

			// 실제 데이터를 연결한 쿼리문 실행하라 데이터 베이스에게 명령(번개문)
			rs = psmt.executeQuery();

			while (rs.next()) {
				resultString = rs.getString(1);
			}
			if (resultString == null) {
				return resultString;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// CLOSE DataBase psmt object
				// 제일 먼저 불렀던 것을 제일 나중에 닫는다.
				// 반드시 있으면 닫아라.
				if (psmt != null)
					psmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				SharedMethod.alertDisplay(1, "문제 발생", "문제가 발생하였습니다.",
						"자원 닫기 실패 : psmt & con (데이터 자원) 닫는 데에 문제가 발생했어요.");
			}
		}

		return resultString;
	}
	

	// 비밀번호를 찾기 위해 이름과 전화번호,아이디를 DB에서 가져와 확인 하는 함수
	public static String findPWByPhone(String txtName, String txtPhone, String iD) throws Exception {
		StringBuffer checkTchPW = new StringBuffer(
				"select * from memberTBL where memberID = ? and name = ? and phoneNumber = ?");
		String resultString = null;
		Connection con = null;
		PreparedStatement psmt = null;

		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			psmt = con.prepareStatement(checkTchPW.toString());

			psmt.setString(2, txtName);
			psmt.setString(3, txtPhone);
			psmt.setString(1, iD);

			// 실제 데이터를 연결한 쿼리문 실행하라 데이터 베이스에게 명령(번개문)
			rs = psmt.executeQuery();

			while (rs.next()) {
				MemberVO member = new MemberVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
				resultString = member.getPassword();
			}
			if (resultString == null) {
				return resultString;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// CLOSE DataBase psmt object
				// 제일 먼저 불렀던 것을 제일 나중에 닫는다.
				// 반드시 있으면 닫아라.
				if (psmt != null)
					psmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				SharedMethod.alertDisplay(1, "문제 발생", "문제가 발생하였습니다.",
						"자원 닫기 실패 : psmt & con (데이터 자원) 닫는 데에 문제가 발생했어요.");
			}
		}
		return resultString;
	}

	// 로그인 시도 시 해당 아이디와패스워드를 가진 사용자가 있는지 검색하는 함수 
	public static String getMemberIDPW(String id, String pw) { 
        StringBuffer checkTchPW = new StringBuffer("select * from memberTBL where memberID = ? and password  = ?");
         String resultString =null;
            Connection con = null;
            PreparedStatement psmt = null;
            
            ResultSet rs = null;
            try {
               try {
             con = DBUtil.getConnection();
          } catch (ClassNotFoundException e) {
             SharedMethod.alertDisplay(1, "DB 연결 오류", "다시 시도해주세요", "다시 시도해주세요");
          }
               psmt = con.prepareStatement(checkTchPW.toString());
               
               psmt.setString(1, id);
               psmt.setString(2, pw);
         
               rs = psmt.executeQuery();
               
               while(rs.next()) {
                  MemberVO member = new MemberVO(rs.getString(1), 
                        rs.getString(2), rs.getString(3), 
                        rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7));
                  resultString = member.getPassword();
               }
               if (resultString==null) {
                  return resultString;
               }
            } catch (SQLException e) {
               e.printStackTrace();
            } finally {
               try {
                  // CLOSE DataBase psmt object
                  // 제일 먼저 불렀던 것을 제일 나중에 닫는다.
                  // 반드시 있으면 닫아라.
                  if (psmt != null)
                     psmt.close();
                  if (con != null)
                     con.close();
               } catch (SQLException e) {
                  SharedMethod.alertDisplay(1,"문제 발생","문제가 발생하였습니다.","자원 닫기 실패 : psmt & con (데이터 자원) 닫는 데에 문제가 발생했어요.");
               }
            }
           return resultString;
    }
}