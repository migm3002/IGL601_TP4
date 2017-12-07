package ca.udes.tp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONObject;

import ca.udes.tp.communication.Message;
import ca.udes.tp.server.runnable.RequestListenerUdp;
import ca.udes.tp.tool.JsonUtility;
import ca.udes.tp.tool.UdpUtility;

/**
 * Sends UDP responses given by threads from the server thread pool.
 * Sending method is thread-safe, because of the several threads 
 * from threadPool which can call it at the same time.
 */
public class ResponseSender{

	public static final int DEFAULT_SENDING_PORT_UDP=16100;

	private int sendingPortUdp;

	public ResponseSender() {
		this.sendingPortUdp=DEFAULT_SENDING_PORT_UDP;
	}

	/**
	 * Sends back a UDP response to the client, with the given information.
	 * @param Message : response
	 * @return true if response is successfully sent.
	 */
	public synchronized boolean sendUdpResponse(Message response){
		boolean sent=false;
		
		JSONObject jsonMessage = JsonUtility.convertMessageIntoJsonMessage(response);

		DatagramSocket responseSocket = null;
		try{
			
			responseSocket = new DatagramSocket(getSendingPortUdp());
			byte[] buffer = UdpUtility.convertToBytes(jsonMessage.toString());

			if(buffer.length<=RequestListenerUdp.BUFFER_SIZE) {

				DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length,
						InetAddress.getByName(response.getClientAddress()), response.getClientPort());

				responseSocket.send(responsePacket);

				sent=true;
			}else {
				System.out.println("Error : json object size is larger than limit");
				sent=false;
			}
		}catch(SocketException e) {
			e.printStackTrace();
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(responseSocket!=null) {
				responseSocket.close();
			}
		}

		return sent;
	}

	public int getSendingPortUdp() {
		return sendingPortUdp;
	}

	public void setSendingPortUdp(int sendingPortUdp) {
		this.sendingPortUdp = sendingPortUdp;
	}



}
