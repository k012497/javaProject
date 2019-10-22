package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MemberVO;
import model.RestaurantVO;

public class MyPageController implements Initializable {
	@FXML
	private ComboBox<String> cbAge;
	ObservableList<String> cbAgeList;
	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;

	@FXML
	private Label lblMemberId;

	@FXML
	private Button btnEdit;
	@FXML
	private Button btnLeave;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtNumber;
	@FXML
	private TextField txtPw;
	@FXML
	private TextField txtPwAgain;

	@FXML
	private TableView<RestaurantVO> favTable;
	@FXML
	private TableView reviewTable;

	ArrayList<MemberVO> memberList;
	private ObservableList<RestaurantVO> favData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxInitSetting();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getMemberInfo();
			}
		});
		favTableColSetting();
		totalFavList();

		
		btnCancel.setOnAction((e)->handlerButtonCancel());
		
		btnEdit.setOnAction((e) -> handlerEditButtonAction());
	}

	public void handlerEditButtonAction() {
		try {
			//////if 비번v필드 일치 확인///////
			if ( txtName.getText().equals("") || txtNumber.getText().equals("")
					|| txtPw.getText().equals("") || cbGu.getValue().equals("") || cbDong.getValue().equals("") || cbAge.getValue().equals("")) {
				throw new Exception();
			} else {
				MemberVO mvo = new MemberVO(txtPw.getText(), txtName.getText(), txtNumber.getText(), cbGu.getValue() + " " + cbDong.getValue(), cbAge.getValue());
				
				MemberDAO memberDAO = new MemberDAO();
				MemberVO memberVO = memberDAO.getMemberUpdate(mvo, lblMemberId.getText());
				getMemberInfo();
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
		}
	}

	public void handlerButtonCancel() {
		Stage stage = (Stage) (btnCancel.getScene().getWindow());
		stage.close();
	}

	public void getMemberInfo() {
		MemberDAO memberDAO = new MemberDAO();
		try {
			System.out.println(lblMemberId.getText()+"님");
			memberList = memberDAO.getMemberInfoUsingId(lblMemberId.getText());
			System.out.println(memberList.get(0).getName());
			txtName.setText(memberList.get(0).getName());
			txtNumber.setText(memberList.get(0).getPhoneNumer());
			txtPw.setText(memberList.get(0).getPassword());
			cbAge.setValue(memberList.get(0).getAgeGroup());
			
			String addr = memberList.get(0).getAddress();
			
			// 공백 의 인덱스를 찾는다
			int idx = addr.indexOf(" ");
			String gu = addr.substring(0, idx); //공백 앞부분 
			String dong = addr.substring(idx + 1); //공백 뒷부분
			cbGu.setValue(gu);
			cbDong.setValue(dong);
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "LOAD INFORMATION FAILED", "사용자 정보 불러오기 실패", "사용자 정보를 불러오기를 실패했습니다.");
		}

	}

	public void comboBoxInitSetting() {
		AddressDAO addressDAO = new AddressDAO();
		addressGuList = addressDAO.getGu();
		cbGu.setItems(addressGuList);
		cbGu.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				addressDongList = addressDAO.getDong(cbGu.getValue());
				cbDong.setItems(addressDongList);
			}
		});

		cbAgeList = FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90");
		cbAge.setItems(cbAgeList);

	}

	public void totalFavList() {
		ArrayList<RestaurantVO> list = null;
		RestaurantDAO restDAO = new RestaurantDAO();
		RestaurantVO restVO = null;
		list = restDAO.getListForFav(lblMemberId.getText());

		if (list == null) {
			SharedMethod.alertDisplay(1, "warning", "ERROR in CALLING DB", "please check again");
		}

		for (int i = 0; i < list.size(); i++) {
			restVO = list.get(i);
			favData.add(restVO);
		}
	}// end of totalList

	public void favTableColSetting() {
		favData = FXCollections.observableArrayList();
		favTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colName = new TableColumn("상호명");
		colName.setMaxWidth(150);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("restaurantName"));

		TableColumn colAddr = new TableColumn("주소");
		colAddr.setMaxWidth(200);
		colAddr.setStyle("-fx-alignment:CENTER;");
		colAddr.setCellValueFactory(new PropertyValueFactory("address"));

		TableColumn colKind = new TableColumn("음식 종류");
		colKind.setMaxWidth(100);
		colKind.setStyle("-fx-alignment:CENTER;");
		colKind.setCellValueFactory(new PropertyValueFactory("kind"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		favTable.setItems(favData);
		favTable.getColumns().addAll(colName, colAddr, colKind);

	} // end of favTableColSetting

}
