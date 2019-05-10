
package at.tspi.ttnclientj2.clientmqtt;

import java.nio.charset.Charset;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import at.tspi.ttnclientj2.client.TTNClient;
import at.tspi.ttnclientj2.exceptions.TTNConnectionFailedException;
import at.tspi.ttnclientj2.exceptions.TTNTransmissionError;
import at.tspi.ttnclientj2.messages.TTNDeviceEventActivation;
import at.tspi.ttnclientj2.messages.TTNDeviceEventCreated;
import at.tspi.ttnclientj2.messages.TTNDeviceEventDeleted;
import at.tspi.ttnclientj2.messages.TTNDeviceEventDownlinkAck;
import at.tspi.ttnclientj2.messages.TTNDeviceEventDownlinkSheduled;
import at.tspi.ttnclientj2.messages.TTNDeviceEventErrorActivation;
import at.tspi.ttnclientj2.messages.TTNDeviceEventErrorDownlink;
import at.tspi.ttnclientj2.messages.TTNDeviceEventErrorUplink;
import at.tspi.ttnclientj2.messages.TTNDownlinkSent;
import at.tspi.ttnclientj2.messages.TTNMessage;
import at.tspi.ttnclientj2.messages.TTNMessageDownlink;
import at.tspi.ttnclientj2.messages.TTNMessageUplink;

public class TTNClientMQTT extends TTNClient implements MqttCallbackExtended {
	private MqttConnectOptions conOptions;
	private MqttAsyncClient mqttClient;
	private String brokerUri;
	private int currentState;

	public TTNClientMQTT(String region, String appId, String accessKey, int port, String clientId) {
		super(region, appId, accessKey, port, clientId);

		conOptions = new MqttConnectOptions();
		conOptions.setAutomaticReconnect(true);
		conOptions.setHttpsHostnameVerificationEnabled(true);
		conOptions.setPassword(accessKey.toCharArray());
		conOptions.setUserName(appId);
		if(clientId == null) {
			conOptions.setCleanSession(true);
			this.setClientId(MqttAsyncClient.generateClientId());
		} else {
			conOptions.setCleanSession(false);
		}

		brokerUri = "";
		if(port == 1883) {
			brokerUri = brokerUri + "tcp://";
		} else if(port == 8883) {
			brokerUri = brokerUri + "ssl://";
		} else {
			// Whenever we cannot decide assume its SSL (not insane unencrypted traffic)
			brokerUri = brokerUri + "ssl://";
		}
		brokerUri = brokerUri + region + ".thethings.network:" + port;

		mqttClient = null;
	}

