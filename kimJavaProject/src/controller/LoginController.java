package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

		// 아이디 찾기
		lblFindId.setOnMousePressed((e) -> handlerLabelFindId());

		// 비밀번호 찾기
		lblFindPw.setOnMousePressed((e) -> handlerLabelFindPw());

		// 로그인
		btnSignIn.setOnAction((e) -> handlerBtnSignInAction(e));
		// 비밀번호 패스워드필드에 엔터키를 누르면 버튼을 눌리게 하는 함수
		txtPw.setOnKeyPressed(e1 -> {
			if (e1.getCode().equals(KeyCode.ENTER))
				handlerBtnSignInAction(e1);
		});

		// 회원가입
		lblSignUp.setOnMousePressed((e) -> handlerLabelSignUpAction(e));

		// 관리자 모드
		imgAdmin.setOnMousePressed((e) -> handlerAdminAction(e));
	}

	// 1-1. Login ActionEvent
	public void handlerBtnSignInAction(ActionEvent e) {
		handlerSignInAction();
	}

	// 1-2. Login KeyEvent
	public void handlerBtnSignInAction(KeyEvent e1) {
		handlerSignInAction();
	}

	public void handlerAdminAction(MouseEvent e) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/adminPW.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(lblFindId.getScene().getWindow());
			stage.setTitle("아이디 찾기");

			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			TextField txtPw = (TextField) root.lookup("#txtPw");

			btnCancel.setOnAction((e3) -> {
				stage.close();
			});

			txtPw.setOnKeyPressed((e1) -> {
				if (e1.getCode().equals(KeyCode.ENTER)) {
					if (txtPw.getText().equals("01240802")) {
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
						} catch (Exception e2) {
							SharedMethod.alertDisplay(1, "ADMIN PAGE LOAD FAILED", "관리자 모드 전환 실패",
									e2.toString() + e2.getMessage());
						}
					} else {
						System.out.println("틀렸지롱");
					}
				}
			});

			btnOk.setOnAction((e4) -> {
				if (txtPw.getText().equals("01240802")) {
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
						SharedMethod.alertDisplay(1, "ADMIN PAGE LOAD FAILED", "관리자 모드 전환 실패",
								e1.toString() + e1.getMessage());
					}
				} else {
					System.out.println("틀렸지롱");
				}
			});

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			System.out.println("비번창 호출 실패 ");
		}

	}

	// 1. SingIn으로 로그인
	public void handlerSignInAction() {

		MemberDAO memberDAO = new MemberDAO();
		String signIn = MemberDAO.getuserIDPW(txtId.getText(), txtPw.getText());

		if (txtId.getText().trim().equals("") || txtPw.getText().trim().equals("")) {
			SharedMethod.alertDisplay(1, "로그인 실패", "아이디, 패스워드 미입력", "다시 제대로 입력하시오");
		} else if (signIn == null) {
			SharedMethod.alertDisplay(1, "ID/PASSWORD 오류", "아이디 또는 비밀번호가 맞지않습니다.", "아이디 또는 비밀번호를 확인해주세요.");
		} else {
			Parent mainView = null;
			Stage mainStage = null;
			SharedMethod.alertDisplay(5, "로그인 성공", "맛있을지도에 오신걸 환영합니다",
					"저희 맛있을 지도엔 사용자가 지역을\n선택시 지역안에서 다양한 음식의 메뉴로 나누어\n원하는 음식점을 찾게 해주는 프로그램입니다\n저희 [맛있을 지도]에서 다양한 음식을 만나 보세요 #><#");
			try {
				mainView = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
				Scene scene = new Scene(mainView);
				mainStage = new Stage();
				mainStage.setTitle("[김시스터즈]");
				mainStage.setScene(scene);
				mainStage.setResizable(true);

				Label lblMember = (Label) mainView.lookup("#lblMember");
				lblMember.setText(txtId.getText());

				// 현재의 스테이지를 닫음
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

	// 3. ID찾기
	public void handlerLabelFindId() {
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

			SharedMethod.inputDecimalFormatThirteenDigit(txtPhoneNum);

			btnOk.setOnAction((event) -> {
				SharedMethod.inputDecimalFormatThirteenDigit(txtPhoneNum);
				String existID = null;
				try {
					existID = MemberDAO.findIDByPhone(txtPhoneNum.getText(), txtName.getText());
					System.out.println("existID" + existID);
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

	// 4. PW찾기
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

			SharedMethod.inputDecimalFormatThirteenDigit(txtPhoneNum);

			btnOk.setOnAction(e -> {
				if (txtId.getText().equals("")) {
					SharedMethod.alertDisplay(1, "오류발생", "오류가 발생하였습니다.", "[PW양식 오류] : 패스워드를 다시 입력 하세요. ");
				} else {
					try {
						String existPW = MemberDAO.findPWByPhone(txtName.getText(), txtPhoneNum.getText(),
								txtId.getText());
						System.out.println(existPW);

						if (existPW == null) {
							SharedMethod.alertDisplay(1, "경고", "pw찾기 실패", "[PW 찾기 실패]: 존재하지 않는 회원정보 입니다.");
							return;
						} else {
							SharedMethod.alertDisplay(1, "성공", "PW찾기 성공",
									"[PW 찾기 성공]: 회원님의 패스워드는 " + existPW + " 입니다.");
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

}
