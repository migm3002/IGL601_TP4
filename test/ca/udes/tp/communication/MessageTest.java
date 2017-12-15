package ca.udes.tp.communication;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import ca.udes.tp.communication.Message.Method;

public class MessageTest {
	private boolean isRequest=true;
	private int requestNumber = 149;
	private Method method = Method.bet;
	private JSONObject jsonArgument = new JSONObject();
	private String clientAddress = "127.0.0.1";
	private int clientPort = 12000;
	
	@Test
	public void messageConstructorTest(){
		Message message = new Message(isRequest, requestNumber, method, jsonArgument, clientAddress, clientPort);
	
		Assert.assertEquals(isRequest, message.isRequest());
		Assert.assertEquals(requestNumber, message.getRequestNumber());
		Assert.assertEquals(method, message.getMethod());
		Assert.assertEquals(jsonArgument, message.getArgument());
		Assert.assertEquals(clientAddress, message.getClientAddress());
		Assert.assertEquals(clientPort, message.getClientPort());
	}

	@Test
	public void messageGettersSettersTest() {
		JSONArray newArgument = new JSONArray();
		Message message = new Message(isRequest, requestNumber, method, jsonArgument, clientAddress, clientPort);
		
		message.setRequest(false);
		message.setRequestNumber(161);
		message.setMethod(Method.getEarnings);
		message.setArgument(newArgument);
		message.setClientAddress("192.168.4.16");
		message.setClientPort(13000);
		
		Assert.assertEquals(false, message.isRequest());
		Assert.assertEquals(161, message.getRequestNumber());
		Assert.assertEquals(Method.getEarnings, message.getMethod());
		Assert.assertEquals(newArgument, message.getArgument());
		Assert.assertEquals("192.168.4.16", message.getClientAddress());
		Assert.assertEquals(13000, message.getClientPort());
	}
}
