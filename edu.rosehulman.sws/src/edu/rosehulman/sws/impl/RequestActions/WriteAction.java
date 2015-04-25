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

/**
 * 
 */
public class WriteAction extends AbstractRequestAction {
	private char[] body;

	public WriteAction(IHTTPResponse response, File file, char[] body) {
		this.response = response;
		this.file = file;
		this.body = body;
	}
	
	@Override
	public IHTTPResponse performAction() {
		// Then create new file
		try {
			this.file.createNewFile();
			// false so it overwrites existing file
			 FileWriter fw = new FileWriter(this.file,false);
             fw.write(this.body);
             fw.close();
             System.out.println("FILE WRITE PATH: " + this.file.getAbsolutePath());
             System.out.println("FILE WRITE CONTENT: " + this.body.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Lets fill up header fields with more information
		response.fillGeneralHeader(Protocol.CLOSE);
		// Lets add last modified date for the file
		long timeSinceEpoch = file.lastModified();
		Date modifiedTime = new Date(timeSinceEpoch);
		response.put(Protocol.LAST_MODIFIED, modifiedTime.toString());
		
		System.out.println(response);
		return response;
	}

}
