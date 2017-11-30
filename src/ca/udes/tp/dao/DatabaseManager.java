package ca.udes.tp.dao;

import java.util.concurrent.ArrayBlockingQueue;

import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Pari;
import ca.udes.tp.object.Pari.Status;
import ca.udes.tp.server.Server;


public class DatabaseManager implements Runnable{

	public static final int QUEUE_INPUT_CAPACITY = 50;  	//Queue that receives inputs from RequestListenerTcp and EventUpdater
	public static final int QUEUE_OUTPUT_CAPACITY = 25;		//Queue used to send responses to RequestListenerTcp

	private Server server;
	/**
	 * ArrayBlockingQueues are thread-safe, according to the documentation.
	 * The fairness boolean in queue Input enables to avoid starvation between
	 * RequestListenerTcp and EventUpdater.
	 */
	private ArrayBlockingQueue<Object> queueInput; 			//Queue that receives inputs from RequestListenerTcp and EventUpdater
	private ArrayBlockingQueue<Message> queueOutput;		//Queue used to send responses to RequestListenerTcp

	public DatabaseManager(Server server) {
		this.queueInput= new ArrayBlockingQueue<>(QUEUE_INPUT_CAPACITY, true);
		this.queueOutput = new ArrayBlockingQueue<>(QUEUE_OUTPUT_CAPACITY);
		this.server=server;
	}

	public void run() {
		try {
			while(true) {
				//Get an input from the Input Queue (blocking method while queue is empty)
				Object newInput = getQueueInput().take();
				
				//If input is a Message object
				if(newInput.getClass()==Message.class) {
					Message request = (Message) newInput;
					//If it is a bet method
					if(request.getMethod()==Method.bet) {
						insertBet((Message)newInput);
					//If it is a getEarnings method
					}else if(request.getMethod()==Method.getEarnings) {
						returnEarnings((Message) newInput);
					}
				//If input is a MatchObject
				}else if(newInput.getClass()==Match.class){
					updateMatchWinner((Match) newInput);
				}
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayBlockingQueue<Object> getQueueInput() {
		return this.queueInput;
	}

	public ArrayBlockingQueue<Message> getQueueOutput() {
		return queueOutput;
	}

	/**
	 * Insert the bet given in the request Message, in the database ;  
	 * and put a response for the client in the output queue.
	 * 
	 * @param Message : request
	 */
	private void insertBet(Message request) {

		Pari pari =(Pari) request.getArgument();
		ListeDesMatchs currentListeDesMatchs= server.getCurrentListeDesMatchs();
		Match selectedMatch = currentListeDesMatchs.getMatchWithId(pari.getIdMatch());
		//If intended match is currently playing
		if(selectedMatch!=null) {
			//If set1 of match is not over.
			if(!selectedMatch.isSet1Finished()) {
				//If bet is correctly inserted into the database
				if(PariDAO.placeBet(pari.getIdParieur(), pari.getIdMatch(), pari.getIdJoueur(),
						pari.getMontant())) {
					pari.setStatus(Status.commited);
				}else {

					System.out.println("Error with bet insert");
					pari.setStatus(Status.refused);
				}

				//We create the response with the new argument (status of Pari changed)
				Message response = new Message(false, request.getRequestNumber(), request.getMethod(),
						pari, request.getClientAddress(),request.getClientPort());

				//Put response into the response queue
				try {
					getQueueOutput().put(response);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Bet refused : set 1 of this match is over.");
			}
		}else {
			System.out.println("Bet refused : match is over or not started yet.");
		}
	}

	/**
	 * Update the winnerId of given match in the database.
	 * 
	 * @param Match : match
	 */
	private void updateMatchWinner(Match match) {
		if(match.getWinner()!=null) {
			int idMatch = match.getId();
			int idWinner = match.getWinner().getId();
			if(!MatchDAO.updateMatchWinner(idMatch, idWinner)) {
				System.out.println("Error : new winner has not been updated in the database");
			}

		}else {
			System.out.println("Error : no winner for this match");
		}
	}
	
	/**
	 * Get the earnings of a given bettor, on a given match (in given Message)
	 * and put a response for the client in the output queue.
	 * 
	 * @param Message : request
	 */
	private void returnEarnings(Message request) {
		Pari betInfo = (Pari) request.getArgument();
		int idParieur = betInfo.getIdParieur();
		int idMatch = betInfo.getIdMatch();
		double gain=-1;
		try {
			Double earnings = PariDAO.calculateEarnings(idMatch, idParieur);
			if(earnings!=null) {
				gain=earnings;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		Pari pari = new Pari(idParieur, idMatch, -1, gain, betInfo.getStatus());
		Message response = new Message(false, request.getRequestNumber(), request.getMethod(),
				pari, request.getClientAddress(),request.getClientPort());
		
		//Put response into the response queue
		try {
			getQueueOutput().put(response);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
