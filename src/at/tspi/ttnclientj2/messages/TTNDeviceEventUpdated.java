package at.tspi.ttnclientj2.messages;

// <AppID>/devices/<DevID>/events/update

public class TTNDeviceEventUpdated extends TTNDeviceEvent {

	public TTNDeviceEventUpdated() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventUpdated(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Device updated";
	}
}
