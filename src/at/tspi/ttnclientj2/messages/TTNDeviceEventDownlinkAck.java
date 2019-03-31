package at.tspi.ttnclientj2.messages;

public class TTNDeviceEventDownlinkAck extends TTNDeviceEvent {

	public TTNDeviceEventDownlinkAck() {
		// TODO Auto-generated constructor stub
	}

	public TTNDeviceEventDownlinkAck(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Downlink ACK";
	}
}