	@Override
	public void connect() throws TTNConnectionFailedException {
		if (mqttClient != null) {
			return;
		}

		try {
			/*
				Currently we do not support persistence - may be we should add
				that later on?
			 */
			this.currentState = TTNClient.STATE__CONNECTING;
			mqttClient = new MqttAsyncClient(brokerUri, getClientId(), new MemoryPersistence());
			mqttClient.setManualAcks(true); // We do acknowledge on our own
											// AFTER event handlers processed
											// messages
			mqttClient.setCallback(this);
			mqttClient.connect(conOptions, this, new IMqttActionListener() {
				@Override
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					System.err.println("OnFailure: "+(arg1 != null ? arg1.toString() : ""));
					/*
						This callback is called whenever we couldn't establish
						a connection to the server
					 */
					currentState = TTNClient.STATE__ERROR;
				}

				@Override
				public void onSuccess(IMqttToken arg0) {
					/*
						Subscribe to all relevant topics for TTN

						First group: <appid>/devices/<devid>/events/create ok

						<appid>/devices/<devid>/events/delete ok
						<appid>/devices/<devid>/events/activations ok

						<appid>/devices/<devid>/events/down/acks ok
						<appid>/devices/<devid>/events/up/errors ok
						<appid>/devices/<devid>/events/down/errors ok
						<appid>/devices/<devid>/events/down/sent ok
						<appid>/devices/<devid>/events/activations/errors ok

						Second group: <appid>/devices/<devid>/up

						Not subscribed because they are downlink:
						<appid>/devices/<devid>/down
					 */
					System.out.println("MQTT: Connection success, subscribing to channels");
					synchronized (this) {
						try {
							currentState = TTNClient.STATE__CONNECTED;

							mqttClient.subscribe(getAppId() + "/devices/+/events/#", 1, new IMqttMessageListener() {
								@Override
								public void messageArrived(String topic, MqttMessage message) {
									Date date = new Date(System.currentTimeMillis());
									System.out.println("Message arrived at "+topic+" ("+date.toString()+")");
									try {
										String[] topicParts = topic.split("\\/");
										if (topicParts.length < 5) {
											System.err.println("Unknown topic " + topic);
											mqttClient.messageArrivedComplete(message.getId(), message.getQos());
											return; // Simply ignore unknown messages
										}

										String appId = topicParts[0];
										String devId = topicParts[2];
										String eventName = topicParts[4];

										TTNMessage msg = null;

										if (topicParts.length == 5) {
											if (eventName.equals("create")) {
												msg = new TTNDeviceEventCreated(devId, appId);
												((TTNDeviceEventCreated) msg).setImmutable();
											} else if (eventName.equals("delete")) {
												msg = new TTNDeviceEventDeleted(devId, appId);
												((TTNDeviceEventDeleted) msg).setImmutable();
											} else if (eventName.equals("activations")) {
												msg = new TTNDeviceEventActivation(devId, appId);
												String jsonString = new String(message.getPayload());
												((TTNDeviceEventActivation) msg).setFromJSON(jsonString);
												((TTNDeviceEventActivation) msg).setImmutable();
											} else {
												System.err.println("Unknown message at " + topic);
												mqttClient.messageArrivedComplete(message.getId(), message.getQos());
												return;
											}
										} else if (topicParts.length == 6) {
											if (eventName.equals("down") && topicParts[5].equals("acks")) {
												msg = new TTNDeviceEventDownlinkAck(devId, appId);
												((TTNDeviceEventDownlinkAck) msg).setImmutable();
											} else if (eventName.equals("down") && topicParts[5].equals("errors")) {
												msg = new TTNDeviceEventErrorDownlink(devId, appId);
												((TTNDeviceEventErrorDownlink) msg).setImmutable();
											} else if (eventName.equals("down") && topicParts[5].equals("scheduled")) {
												msg = new TTNDeviceEventDownlinkSheduled(devId, appId);
												((TTNDeviceEventDownlinkSheduled) msg).setImmutable();
											} else if (eventName.equals("down") && topicParts[5].equals("sent")) {
												// ToDo: Fill with data
												msg = new TTNDownlinkSent(devId, appId);
												((TTNDownlinkSent) msg).setImmutable();
											} else if (eventName.equals("up") && topicParts[5].equals("errors")) {
												msg = new TTNDeviceEventErrorUplink(devId, appId);
												((TTNDeviceEventErrorUplink) msg).setImmutable();
											} else if (eventName.equals("activations")
													&& topicParts[5].equals("errors")) {
												msg = new TTNDeviceEventErrorActivation(devId, appId);
												((TTNDeviceEventErrorActivation) msg).setImmutable();
											} else {
												System.err.println("Unknown message at " + topic);
												mqttClient.messageArrivedComplete(message.getId(), message.getQos());
												return;
											}
										} else {
											System.err.println("Unknown message at " + topic);
											mqttClient.messageArrivedComplete(message.getId(), message.getQos());
											return;
										}

										// Post to attached listeners and ACK if any of them handeled the message
										if (internalHandleMessage(msg)) {
											mqttClient.messageArrivedComplete(message.getId(), message.getQos());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

							mqttClient.subscribe(getAppId() + "/devices/+/up", 1, new IMqttMessageListener() {
								@Override
								public void messageArrived(String topic, MqttMessage message) {
									try {
										String[] topicParts = topic.split("\\/");
										if (topicParts.length != 4) {
											System.err.println("Unknown message at " + topic);
											mqttClient.messageArrivedComplete(message.getId(), message.getQos());
											return;
										}

										String appId = topicParts[0];
										String devId = topicParts[2];
										byte[] payload = message.getPayload();
										TTNMessageUplink msg = new TTNMessageUplink();

										msg.setAppId(appId);
										msg.setDeviceId(devId);
										msg.setFromJSON(new String(payload));
										msg.setImmutable();
										if (internalHandleMessage(msg)) {
											mqttClient.messageArrivedComplete(message.getId(), message.getQos());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						} catch (MqttException e) {
							try {
								mqttClient.close();
							} catch (MqttException e2) {
								e2.printStackTrace();
							}
							mqttClient = null;
						}
					}
				}
			});
		} catch (MqttException e) {
			throw new TTNConnectionFailedException(e);
		}
	}

	@Override
	public boolean isConnected() {
		synchronized(this) {
			if(mqttClient == null) { return false; }

			return mqttClient.isConnected();
		}
	}

	@Override
	public int getConnectionState() {
		return this.currentState;
	}

	@Override
	public boolean send(TTNMessageDownlink msg) throws TTNTransmissionError {
		return send(msg, false, 0);
	}
	@Override
	public boolean send(TTNMessageDownlink msg, boolean retained) throws TTNTransmissionError {
		return send(msg, retained, 0);
	}
	@Override
	public boolean send(TTNMessageDownlink msg, boolean retained, int qos) throws TTNTransmissionError {
		synchronized(this) {
			if(mqttClient == null) { return false; }
			if(!mqttClient.isConnected()) { return false; }

		
			try {
				mqttClient.publish(
					msg.getAppId()+"/devices/"+msg.getDevId()+"/down",
					msg.toJSON().getBytes(Charset.forName("UTF-8")),
					qos,
					retained
				);
			} catch(MqttPersistenceException e) {
				throw new TTNTransmissionError(e);
			} catch(MqttException e) {
				throw new TTNTransmissionError(e);
			}

			return true;
		}
	}

	@Override
	public void close() {
		Date date = new Date(System.currentTimeMillis());
		System.out.println("MQTT: Close requested ("+date.toString()+")");
		synchronized(this) {
			if(mqttClient == null) { return; }
			if(!mqttClient.isConnected()) { return; }
			try {
				// We do a synchronous close on shutdown
				IMqttToken token = mqttClient.disconnect(5000);
				long timeoutSeconds = 30;
				while((!token.isComplete()) && (timeoutSeconds > 0)) {
					try { Thread.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); }
					timeoutSeconds = timeoutSeconds - 1;
				}
				mqttClient.close();
			} catch(MqttException e) {
				e.printStackTrace();
			}
			mqttClient = null;
		}

	}

	/*
		MQTT client callbacks below
	 */
	@Override
	public void connectionLost(Throwable arg0) {
		Date date = new Date(System.currentTimeMillis());
		System.err.println("MQTT: Connection lost ("+date+")");
		this.currentState = TTNClient.STATE__ERROR;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// Unused callback
		return;
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// Unused callback
		Date date = new Date(System.currentTimeMillis());
		System.err.println("MQTT: Message arrived (wrong callback) lost ("+date.toString()+")");
		return;
	}

	@Override
	public void connectComplete(boolean arg0, String arg1) {
		// We use this to track our connection state for the isConnected function
		Date date = new Date(System.currentTimeMillis());
		System.out.println("MQTT: Connect complete ("+date.toString()+")");
		this.currentState = TTNClient.STATE__CONNECTED;
	}
}
