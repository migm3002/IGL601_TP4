package ca.udes.tp.communication;


public class Message {

	public static final Object EMPTY_ARGUMENT = null; 

	private boolean request;
	private int requestNumber;
	private Method method;
	private Object argument;
	private String clientAddress;
	private int clientPort;

	public enum Method{
		updateScore,
		bet,
		getEarnings

	}

	public Message(boolean request, int requestNumber, Method method, Object argument,
			String clientAddress, int clientPort) {
		this.request=request;
		this.requestNumber=requestNumber;
		this.method=method;
		this.argument=argument;
		this.clientAddress=clientAddress;
		this.clientPort=clientPort;
	}


	public boolean isRequest() {
		return request;
	}

	public void setRequest(boolean request) {
		this.request = request;
	}

	public int getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(int requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getArgument() {
		return argument;
	}

	public void setArgument(Object argument) {
		this.argument = argument;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}


}
