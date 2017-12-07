package ca.udes.tp.client;

import ca.udes.tp.client.controller.RequestHandlerClient;
import ca.udes.tp.client.controller.RequestSender;
import ca.udes.tp.client.view.ListMatchPanel;
import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.server.Server;
import ca.udes.tp.server.runnable.RequestListenerUdp;
import ca.udes.tp.tool.JsonUtility;
import ca.udes.tp.tool.UdpUtility;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

public class ClientTest {
	
	private Server serverTest = new Server();
	//private RequestHandlerClient requestHandlerClientTest;
	//private RequestSender requestSenderTest;
	private ListeDesMatchs listeDesMatchsTest = new ListeDesMatchs();
	
	private Message msg1 = new Message (true, 0, Method.updateScore, Message.EMPTY_ARGUMENT, RequestHandlerClient.HOME_ADDRESS, RequestHandlerClient.LISTENING_PORT_UDP_CLIENT);
	
	
	//on simule la fonction sendRequest pour retourner un objet ListeDesMatchs
	public ListeDesMatchs sendRequestBis (Message request) {
		this.serverTest.run();
		ListeDesMatchs listMatchs = null;

		DatagramSocket requestSocket = null;
		DatagramSocket responseSocket = null;

		try {

			// create socket from which the request is sent
			requestSocket = new DatagramSocket(RequestSender.SENDING_PORT_UDP_CLIENT, InetAddress.getByName(RequestSender.HOME_ADDRESS));

			// convert Message to JSONObject
			JSONObject jsonMsg = JsonUtility.convertMessageIntoJsonMessage(request);
			// convert JSONObject to bytes
			byte[] bufferReq = UdpUtility.convertToBytes(jsonMsg.toString());

			if(bufferReq.length <= RequestListenerUdp.BUFFER_SIZE) {	// if the buffer is smaller than max size
				//create packet to be sent to the server (contains the bytes representing the message)
				DatagramPacket requestPacket = new DatagramPacket(bufferReq, bufferReq.length, InetAddress.getByName(request.getClientAddress()), RequestSender.LISTENING_PORT_UDP_SERVER);
				// send the packet to server
				requestSocket.send(requestPacket);
				
				System.out.println("DEBUG : request for listMatch sent");
				// create buffer to receive data
				byte[] bufferResp = new byte[RequestListenerUdp.BUFFER_SIZE];
				// create the socket to receive response from server
				responseSocket = new DatagramSocket(RequestSender.LISTENING_PORT_UDP_CLIENT, InetAddress.getByName(RequestSender.HOME_ADDRESS));
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
		return listMatchs;
	}
	
	@Test
	public void testSendUdpRequest() {	
		boolean same = true;
		//On commpare les contenus des deux objets listeDesMatchs : 
		//la liste attendue au debut et la liste reçue du serveur au debut de la demo
		ListeDesMatchs list1 = this.listeDesMatchsTest;
		ListeDesMatchs list2 = this.sendRequestBis(msg1);
		ArrayList<Match> array1 = list1.getListeMatchs();
		ArrayList<Match> array2 = list2.getListeMatchs();
		
		for(int i=0; i<array1.size(); i++) {

			//Joueurs
			if(list1.get(i).getJoueur1().getId()!=list2.get(i).getJoueur1().getId()) {
				same=false;
			}
			if(list1.get(i).getJoueur2().getId()!=list2.get(i).getJoueur2().getId()) {
				same=false;
			}
			//Contestations
			if(list1.get(i).getContestationsRestantesJoueur1()!=list2.get(i).getContestationsRestantesJoueur1()) {
				same=false;
			}
			if(list1.get(i).getContestationsRestantesJoueur2()!=list2.get(i).getContestationsRestantesJoueur2()) {
				same=false;
			}
			//Chronometre
			if(!list1.get(i).getChronometre().equals(list2.get(i).getChronometre())) {
				same=false;
			}
			
			//TabSet
			if(!list1.get(i).getScore().getTabSet()[0][0].equals(list2.get(i).getScore().getTabSet()[0][0])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabSet()[0][1].equals(list2.get(i).getScore().getTabSet()[0][1])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabSet()[0][2].equals(list2.get(i).getScore().getTabSet()[0][2])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabSet()[1][0].equals(list2.get(i).getScore().getTabSet()[1][0])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabSet()[1][1].equals(list2.get(i).getScore().getTabSet()[1][1])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabSet()[1][2].equals(list2.get(i).getScore().getTabSet()[1][2])){
				same=false;
			}
			
			//TabJeu
			if(!list1.get(i).getScore().getTabJeu()[0].equals(list2.get(i).getScore().getTabJeu()[0])){
				same=false;
			}
			if(!list1.get(i).getScore().getTabJeu()[1].equals(list2.get(i).getScore().getTabJeu()[1])){
				same=false;
			}
		}
		assertTrue("La liste des matchs reçue est differente de celle attendue",same);
	}
}
