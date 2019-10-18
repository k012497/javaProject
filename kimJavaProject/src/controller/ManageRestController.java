package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private TextField txtResFoodtKind;
	@FXML
	private TextField txtRestName;
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

	ObservableList<RestaurantVO> data;

	// 테이블 뷰를 선택했을 때 위치값과 객체값을 저장할 수있는 변수를 선언
	private int selectedIndex;
	private ObservableList<RestaurantVO> selectedRest;
	private ObservableList<MenuVO> selectedMenu;
	private RestaurantDAO restaurantDAO;

	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;

	private int no; // 삭제시 테이블에서 선택한 학생의 번호 저장
	private File selectedFile = null;

	// 이미지 처리
	// 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("/Users/kimsojin/Desktop/code/images");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		restTableColSetting();
		totalList();
		//imageViewInit();
		btnImage.setOnAction((e)-> handlerBtnImageFileAction(e));
		// when select an object in tableView
		restTable.setOnMousePressed((e) -> handlerTableViewPressedAction());

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
			data.add(restVO);
		}
	}
	
	public void restTableColSetting() {
		data = FXCollections.observableArrayList();
		restTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

		TableColumn colId = new TableColumn("식당번호");
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
		restTable.setItems(data);
		restTable.getColumns().addAll(colId, colName, colAddr, colPhone, colKind, colVege, colFav, colStars, colRegiste, colTake, colPark, colReserve);
	} // end of restaurantTableViewSetting
	
	public void menuTableColSetting() {
		data = FXCollections.observableArrayList();
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
		restTable.setItems(data);
		restTable.getColumns().addAll(colName, colName);

		
	} // end of menuTableViewSetting

	public void imageViewInit() {
		localUrl = "/images/default-restaurant.png";
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
			txtResFoodtKind.setText(selectedRest.get(0).getKind());
			txtRestVegeKind.setText(selectedRest.get(0).getVeganLevel());
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
				imgView.setFitHeight(250);
				imgView.setFitWidth(230);
			}

			// 수정/삭제 버튼 활성화
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 누른 위치와 해당 객체를 가져온다
			int restID = selectedRest.get(0).getRestaurantID();
			
//			selectedIndex = restTable.getSelectionModel().getSelectedIndex();
//			selectedRest = restTable.getSelectionModel().getSelectedItems();
			

			// 가져온 정보를 데이터 필드에 출력
//			txtMenuName.setText(selectedMenu.get(0).getRestaurantName());
//			txtMenuPrice.setText(selectedRest.get(0).getAddress());

		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}