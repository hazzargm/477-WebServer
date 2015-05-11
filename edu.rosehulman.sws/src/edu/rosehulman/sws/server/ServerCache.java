/*
 * ServerCache.java
 * May 6, 2015
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

import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.protocol.IHttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerCache {
	private Map<String, IHttpResponse> cachedResponses;
	
	public ServerCache() {
		this.cachedResponses = new HashMap<String, IHttpResponse>();
	}
	
	public void cacheResponse(String method, String uri, IHttpResponse response) {
		// only cache static GET requests that have a set expiration time
		if (response.getExpiresAt() > -1 && method == Protocol.GET) {
			System.out.println("CACHED!!!! -- " + getServerCacheKey(method, uri));
			this.cachedResponses.put(getServerCacheKey(method, uri), response);
		}
	}
	
	public IHttpResponse getCachedResponse(String method, String uri) {
		String cacheKey = getServerCacheKey(method, uri);
		IHttpResponse response = cachedResponses.get(cacheKey);
		boolean hasExpired = response != null && Server.getCurrentTime() >= response.getExpiresAt();
		
		if (hasExpired) {
			cachedResponses.remove(cacheKey);
			response = null;
		}
		
		if (response != null) 
			System.out.println("GET CACHED -- " + hasExpired + " -------- " + Server.getCurrentTime() + " --------- " + response.getExpiresAt() + " ------- " + (Server.getCurrentTime() - response.getExpiresAt()));
		return response;
	}
	
	private String getServerCacheKey(String method, String uri) {
		return method + uri;
	}
}