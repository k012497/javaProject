package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.MemberVO;

public class MyPageController implements Initializable{
	@FXML
	private ComboBox<String> cbAge;
	ObservableList<String> cbAgeList;
	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;
	
	@FXML private Label lblMemberId;
	
	@FXML private Button btnEdit;
	@FXML private Button btnLeave;
	@FXML private Button btnCancel;
	@FXML private TextField txtName;
	@FXML private TextField txtNumber;
	@FXML private TextField txtPw;
	@FXML private TextField txtPwAgain;

	@FXML private TableView favTable;
	@FXML private TableView reviewTable;
	
	ArrayList<MemberVO> memberList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxInitSetting();
		getInfo();
		
	}
	public void getInfo() {
		MemberDAO memberDAO = new MemberDAO();
		try {
			memberList = memberDAO.getMemberInfoUsingId(lblMemberId.getText());
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "LOAD INFORMATION FAILED", "사용자 정보 불러오기 실패", "사용자 정보를 불러오기를 실패했습니다.");
		}
		txtName.setText(memberList.get(0).getName());
		txtNumber.setText(memberList.get(0).getPhoneNumer());
		txtPw.setText(memberList.get(0).getPassword());
		
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
	
}
