package ca.udes.tp.server.runnable;


import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.server.Server;

/**
 * Updates the chronometre object of current matches every 30 seconds.
 *
 */
public class ChronoUpdater implements Runnable{

	private Server server;
	private EventsGenerator eventsGenerator;

	public ChronoUpdater(Server server) {
		this.server=server;
		this.eventsGenerator=server.getEventsGenerator();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(30000);                 //Wait 30 seconds before updating chronometre again.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			
			ListeDesMatchs currentListeDesMatchs = getServer().getCurrentListeDesMatchs();
			for (Match match : currentListeDesMatchs.getListeMatchs()) {
				String chronometre = match.getChronometre();
				if(!match.isEnded()) {
					match.setChronometre(add30Secondes(chronometre));
				}
			}
			
		}
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

	/**
	 * Add 30 seconds to the given String chronometre.
	 * @param String : oldChronometre
	 * @return String : newChronometre
	 */
	private String add30Secondes(String chronometre) {
		int indexOfH = chronometre.indexOf("h");
		int indexOfM = chronometre.indexOf("m");
		int indexOfS = chronometre.indexOf("s");

		int hours = Integer.parseInt(chronometre.substring(0, indexOfH));
		int minutes = Integer.parseInt(chronometre.substring(indexOfH+1, indexOfM));
		int seconds = Integer.parseInt(chronometre.substring(indexOfM+1, indexOfS));

		int newHours = hours;
		int newMinutes = minutes;
		int newSeconds = seconds;

		newSeconds+=30;
		if(newSeconds>=60) {
			newSeconds-=60;
			newMinutes+=1;
			if(newMinutes>=60) {
				newMinutes-=60;
				newHours+=1;
			}
		}

		return newHours+"h"+newMinutes+"m"+newSeconds+"s";

	}

}
