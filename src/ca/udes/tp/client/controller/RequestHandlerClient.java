package ca.udes.tp.client.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import ca.udes.tp.client.view.BetPanel;
import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.Pari;
import ca.udes.tp.server.ResponseSender;
import ca.udes.tp.server.Server;
import ca.udes.tp.tool.JsonUtility;

public class RequestHandlerClient implements Runnable {

	public static final int LISTENING_PORT_UDP_SERVER = Server.DEFAULT_LISTENING_PORT_UDP;
	public static final int SENDING_PORT_UDP_SERVER = ResponseSender.DEFAULT_SENDING_PORT_UDP;
	public static final int LISTENING_PORT_TCP_SERVER = Server.DEFAULT_LISTENING_PORT_TCP;
	public static final int LISTENING_PORT_UDP_CLIENT = 16002;
	public static final int SENDING_PORT_UDP_CLIENT = 16003;
	public static final int DEFAULT_LISTENING_PORT_TCP_CLIENT = 17000;
	public static final String HOME_ADDRESS = "127.0.0.1";

	private MethodClient method;
	private ArrayList<Object> arguments;
	private RequestSender requestSender;

	public enum MethodClient{
		initListeDesMatchs,
		bet,
		getEarnings
	}

	public RequestHandlerClient(MethodClient method, ArrayList<Object> args, RequestSender requestSender) {
		this.method = method;
		this.arguments = args;
		this.requestSender = requestSender;
	}

	
	public void run() {
		switch(method) {

		case initListeDesMatchs :

			if(this.arguments.isEmpty()) {		// arguments are supposed to be inexistent here. Arguments are only necessary for bets
				// create message that contains the method to execute on the server (updateScore)
				Message msg = new Message(true, 0, Method.updateScore, Message.EMPTY_ARGUMENT, HOME_ADDRESS, LISTENING_PORT_UDP_CLIENT);

				requestSender.sendUdpRequest(msg);	// ask RequestSender to send the message to the server

			} else {
				System.out.println("ERROR : arguments do not match the methode invoked");
			}

			break;

		case bet :

			if(!this.arguments.isEmpty()) {
				// Create the string that will be formatted to JSON to be sent to the server
				String jsonStringRequest = "{"
						+ "\"request\" : true,"
						+ "\"requestNumber\" : 1456,"
						+ "\"method\" : \"bet\","
						+ "\"clientAddress\" : "+HOME_ADDRESS+","
						+ "\"clientPort\" : "+DEFAULT_LISTENING_PORT_TCP_CLIENT+","
						+ "\"pari\" :"
						+ "{"
						+ "\"idJoueur\" : "+arguments.get(0)+","
						+ "\"idParieur\" : "+arguments.get(1)+","
						+ "\"idMatch\" : "+arguments.get(2)+","
						+ "\"montant\" : "+arguments.get(3)+","
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
					JSONObject jsonResponse = JsonUtility.getJsonObjectFromString(jsonStringResponse);
					// Find the part about the bet and put it in a "Pari" object type
					Pari pariResponse =JsonUtility.getPariFromPariJsonObject(jsonResponse.getJSONObject("pari"));


					if(pariResponse.getStatus().equals(Pari.Status.commited)) {	// if the bet previously created has a "commited" status
						// the bet is added to the gambler's list of bets
						BetPanel.getListParis().add(pariResponse);
						// A pop up appears to inform the gambler that their bet was correctly recorded in DB
						JOptionPane.showMessageDialog((JPanel)arguments.get(4), "Votre pari a bien ete enregistre", "Pari", JOptionPane.INFORMATION_MESSAGE);
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
			} else {
				System.out.println("ERROR : arguments missing, impossible to place bet");
			}

			break;

		case getEarnings :

			if(!this.arguments.isEmpty()) {
				// Create the String that will be formatted to JSON to be sent to the server
				String jsonStringRequest = "{"
						+ "\"request\" : true,"
						+ "\"requestNumber\" : 1456,"
						+ "\"method\" : \"getEarnings\","
						+ "\"clientAddress\" : "+HOME_ADDRESS+","
						+ "\"clientPort\" : "+DEFAULT_LISTENING_PORT_TCP_CLIENT+","
						+ "\"betInfo\" :"
						+ "{"
						+ "\"idJoueur\" : "+arguments.get(2)+","
						+ "\"idParieur\" : "+arguments.get(0)+","
						+ "\"idMatch\" : "+arguments.get(1)+","
						+ "\"montant\" : "+arguments.get(3)+","
						+ "\"status\" : "+arguments.get(4)+""
						+ "}"
						+ "}";

				Socket outputSocket = null;
				try {
					// Transform the previous String into JSON
					JSONObject jsonRequest = new JSONObject(jsonStringRequest);

					// Create the socket that will send the request to the server
					outputSocket = new Socket(InetAddress.getByName(HOME_ADDRESS), LISTENING_PORT_TCP_SERVER);
					DataOutputStream outToServer = new DataOutputStream(outputSocket.getOutputStream());

					// Send the request to the server
					outToServer.writeBytes(jsonRequest.toString()+"\n");

					// Create the BufferedReader that will receive the response from the server
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(outputSocket.getInputStream()));
					// Read the response and put it in a String
					String jsonStringResponse = bufferedReader.readLine();
					// Transform the String response into JSON
					JSONObject jsonResponse = JsonUtility.getJsonObjectFromString(jsonStringResponse);
					// Find the part about the bet and put it in a "Pari" object type
					Pari pariResponse =JsonUtility.getPariFromPariJsonObject(jsonResponse.getJSONObject("betEarnings"));
					// Inform the gambler of their earnings
					JOptionPane.showMessageDialog((JFrame)arguments.get(5),
							"Match : "+pariResponse.getIdMatch()
							+ "\nVous avez gagne : "
							+pariResponse.getMontant()
							+" $CA",
							"Pari",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
			}

		default :
			System.out.println("ERROR : unknown method");
		}
	}

}
