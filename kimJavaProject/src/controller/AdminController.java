package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.MemberVO;
import model.MenuVO;
import model.RestaurantVO;

public class AdminController implements Initializable {
	@FXML
	private Button btnSignOut;

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

	// restaurant management tab
	@FXML
	private TableView<RestaurantVO> restTable;
	@FXML
	private Button btnRestDelete;
	@FXML
	private Button btnRestEdit;
	@FXML
	private Button btnImage;
	@FXML
	private TextField txtRestAddr;
	@FXML
	private TextField txtRestPark;
	@FXML
	private TextField txtResFoodtKind;
	@FXML
	private TextField txtRestName;
	@FXML
	private TextField txtRestReserve;
	@FXML
	private TextField txtRestTakeout;
	@FXML
	private TextField txtRestStars;
	@FXML
	private TextField txtRestDate;
	@FXML
	private TextField txtRestVegeKind;
	@FXML
	private TextField txtRestFav;
	@FXML
	private ImageView imgView;
	@FXML
	private ImageView imgOpenHours;

	@FXML
	private TableView<MenuVO> menuTable;
	@FXML
	private TextField txtMenuName;
	@FXML
	private TextField txtMenuPrice;
	@FXML
	private Button btnMenuEdit;
	@FXML
	private Button btnMenuDelete;

	// chart analysis tab

	private ObservableList<MemberVO> data;
	private MemberDAO memberDAO;
	
	private int selectedIndex;
	private ObservableList<MemberVO> selectedMember;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberTableViewSetting();
		//cbMemAge.set;
		cbMemAge.setItems(FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70","80","90"));
		btnMemEdit.setOnAction((ActionEvent event)-> {	handlerBtnMemEditAction();	});
	}

	public void memberTableViewSetting() {
		data = FXCollections.observableArrayList();
		memTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colId = new TableColumn("ID");
		colId.setMaxWidth(50);
		colId.setStyle("-fx-alignment:CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("memberID"));

		TableColumn colName = new TableColumn("이름");
		colName.setMaxWidth(50);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colPw = new TableColumn("비밀번호");
		colPw.setMaxWidth(50);
		colPw.setStyle("-fx-alignment:CENTER;");
		colPw.setCellValueFactory(new PropertyValueFactory("password"));

		TableColumn colPhoneNum = new TableColumn("전화번호");
		colPhoneNum.setMaxWidth(50);
		colPhoneNum.setStyle("-fx-alignment:CENTER;");
		colPhoneNum.setCellValueFactory(new PropertyValueFactory("phoneNumer"));

		TableColumn colGender = new TableColumn("성별");
		colGender.setMaxWidth(50);
		colGender.setStyle("-fx-alignment:CENTER;");
		colGender.setCellValueFactory(new PropertyValueFactory("gender"));

		TableColumn colAge = new TableColumn("연령대");
		colAge.setMaxWidth(50);
		colAge.setStyle("-fx-alignment:CENTER;");
		colAge.setCellValueFactory(new PropertyValueFactory("ageGroup"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		memTable.setItems(data);
		memTable.getColumns().addAll(colId, colName, colPw, colPhoneNum, colGender, colAge);

	} // end of memberTableViewSetting

	public void handlerBtnMemEditAction() {
		// 총점과 평균이 있는지 확인
		try {

			if (txtMemId.getText().equals("") || txtMemPw.getText().equals("") || txtMemName.getText().equals("")
					|| txtMemGender.getText().equals("") || txtMemAddr.getText().equals("")) {
				throw new Exception();
			} else {
				MemberVO mvo = new MemberVO(txtMemId.getText(), txtMemPw.getText(), txtMemName.getText(), txtMemPhone.getText(), txtMemAddr.getText(), txtMemGender.getText(), cbMemAge.getSelectionModel().getSelectedItem());

				// 먼저 DB에 추가해야함.
				memberDAO = new MemberDAO();
				int count = memberDAO.getMemberRegiste(mvo); // 이 순간 db에 레코드값 insert됨.
				if (count != 0) {
					data.removeAll(data);
					totalList();
				} else {
					throw new Exception("DB 등록 실패 ");
				}
				SharedMethod.alertDisplay(5, "SUCCESS", "congratulations!", "Registration successfully completed");
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "RESISTERATION FAILED", "error!", e.toString());
		}

	}//end of handlerBtnMemEditAction

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
	}//end of totalList
	
	
}