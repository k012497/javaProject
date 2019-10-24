package controller;

import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.MemberVO;

public class SignUpController implements Initializable {
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
	
	// 영문 & 숫자만 입력받았는지 검사하는 플래그 
	boolean result1;
	boolean result2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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

		// 11자리의 숫자만 입력받게 하는 함수
		SharedMethod.inputDecimalFormatElevenDigit(txtPhoneNum);
	}

	// 콤보박스 셋팅
	// 구안에 있는 동을 보여주기 위한 콤보 박스 셋팅
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
		// 나이 콤보 박스 셋팅
		cbAgeList = FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90");
		cbAge.setItems(cbAgeList);
		// 성별 콤보 박스 셋팅
		cbGenderList = FXCollections.observableArrayList("여", "남");
		cbGender.setItems(cbGenderList);

	}

	// 중복확인
	public void handleBtnIdCheckAction(ActionEvent e) {
		// 중복확인 버튼 이벤트 처리
		MemberDAO memberDAO = new MemberDAO();
		int i = memberDAO.getMemeberIdSearch(txtId.getText());

		if (i == 1) {
			// 아이디가 중복이 될경우
			SharedMethod.alertDisplay(5, "아이디 중복", "이미 사용중인 아이디입니다.", "다른 아이디를 입력해주세요.");
			return;
		} else if (i == -1) {
			// 아이디가 중복이 되지 않을 경우
			idCheck = true;
			SharedMethod.alertDisplay(5, "아이디 사용가능", "사용할 수 있는 아이디입니다.", "다른 항목도 입력해주세요.");
		}
	}

	// sign up버튼
	public void handleBtnSignUpAction() {
		// 비밀번호가 오류일 경우
		if (txtPw.getText().equals(txtPwCheck.getText())) {
		} else {
			SharedMethod.alertDisplay(1, "비밀번호 오류", "[비밀번호 오류]", "비밀번호 오류입니다 다시 확인 해주세요");
			return;
		}
		Parent mainView = null;
		Stage mainStage = null;

		// 영어와 숫자 이외의 문자는 받지 않는 함수 (false를 반환)
		result1 = SharedMethod.checkOnlyNumberAndEnglish(txtId.getText());
		if(!result1) return;
		result2 = SharedMethod.checkOnlyNumberAndEnglish(txtPw.getText());
		if(!result2) return;
		SharedMethod.inputDecimalFormatElevenDigit(txtPhoneNum);

		// 콤보박스 안에 값이 없을때 빈칸없이 입력
		if (txtId.getText().equals("") || txtName.getText().equals("") || txtPhoneNum.getText().equals("")
				|| txtPw.getText().equals("") || cbAge.getValue() == null || cbDong.getValue() == null
				|| cbGu.getValue() == null || cbGender.getValue() == null) {

			SharedMethod.alertDisplay(1, "빈칸 있음", "빈칸 없이 입력 해주세요", "재입력 바랍니다");
		}

		else {
			// 중복확인하는 검사
			if (idCheck) {
				try {
					MemberDAO memberDAO = new MemberDAO();
					MemberVO mvo = new MemberVO(txtId.getText().trim(), txtPw.getText(), txtName.getText().trim(),
							txtPhoneNum.getText().trim(), cbGu.getValue() + " " + cbDong.getValue(),
							cbGender.getValue(), cbAge.getValue());
					memberDAO.getMemberRegiste(mvo);
				} catch (Exception e) {
				}
				try {
					mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
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

				SharedMethod.alertDisplay(5, "회원가입", "회원가입", "회원가입성공 : 회원가입을 축하드립니다.");

				Stage stage1 = (Stage) btnSignUp.getScene().getWindow();
				stage1.close();
			} else {
				SharedMethod.alertDisplay(1, "ID 중복 체크", "ID 중복 체크를 하지 않음", "ID 중복 체크를 눌러주세요");
			}
		}

	}

	// cancel버튼
	public void handelBtnCancelAction() {

		Parent mainView = null;
		Stage mainStage = null;

		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(mainView);
			mainStage = new Stage();
			mainStage.setTitle("[김시스터즈]");
			mainStage.setScene(scene);
			mainStage.setResizable(true);
			((Stage) btnCancel.getScene().getWindow()).close();
			mainStage.show();
		} catch (Exception e1) {
			SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
		}
	}

}
