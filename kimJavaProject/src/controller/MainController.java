package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class MainController implements Initializable {
	@FXML
	private Button btnMyPage;
	@FXML
	private Button btnSingOut;
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
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;
	@FXML
	private ComboBox<String> cbArrange;
	ObservableList<String> arrangeList = FXCollections.observableArrayList("기본 정렬", "별점순");

	@FXML
	private ListView<RestImageList> listView;
	private final ObservableList<RestImageList> imageData = FXCollections.observableArrayList();
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		RecommendlabelSetting();
		AddressComboBoxSetting();
		buttonInitSetting(true, true);
		
		btnSearch.setOnAction((e) -> handlerSearchAction(e));
		btnMyPage.setOnAction((e) -> handlerMyPageAction(e));
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
				
				mainStage.setScene(scene);
				mainStage.setResizable(true);

				mainStage.show();
			} catch (Exception e1) {
				SharedMethod.alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e1.toString() + e1.getMessage());
			}
	}

	public void handlerSearchAction(ActionEvent e) {
		try {
			if (cbDong.getValue().equals("")) {
				buttonInitSetting(true, true);
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

	public void AddressComboBoxSetting() {
		AddressDAO addressDAO = new AddressDAO();
		addressGuList = addressDAO.getGu();
		cbGu.setItems(addressGuList);

		cbGu.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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

	public void RecommendlabelSetting() {
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

	public void setListWithImage() {
		ObservableList<RestImageList> data = FXCollections.observableArrayList();
		data.addAll(new RestImageList("Cheese", "address", 1.1), new RestImageList("Horse", "addr 2", 3.3),
				new RestImageList("Jam", "111", 4.4));

		listView = new ListView<RestImageList>(data);
		listView.setCellFactory(new Callback<ListView<RestImageList>, ListCell<RestImageList>>() {
			@Override
			public ListCell<RestImageList> call(ListView<RestImageList> listView) {
				return new CustomListCell();
			}
		});
		StackPane root = new StackPane();
		root.getChildren().add(listView);
	}

	private class CustomListCell extends ListCell<RestImageList> {
		private HBox content;
		private Text name;
		private Text price;

		public CustomListCell() {
			super();
			name = new Text();
			price = new Text();
			VBox vBox = new VBox(name, price);

			FileInputStream input;
			try {
				File dirSave = new File("/Users/kimsojin/Desktop/n");
				input = new FileInputStream(new File(dirSave.getAbsolutePath() + "//" + "moodindigo.jpg"));
				Image image = new Image(input);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(200);
				imageView.setFitHeight(200);
				content = new HBox(imageView, vBox);
				content.setSpacing(10);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void updateItem(RestImageList item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null && !empty) { // <== test for null item and empty parameter
				name.setText(item.getName());
				setGraphic(content);
			} else {
				setGraphic(null);
			}
		}
	}
}
