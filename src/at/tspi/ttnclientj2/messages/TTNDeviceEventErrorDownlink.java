package at.tspi.ttnclientj2.messages;

public class TTNDeviceEventErrorDownlink extends TTNDeviceEvent {

	public TTNDeviceEventErrorDownlink() {
		// TODO Auto-generated constructor stub
	}
	public TTNDeviceEventErrorDownlink(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Downlink error";
	}
}
