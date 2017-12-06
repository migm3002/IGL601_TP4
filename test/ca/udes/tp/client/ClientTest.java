package ca.udes.tp.client;

import ca.udes.tp.client.controller.RequestHandlerClient;
import ca.udes.tp.client.controller.RequestSender;
import ca.udes.tp.client.view.ListMatchPanel;
import ca.udes.tp.communication.Message;
import ca.udes.tp.communication.Message.Method;
import ca.udes.tp.server.Server;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientTest {
	
	private Server serverTest;
	private RequestHandlerClient requestHandlerClientTest;
	private RequestSender requestSenderTest;
	private ListMatchPanel listMatchPanelTest;
	
	private Message msg1 = new Message (true, 0, Method.updateScore, Message.EMPTY_ARGUMENT, requestHandlerClientTest.HOME_ADDRESS, requestHandlerClientTest.LISTENING_PORT_UDP_CLIENT);

	@Test
	public void testSendUdpRequest() {
		//objet a comparer ListMatchPanel attendu et celui obtenu ?
		//assertSame(Object expected, Object actual //RequestSender.sendUdpRequest(msg1));
	}
}
