package utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONObject;

import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.Server;
import ca.udes.tp.server.runnable.RequestListenerUdp;
import ca.udes.tp.tool.JsonUtility;
import ca.udes.tp.tool.UdpUtility;

public class UdpRequestSenderForTest implements Runnable {

	private static final int LISTENING_PORT_UDP_SERVER = Server.DEFAULT_LISTENING_PORT_UDP+1;
	private static final int SENDING_PORT_UDP_CLIENT = 16003;
	private static final int LISTENING_PORT_UDP_CLIENT = 16002;
	private static final String HOME_ADDRESS = "127.0.0.1";

	public void run() {

		DatagramSocket requestSocket = null;

		try {

			Message request = new Message(true, 1, Method.updateScore, Message.EMPTY_ARGUMENT,
					HOME_ADDRESS, LISTENING_PORT_UDP_CLIENT);

			// create socket from which the request is sent
			requestSocket = new DatagramSocket(SENDING_PORT_UDP_CLIENT, InetAddress.getByName(HOME_ADDRESS));

			// convert Message to JSONObject
			JSONObject jsonMsg = JsonUtility.convertMessageIntoJsonMessage(request);
			// convert JSONObject to bytes
			byte[] bufferReq = UdpUtility.convertToBytes(jsonMsg.toString());

			if(bufferReq.length <= RequestListenerUdp.BUFFER_SIZE) {	// if the buffer is smaller than max size
				//create packet to be sent to the server (contains the bytes representing the message)
				DatagramPacket requestPacket = new DatagramPacket(bufferReq, bufferReq.length, InetAddress.
						getByName(request.getClientAddress()), LISTENING_PORT_UDP_SERVER);
				System.out.println("DEBUG : sending to port "+LISTENING_PORT_UDP_SERVER);
				// send the packet to server
				requestSocket.send(requestPacket);

				System.out.println("DEBUG : request for listMatch sent (test)");

			} else {
				System.out.println("ERROR : jsonobject size is larger than limit");
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(requestSocket != null) {
				requestSocket.close();
			}
		}
	}

}
