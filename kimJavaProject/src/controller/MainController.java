package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import model.RestaurantVO;

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
	private ImageView imgOpen;

	@FXML
	private Label lblRecommend;
	@FXML
	private Label lblMember;

	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;
	@FXML
	private ComboBox<String> cbArrange;
	ObservableList<String> arrangeList = FXCollections.observableArrayList("기본 정렬", "별점순");

	// Inject controller
	@FXML
	private MyPageController myPageController;

	@FXML
	private ListView<CustomThing> listView;
	private final ObservableList<RestaurantVO> imageData = FXCollections.observableArrayList();
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;

	private int selectedIndex;
	private ObservableList<CustomThing> selectedRest;
	ObservableList<RestaurantVO> restData;
	ObservableList<MenuVO> menuData;

	RestaurantDAO restaurantDAO = new RestaurantDAO();
	ArrayList<RestaurantVO> rvo = null;
	String fileName = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 라벨, 버튼, 콤보박스 설정
		recommendlabelSetting();
		Platform.runLater(() -> { addressComboBoxSetting(); });
		buttonInitSetting(true, true);

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

		// 로그아웃 버튼 눌렀을 때
		btnSignOut.setOnAction((e) -> handlerSignOutAction());
	}

	public void setListWithImagebyKind(String kind) {
		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/test.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAll.getScene().getWindow());
			stage.setTitle("식당 리스트");

			restData = null;

			ObservableList<CustomThing> data = FXCollections.observableArrayList();
//	        data.addAll(new CustomThing("Cheese", "add", 1.23), new CustomThing("Horse", "add", 45.6), new CustomThing("Jam", "addr", 7.89));
			RestaurantDAO restDAO = new RestaurantDAO();

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
			String nameString = listView.getSelectionModel().getSelectedItems().get(0).getName();
			System.out.println(nameString);
			ArrayList<RestaurantVO> rvoList = null;
			try {
				rvoList = restaurantDAO.getRestByName(nameString);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String file = rvoList.get(0).getImageFileName();
			System.out.println(file);

			final ListView<CustomThing> listView = new ListView<CustomThing>(data);
			listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
				@Override
				public ListCell<CustomThing> call(ListView<CustomThing> listView) {
					return new CustomListCell(file);
				}
			});

			listView.setOnMousePressed((e) -> handlerListViewPressed(e, listView));

			StackPane root = new StackPane();
			root.getChildren().add(listView);
			stage.setScene(new Scene(root, 500, 500));
			stage.show();
		} catch (IOException e) {
			SharedMethod.alertDisplay(1, "리스트 창 호출 실패 ", "리스트 창 호출 실패", "리스트 창 호출 실패하였습니다. ");
		}

		StackPane root = new StackPane();
		root.getChildren().add(listView);
	}

	private void handlerSignOutAction() {
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
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/test.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnAll.getScene().getWindow());
			stage.setTitle("식당 리스트");

			restData = null;

			ObservableList<CustomThing> data = FXCollections.observableArrayList();
