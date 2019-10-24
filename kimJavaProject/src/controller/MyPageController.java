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

/*
 * 사용자가 본인의 정보(인적 정보, 즐겨찾기 목록, 별점 목록)을 관리할 수 있는 페이지를 위한 컨트롤러
 * 만든이 : 김소진 
 */
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
	private Button btnFavDelete;
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

	private int selectedIndex;
	private ObservableList<RestaurantVO> selectedFav;
	String selectedRestName;
	
	// 영문 & 숫자만 입력받았는지 검사하는 플래그 
	boolean result1;
	boolean result2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 콤보박스 세팅
		comboBoxInitSetting();

		// 접속한 멤버 정보 불러오기
		Platform.runLater(() -> getMemberInfo());

		// 테이블뷰 컬럼 세팅
		favTableColSetting();
		reviewTableColSetting();

		// 테이블 뷰 속 객체를 눌렀을 때
		favTable.setOnMousePressed((e) -> handlerTableViewPressedAction());

		// 메인으로 버튼을 눌렀을 때
		btnCancel.setOnAction((e) -> handlerButtonCancel());

		// 회원탈퇴 버튼을 눌렀을 때
		btnLeave.setOnAction((e) -> handlerButtonLeaveAction());

		// 정보 수정 버튼을 눌렀을 때
		btnEdit.setOnAction((e) -> handlerEditButtonAction());

		// 찜뽕 취소 버튼을 눌렀을 때
		btnFavDelete.setOnAction((e) -> handlerButtonDeleteAction());

		/*
		 * my찜뽕 탭을 열면 테이블뷰에 각 사용자에 해당하는 데이터를 가져오는 이벤트 처리 favFlag를 이용하여 창이 다시 실행되지 않는 한
		 * 테이블뷰 로드는 한 번만 하도록 함 만든이 : 김소진
		 */
		favFlag = true;
		favTab.setOnSelectionChanged((e) -> {
			handlerFavTabAction();
		});

		/*
		 * my review 탭을 열면 테이블뷰에 각 사용자에 해당하는 데이터를 가져오는 이벤트 처리 reviewFlag를 이용하여 창이 다시
		 * 실행되지 않는 한 테이블뷰 로드는 한 번만 하도록 함 만든이 : 김소진
		 */
		reviewFlag = true;
		reviewTab.setOnSelectionChanged((e) -> {
			handlerReviewTabAction();
		});

		// 휴대폰 번호 필드의 포맷 제한 - 11자리 숫자만
		SharedMethod.inputDecimalFormatElevenDigit(txtNumber);

	}

	// 정보 수정버튼을 눌렀을 때
	public void handlerEditButtonAction() {
		// 패스워드는 영어 또는 숫자만 입력 가능

		result1 = SharedMethod.checkOnlyNumberAndEnglish(txtPw.getText());
		if(!result1) return;
		result2 = SharedMethod.checkOnlyNumberAndEnglish(txtPwAgain.getText());
		if(!result1 || !result2) return;

		if (txtPw.getText().equals(txtPwAgain.getText())) {
		} else {
			SharedMethod.alertDisplay(1, "비밀번호 오류", "[비밀번호 오류]", "비밀번호 오류입니다 다시 확인 해주세요");
			return;
		}

		// 비어있는 필드가 있을 경우 경고창 표시
		try {
			if (txtPw.getText().equals("") || txtName.getText().equals("") || txtNumber.getText().equals("")
					|| cbGu.getValue().equals("") || cbDong.getValue().equals("") || cbGender.getValue().equals("")) {
				throw new Exception();
			} else {
				MemberVO mvo = new MemberVO(lblMemberId.getText(), txtPw.getText(), txtName.getText(),
						txtNumber.getText(), cbGu.getValue() + " " + cbDong.getValue(), cbGender.getValue(),
						cbAge.getValue());

				MemberDAO memberDAO = new MemberDAO();
				memberDAO.getMemberUpdate(mvo, lblMemberId.getText());
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", e.toString());
		}
	}

	// 창을 닫는 메소드
	public void handlerButtonCancel() {
		Stage stage = (Stage) (btnCancel.getScene().getWindow());
		stage.close();
	}

	// 접속중인 사용자의 정보를 필드 및 콤보박스에 세팅하는 메소드
	public void getMemberInfo() {
		MemberDAO memberDAO = new MemberDAO();
		try {
			memberList = memberDAO.getMemberInfoUsingId(lblMemberId.getText());
			txtName.setText(memberList.get(0).getName());
			txtNumber.setText(memberList.get(0).getPhoneNumer());
			txtPw.setText(memberList.get(0).getPassword());
			cbAge.setValue(memberList.get(0).getAgeGroup());
			cbGender.setValue(memberList.get(0).getGender());

			String addr = memberList.get(0).getAddress();

			// 공백의 인덱스를 찾는다
			int idx = addr.indexOf(" ");
			String gu = addr.substring(0, idx); // 공백 앞부분 (구)
			String dong = addr.substring(idx + 1); // 공백 뒷부분 (동)
			cbGu.setValue(gu);
			cbDong.setValue(dong);
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "LOAD INFORMATION FAILED", "사용자 정보 불러오기 실패", "사용자 정보를 불러오기를 실패했습니다.");
		}

	}

	// 콤보박스 세팅
	public void comboBoxInitSetting() {
		AddressDAO addressDAO = new AddressDAO();
		addressGuList = addressDAO.getGu();
		cbGu.setItems(addressGuList);

		// 구를 선택하면 각 구에 속한 동의 리스트를 세팅하는 이벤트 처리
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

	// 회원 탈퇴를 할 것인지 예/아니오를 받아서 처리하는 메소드
	public void handlerButtonLeaveAction() {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("회원을 탈퇴 하시겠습니까?");
			alert.setContentText("[ok]를 누르면 회원님의 탈퇴를 진행하겠습니다\n\n\n\n\n\n [cancel]을 누르면 누르기 전으로 돌아갑니다 ");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				SharedMethod.alertDisplay(1, "bye", "USER_BYE",
						"[ok]를 누르면 모든 프로그램이 종료 됩니다\n그동안 저희 프로그램을 이용해 주셔서 감사합니다 (#><#)\n");
				Platform.exit();
			}
		} catch (Exception e) {

		}
	}

	// my 찜뽕 테이블 컬럼 세팅
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

	// 해당 사용자의 찜 목록을 가져와서 테이블뷰에 세팅하는 메소드
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

	// 테이블 뷰에서 선택한 식당의 이름 정보를 저장해두는 메소드
	public void handlerTableViewPressedAction() {
		try {
			// 누른 위치와 해당 객체를 가져온다
			selectedIndex = favTable.getSelectionModel().getSelectedIndex();
			selectedFav = favTable.getSelectionModel().getSelectedItems();
			selectedRestName = selectedFav.get(0).getRestaurantName();
		} catch (Exception e) {
			SharedMethod.alertDisplay(5, "해당 식당 정보 로드 오류", "해당 식당 정보 로드 오류", "선택한 식당의 정보를 가져오지 못했습니다");
		}
	}

	// 선택한 식당을 삭제하는 메소드
	public void handlerButtonDeleteAction() {
		try {
			// 선택한 식당의 이름을 이용해 식당의 아이디를 받아옴 
			RestaurantDAO restDAO = new RestaurantDAO();
			RestaurantVO rvo = restDAO.getRestByName(selectedRestName).get(0);
			int restId = rvo.getRestaurantID();
			
			// 식당의 아이디와 사용자 아이디로 삭제 실행
			FavoriteDAO favDAO = new FavoriteDAO();
			favDAO.getFavDelete(restId, lblMemberId.getText());
			
			// 테이블 뷰에 새로 정보 가져오기
			favFlag = true;
			favData.removeAll(favData);
			handlerFavTabAction();
			
		} catch (Exception e) {
			SharedMethod.alertDisplay(5, "즐겨찾기 삭제 실패", "즐겨찾기 삭제 실패", "즐겨찾기 목록에서 삭제하기를 실패하였습니다");
		}
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
