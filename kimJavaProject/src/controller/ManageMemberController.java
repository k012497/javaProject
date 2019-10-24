package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MemberVO;

/*
 * 멤버 정보를 관리(조회/수정/삭제)할 수 있는 탭
 * 만든이 : 김소진
 */
public class ManageMemberController implements Initializable {
	// member management tab
	@FXML
	private TableView<MemberVO> memTable;
	@FXML
	private TextField txtMemId;
	@FXML
	private TextField txtMemPw;
	@FXML
	private TextField txtMemName;
	@FXML
	private TextField txtMemGender;
	@FXML
	private TextField txtMemAddr;
	@FXML
	private TextField txtMemPhone;
	@FXML
	private ComboBox<String> cbMemAge;
	@FXML
	private Button btnMemDelete;
	@FXML
	private Button btnMemEdit;

	private ObservableList<MemberVO> data;
	private int selectedIndex;
	private ObservableList<MemberVO> selectedMember;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 멤버 테이블 뷰에 칼럼 및 데이터 세팅
		memberTableColSetting();
		totalList();
		
		// 아이디는 수정 불가 
		txtMemId.setDisable(true);

		// 콤보박스 세팅
		cbMemAge.setItems(FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90"));
		
		// 테이블 뷰 속 객체를 눌렀을 때
		memTable.setOnMousePressed((e) -> handlerTableViewPressedAction());

		// 수정 버튼을 눌렀을 때
		btnMemEdit.setOnAction((ActionEvent event) -> handlerBtnMemEditAction());
		
		// 삭제 버튼을 눌렀을 때 
		btnMemDelete.setOnAction((e)-> handlerBtnDeleteAction());
	}

	// 테이블 뷰의 칼럼을 세팅하는 메소드 
	public void memberTableColSetting() {
		data = FXCollections.observableArrayList();
		memTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colId = new TableColumn("ID");
		colId.setPrefWidth(100);
		colId.setStyle("-fx-alignment:CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("memberID"));

		TableColumn colPw = new TableColumn("비밀번호");
		colPw.setMaxWidth(160);
		colPw.setStyle("-fx-alignment:CENTER;");
		colPw.setCellValueFactory(new PropertyValueFactory("password"));
		
		TableColumn colName = new TableColumn("이름");
		colName.setPrefWidth(90);
		colName.setMaxWidth(120);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colPhoneNum = new TableColumn("전화번호");
		colPhoneNum.setMaxWidth(140);
		colPhoneNum.setStyle("-fx-alignment:CENTER;");
		colPhoneNum.setCellValueFactory(new PropertyValueFactory("phoneNumer"));

		TableColumn colAddr = new TableColumn("주소");
		colAddr.setMaxWidth(160);
		colAddr.setStyle("-fx-alignment:CENTER;");
		colAddr.setCellValueFactory(new PropertyValueFactory("address"));

		TableColumn colGender = new TableColumn("성별");
		colGender.setMaxWidth(40);
		colGender.setStyle("-fx-alignment:CENTER;");
		colGender.setCellValueFactory(new PropertyValueFactory("gender"));

		TableColumn colAge = new TableColumn("연령대");
		colAge.setMaxWidth(60);
		colAge.setStyle("-fx-alignment:CENTER;");
		colAge.setCellValueFactory(new PropertyValueFactory("ageGroup"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		memTable.setItems(data);
		memTable.getColumns().addAll(colId, colName, colPw, colPhoneNum, colAddr, colGender, colAge);

	} // end of memberTableViewSetting

	// 빈칸이 없으면 각 필드 내용을 담은 MemberVO로 만들고 DB에 저장하는 함수. 2019-10-17
	public void handlerBtnMemEditAction() {
		try {
			if (txtMemId.getText().equals("") || txtMemPw.getText().equals("") || txtMemName.getText().equals("")
					|| txtMemGender.getText().equals("") || txtMemAddr.getText().equals("")) {
				throw new Exception();
			} else {
				MemberVO mvo = new MemberVO(txtMemId.getText(), txtMemPw.getText(), txtMemName.getText(),
					txtMemPhone.getText(), txtMemAddr.getText(), txtMemGender.getText(),
					cbMemAge.getSelectionModel().getSelectedItem());
				
				MemberDAO memberDAO = new MemberDAO();
				memberDAO.getMemberUpdate(mvo, selectedMember.get(0).getMemberID());
				
				data.remove(selectedIndex); // 선택된 레코드를 지워버리고
				data.add(selectedIndex, mvo);
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
		}

	}// end of handlerBtnMemEditAction

	// DB에 저장된 모든 멤버 레코드를 가져와서 ObservableList에 저장
	public void totalList() {
		ArrayList<MemberVO> list = null;
		MemberDAO memberDAO = new MemberDAO();
		MemberVO memberVO = null;
		list = memberDAO.getTotalMember();

		if (list == null) {
			SharedMethod.alertDisplay(1, "warning", "ERROR in CALLING DB", "please check again");
		}

		for (int i = 0; i < list.size(); i++) {
			memberVO = list.get(i);
			data.add(memberVO);
		}
	}// end of totalList

	// 누른 위치와 해당 객체를 가져와서 필드에 데이터를 출력
	public void handlerTableViewPressedAction() {
		try {
			// 누른 위치와 해당 객체를 가져온다
			selectedIndex = memTable.getSelectionModel().getSelectedIndex();
			selectedMember = memTable.getSelectionModel().getSelectedItems();

			// 가져온 정보를 필드에 출력
			txtMemId.setText(selectedMember.get(0).getMemberID());
			cbMemAge.setValue(selectedMember.get(0).getAgeGroup());
			txtMemPw.setText(selectedMember.get(0).getPassword());
			txtMemAddr.setText(selectedMember.get(0).getAddress());
			txtMemName.setText(selectedMember.get(0).getName());
			txtMemPhone.setText(selectedMember.get(0).getPhoneNumer());
			txtMemGender.setText(selectedMember.get(0).getGender());
		} catch (Exception e) {
		}
	}

	//선택한 테이블뷰의 인덱스를 이용하여 해당 객체를 DB에서 삭제하고, 테이블뷰를 다시 세팅하는 함수
	public void handlerBtnDeleteAction() {
		try {
			MemberDAO memberDAO = new MemberDAO();
			memberDAO.getMemberDelete(selectedMember.get(0).getMemberID());
			data.removeAll(data); // 지우고
			totalList(); // 다시 전체 부르기
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "DELETE ERROR", "error", "error");
		}
	}
}