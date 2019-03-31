SRCS=src/at/tspi/ttnclientj2/client/TTNClient.java \
	src/at/tspi/ttnclientj2/client/TTNClientFactory.java \
	src/at/tspi/ttnclientj2/client/TTNMessageHandler.java \
	src/at/tspi/ttnclientj2/clientmqtt/TTNClientMQTT.java \
	src/at/tspi/ttnclientj2/exceptions/TTNAccessDeniedException.java \
	src/at/tspi/ttnclientj2/exceptions/TTNConnectionFailedException.java \
	src/at/tspi/ttnclientj2/exceptions/TTNMessageParsingException.java \
	src/at/tspi/ttnclientj2/exceptions/TTNTransmissionError.java \
	src/at/tspi/ttnclientj2/listener/CtxListener.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEvent.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventActivation.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventCreated.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventDeleted.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventDownlinkAck.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventDownlinkSheduled.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventErrorActivation.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventErrorDownlink.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventErrorUplink.java \
	src/at/tspi/ttnclientj2/messages/TTNDeviceEventUpdated.java \
	src/at/tspi/ttnclientj2/messages/TTNDownlinkSent.java \
	src/at/tspi/ttnclientj2/messages/TTNMessage.java \
	src/at/tspi/ttnclientj2/messages/TTNMessageDownlink.java \
	src/at/tspi/ttnclientj2/messages/TTNMessageUplink.java \
	src/at/tspi/ttnclientj2/messages/UplinkMetadata.java

# ToDo: Setup classpath to include
#   TJSON.jar
#   org.eclipse.paho.client.mqttv3-1.2.1.jar
#   servlet-api.jar
#   tomcat-util.jar

all: dirs classes jar clean

dirs:

	-@mkdir -p bin
	-@mkdir -p classes/at/tspi/ttnclientj2/client
	-@mkdir -p classes/at/tspi/ttnclientj2/clientmqtt
	-@mkdir -p classes/at/tspi/ttnclientj2/exceptions
	-@mkdir -p classes/at/tspi/ttnclientj2/listener
	-@mkdir -p classes/at/tspi/ttnclientj2/messages

classes:

	javac -d classes/ $(SRCS)

jar:

	jar -cvf bin/ttnclientj2.jar -C ./classes .

clean:

	rm -rf ./classes

.PHONY: dirs classes jar