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

	// chart analysis tab

	private MemberDAO memberDAO;
	private ObservableList<MemberVO> data;
	private int selectedIndex;
	private ObservableList<MemberVO> selectedMember;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//아이디는 수정 불가 
		txtMemId.setDisable(true);
		
		//table column setting
		memberTableColSetting();
		//load total list into tableView
		totalList();

		// initial setting - comboBox;
		cbMemAge.setItems(FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90"));
		
		// when click btnMemEdit
		btnMemEdit.setOnAction((ActionEvent event) -> handlerBtnMemEditAction());
		
		// when select an object in tableView
		memTable.setOnMousePressed((e) -> handlerTableViewPressedAction());
		
		// when click btnMemDelete
		btnMemDelete.setOnAction((e)-> handlerBtnDeleteAction());

	}

	public void memberTableColSetting() {
		data = FXCollections.observableArrayList();
		memTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colId = new TableColumn("ID");
		colId.setMaxWidth(120);
		colId.setStyle("-fx-alignment:CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("memberID"));

		TableColumn colPw = new TableColumn("비밀번호");
		colPw.setMaxWidth(160);
		colPw.setStyle("-fx-alignment:CENTER;");
		colPw.setCellValueFactory(new PropertyValueFactory("password"));
		
		TableColumn colName = new TableColumn("이름");
		colName.setMaxWidth(80);
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
		memTable.getColumns().addAll(colId, colPw, colName, colPhoneNum, colAddr, colGender, colAge);

	} // end of memberTableViewSetting

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
				MemberVO memberVO = memberDAO.getMemberUpdate(mvo, selectedMember.get(0).getMemberID());
				
				data.remove(selectedIndex); // 선택된 레코드를 지워버리고
				data.add(selectedIndex, mvo);
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
		}

	}// end of handlerBtnMemEditAction

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

	public void handlerTableViewPressedAction() {
		// 테이블 뷰 객체 없는 부분 클릭 시 방어
		try {
			// 누른 위치와 해당 객체를 가져온다
			selectedIndex = memTable.getSelectionModel().getSelectedIndex();
			selectedMember = memTable.getSelectionModel().getSelectedItems();

			// 가져온 정보를 데이터 필드에 출력
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

	public void handlerBtnDeleteAction() {
		try {
			MemberDAO memberDAO = new MemberDAO();
			memberDAO.getMemberDelete(selectedMember.get(0).getMemberID());
			data.removeAll(data); // 지우고
			totalList(); // 다시 전체 부르기
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "DELETE ERROR", "error", "error");
		}

		// 경고창 확인/취소 값 받아서 삭제할 지 말 지 이벤트 처리
	}
}