package javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import model.Message;
import net.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{

	@FXML TextField username;
	@FXML TextField password;
	@FXML Button newUserButton;
	@FXML Button login;
	private Client c;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startConnection();

	}

	public void handleLogin(){
		String uname = username.getText();
		String pwd = password.getText();

		System.out.println("username gui: " + uname);
		System.out.println("passord: " + pwd);

		c.sendMessage(uname, pwd, "LOGIN");
	}

	public void runNewUser(ActionEvent event){ //Opens a new stage when new user is pressed
		
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafx/NewUser.fxml"));

		Parent root;
		try {
			root = fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		NewUserController controller = fxmlLoader.getController();
		controller.setClient(this.c);

		Platform.runLater(() -> {
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide(); //Hides existing window
		});
	}
	
	public void setClient(Client c){
		this.c = c;
	}


	public void startConnection(){
		char port = 8888;
		c = new Client("localhost", port, this);
		Thread t = new Thread(c);
		t.start();
	}

	public void startMain(){
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafx/chatProgram.fxml"));
		Stage s = (Stage) this.login.getScene().getWindow();
		
		
		Parent root;
		try {
			root = fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		
		root.getStylesheets().add("/javafx/test.css");
		
		MainController mainController = fxmlLoader.getController();
		mainController.setClient(this.c);

		Platform.runLater(() -> {
			s.close();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		});
	}
}
