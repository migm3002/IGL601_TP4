package ca.udes.tp.server.runnable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import ca.udes.tp.communication.Message;
import ca.udes.tp.server.ResponseSender;
import ca.udes.tp.server.Server;
import ca.udes.tp.tool.UdpUtility;

/**
 *Listens on UDP port for new requests and transfer them to
 *one of the threads in the threadPool.
 */
public class RequestListenerUdp implements Runnable {

	public static final int BUFFER_SIZE=8192;

	private int listeningPort;
	private ExecutorService threadPool;
	private ResponseSender responseSender;
	private Server server;
	//Indicate if the listener has received at least one request and handled it
	private boolean hasReceivedOnce = false;
	

	public RequestListenerUdp(int listeningPort, Server server) {
		this.listeningPort=listeningPort;
		this.responseSender=server.getResponseSender();
		this.threadPool = server.getThreadPool();
		this.server=server;
	}

	public RequestListenerUdp(Server server) {
		this.threadPool = server.getThreadPool();
		this.responseSender= server.getResponseSender();
		this.server=server;
		this.listeningPort=Server.DEFAULT_LISTENING_PORT_UDP;
	}

	public void run() {
		DatagramSocket requestSocket = null;

		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			requestSocket= new DatagramSocket(getListeningPort()); 
			
			while(true) {
				
				DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
				
				//Receive the client's packet. (Blocking method until a packet is received)
				requestSocket.receive(incomingPacket);
				Message request = UdpUtility.convertIntoMessage(incomingPacket);
				
				if(request.getClientAddress().equals("")) {
					request.setClientAddress(incomingPacket.getAddress().toString().substring(1));
				}
				
				if(threadPool!=null) {
					//Launch one of the inactive threads from threadPool, and give it the client's request message. 
					threadPool.execute(new UpdateScoreRequestThread(request, server));
					this.hasReceivedOnce = true;
				}else {
					System.out.println("Error : no thread pool assigned to this server");
				}
			}	
		}catch(SocketException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(requestSocket!=null) {
				requestSocket.close();
			}
		}
	}

	public int getListeningPort() {
		return this.listeningPort;
	}

	public ExecutorService getThreadPool() {
		return this.threadPool;
	}
	
	public ResponseSender getResponseSender() {
		return this.responseSender;
	}

	public Server getServer() {
		return this.server;
	}
	
	public boolean hasReceivedOnce() {
		return this.hasReceivedOnce;
	}
	

	

}
