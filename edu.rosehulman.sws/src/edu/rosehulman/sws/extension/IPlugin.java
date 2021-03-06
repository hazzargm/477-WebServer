/*
 * IPlugin.java
 * Apr 30, 2015
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
 
package edu.rosehulman.sws.extension;

import java.util.Map;

import edu.rosehulman.sws.protocol.IHttpRequest;
import edu.rosehulman.sws.protocol.IHttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public interface IPlugin {
	public static String ROUTE_FILE_NAME = "routes.txt";
	
	public void route(IHttpRequest request, IHttpResponse response);
	public void loadServlets(String rootDir);
	public String getDomain();
	public IPlugin getPlugin();
	public Map<String, User> getUsers();
}
