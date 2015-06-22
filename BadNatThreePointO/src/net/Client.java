package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.LoginController;
import javafx.MainController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Message;
import model.User;

public class Client implements Runnable {
	private Client c;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Socket s;
	private String host;
	private char port;
	private ServerListener serverlistener;
	private MainController controller;
	private LoginController loginController;
	private User user;
	private ObservableList<User> friends;
	private ObservableList<String> friendsString;
	private String Email;

	public Client(String host, char port, LoginController controller){
		this.host = host;
		this.port = port;
		this.loginController = controller;
		this.friends = FXCollections.observableArrayList();
	}

	@Override
	public void run() {

		try {

			s = new Socket(host, port);

			outStream = new ObjectOutputStream(s.getOutputStream());
			inStream = new ObjectInputStream(s.getInputStream());

			while(true){

				Message m = null;

				try {
					if((m = (Message) inStream.readObject()) != null){
						System.out.println(m.getMessage());
						System.out.println(m.getType());
						messageReceived(m);
					}

				} catch (ClassNotFoundException e) {
					System.out.println("ObjectSendt Through stream is NOT of type Message");
					e.printStackTrace();
					
				} catch (IOException e) {
					System.out.println("Failed to read inputStream in class Client");
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void messageReceived(Message m){

		switch (m.getType()) {

		case "PRIV_MESSAGE":{

			System.out.println("messageFrom: " + m.getSender() + " " + "content: " + m.getMessage());

			
			//Checks if this instance is the sender of the email. Adds it to the conversation
			if(m.getSender().equals(this.Email)){
				
				for(User u : friends){
					if(u.getEmail().equals(m.getReceiver())){
						u.addMessage(m);
					}
				}
			}
			
			//Adds the message to the list of conversations, if not from server.
			for(User u : friends){
				if(u.getEmail().equals(m.getSender())){
					u.addMessage(m);
				}
			}
			
			//Displays the message(have to find a better way of doing this. Some kind of listener on the conversationlist?)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					
					User u = null;
					if((u = controller.getActiveFriendListItem()) != null){
						if(u.getEmail().equals(m.getSender())){
							controller.displayMessage(m);
						}
						else{
							controller.displayMessage(m);
						}
					}
				}
			}); 

		break;
		}

	case "INITIAL_DATA":{

		this.Email = m.getReceiver();

		if(m.getFriends() != null){

			for(User u : m.getFriends()){
				System.out.println("Friends: " + u.getEmail());
				this.friends.add(u);
			}
		}

		loginController.setClient(this);
		loginController.startMain();
		this.controller.setFriendList(this.getFriends());

		break;
	}

	default:
		break;
	}
}

public void sendMessageToUser(String user, String message){

	Message m = new Message(this.Email, user,  message, "PRIV_MESSAGE");

	try {

		outStream.writeObject(m);
		outStream.flush();

	} catch (IOException e) {
		System.out.println("failed to send message to user. IOException");
		e.printStackTrace();
	}
}

public void sendMessage(String fromUsername, String message, String type){
	Message m = new Message(fromUsername, null, message, type);

	try {

		if(outStream != null){
			outStream.writeObject(m);
			outStream.flush();
		}

		else{
			System.out.println("outstream er null, hvorfor det montro");
		}

	} catch (IOException e) {
		// TODO: handle exception
	}
}

public boolean registerNewUser(String email, String password, String username){

	String message = email + "-" + password + "-" + username;

	Message m = new Message(null, message, "NEW_USER");

	try {
		outStream.writeObject(m);
		outStream.flush();

	} catch (IOException e) {
		System.out.println("failed to send message to server.(NEW_USER)");
		e.printStackTrace();
		return false;
	}
	return true;
}

public void setMainController(MainController controller){
	this.controller = controller;
}

public ObservableList<User> getFriends(){

	return this.friends;
}
}