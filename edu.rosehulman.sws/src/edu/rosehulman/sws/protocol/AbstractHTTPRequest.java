/*
 * AbstractHTTPRequest.java
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

package edu.rosehulman.sws.protocol;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.sws.gui.SpringUtilities;
import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public abstract class AbstractHTTPRequest implements IHTTPRequest {
	protected String method;
	protected String uri;
	protected String version;
	protected Map<String, String> header;
	protected char[] body;
	protected Map<String, String> bodyHeader;

	protected IHTTPResponse response;

	public AbstractHTTPRequest() {
		this.header = new HashMap<String, String>();
		this.body = new char[0];
	}

	/**
	 * The request method.
	 * 
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * The URI of the request object.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * The version of the http request.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	public char[] getBody() {
		return body;
	}

	/**
	 * The key to value mapping in the request header fields.
	 * 
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		// Lets return the unmodifable view of the header map
		return Collections.unmodifiableMap(header);
	}

	public File lookup(Server server, boolean ensureFileCreation, String fileName) {
		// Get root directory path from server
		String rootDirectory = server.getRootDirectory();
		
		// normalize optional filename
		if (fileName == null) {
			fileName = "";
		} else {
			fileName = File.separator + fileName;
		}
		
		// Combine them together to form absolute file path
		File file = new File(SpringUtilities.combine(rootDirectory, uri) + fileName);
		System.out.println("ROOT" + rootDirectory);
		System.out.println("URI --- " + uri);
		System.out.println("PATH --- " + file.getAbsolutePath());
		
		
		// Check if the file exists
		if (file.exists()) {
			System.out.println("LOOKUP: Found File");
			if (file.isDirectory()) {
				// Look for default index.html file in a directory
				String location = rootDirectory + uri
						+ System.getProperty("file.separator")
						+ Protocol.DEFAULT_FILE;
				file = new File(location);
				if (file.exists()) {
					return file;
				}
			} else { // Its a file
				return file;
			}
		} else if (ensureFileCreation) {
			// check if should create file
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return file;
		}
		
		// File does not exist so lets create 404 file not found code
		System.out.println("FILE DOES NOT EXIST FOR LOOKUP");
		
		
		
		return null;

	}


	
//	protected void printHeaderValuesOnly() {
//		for(String s : header.keySet()){
//			System.out.println("{key:"+ s + "} ->" + "{value:"+ header.get(s)+"}");
//		}
//	}
//	
//	protected void printBodyHeaderValuesOnly() {
//		for(String s : bodyHeader.keySet()){
//			System.out.println("{key:"+ s + "} ->" + "{value:"+ bodyHeader.get(s)+"}");
//		}
//	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------- Header ----------------\n");
		buffer.append(this.method);
		buffer.append(Protocol.SPACE);
		buffer.append(this.uri);
		buffer.append(Protocol.SPACE);
		buffer.append(this.version);
		buffer.append(Protocol.LF);

		for (Map.Entry<String, String> entry : this.header.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(Protocol.SEPERATOR);
			buffer.append(Protocol.SPACE);
			buffer.append(entry.getValue());
			buffer.append(Protocol.LF);
		}
		buffer.append("------------- Body ---------------\n");
		buffer.append(this.body);
		buffer.append("----------------------------------\n");
		return buffer.toString();
	}
}
