package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MyPageController implements Initializable{
	@FXML
	private ComboBox<String> cbAge;
	ObservableList<String> cbAgeList;
	@FXML
	private ComboBox<String> cbGu;
	ObservableList<String> addressGuList;
	@FXML
	private ComboBox<String> cbDong;
	ObservableList<String> addressDongList;
	
	@FXML private Button btnEdit;
	@FXML private Button btnLeave;
	@FXML private Button btnCancel;
	@FXML private TextField txtName;
	@FXML private TextField txtNumber;
	@FXML private TextField txtPw;
	@FXML private TextField txtPwAgain;

	@FXML private TableView favTable;
	@FXML private TableView reviewTable;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxInitSetting();
		
	}
	public void getInfo() {
//		txtName.setText(value);
//		txtNumber.setText(value);
//		txtPw.setText(value);
//		txtPwAgain.setText(value);
		
	}
	
	public void comboBoxInitSetting() {
		AddressDAO addressDAO = new AddressDAO();
		addressGuList = addressDAO.getGu();
		cbGu.setItems(addressGuList);
		cbGu.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				addressDongList = addressDAO.getDong(cbGu.getValue());
				cbDong.setItems(addressDongList);
			}
		});

		cbAgeList = FXCollections.observableArrayList("10", "20", "30", "40", "50", "60", "70", "80", "90");
		cbAge.setItems(cbAgeList);

	}
	
}
