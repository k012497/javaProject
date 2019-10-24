package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.RestaurantVO;

public class ManageChartController implements Initializable {

	@FXML
	private PieChart pieChart;
	@FXML
	private HBox hTopCount;
	@FXML
	private HBox hTopStars;
	@FXML
	private Label lblPercent;

	ArrayList<RestaurantVO> data;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hTopStars.setOnMousePressed((e) -> handlerStarsBarChartAction(e));
		hTopCount.setOnMousePressed((e) -> handlerCountBarChartAction(e));
		pieChartSetting();
	}

	// 파이차트 세팅
	public void pieChartSetting() {
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		
		pieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("양식", (double)restaurantDAO.getCountbyKind("양식")),
				new PieChart.Data("한식", (double)restaurantDAO.getCountbyKind("한식")),
				new PieChart.Data("일식", (double)restaurantDAO.getCountbyKind("일식")),
				new PieChart.Data("채식", (double)restaurantDAO.getCountbyKind("채식")),
				new PieChart.Data("세계음식", (double)restaurantDAO.getCountbyKind("세계음식")),
				new PieChart.Data("카페", (double)restaurantDAO.getCountbyKind("카페")),
				new PieChart.Data("뷔페", (double)restaurantDAO.getCountbyKind("뷔페")),
				new PieChart.Data("중식", (double)restaurantDAO.getCountbyKind("중식"))));

		// 파이 클릭 시 비율(%) 정보를 라벨에 출력
		for (final PieChart.Data data : pieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
				double total = 0;
				for (PieChart.Data d : pieChart.getData()) {
					total += d.getPieValue();
				}
				
				String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
				lblPercent.setText(data.getName()+" "+text);
			});
		}
	}

	// 별점이 높은 식당 순으로 상위 10개 식당을 바차트에 출력한 모달창 띄우기
	public void handlerStarsBarChartAction(MouseEvent e) {
		// 별점 순으로 정렬한 식당 리스트 10개를 가져온다.
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		data = restaurantDAO.getRest10();

		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(pieChart.getScene().getWindow());
			stage.setTitle("인기 식당 (별점)");

			BarChart barChart = (BarChart) barChartRoot.lookup("#barChart");
			Button btnClose = (Button) barChartRoot.lookup("#btnClose");

			// 모든 학생들의 국어점수 가져와서 막대그래프에 넣기
			XYChart.Series seriesStars = new XYChart.Series(); // chart label
			seriesStars.setName("별점");
			ObservableList starsList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				starsList.add(new XYChart.Data(data.get(i).getRestaurantName(), data.get(i).getAvgStars()));
			}

			seriesStars.setData(starsList);
			barChart.getData().add(seriesStars);

			XYChart.Series seriesFav = new XYChart.Series();
			seriesFav.setName("즐찾 수");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				mathList.add(new XYChart.Data(data.get(i).getRestaurantName(), data.get(i).getFavCount()));
			}

			seriesFav.setData(mathList);
			barChart.getData().add(seriesFav);

			btnClose.setOnAction((event) -> {
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// 구를 기준으로 각 지역에 등록된 식당 수를 바차트로 출력한 화면을 띄우기
	public void handlerCountBarChartAction(MouseEvent e) {
		RestaurantDAO restaurantDAO = new RestaurantDAO();

		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(pieChart.getScene().getWindow());
			stage.setTitle("지역 별 등록 식당 수");

			BarChart barChart = (BarChart) barChartRoot.lookup("#barChart");
			Button btnClose = (Button) barChartRoot.lookup("#btnClose");

			XYChart.Series seriesNumber = new XYChart.Series(); // chart label
			seriesNumber.setName("등록 식당 수");
			ObservableList guList = FXCollections.observableArrayList();
			guList.add(new XYChart.Data("강남구", restaurantDAO.getCountbyGu("강남구")));
			guList.add(new XYChart.Data("관악구", restaurantDAO.getCountbyGu("관악구")));
			guList.add(new XYChart.Data("광진구", restaurantDAO.getCountbyGu("광진구")));
			guList.add(new XYChart.Data("동대문구", restaurantDAO.getCountbyGu("동대문구")));
			guList.add(new XYChart.Data("마포구", restaurantDAO.getCountbyGu("마포구")));
			guList.add(new XYChart.Data("서대문구", restaurantDAO.getCountbyGu("서대문구")));
			guList.add(new XYChart.Data("성동구", restaurantDAO.getCountbyGu("성동구")));
			guList.add(new XYChart.Data("성북구", restaurantDAO.getCountbyGu("성북구")));
			guList.add(new XYChart.Data("용산구", restaurantDAO.getCountbyGu("용산구")));
			guList.add(new XYChart.Data("은평구", restaurantDAO.getCountbyGu("은평구")));
			guList.add(new XYChart.Data("종로구", restaurantDAO.getCountbyGu("종로구")));
			guList.add(new XYChart.Data("중구", restaurantDAO.getCountbyGu("중구")));
			guList.add(new XYChart.Data("중랑구", restaurantDAO.getCountbyGu("중랑구")));

			seriesNumber.setData(guList);
			barChart.getData().add(seriesNumber);

			btnClose.setOnAction((event) -> {
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
