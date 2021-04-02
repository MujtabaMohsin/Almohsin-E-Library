package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("„ﬂ »… «·„Õ”‰ «·≈·ﬂ —Ê‰Ì…");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.jpg")));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
