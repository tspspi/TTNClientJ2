package at.tspi.ttnclientj2.client;

import java.util.ArrayList;
import java.util.List;

import at.tspi.ttnclientj2.exceptions.TTNConnectionFailedException;
import at.tspi.ttnclientj2.exceptions.TTNTransmissionError;
import at.tspi.ttnclientj2.messages.TTNMessage;
import at.tspi.ttnclientj2.messages.TTNMessageDownlink;

public abstract class TTNClient {
	public static final int STATE__UNKNOWN 		= 0;
	public static final int STATE__CONNECTING 	= 1;
	public static final int STATE__CONNECTED 	= 2;
	public static final int STATE__ERROR		= 3;
	public static final int STATE__CLOSED		= 4;

	private String region;
	private String appId;
	private String accessKey;
	private int port;
	private String clientId;

	private List<TTNMessageHandler> msgHandler;

	abstract public void connect() throws TTNConnectionFailedException;
	abstract public boolean isConnected();
	abstract public int getConnectionState();
	abstract public boolean send(TTNMessageDownlink msg) throws TTNTransmissionError;
	abstract public boolean send(TTNMessageDownlink msg, boolean retained) throws TTNTransmissionError;
	abstract public boolean send(TTNMessageDownlink msg, boolean retained, int qos) throws TTNTransmissionError;
	abstract public void close();

	public TTNClient(String region, String appId, String accessKey, int port, String clientId) {
		this.region = region;
		this.appId = appId;
		this.accessKey = accessKey;
		this.port = port;
		this.clientId = clientId;

		this.msgHandler = new ArrayList<TTNMessageHandler>();
	}

	public String getRegion() { return this.region; }
	public String getAppId() { return this.appId; }
	protected String getAccessKey() { return this.accessKey; }
	public int getPort() { return this.port; }
	public String getClientId() { return this.clientId; }

	protected void setClientId(String clientId) { this.clientId = clientId; }

	public void attachMessageHandler(TTNMessageHandler handler) {
		if(!msgHandler.contains(handler)) { msgHandler.add(handler); }
	}
	public void detachMessageHandler(TTNMessageHandler handler) {
		msgHandler.remove(handler);
	}

	protected boolean internalHandleMessage(TTNMessage msg) {
		boolean bHandeled = false;

		// TODO: Set message immutable before posting it to handlers
		for(TTNMessageHandler handler : this.msgHandler) {
			bHandeled = bHandeled | handler.handleTTNMessage(msg, this);
		}

		return bHandeled;
	}
}
