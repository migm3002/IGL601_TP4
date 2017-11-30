package ca.udes.tp.server.runnable;


import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.ResponseSender;
import ca.udes.tp.server.Server;

/**
 * A thread from the thread pool, which is started with a new request
 * and finish his task after sending its response to the ResponseSender.
 */
public class UpdateScoreRequestThread implements Runnable{

	private Message request;
	private ResponseSender responseSender;
	private Server server;
	

	public UpdateScoreRequestThread(Message request, Server server) {
		this.request = request;
		this.responseSender=server.getResponseSender();
		this.server=server;
	}

	public void run() {
		//If the message is actually a request
		if(request!=null && request.isRequest()) {
			System.out.println("Handling :" + Thread.currentThread().getName());

			Message answer;
			Method method = request.getMethod();
			switch(method) {
			//If it's an updateScore request
			case updateScore: 
				ListeDesMatchs listeMatchs = server.getCurrentListeDesMatchs();
				//New response is made.
				answer= new Message(false, request.getRequestNumber(), method, listeMatchs,
						request.getClientAddress(),request.getClientPort());

				//Response is sent to the ResponseSender
				if(responseSender.sendUdpResponse(answer)) {
					System.out.println("UDP Answer to request succesfully sent.");
				}else {
					System.out.println("Error while sending of answer to request.");
				}
				break;
			case bet:
				System.out.println("Error : bet request sent to an updateScore thread !");
				break;
			default:
				System.out.println("Error : unknown method !");
				break;
			}
		}else {
			System.out.println("Error in UpdateScoreRequestThread while updating listeDesMatchs");
		}

	}



}
