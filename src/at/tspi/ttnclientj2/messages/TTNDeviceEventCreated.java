package at.tspi.ttnclientj2.messages;

// <AppID>/devices/<DevID>/events/create

public class TTNDeviceEventCreated extends TTNDeviceEvent {

	public TTNDeviceEventCreated() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventCreated(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Device created";
	}
}
