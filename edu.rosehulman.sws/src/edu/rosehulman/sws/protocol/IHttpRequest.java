/*
 * IHTTPRequest.java
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
import java.io.OutputStream;
import java.util.Map;

import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public interface IHttpRequest {	
	public abstract void handleRequest();
	public File lookup(boolean ensureFileCreation, String filename);
	public void setCallback(Server server, OutputStream outStream, long start);
	
	public String getMethod();
	public String getUri();
	public void setUri(String uri);
	public String getVersion();
	public char[] getBody();
	public Map<String, String> getHeader();	
	public OutputStream getClientOutputStream();
	public Server getServer();
	public void setServer(Server server);
	public long getStart();
}
