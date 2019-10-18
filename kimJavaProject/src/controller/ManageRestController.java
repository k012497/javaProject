package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.MenuVO;
import model.RestaurantVO;

public class ManageRestController implements Initializable {
	// restaurant management tab
	@FXML
	private TableView<RestaurantVO> restTable;
	@FXML
	private Button btnRestDelete;
	@FXML
	private Button btnRestEdit;
	@FXML
	private Button btnImage;
	@FXML
	private TextField txtRestAddr;
	@FXML
	private TextField txtRestPark;
	@FXML
	private TextField txtResFoodtKind;
	@FXML
	private TextField txtRestName;
	@FXML
	private TextField txtRestReserve;
	@FXML
	private TextField txtRestTakeout;
	@FXML
	private TextField txtRestStars;
	@FXML
	private TextField txtRestDate;
	@FXML
	private TextField txtRestVegeKind;
	@FXML
	private TextField txtRestFav;
	@FXML
	private ImageView imgView;
	@FXML
	private ImageView imgOpenHours;

	@FXML
	private TableView<MenuVO> menuTable;
	@FXML
	private TextField txtMenuName;
	@FXML
	private TextField txtMenuPrice;
	@FXML
	private Button btnMenuEdit;
	@FXML
	private Button btnMenuDelete;
	@FXML
	private Button btnNewRest;
	@FXML
	private Button btnNewMenu;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

	}
}