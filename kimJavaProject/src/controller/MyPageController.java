package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MemberVO;
import model.RestaurantVO;
import model.ReviewJoinRestaurantVO;
import model.ReviewVO;

public class MyPageController implements Initializable {
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
	private Tab favTab;
	@FXML
	private Tab reviewTab;

	@FXML
	private TableView<RestaurantVO> favTable;
	private ObservableList<RestaurantVO> favData;
	@FXML
	private TableView<ReviewJoinRestaurantVO> reviewTable;
	private ObservableList<ReviewJoinRestaurantVO> reviewData;

	ArrayList<MemberVO> memberList;

	private boolean favFlag = false;
	private boolean reviewFlag = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 콤보박스 세팅
		comboBoxInitSetting();

		// 접속한 멤버 정보 불러오기
		Platform.runLater(() -> getMemberInfo());

		// 테이블뷰 컬럼 세팅
		favTableColSetting();
		reviewTableColSetting();

		btnCancel.setOnAction((e) -> handlerButtonCancel());
		btnLeave.setOnAction((e) -> handlerButtonLeaveAction());
		btnEdit.setOnAction((e) -> handlerEditButtonAction());

		// 탭을 열면 해당 테이블뷰에 데이터를 가져옴
		// 창이 다시 실행되지 않는 한 테이블뷰 로드는 한 번만 하도록 함
		favFlag = true;
		favTab.setOnSelectionChanged((e) -> {
			handlerFavTabAction();
		});

