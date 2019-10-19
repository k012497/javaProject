package application;

import controller.AdminController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppMain extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {

//		Scene scene = new Scene(new StackPane());
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminPage.fxml"));
//		scene.setRoot(loader.load());
//		AdminController controller = loader.getController();
//		controller.init();

    	Parent loader = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(loader);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
