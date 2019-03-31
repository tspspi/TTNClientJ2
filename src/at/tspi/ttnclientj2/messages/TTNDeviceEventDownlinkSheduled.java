package at.tspi.ttnclientj2.messages;

public class TTNDeviceEventDownlinkSheduled extends TTNDeviceEvent {

	public TTNDeviceEventDownlinkSheduled() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventDownlinkSheduled(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Downlink sheduled";
	}
}
