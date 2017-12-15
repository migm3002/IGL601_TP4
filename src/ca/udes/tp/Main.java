package ca.udes.tp;

import ca.udes.tp.client.jarclient.view.TournoiTennisFrame;
import ca.udes.tp.server.Server;


public class Main {

	/**
	 * Main function : run to launch the java server.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the tennis app!");
		// TODO Auto-generated method stub
		try {
			Server server = new Server();
			server.run();

			Thread.sleep(1000);

			new TournoiTennisFrame();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
