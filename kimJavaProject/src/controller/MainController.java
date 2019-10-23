package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.FavoriteVO;
import model.MemberVO;
import model.MenuVO;
import model.OpenVO;
import model.RestaurantVO;
import model.ReviewVO;

public class MainController implements Initializable {
	@FXML
	private Button btnMyPage;
	@FXML
	private Button btnSignOut;
	@FXML
	private Button btnAll;
	@FXML
	private Button btnCafe;
	@FXML
	private Button btnBuffet;
	@FXML
	private Button btnVege;
	@FXML
	private Button btnGlobal;
	@FXML
	private Button btnWestern;
	@FXML
	private Button btnChinese;
	@FXML
	private Button btnJapanese;
	@FXML
	private Button btnKorean;
	@FXML
	private Button btnSearch;

	@FXML
	private HBox hBoxPopular;

	@FXML
	private ImageView imgOpen;

	@FXML
	private Label lblRecommend;
	@FXML
	private Label lblAge;
	@FXML
	private Label lblMember;
	private static String memberID;

	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;

	static int selectedRestId;
	static double starsUpdated;

	@FXML
	private ListView<CustomThing> listView;
//	private final ObservableList<RestaurantVO> imageData = FXCollections.observableArrayList();
	private String localUrl = "";
	private Image localImage;

//	private int selectedIndex;
	private ObservableList<CustomThing> selectedRest;
	ObservableList<RestaurantVO> restData;
	ObservableList<MenuVO> menuData;

	RestaurantDAO restaurantDAO = new RestaurantDAO();
	ArrayList<RestaurantVO> rvo;
	ArrayList<RestaurantVO> data;
	String fileName;
	private File selectedFile;

	// 영업중인 식당을 계산하기 위해 현재 시간을 가져올 변수 선언
	static Date now = new Date();
	static int nowHours = now.getHours();
	static int nowMinutes = now.getMinutes();
	static String nowTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 라벨, 버튼, 콤보박스 설정
		recommendlabelSetting();
		Platform.runLater(() -> {
			addressComboBoxSetting();
		});
		buttonInitSetting(true, true);

		// load가 끝난 후 라벨의 텍스트에 ID가 세팅되면 해당 ID의 정보 불러옴
		Platform.runLater(() -> {
			getMemberInfo();
		});

		// 검색 버튼 눌렀을 때
		btnSearch.setOnAction((e) -> handlerSearchAction(e));

		// 마이페이지 버튼 눌렀을 때
		btnMyPage.setOnAction((e) -> handlerMyPageAction(e));

		// 전체보기 눌렀을 때
		btnAll.setOnAction((e) -> setListWithImageAll());

		// 각 음식 종류 버튼 눌렀을 때
		btnKorean.setOnAction((e) -> setListWithImagebyKind("한식"));
		btnChinese.setOnAction((e) -> setListWithImagebyKind("중식"));
		btnJapanese.setOnAction((e) -> setListWithImagebyKind("일식"));
		btnWestern.setOnAction((e) -> setListWithImagebyKind("양식"));
		btnVege.setOnAction((e) -> setListWithImagebyKind("채식"));
		btnGlobal.setOnAction((e) -> setListWithImagebyKind("세계음식"));
		btnCafe.setOnAction((e) -> setListWithImagebyKind("카페"));
		btnBuffet.setOnAction((e) -> setListWithImagebyKind("뷔페"));

		// 연령대별 인기식당을 눌렀을 때
		hBoxPopular.setOnMousePressed((e) -> handlerFavBarChartAction(e));

