package ca.udes.tp.server.runnable;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.communication.Message;
import ca.udes.tp.object.Joueur;
import ca.udes.tp.object.ListeDesMatchs;
import ca.udes.tp.object.Match;
import ca.udes.tp.object.Score;
import ca.udes.tp.server.ResponseSender;
import ca.udes.tp.server.Server;
import utility.UdpRequestSenderForTest;

public class RequestListenerUdpTest {

	private ExecutorService threadPoolTest;
	private Server serverTest;
	private ResponseSender responseSenderTest;
	private int listeningPortTest;

	private UpdateScoreRequestThread updateScoreRequestThreadTest;

	@Before
	public void setUp() {
		Joueur j3 = new Joueur(3, "WILLIAMS", "Serena", 1, false);
		Joueur j4 = new Joueur(4, "SHARAPOVA", "Maria", 2, false);
		String[][] tabSet3 = {{"6","2","6"},{"4","6","3"}};
		String[] tabJeu3 = {"",""};
		Score s3 = new Score(tabSet3, tabJeu3);
		Match matchTest = new Match(3, j3, j4, s3, "2:06:51", 3, 3);
		ArrayList<Match> arrayListMatch = new ArrayList<Match>();
		arrayListMatch.add(matchTest);
		ListeDesMatchs listMatchTest = new ListeDesMatchs(arrayListMatch);

		threadPoolTest = Executors.newFixedThreadPool(Server.THREAD_POOL_SIZE);
		serverTest = EasyMock.createMock(Server.class);
		responseSenderTest = EasyMock.createMock(ResponseSender.class);
		listeningPortTest = Server.DEFAULT_LISTENING_PORT_UDP+1;

		EasyMock.expect(serverTest.getResponseSender()).andStubReturn(responseSenderTest);
		EasyMock.expect(serverTest.getThreadPool()).andStubReturn(threadPoolTest);
		EasyMock.expect(serverTest.getCurrentListeDesMatchs()).andStubReturn(listMatchTest);
		EasyMock.replay(serverTest);

		EasyMock.expect(responseSenderTest.sendUdpResponse(EasyMock.isA(Message.class))).andStubReturn(true);
		EasyMock.replay(responseSenderTest);
	}

	@Test
	public void testConstructor1() {
		RequestListenerUdp listenerUdpTest = new RequestListenerUdp(listeningPortTest, serverTest);
		assertTrue("ListenerUdp server is not the one given in parameter",listenerUdpTest.getServer()==serverTest);
		assertTrue("ListenerUdp threadPool is not the one given in parameter", listenerUdpTest.getThreadPool()==serverTest.getThreadPool());
		assertTrue("ListenerUdp responseSender is not the one given in parameter", listenerUdpTest.getResponseSender()==serverTest.getResponseSender());
		assertTrue("ListenerUdp listening port is not the one given in parameter", listenerUdpTest.getListeningPort() == listeningPortTest);
		EasyMock.reset(serverTest);
	}

	@Test
	public void testConstructor2() {
		RequestListenerUdp listenerUdpTest = new RequestListenerUdp(serverTest);
		assertTrue("ListenerUdp server is not the one given in parameter",listenerUdpTest.getServer()==serverTest);
		assertTrue("ListenerUdp threadPool is not the one given in parameter", listenerUdpTest.getThreadPool()==serverTest.getThreadPool());
		assertTrue("ListenerUdp responseSender is not the one given in parameter", listenerUdpTest.getResponseSender()==serverTest.getResponseSender());
		assertTrue("ListenerUdp listening port is not the default one", listenerUdpTest.getListeningPort() == Server.DEFAULT_LISTENING_PORT_UDP);
		EasyMock.reset(serverTest);
	}

	@Test
	public void testRunAndReceiveRequest() {
		RequestListenerUdp listenerUdpTest = new RequestListenerUdp(listeningPortTest, serverTest);
		UdpRequestSenderForTest udpSender = new UdpRequestSenderForTest();
		Thread udpSenderThread = new Thread(udpSender);
		udpSenderThread.start();
		Thread udpListenerThread = new Thread(listenerUdpTest);
		udpListenerThread.start();
		System.out.println("DEBUG : listening on port"+ listeningPortTest);
		try {
			Thread.sleep(1500);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("ListenerUdp has not received one of the test requests", listenerUdpTest.hasReceivedOnce());
	}

}
