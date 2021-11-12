package at.tspi.ttnclientj2.messages;

import org.apache.tomcat.util.codec.binary.Base64;

import at.tspi.tjson.JSONBool;
import at.tspi.tjson.JSONNumber;
import at.tspi.tjson.JSONObject;
import at.tspi.tjson.JSONParser;
import at.tspi.tjson.JSONParserException;
import at.tspi.tjson.JSONString;
import at.tspi.tjson.JSONValue;

// Topic: <AppID>/devices/<DevID>/up

import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;
import at.tspi.ttnclientj2.exceptions.TTNMessageParsingException;

public class TTNMessageUplink extends TTNMessage {
	String deviceId;				// Device ID (choosen when creating the device, not EUI)
	String appId;					// Application ID (choosen when creating the device, not EUI)
	String hardwareSerial;			// Hardware serial, LoRA: EUI
	long port;						// LoRA Port
	long counter;					// Frame counter
	boolean isRetry;				// Retry?
	boolean confirmed;				// Confirmed?
	byte[] payloadRaw;				// Base64 DEcoded payload
	UplinkMetadata metadata;		// Metadata about the message (gateways, channels, etc.)

	private boolean bImmutable = false;

	public TTNMessageUplink() { }

	public String getDeviceId() 										{ return deviceId; }
	public String getAppId() 											{ return appId; }
	public String getHardwareSerial() 									{ return hardwareSerial; }
	public long getPort() 												{ return port; }
	public long getCounter() 											{ return counter; }
	public boolean isRetry() 											{ return isRetry; }
	public boolean isConfirmed() 										{ return confirmed; }
	public byte[] getPayloadRaw() 										{ return payloadRaw; }
	public UplinkMetadata getMetadata() 								{ return metadata; }

	public TTNMessageUplink setDeviceId(String deviceId) 				{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.deviceId = deviceId; return this; }
	public TTNMessageUplink setAppId(String appId) 						{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.appId = appId; return this; }
	public TTNMessageUplink setHardwareSerial(String hardwareSerial) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.hardwareSerial = hardwareSerial; return this; }
	public TTNMessageUplink setPort(long port) 							{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.port = port; return this; }
	public TTNMessageUplink setCounter(long counter) 					{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.counter = counter; return this; }
	public TTNMessageUplink setRetry(boolean isRetry) 					{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.isRetry = isRetry; return this; }
	public TTNMessageUplink setConfirmed(boolean confirmed) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.confirmed = confirmed; return this; }
	public TTNMessageUplink setPayloadRaw(byte[] payloadRaw) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.payloadRaw = payloadRaw; return this; }
	public TTNMessageUplink setMetadata(UplinkMetadata metadata) 		{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.metadata = metadata; return this; }

	public TTNMessageUplink setFromJSON(String json) throws TTNMessageParsingException {
		if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); }

		try {
			System.out.println(json);
			JSONValue v = JSONParser.parseString(json);
			if(!(v instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected JSON data type"); }
			return setFromJSON((JSONObject)v);
		} catch(JSONParserException e) {
			throw new TTNMessageParsingException(e);
		}
	}
	public TTNMessageUplink setFromJSON(JSONObject msgObj) throws TTNMessageParsingException {
		if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); }

 		{
			JSONValue vEndDeviceIDs = msgObj.get("end_device_ids");
			if(vEndDeviceIDs != null) {
				if(!(vEndDeviceIDs instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected data type"); }
				JSONValue vDevId = ((JSONObject)vEndDeviceIDs).get("device_id");
				if(vDevId != null) {
					if(!(vDevId instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
					this.deviceId = ((JSONString)vDevId).get();
				}

				JSONValue vApplicationIds = ((JSONObject)vEndDeviceIDs).get("application_ids");
				if(vApplicationIds != null) {
					if(!(vApplicationIds instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected data type"); }
					vAppId = ((JSONObject)vApplicationIds).get("application_id");
					if(vAppId != null) {
						if(!(vAppId instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
						this.appId = ((JSONString)vAppId).get();
					}
				}
			}
		}
 		{
			JSONValue v = msgObj.get("hardware_serial");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.hardwareSerial = ((JSONString)v).get();
			} else {
				this.hardwareSerial = null;
			}
		}
 		{
			JSONValue v = msgObj.get("port");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.port = ((JSONNumber)v).getLong();
			} else {
				this.port = -1;
			}
		}
 		{
			JSONValue v = msgObj.get("counter");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.counter = ((JSONNumber)v).getLong();
			} else {
				this.counter = -1;
			}
		}
 		{
			JSONValue v = msgObj.get("is_retry");
			if(v != null) {
				if(!(v instanceof JSONBool)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.isRetry = ((JSONBool)v).get();
			} else {
				this.isRetry = false;
			}
		}
 		{
			JSONValue v = msgObj.get("confirmed");
			if(v != null) {
				if(!(v instanceof JSONBool)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.confirmed = ((JSONBool)v).get();
			} else {
				this.confirmed = false;
			}
		}
 		{
			JSONValue v = msgObj.get("payload_raw");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.payloadRaw = Base64.decodeBase64(((JSONString)v).get());
			} else {
				this.payloadRaw = null;
			}
		}
 		{
			JSONValue v = msgObj.get("metadata");
			if(v != null) {
				if(!(v instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.metadata = new UplinkMetadata();
				this.metadata.setFromJSON((JSONObject)v);
				this.metadata.setImmutable();
			} else {
				this.metadata = null;
			}
		}

		return this;
	}

	public TTNMessageUplink setImmutable() { this.bImmutable = true; if(this.metadata != null) { this.metadata.setImmutable(); } return this; }

	public String toString() {
		return "["+this.appId+":"+this.deviceId+"] Uplink message:\n"
				+ "\tHardware serial: "+this.hardwareSerial+"\n"
				+ "\tPort: "+this.port+"\n"
				+ "\tCounter: "+this.counter+"\n"
				+ "\tRetry: "+(this.isRetry ? "true" : "false")+"\n"
				+ "\tConfirmed: "+(this.confirmed ? "true" : "false")+"\n"
				+ (this.metadata != null ? ("\tMetadata:\n"+this.metadata) : "")
				+ "\tRaw Payload:\n"
				+ new String(this.payloadRaw)+"\n\n";
	}
}
