# Simple TTN client for JavaEE

This is a simple client library to connect to the things network and receive messages
inside a JavaEE application container. It provides an context listener that will
be initialized (should be discovered by a container like Apache Tomcat by it's
annotation) based on the context configuration (either from your servers
context.xml or the local WebContent/WEB-INF/web.xml.

The configuration is pretty simple. There are three basic configuration parameters.
The first context parameter is ```ttnconnections```. It contains a comma
separated list of names of connections that should be automatically established
on context initialization (i.e. most of the time this is equivalent to the
deployment of the application container).

```
`<context-param>
	<param-name>ttnconnections</param-name>
	<param-value>con1</param-value>
</context-param>
```

For every named connection there is an ```url``` and ```handler``` parameter. The
URL parameter contains protocol, application ID and authentication information as
well as the region that the client should connect to. To connect to an application
in the EU region the URI would look like the following:

```
<context-param>
	<param-name>con1-url</param-name>
	<param-value>mqtt://your-app-id:ttn-account-v2.theauthtoken-that-issupplied-by-ttn@eu:8883</param-value>
</context-param>
```

The handler specifies which class should be instantiated and invoked whenever a message
arrives via this connection. This class has to implement the
```at.tspi.ttnclientj2.client.TTNMessageHandler``` interface. This interface provides
an ```initializeHandler``` routine that can be used to configure the handler from
the ```ServletContextEvent``` that has been passed to the context listener and an
handler function (```handleTTNMessage```) that gets invoked on every received packet.

```
<context-param>
	<param-name>con1-handler</param-name>
	<param-value>com.example.handlers.MyHandlerClass</param-value>
</context-param>
```

The packets are passed as ```TTNMessage```. Various types of messages exist:

* TTN Device Events:
  * ```TTNDeviceEventActivation``` is invoked whenever an device is activated
    (for example via OTAA)
  * ```TTNDeviceEventCreated``` is invoked whenever a new device is created in
    the console or via the API
  * ```TTNDeviceEventDeleted``` is the counterpart to the created event.
  * ```TTNDeviceEventUpdated```
  * ```TTNDeviceEventDownlinkAck```
  * ```TTNDeviceEventDownlinkSheduled```
  * ```TTNDownlinkSent```
  * ```TTNDeviceEventErrorActivation```
  * ```TTNDeviceEventErrorDownlink```
  * ```TTNDeviceEventErrorUplink```
* ```TTNMessageUplink``` is the message container that contains a normal
  received message as well as all associated metadata (inside an
  ```UplinkMetadata``` object
* ```TTNMessageDownlink``` is the only container that gets instantiated by
  the application itself - it is used by the ```send``` routine of the
  ```TTNClient``` implementations.

## Simple usage example

To be done