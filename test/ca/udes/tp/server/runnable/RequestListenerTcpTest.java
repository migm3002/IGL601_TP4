package ca.udes.tp.server.runnable;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.communication.Message;
import ca.udes.tp.dao.DatabaseManager;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Score;
import ca.udes.tp.server.Server;
import utility.TcpRequestSenderForTest;
import utility.TestUtility;

public class RequestListenerTcpTest {
	
	private Server serverTest;
	private DatabaseManager dbManagerTest;
	private int listeningPortTest;
	private ArrayBlockingQueue<Object> queueInputTest;
	private ArrayBlockingQueue<Message> queueOutputTest;
	
	@Before
	public void setUp() {
		
		Joueur j3 = new Joueur(11, "WILLIAMS", "Serena", 1, false);
		Joueur j4 = new Joueur(15, "SHARAPOVA", "Maria", 2, false);
		String[][] tabSet3 = {{"4","","6"},{"4","",""}};
		String[] tabJeu3 = {"",""};
		Score s3 = new Score(tabSet3, tabJeu3);
		Match matchTest = new Match(1, j3, j4, s3, "0:26:51", 3, 3);
		ArrayList<Match> arrayListMatch = new ArrayList<Match>();
		arrayListMatch.add(matchTest);
		ListeDesMatchs listMatchTest = new ListeDesMatchs(arrayListMatch);
		
		serverTest = EasyMock.createMock(Server.class);
		dbManagerTest = new DatabaseManager(serverTest);
		listeningPortTest = Server.DEFAULT_LISTENING_PORT_TCP+1;
		queueInputTest = new ArrayBlockingQueue<>(DatabaseManager.QUEUE_INPUT_CAPACITY, true);
		queueOutputTest = new ArrayBlockingQueue<>(DatabaseManager.QUEUE_OUTPUT_CAPACITY);
		
		EasyMock.expect(serverTest.getDbManager()).andStubReturn(dbManagerTest);
		EasyMock.expect(serverTest.getCurrentListeDesMatchs()).andStubReturn(listMatchTest);
		EasyMock.replay(serverTest);
				
		TestUtility.CleanUpDB();
		TestUtility.addInfosForTestPariDAO();
		
	}
	
	@After
	public void tearDown() {
		TestUtility.CleanUpDB();
	}
	
	@Test
	public void testConstructor1() {
		RequestListenerTcp listenerTcp = new RequestListenerTcp(listeningPortTest, serverTest);
		Assert.assertTrue(listenerTcp.getDatabaseManager()==serverTest.getDbManager());
		Assert.assertTrue(listenerTcp.getListeningPort()==listeningPortTest);
		EasyMock.reset(serverTest);
	}
	
	@Test
	public void testConstructor2() {
		RequestListenerTcp listenerTcp = new RequestListenerTcp(serverTest);
		Assert.assertTrue(listenerTcp.getDatabaseManager()==serverTest.getDbManager());
		Assert.assertTrue(listenerTcp.getListeningPort()==Server.DEFAULT_LISTENING_PORT_TCP);
		EasyMock.reset(serverTest);
	}
	
	@Test
	public void testRun() {
		RequestListenerTcp listenerTcp = new RequestListenerTcp(listeningPortTest, serverTest);
//		DatabaseManager dbManagerT = new DatabaseManager(serverTest);
		TcpRequestSenderForTest tcpSender = new TcpRequestSenderForTest();
		Thread tcpSenderThread = new Thread(tcpSender);
		Thread dbManagerThread = new Thread(dbManagerTest);
		Thread tcpListenerThread = new Thread(listenerTcp);
		tcpSenderThread.start();
		tcpListenerThread.start();
		dbManagerThread.start();
		
		try {
			Thread.sleep(4000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue(tcpSender.isSent());
		
		tcpListenerThread.interrupt();
		tcpSenderThread.interrupt();
		dbManagerThread.interrupt();
	}

}
