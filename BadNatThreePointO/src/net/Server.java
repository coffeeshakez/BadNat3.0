package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.Message;

public class Server {
	
	private char port = 8888;
	private ArrayList<ClientHandler> connectedClients;
	
	public Server(){
		connectedClients = new ArrayList<ClientHandler>();
	}
	
	public void run(){
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(8888);
			
			while(true){
				
				System.out.println("waiting for connections");
				Socket s = serverSocket.accept();
				System.out.println(s.getInetAddress() + " Connected");
				ClientHandler c = new ClientHandler(this, s);
				c.start();
				
				this.connectedClients.add(c);
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			finally{
				if(serverSocket != null){
					try {
						serverSocket.close();
					} catch (IOException e) {
						System.out.println("Could not close server.");
						e.printStackTrace();
					}
				}
			}
		}
	
	public boolean broadcastToAll(Message m){
		for(ClientHandler c : connectedClients){
			if(connectedClients.size() < 2){
				return false;
			}
			else{
			
			}
		}
		return true;
	}
	
	public boolean broadcastMessageToUser(Message message){
		
		if(message != null){
			for (ClientHandler c : connectedClients){
				if(c.getEmail().equals(message.getReceiver())){
					c.sendMessage(message);
					return true;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		Server s = new Server();
		s.run();
	}
}