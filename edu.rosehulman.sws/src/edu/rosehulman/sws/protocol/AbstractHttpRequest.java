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
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.sws.gui.SpringUtilities;
import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.impl.HTTPResponses.Response200OK;
import edu.rosehulman.sws.impl.HTTPResponses.Response404NotFound;
import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public abstract class AbstractHttpRequest implements IHttpRequest {
	protected String method;
	protected String uri;
	protected String version;
	protected Map<String, String> header;
	protected char[] body;
	
	protected Server server;
	protected OutputStream out;

	protected IHttpResponse response;

	public AbstractHttpRequest() {
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
	 * The request method.
	 * 
	 * @return the method
	 */
	public IHttpResponse getResponse() {
		return response;
	}
	
	/**
	 * The URI of the request object.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
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
	
	public OutputStream getClientOutputStream() {
		return this.out;
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public void setServer(Server server) {
		this.server = server;
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

	public File lookup(boolean ensureFileCreation, String fileName) {
		File file = findFile(server, ensureFileCreation, fileName);
		
		if (file == null) {
			// File does not exist so lets create 404 file not found code
			System.out.println("FILE DOES NOT EXIST FOR LOOKUP");
			this.response = new Response404NotFound(this.getVersion(), null);
		} else {
			// assume request is ok - change later if need be
			this.response = new Response200OK(this.getVersion(), file);
		}
		
		return file;
	}


	private File findFile(Server server, boolean ensureFileCreation, String fileName) {
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
		System.out.println("FILE - " + SpringUtilities.combine(rootDirectory, uri) + fileName);
		// Check if the file exists
		if (file.exists()) {
			System.out.println("FILE EXISTS");
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
			System.out.println("ENSURE FILE CREATION");
			// check if should create file
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return file;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.rosehulman.sws.protocol.IHTTPRequest#setCallback(edu.rosehulman.sws.server.Server, java.io.OutputStream, long)
	 */
	@Override
	public void setCallback(Server server, OutputStream outStream) {
		this.server = server;
		this.out = outStream;
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