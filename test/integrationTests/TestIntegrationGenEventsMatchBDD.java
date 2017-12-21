package integrationTests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

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
import utility.TestUtility;

public class TestIntegrationGenEventsMatchBDD {

	private Server serverTest;
	private EventsGenerator eventsGen;
	private EventsUpdater eventsUp;
	private ChronoUpdater chronoUp;
	private DatabaseManager dbManager;
	
	@Before
	public void setUp(){
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestIntegrationMatch();
		serverTest = EasyMock.createMock(Server.class);
		
		eventsGen = new EventsGenerator();
		
		EasyMock.expect(serverTest.getDbManager()).andStubReturn(dbManager);
		EasyMock.expect(serverTest.getEventsGenerator()).andStubReturn(eventsGen);
		EasyMock.expect(serverTest.getCurrentListeDesMatchs()).andStubReturn(new ListeDesMatchs());
		EasyMock.replay(serverTest);
		
		eventsUp = new EventsUpdater(serverTest);
		chronoUp = new ChronoUpdater(serverTest);
		dbManager = new DatabaseManager(serverTest);
		
	}

	@After
	public void tearDown(){
		TestUtility.CleanUpDB();
	}

	@Test
	public void testRun(){
		Thread eventsGeneratorThread = new Thread(eventsGen);
		eventsGeneratorThread.start();
		Thread eventsUpdaterThread = new Thread(eventsUp);		
		eventsUpdaterThread.start();
		Thread chronoUpdaterThread = new Thread(chronoUp);
		chronoUpdaterThread.start();
		Thread dbManagerThread =  new Thread(dbManager);
		dbManagerThread.start();
		
		try {
			Thread.sleep(10000);
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		int[] tabIdsMatchsEndedFromBDD = TestUtility.obtainMatchWithWinners();
		int[] tabIdsMatchs = new int[10];
		int indice = 0;
		for(int i=0; i<10; i++) {
			if(eventsUp.getCurrentListeDesMatchs().get(i).isEnded()) {
				tabIdsMatchs[indice] = eventsUp.getCurrentListeDesMatchs().get(i).getId();
				indice++;
			}
		}
		assertTrue("These two arrays of ids Matchs must be the same.", Arrays.equals(tabIdsMatchsEndedFromBDD,tabIdsMatchs));

	}
	
}
