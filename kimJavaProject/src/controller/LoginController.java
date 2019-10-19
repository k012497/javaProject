package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {
	@FXML
	private TextField txtPw;
	@FXML
	private TextField txtId;

	@FXML
	private Button btnSignIn;
	@FXML
	private Label lblSignUp;
	@FXML
	private Label lblFindId;
	@FXML
	private Label lblFindPw;
	@FXML
	private ImageView imgAdmin;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblFindId.setOnMousePressed((e) -> handlerLabelFindId());
		lblFindPw.setOnMousePressed((e) -> handlerLabelFindPw());
	}

	public void handlerLabelFindPw() {
		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/findPw.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(lblFindId.getScene().getWindow());
			stage.setTitle("비밀번호 찾기");

			Button btnOk = (Button) barChartRoot.lookup("#btnOk");
			Button btnCancel = (Button) barChartRoot.lookup("#btnCancel");
			TextField txtName = (TextField) barChartRoot.lookup("#txtName");
			TextField txtPhoneNum = (TextField) barChartRoot.lookup("#txtPhoneNum");
			TextField txtId = (TextField) barChartRoot.lookup("#txtId");

			btnCancel.setOnAction((e3) -> {
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handlerLabelFindId() {
		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/findID.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(lblFindId.getScene().getWindow());
			stage.setTitle("아이디 찾기");

			Button btnOk = (Button) barChartRoot.lookup("#btnOk");
			Button btnCancel = (Button) barChartRoot.lookup("#btnCancel");
			TextField txtName = (TextField) barChartRoot.lookup("#txtName");
			TextField txtPhoneNum = (TextField) barChartRoot.lookup("#txtPhoneNum");

			btnOk.setOnAction((event) -> {
	            inputDecimalFormatThirteenDigit(txtPhoneNum);
				String existID = null;
				try {
					existID = MemberDAO.findIDByPhone(txtPhoneNum.getText(), txtName.getText());
					System.out.println("&&&&&" + existID);
					int existPW = 0;
					if (existID == null) {
						SharedMethod.alertDisplay(1, "ID 찾기 실패", "ID찾기실패", "[ID 찾기 실패]: 존재하지 않는 회원정보 입니다.");
						return;
					} else {
						SharedMethod.alertDisplay(1, "ID 찾기 성공", "ID찾기성공",
								"[ID 찾기 성공]:  회원님의 아이디는 " + existID + " 입니다.");
						stage.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			btnCancel.setOnAction((e3) -> {
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void inputDecimalFormatThirteenDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("##############");

		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치 를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 12) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}
}
