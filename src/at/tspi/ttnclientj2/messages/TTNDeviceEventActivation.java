package at.tspi.ttnclientj2.messages;

import at.tspi.tjson.JSONObject;
import at.tspi.tjson.JSONParser;
import at.tspi.tjson.JSONParserException;
import at.tspi.tjson.JSONString;
import at.tspi.tjson.JSONValue;
import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;
import at.tspi.ttnclientj2.exceptions.TTNMessageParsingException;

// Topic: <AppID>/devices/<DevID>/events/activations

public class TTNDeviceEventActivation extends TTNDeviceEvent {
	String appEui;
	String deviceEui;
	String deviceAddress;

	UplinkMetadata metadata;

	private boolean bImmutable = false;
	
	public TTNDeviceEventActivation() { super(); }
	public TTNDeviceEventActivation(String deviceId, String appId) { super(deviceId, appId); }

	public String getAppEui() 												{ return appEui; }
	public String getDeviceEui() 											{ return deviceEui; }
	public String getDeviceAddress() 										{ return deviceAddress; }

	public TTNDeviceEventActivation setAppEui(String appEui) 				{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.appEui = appEui; return this; }
	public TTNDeviceEventActivation setDeviceEui(String deviceEui) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.deviceEui = deviceEui; return this; }
	public TTNDeviceEventActivation setDeviceAddress(String deviceAddress) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.deviceAddress = deviceAddress; return this; }

	public TTNDeviceEventActivation setFromJSON(String json) throws TTNMessageParsingException {
		if(bImmutable) {
			throw new TTNAccessDeniedException("Object is immutable");
		}

		try {
			JSONValue parsedMessage = JSONParser.parseString(json);

			if(!(parsedMessage instanceof JSONObject)) { throw new TTNMessageParsingException("Activation message did not contain JSON object"); }

			JSONObject msgObj = (JSONObject)parsedMessage;
			{
				JSONValue vEui = msgObj.get("app_eui");
				if(vEui != null) {
					if(!(vEui instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
					this.appEui = ((JSONString)vEui).get();
				} else {
					this.appEui = null;
				}
			}
			{
				JSONValue vEui = msgObj.get("dev_eui");
				if(vEui != null) {
					if(!(vEui instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
					this.deviceEui = ((JSONString)vEui).get();
				} else {
					this.deviceEui = null;
				}
			}
			{
				JSONValue vAddr = msgObj.get("dev_addr");
				if(vAddr != null) {
					if(!(vAddr instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
					this.deviceAddress = ((JSONString)vAddr).get();
				} else {
					this.deviceAddress = null;
				}
			}
			{
				JSONValue v = msgObj.get("metadata");
				if(v != null) {
					if(!(v instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected data type"); }
					this.metadata = new UplinkMetadata();
					this.metadata.setFromJSON((JSONObject)v);
				} else {
					this.metadata = null;
				}
			}
		} catch(JSONParserException e) {
			throw new TTNMessageParsingException(e);
		}

		return this;
	}

	public TTNDeviceEventActivation setImmutable() { super.setImmutable(); this.bImmutable = true; return this; }

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Device activation\n"
				+ "\tDevice EUI: "+this.deviceEui+"\n"
				+ "\tDevice address: "+this.deviceAddress+"\n"
				+ (this.metadata != null ? this.metadata.toString() : "");
	}
}
