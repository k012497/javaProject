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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomListCell extends ListCell<CustomThing> {
    private HBox content;
    private Text name;
    private Text price;
    private Text address;

    public CustomListCell() {
        super();
        name = new Text();
        price = new Text();
        address = new Text();
        name.setFont(new Font(25.0));
        VBox vBox = new VBox(name, price, address);
//        ImageView imageView = new ImageView();
//        imageView.setImage(new Image("/images/moodindigo.jpg", false));
//        content = new HBox(imageView, vBox);
        
        FileInputStream input;
		try {
			File dirSave = new File("/Users/kimsojin/Desktop/n"); 
			input = new FileInputStream(new File(dirSave.getAbsolutePath()+"//"+"moodindigo.jpg"));
			Image image = new Image(input);
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(150);
			imageView.setFitHeight(150);
			content = new HBox(imageView, vBox);
			content.setSpacing(10);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void updateItem(CustomThing item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getName());
            price.setText(String.valueOf(item.getPrice()));
            address.setText(item.getAddress());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}