package ca.udes.tp.server.runnable;


import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Simulate a real-time update of currently playing matches,
 * by using events taken from a text file.
 *
 */
public class EventsGenerator implements Runnable{

	public static final int EVENT_QUEUE_CAPACITY = 50;
	public static final int EVENT_TABLE_CAPACITY = 91;
	public static final String PATH_TO_EVENT_TEXT_FILE = 
			"events\\eventFile.txt";

	ArrayBlockingQueue<String> eventQueue;	// Queue where EventsGenerator puts events from eventTable, for EventsUpdater.
	private int eventCounter; 				// The next event index in event table.
	private String[] eventTable; 			// Table that contains every events extracted from event file.

	public EventsGenerator() {
		this.eventQueue = new ArrayBlockingQueue<String>(50);
		this.eventCounter=0;
		initializeEventTable();
	}


	public void run() {
		try {
			//Wait before sending events
			Thread.sleep(1000);
			while(true) {
				//Put an event into the event queue.
				getEventQueue().put(generateEvent());
				//Wait before next event.
				Thread.sleep(1000);
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get an event String from the event table.
	 * @return String
	 */
	public String generateEvent() {

		String event;
		if(eventCounter<EVENT_TABLE_CAPACITY) {
			event = getEventTable()[eventCounter];
			eventCounter++;
		}else {
			throw new Error("End of demonstration, event file has been fully read.");
		}

		return event;
	}

	/**
	 * Extract events from the event file, and put them into eventTable.
	 */
	private void initializeEventTable() {
		String[] eventTable = new String[EVENT_TABLE_CAPACITY];

		ArrayList<String> events = new ArrayList<String>();
		events.add("+M1P1.");
		events.add("+M9P2.");
		events.add("CM4P2.");
		events.add("+M4P1.");
		events.add("+M2P1.");
		events.add("+M6P1.");
		events.add("+M5P2.");
		events.add("+M8P1.");
		events.add("+M10P2.");
		events.add("+M3P1.");
		events.add("CM6P2.");
		events.add("+M6P1.");
		events.add("+M5P2.");
		events.add("+M1P2.");
		events.add("+M2P1.");
		events.add("+M9P1.");
		events.add("+M10P1.");
		events.add("+M3P1.");
		events.add("+M6P2.");
		events.add("+M5P2.");
		events.add("+M8P1.");
		events.add("+M1P2.");
		events.add("+M9P2.");
		events.add("+M5P2.");
		events.add("+M2P1.");
		events.add("+M4P2.");
		events.add("+M10P1.");
		events.add("+M5P2.E");
		events.add("+M8P2.");
		events.add("+M6P1.");
		events.add("+M3P1.");
		events.add("+M9P2.");
		events.add("+M1P1.");
		events.add("+M2P2.");
		events.add("+M9P2.");
		events.add("+M8P1.");
		events.add("+M3P1.");
		events.add("+M9P2.");
		events.add("+M4P2.");
		events.add("+M6P1.");
		events.add("+M9P1.");
		events.add("+M10P2.");
		events.add("+M1P1.");
		events.add("+M8P2.");
		events.add("+M6P1.");
		events.add("+M9P2.");
		events.add("+M2P1.");
		events.add("+M10P2.");
		events.add("+M4P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M3P2.");
		events.add("+M8P2.");
		events.add("+M9P2.");
		events.add("+M3P2.");
		events.add("+M1P1.");
		events.add("+M9P1.");
		events.add("+M2P2.");
		events.add("+M4P1.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M6P1.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M10P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.");
		events.add("+M9P2.E");
		
		if(events.size()==EVENT_TABLE_CAPACITY) {
			for(int i=0; i< events.size(); i++) {
				eventTable[i]=events.get(i);
			}
			setEventTable(eventTable);
		}else {
			throw new Error("Error : event table capacity is not equal to number of events");
		}
		
	}



	public ArrayBlockingQueue<String> getEventQueue() {
		return eventQueue;
	}

	public void setEventQueue(ArrayBlockingQueue<String> eventQueue) {
		this.eventQueue = eventQueue;
	}


	public int getEventCounter() {
		return eventCounter;
	}


	public void setEventCounter(int eventCounter) {
		this.eventCounter = eventCounter;
	}


	public String[] getEventTable() {
		return eventTable;
	}


	public void setEventTable(String[] eventTable) {
		this.eventTable = eventTable;
	}
}
