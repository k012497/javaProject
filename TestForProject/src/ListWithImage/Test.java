package ListWithImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ObservableList<CustomThing> data = FXCollections.observableArrayList();
		data.addAll(new CustomThing(1, "", "", 1.1, ""));

		final ListView<CustomThing> listView = new ListView<CustomThing>(data);
		listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
			@Override
			public ListCell<CustomThing> call(ListView<CustomThing> listView) {
				return new CustomListCell();
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(listView);
		primaryStage.setScene(new Scene(root, 500, 500));
		primaryStage.show();
	}

    public class CustomListCell extends ListCell<CustomThing> {
        private HBox content;
        private Text name;
        private Text price;

        public CustomListCell() {
            super();
            name = new Text();
            price = new Text();
            VBox vBox = new VBox(name, price);
            ImageView imageView = new ImageView();
            imageView.setImage(new Image("/images/moodindigo.jpg", false));
            content = new HBox(imageView, vBox);
            
            FileInputStream input;
			try {
				File dirSave = new File("/Users/kimsojin/Desktop/n"); 
				input = new FileInputStream(new File(dirSave.getAbsolutePath()+"//"+"moodindigo.jpg"));
				Image image = new Image(input);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(200);
				imageView.setFitHeight(200);
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
                price.setText(String.format("%d $", item.getPrice()));
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}
