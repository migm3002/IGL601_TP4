package ca.udes.tp;

import ca.udes.tp.client.view.TournoiTennisFrame;
import ca.udes.tp.server.Server;

public class Main {

	/**
	 * Main function : run to launch the java server.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Server server = new Server();
			server.run();

			Thread.sleep(2000);

			new TournoiTennisFrame();
		}catch(InterruptedException e) {

		}
	}

}
