package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import controller.CustomThing;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
 * listView Cell 정의 및 생성을 위한 클래스
 */

public class CustomListCell extends ListCell<CustomThing> {
	private HBox content;
	private Text name;
	private Text address;
	private Text avgStars;
	private ImageView imgView = new ImageView();

	public CustomListCell() {
		super();
		name = new Text();
		address = new Text();
		avgStars = new Text();
		VBox vBox = new VBox(name, address, avgStars);

		content = new HBox(imgView, vBox);
		content.setSpacing(10);
	}

	@Override
	protected void updateItem(CustomThing item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null && !empty) { // test for null item and empty parameter
			name.setText(item.getName());
			address.setText(String.valueOf(item.getAddress()));
			avgStars.setText(String.valueOf(item.getAvgStars()));
			FileInputStream input;
			try {
				File dirSave = new File("/Users/kimsojin/Desktop/code/images");
				input = new FileInputStream(new File(dirSave.getAbsolutePath() + "//" + item.getFileName()));
				Image image = new Image(input);
				imgView.setImage(image);
				imgView.setFitWidth(100);
				imgView.setFitHeight(100);

			} catch (FileNotFoundException e) {
			}
			setGraphic(content);
		} else {
			setGraphic(null);
		}
	}
}