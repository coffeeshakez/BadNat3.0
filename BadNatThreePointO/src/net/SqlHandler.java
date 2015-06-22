package net;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;



public class SqlHandler {


	private  String url = "jdbc:mysql://mysql.stud.ntnu.no/dabakke_chat";
	private  String username = "dabakke_admin";
	private  String passwd = "admin1337";
	private  Connection connection = null;
	private  java.sql.Statement statement = null;
	private  ResultSet resultSet = null;



	public SqlHandler(){

		try {
			System.out.println("Loading MYSQL driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("MYSQL Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
	}
	
	
	public void connect() {

		System.out.println("Connecting to SQLdatabase...");
		try {
			connection = (Connection) DriverManager.getConnection(url, username, passwd);
		} catch (SQLException e) {
			System.out.println("Could not establish a connection to the SQL database");
			e.printStackTrace();
		}
		System.out.println("connection to Mysql established");
	}
	
	public  void closeConnection(){

		if(connection != null){

			try {
				System.out.println("Closing Mysql connection...");
				connection.close();

				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
			}

			catch (SQLException e) {
				System.out.println("Failed to close connections" + e);
			}
			System.out.println("Connection to Mysql closed!");
		}
	}
	
	public boolean validate(String Email, String password){
		
		Boolean valid = false;
		
		connect();

		if(connection != null){
			try {
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(
						"SELECT Email FROM users WHERE " +
								"Email=" +  "'" + Email + "'" + " AND PasswordHash= " + "'" + password +  "'");

				if(rs.next()){
					System.out.println("Yep, correct");
					valid = true;
				}

			} catch (SQLException e) {
				System.out.println("sql exception while retrieveing username");
				e.printStackTrace();
			}
			
			finally{
				closeConnection();
			}
		}
		return valid;
		
	}
	
	public boolean newUser(String Email, String Password, String Username){
		
		String email;
		String pwd;
		String uname;
		
		if(Email != null){
			email = Email;
		}
		
		else return false;
		
		if(Password != null){
			pwd = Password;
		}
		
		else return false;
		
		if(Username != null){
			uname = Username;
		}
		else return false;
	
		connect();

		try {
			statement = connection.createStatement();
			java.sql.PreparedStatement add = connection.prepareStatement(
					"INSERT INTO users (Email, Username, PasswordHash) VALUES(?, ?, ?)");
			add.setString(1, email);
			add.setString(2, uname);
			add.setString(3, pwd);
			
			add.executeUpdate();
			
			System.out.println("sucsessfully added user to database: " + email);
			return true;

		} catch (SQLException e) {
			System.out.println("failed to add user to database");
			e.printStackTrace();
			return false;
		}

		finally{
			closeConnection();
		}
	}
	
	public String getUserIdFromUsername(String Email){
		String userID = null;

		connect();

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT UserID FROM users WHERE Email = " + "'" + Email + "'");

			rs.next();
			userID = rs.getString("UserID");

		} catch (SQLException e) {
			System.out.println("failed to get user id from username");
			e.printStackTrace();
		}
		
		finally{
			closeConnection();
		}
		
		return userID;
	}
	
	public boolean addFriends(String username, String username2){
		
		connect();
		
		String u1 = getUserIdFromUsername(username);
		String u2 = getUserIdFromUsername(username2);
		
		try{
			java.sql.PreparedStatement add = connection.prepareStatement("INSERT INTO friendRelations(UID1, UID2) VALUES (?,?)");
			add.setString(1, u1);
			add.setString(2, u2);
			add.executeUpdate();
			
			java.sql.PreparedStatement add2 = connection.prepareStatement("INSERT INTO friendRelations(UID1, UID2) VALUES (?,?)");
			add.setString(1, u2);
			add.setString(2, u1);
			add2.executeUpdate();
			
			System.out.println("sucsessfully created friendship");
			return true;
		}
		
		catch (SQLException e){
			System.out.println("could not insert new friendship into friends");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public ArrayList<User> getFriends(String username){
		
		String userid = getUserIdFromUsername(username);
		ArrayList<User> friends = new ArrayList<User>();
		
		connect();
		
		try {
			
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(
							"SELECT Username, Email FROM users"
							+" INNER JOIN friendRelations ON friendRelations.UID2 = users.UserID"
							+" WHERE friendRelations.UID1 = " + "'" + userid + "'");
			
			while(rs.next()){
				User u = new User(rs.getString("Username"), rs.getString("Email"));
				friends.add(u);
			}
			
		} catch (SQLException e) {
			System.out.println("failed to fetch friends from database.");
			e.printStackTrace();
		}
		
		finally{
			closeConnection();
		}
		
		for(User u : friends){
			System.out.println(u.getEmail());
		}
		
		return friends;
	}
}