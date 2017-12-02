package ca.udes.tp.server;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.runnable.ChronoUpdater;
import ca.udes.tp.server.runnable.EventsGenerator;
import ca.udes.tp.server.runnable.EventsUpdater;
import ca.udes.tp.server.runnable.RequestListenerTcp;
import ca.udes.tp.server.runnable.RequestListenerUdp;

/**
 * The server object, which launch all the threads.
 */
public class Server implements Runnable{

	public static final int THREAD_POOL_SIZE = 4;  
	public static final int DEFAULT_LISTENING_PORT_UDP = 16000;
	public static final int DEFAULT_LISTENING_PORT_TCP = 16200;

	private ListeDesMatchs currentListeDesMatchs;	// The object that is returned after updateScore requests.

	private EventsGenerator eventsGenerator; 		// Generate events on matches (following a planned schedule for the demo).

	private EventsUpdater eventsUpdater;			// Updates the score and contestations objects of current matches
													// with events from EventsGenerator.
	
	private ChronoUpdater chronoUpdater;			// Updates the chronometre object of current matches every 30 seconds.

	private RequestListenerUdp listenerUdp;			// Listen for UDP requests and send them to the threadPool.
	
	private ExecutorService threadPool;				// Handles UDP requests from clients (implements Thread-per-request model)
													// and create responses sent by ResponseSender.
	
	private ResponseSender responseSender; 			// Contains methods in order to send an UDP packet to a client.

	private RequestListenerTcp listenerTcp;			// Listen for TCP requests and handle them thanks to DatabaseManager.
	
	private DatabaseManager dbManager; 				// Manages interactions with database (implements Thread-per-object model).





	/**
	 * Instantiate the server and all of its arguments (some of them are Runnable).
	 * 
	 */
	public Server() {
		
		this.currentListeDesMatchs = new ListeDesMatchs();
		this.dbManager = new DatabaseManager(this);
		this.eventsGenerator = new EventsGenerator();
		this.eventsUpdater = new EventsUpdater(this);
		this.chronoUpdater = new ChronoUpdater(this);

		this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		this.responseSender = new ResponseSender();
		this.listenerUdp = new RequestListenerUdp(this);

		this.listenerTcp = new RequestListenerTcp(this);
	}

	/**
	 * Put every Runnable arguments inside of Threads and start them.
	 */
	public void run() {
		// TODO Auto-generated method stub

		Thread chronoUpdaterThread = new Thread(chronoUpdater);
		Thread eventsGeneratorThread = new Thread(eventsGenerator);
		Thread eventsUpdaterThread = new Thread(eventsUpdater);
		Thread dbManagerThread = new Thread(dbManager);
		Thread udpThread = new Thread(listenerUdp);
		Thread tcpThread = new Thread(listenerTcp);

		dbManagerThread.start();
		udpThread.start();
		tcpThread.start();
		chronoUpdaterThread.start();
		eventsGeneratorThread.start();
		eventsUpdaterThread.start();
		System.out.println("Server is started");
	}

	public ResponseSender getResponseSender() {
		return responseSender;
	}

	public DatabaseManager getDbManager() {
		return dbManager;
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public RequestListenerUdp getListenerUdp() {
		return listenerUdp;
	}

	public RequestListenerTcp getListenerTcp() {
		return listenerTcp;
	}

	public EventsGenerator getEventsGenerator() {
		return eventsGenerator;
	}

	public ListeDesMatchs getCurrentListeDesMatchs() {
		return currentListeDesMatchs;
	}



}
