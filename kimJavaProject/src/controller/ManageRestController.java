package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.MenuVO;
import model.OpenVO;
import model.RestaurantVO;
/*
 * 관리자 모드 - 식당관리 탭 
 */
public class ManageRestController implements Initializable {
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
	private TextField txtRestFoodtKind;
	@FXML
	private TextField txtRestName;
	@FXML
	private TextField txtRestPhone;
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
	private HBox openHours;

	@FXML
	private ComboBox<String> cbGu;
	@FXML
	private ComboBox<String> cbDong;

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
	@FXML
	private Button btnNewRest;
	@FXML
	private Button btnNewMenu;

	ObservableList<RestaurantVO> restData;
	ObservableList<MenuVO> menuData;

	// 테이블 뷰를 선택했을 때 위치값과 객체값을 저장할 수있는 변수를 선언
	private int selectedIndex;
	private ObservableList<RestaurantVO> selectedRest;
	private ObservableList<MenuVO> selectedMenu;
	int selectedMenuId;

	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	private String fileName = null;

	private File selectedFile = null;

	// 이미지 처리
	// 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("/Users/kimsojin/Desktop/code/images");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;

	ObservableList<String> addressGuList;
	ObservableList<String> addressDongList;
	
	boolean flag = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// column & default image setting
		restTableColSetting();
		menuTableColSetting();
		imageViewInit();
		menuFieldInitSetting(true, true, true);
		imgView.maxWidth(143);
		imgView.maxHeight(134);

		txtRestDate.setDisable(true);
		txtRestFav.setDisable(true);
		txtRestStars.setDisable(true);

		totalList();

		// imageViewInit();
		btnImage.setOnAction((e) -> {
			handlerBtnImageFileAction(e);
		});

		// 테이블뷰에서 선택 시
		restTable.setOnMousePressed((e) -> handlerTableViewPressedAction());
		menuTable.setOnMousePressed((e) -> handlerMenuTableViewPressedAction());

		// 식당 수정, 삭제 시
		btnRestEdit.setOnAction((e) -> handlerBtnRestEditAction());
		btnRestDelete.setOnAction((e) -> handlerBtnRestDeleteAction());

