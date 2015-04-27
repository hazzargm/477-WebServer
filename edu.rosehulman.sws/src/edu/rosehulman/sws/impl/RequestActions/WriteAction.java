/*
 * WriteAction.java
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

package edu.rosehulman.sws.impl.RequestActions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.impl.HTTPResponses.Response500InternalServiceError;
import edu.rosehulman.sws.protocol.AbstractHTTPResponse;
import edu.rosehulman.sws.protocol.AbstractRequestAction;
import edu.rosehulman.sws.protocol.IHTTPResponse;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public class WriteAction extends AbstractRequestAction {
	private boolean shouldAppend;

	public WriteAction(IHTTPResponse response, Server server, File file, char[] body, boolean shouldAppend) {
		this.response = response;
		this.server = server;
		this.body = body;
		this.shouldAppend = shouldAppend;
		this.file = file;
	}

	@Override
	public IHTTPResponse performAction() {
		// Then create new file
		try {
			String fileBody = new String(getFileBody());
			FileWriter fw = new FileWriter(file, this.shouldAppend);
			BufferedWriter bufferWritter = new BufferedWriter(fw);
			System.out.println("FILE-BODY " + fileBody);
			if (this.shouldAppend) {
				bufferWritter.append(fileBody);
			} else {
				bufferWritter.write(fileBody);
			}
			bufferWritter.flush();
	        bufferWritter.close();
			fw.close();

			// Lets fill up header fields with more information
			response.fillGeneralHeader(Protocol.CLOSE);
			// Lets add last modified date for the file
			long timeSinceEpoch = file.lastModified();
			Date modifiedTime = new Date(timeSinceEpoch);
			response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());

		} catch (IOException e) {
			e.printStackTrace();
			return new Response500InternalServiceError(this.response.getVersion(), file);
		}
		
		return response;
	}

}
