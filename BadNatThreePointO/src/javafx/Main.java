package javafx;

import net.Client;
import net.ServerListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private ServerListener serverComm;
	private char port = 8888;
	private Client client;

	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			Parent root = FXMLLoader.load(Main.class.getResource("/javafx/Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/javafx/test.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}