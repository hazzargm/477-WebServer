/*
 * ConnectionMonitor.java
 * May 10, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */
 
package edu.rosehulman.sws.server;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ConnectionMonitor {
	
	public static int MAX_CLIENT_CONNECTIONS = 10;
	public static int MAX_CONNECTION_AGE = 3000;
	
	private Map<String, PriorityQueue<ConnectionHandler>> connections;
	private ConnectionComparator connectionComparator;
	
	public ConnectionMonitor() {
		connectionComparator = new ConnectionComparator();
		connections = new HashMap<String, PriorityQueue<ConnectionHandler>>();
	}
	
	public void removeOldConnections() {
		for (PriorityQueue<ConnectionHandler> c : connections.values()) {
			if (c.size() > 0 && c.element().getAge() >= MAX_CONNECTION_AGE) {
				stopMonitoringConnection(c.element());
			}
		}
	}
	
	/**
	 * @param handler
	 * @return if it decides to monitor that connection
	 */
	public boolean monitor(ConnectionHandler handler) {
		String address = handler.getAddress();
		PriorityQueue<ConnectionHandler> connectionQueue = connections.get(address);
		
		// ensure client connection queue
		if (connectionQueue == null) {
			connectionQueue = new PriorityQueue<ConnectionHandler>(connectionComparator);
			connections.put(address, connectionQueue);
		}
		
		System.out.println("Monitoring Connection For: " + handler.getAddress() + " --- with connection amount: " + connectionQueue.size());
		
		// check to see if there are too many connections
		if (connectionQueue.size() >= MAX_CLIENT_CONNECTIONS) {
			return false;
		} else {
			connectionQueue.add(handler);
			return true;
		}
	}
	
	/**
	 * @param connectionHandler
	 */
	public void stopMonitoringConnection(ConnectionHandler c) {
		System.out.println("Stopped Monitoring Connection For: " + c.getAddress());
		PriorityQueue<ConnectionHandler> connectionQueue = connections.get(c.getAddress());
		try {
			c.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (connectionQueue == null) return;
		connectionQueue.remove(c);
	}
	
	// comparable class for ConnectionHandlers
	 static class ConnectionComparator implements Comparator<ConnectionHandler>
	 {
	     public int compare(ConnectionHandler c1, ConnectionHandler c2)
	     {
	    	 long age1 = c1.getAge();
	    	 long age2 = c2.getAge();
	    	 
	    	 if (age1 > age2) {
	    		 return 1;
	    	 } else if (age1 < age2) {
	    		 return -1;
	    	 } else {
	    		 return 0;
	    	 }
	     }
	 }
}