		// 로그아웃 버튼 눌렀을 때
		btnSignOut.setOnAction((e) -> handlerSignOutAction());
	}

	// 접속중인 사용자의 정보를 불러오는 메소드
	public void getMemberInfo() {
		memberID = lblMember.getText();
		MemberDAO memberDAO = new MemberDAO();
		try {
			ArrayList<MemberVO> memberList = memberDAO.getMemberInfoUsingId(lblMember.getText());
			lblAge.setText(String.valueOf(memberList.get(0).getAgeGroup()));
		} catch (Exception e1) {
			SharedMethod.alertDisplay(5, "멤버 정보 호출 실패", "멤버 정보 호출 실패", "멤버 정보를 불러오지 못했습니다.");
		}
	}

	// 로그아웃 버튼을 눌렀을 때 현재 창을 닫고 로그인 창으로 돌아감
	public void handlerSignOutAction() {
		Stage primaryStage = new Stage();
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(loader);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			SharedMethod.alertDisplay(1, "로그인창 콜실패", "로그인창 부르기 실패", e.toString() + e.getMessage());
		}

		Stage stage = (Stage) btnSignOut.getScene().getWindow();
		stage.close();
	}

	// 음식 종류에 따른 리스트 뷰 모달창을 호출하는 메소드
	public void setListWithImagebyKind(String kind) {
		try {
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAll.getScene().getWindow());
			stage.setTitle("식당 리스트");

			restData = null;

			ObservableList<CustomThing> data = FXCollections.observableArrayList();
			RestaurantDAO restDAO = new RestaurantDAO();

			// 해당 지역 및 종류에 등록된 식당이 없는 경우
			try {
				if (restDAO.getRestByAddrAndKind(cbGu.getValue(), cbDong.getValue(), kind).get(0) == null) {
					SharedMethod.alertDisplay(5, "!", "등록된 식당 없음", "등록된 식당이 없습니다.");
				} else {
					data.addAll(restDAO.getRestByAddrAndKind(cbGu.getValue(), cbDong.getValue(), kind));
				}

			} catch (Exception e) {
				SharedMethod.alertDisplay(1, "등록된 식당 없음", "등록된 식당이 없습니다.", "다른 지역/종류를 검색해주세요 ");
				return;
			}

			final ListView<CustomThing> listView = new ListView<CustomThing>(data);
			listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
				@Override
				public ListCell<CustomThing> call(ListView<CustomThing> listView) {
					return new CustomListCell();
				}
			});

			listView.setOnMousePressed((e) -> handlerListViewPressed(e, listView));

			StackPane root = new StackPane();
			root.getChildren().addAll(listView);
			stage.setScene(new Scene(root, 500, 700));
			stage.show();
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "리스트 창 호출 실패 ", "리스트 창 호출 실패", "리스트 창 호출 실패하였습니다. ");
		}

		StackPane root = new StackPane();
		root.getChildren().add(listView);
	}

	/*
	 * 접속중인 사용자의 연령대에 맞는 인기식당을 BarChart로 보여주는 창을 띄우는 메소드 2019-10-23
	 * 
	 * 라벨을 클릭하면 이벤트를 전달 받아서 실행되는 메소드.
	 * 라벨에 적힌 사용자의 연령대 값을 파라미터로 select 메소드에 전달함 
	 * 해당 연령대가 즐겨찾기에 많이 등록한 식당을 내림차순으로 가져온다.
	 * 
	 * 만든이 : 김소진
	 */
	public void handlerFavBarChartAction(MouseEvent e) {
		// 별점 순으로 정렬한 식당 리스트 10개를 가져온다.
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		data = restaurantDAO.getTopFavCounByAge(Integer.parseInt(lblAge.getText()));

		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(lblAge.getScene().getWindow());
			stage.setTitle("연령대별 인기 식당");

			BarChart barChart = (BarChart) barChartRoot.lookup("#barChart");
			Button btnClose = (Button) barChartRoot.lookup("#btnClose");

			XYChart.Series seriesFav = new XYChart.Series();
			seriesFav.setName("즐찾 수");
			ObservableList countList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				countList.add(new XYChart.Data(data.get(i).getRestaurantName(), data.get(i).getFavCount()));
			}

			seriesFav.setData(countList);
			barChart.getData().add(seriesFav);

			btnClose.setOnAction((event) -> {
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			SharedMethod.alertDisplay(5, "창 호출 실패", "창 호출 실패", "창을 열지 못했습니다.");
		}
	}

	public void handlerButtonAllAction() {
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		restaurantDAO.getRestTotal();
	}

	public void handlerMyPageAction(ActionEvent e) {
		Parent mainView = null;
		Stage mainStage = null;

		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/myPage.fxml"));
			Scene scene = new Scene(mainView);
			mainStage = new Stage(StageStyle.UTILITY);
			mainStage.initModality(Modality.WINDOW_MODAL);
			mainStage.initOwner(btnMyPage.getScene().getWindow());
			mainStage.setTitle("My Page");

			Label lblMemberId = (Label) mainView.lookup("#lblMemberId");
			lblMemberId.setText(lblMember.getText());

			mainStage.setScene(scene);
			mainStage.setResizable(true);

			mainStage.show();
		} catch (Exception e1) {
			SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
		}
	}

	public void handlerSearchAction(ActionEvent e) {
		try {
			if (cbDong.getValue().equals("") || cbDong.getValue().equals("e) OO동")) {
				buttonInitSetting(true, true);
				throw new Exception();
			} else {
				buttonInitSetting(false, true);
			}
		} catch (Exception e2) {
			SharedMethod.alertDisplay(1, "검색 실패", "주소를 입력해주세요 !", "구와 동을 선택해주세요");
		}
	}

	private void buttonInitSetting(boolean a, boolean b) {
		btnAll.setDisable(a);
		btnBuffet.setDisable(a);
		btnCafe.setDisable(a);
		btnChinese.setDisable(a);
		btnGlobal.setDisable(a);
		btnJapanese.setDisable(a);
		btnKorean.setDisable(a);
		btnVege.setDisable(a);
		btnWestern.setDisable(a);

		btnSearch.setDisable(b);
	}

	public void addressComboBoxSetting() {
		AddressDAO addressDAO = new AddressDAO();
		addressGuList = addressDAO.getGu();
		cbGu.setItems(addressGuList);

		// lblMember의 주소를 불러오기
		MemberDAO memberDAO = new MemberDAO();
		ArrayList<MemberVO> memberList;
		try {
			memberList = memberDAO.getMemberInfoUsingId(lblMember.getText());
			String addr = memberList.get(0).getAddress();
			int idx = addr.indexOf(" ");
			String gu = addr.substring(0, idx);
			String dong = addr.substring(idx + 1);
			cbGu.setValue(gu);
			cbDong.setValue(dong);
			buttonInitSetting(true, false);

		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "사용자 정보 불러오기 실패", "사용자 정보 불러오기 실패", "사용자 정보 불러오기에 실패했습니다.");
		}

		cbGu.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				cbDong.setValue("e) OO동");
				addressDongList = addressDAO.getDong(cbGu.getValue());
				cbDong.setItems(addressDongList);
				cbDong.valueProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						buttonInitSetting(true, false);
					}
				});

			}
		});
	}

	public void recommendlabelSetting() {
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		int maxCount = 0;
		String gu = "";
		int count1 = restaurantDAO.getCountbyGu("강남구");
		int count2 = restaurantDAO.getCountbyGu("관악구");
		int count3 = restaurantDAO.getCountbyGu("광진구");
		int count4 = restaurantDAO.getCountbyGu("동대문구");
		int count5 = restaurantDAO.getCountbyGu("마포구");
		int count6 = restaurantDAO.getCountbyGu("서대문구");
		int count7 = restaurantDAO.getCountbyGu("성동구");
		int count8 = restaurantDAO.getCountbyGu("성북구");
		int count9 = restaurantDAO.getCountbyGu("용산구");
		int count10 = restaurantDAO.getCountbyGu("은평구");
		int count11 = restaurantDAO.getCountbyGu("종로구");
		int count12 = restaurantDAO.getCountbyGu("중구");
		int count13 = restaurantDAO.getCountbyGu("중랑구");

		for (int i = 0; i < 13; i++) {
			if (maxCount < count1) {
				maxCount = count1;
				gu = "강남구";
			} else if (maxCount < count2) {
				maxCount = count2;
				gu = "관악구";
			} else if (maxCount < count3) {
				maxCount = count3;
				gu = "광진구";
			} else if (maxCount < count4) {
				maxCount = count4;
				gu = "동대문구";
			} else if (maxCount < count5) {
				maxCount = count5;
				gu = "마포구";
			} else if (maxCount < count6) {
				maxCount = count6;
				gu = "서대문구";
			} else if (maxCount < count7) {
				maxCount = count7;
				gu = "성동구";
			} else if (maxCount < count8) {
				maxCount = count8;
				gu = "성북구";
			} else if (maxCount < count9) {
				maxCount = count9;
				gu = "용산구";
			} else if (maxCount < count10) {
				maxCount = count10;
				gu = "은평구";
			} else if (maxCount < count11) {
				maxCount = count11;
				gu = "종로구";
			} else if (maxCount < count12) {
				maxCount = count12;
				gu = "중구";
			} else if (maxCount < count13) {
				maxCount = count13;
				gu = "중랑구";
			}
		}
		lblRecommend.setText(gu);

	}

	public void setListWithImageAll() {
		try {
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAll.getScene().getWindow());
			stage.setTitle("식당 리스트");

			restData = null;

			ObservableList<CustomThing> data = FXCollections.observableArrayList();
			RestaurantDAO restDAO = new RestaurantDAO();

			// 해당 지역에 원하는 조건의 식당이 없을 때
			try {
				if (restDAO.getRestByAddr(cbGu.getValue(), cbDong.getValue()).get(0) == null) {
					SharedMethod.alertDisplay(5, "!", "등록된 식당 없음", "등록된 식당이 없습니다.");
				} else {
					try {
						data.addAll(restDAO.getRestByAddr(cbGu.getValue(), cbDong.getValue()));
					} catch (Exception e) {
						SharedMethod.alertDisplay(1, "식당 리스트를 가져오기 실패", "지역에 맞는 식당 리스트 가져오기 실패",
								"지역에 맞는 식당 리스트를 가져오지 못했습니다.");
					}
				}

			} catch (Exception e) {
				SharedMethod.alertDisplay(1, "등록된 식당 없음", "등록된 식당이 없습니다.", "다른 지역/종류를 검색해주세요 ");
				return;
			}

			////////////////////////////////////////////////////////////////////////

			final ListView<CustomThing> listView = new ListView<CustomThing>(data);
			listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
				@Override
				public ListCell<CustomThing> call(ListView<CustomThing> listView) {
					return new CustomListCell();
				}
			});
			////////////////////////////////////////////////////////////////////////

			listView.setOnMousePressed((e) -> handlerListViewPressed(e, listView));

			StackPane root = new StackPane();
			root.getChildren().add(listView);
			stage.setScene(new Scene(root, 500, 500));
			stage.show();
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "리스트 창 오류 ", "리스트 창 호출 실패 ..", "리스트 창을 불러오지 못했습니다");
		}

		StackPane root = new StackPane();
		root.getChildren().add(listView);
	}

	public void handlerListViewPressed(MouseEvent e, ListView<CustomThing> listView) {
		selectedRest = listView.getSelectionModel().getSelectedItems();
		try {
			int restId = selectedRest.get(0).getRestaurantID();
		} catch (Exception e2) {
			SharedMethod.alertDisplay(1, "식당 정보 읽기 실패 ", "식당 정보 읽기 실패", "등록된 식당을 클릭해주세요");
		}

		RestaurantDAO restDAO = new RestaurantDAO();
		ArrayList<RestaurantVO> rvo = null;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/shopInfo.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAll.getScene().getWindow());
			stage.setTitle("상세 정보");

			try {
				rvo = restDAO.getRestByName(selectedRest.get(0).getName());
				selectedRestId = selectedRest.get(0).getRestaurantID();
			} catch (Exception e1) {
				SharedMethod.alertDisplay(1, "식당 정보오류", "식당 정보오류", "식당 정보를 불러올 수 없습니다. ");
				return;
			}

			Button btnCancel = (Button) root.lookup("#btnCancel");
			HBox businessHours = (HBox) root.lookup("#businessHours");
			Label lblName = (Label) root.lookup("#lblName");
			Label lblAddress = (Label) root.lookup("#lblAddress");
			Label lblPhoneNum = (Label) root.lookup("#lblPhoneNum");
			Label lblPark = (Label) root.lookup("#lblPark");
			Label lblTakeout = (Label) root.lookup("#lblTakeout");
			Label lblReserve = (Label) root.lookup("#lblReserve");
			Label lblStars = (Label) root.lookup("#lblStars");
			Label lblOpenHours = (Label) root.lookup("#lblOpenHours");
			ImageView imageView = (ImageView) root.lookup("#imgView");
			ImageView imgLocation = (ImageView) root.lookup("#imgLocation");
			ImageView imgFav = (ImageView) root.lookup("#imgFav");
			ImageView imgStars = (ImageView) root.lookup("#imgStars");
			ImageView refresh = (ImageView) root.lookup("#refresh");
			TableView<MenuVO> menuTable = (TableView<MenuVO>) root.lookup("#menuTable");

			menuTable.setEditable(false); // 테이블 뷰 편집 못 하게 설정

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					menuData = FXCollections.observableArrayList();

					TableColumn colName = new TableColumn("메뉴");
					colName.setMaxWidth(300);
					colName.setStyle("-fx-alignment:CENTER;");
					colName.setCellValueFactory(new PropertyValueFactory("menuName"));

					TableColumn colPrice = new TableColumn("가격");
					colPrice.setMaxWidth(200);
					colPrice.setStyle("-fx-alignment:CENTER;");
					colPrice.setCellValueFactory(new PropertyValueFactory("menuPrice"));

					// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
					menuTable.setItems(menuData);
					menuTable.getColumns().addAll(colName, colPrice);

					try {
						// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
						// int restID = selectedRest.get(0).getRestaurantID();
						System.out.println("selectedRestId  " + selectedRestId);
						MenuDAO menuDAO = new MenuDAO();
						menuData = FXCollections.observableArrayList();
						menuData = menuDAO.getMenu(selectedRestId);

						menuTable.setItems(menuData);
					} catch (Exception e) {
						SharedMethod.alertDisplay(1, "메뉴 세팅 실패", "메뉴 세팅 실패", "메뉴 세팅 실패");
					}

					// 이미지뷰 세팅
					try {
						String fileName = selectedRest.get(0).getFileName();
						selectedFile = new File("/Users/kimsojin/Desktop/code/images/" + fileName);
						System.out.println("selectedFile = " + selectedFile);
						if (selectedFile != null) {
							// 이미지 파일 경로
							localUrl = selectedFile.toURI().toURL().toString();
							localImage = new Image(localUrl, false);
							System.out.println(localImage.toString());
							imageView.setImage(localImage);
							imageView.setFitHeight(250);
							imageView.setFitWidth(230);
						}
					} catch (MalformedURLException e) {
						SharedMethod.alertDisplay(1, "이미지뷰 세팅 실패", "이미지뷰 세팅 실패", "이미지뷰 세팅 실패");
					}

					// 영업중인지 검사
					int resultNum = checkTime();
					if (resultNum == 1) {
						lblOpenHours.setText("영업중!");
						businessHours.setDisable(false);
					} else if (resultNum == -1) {
						lblOpenHours.setText("영업 종료");
						businessHours.setDisable(true);
					} else {
						businessHours.setDisable(true);
					}

					businessHours.setOnMousePressed((e) -> {
						try {
							Parent root = FXMLLoader.load(getClass().getResource("/view/openHours_shopInfo.fxml"));
							Stage stage = new Stage(StageStyle.UTILITY);
							stage.initModality(Modality.WINDOW_MODAL);
							stage.initOwner(businessHours.getScene().getWindow());
							stage.setTitle("아이디 찾기");

							Button btnOk = (Button) root.lookup("#btnOk");
							Label cbMonOpen = (Label) root.lookup("#lblMonOpen");
							Label cbTueOpen = (Label) root.lookup("#lblTueOpen");
							Label cbWedOpen = (Label) root.lookup("#lblWedOpen");
							Label cbThuOpen = (Label) root.lookup("#lblThuOpen");
							Label cbFriOpen = (Label) root.lookup("#lblFriOpen");
							Label cbSatOpen = (Label) root.lookup("#lblSatOpen");
							Label cbSunOpen = (Label) root.lookup("#lblSunOpen");

							Label cbMonClose = (Label) root.lookup("#lblMonClose");
							Label cbTueClose = (Label) root.lookup("#lblTueClose");
							Label cbWedClose = (Label) root.lookup("#lblWedClose");
							Label cbThuClose = (Label) root.lookup("#lblThuClose");
							Label cbFriClose = (Label) root.lookup("#lblFriClose");
							Label cbSatClose = (Label) root.lookup("#lblSatClose");
							Label cbSunClose = (Label) root.lookup("#lblSunClose");

							CheckBox chkMonOff = (CheckBox) root.lookup("#chkMonOff");
							CheckBox chkTueOff = (CheckBox) root.lookup("#chkTueOff");
							CheckBox chkWedOff = (CheckBox) root.lookup("#chkWedOff");
							CheckBox chkThuOff = (CheckBox) root.lookup("#chkThuOff");
							CheckBox chkFriOff = (CheckBox) root.lookup("#chkFriOff");
							CheckBox chkSatOff = (CheckBox) root.lookup("#chkSatOff");
							CheckBox chkSunOff = (CheckBox) root.lookup("#chkSunOff");

							btnOk.setOnAction((e3) -> {
								stage.close();
							});
							OpenDAO openDAO = new OpenDAO();
							ArrayList<OpenVO> ovo = null;
							ovo = openDAO.getOpenHours(selectedRestId);
							cbMonOpen.setText(ovo.get(0).getMonOpen());
							cbMonClose.setText(ovo.get(0).getMonClose());
							cbTueOpen.setText(ovo.get(0).getMonOpen());
							cbTueClose.setText(ovo.get(0).getTueClose());
							cbWedOpen.setText(ovo.get(0).getMonOpen());
							cbWedClose.setText(ovo.get(0).getWedClose());
							cbThuOpen.setText(ovo.get(0).getMonOpen());
							cbThuClose.setText(ovo.get(0).getThuClose());
							cbFriOpen.setText(ovo.get(0).getMonOpen());
							cbFriClose.setText(ovo.get(0).getFriClose());
							cbSatOpen.setText(ovo.get(0).getMonOpen());
							cbSatClose.setText(ovo.get(0).getSatClose());
							cbSunOpen.setText(ovo.get(0).getMonOpen());
							cbSunClose.setText(ovo.get(0).getSunClose());

							chkMonOff.setDisable(true);
							chkTueOff.setDisable(true);
							chkWedOff.setDisable(true);
							chkThuOff.setDisable(true);
							chkFriOff.setDisable(true);
							chkSatOff.setDisable(true);
							chkSunOff.setDisable(true);

							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					});

				}
			});

			lblName.setText(rvo.get(0).getRestaurantName());
			lblAddress.setText(rvo.get(0).getAddress());
			lblPhoneNum.setText(rvo.get(0).getTelephone());
			lblPark.setText(rvo.get(0).getParking());
			lblTakeout.setText(rvo.get(0).getTakeout());
			lblReserve.setText(rvo.get(0).getReservation());
			lblStars.setText(String.valueOf(rvo.get(0).getAvgStars()));

			btnCancel.setOnAction((e1) -> {
				stage.close();
			});

			imgLocation.setOnMousePressed((e2) -> handlerLocationAction(imgLocation, lblName.getText()));
			imgStars.setOnMousePressed((e4) -> {
				handlerAddStars(imgStars);
			});

			refresh.setOnMousePressed(
					(e3) -> lblStars.setText(String.valueOf(restaurantDAO.getAvgStarsbyId(selectedRestId))));
			imgFav.setOnMousePressed((e4) -> handlerAddFavorite());

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e3) {
			SharedMethod.alertDisplay(1, "상세정보 창 오류", "오류", "오류");
		}
	}

	public static double refreshAvgStars() {
		RestaurantDAO restDAO = new RestaurantDAO();
		double result = restDAO.getAvgStarsbyId(selectedRestId);
		return result;
	}

	public void handlerAddFavorite() {
		// 즐겨찾기 테이블에 insert
		FavoriteDAO favDAO = new FavoriteDAO();
		int restId = selectedRest.get(0).getRestaurantID();

		int isEmpty = favDAO.getFavFlag(restId, lblMember.getText());

		if (isEmpty == 0) {
			FavoriteVO fvo = new FavoriteVO(lblMember.getText(), restId);

			try {
				// 즐겨찾기 테이블에 등록
				favDAO.getFavregiste(fvo, lblMember.getText());

				// 즐겨찾기 테이블에 있는 식당아이디를 카운트해서
				System.out.println(favDAO.getFavCount(restId));
				System.out.println("testest");

				// 즐겨찾기 속성에 추가
				RestaurantDAO restDAO = new RestaurantDAO();
				int favoriteCount = favDAO.getFavCount(restId);
				System.out.println("식 아이디=" + restId);
				System.out.println("즐찾 수 = " + favoriteCount);
				restDAO.getFavCountUpdate(favoriteCount, restId);
				System.out.println("테스트");
			} catch (Exception e) {
				SharedMethod.alertDisplay(1, "즐겨찾기 추가 실패", "즐겨찾기 추가 실패", "즐겨찾기 추가 실패");
			}
		} else {
			SharedMethod.alertDisplay(1, "즐겨찾기 추가 실패", "이미 즐겨찾기에 등록되었습니다", "이미 즐겨찾기에 등록되었습니다");
		}

	}

	public void handlerAddStars(ImageView imgStars) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/stars.fxml"));
			Stage stage = new Stage();