		// 신규 식당 등록
		btnNewRest.setOnAction((e) -> handlerNewRestAction(e));
	}

	public void handlerBtnMenuDeleteAction() {
		try {
			MenuDAO menuDAO = new MenuDAO();
			menuDAO.getMenuDelete(selectedMenu.get(0).getMenuID());
			menuData.removeAll(menuData);
			menuTable.setItems(menuDAO.getMenu(selectedMenu.get(0).getRestaurantID()));
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "DELETE ERROR", "error", "error");
		}
	}

	public void handlerBtnMenuEditAction() {
		try {
			if (txtMenuName.getText().equals("") || txtMenuPrice.getText().equals("")) {
				throw new Exception();
			} else {
				MenuDAO menuDAO = new MenuDAO();
				MenuVO mvo = new MenuVO(txtMenuName.getText(), Integer.parseInt(txtMenuPrice.getText()));
				menuDAO.getMenuUpdate(mvo, selectedMenuId);

				menuData.remove(selectedIndex); // 선택된 레코드를 지워버리고
				// 해당 식당의 메뉴를 불러온다.
				try {
					// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
					int restID = selectedRest.get(0).getRestaurantID();
					menuData = FXCollections.observableArrayList();
					menuData = menuDAO.getMenu(restID);
					menuTable.setItems(menuData);
				} catch (Exception e) {
					SharedMethod.alertDisplay(1, "메뉴 가져오기 실패", "메뉴 가져오기 실패", "메뉴를 가져오는 데에 실패했습니다.");
				}
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "메뉴 수정 실패", "메뉴 수정에 실패하였습니다");
		}
	}

	public void handlerBtnRestEditAction() {
		try {
			File dirMake = new File(dirSave.getAbsolutePath());

			// 이미지 저장 폴더 생성
			if (!dirMake.exists()) {
				dirMake.mkdir();
			}

			// 이미지 파일 저장 //수정
			if (selectedFile == null) {
				fileName = "default_restaurant.png";
			} else {
				String fileName = imageSave(selectedFile);

			}

			if (txtRestAddr.getText().equals("") || txtRestFoodtKind.getText().equals("")
					|| txtRestName.getText().equals("") || txtRestReserve.getText().equals("")
					|| txtRestTakeout.getText().equals("") || txtRestPark.getText().equals("")) {
				throw new Exception();
			} else {
				RestaurantVO rvo = new RestaurantVO(txtRestName.getText(), txtRestAddr.getText(),
						txtRestPhone.getText(), txtRestFoodtKind.getText(), txtRestVegeKind.getText(), fileName,
						Integer.parseInt(txtRestFav.getText()), Double.parseDouble(txtRestStars.getText()),
						txtRestDate.getText(), txtRestTakeout.getText(), txtRestPark.getText(),
						txtRestReserve.getText());
				// 변경사항을 DB로 보냄.
				RestaurantDAO restaurantDAO = new RestaurantDAO();
				RestaurantVO restaurantVO = restaurantDAO.getRestUpdate(rvo, selectedRest.get(0).getRestaurantID());
				restData.remove(selectedIndex);
				restData.add(selectedIndex, rvo); // 테이블에 들어가버림. setItems(data);해놨으니까.
			}
		} catch (Exception e1) {
			SharedMethod.alertDisplay(1, "CORRECTION FAILED", "error!", "enter ALL value" + e1.getMessage());
		}
	}

	public String imageSave(File file) {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		try {
			// 이미지 파일명 생성
			fileName = "r" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "//" + fileName));

			// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return fileName;
	}

	public void totalList() {
		ArrayList<RestaurantVO> list = null;
		RestaurantDAO restDAO = new RestaurantDAO();
		RestaurantVO restVO = null;
		list = restDAO.getRestTotal();

		if (list == null) {
			SharedMethod.alertDisplay(1, "warning", "ERROR in CALLING DB", "please check again");
		}

		for (int i = 0; i < list.size(); i++) {
			restVO = list.get(i);
			restData.add(restVO);
		}
	}

	public void restTableColSetting() {
		restData = FXCollections.observableArrayList();
		restTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colId = new TableColumn("번호");
		colId.setMaxWidth(50);
		colId.setStyle("-fx-alignment:CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("restaurantID"));

		TableColumn colName = new TableColumn("상호명");
		colName.setPrefWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("restaurantName"));

		TableColumn colAddr = new TableColumn("주소");
		colAddr.setMaxWidth(250);
		colAddr.setStyle("-fx-alignment:CENTER;");
		colAddr.setCellValueFactory(new PropertyValueFactory("address"));

		TableColumn colPhone = new TableColumn("전화번호");
		colPhone.setMaxWidth(140);
		colPhone.setStyle("-fx-alignment:CENTER;");
		colPhone.setCellValueFactory(new PropertyValueFactory("telephone"));

		TableColumn colKind = new TableColumn("음식종류");
		colKind.setMaxWidth(70);
		colKind.setStyle("-fx-alignment:CENTER;");
		colKind.setCellValueFactory(new PropertyValueFactory("kind"));

		TableColumn colVege = new TableColumn("채식단계");
		colVege.setMaxWidth(70);
		colVege.setStyle("-fx-alignment:CENTER;");
		colVege.setCellValueFactory(new PropertyValueFactory("veganLevel"));

		TableColumn colFav = new TableColumn("찜뽕 수");
		colFav.setMaxWidth(60);
		colFav.setStyle("-fx-alignment:CENTER;");
		colFav.setCellValueFactory(new PropertyValueFactory("favCount"));

		TableColumn colStars = new TableColumn("별점");
		colStars.setMaxWidth(60);
		colStars.setStyle("-fx-alignment:CENTER;");
		colStars.setCellValueFactory(new PropertyValueFactory("avgStars"));

		TableColumn colRegiste = new TableColumn("등록일");
		colRegiste.setMaxWidth(140);
		colRegiste.setStyle("-fx-alignment:CENTER;");
		colRegiste.setCellValueFactory(new PropertyValueFactory("registeDate"));

		TableColumn colTake = new TableColumn("포장가능");
		colTake.setMaxWidth(50);
		colTake.setStyle("-fx-alignment:CENTER;");
		colTake.setCellValueFactory(new PropertyValueFactory("takeout"));

		TableColumn colPark = new TableColumn("주차가능");
		colPark.setMaxWidth(50);
		colPark.setStyle("-fx-alignment:CENTER;");
		colPark.setCellValueFactory(new PropertyValueFactory("parking"));

		TableColumn colReserve = new TableColumn("예약가능");
		colReserve.setMaxWidth(50);
		colReserve.setStyle("-fx-alignment:CENTER;");
		colReserve.setCellValueFactory(new PropertyValueFactory("reservation"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		restTable.setItems(restData);
		restTable.getColumns().addAll(colName, colAddr, colPhone, colKind, colVege, colFav, colStars, colRegiste,
				colTake, colPark, colReserve);
	} // end of restaurantTableViewSetting

	public void menuTableColSetting() {
		menuData = FXCollections.observableArrayList();
		menuTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colName = new TableColumn("메뉴");
		colName.setPrefWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("menuName"));

		TableColumn colPrice = new TableColumn("가격");
		colPrice.setPrefWidth(130);
		colPrice.setStyle("-fx-alignment:CENTER;");
		colPrice.setCellValueFactory(new PropertyValueFactory("menuPrice"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		menuTable.setItems(menuData);
		menuTable.getColumns().addAll(colName, colPrice);

	} // end of menuTableViewSetting

	public void imageViewInit() {
		localUrl = "/images/default_restaurant.png";
		localImage = new Image(localUrl, false);
		imgView.setImage(localImage);
	}

	public Object handlerBtnImageFileAction(ActionEvent e) {
		// 이미지 파일 선택 창
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif", "*.jpeg"));

		try {
			selectedFile = fileChooser.showOpenDialog(btnImage.getScene().getWindow());
			if (selectedFile != null) {
				// 이미지 파일 경로
				localUrl = selectedFile.toURI().toURL().toString();
			}
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}

		localImage = new Image(localUrl, false);
		imgView.setImage(localImage);
		imgView.setFitHeight(150);
		imgView.setFitWidth(130);

		if (selectedFile != null) {
			selectFileName = selectedFile.getName();
		}
		return fileChooser;
	}

	public void handlerTableViewPressedAction() {
		// 테이블 뷰 객체 없는 부분 클릭 시 방어

		// 누른 위치와 해당 객체를 가져온다
		selectedIndex = restTable.getSelectionModel().getSelectedIndex();
		selectedRest = restTable.getSelectionModel().getSelectedItems();
		int selectedRestId = selectedRest.get(0).getRestaurantID();

		menuFieldInitSetting(true, false, true);
		btnMenuEdit.setOnAction((e) -> handlerBtnMenuEditAction());
		btnMenuDelete.setOnAction((e) -> handlerBtnMenuDeleteAction());

		openHours.setOnMousePressed((e) -> handlerOpenHours());

		// when click new in restaurant tab
		btnNewMenu.setOnAction((e) -> handlerNewMenuAction(e, selectedRestId));

		try {
			// 가져온 정보를 데이터 필드에 출력
			txtRestName.setText(selectedRest.get(0).getRestaurantName());
			txtRestAddr.setText(selectedRest.get(0).getAddress());
			txtRestFoodtKind.setText(selectedRest.get(0).getKind());
			txtRestVegeKind.setText(selectedRest.get(0).getVeganLevel());
			txtRestPhone.setText(selectedRest.get(0).getTelephone());
			txtRestFav.setText(String.valueOf(selectedRest.get(0).getFavCount()));
			txtRestDate.setText(String.valueOf(selectedRest.get(0).getRegisteDate()));
			txtRestStars.setText(String.valueOf(selectedRest.get(0).getAvgStars()));
			txtRestTakeout.setText(String.valueOf(selectedRest.get(0).getTakeout()));
			txtRestReserve.setText(String.valueOf(selectedRest.get(0).getReservation()));
			txtRestPark.setText(String.valueOf(selectedRest.get(0).getParking()));

			// 클릭 시 이미지뷰 세팅
			String fileName = selectedRest.get(0).getImageFileName();
			selectedFile = new File("/Users/kimsojin/Desktop/code/images/" + fileName);
			if (selectedFile != null) {
				// 이미지 파일 경로
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imgView.setImage(localImage);
				imgView.setFitHeight(150);
				imgView.setFitWidth(130);
			}

		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "필드 출력 실패 ", "필드 출력 실패", "식당 정보를 출력할 수 없습니다.");
		}

		// 해당 식당의 메뉴를 불러온다.
		try {
			// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
			int restID = selectedRest.get(0).getRestaurantID();
			MenuDAO menuDAO = new MenuDAO();
			menuData = FXCollections.observableArrayList();
			menuData = menuDAO.getMenu(restID);
			menuTable.setItems(menuData);
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "메뉴 가져오기 실패", "메뉴 가져오기 실패", "메뉴를 가져오는 데에 실패했습니다.");
		}
	}

	public void handlerOpenHours() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/openHours.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(openHours.getScene().getWindow());
			stage.setTitle("매장 운영 시간");

			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");

			ComboBox<String> cbMonOpen = (ComboBox<String>) root.lookup("#cbMonOpen");
			ComboBox<String> cbTueOpen = (ComboBox<String>) root.lookup("#cbTueOpen");
			ComboBox<String> cbWedOpen = (ComboBox<String>) root.lookup("#cbWedOpen");
			ComboBox<String> cbThuOpen = (ComboBox<String>) root.lookup("#cbThuOpen");
			ComboBox<String> cbFriOpen = (ComboBox<String>) root.lookup("#cbFriOpen");
			ComboBox<String> cbSatOpen = (ComboBox<String>) root.lookup("#cbSatOpen");
			ComboBox<String> cbSunOpen = (ComboBox<String>) root.lookup("#cbSunOpen");

			ComboBox<String> cbMonClose = (ComboBox<String>) root.lookup("#cbMonClose");
			ComboBox<String> cbTueClose = (ComboBox<String>) root.lookup("#cbTueClose");
			ComboBox<String> cbWedClose = (ComboBox<String>) root.lookup("#cbWedClose");
			ComboBox<String> cbThuClose = (ComboBox<String>) root.lookup("#cbThuClose");
			ComboBox<String> cbFriClose = (ComboBox<String>) root.lookup("#cbFriClose");
			ComboBox<String> cbSatClose = (ComboBox<String>) root.lookup("#cbSatClose");
			ComboBox<String> cbSunClose = (ComboBox<String>) root.lookup("#cbSunClose");

			CheckBox chkMonOff = (CheckBox) root.lookup("#chkMonOff");
			CheckBox chkTueOff = (CheckBox) root.lookup("#chkTueOff");
			CheckBox chkWedOff = (CheckBox) root.lookup("#chkWedOff");
			CheckBox chkThuOff = (CheckBox) root.lookup("#chkThuOff");
			CheckBox chkFriOff = (CheckBox) root.lookup("#chkFriOff");
			CheckBox chkSatOff = (CheckBox) root.lookup("#chkSatOff");
			CheckBox chkSunOff = (CheckBox) root.lookup("#chkSunOff");

			OpenDAO openDAO = new OpenDAO();

			ArrayList<OpenVO> ovo = null;

			// 콤보박스 세팅
			ObservableList<String> HoursRangeList;
			HoursRangeList = FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
					"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
					"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");

			cbMonOpen.setItems(HoursRangeList);
			cbMonClose.setItems(HoursRangeList);
			cbTueOpen.setItems(HoursRangeList);
			cbTueClose.setItems(HoursRangeList);
			cbWedOpen.setItems(HoursRangeList);
			cbWedClose.setItems(HoursRangeList);
			cbThuOpen.setItems(HoursRangeList);
			cbThuClose.setItems(HoursRangeList);
			cbFriOpen.setItems(HoursRangeList);
			cbFriClose.setItems(HoursRangeList);
			cbSatOpen.setItems(HoursRangeList);
			cbSatClose.setItems(HoursRangeList);
			cbSunOpen.setItems(HoursRangeList);
			cbSunClose.setItems(HoursRangeList);

			// 초기값 가져오기 (등록된 운영시간 정보가 있을 경우만)
			// 수정인지 등록인지 구분하기 위한 flag
			// 등록된 정보 있을 경우 수정이므로 flag = true, 없을 경우 등록이므로 flag = false;

			ovo = openDAO.getOpenHours(selectedRest.get(0).getRestaurantID());
			if(ovo != null) {
				flag = true;
				cbMonOpen.setValue(ovo.get(0).getMonOpen());
				cbMonClose.setValue(ovo.get(0).getMonClose());
				cbTueOpen.setValue(ovo.get(0).getTueOpen());
				cbTueClose.setValue(ovo.get(0).getTueClose());
				cbWedOpen.setValue(ovo.get(0).getWedOpen());
				cbWedClose.setValue(ovo.get(0).getWedClose());
				cbThuOpen.setValue(ovo.get(0).getThuOpen());
				cbThuClose.setValue(ovo.get(0).getThuClose());
				cbFriOpen.setValue(ovo.get(0).getFriOpen());
				cbFriClose.setValue(ovo.get(0).getFriClose());
				cbSatOpen.setValue(ovo.get(0).getSatOpen());
				cbSatClose.setValue(ovo.get(0).getSatClose());
				cbSunOpen.setValue(ovo.get(0).getSunOpen());
				cbSunClose.setValue(ovo.get(0).getSunClose());
			}

			// 오프 체크 시 설정
			chkMonOff.selectedProperty().addListener((e) -> {
				if (chkMonOff.isSelected()) {
					cbMonOpen.setValue("24:00");
					cbMonClose.setValue("24:00");
					cbMonOpen.setDisable(true);
					cbMonClose.setDisable(true);
				} else {
					cbMonOpen.setDisable(false);
					cbMonClose.setDisable(false);
				}
			});
			
			chkTueOff.selectedProperty().addListener((e) -> {
				if (chkTueOff.isSelected()) {
					cbTueOpen.setValue("24:00");
					cbTueClose.setValue("24:00");
					cbTueOpen.setDisable(true);
					cbTueClose.setDisable(true);
				} else {
					cbTueOpen.setDisable(false);
					cbTueClose.setDisable(false);
				}
			});
			
			chkWedOff.selectedProperty().addListener((e) -> {
				if (chkWedOff.isSelected()) {
					cbWedOpen.setValue("24:00");
					cbWedClose.setValue("24:00");
					cbWedOpen.setDisable(true);
					cbWedClose.setDisable(true);
				} else {
					cbWedOpen.setDisable(false);
					cbWedClose.setDisable(false);
				}
			});
			
			chkThuOff.selectedProperty().addListener((e) -> {
				if (chkThuOff.isSelected()) {
					cbThuOpen.setValue("24:00");
					cbThuClose.setValue("24:00");
					cbThuOpen.setDisable(true);
					cbThuClose.setDisable(true);
				} else {
					cbThuOpen.setDisable(false);
					cbThuClose.setDisable(false);
				}
			});
			
			chkFriOff.selectedProperty().addListener((e) -> {
				if (chkFriOff.isSelected()) {
					cbFriOpen.setValue("24:00");
					cbFriClose.setValue("24:00");
					cbFriOpen.setDisable(true);
					cbFriClose.setDisable(true);
				} else {
					cbFriOpen.setDisable(false);
					cbFriClose.setDisable(false);
				}
			});
			
			chkSatOff.selectedProperty().addListener((e) -> {
				if (chkSatOff.isSelected()) {
					cbSatOpen.setValue("24:00");
					cbSatClose.setValue("24:00");
					cbSatOpen.setDisable(true);
					cbSatClose.setDisable(true);
				} else {
					cbSatOpen.setDisable(false);
					cbSatClose.setDisable(false);
				}
			});
			
			chkSunOff.selectedProperty().addListener((e) -> {
				if (chkSunOff.isSelected()) {
					cbSunOpen.setValue("24:00");
					cbSunClose.setValue("24:00");
					cbSunOpen.setDisable(true);
					cbSunClose.setDisable(true);
				} else {
					cbSunOpen.setDisable(false);
					cbSunClose.setDisable(false);
				}
			});

			// OK버튼을 누르면 빈칸이 없는지 확인 후 수정
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// 빈칸이 없으면 입력값을 가져와서 DB에 등록
					try {
						if (cbMonOpen.getValue().equals("") || cbMonClose.getValue().equals("")
								|| cbTueOpen.getValue().equals("") || cbTueClose.getValue().equals("")
								|| cbWedOpen.getValue().equals("") || cbWedClose.getValue().equals("")
								|| cbThuOpen.getValue().equals("") || cbThuClose.getValue().equals("")
								|| cbFriOpen.getValue().equals("") || cbFriClose.getValue().equals("")
								|| cbSatOpen.getValue().equals("") || cbSatClose.getValue().equals("")
								|| cbSunOpen.getValue().equals("") || cbSunClose.getValue().equals("")) {
							SharedMethod.alertDisplay(5, "영업 시간 등록 실패", "영업 시간 등록 실패", "모든 요일의 정보를 입력해주세요");
						}
						// 입력값 가져오기
						OpenVO openVO = new OpenVO(cbMonOpen.getValue(), cbMonClose.getValue(), cbTueOpen.getValue(),
								cbTueClose.getValue(), cbWedOpen.getValue(), cbWedClose.getValue(),
								cbThuOpen.getValue(), cbThuClose.getValue(), cbFriOpen.getValue(),
								cbFriClose.getValue(), cbSatOpen.getValue(), cbSatClose.getValue(),
								cbSunOpen.getValue(), cbSunClose.getValue());
						
						int result = 0; //insert 결과값을 받기 위한 변수 선언
						if(flag) {
							openDAO.getOpenHoursUpdate(openVO, selectedRest.get(0).getRestaurantID());
						} else {
							result = openDAO.getOpenHoursRegiste(openVO, selectedRest.get(0).getRestaurantID());
							if( result > 0 ) {
								SharedMethod.alertDisplay(5, "영업 시간 등록 성공", "영업 시간 등록 성공", "영업 시간이 정상적으로 등록되었습니다!");
							}
						}
					} catch (Exception e) {
						SharedMethod.alertDisplay(5, "영업 시간 등록 실패", "영업 시간 등록 실패", "모든 요일의 정보를 입력해주세요");
					}
					
				}
			});
			
			btnCancel.setOnAction((e) -> {
				stage.close();
			});

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			SharedMethod.alertDisplay(5, "페이지 호출 실패", "영업 시간 등록 창 호출 실패", "영업 시간 등록 창 호출에 실패하였습니다.");
		}
	}

	// when click an object in menu table
	public void handlerMenuTableViewPressedAction() {
		// 테이블 뷰 객체 없는 부분 클릭 시 방어

		// 메뉴 필드 활성화
		menuFieldInitSetting(false, false, false);

		// 누른 위치와 해당 객체를 가져온다
		selectedIndex = menuTable.getSelectionModel().getSelectedIndex();
		selectedMenu = menuTable.getSelectionModel().getSelectedItems();
		selectedMenuId = selectedMenu.get(0).getMenuID();

		try {
			// 가져온 정보를 데이터 필드에 출력
			txtMenuName.setText(selectedMenu.get(0).getMenuName());
			txtMenuPrice.setText(String.valueOf(selectedMenu.get(0).getMenuPrice()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handlerBtnRestDeleteAction() {
		try {
			RestaurantDAO restDAO = new RestaurantDAO();
			restDAO.getRestDelete(selectedRest.get(0).getRestaurantID());
			restData.removeAll(restData); // 지우고
			totalList(); // 다시 전체 부르기
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "DELETE ERROR", "error", "error");
		}

		// 경고창 확인/취소 값 받아서 삭제할 지 말 지 이벤트 처리
	}

	public void handlerNewRestAction(ActionEvent e) {
		ObservableList<String> kindList = FXCollections.observableArrayList("한식", "중식", "일식", "양식", "채식", "세계음식", "뷔페",
				"카페");
		ObservableList<String> vegeList = FXCollections.observableArrayList("야채만", "동물 생산품", "채식 옵션");
		ObservableList<String> booleanList = FXCollections.observableArrayList("Y", "N");

		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/newRest.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnNewRest.getScene().getWindow());
			stage.setTitle("식당 추가하기");

			Button btnClose = (Button) barChartRoot.lookup("#btnClose");
			Button btnRestRegiste = (Button) barChartRoot.lookup("#btnRestRegiste");
			Button btnClear = (Button) barChartRoot.lookup("#btnClear");
			TextField txtNewName = (TextField) barChartRoot.lookup("#txtNewName");
			TextField txtNewAddr = (TextField) barChartRoot.lookup("#txtNewAddr");
			TextField txtNewPhone = (TextField) barChartRoot.lookup("#txtNewPhone");
			ComboBox<String> cbNewKind = (ComboBox<String>) barChartRoot.lookup("#cbNewKind");
			ComboBox<String> cbNewVege = (ComboBox<String>) barChartRoot.lookup("#cbNewVege");
			ComboBox<String> cbNewTakeout = (ComboBox<String>) barChartRoot.lookup("#cbNewTakeout");
			ComboBox<String> cbNewReserve = (ComboBox<String>) barChartRoot.lookup("#cbNewReserve");
			ComboBox<String> cbNewPark = (ComboBox<String>) barChartRoot.lookup("#cbNewPark");
			ComboBox<String> cbGu = (ComboBox<String>) barChartRoot.lookup("#cbGu");
			ComboBox<String> cbDong = (ComboBox<String>) barChartRoot.lookup("#cbDong");

			cbNewKind.setItems(kindList);
			cbNewVege.setItems(vegeList);
			cbNewVege.setDisable(true);
			cbNewTakeout.setItems(booleanList);
			cbNewReserve.setItems(booleanList);
			cbNewPark.setItems(booleanList);
			txtNewAddr.setText("123");

			cbNewKind.valueProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (cbNewKind.getValue().equals("채식")) {
						cbNewVege.setDisable(false);
					}
				}
			});

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

			btnRestRegiste.setOnAction((e1) -> {
				for(RestaurantVO rvo : restData) {
					// 이미 존재하는 식당명일 경우 등록 불가
					if(rvo.getRestaurantName().equals(txtNewName.getText())) {
						SharedMethod.alertDisplay(1, "REGISTERATION FAILED", "식당 등록 실패 !", "이미 등록된 상호명입니다. \n프랜차이즈의 경우 지점명을 명시해주세요 ^v^");
						return;
					}
				}
				if (txtNewName.getText().equals("") || txtNewAddr.getText().equals("")
						|| txtNewPhone.getText().equals("")
						|| cbNewKind.getSelectionModel().getSelectedItem().equals("")
						|| cbNewTakeout.getSelectionModel().getSelectedItem().equals("")
						|| cbNewPark.getSelectionModel().getSelectedItem().equals("")
						|| cbNewReserve.getSelectionModel().getSelectedItem().equals("")) {
					SharedMethod.alertDisplay(1, "REGISTERATION FAILED", "식당 등록 실패 !", "모든 항목을 입력해주세요(채식 종류 제외)");
				} else {
					try {
						String fileName = "default_restaurant.png";
						RestaurantDAO restaurantDAO = new RestaurantDAO();
						RestaurantVO rvo = new RestaurantVO(txtNewName.getText(),
								cbGu.getSelectionModel().getSelectedItem() + " "
										+ cbDong.getSelectionModel().getSelectedItem() + " " + txtNewAddr.getText(),
								txtNewPhone.getText(), cbNewKind.getSelectionModel().getSelectedItem(),
								cbNewVege.getSelectionModel().getSelectedItem(), fileName, 0, 0.0, null,
								cbNewTakeout.getSelectionModel().getSelectedItem(),
								cbNewPark.getSelectionModel().getSelectedItem(),
								cbNewReserve.getSelectionModel().getSelectedItem());

						int count = restaurantDAO.getRestregiste(rvo); // 이 순간 db에 레코드값 insert됨.
						if (count != 0) {
							restData.removeAll(restData);
							totalList();
							imageViewInit();
							SharedMethod.alertDisplay(5, "REGISTERATION SUCCESS", "식당 등록 성공 !", "식당 등록을 성공하였습니다.");
							stage.close();
						}
					} catch (Exception e4) {
						SharedMethod.alertDisplay(1, "REGISTERATION FAILED", "식당 등록 실패 !", "식당 등록을 실패하였습니다.");
					}
				}
			});

			btnClear.setOnAction((e2) -> {
				txtNewName.clear();
				txtNewAddr.clear();
				cbNewKind.setValue("");
				cbNewVege.setValue("");
				cbNewTakeout.setValue("");
				cbNewReserve.setValue("");
				cbNewPark.setValue("");
				imageViewInit();
			});

			btnClose.setOnAction((e3) -> {
				// 메뉴 테이블을 다시 세팅하고 창 닫기
				restData.removeAll(restData);
				totalList();
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void handlerNewMenuAction(ActionEvent e, int restId) {
		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/newMenu.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnNewRest.getScene().getWindow());
			stage.setTitle("메뉴 추가하기");

			Button btnClose = (Button) barChartRoot.lookup("#btnClose");
			Button btnMenuRegiste = (Button) barChartRoot.lookup("#btnMenuRegiste");
			Button btnClear = (Button) barChartRoot.lookup("#btnClear");
			TextField txtNewMenu = (TextField) barChartRoot.lookup("#txtNewMenu");
			TextField txtNewPrice = (TextField) barChartRoot.lookup("#txtNewPrice");

			// 숫자 11자리만 입력받음(정수만 입력받음)
			SharedMethod.inputDecimalFormatElevenDigit(txtNewPrice);
			SharedMethod.inputDecimalFormatElevenDigit(txtMenuPrice);
			

			// 메뉴 등록 실행 (insert, 테이블뷰 다시 세팅)
			btnMenuRegiste.setOnAction((e3) -> {
				try {
					MenuVO mvo = new MenuVO(txtNewMenu.getText(), Integer.parseInt(txtNewPrice.getText()));
					MenuDAO menuDAO = new MenuDAO();
					int count = menuDAO.getMenuregiste(mvo, restId);
					if (count != 0) {
						menuData.removeAll(menuData);
						// 해당 식당의 메뉴를 불러온다.
						try {
							// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
							int restID = selectedRest.get(0).getRestaurantID();
							menuData = FXCollections.observableArrayList();
							menuData = menuDAO.getMenu(restID);
							menuTable.setItems(menuData);
						} catch (Exception e4) {
							SharedMethod.alertDisplay(1, "메뉴 가져오기 실패", "메뉴 가져오기 실패", "메뉴를 가져오는 데에 실패했습니다.");
						}
						SharedMethod.alertDisplay(5, "REGISTERATION SUCCESS", "메뉴 등록 성공 !", "메뉴 등록을 성공하였습니다.");
						stage.close();
					}
				} catch (Exception e4) {
					SharedMethod.alertDisplay(1, "REGISTERATION FAILED", "메뉴 등록 실패!", "메뉴 등록에 실패하였습니다ㅠㅠ");
				}
				
				btnClose.setOnAction((e2) -> {
					stage.close();
				});
			});

			btnClear.setOnAction((event) -> {
				txtNewMenu.clear();
				txtNewPrice.clear();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void menuFieldInitSetting(boolean textField, boolean newButton, boolean EditDelete) {
		txtMenuName.setDisable(textField);
		txtMenuPrice.setDisable(textField);
		btnNewMenu.setDisable(newButton);
		btnMenuEdit.setDisable(EditDelete);
		btnMenuDelete.setDisable(EditDelete);
	}

}