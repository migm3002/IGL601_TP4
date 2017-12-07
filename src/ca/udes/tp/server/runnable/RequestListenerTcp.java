package ca.udes.tp.server.runnable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

import ca.udes.tp.communication.Message;
import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.server.Server;
import ca.udes.tp.tool.JsonUtility;

/**
 * Listens on TCP port for new requests, and answer them.
 *
 */
public class RequestListenerTcp implements Runnable{

	private int listeningPort;			//Port given to clients, on which they must connect with TCP protocol.
	private DatabaseManager dbManager;	//DatabaseManager, which manages database actions.

	public RequestListenerTcp(int listeningPort, Server server) {
		this.listeningPort=listeningPort;
		this.dbManager=server.getDbManager();
	}

	public RequestListenerTcp(Server server) {
		this.listeningPort=Server.DEFAULT_LISTENING_PORT_TCP;
		this.dbManager=server.getDbManager();
	}
	
	public void run() {

		ServerSocket requestServerSocket = null;
		Socket requestSocket = null;
		try {
			requestServerSocket = new ServerSocket(getListeningPort());

			while (true) {

				requestSocket = requestServerSocket.accept();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(requestSocket.getOutputStream());

				//Read Json String message given by client.
				String jsonString = bufferedReader.readLine();

				Message request = JsonUtility.convertJsonStringIntoMessage(jsonString);
				//Put the client message into DatabaseManager's input queue.
				dbManager.getQueueInput().put(request);

				//Get the response given by DatabaseManager in its output queue (blocking method while queue is empty). 
				Message response = dbManager.getQueueOutput().take();
				System.out.println("TCP response successfully sent");
				JSONObject jsonResponse = JsonUtility.convertMessageIntoJsonMessage(response);				
				
				//Send response to client.
				outToClient.writeBytes(jsonResponse.toString()+"\n");

			}
		}catch(IOException e) {
			e.printStackTrace();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			
			//Close the socket
			try {
				if(requestSocket!=null) {
					requestSocket.close();
				}
				if(requestServerSocket!=null) {
					requestServerSocket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}


	public int getListeningPort() {
		return listeningPort;
	}

	public DatabaseManager getDatabaseManager() {
		return this.dbManager;
	}
}
