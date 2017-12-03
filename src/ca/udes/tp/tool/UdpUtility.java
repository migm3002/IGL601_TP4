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


		String jsonString = String.valueOf(convertFromBytes(data));
		message = JsonUtility.convertJsonStringIntoMessage(jsonString);


		return message;
	}

	/**
	 * Convert a byte array into an Object.
	 * @param byte[] : bytes
	 * @return Object : object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object convertFromBytes(byte[] bytes){
		ObjectInput in = null;
		Object object = null;
		try { 
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			in = new ObjectInputStream(bis);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(in!=null) {
					object = in.readObject();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return object;
	}

	/**
	 * Convert an Object into a byte array.
	 * @param Object : object
	 * @return byte[] bytes
	 * @throws IOException
	 */
	public static byte[] convertToBytes(Object object){
		ObjectOutput out = null;
		ByteArrayOutputStream bos = null;
		byte[] bytes = null;
		try { 
			bos = new ByteArrayOutputStream(); 
			out = new ObjectOutputStream(bos);
		}catch(IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null && bos!=null) {
					out.writeObject(object);
					bytes = bos.toByteArray();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		} 
		return bytes;
	}
}
