package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

public class ManageChartController implements Initializable{

	@FXML private PieChart pieChart;
	@FXML private BarChart barBest;
	@FXML private BarChart barNumber;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("양식", 10),new PieChart.Data("한식", 35),new PieChart.Data("일식", 55),new PieChart.Data("채식", 3),
				new PieChart.Data("세계음식", 35),new PieChart.Data("카페", 77),new PieChart.Data("뷔페", 14),new PieChart.Data("중식", 50)));
	}

}
