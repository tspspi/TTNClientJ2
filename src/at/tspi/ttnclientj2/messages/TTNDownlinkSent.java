package at.tspi.ttnclientj2.messages;

import at.tspi.ttnclientj2.exceptions.TTNAccessDeniedException;

public class TTNDownlinkSent extends TTNDeviceEvent {
	byte[] rawPayload;
	String gatewayId;

	String modulation;
	String dataRate;
	long airtime;
	long counter;
	double frequency;
	double power;

	boolean bImmutable = false;

	public TTNDownlinkSent() 								{ super(); }
	public TTNDownlinkSent(String devId, String appId)		{ super(devId, appId); }

	public byte[] getRawPayload() 							{ return rawPayload; }
	public String getGatewayId() 							{ return gatewayId; }
	public String getModulation() 							{ return modulation; }
	public String getDataRate() 							{ return dataRate; }
	public long getAirtime() 								{ return airtime; }
	public long getCounter() 								{ return counter; }
	public double getFrequency() 							{ return frequency; }
	public double getPower() 								{ return power; }

	public TTNDownlinkSent setRawPayload(byte[] rawPayload)	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.rawPayload = rawPayload; return this; }
	public TTNDownlinkSent setGatewayId(String gatewayId)	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.gatewayId = gatewayId; return this; }
	public TTNDownlinkSent setModulation(String modulation)	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.modulation = modulation; return this; }
	public TTNDownlinkSent setDataRate(String dataRate) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.dataRate = dataRate; return this; }
	public TTNDownlinkSent setAirtime(long airtime) 		{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.airtime = airtime; return this; }
	public TTNDownlinkSent setCounter(long counter) 		{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.counter = counter; return this; }
	public TTNDownlinkSent setFrequency(double frequency) 	{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.frequency = frequency; return this; }
	public TTNDownlinkSent setPower(double power) 			{ if(bImmutable) { throw new TTNAccessDeniedException("Object is immutable"); } this.power = power; return this; }

	public TTNDownlinkSent setImmutable()					{ this.bImmutable = true; super.setImmutable(); return this; }

	public String toString() {
		return "["+this.getAppId()+":"+this.getDeviceId()+"] Downlink sent";
	}
}
