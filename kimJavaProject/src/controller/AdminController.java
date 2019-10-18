package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AdminController {

    @FXML private TabPane tabPane;

    @FXML private Tab memberPage;
    @FXML private Tab restaurantPage;
    @FXML private Tab chartPage;
    
//    // Inject controller
//    @FXML private BarTabController barTabPageController;

    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {

        });
    }
}