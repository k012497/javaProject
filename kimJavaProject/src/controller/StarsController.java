package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class StarsController implements Initializable {
	@FXML
	Button btnOk;
	@FXML
	Button btnCancel;
	@FXML
	Label lblNum;
	@FXML
	Slider sldSize;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		sldSize.setOnMouseDragged((e) -> {
			String stars = sldSize.getValue() + "";
			lblNum.setText(stars.substring(0, 3));
		});

		btnCancel.setOnAction((e3) -> {
			Stage stage = (Stage) (btnCancel.getScene().getWindow());
			stage.close();
		});
		
//		btnOk.setOnAction((e)->{
//			MainController.handlerAddStarsAction();
//		});

	}

}
