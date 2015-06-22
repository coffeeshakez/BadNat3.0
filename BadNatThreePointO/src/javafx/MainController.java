package javafx;




import java.net.URL;
import java.util.ResourceBundle;

import model.Message;
import model.User;
import net.Client;
import net.Server;
import net.ServerListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebView;
import javafx.util.Callback;

public class MainController implements Initializable {

	@FXML private TextField inputTextField;
	//	@FXML private TextArea textArea;
	@FXML private ScrollPane textArea;
	private GridPane grid;
	@FXML ListView<User> friendList;

	private Client client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub



		grid = new GridPane();
		textArea.setContent(grid);

		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(100);

		grid.getColumnConstraints().add(c1);
		grid.setStyle("-fx-background-color: white");

		textArea.setFitToHeight(true);
		textArea.setFitToWidth(true);

		textArea.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		

		//Add listener to scrollable area so that the scrollbar can navigate to bottom.
		grid.heightProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldvalue, Object newValue) {

				textArea.setVvalue((Double)newValue );  
			}
		});

		friendList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				
				
				loadMessagesIntoTextArea(newValue);
				
				System.out.println("ListView selection changed from oldValue = " 
						+ oldValue + " to newValue = " + newValue);
			}
		});
		
		
		friendList.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){

			@Override
			public ListCell<User> call(ListView<User> p) {

				ListCell<User> cell = new ListCell<User>(){

					@Override
					protected void updateItem(User t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getUsername());
						}
					}
				};

				return cell;
			}
		});
	}


	public void handleEnterPressed(ActionEvent action){

		String message = inputTextField.textProperty().getValue();
		String Email = friendList.getSelectionModel().getSelectedItem().getEmail();

		if(friendList.getSelectionModel().getSelectedItem() != null){
			client.sendMessageToUser(Email, message);
		}

		inputTextField.clear();
	}


	public void loadMessagesIntoTextArea(User u){
		
		this.grid.getChildren().clear();
		
		if(u.getMessages() != null){
			for(Message m : u.getMessages()){
				System.out.println(m.getMessage());
				displayMessage(m);
			}
		}
	}

	public void displayMessage(Message m){

		VBox vbox = new VBox();

		Image image = new Image(getClass().getResourceAsStream("twitter_icon.jpg"));

		Label message = new Label(m.getMessage());
		message.getStyleClass().add("chat-bubble");
		message.setWrapText(true);

		Label username = new Label("David Bakke    Sendt @ 23:42:10");
		username.getStyleClass().add("username");

		ImageView iv2 = new ImageView(image);

		iv2.setFitWidth(20);
		iv2.setPreserveRatio(true);
		iv2.setSmooth(true);
		iv2.setCache(true);

		message.setGraphic(iv2);

		message.setMinWidth(textArea.getWidth() - 30);
		message.setMaxWidth(textArea.getWidth() - 30);

		vbox.getChildren().add(username);
		vbox.getChildren().add(message);

		grid.setHalignment(vbox, HPos.RIGHT);
		grid.addRow(grid.getChildren().size(), vbox);
		grid.setMargin(vbox, new Insets(0,0,20,0));


		//		WebView web = new WebView();
		//		web.getEngine().load("https://www.youtube.com/embed/LQYNs4wqlO8");
		//		//web.getEngine().load("http://www.youtube.com/embed/utUPth77L_o?autoplay=1");
		//		web.setPrefSize(300, 250);
		//		grid.addRow(grid.getChildren().size(), web);
	}

	public void setClient(Client client){
		this.client = client;
		client.setMainController(this);
	}
	public void setFriendList(ObservableList<User> friends){
		this.friendList.setItems(friends);
	}
	
	public User getActiveFriendListItem(){
		return this.friendList.getSelectionModel().getSelectedItem();
	}
}
