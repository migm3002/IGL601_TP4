package ca.udes.tp.server.runnable;


import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.server.Server;

/**
 * Updates matches from currentListeDesMatchs, with events from EventsGenerator.
 *
 */
public class EventsUpdater implements Runnable{

	private Server server;
	private EventsGenerator eventsGenerator;		//EventGenerator, which generates events for EventUpdater.
	private ListeDesMatchs currentListeDesMatchs;	//Current list of matches, updated by EventsUpdater.
	private DatabaseManager dbManager;				//Database manager, which updates winner of an ended match given by EventUpdater.

	public static final String STRING_VALUE_FOR_POINT_ADDING = "+";			//String value of "adding points to score" events in event file.
	public static final String STRING_VALUE_FOR_CONTESTATION_LOSS = "C";	//String value for "decreasing contestation point" events in event file.

	public EventsUpdater(Server server) {
		this.server=server;
		this.eventsGenerator=server.getEventsGenerator();
		this.currentListeDesMatchs = server.getCurrentListeDesMatchs();
		this.dbManager = server.getDbManager();
	}

	public void run() {

		while(true) {
			try {
				String event = eventsGenerator.getEventQueue().take();
				if(isAddPointEvent(event)) {
					addPoint(event);
				}else if(isContestationEvent(event)) {
					decreaseContestation(event);
				}else {
					System.out.println("Error : event syntax incorrect");
				}

			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return true if given String is a "adding points to score" event.
	 * @param String : event
	 * @return boolean
	 */
	public boolean isAddPointEvent(String event) {
		boolean isAddPointEvent=false;
		if(event.substring(0,1).equals(STRING_VALUE_FOR_POINT_ADDING)) {
			isAddPointEvent = true;
		}
		return isAddPointEvent;
	}

	/**
	 * Return true if given String is a "decreasing contestation point" event.
	 * @param String : event
	 * @return boolean
	 */
	public boolean isContestationEvent(String event) {
		boolean isContestationEvent=false;
		if(event.substring(0,1).equals(STRING_VALUE_FOR_CONTESTATION_LOSS)){
			isContestationEvent=true;
		}
		return isContestationEvent;
	}

	/**
	 * Add a point to the the player given by event, in a match given by event.
	 * If this point ends the match, notify the DatabaseManager, which will update 
	 * the winner value of the match.
	 * 
	 * @param String : event
	 */
	private void addPoint(String event) {
		int indexOfM = event.indexOf("M");
		int indexOfP = event.indexOf("P");
		int indexOfDot = event.indexOf(".");
		int indexOfE = event.indexOf("E");
		boolean endOfMatch=false;
		if(indexOfE!=-1) {
			endOfMatch=true;
		}
		int idMatch = Integer.parseInt(event.substring(indexOfM+1, indexOfP));
		int playerNumber = Integer.parseInt(event.substring(indexOfP+1, indexOfDot));

		Match match = getCurrentListeDesMatchs().getMatchWithId(idMatch);
		match.addPoint(playerNumber, endOfMatch);

		if(endOfMatch) {
			System.out.println("End of match "+match.getId());
			try {
				getDbManager().getQueueInput().put(match);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Decrease contestation point of player given by event, in match given by event.
	 * 
	 * @param String : event
	 */
	private void decreaseContestation(String event) {
		int indexOfM = event.indexOf("M");
		int indexOfP = event.indexOf("P");
		int indexOfDot = event.indexOf(".");

		int idMatch = Integer.parseInt(event.substring(indexOfM+1, indexOfP));
		int playerNumber = Integer.parseInt(event.substring(indexOfP+1, indexOfDot));

		Match match = getCurrentListeDesMatchs().getMatchWithId(idMatch);
		match.decreaseContestationPointFor(playerNumber);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public EventsGenerator getEventsGenerator() {
		return eventsGenerator;
	}

	public void setEventsGenerator(EventsGenerator eventsGenerator) {
		this.eventsGenerator = eventsGenerator;
	}


	public ListeDesMatchs getCurrentListeDesMatchs() {
		return currentListeDesMatchs;
	}

	public void setCurrentListeDesMatchs(ListeDesMatchs currentListeDesMatchs) {
		this.currentListeDesMatchs = currentListeDesMatchs;
	}

	public DatabaseManager getDbManager() {
		return dbManager;
	}




}
