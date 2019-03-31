package at.tspi.ttnclientj2.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.tspi.tjson.JSONArray;
import at.tspi.tjson.JSONNumber;
import at.tspi.tjson.JSONObject;
import at.tspi.tjson.JSONParser;
import at.tspi.tjson.JSONParserException;
import at.tspi.tjson.JSONString;
import at.tspi.tjson.JSONValue;
import at.tspi.tjson.annotations.JSONSerializeObject;
import at.tspi.tjson.annotations.JSONSerializeValue;
import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;
import at.tspi.ttnclientj2.exceptions.TTNMessageParsingException;

@JSONSerializeObject
public class UplinkMetadata implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String MODULATION_LORA = "LORA";
	public static final String MODULATION_FSK = "FSK";

	@JSONSerializeValue(name = "airtime")
	long airtime; 					// Airtime in nanoseconds
	@JSONSerializeValue(name = "time")
	String time; 					// Time when message has been received by server (1970-01-01T00:00:00Z)
	@JSONSerializeValue(name = "frequency")
	double frequency; 				//Frequency at which the message was sent
	@JSONSerializeValue(name = "modulation")
	String modulation;				// Modulation that was used
	@JSONSerializeValue(name = "data_rate")
	String loraDataRate;			// Data rate string for LORA (Format like SF7BW125)
	@JSONSerializeValue(name = "bit_rate")
	long fskBitRate;				// Bitrate for FSK modes
	@JSONSerializeValue(name = "coding_rate")
	String codingRate;				// Coding rate (like 4/5)
	@JSONSerializeValue(name = "latitude")
	double locLatitude;				// Estimated latitude of device
	@JSONSerializeValue(name = "longitude")
	double locLongitude;			// Estimated longitude of device
	@JSONSerializeValue(name = "altitude")
	double locAltitude;				// Estimated altitude of device
	@JSONSerializeValue(name = "gateways")
	List<UplinkGateway> gateways;	// List of all gateway that received that message

	@JSONSerializeObject
	public class UplinkGateway implements Serializable {
		private static final long serialVersionUID = 1L;

		@JSONSerializeValue(name = "gtw_id")
		String gatewayId;		// Gateway ID
		@JSONSerializeValue(name = "timestamp")
		long timestamp;			// Timestamp
		@JSONSerializeValue(name = "time")
		String time;			// Time (left ouf when no relieable time source at gateway)
		@JSONSerializeValue(name = "channel")
		int channel;			// Channel that this message has been received on
		@JSONSerializeValue(name = "rssi")
		double rssi;			// RSSI of signal
		@JSONSerializeValue(name = "snr")
		double snr;				// SNR
		@JSONSerializeValue(name = "rf_chain")
		long rfChain;			// Which RF chain has been used
		@JSONSerializeValue(name = "latitude")
		double latitude;		// Location (optional)
		@JSONSerializeValue(name = "longitude")
		double longitude;		// Location (optional)
		@JSONSerializeValue(name = "altitude")
		double altitude;		// Location (optional)

		boolean bImmutable = false;

		public UplinkGateway() { };

		public String getGatewayId() { return gatewayId; }
		public long getTimestamp() { return timestamp; }
		public String getTime() { return time; }
		public int getChannel() { return channel; }
		public double getRssi() { return rssi; }
		public double getSnr() { return snr; }
		public long getRfChain() { return rfChain; }
		public double getLatitude() { return latitude; }
		public double getLongitude() { return longitude; }
		public double getAltitude() { return altitude; }

		public UplinkGateway setGatewayId(String gatewayId) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.gatewayId = gatewayId; return this; }
		public UplinkGateway setTimestamp(long timestamp) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.timestamp = timestamp;  return this; }
		public UplinkGateway setTime(String time) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.time = time; return this; }
		public UplinkGateway setChannel(int channel) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.channel = channel; return this; }
		public UplinkGateway setRssi(double rssi) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.rssi = rssi; return this; }
		public UplinkGateway setSnr(double snr) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.snr = snr; return this; }
		public UplinkGateway setRfChain(long rfChain) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.rfChain = rfChain; return this; }
		public UplinkGateway setLatitude(double latitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.latitude = latitude; return this; }
		public UplinkGateway setLongitude(double longitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.longitude = longitude; return this; }
		public UplinkGateway setAltitude(double altitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.altitude = altitude; return this; }

		public UplinkGateway setImmutable() { this.bImmutable = true; return this; }

		public String toString() {
			return "\t\tGateway ID: "+this.gatewayId+"\n"
					+"\t\tTimestamp: "+this.timestamp+"\n"
					+"\t\tTime: "+this.time+"\n"
					+"\t\tChannel: "+this.channel+"\n"
					+"\t\tRSSI: "+this.rssi+"\n"
					+"\t\tSNR: "+this.snr+"\n"
					+"\t\tRF chain: "+this.rfChain+"\n"
					+"\t\tLocation: "+this.latitude+";"+this.longitude+" at "+this.altitude+"m\n";
		}
	}

	public UplinkMetadata() { this.gateways = new ArrayList<UplinkGateway>(); }

	public long getAirtime() { return airtime; }
	public String getTime() { return time; }
	public double getFrequency() { return frequency; }
	public String getModulation() { return modulation; }
	public String getLoraDataRate() { return loraDataRate; }
	public long getFskBitRate() { return fskBitRate; }
	public String getCodingRate() { return codingRate; }
	public double getLocLatitude() { return locLatitude; }
	public double getLocLongitude() { return locLongitude; }
	public double getLocAltitude() { return locAltitude; }
	public List<UplinkGateway> getGateways() { return gateways; }

	private boolean bImmutable = false;
	
	public UplinkMetadata setAirtime(long airtime) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.airtime = airtime; return this; }
	public UplinkMetadata setTime(String time) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.time = time; return this; }
	public UplinkMetadata setFrequency(double frequency) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.frequency = frequency; return this; }
	public UplinkMetadata setModulation(String modulation) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.modulation = modulation; return this; }
 	public UplinkMetadata setLoraDataRate(String loraDataRate) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.loraDataRate = loraDataRate; return this; }
	public UplinkMetadata setFskBitRate(long fskBitRate) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.fskBitRate = fskBitRate; return this; }
	public UplinkMetadata setCodingRate(String codingRate) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.codingRate = codingRate; return this; }
	public UplinkMetadata setLocLatitude(double locLatitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.locLatitude = locLatitude; return this; }
	public UplinkMetadata setLocLongitude(double locLongitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.locLongitude = locLongitude; return this; }
	public UplinkMetadata setLocAltitude(double locAltitude) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.locAltitude = locAltitude; return this; }
 	public UplinkMetadata setGateways(List<UplinkGateway> gateways) { if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); } this.gateways = gateways; return this; }

 	public UplinkMetadata setFromJSON(String json) throws TTNMessageParsingException  {
 		if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); }

 		try {
 			JSONValue parsedMessage = JSONParser.parseString(json);
 			if(!(parsedMessage instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected format"); }
 			JSONObject msgObj = (JSONObject)parsedMessage;

 			return setFromJSON(msgObj);
 		} catch(JSONParserException e) {
 			throw new TTNMessageParsingException(e);
 		}
 	}
 	public UplinkMetadata setFromJSON(JSONObject msgObj) throws TTNMessageParsingException {
 		if(bImmutable) { throw new TTNAccessDeniedException("Object immutable"); }
 		
 		
 		{
			JSONValue v = msgObj.get("airtime");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.airtime = ((JSONNumber)v).getLong();
			} else {
				this.airtime = -1;
			}
		}
		{
			JSONValue v = msgObj.get("time");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.time = ((JSONString)v).get();
			} else {
				this.time = null;
			}
		}
		{
			JSONValue v = msgObj.get("frequency");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.frequency = ((JSONNumber)v).getDouble();
			} else {
				this.frequency = -1;
			}
		}
		{
			JSONValue v = msgObj.get("modulation");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.modulation = ((JSONString)v).get();
				if(!(this.modulation.toUpperCase().equals(MODULATION_FSK) || this.modulation.toUpperCase().equals(MODULATION_LORA))) {
					throw new TTNMessageParsingException("Unknown modulation type "+this.modulation);
				}
			} else {
				this.modulation = null;
			}
		}
		{
			JSONValue v = msgObj.get("data_rate");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.loraDataRate = ((JSONString)v).get();
			} else {
				this.loraDataRate = null;
			}
		}
		{
			JSONValue v = msgObj.get("bit_rate");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.fskBitRate = ((JSONNumber)v).getLong();
			} else {
				this.fskBitRate = -1;
			}
		}
		{
			JSONValue v = msgObj.get("coding_rate");
			if(v != null) {
				if(!(v instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.codingRate = ((JSONString)v).get();
			} else {
				this.codingRate = null;
			}
		}
		{
			JSONValue v = msgObj.get("latitude");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.locLatitude = ((JSONNumber)v).getDouble();
			} else {
				this.locLatitude = -1;
			}
		}
		{
			JSONValue v = msgObj.get("longitude");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.locLongitude = ((JSONNumber)v).getDouble();
			} else {
				this.locLongitude = -1;
			}
		}
		{
			JSONValue v = msgObj.get("altitude");
			if(v != null) {
				if(!(v instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
				this.locAltitude = ((JSONNumber)v).getLong();
			} else {
				this.locAltitude = -1000000;
			}
		}
		{
			JSONValue v = msgObj.get("gateways");
			if(v != null) {
				if(!(v instanceof JSONArray)) { throw new TTNMessageParsingException("Unexpected data type"); }
				for(JSONValue member : ((JSONArray)v)) {
					/* Create a gateway for each entry ... */
					if(!(member instanceof JSONObject)) { throw new TTNMessageParsingException("Unexpected data type"); }

					UplinkGateway gw = new UplinkGateway();
					{
						JSONValue v2 = ((JSONObject)member).get("gtw_id");
						if(v2 != null) {
							if(!(v2 instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setGatewayId(((JSONString)v2).get());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("timestamp");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setTimestamp(((JSONNumber)v2).getLong());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("time");
						if(v2 != null) {
							if(!(v2 instanceof JSONString)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setTime(((JSONString)v2).get());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("channel");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setChannel(((JSONNumber)v2).getInt());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("rssi");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setRssi(((JSONNumber)v2).getDouble());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("snr");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setSnr(((JSONNumber)v2).getDouble());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("rf_chain");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setRfChain(((JSONNumber)v2).getLong());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("latitude");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setLatitude(((JSONNumber)v2).getDouble());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("longitude");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setLongitude(((JSONNumber)v2).getDouble());
						}
					}
					{
						JSONValue v2 = ((JSONObject)member).get("altitude");
						if(v2 != null) {
							if(!(v2 instanceof JSONNumber)) { throw new TTNMessageParsingException("Unexpected data type"); }
							gw.setAltitude(((JSONNumber)v2).getDouble());
						}
					}

					gw.setImmutable();
					this.gateways.add(gw);
				}
			}
		}

 		return this;
 	}

 	public UplinkMetadata setImmutable() {
 		this.bImmutable = true;
 		if(this.gateways != null) {
 			for(UplinkGateway gw : this.gateways) {
 				gw.setImmutable();
 			}
 		}
 		return this;
 	}

 	public String toString() {
 		StringBuilder builder = new StringBuilder();
 		builder.append("\tAirtime: "+this.airtime+"\n"
 				+"\tTime: "+this.time+"\n"
 				+"\tFrequency: "+this.frequency/1000000+" MHz\n"
 				+"\tModulation: "+this.modulation+"\n"
 				+"\tData rate (LoRA): "+this.loraDataRate+"\n"
 				+"\tBitrate (FSK): "+this.fskBitRate+"\n"
 				+"\tCoding rate: "+this.codingRate+"\n"
 				+"\tLocation: "+this.locLatitude+";"+this.locLongitude+" at "+this.locAltitude+"m\n"
 				+"\tGateways:\n");

 		for(UplinkGateway gw : this.gateways) {
 			builder.append(gw.toString());
 		}

 		return builder.toString();
 	}
}
