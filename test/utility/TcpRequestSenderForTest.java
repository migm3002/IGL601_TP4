package utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import ca.udes.tp.client.jarclient.view.BetPanel;
import ca.udes.tp.object.Pari;
import ca.udes.tp.server.Server;
import ca.udes.tp.tool.JsonUtility;

public class TcpRequestSenderForTest implements Runnable {
	
	private static final String HOME_ADDRESS = "127.0.0.1";
	private static final int LISTENING_PORT_TCP_SERVER = Server.DEFAULT_LISTENING_PORT_TCP+1;
	private static final int DEFAULT_LISTENING_PORT_TCP_CLIENT = 17000;
	public boolean sent;
	
	
	
	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}


	public void run() {
		
		System.out.println("DEBUG : in run TcpRequestSenderForTest");
		
		boolean sent = false;
		
		String jsonStringRequest = "{"
				+ "\"request\" : true,"
				+ "\"requestNumber\" : 1456,"
				+ "\"method\" : \"bet\","
				+ "\"clientAddress\" : "+HOME_ADDRESS+","
				+ "\"clientPort\" : "+DEFAULT_LISTENING_PORT_TCP_CLIENT+","
				+ "\"pari\" :"
				+ "{"
				+ "\"idJoueur\" : 1,"
				+ "\"idParieur\" : 1,"
				+ "\"idMatch\" : 1,"
				+ "\"montant\" : 100.0,"
				+ "\"status\" : \"asked\""
				+ "}"
				+ "}";

		Socket outputSocket = null;
		try {
			// create the JSON from the previous String
			JSONObject jsonRequest = new JSONObject(jsonStringRequest);

			// Create the socket that will send the request
			outputSocket = new Socket(InetAddress.getByName(HOME_ADDRESS), LISTENING_PORT_TCP_SERVER);
			DataOutputStream outToServer = new DataOutputStream(outputSocket.getOutputStream());

			// send the request to the server
			outToServer.writeBytes(jsonRequest.toString()+"\n");

			// Create the BufferedReader that will read the response coming from the server
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(outputSocket.getInputStream()));
			// Read the response and put it in a String
			String jsonStringResponse = bufferedReader.readLine();
			// Transform the String response into JSON
			System.out.println("DEBUG : message received from RequestListenerTcp : "+jsonStringResponse);
			JSONObject jsonResponse = JsonUtility.getJsonObjectFromString(jsonStringResponse);
			// Find the part about the bet and put it in a "Pari" object type
			Pari pariResponse =JsonUtility.getPariFromPariJsonObject(jsonResponse.getJSONObject("pari"));


			if(pariResponse.getStatus().equals(Pari.Status.commited)) {	// if the bet previously created has a "commited" status
				sent = true;
			} else {
				sent = false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(outputSocket!=null) {
				try {
					outputSocket.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		this.setSent(sent);
	}

}
