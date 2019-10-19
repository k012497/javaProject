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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.MenuVO;
import model.RestaurantVO;

public class ManageRestController implements Initializable {
	// restaurant management tab
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
	private RestaurantDAO restaurantDAO;

	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	private String fileName = null;
	
	private int no; // 삭제시 테이블에서 선택한 멤버의 번호 저장
	private File selectedFile = null;

	// 이미지 처리
	// 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("/Users/kimsojin/Desktop/code/images");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// column & default image setting
		restTableColSetting();
		menuTableColSetting();
		imageViewInit();
		imgView.maxWidth(143);
		imgView.maxHeight(134);

		txtRestDate.setDisable(true);
		txtRestFav.setDisable(true);
		txtRestStars.setDisable(true);

		totalList();

		// imageViewInit();
		btnImage.setOnAction((e) -> handlerBtnImageFileAction(e));

		// when select an object in tableView
		restTable.setOnMousePressed((e) -> handlerTableViewPressedAction());
		menuTable.setOnMousePressed((e)-> handlerMenuTableViewPressedAction());

		btnRestEdit.setOnAction((e) -> handlerBtnRestEditAction());
		btnRestDelete.setOnAction((e) -> handlerBtnRestDeleteAction());
		
		btnMenuEdit.setOnAction((e) -> handlerBtnMenuEditAction());
		btnMenuDelete.setOnAction((e) -> handlerBtnMenuDeleteAction());
	}

	private Object handlerBtnMenuDeleteAction() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object handlerBtnMenuEditAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public void handlerBtnRestEditAction() {
		try {
			File dirMake = new File(dirSave.getAbsolutePath());

			// 이미지 저장 폴더 생성
			if (!dirMake.exists()) {
				dirMake.mkdir();
			}

			// 이미지 파일 저장
			String fileName = imageSave(selectedFile);
			System.out.println(fileName);

			if (txtRestAddr.getText().equals("") || txtRestFoodtKind.getText().equals("")
					|| txtRestName.getText().equals("") || txtRestReserve.getText().equals("")
					|| txtRestTakeout.getText().equals("") || txtRestPark.getText().equals("")) {
				throw new Exception();
			} else {
				RestaurantVO rvo = new RestaurantVO(txtRestName.getText().trim(), txtRestAddr.getText().trim(), 
						txtRestPhone.getText().trim(), txtRestFoodtKind.getText().trim(), 
						txtRestVegeKind.getText().trim(), fileName, Integer.parseInt(txtRestFav.getText().trim()),
						Double.parseDouble(txtRestStars.getText().trim()), txtRestDate.getText().trim(), 
						txtRestTakeout.getText().trim(), txtRestPark.getText().trim(), txtRestReserve.getText().trim());

				// 변경사항을 DB로 보냄.
				RestaurantDAO restaurantDAO = new RestaurantDAO();
				RestaurantVO restaurantVO = restaurantDAO.getRestUpdate(rvo, selectedRest.get(0).getRestaurantID());

				restData.remove(selectedIndex);
				restData.add(selectedIndex, rvo); // 테이블에 들어가버림. setItems(data);해놨으니까.

				SharedMethod.alertDisplay(5, "SUCCESS", "congratulations!", "Registration successfully completed");
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
		colName.setMaxWidth(130);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("restaurantName"));

		TableColumn colAddr = new TableColumn("주소");
		colName.setMaxWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("address"));

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
		restTable.getColumns().addAll(colId, colName, colAddr, colPhone, colKind, colVege, colFav, colStars, colRegiste,
				colTake, colPark, colReserve);
	} // end of restaurantTableViewSetting

	public void menuTableColSetting() {
		menuData = FXCollections.observableArrayList();
		restTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colName = new TableColumn("메뉴");
		colName.setMaxWidth(200);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("menuName"));

		TableColumn colPrice = new TableColumn("가격");
		colPrice.setMaxWidth(100);
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
		imgView.setFitHeight(250);
		imgView.setFitWidth(230);

		if (selectedFile != null) {
			selectFileName = selectedFile.getName();
		}
		return fileChooser;
	}

	public void handlerTableViewPressedAction() {
		// 테이블 뷰 객체 없는 부분 클릭 시 방어
		try {
			// 누른 위치와 해당 객체를 가져온다
			selectedIndex = restTable.getSelectionModel().getSelectedIndex();
			selectedRest = restTable.getSelectionModel().getSelectedItems();

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
//				imgView.setFitHeight(250);
//				imgView.setFitWidth(230);

			}

		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

	}

	// when click an object in menu table
	public void handlerMenuTableViewPressedAction() {
		// 테이블 뷰 객체 없는 부분 클릭 시 방어
		try {
			// 누른 위치와 해당 객체를 가져온다
			selectedIndex = menuTable.getSelectionModel().getSelectedIndex();
			selectedMenu = menuTable.getSelectionModel().getSelectedItems();
			System.out.println(selectedIndex);
			System.out.println(selectedMenu);

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
}