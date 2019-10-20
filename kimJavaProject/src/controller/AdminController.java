package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class AdminController{

    @FXML private TabPane tabPane;

    @FXML private Tab memberPage;
    @FXML private Tab restaurantPage;
    @FXML private Tab chartPage;
    
    @FXML private Button btnSignOut;

    public void init() {
    	btnSignOut.setOnAction((e)-> handlerButtonSignOut());
    }

	public void handlerButtonSignOut() {
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

}