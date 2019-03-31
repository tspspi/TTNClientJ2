package at.tspi.ttnclientj2.messages;

// Topic: <AppID>/devices/<DevID>/down

import java.util.Base64;

import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;

public class TTNMessageDownlink extends TTNMessage {
	private long port;
	private boolean confirmed;
	private byte[] rawPayload;

	private String appId;
	private String devId;
	
	private boolean bImmutable = false;

	public TTNMessageDownlink(String appId, String devId) { this.appId = appId; this.devId = devId; }

	public long getPort() 										{ return port; }
	public boolean isConfirmed() 								{ return confirmed; }
	public byte[] getRawPayload() 								{ return rawPayload; }
	public String getAppId() 									{ return appId; }
	public String getDevId() 									{ return devId; }

	public TTNMessageDownlink setPort(long port) 				{ if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.port = port; return this; }
	public TTNMessageDownlink setConfirmed(boolean confirmed) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.confirmed = confirmed; return this; }
	public TTNMessageDownlink setRawPayload(byte[] rawPayload) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.rawPayload = rawPayload; return this; }
	public TTNMessageDownlink setAppId(String appId) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.appId = appId; return this; }
	public TTNMessageDownlink setDevId(String devId) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.devId = devId; return this; }

	public TTNMessageDownlink setImmutable() { this.bImmutable = true; return this; }

	public String toString() {
		return "["+appId+":"+devId+"] DOWNLINK:\n"
				+ "\tport: "+this.port+"\n"
				+ "\tConfirmed: "+(this.confirmed ? "true" : "false")+"\n"
				+ "\tRaw Payload: "+new String(this.rawPayload)+"\n";
	}

	public String toJSON() {
		Base64.Encoder b64Enc = Base64.getEncoder();
		return "{\"port\":"+port+",\"confirmed\":"+(confirmed ? "true" : "false")+",\"payload_raw\":\""+b64Enc.encodeToString(this.rawPayload)+"\"}";
	}
}
