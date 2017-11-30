package ca.udes.tp.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import ca.udes.tp.communication.Message;

/**
 * Contains methods used to handle UDP requests.
 */
public final class UdpUtility {

	/**
	 * Convert a DatagramPacket into a Message. 
	 * @param DatagramPacket : datagramPacket
	 * @return Message : message
	 */
	public static Message convertIntoMessage(DatagramPacket datagramPacket) {
		Message message=null;
		byte[] data = datagramPacket.getData();

		try {
			String jsonString = String.valueOf(convertFromBytes(data));
			message = JsonUtility.convertJsonStringIntoMessage(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		return message;
	}

	/**
	 * Convert a byte array into an Object.
	 * @param byte[] : bytes
	 * @return Object : object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {

		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInput in = new ObjectInputStream(bis)) {
			Object object = in.readObject();
			
			return object;
		} 
	}
	
	/**
	 * Convert an Object into a byte array.
	 * @param Object : object
	 * @return byte[] bytes
	 * @throws IOException
	 */
	public static byte[] convertToBytes(Object object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			byte[] bytes = bos.toByteArray();
			
			return bytes;
		} 
	}
}
