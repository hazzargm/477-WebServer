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
import java.net.Socket;

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

	public ConnectionHandler(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.serverCahce = server.getCache();
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
		// Get the start time
		long start = System.currentTimeMillis();

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = this.socket.getInputStream(); // for incoming requests
			outStream = this.socket.getOutputStream(); // for outgoing responses
			
		} catch (Exception e) {
			// Cannot do anything if we have exception reading input or output
			// stream
			// May be have text to log this for further analysis?
			e.printStackTrace();

			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end - start);
			return;
		}

		// At this point we have the input and output stream of the socket
		// Now lets create a HttpRequest object
		IHttpRequest request = null;
		try {
			System.out.println("STREAM" + inStream.toString());
			if (inStream == null) return;
			request = URLParser.parseIncomingRequest(inStream);
			request.setCallback(server, outStream, start);
			
			// check if request is cached
			IHttpResponse cachedResponse = serverCahce.getCachedResponse(request.getMethod(), request.getUri());
			if (cachedResponse != null) {
				System.out.println("@@@@@@Responding with Cahced Response@@@@@@@");
				cachedResponse.write(outStream);
			} 
			// otherwise handle request as normal
			else {
				this.distributeRequest(request);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			IHttpResponse errorResponse = new Response500InternalServiceError(Protocol.VERSION, AbstractHttpResponse.createTempResponseFile());
			try {
				errorResponse.write(outStream);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
	}
	
	private void distributeRequest(IHttpRequest request) {
		System.out.println(request);
		String pluginDomain = URLParser.getPluginDomain(request.getUri());
		IPlugin p = server.getPlugin(pluginDomain);
		if(p != null) {
			System.out.println("PLUGIN FOUND");
			p.route(request, new Response200OK());
		} else {
			System.out.println("NO-PLUGIN FOUND");
			request.handleRequest();
		}
		
		// cache response
//		this.serverCahce.cacheResponse(request.getMethod(), request.getUri(), request.getResponse());
		
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}