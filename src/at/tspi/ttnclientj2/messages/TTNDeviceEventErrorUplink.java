package at.tspi.ttnclientj2.messages;

public class TTNDeviceEventErrorUplink extends TTNDeviceEvent {

	public TTNDeviceEventErrorUplink() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventErrorUplink(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Uplink error";
	}

}
