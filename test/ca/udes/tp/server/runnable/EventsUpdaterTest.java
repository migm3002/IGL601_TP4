package ca.udes.tp.server.runnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.server.Server;

public class EventsUpdaterTest {
	
	private Server serverTest;
	private EventsGenerator eventsGeneratorTest;
	private ListeDesMatchs listeMatchs;
	private DatabaseManager dbManagerTest;
	
	@Before
	public void setUp() {
		ArrayBlockingQueue<String> queueEvent = new ArrayBlockingQueue<String>(50);
		listeMatchs = new ListeDesMatchs();
		serverTest = EasyMock.createMock(Server.class);
		eventsGeneratorTest = EasyMock.createMock(EventsGenerator.class);
		dbManagerTest = EasyMock.createMock(DatabaseManager.class);
		
		EasyMock.expect(eventsGeneratorTest.getEventQueue()).andStubReturn(queueEvent);
		EasyMock.replay(eventsGeneratorTest);
		
		EasyMock.expect(serverTest.getCurrentListeDesMatchs()).andStubReturn(listeMatchs);
		EasyMock.expect(serverTest.getDbManager()).andStubReturn(dbManagerTest);
		EasyMock.expect(serverTest.getEventsGenerator()).andStubReturn(eventsGeneratorTest);
		EasyMock.replay(serverTest);
		
		
		
	}
	
	@After
	public void takeDown() {
		
	}
	
	@Test
	public void testConstructor() {
		EventsUpdater eventsUpdater = new EventsUpdater(serverTest);
		assertEquals(eventsUpdater.getServer(), serverTest);
		assertEquals(eventsUpdater.getDbManager(), serverTest.getDbManager());
		assertEquals(eventsUpdater.getEventsGenerator(), serverTest.getEventsGenerator());
		assertEquals(eventsUpdater.getCurrentListeDesMatchs(), serverTest.getCurrentListeDesMatchs());
	}
	
	@Test
	public void testIsAddPointEvent() {
		EventsUpdater eventsUpdater = new EventsUpdater(serverTest);
		String goodEvent = "+M8P1";
		String badEvent = "CM6P2";
		
		assertTrue("This could be the good event", eventsUpdater.isAddPointEvent(goodEvent));
		assertFalse("This could be the bad event", eventsUpdater.isAddPointEvent(badEvent));
	}
	
	@Test
	public void testIsContestationEvent() {
		EventsUpdater eventsUpdater = new EventsUpdater(serverTest);
		String goodEvent = "CM4P2";
		String badEvent = "+M3P1";
		
		assertTrue("This could be the good event", eventsUpdater.isContestationEvent(goodEvent));
		assertFalse("This could be the bad event", eventsUpdater.isContestationEvent(badEvent));
	}

	@Test
	public void testRun() {
		EventsUpdater eventsUpdater = new EventsUpdater(serverTest);
		Thread eventsUpThread = new Thread(eventsUpdater);
		eventsUpThread.start();
		
		try {
			Thread.sleep(20000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		assertFalse("Two listeMatchs must be different", eventsUpdater.getCurrentListeDesMatchs().equals(listeMatchs));
		
	}
	
}
