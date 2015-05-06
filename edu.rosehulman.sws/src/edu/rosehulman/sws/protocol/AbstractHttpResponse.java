/*
 * AbstractHTTPResponse.java
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import edu.rosehulman.sws.impl.Protocol;

/**
 * 
 */
public abstract class AbstractHttpResponse implements IHttpResponse {

	protected String version;
	protected int code;
	protected String phrase;
	protected Map<String, String> header;
	protected File file;
	protected long expiresAt = -1;
	
	public static File createTempResponseFile() {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("response", "temp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile;
	}
	
	/**
	 * Set time response becomes stale and expires
	 * 
	 */
	public void setExpiresAt(long time) {
		this.expiresAt = time;
	}
	
	/**
	 * Get the time response becomes stale and expires
	 * 
	 */
	public long getExpiresAt() {
		return expiresAt;
	}
	
	/**
	 * Set the response file
	 * 
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Tells if code is error.
	 * 
	 * @return is error
	 */
	public boolean isError() {
		return code >= 400;
	}
	
	/**
	 * Gets the version of the HTTP.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the status code of the response object.
	 * @return the status
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets the status phrase of the response object.
	 * 
	 * @return the phrase
	 */
	public String getPhrase() {
		return phrase;
	}
	
	/**
	 * The file to be sent.
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the header fields associated with the response object.
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		// Lets return the unmodifable view of the header map
		return Collections.unmodifiableMap(header);
	}

	/**
	 * Maps a key to value in the header map.
	 * @param key A key, e.g. "Host"
	 * @param value A value, e.g. "www.rose-hulman.edu"
	 */
	public void put(String key, String value) {
		this.header.put(key, value);
	}
		
	/**
	 * @param out
	 */
	protected void writeGenericHeader(BufferedOutputStream out) {
		// First status line
		String line = version + Protocol.SPACE + code + Protocol.SPACE + phrase + Protocol.CRLF;
		try {
			out.write(line.getBytes());		
			// Write header fields if there is something to write in header field
			if(header != null && !header.isEmpty()) {
				for(Map.Entry<String, String> entry : header.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					
					// Write each header field line
					line = key + Protocol.SEPERATOR + Protocol.SPACE + value + Protocol.CRLF;
					out.write(line.getBytes());
				}
			}
		
			// Write a blank line
			out.write(Protocol.CRLF.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fillGeneralHeader(String connection) {
		// Lets add Connection header
		put(Protocol.CONNECTION, connection);

		// Lets add current date
		Date date = Calendar.getInstance().getTime();
		put(Protocol.DATE, date.toString());
		
		// Lets add server info
		put(Protocol.Server, Protocol.getServerInfo());

		// Lets add extra header with provider info
		put(Protocol.PROVIDER, Protocol.AUTHOR);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------------------------------\n");
		buffer.append(this.version);
		buffer.append(Protocol.SPACE);
		buffer.append(this.code);
		buffer.append(Protocol.SPACE);
		buffer.append(this.phrase);
		buffer.append(Protocol.LF);
		
		for(Map.Entry<String, String> entry : this.header.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(Protocol.SEPERATOR);
			buffer.append(Protocol.SPACE);
			buffer.append(entry.getValue());
			buffer.append(Protocol.LF);
		}
		
		buffer.append(Protocol.LF);
		if(file != null) {
			buffer.append("Data: ");
			buffer.append(this.file.getAbsolutePath());
		}
		buffer.append("\n----------------------------------\n");
		return buffer.toString();
	}
}