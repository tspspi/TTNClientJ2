package at.tspi.ttnclientj2.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import at.tspi.ttnclientj2.clientmqtt.TTNClientMQTT;

public class TTNClientFactory {
	// Singleton collection
	private static HashMap<String, TTNClient> clientPool;
	private static Object sync;

	static {
		clientPool = new HashMap<String, TTNClient>();

		sync = new Object();
	}

	public static TTNClient createClient(URI uri) throws MalformedURLException {
		/*
			Creates a TTN Client by (non FQDN) URI. Supported formats look like:

				mqtt://appid:user@region:port/localClientId
				amqp://appid:user@region:port/localClientId
		 */
		String protocol = uri.getScheme().toLowerCase();

		if(!(protocol.equals("mqtt")
			|| protocol.equals("amqp"))) {
			throw new MalformedURLException("Only MQTT or AMQP URIs are supported by TTN Client");
		}

		String userInfo = uri.getUserInfo();
		if(userInfo == null) { throw new MalformedURLException("TTN Client requires authentication information"); }
		String[] userInfoParts = userInfo.split("\\:", 2);
		if(userInfoParts.length != 2) { throw new MalformedURLException("TTN Client requires app id and application key as authentication information"); }

		String region = uri.getHost().toLowerCase();
		String appId = userInfoParts[0];
		String appSecret = userInfoParts[1];
		int port = uri.getPort();
		if(port == -1) {
			if(protocol.equals("mqtt")) {
				port = 8883; // Default to SSL port
				// TODO: Same for AMQP
			} else {
				throw new MalformedURLException("No default port known for schema "+protocol);
			}
		}
		if((port < 0) || (port > 65535)) { throw new MalformedURLException("Port is outside of supported range"); }

		String clientId = uri.getPath();

		String identifier = appId+"@"+region;

		synchronized (sync) {
			if(clientPool.containsKey(identifier)) {
				return clientPool.get(identifier);
			}

			// Create a new client
			if(protocol.equals("mqtt")) {
				TTNClient cli = new TTNClientMQTT(region, appId, appSecret, port, clientId);
				clientPool.put(identifier, cli);
				return cli;
			} else if(protocol.equals("amqp")) {
				// TODO
				throw new RuntimeException("Not implemented");
			}
			return null; // This should be unreachable ...
		}
	}

	public static void closeAll() {
		synchronized(sync) {
			for(Map.Entry<String, TTNClient> e : clientPool.entrySet()) {
				TTNClient cli = e.getValue();
				cli.close(); // Note that "close" is synchronous!
			}

			clientPool.clear();
		}
	}
}
