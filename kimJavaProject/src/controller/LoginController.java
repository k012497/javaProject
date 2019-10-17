package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController implements Initializable {
	@FXML private TextField txtPw;
	@FXML private TextField txtId;

	@FXML private Button btnSignIn;
	@FXML private Label lblSignUp;
	@FXML private Label lblFindId;
	@FXML private Label lblFindPw;
	@FXML private ImageView imgAdmin;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
