package integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.Server;
import ca.udes.tp.server.runnable.ChronoUpdater;
import ca.udes.tp.server.runnable.EventsGenerator;
import ca.udes.tp.server.runnable.EventsUpdater;


public class TestIntegrationGenEvents {

	private Server serverTest;
	private EventsGenerator eventsGen;
	private EventsUpdater eventsUp;
	private ChronoUpdater chronoUp;
	private ListeDesMatchs listeMatchs;

	
	@Before
	public void setUp(){
		//Initialisation
		DatabaseManager dbManagerTest;
		ListeDesMatchs lm = new ListeDesMatchs();
		dbManagerTest = EasyMock.createMock(DatabaseManager.class);
		serverTest = EasyMock.createMock(Server.class);
		
		eventsGen = new EventsGenerator();
		
		EasyMock.expect(serverTest.getDbManager()).andStubReturn(dbManagerTest);
		EasyMock.expect(serverTest.getEventsGenerator()).andStubReturn(eventsGen);
		EasyMock.expect(serverTest.getCurrentListeDesMatchs()).andStubReturn(lm);
		EasyMock.replay(serverTest);
		
		eventsUp = new EventsUpdater(serverTest);
		chronoUp = new ChronoUpdater(serverTest);
		
		listeMatchs = new ListeDesMatchs();
		
	}

	@After
	public void tearDown(){
		
	}

	@Test
	public void testRun(){
		Thread eventsGeneratorThread = new Thread(eventsGen);
		eventsGeneratorThread.start();
		Thread eventsUpdaterThread = new Thread(eventsUp);		
		eventsUpdaterThread.start();
		Thread chronoUpdaterThread = new Thread(chronoUp);
		chronoUpdaterThread.start();
		
		try {
			Thread.sleep(10000);
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<10; i++) {
			assertFalse("these two matchs must be different", eventsUp.getCurrentListeDesMatchs().get(i).equals(listeMatchs.get(i)));
			if(eventsUp.getCurrentListeDesMatchs().get(i).isEnded()) {
				assertTrue("these two chronos must be different", eventsUp.getCurrentListeDesMatchs().get(i).getChronometre().equals(listeMatchs.get(i).getChronometre()));			

			}else {
				assertFalse("these two chronos must be different", eventsUp.getCurrentListeDesMatchs().get(i).getChronometre().equals(listeMatchs.get(i).getChronometre()));			
			}
		}

	}
}
