/*
 * ConnectionHandler.java
 * Apr 23, 2015
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import edu.rosehulman.sws.extension.IPlugin;
import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.impl.HTTPResponses.Response200OK;
import edu.rosehulman.sws.impl.HTTPResponses.Response500InternalServiceError;
import edu.rosehulman.sws.protocol.AbstractHttpResponse;
import edu.rosehulman.sws.protocol.IHttpRequest;
import edu.rosehulman.sws.protocol.IHttpResponse;
import edu.rosehulman.sws.protocol.ProtocolException;

/**
 * 
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket socket;
	private ServerCache serverCahce;
	private boolean keep_alive = true;
	
	private int reqsReceived;
	private int reqsServed;
	private long lastServiceTime;
	private long totalServiceTime;
	private double avgRespTime;
	
	public ConnectionHandler(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.serverCahce = server.getCache();
		
		reqsReceived = 0;
		reqsServed = 0;
		lastServiceTime = 0;
		totalServiceTime = 0;
		avgRespTime = 0;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			inStream = this.socket.getInputStream(); // for incoming requests
			outStream = this.socket.getOutputStream(); // for outgoing responses
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// At this point we have the input and output stream of the socket
		// Now lets create a HttpRequest object
		IHttpRequest request = null;
		while(keep_alive) {
			/* This code was used to test if a "keep-alive" connection gets closed or not
			if(reqsServed > 0) {
				System.out.println("Waiting for connection to close...");
				while(true) {
					if(socket.isClosed()) {
						System.out.println("Connection Closed!!!!");
					}
				}
			}
			*/
			keep_alive = false;
			long start = System.currentTimeMillis();
			try {
//				if (inStream == null)
//					return;
				try {
					request = URLParser.parseIncomingRequest(inStream);
					request.setCallback(server, outStream);
					reqsReceived++;
					keep_alive = request.getHeader().get(Protocol.CONNECTION.toLowerCase()).equals("keep-alive");
					System.out.println("Pre-Response: Keep-Alive = " + keep_alive);
				} catch (Exception e) {
					e.printStackTrace(); //TODO this is a socket exception when the client unexpectedly closes a keep-alive connection
					break;
				}
				
//				// check if request is cached
//				IHttpResponse cachedResponse = serverCahce.getCachedResponse(request.getMethod(), request.getUri());
//				if (cachedResponse != null) {
//					System.out.println("Responding with Cached Response");
//					cachedResponse.write(outStream);
//				} 
				// otherwise handle request as normal
//				else {
					System.out.println(request.toString());
					this.distributeRequest(request);
					reqsServed++;
					System.out.println("Response Sent!");
					// Increment number of connections by 1
					// Get the end time
					long end = System.currentTimeMillis();
					lastServiceTime = end - start;
					totalServiceTime += lastServiceTime;
					avgRespTime = (double) totalServiceTime / (double) reqsServed;					
//				}
				
			} catch (Exception e) {
				e.printStackTrace();
				IHttpResponse errorResponse = new Response500InternalServiceError(Protocol.VERSION, AbstractHttpResponse.createTempResponseFile());
				try {
					errorResponse.write(outStream);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			keep_alive = request.getHeader().get(Protocol.CONNECTION.toLowerCase()).equals(Protocol.OPEN.toLowerCase());
			System.out.println(request.getHeader().get(Protocol.CONNECTION.toLowerCase()));
			System.out.println("Restarting loop: Keep-Alive: " + keep_alive);
			this.printStatistics();
		}
	}
	
	private void distributeRequest(IHttpRequest request) {
		String pluginDomain = URLParser.getPluginDomain(request.getUri());
		IPlugin p = server.getPlugin(pluginDomain);
		if(p != null) {
//			System.out.println("PLUGIN FOUND");
			p.route(request, new Response200OK());
		} else {
//			System.out.println("NO-PLUGIN FOUND");
			System.out.println("Handling Request");
			request.handleRequest();
		}
		
//		// cache response
//		this.serverCahce.cacheResponse(request.getMethod(), request.getUri(), request.getResponse());
		if(!keep_alive) {
			try {
				System.out.println("Server is closing socket");
				this.socket.close();
				server.incrementConnections(1);
				server.incrementServiceTime(totalServiceTime);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void printStatistics() {
		System.out.println("Statistics-------------------------------");
		System.out.println("Requests Received: " + reqsReceived);
		System.out.println("Requests Served: " + reqsServed);
		System.out.println("Total Service Time: " + totalServiceTime + " ms");
		System.out.println("Last Service Time: " + lastServiceTime + " ms");
		System.out.println("Avg. Response Time: " + avgRespTime + " ms/req");
		System.out.println("-----------------------------------------");
	}
}