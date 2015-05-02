/*
 * PUTRequest.java
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
import edu.rosehulman.sws.protocol.AbstractHttpRequest;
import edu.rosehulman.sws.protocol.AbstractHttpResponse;
import edu.rosehulman.sws.protocol.IHttpResponse;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public class PUTRequest extends AbstractHttpRequest {

	public PUTRequest(String uri, String version, Map<String, String> header,
			char[] body) {
		this.method = Protocol.PUT;
		this.uri = uri;
		this.version = version;
		this.header = header;
		this.body = body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rosehulman.sws.protocol.IHTTPRequest#handleRequest()
	 */
	@Override
	public void handleRequest() {
		String date = header.get("if-modified-since"); // TODO: put in protocol
		String hostName = header.get("host"); // TODO: put in protocol

		// Create type ErrorResponse and verify that the response is not an error
		File file = lookup(false, null);
		
		if (!response.isError()) {
			this.response.setFile(AbstractHttpResponse.createTempResponseFile());
			// pass in true so that file is appended to
			WriteAction writeAction = new WriteAction(response, server, file, this.body, true);
			response = writeAction.performAction();
		}

		try {
			response.write(out);
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
