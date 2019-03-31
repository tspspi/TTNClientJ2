package at.tspi.ttnclientj2.client;

import javax.servlet.ServletContextEvent;

import at.tspi.ttnclientj2.messages.TTNMessage;

public interface TTNMessageHandler {
	public boolean initializeHandler(ServletContextEvent sce);
	public boolean handleTTNMessage(TTNMessage msg, TTNClient cli);
}