		reviewFlag = true;
		reviewTab.setOnSelectionChanged((e) -> {
			handlerReviewTabAction();
		});

	}

	public void handlerEditButtonAction() {
		if (txtPw.getText().equals(txtPwAgain.getText())) {
		} else {
			SharedMethod.alertDisplay(1, "비밀번호 오류", "[비밀번호 오류]", "비밀번호 오류입니다 다시 확인 해주세요");
			return;
		}
		
		try {
			if (txtPw.getText().equals("") || txtName.getText().equals("") || txtNumber.getText().equals("")
					|| cbGu.getValue().equals("") || cbDong.getValue().equals("") || cbGender.getValue().equals("")) {
				throw new Exception();
			} else {
				MemberVO mvo = new MemberVO(lblMemberId.getText(), txtPw.getText(), txtName.getText(),
					txtNumber.getText(), cbGu.getValue() + " " + cbDong.getValue() , cbGender.getValue(),
					cbAge.getValue());
				
				MemberDAO memberDAO = new MemberDAO();
				MemberVO memberVO = memberDAO.getMemberUpdate(mvo, lblMemberId.getText());
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
		}
		

//		SharedMethod.alertDisplay(1, "정보수정", "정보수정", "정보 수정을 하려면 관리자에게 카톡으로 문의 해주세요 \n\n\n\n\n카카오톡 : @맛있을지도 김시스터즈");

//		try {
//			//////if 비번v필드 일치 확인///////
//			if ( txtName.getText().equals("") || txtNumber.getText().equals("")
//					|| txtPw.getText().equals("") || cbGu.getValue().equals("") || cbDong.getValue().equals("") || cbAge.getValue().equals("")) {
//				throw new Exception();
//			} else {
//				MemberVO mvo = new MemberVO(txtPw.getText(), txtName.getText(), txtNumber.getText(), cbGu.getValue() + " " + cbDong.getValue(), cbAge.getValue());
//				
//				MemberDAO memberDAO = new MemberDAO();
//				MemberVO memberVO = memberDAO.getMemberUpdate(mvo, lblMemberId.getText());
//				getMemberInfo();
//			}
//		} catch (Exception e) {
//			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
//		}
	}

	public void handlerButtonCancel() {
		Stage stage = (Stage) (btnCancel.getScene().getWindow());
		stage.close();
	}

	public void getMemberInfo() {
		MemberDAO memberDAO = new MemberDAO();
		try {
			System.out.println(lblMemberId.getText() + "님");
			memberList = memberDAO.getMemberInfoUsingId(lblMemberId.getText());
			System.out.println(memberList.get(0).getName());
			txtName.setText(memberList.get(0).getName());
			txtNumber.setText(memberList.get(0).getPhoneNumer());
			txtPw.setText(memberList.get(0).getPassword());
			cbAge.setValue(memberList.get(0).getAgeGroup());
			cbGender.setValue(memberList.get(0).getGender());

			String addr = memberList.get(0).getAddress();

			// 공백 의 인덱스를 찾는다
			int idx = addr.indexOf(" ");
			String gu = addr.substring(0, idx); // 공백 앞부분
			String dong = addr.substring(idx + 1); // 공백 뒷부분
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
		
		cbGenderList = FXCollections.observableArrayList("여", "남");
		cbGender.setItems(cbGenderList);

	}

	public void handlerButtonLeaveAction() {
		try {
			MemberDAO memberDAO = new MemberDAO();
			// SharedMethod.alertDisplay(2, "회원탈퇴", "회원탈퇴", "회원을 탈퇴하세요?");
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("회원을 탈퇴 하시겠습니까?");
			alert.setContentText("[ok]를 누르면 회원님의 탈퇴를 진행하겠습니다\n\n\n\n\n\n [cancel]을 누르면 누르기 전으로 돌아갑니다 ");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				// memberDAO.getMemberDelete(lblMemberId.getText());
				SharedMethod.alertDisplay(1, "bye", "USER_BYE",
						"[ok]를 누르면 모든 프로그램이 종료 됩니다\n그동안 저희 프로그램을 이용해 주셔서 감사합니다 (#><#)\n");
				Platform.exit();
			} else {
				// 취소를 누르면 다시 원래의 창으로 돌아 간다
			}
		} catch (Exception e) {

		}
	}

	public void favTableColSetting() {
		favData = FXCollections.observableArrayList();
		favTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colName = new TableColumn("상호명");
		colName.setPrefWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("restaurantName"));

		TableColumn colAddr = new TableColumn("주소");
		colAddr.setPrefWidth(200);
		colAddr.setStyle("-fx-alignment:CENTER;");
		colAddr.setCellValueFactory(new PropertyValueFactory("address"));

		TableColumn colKind = new TableColumn("음식 종류");
		colKind.setMaxWidth(100);
		colKind.setStyle("-fx-alignment:CENTER;");
		colKind.setCellValueFactory(new PropertyValueFactory("kind"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		// favTable.setItems(favData);
		favTable.getColumns().addAll(colName, colAddr, colKind);

	} // end of favTableColSetting

	public void handlerFavTabAction() {
		if (favFlag) {
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
			favTable.setItems(favData);
		}
		favFlag = false;
	}

	public void handlerReviewTabAction() {
		if (reviewFlag) {
			ArrayList<ReviewJoinRestaurantVO> list = null;
			ReviewDAO reviewDAO = new ReviewDAO();
			ReviewJoinRestaurantVO reviewVO = null;
			list = reviewDAO.getReveiw(lblMemberId.getText());

			if (list == null) {
				SharedMethod.alertDisplay(1, "warning", "ERROR in CALLING DB", "please check again");
			}

			for (int i = 0; i < list.size(); i++) {
				reviewVO = list.get(i);
				reviewData.add(reviewVO);
			}
			reviewTable.setItems(reviewData);
		}
		reviewFlag = false;
	}

	public void reviewTableColSetting() {
		reviewData = FXCollections.observableArrayList();
		reviewTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colName = new TableColumn("상호명");
		colName.setPrefWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("restaurantName"));

		TableColumn colStars = new TableColumn("별점");
		colStars.setMaxWidth(60);
		colStars.setStyle("-fx-alignment:CENTER;");
		colStars.setCellValueFactory(new PropertyValueFactory("stars"));

		TableColumn colDate = new TableColumn("등록일");
		colDate.setPrefWidth(150);
		colDate.setStyle("-fx-alignment:CENTER;");
		colDate.setCellValueFactory(new PropertyValueFactory("registeDate"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		reviewTable.getColumns().addAll(colName, colStars, colDate);

	} // end of reviewColSetting

}