//			Stage stage = new Stage(StageStyle.UTILITY);
//			stage.initModality(Modality.WINDOW_MODAL);
//			stage.initOwner(imgStars.getScene().getWindow());
//			stage.setTitle("아이디 찾기");
//
//			Button btnOk = (Button) root.lookup("#btnOk");
//			Button btnCancel = (Button) root.lookup("#btnCancel");
//			Label lblNum = (Label) root.lookup("#lblNum");
//			Slider sldSize = (Slider) root.lookup("#sldSize");
//
//			btnCancel.setOnAction((e3) -> {
//				stage.close();
//			});

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			SharedMethod.alertDisplay(1, "별점 추가 오류", "별점 추가 오류", "별점 추가 창을 부르는 데 실패했습니다.");
			e.printStackTrace();
		}
	}

	/*
	 * 해당 식당의 지도를 새 창의 WebView로 띄워주는 메소드 Google map API URL을 이용하여 query에 상호명을 전달 만든이
	 * 만든이 : 김소진
	 */
	public void handlerLocationAction(ImageView imgLocation, String name) {
		try {

			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(imgLocation.getScene().getWindow());
			stage.setTitle("지도");

			WebView webView = new WebView();

			webView.getEngine().load("https://www.google.com/maps/search/?api=1&parameters&query=" + name);

			VBox vBox = new VBox(webView);
			Scene scene = new Scene(vBox, 960, 600);

			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void menuTableColSetting(TableView<MenuVO> menuTable) {
		menuData = FXCollections.observableArrayList();

		TableColumn colName = new TableColumn("메뉴");
		colName.setMaxWidth(300);
		colName.setStyle("-fx-alignment:CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("menuName"));

		TableColumn colPrice = new TableColumn("가격");
		colPrice.setMaxWidth(200);
		colPrice.setStyle("-fx-alignment:CENTER;");
		colPrice.setCellValueFactory(new PropertyValueFactory("menuPrice"));

		// 컬럼 객체들을 테이블 뷰에 추가 & 항목 추가
		menuTable.setItems(menuData);
		menuTable.getColumns().addAll(colName, colPrice);

//		try {
//			// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
//			int restID = selectedRest.get(0).getRestaurantID();
//			System.out.println(restID);
//			MenuDAO menuDAO = new MenuDAO();
//			menuData = FXCollections.observableArrayList();
//			menuData = menuDAO.getMenu(restID);
//
//			menuTable.setItems(menuData);
//		} catch (Exception e) {
//			SharedMethod.alertDisplay(1, "메뉴 세팅 실패", "메뉴 세팅 실패", "메뉴 세팅 실패");
//		}

	} // end of menuTableViewSetting

	public static int handlerAddStarsAction(String stars) {

		// 1. 리뷰 테이블에 insert
		ReviewVO rvo = new ReviewVO(memberID, selectedRestId, Double.parseDouble(stars));
		ReviewDAO reviewDAO = new ReviewDAO();
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		int result = 0;
		try {
			result = reviewDAO.getReviewRegiste(rvo);
			if (result == 0) {
				SharedMethod.alertDisplay(5, "리뷰 등록 실패 ", "리뷰 등록 실패 ㅠㅠ", "리뷰 등록 실패하였습니다 ");
				return 0;
			} else {
				// 2. 식당 테이블의 별점 정보 수정
				restaurantDAO.getRestStarsUpdate(selectedRestId);

				return 1;
			}
		} catch (Exception e) {
			SharedMethod.alertDisplay(5, "리뷰 등록 실패 ", "리뷰 등록 실패 ㅠㅠ", "리뷰 등록 실패하였습니다 ");
		}

		return 0;
	}

	// 해당 식당의 영업시간(OpenVO)를 받아와서 오늘의 요일에 따른 open/close시간을 checkOpenHour에 전달
	public int checkTime() {
		OpenDAO openDAO = new OpenDAO();
		ArrayList<OpenVO> ovo = openDAO.getOpenHours(selectedRestId);

		// 해당 식당의 운영시간 정보가 등록되지 않았을 경우
		if (ovo == null)
			return 0;

		if (nowHours < 10 && nowMinutes >= 10) {
			nowTime = "0" + nowHours + ":" + nowMinutes;
		} else if (nowHours >= 10 && nowMinutes < 10) {
			nowTime = nowHours + ":0" + nowMinutes;
		} else if (nowMinutes < 10 && nowHours < 10) {
			nowTime = "0" + nowHours + ":0" + nowMinutes;
		} else {
			nowTime = nowHours + ":" + nowMinutes;
		}
		System.out.println(nowTime);

		// get day of week
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		System.out.println("ovo= " + ovo.toString());
		String monOpen = ovo.get(0).getMonOpen();
		String monClose = ovo.get(0).getMonClose();
		String tueOpen = ovo.get(0).getTueOpen();
		String tueClose = ovo.get(0).getTueClose();
		String wedOpen = ovo.get(0).getWedOpen();
		String wedClose = ovo.get(0).getWedClose();
		String thuOpen = ovo.get(0).getThuOpen();
		String thuClose = ovo.get(0).getThuClose();
		String friOpen = ovo.get(0).getFriOpen();
		String friClose = ovo.get(0).getFriClose();
		String satOpen = ovo.get(0).getSatOpen();
		String satClose = ovo.get(0).getSatClose();
		String sunOpen = ovo.get(0).getSunOpen();
		String sunClose = ovo.get(0).getSunClose();

		int result = 0;
		// switch case by dayOfWeek
		switch (dayOfWeek) {
		case 1:
			result = checkOpenHour(sunOpen, sunClose);
			break;
		case 2:
			result = checkOpenHour(monOpen, monClose);
			break;
		case 3:
			result = checkOpenHour(tueOpen, tueClose);
			break;
		case 4:
			result = checkOpenHour(wedOpen, wedClose);
			break;
		case 5:
			result = checkOpenHour(thuOpen, thuClose);
			break;
		case 6:
			result = checkOpenHour(friOpen, friClose);
			break;
		case 7:
			result = checkOpenHour(satOpen, satClose);
			break;

		default:
			break;
		}

		return result;
	}

	/*
	 * 식당이 현재 영업중인지 계산하기 위한 메소드 2019-10-23
	 * 
	 * compareTo()메소드를 이용하여 현재시간과 오픈시간, 마감시간을 비교함 (String ) time1.compareTo(time2)
	 * => time1이 time2보다 이후 날짜이면 양수, 반대의 경우 음수, 같으면 0을 반환
	 * 
	 * 영업중인 경우 1을, 그렇지 않은 경우 -1을 반환. 만든이 : 김소진
	 */
	public static int checkOpenHour(String open, String close) {
		try {
			int result1 = nowTime.compareTo(open);
			int result2 = close.compareTo(nowTime);

			// 오픈시간 < 현재시간 < 마감시간이어야 영업중인 상태이므로
			if (result1 >= 0 && result2 >= 0) {
				return 1;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}