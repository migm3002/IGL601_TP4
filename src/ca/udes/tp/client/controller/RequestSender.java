package ca.udes.tp.client.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONObject;

import ca.udes.tp.client.view.ListMatchPanel;
import ca.udes.tp.communication.Message;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.Server;
import ca.udes.tp.server.runnable.RequestListenerUdp;
import ca.udes.tp.tool.JsonUtility;
import ca.udes.tp.tool.UdpUtility;

public class RequestSender {

	public static final int LISTENING_PORT_UDP_SERVER = Server.DEFAULT_LISTENING_PORT_UDP;
	public static final int SENDING_PORT_UDP_CLIENT = 16003;
	public static final int LISTENING_PORT_UDP_CLIENT = 16002;
	public static final String HOME_ADDRESS = "127.0.0.1";

	public RequestSender() {

	}

	public synchronized void sendUdpRequest(Message request) {

		ListeDesMatchs listMatchs = null;

		DatagramSocket requestSocket = null;
		DatagramSocket responseSocket = null;

		try {

			// create socket from which the request is sent
			requestSocket = new DatagramSocket(SENDING_PORT_UDP_CLIENT, InetAddress.getByName(HOME_ADDRESS));

			// convert Message to JSONObject
			JSONObject jsonMsg = JsonUtility.convertMessageIntoJsonMessage(request);
			// convert JSONObject to bytes
			byte[] bufferReq = UdpUtility.convertToBytes(jsonMsg.toString());

			if(bufferReq.length <= RequestListenerUdp.BUFFER_SIZE) {	// if the buffer is smaller than max size
				//create packet to be sent to the server (contains the bytes representing the message)
				DatagramPacket requestPacket = new DatagramPacket(bufferReq, bufferReq.length, InetAddress.getByName(request.getClientAddress()), LISTENING_PORT_UDP_SERVER);
				// send the packet to server
				requestSocket.send(requestPacket);
				
				System.out.println("DEBUG : request for listMatch sent");
				// create buffer to receive data
				byte[] bufferResp = new byte[RequestListenerUdp.BUFFER_SIZE];
				// create the socket to receive response from server
				responseSocket = new DatagramSocket(LISTENING_PORT_UDP_CLIENT, InetAddress.getByName(HOME_ADDRESS));
				responseSocket.setSoTimeout(2500);

				// create the packet to receive data
				DatagramPacket incomingPacket = new DatagramPacket(bufferResp, bufferResp.length);
				
				boolean responseReceived = false;
				while(!responseReceived) {
					try {
						// receive data from server
						responseSocket.receive(incomingPacket);
						System.out.println("DEBUG : packet received");
						responseReceived = true;
					} catch(SocketTimeoutException e) {
						// in case socket times out, resend the request
						requestSocket.send(requestPacket);
						System.out.println("DEBUG : resending packet");
					}
				}
				
				// convert data received from server into Message
				Message msgListMatchs = UdpUtility.convertIntoMessage(incomingPacket);
				// convert Message to JSONObject
				JSONObject jsonListMatchs = JsonUtility.convertMessageIntoJsonMessage(msgListMatchs);
				// convert JSONObject to ListeDesMatchs
				listMatchs = JsonUtility.getListeDesMatchs(jsonListMatchs);
				
			} else {
				System.out.println("ERROR : jsonobject size is larger than limit");
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(requestSocket != null) {
				requestSocket.close();
			}

			if(responseSocket != null) {
				responseSocket.close();
			}
		}

		ListMatchPanel.setListMatchs(listMatchs);
	}

}
