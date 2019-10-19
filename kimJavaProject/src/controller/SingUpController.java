package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.MemberVO;

public class SingUpController implements Initializable {
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPw;
	@FXML
	private PasswordField txtPwCheck;
	@FXML
	private TextField txtName;
	@FXML
	private ComboBox<String> cbAge;
	ObservableList<String> cbAgeList;
	@FXML
	private ComboBox<String> cbGender;
	ObservableList<String> cbGenderList;
	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;
	@FXML
	private TextField txtPhoneNum;
	@FXML
	private Button btnIdCheck;
	@FXML
	private Button btnSignUp;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblPwCheck;

	ArrayList<MemberVO> dbMember;
	boolean idCheck = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			Connection con = DBUtil.getConnection();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// 콤보박스 셋팅
		comboBoxInitSetting();

		// 중복확인
		btnIdCheck.setOnAction(e -> {
			handleBtnIdCheckAction(e);
		});

		// signUp버튼
		btnSignUp.setOnAction(e -> {
			handleBtnSignUpAction();
		});

		// cancel버튼
		btnCancel.setOnAction(e -> {
			handelBtnCancelAction();
		});
	}

	// 콤보박스 셋팅
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
		/*
		 * /교수님버전/ String[] array1 = new String[] { "강서구", "강남구", "강북구" }; String[][]
		 * array2 = new String[][] { { "남동", "강동", "다동" }, { "남동2", "강동2", "다동2" }, {
		 * "남동3", "강동3", "다동3" } };
		 * 
		 * ObservableList<String> array1List = FXCollections.observableArrayList();
		 * for(int i=0;i<array1.length;i++) { array1List.add(array1[i]); }
		 * cbGu.setItems(array1List); cbGu.setOnAction(e->{ int in =
		 * cbGu.getSelectionModel().getSelectedIndex(); System.out.println("in="+in);
		 * ObservableList<String> array2List = FXCollections.observableArrayList();
		 * for(int i = 0;i<array2[in].length;i++){ array2List.add(array2[in][i]); }
		 * cbDong.setItems(array2List);
		 * 
		 * });
		 */

		cbAgeList = FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90");
		cbAge.setItems(cbAgeList);

		cbGenderList = FXCollections.observableArrayList("여", "남");
		cbGender.setItems(cbGenderList);

	}

	// 중복확인
	public void handleBtnIdCheckAction(ActionEvent e) {
		// 1. 중복확인 버튼 이벤트 처리
		MemberDAO memberDAO = new MemberDAO();
		int i = memberDAO.getUserIdSearch(txtId.getText());

		if (i == 1) {
			SharedMethod.alertDisplay(1, "아이디 중복", "이미 사용중인 아이디입니다.", "다른 아이디를 입력해주세요.");
			return;
		} else if (i == -1) {
			idCheck = true;
			SharedMethod.alertDisplay(1, "아이디 사용가능", "사용할 수 있는 아이디입니다.", "다른 항목도 입력해주세요.");
		}
	}

	// sign up버튼
	public void handleBtnSignUpAction() {
		Parent mainView = null;
		Stage mainStage = null;
		if (txtId.getText().equals("") || txtName.getText().equals("") || txtPhoneNum.getText().equals("")
				|| txtPw.getText().equals("") || txtPwCheck.getText().equals("") || cbAge.getValue().equals("")
				|| cbDong.getValue().equals("") || cbGu.getValue().equals("") || cbGender.getValue().equals("")) {
			SharedMethod.alertDisplay(1, "빈칸 있음", "빈칸 없이 입력 해주세요", "재입력 바랍니다");
		} else {
			if (idCheck) {
				try {
					MemberDAO memberDAO = new MemberDAO();
					MemberVO mvo = new MemberVO(txtId.getText().trim(), txtPw.getText(), txtName.getText().trim(),
							txtPhoneNum.getText().trim(), cbGu.getValue() + " " + cbDong.getValue(), cbGender.getValue(), cbAge.getValue());
					memberDAO.getMemberRegiste(mvo);
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					mainView = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
					Scene scene = new Scene(mainView);
					mainStage = new Stage();
					mainStage.setTitle("[김시스터즈]");
					mainStage.setScene(scene);
					mainStage.setResizable(true);
					((Stage) btnSignUp.getScene().getWindow()).close();
					mainStage.show();
				} catch (Exception e1) {
					SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
				}
				MemberVO memberID = new MemberVO();
				int count = 1;
//            int count = MemberDAO.insertMemberDataSearch(memberID);

				// if(memberID.getText().equals("") || memberPassword.getText().equals(""))

				if (count != 0) {
					SharedMethod.alertDisplay(5, "회원가입", "회원가입", "회원가입성공 : 회원가입을 축하드립니다.");
				}
				Stage stage1 = (Stage) btnSignUp.getScene().getWindow();
				stage1.close();
			} else {
				SharedMethod.alertDisplay(1, "ID 중복 체크", "ID 중복 체크를 하지 않음", "ID 중복 체크를 눌러주세요");
			}
		}

	}

	// cancel버튼
	public void handelBtnCancelAction() {
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}

}