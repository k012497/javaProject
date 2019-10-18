package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AdminController{

    @FXML private TabPane tabPane;

    @FXML private Tab memberPage;
    @FXML private Tab restaurantPage;
    @FXML private Tab chartPage;
    
    @FXML private Button btnSignOut;

    public void init() {
//    	Stage stage  = (Stage)(btnSignOut.getScene().getWindow());
//    	btnSignOut.setOnAction((e)-> stage.close());
    	btnSignOut.setOnAction((e)-> Platform.exit());
    }


}