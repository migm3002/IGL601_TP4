package ca.udes.tp.tool;

import static org.junit.Assert.assertTrue;

import java.net.DatagramPacket;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.udes.tp.communication.Message;

public class TestUdpUtility {
	private byte[] buffer;
	private JSONObject jsonMessage;
	
	@Before
	public void setUp(){
		jsonMessage = new JSONObject("{\r\n" + 
				"	\"request\" : true,\r\n" + 
				"	\"requestNumber\" : 1456,\r\n" + 
				"	\"method\" : \"updateScore\",\r\n" + 
				"	\"clientAddress\" : \"132.212.240.124\",\r\n" + 
				"	\"clientPort\" : 16000\r\n" + 
				"}");
		buffer = UdpUtility.convertToBytes(jsonMessage.toString());
	}

	@After
	public void tearDown(){
		
	}
	
	
	@Test
	public void testConvertIntoMessage() {
		DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);
		assertTrue("ConvertIntoMessage not a message", UdpUtility.convertIntoMessage(dgram)instanceof Message);
	}
	
	@Test
	public void testConvertFromBytes() {
		assertTrue("ConvertFromBytes not an Object", UdpUtility.convertFromBytes(buffer) instanceof Object);
	}
	
	@Test
	public void testConvertToBytes() {
		assertTrue("ConvertToBytes not bytes", UdpUtility.convertToBytes(jsonMessage.toString())instanceof byte[]);
	}
}
