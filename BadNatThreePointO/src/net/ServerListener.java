package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.MainController;
import model.Message;

public class ServerListener implements Runnable{

	private Client c;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Socket s;
	private String host;
	private char port;
	private MainController controller;
	
	public ServerListener(Socket s, MainController controller) throws IOException{
		this.s = s;
		this.controller = controller;
		this.outStream = new ObjectOutputStream(s.getOutputStream());
		this.inStream = new ObjectInputStream(s.getInputStream());
		
		
	}
	
	@Override
	public void run() {
		
		while(true){
			
			Message m = null;
				
				try {
					if((m = (Message) inStream.readObject()) != null){
						System.out.println(m.getMessage());
						System.out.println(m.getType());
						messageReceived(m);
					}
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private void messageReceived(Message m){
		
		switch (m.getType()) {
		
		case "PRIV_MESSAGE":
			
			controller.displayMessage(m);
			break;

		default:
			break;
		}
	}
}