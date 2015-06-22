package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.Message;
import model.User;

public class ClientHandler extends Thread{

	private Socket s;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String Email;
	private ArrayList<String> friends;
	private ArrayList<String> groups;
	private Server server ; 
	private boolean loggedIn = false;
	private SqlHandler sql;

	public ClientHandler(Server server, Socket s) throws IOException{
		this.s = s;
		this.server = server;
		outStream = new ObjectOutputStream(s.getOutputStream());
		inStream = new ObjectInputStream(s.getInputStream());
		sql = new SqlHandler();
	}

	@Override
	public void run() {
		try {

			Message m = null;
			
			while(true){
				
				try{

					if((m = (Message) inStream.readObject()) != null){
						System.out.println(m.getMessage());
						System.out.println(m.getType());
						messageReceived(m);
					}
				}


				catch(IOException e){
					//System.out.println("END OF STREAM, No object sent");
					//This is a bad quickfix. I have to find a better solution.
				}
			}
		}

		catch (ClassNotFoundException e) {
			System.out.println("IOexeption / ClassNotFoundException");
			e.printStackTrace();

		} 
	}

	public Socket getSocket(){
		return this.s;
	}

	public String getEmail(){
		return this.Email;
	}

	public void messageReceived(Message m){
		
		switch (m.getType()) {

		case "PRIV_MESSAGE": {
			
			sendMessage(m);
			server.broadcastMessageToUser(m);
			break;
		}

		case "LOGIN":{
			
			System.out.println("switch login. username: " + m.getSender() + " password: " + m.getMessage());
			
			if(logIn(m.getSender(), m.getMessage())){
				System.out.println("username is valid!! Hurray");
				this.Email = m.getSender();
				sendInitialData();
			}
			
			break;
		}
		
		case "NEW_USER":{
			
			String[] splitted = m.getMessage().split("-");
			sql.newUser(splitted[0], splitted[1], splitted[2]);
			
			break;
		}
		
		case "ADD_FRIEND":{
			String friendUsername = m.getMessage();
			String username = m.getSender();
			
			sql.addFriends(friendUsername, username);
			
			break;
		}

		default:
			Message error = new Message("SERVER" ,this.getEmail(), "ERROR", "Bad request");
			sendMessage(error);
			break;
		}
	}

	public boolean sendMessage(Message message){

		try {

			outStream.writeObject(message);

		} catch (IOException e) {
			System.out.println("failed to send object to: " + this.getEmail());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean logIn(String Email, String password){
		
		if(sql.validate(Email, password)){
			this.Email = Email;
			return true;
		}
		return false;
	}
	
	public void sendInitialData(){
		
		ArrayList<User> friends = sql.getFriends(this.Email);
		
		Message m = new Message("SERVER" ,this.Email, "initialdata", "INITIAL_DATA" );
		
		if(friends != null || friends.size() > 0){
			m.setFriends(friends);
		}
		
		sendMessage(m);
	}
	
	public void closeConnection(){
		try {
			this.s.close();
		} catch (IOException e) {
			System.out.println("could not close Thread: " + this.getName() + " Username: " + Email);
			e.printStackTrace();
		}
	}
}
