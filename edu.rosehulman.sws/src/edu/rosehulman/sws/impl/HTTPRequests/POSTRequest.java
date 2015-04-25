/*
 * POSTRequest.java
 * Apr 22, 2015
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
 
package edu.rosehulman.sws.impl.HTTPRequests;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.impl.HTTPResponses.Response200OK;
import edu.rosehulman.sws.impl.RequestActions.ReadAction;
import edu.rosehulman.sws.impl.RequestActions.WriteAction;
import edu.rosehulman.sws.protocol.AbstractHTTPRequest;
import edu.rosehulman.sws.protocol.IHTTPResponse;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public class POSTRequest extends AbstractHTTPRequest {
	
	public POSTRequest(String uri, String version, Map<String,String> header) {
		this.uri = uri;
		this.version = version;
		this.header = header;
	}

	/* (non-Javadoc)
	 * @see edu.rosehulman.sws.protocol.IHTTPRequest#handleRequest()
	 */
	@Override
	public void handleRequest(Server server, OutputStream outStream, long start) {
		String date = header.get("if-modified-since"); //TODO: put in protocol
		String hostName = header.get("host"); //TODO: put in protocol
		File file = lookup(server);
		// Create type ErrorResponse and verify that the response is not an error
		IHTTPResponse response = new Response200OK(Protocol.VERSION, file);
		WriteAction writeAction = new WriteAction(response, file, this.body);
		response = writeAction.performAction();
		try {
			response.write(outStream);
			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			server.incrementServiceTime(end - start);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
