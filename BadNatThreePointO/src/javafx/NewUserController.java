package javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class NewUserController implements Initializable {

	
	@FXML TextField email;
	@FXML TextField email2;
	@FXML TextField password;
	@FXML Button sendButton;
	
	private Client client;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleSend(ActionEvent event){
		
		if(client.registerNewUser(email.getText(), password.getText(), null)){
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafx/Login.fxml"));

			Parent root;
			try {
				root = fxmlLoader.load();
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			LoginController controller = fxmlLoader.getController();
			controller.setClient(this.client);

			Platform.runLater(() -> {
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.show();
				((Node)(event.getSource())).getScene().getWindow().hide(); //Hides existing window
			});
			
			
			((Node)(event.getSource())).getScene().getWindow().hide(); //Hides existing window
			System.out.println("jada kommer jo helt hit da for fænden");
		}
		
		else{
			System.out.println("failed to create new user");
		}
	}
	
	public void setClient(Client client){
		this.client = client;
	}
	
	
	
	
}
