package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class User implements Serializable{
	
	private String username;
	private String email;
	private ObservableList<User> friends;
	private ObservableList<String> friendsAsString;
	private ArrayList<Message> messages;
	
	public User(String username, String email){
		this.username = username;
		this.email = email;
		this.messages = new ArrayList<Message>();
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public ObservableList<User> getFriends(){
		return this.friends;
	}
	
	public void setFriends(ObservableList<User> friends){
		this.friends = friends;
		
		for(User u : friends){
			friendsAsString.add(u.getUsername());
		}
	}
	
	public void addMessage(Message m){
		this.messages.add(m);
	}
	
	public void setMessages(ArrayList<Message> messages ){
		this.messages = messages;
	}
	
	public ArrayList<Message> getMessages(){
		return this.messages;
	}

}
