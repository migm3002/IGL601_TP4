package ca.udes.tp.server.runnable;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.server.ResponseSender;
import ca.udes.tp.server.Server;

public class RequestListenerUdpTest {

	private ExecutorService threadPoolTest;
	private Server serverTest;
	private ResponseSender responseSenderTest;
	private int listeningPortTest;
	
	@Before
	public void setUp() {
		threadPoolTest = Executors.newFixedThreadPool(Server.THREAD_POOL_SIZE);
		serverTest = EasyMock.createMock(Server.class);
		responseSenderTest = EasyMock.createMock(ResponseSender.class);
		listeningPortTest = Server.DEFAULT_LISTENING_PORT_UDP;
		
		EasyMock.expect(serverTest.getResponseSender()).andReturn(responseSenderTest);
		EasyMock.expect(serverTest.getThreadPool()).andReturn(threadPoolTest);
		EasyMock.replay(serverTest);
		
	}
	
	@Test
	public void testConstructor1() {
		RequestListenerUdp listenerUdpTest = new RequestListenerUdp(listeningPortTest, serverTest);
		assertTrue("ListenerUdp server is not the one given in parameter",listenerUdpTest.getServer()==serverTest);
		EasyMock.reset(serverTest);
	}
	
}
