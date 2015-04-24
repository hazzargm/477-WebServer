/*
 * GenericResponse.java
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
 
package edu.rosehulman.sws.impl.HTTPResponses;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.protocol.AbstractHTTPResponse;

/**
 * 
 */
public class Response200OK extends AbstractHTTPResponse {

	public Response200OK(String version, File file) {
		this.version = version;
		this.status = Protocol.OK_CODE;
		this.phrase = Protocol.OK_TEXT;
		this.header = new HashMap<String,String>();
		this.file = file;
	}
	
	/* (non-Javadoc)
	 * @see edu.rosehulman.sws.protocol.IHTTPResponse#write(java.io.OutputStream)
	 */
	@Override
	public void write(OutputStream outStream) {
		BufferedOutputStream out = new BufferedOutputStream(outStream, Protocol.CHUNK_LENGTH);
		writeGenericHeader(out);
	
//		System.out.println("\n\nResponse:\n" + this);
		// We are reading a file
		if(file != null) {
			// Process text documents
			FileInputStream fileInStream;
			try {
				fileInStream = new FileInputStream(file);
				BufferedInputStream inStream = new BufferedInputStream(fileInStream, Protocol.CHUNK_LENGTH);
			
				byte[] buffer = new byte[Protocol.CHUNK_LENGTH];
				int bytesRead = 0;
				// While there is some bytes to read from file, read each chunk and send to the socket out stream
				while((bytesRead = inStream.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				// Close the file input stream, we are done reading
				inStream.close();
					
				// Flush the data so that outStream sends everything through the socket 
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
