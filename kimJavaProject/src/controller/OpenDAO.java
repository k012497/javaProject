package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.OpenVO;

public class OpenDAO {
	public OpenDAO() {
	}

	// select - 식당ID로 영업시간 조회
	public ArrayList<OpenVO> getOpenHours(int restId) {
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
						rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
						rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
				list.add(ovo);
			}

			list.get(0);

		} catch (IndexOutOfBoundsException e) {
			return null;
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

	// update - 영업시간 수정
	public OpenVO getOpenHoursUpdate(OpenVO ovo, int restId) throws Exception {
		// 데이터 처리를 위한 SQL 문
		String dml = "UPDATE openTBL " + "SET "
				+ "monOpen = ?, monClose = ?,tueOpen = ?,tueClose = ?, wedOpen = ?,wedClose = ?, thuOpen = ?, "
				+ "thuClose = ?, friOpen = ?,friClose = ?,satOpen= ?,satClose = ?, sunOpen= ?, sunClose = ? "
				+ "WHERE restaurantID = ? ";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, ovo.getMonOpen());
			pstmt.setString(2, ovo.getMonClose());
			pstmt.setString(3, ovo.getTueOpen());
			pstmt.setString(4, ovo.getTueClose());
			pstmt.setString(5, ovo.getWedOpen());
			pstmt.setString(6, ovo.getWedClose());
			pstmt.setString(7, ovo.getThuOpen());
			pstmt.setString(8, ovo.getThuClose());
			pstmt.setString(9, ovo.getFriOpen());
			pstmt.setString(10, ovo.getFriClose());
			pstmt.setString(11, ovo.getSatOpen());
			pstmt.setString(12, ovo.getSatClose());
			pstmt.setString(13, ovo.getSunOpen());
			pstmt.setString(14, ovo.getSunClose());
			pstmt.setInt(15, restId);

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				SharedMethod.alertDisplay(1, "영업시간 수정 성공", "영업시간 수정 성공", "영업시간을 성공적으로 수정하였습니다.");
			} else {
				SharedMethod.alertDisplay(1, "영업시간 수정 실패", "영업시간 수정 실패", "영업시간 수정에 실패했습니다.");
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
		return ovo;
	}

	// insert - 영업시간 등록
	public int getOpenHoursRegiste(OpenVO ovo, int restId) throws Exception {
		String dml = "insert into openTBL values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
				"?, ?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		int count = 0;
		try {
			// DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml); // for security
			pstmt.setInt(1, restId);
			pstmt.setString(2, ovo.getMonOpen());
			pstmt.setString(3, ovo.getMonClose());
			pstmt.setString(4, ovo.getTueOpen());
			pstmt.setString(5, ovo.getTueClose());
			pstmt.setString(6, ovo.getWedOpen());
			pstmt.setString(7, ovo.getWedClose());
			pstmt.setString(8, ovo.getThuOpen());
			pstmt.setString(9, ovo.getThuClose());
			pstmt.setString(10, ovo.getFriOpen());
			pstmt.setString(11, ovo.getFriClose());
			pstmt.setString(12, ovo.getSatOpen());
			pstmt.setString(13, ovo.getSatClose());
			pstmt.setString(14, ovo.getSunOpen());
			pstmt.setString(15, ovo.getSunClose());

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

}
