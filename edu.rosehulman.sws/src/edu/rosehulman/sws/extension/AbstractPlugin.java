/*
 * AbstractPlugin.java
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import edu.rosehulman.sws.impl.HTTPResponses.Response200OK;
import edu.rosehulman.sws.protocol.IHttpRequest;
import edu.rosehulman.sws.protocol.IHttpResponse;
import edu.rosehulman.sws.server.URLParser;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractPlugin implements IPlugin {
	
	private final String domain = ""; //This will be some constant in all conrete plugin implementations
	private Map<String, IServlet> servletMap;
	private String rootDir;
	
	public static String getServeltRouteKey(String method, String uri) {
		return (method + uri).toLowerCase();
	}
	
	public void loadServlets(String rootDir) {
		this.rootDir = rootDir;
		// lookup route file for specific plugin domain
		String routeFilePath = rootDir + File.separator + getDomain() + IPlugin.ROUTE_FILE_NAME;
		File routeFile = new File(routeFilePath);
		this.servletMap = new HashMap<String, IServlet>();
		 
		try {
			// load each line entry of route file into map
			BufferedReader br = new BufferedReader(new FileReader(routeFile));  
			String line = null;
			while ((line = br.readLine()) != null)  
			{  
				parseRouteLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void parseRouteLine(String line) {
		// split line on any amount of white space in between parts
		String[] routeParts = line.split("\\s+");
		String routeKey = getServeltRouteKey(routeParts[0], routeParts[1]);
		String routeServletClass = routeParts[2];
		
		// create new servlet instance frome routeServlet name
		Class<?> servletClass;
		try {
			servletClass = Class.forName(routeServletClass);
			Constructor<?> servletConstructor = servletClass.getConstructor(String.class);
			IServlet servlet = (IServlet) servletConstructor.newInstance();
			this.servletMap.put(routeKey, servlet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDomain() {
		return domain;
	}
	
	public IPlugin getPlugin() {
		return this;
	}
	
	public void route(IHttpRequest request, IHttpResponse response) {
		String servletName = URLParser.getServletDomain(request.getUri());
		IServlet s = servletMap.get(servletName);
		if(s != null) {
			s.process(request, new Response200OK());
		} else {
			// TODO Return whatever error response for accesses a bad servlet (404 not found?)
		}
	}


}
