package ca.udes.tp.server.runnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		this.eventQueue = new ArrayBlockingQueue<>(50);
		this.eventCounter=0;
		initializeEventTable();
	}


	public void run() {
		try {
			//Wait before sending events
			Thread.sleep(3000);
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
		File eventFile = new File(PATH_TO_EVENT_TEXT_FILE);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader= new BufferedReader(new FileReader(eventFile));

			String eventString;
			int tableIndex=0;
			while((eventString = bufferedReader.readLine())!=null && tableIndex<EVENT_TABLE_CAPACITY) {
				if(!eventString.equals("")) {
					if(!eventString.substring(0, 1).equals("#")) {
						eventTable[tableIndex]=eventString;
						tableIndex++;
					}
				}else {
					System.out.println("Error : event file is not supposed to have a blank line");
				}
			}

			setEventTable(eventTable);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(bufferedReader!=null) {
				try {
					bufferedReader.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
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
