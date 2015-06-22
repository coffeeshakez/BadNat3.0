package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8913357450797102339L;
	private String message, EmailSender, EmailReceiver;
	private String type;
	private ArrayList<User> friends;
	
	public Message(String EmailReceiver, String message, String type){
		this.message = message;
		this.EmailReceiver = EmailReceiver;
		this.type = type;
	}
	
	public Message(String EmailSender, String EmailReceiver, String message, String type){
		this(EmailReceiver, message, type);
		this.EmailSender = EmailSender;
	}
	
	
	public String getMessage(){
		return message;
	}
	
	public String getReceiver(){
		return this.EmailReceiver;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getSender(){
		return this.EmailSender;
	}
	
	public void setFriends(ArrayList<User> friends){
		this.friends = friends;
	}
	
	public void addFriend(User user){
		this.friends.add(user);
	}
	
	public ArrayList<User> getFriends(){
		return this.friends;
	}
}