//	        data.addAll(new CustomThing("Cheese", "add", 1.23), new CustomThing("Horse", "add", 45.6), new CustomThing("Jam", "addr", 7.89));
			RestaurantDAO restDAO = new RestaurantDAO();
			try {
				data.addAll(restDAO.getRestByAddr(cbGu.getValue(), cbDong.getValue()));
			} catch (Exception e) {
				SharedMethod.alertDisplay(1, "식당 리스트를 가져오기 실패", "지역에 맞는 식당 리스트 가져오기 실패", "지역에 맞는 식당 리스트를 가져오지 못했습니다.");
				e.printStackTrace();
			}

			try {
				if (restDAO.getRestByAddr(cbGu.getValue(), cbDong.getValue()).get(0) == null) {
					SharedMethod.alertDisplay(5, "!", "등록된 식당 없음", "등록된 식당이 없습니다.");
				} else {
					data.addAll(restDAO.getRestByAddr(cbGu.getValue(), cbDong.getValue()));
				}

			} catch (Exception e) {
				SharedMethod.alertDisplay(1, "등록된 식당 없음", "등록된 식당이 없습니다.", "다른 지역/종류를 검색해주세요 ");
				return;
			}
			
			
			try {
				rvo = restaurantDAO.getRestByName(selectedRest.get(0).getName());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			fileName = rvo.get(0).getImageFileName();
			

			final ListView<CustomThing> listView = new ListView<CustomThing>(data);
			listView.getSelectionModel().getSelectedItems().get(0);
			listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
				@Override
				public ListCell<CustomThing> call(ListView<CustomThing> listView) {
					return new CustomListCell(fileName);
				}
			});

			listView.setOnMousePressed((e) -> handlerListViewPressed(e, listView));

			StackPane root = new StackPane();
			root.getChildren().add(listView);
			stage.setScene(new Scene(root, 500, 500));
			stage.show();
		} catch (IOException e) {
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
			SharedMethod.alertDisplay(1, "식당 정보 읽기 실패 ", "식당 정보 읽기 실패 ", "식당 정보 읽기 실패 ");
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
			} catch (Exception e1) {
				SharedMethod.alertDisplay(1, "식당 정보오류", "식당 정보오류", "식당 정보를 불러올 수 없습니다. ");
			}

			Button btnCancel = (Button) root.lookup("#btnCancel");
			Label lblName = (Label) root.lookup("#lblName");
			Label lblAddress = (Label) root.lookup("#lblAddress");
			Label lblPhoneNum = (Label) root.lookup("#lblPhoneNum");
			Label lblPark = (Label) root.lookup("#lblPark");
			Label lblTakeout = (Label) root.lookup("#lblTakeout");
			Label lblReserve = (Label) root.lookup("#lblReserve");
			Label lblStars = (Label) root.lookup("#lblStars");
			ImageView imageView = (ImageView) root.lookup("#imageView");
			ImageView imgLocation = (ImageView) root.lookup("#imgLocation");
			ImageView imgFav = (ImageView) root.lookup("#imgFav");
			ImageView imgStars = (ImageView) root.lookup("#imgStars");
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
						int restID = selectedRest.get(0).getRestaurantID();
						System.out.println(restID);
						MenuDAO menuDAO = new MenuDAO();
						menuData = FXCollections.observableArrayList();
						menuData = menuDAO.getMenu(restID);
						
						menuTable.setItems(menuData);
					} catch (Exception e) {
						SharedMethod.alertDisplay(1, "메뉴 세팅 실패", "메뉴 세팅 실패", "메뉴 세팅 실패");
					}
					
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
			imgStars.setOnMousePressed((e4) -> handlerAddStars(imgStars));
			imgFav.setOnMousePressed((e3) -> handlerAddFavorite());

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e3) {
			SharedMethod.alertDisplay(1, "상세정보 창 오류", "오류", "오류");
		}
	}

	private void handlerAddFavorite() {
		// 즐겨찾기 테이블에 insert
		FavoriteDAO favDAO = new FavoriteDAO();
		int restId = selectedRest.get(0).getRestaurantID();
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
			System.out.println("식 아이디="+restId);
			System.out.println("즐찾 수 = "+favoriteCount);
			restDAO.getFavCountUpdate(favoriteCount, restId);
			System.out.println("테스트");
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "즐겨찾기 추가 실패", "즐겨찾기 추가 실패", "즐겨찾기 추가 실패");
		}

	}

	public void handlerAddStars(ImageView imgStars) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/stars.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(imgStars.getScene().getWindow());
			stage.setTitle("아이디 찾기");

			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			Label lblNum = (Label) root.lookup("#lblNum");
			Slider sldSize = (Slider) root.lookup("#sldSize");

			btnCancel.setOnAction((e3) -> {
				stage.close();
			});

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			SharedMethod.alertDisplay(1, "별점 추가 오류", "별점 추가 오류", "별점 추가 창을 부르는 데 실패했습니다.");
		}
	}

	public void handlerLocationAction(ImageView imgLocation, String name) {
		// new stage with web view
		try {

			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(imgLocation.getScene().getWindow());
			stage.setTitle("아이디 찾기");

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

		try {
			// 누른 식당의 ID를 통해 해당 ID를 가진 메뉴를 불러온다.
			int restID = selectedRest.get(0).getRestaurantID();
			System.out.println(restID);
			MenuDAO menuDAO = new MenuDAO();
			menuData = FXCollections.observableArrayList();
			menuData = menuDAO.getMenu(restID);

			menuTable.setItems(menuData);
		} catch (Exception e) {
			SharedMethod.alertDisplay(1, "메뉴 세팅 실패", "메뉴 세팅 실패", "메뉴 세팅 실패");
		}

	} // end of menuTableViewSetting

}
