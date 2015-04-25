/*
 * URLParser.java
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
 
package edu.rosehulman.sws.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.impl.HTTPRequests.DELRequest;
import edu.rosehulman.sws.impl.HTTPRequests.GETRequest;
import edu.rosehulman.sws.impl.HTTPRequests.POSTRequest;
import edu.rosehulman.sws.impl.HTTPRequests.PUTRequest;
import edu.rosehulman.sws.protocol.IHTTPRequest;
import edu.rosehulman.sws.protocol.IProtocol;
import edu.rosehulman.sws.protocol.ProtocolException;

/**
 * 
 */
public class URLParser {

	private static String method;
	private static String uri;
	private static String version;
	private static Map<String, String> header;
	private static char[] body;
	
	public static IHTTPRequest parseIncomingRequest(InputStream inputStream) throws Exception {
		// We will fill this object with the data from input stream and return it				
				InputStreamReader inStreamReader = new InputStreamReader(inputStream);
				BufferedReader reader = new BufferedReader(inStreamReader);
				
				//First Request Line: GET /somedir/page.html HTTP/1.1
				String line = reader.readLine(); // A line ends with either a \r, or a \n, or both
				
				if(line == null) {
					throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT); //TODO: determine what kind of error response to throw
				}
				
				// We will break this line using space as delimeter into three parts
				StringTokenizer tokenizer = new StringTokenizer(line, " ");
				
				// Error checking the first line must have exactly three elements
				if(tokenizer.countTokens() != 3) {
					throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT); //TODO: determine what kind of error response to throw
				}
				
				method = tokenizer.nextToken();		// GET
				uri = tokenizer.nextToken();		// /somedir/page.html
				version = tokenizer.nextToken();	// HTTP/1.1
				header = new HashMap<String, String>();

				// Rest of the request is a header that maps keys to values
				// e.g. Host: www.rose-hulman.edu
				// We will convert both the strings to lower case to be able to search later
				line = reader.readLine().trim();
				while(!line.equals("")) {
					// THIS IS A PATCH 
					// Instead of a string tokenizer, we are using string split
					// Lets break the line into two part with first space as a separator 
					line = line.trim(); // First lets trim the line to remove escape characters
					int index = line.indexOf(' ');// Now, get index of the first occurrence of space
					if(index > 0 && index < line.length()-1) {
						// Now lets break the string in two parts
						String key = line.substring(0, index); // Get first part, e.g. "Host:"
						String value = line.substring(index+1); // Get the rest, e.g. "www.rose-hulman.edu"
						// Lets strip off the white spaces from key if any and change it to lower case
						key = key.trim().toLowerCase();
						// Lets also remove ":" from the key
						key = key.substring(0, key.length() - 1);
						// Lets strip white spaces if any from value as well
						value = value.trim();
						// Now lets put the key=>value mapping to the header map
						header.put(key, value);
					}
					// Processed one more line, now lets read another header line and loop
					line = reader.readLine().trim();
				}
				
				int contentLength = 0;
				try {
					contentLength = Integer.parseInt(header.get(Protocol.CONTENT_LENGTH.toLowerCase()));
				}
				catch(Exception e){}
				if(contentLength > 0) {
					body = new char[contentLength];
					reader.read(body);
				}
				
				return generateRequest();			
	}
	
	private static IHTTPRequest generateRequest() {
		switch(method) {
			case Protocol.GET: return new GETRequest(uri, version, header);
			case Protocol.DELETE: return new DELRequest();
			case Protocol.POST: return new POSTRequest(uri, version, header);
			case Protocol.PUT: return new PUTRequest();
			default: return null;
		}		
	}
}
