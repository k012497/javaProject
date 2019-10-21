package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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
		// setting for test
		txtId.setText("a123");
		txtPw.setText("123123");

		lblFindId.setOnMousePressed((e) -> handlerLabelFindId());
		lblFindPw.setOnMousePressed((e) -> handlerLabelFindPw());
		btnSignIn.setOnAction((e) -> handlerBtnSignInAction(e));
		lblSignUp.setOnMousePressed((e) -> handlerLabelSignUpAction(e));

		imgAdmin.setOnMousePressed((e) -> handlerAdminAction(e));
	}

	public void handlerAdminAction(MouseEvent e) {

		Stage mainStage = null;
		try {
			Scene scene = new Scene(new StackPane());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminPage.fxml"));
			scene.setRoot(loader.load());
			AdminController controller = loader.getController();
			controller.init();
			mainStage = new Stage();
			mainStage.setScene(scene);
			mainStage.show();
			mainStage.setResizable(true);
			((Stage) btnSignIn.getScene().getWindow()).close();
			mainStage.show();
		} catch (Exception e1) {
			SharedMethod.alertDisplay(1, "ADMIN PAGE LOAD FAILED", "관리자 모드 전환 실패", e1.toString() + e1.getMessage());
		}
	}

	// 1. SingIn으로 로그인
	public void handlerBtnSignInAction(ActionEvent e) {
		if (txtId.getText().trim().equals("") || txtPw.getText().trim().equals("")) {
			SharedMethod.alertDisplay(1, "로그인 실패", "아이디, 패스워드 미입력", "다시 제대로 입력하시오");
		} else {

			Parent mainView = null;
			Stage mainStage = null;

			try {
				mainView = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
				Scene scene = new Scene(mainView);
				mainStage = new Stage();
				mainStage.setTitle("[김시스터즈]");
				mainStage.setScene(scene);
				mainStage.setResizable(true);

				Label lblMember = (Label) mainView.lookup("#lblMember");
				lblMember.setText(txtId.getText());

				// 현재의 스테이지를 닫고 새로운창을 연다.
				((Stage) btnSignIn.getScene().getWindow()).close();
				mainStage.show();
			} catch (Exception e1) {
				SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
			}
		}
	}

	// 2. 회원가입 이벤트 처리
	public void handlerLabelSignUpAction(MouseEvent e) {
		Parent mainView = null;
		Stage mainStage = null;

		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/signUp.fxml"));
			Scene scene = new Scene(mainView);
			mainStage = new Stage();
			mainStage.setTitle("[김시스터즈]");
			mainStage.setScene(scene);
			mainStage.setResizable(true);
			((Stage) btnSignIn.getScene().getWindow()).close();
			mainStage.show();
		} catch (Exception e1) {
			SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
		}
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

			btnOk.setOnAction(e -> {
				if (txtId.getText().equals("")) {
					SharedMethod.alertDisplay(1, "오류발생", "오류가 발생하였습니다.", "[ID양식 오류] : 아이디를 다시 입력 하세요. ");
				} else {
					try {
						String existPW = MemberDAO.findPWByPhone(txtName.getText(), txtPhoneNum.getText(),
								txtId.getText());
						System.out.println(existPW);

						if (existPW == null) {
							SharedMethod.alertDisplay(1, "경고", "pw찾기 실패", "[PW 찾기 실패]: 존재하지 않는 회원정보 입니다.");
							return;
						} else {
							SharedMethod.alertDisplay(1, "성공", "ID찾기 성공",
									"[ID 찾기 성공]: 회원님의 패스워드는 " + existPW + " 입니다.");
							stage.close();
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});

			// 취소 버튼 누르면 다시 로그인 페이지로 돌아가기
			btnCancel.setOnAction((e3) -> {
				Parent mainView = null;
				Stage mainStage = null;

				try {
					mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
					Scene scene = new Scene(mainView);
					mainStage = new Stage();
					mainStage.setTitle("[김시스터즈]");
					mainStage.setScene(scene);
					mainStage.setResizable(true);
					((Stage) btnSignIn.getScene().getWindow()).close();
					mainStage.show();
				} catch (Exception e1) {
					SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
				}
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
				SharedMethod.inputDecimalFormatThirteenDigit(txtPhoneNum);
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

			// 취소 버튼 누르면 다시 로그인 페이지로 돌아가기
			btnCancel.setOnAction((e3) -> {
				Parent mainView = null;
				Stage mainStage = null;

				try {
					mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
					Scene scene = new Scene(mainView);
					mainStage = new Stage();
					mainStage.setTitle("[김시스터즈]");
					mainStage.setScene(scene);
					mainStage.setResizable(true);
					((Stage) btnSignIn.getScene().getWindow()).close();
					mainStage.show();
				} catch (Exception e1) {
					SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
				}
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
