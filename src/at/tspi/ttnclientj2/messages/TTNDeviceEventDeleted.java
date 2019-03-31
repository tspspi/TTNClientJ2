package at.tspi.ttnclientj2.messages;

// <AppID>/devices/<DevID>/events/delete

public class TTNDeviceEventDeleted extends TTNDeviceEvent {

	public TTNDeviceEventDeleted() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventDeleted(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Device deleted";
	}
}
