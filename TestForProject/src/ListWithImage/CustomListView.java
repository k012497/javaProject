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

public class CustomListView extends Application {
	private static class CustomThing {
		private int restaurantId;
		private String name;
		private String address;
		private double avgStars;
		private String fileName;

		public String getName() {
			return name;
		}

		public int getRestaurantId() {
			return restaurantId;
		}

		public String getFileName() {
			return fileName;
		}
		
		public String getAddress() {
			return address;
		}
		
		public double getAvgStars() {
			return avgStars;
		}

		public CustomThing(int restaurantId, String name, String address, double avgStars, String fileName) {
			super();
			this.restaurantId = restaurantId;
			this.name = name;
			this.address = address;
			this.avgStars = avgStars;
			this.fileName = fileName;
		}
	}

/////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ObservableList<CustomThing> data = FXCollections.observableArrayList();
		data.addAll(new CustomThing(1,"Cheese", "22", 1.23, "r1571635861165_heart_icon-icons.com_54429.png"));

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

/////////////////////////////////////////////////////////////////////////////////
	private class CustomListCell extends ListCell<CustomThing> {
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
			if (item != null && !empty) { // <== test for null item and empty parameter
				name.setText(item.getName());
				address.setText(String.valueOf(item.getAddress()));
				avgStars.setText(String.valueOf(item.getAvgStars()));
				FileInputStream input;
				try {
					File dirSave = new File("/Users/kimsojin/Desktop/code/images");
					input = new FileInputStream(new File(dirSave.getAbsolutePath() + "//" + item.getFileName()));
					Image image = new Image(input);
					imgView.setImage(image);
					imgView.setFitWidth(200);
					imgView.setFitHeight(200);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				setGraphic(content);
			} else {
				setGraphic(null);
			}
		}
	}

}