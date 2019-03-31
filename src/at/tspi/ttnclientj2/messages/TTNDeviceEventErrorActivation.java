package at.tspi.ttnclientj2.messages;

public class TTNDeviceEventErrorActivation extends TTNDeviceEvent {

	public TTNDeviceEventErrorActivation() {
		// TODO Auto-generated constructor stub
	}
	public TTNDeviceEventErrorActivation(String deviceId, String appId) {
		super(deviceId, appId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Activation error";
	}
}
