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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.protocol.AbstractRequestAction;
import edu.rosehulman.sws.protocol.IHTTPResponse;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public class WriteAction extends AbstractRequestAction {

	public WriteAction(IHTTPResponse response, Server server, char[] body, String uri) {
		this.response = response;
		this.server = server;
		this.body = body;
		this.uri = uri;
	}
	
	@Override
	public IHTTPResponse performAction() {
		// Then create new file
		try {
			
			//TODO: This stuff still needs to be done
			File newFile = new File(server.getRootDirectory() + uri);
			System.out.println("path: " + newFile.getAbsolutePath());
			System.out.println("exists: " + newFile.exists());
			getFileBody(); //TODO CHANGE ME
			// false so it overwrites existing file
			 FileWriter fw = new FileWriter(newFile,false);
             fw.write(this.body);
             fw.close();
             System.out.println("FILE WRITE PATH: " + newFile.getAbsolutePath());
             System.out.println("FILE WRITE CONTENT: ");
             for(char c : body) {
            	 System.out.print(c);
             }
             System.out.println();

		
		// Lets fill up header fields with more information
		response.fillGeneralHeader(Protocol.CLOSE);
		// Lets add last modified date for the file
		long timeSinceEpoch = newFile.lastModified();
		Date modifiedTime = new Date(timeSinceEpoch);
		response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);
		return response;
	}

}
