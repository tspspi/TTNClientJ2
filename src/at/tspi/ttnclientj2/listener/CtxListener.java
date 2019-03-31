package at.tspi.ttnclientj2.listener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import at.tspi.ttnclientj2.client.TTNClient;
import at.tspi.ttnclientj2.client.TTNClientFactory;
import at.tspi.ttnclientj2.client.TTNMessageHandler;
import at.tspi.ttnclientj2.exceptions.TTNConnectionFailedException;

@WebListener
public class CtxListener implements ServletContextListener {
	private class ClientListEntry {
		private TTNClient cli;
		private TTNMessageHandler handler;

		public ClientListEntry(TTNClient cli, TTNMessageHandler handler) {
			this.cli = cli;
			this.handler = handler;
		}
		public TTNClient getClient() { return this.cli; }
		public TTNMessageHandler getHandler() { return this.handler; }
	}

	static List<ClientListEntry> lstClients;
	static {
		lstClients = new ArrayList<ClientListEntry>();
	}

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  {
    	/*
			Shutdown any TTN connections
    	 */

    	for(ClientListEntry ce : lstClients) {
    		System.out.println("Calling close for "+ce.getClient().getAppId()+" in region "+ce.getClient().getRegion());
    		ce.getClient().close(); // Note: This is a synchronous operation
    	}
    	lstClients.clear();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  {
    	/*
			Connect all TTN clients that are configured in the servlet
			context

			They are declared via init params as:
			
			1) A comma separated list of connection names
			
				<context-param>
  					<param-name>ttnconnections</param-name>
  					<param-value>con1,con2</param-value>
  				</context-param>

			2) Parameters per connection

				<context-param>
					<param-name>[conname]-url</param-name>
					<param-value> URI containing also credentials and key in the form mqtt://APPID:APPSECRET@ZONE:PORT </param>
					<!--
						Note that ZONE is really only the region like "eu", etc., not the full URI
						Port determines if the connection uses SSL (8883) or not
					-->

					<param-name>[conname]-handler</param-name>
					<param-value> CLASSNAME </param-value>
					<!--
						This is the name of the handler that should be instantiated. The handler
						has to implement TTNMessageHandler
					-->
				</context-param>
    	 */
    	String strConnections = sce.getServletContext().getInitParameter("ttnconnections");
    	if(strConnections == null) {
    		System.err.println("No TTN connections configured. Skipping");
    		return;
    	}

    	String strConParts[] = strConnections.split(",");
    	for(String conName : strConParts) {
    		String conNameT = conName.trim();

    		// Fetch URI and Handler Class
    		String uri = sce.getServletContext().getInitParameter(conNameT+"-url");
    		String handler = sce.getServletContext().getInitParameter(conNameT+"-handler");

    		if((uri == null) || (handler == null)) {
    			System.err.println("Failed to get full configuration (uri and handler) for "+conNameT);
    			continue;
    		}

    		TTNMessageHandler hand = null;
    		TTNClient cli = null;

    		try {
    			hand = (TTNMessageHandler) Class.forName(handler).newInstance();
    		} catch(Exception e) {
    			e.printStackTrace();
    			continue; // Do NOT initialize this connection and continue with the next one
    		}

    		if(hand == null) { continue; } // Should never happen but catch that possible error anyways

    		try {
				cli = TTNClientFactory.createClient(new URI(uri));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			}

    		lstClients.add(new ClientListEntry(cli, hand));
    		try {
    			cli.attachMessageHandler(hand);
    			cli.connect();
    		} catch(TTNConnectionFailedException e) {
    			System.err.println("Failed to connect with "+cli.getAppId()+" in region "+cli.getRegion()+" when using URI "+uri);
    			e.printStackTrace();
    			continue;
    		}
    		System.out.println("Added handler "+handler+" for "+uri);
    	}
    }
}
