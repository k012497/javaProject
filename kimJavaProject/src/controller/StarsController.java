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
	@FXML
	Label lblRestId;
	
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
		
		/*
		 * Ok버튼을 누르면 해당 별점을 등록하는 메소드 - 2019-10-22
		 * 접속중인 사용자의 ID를 MainController의 handlerAddStarsAction() 메소드에 전달하여
		 * reviewTBL에 insert, restaurantTBL의 avgStars를 update
		 * 
		 * 만든이 : 김소진
		 */
		btnOk.setOnAction((e)->{
			int result = MainController.handlerAddStarsAction(lblNum.getText());
			if(result == 1) {
				Stage stage = (Stage) (btnCancel.getScene().getWindow());
				stage.close();
			}else {
				SharedMethod.alertDisplay(5, "별점 등록 실패", "별점 등록 실패", "별점 등록에 실패하였습니다.");
			}
		});

	}

}
