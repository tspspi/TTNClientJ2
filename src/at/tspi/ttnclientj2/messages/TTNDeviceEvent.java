package at.tspi.ttnclientj2.messages;

import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;

public abstract class TTNDeviceEvent extends TTNMessage {
	private String deviceId;
	private String appId;

	private boolean bImmutable = false;

	public TTNDeviceEvent() { }

	public TTNDeviceEvent(String deviceId, String appId) {
		this.deviceId = deviceId;
		this.appId = appId;
	}

	public String getDeviceId() { return this.deviceId; }
	public String getAppId() { return this.appId; }

	public TTNDeviceEvent setDeviceId(String deviceId) { if(this.bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.deviceId = deviceId; return this; }
	public TTNDeviceEvent setAppId(String appId) { if(this.bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.appId = appId; return this; }

	public TTNDeviceEvent setImmutable() { this.bImmutable = true; return this; }

	public String toString() { return "EVENT["+this.appId+":"+this.appId+"]"; }
}
