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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rosehulman.sws.impl.HTTPResponses.Response200OK;
import edu.rosehulman.sws.protocol.IHttpRequest;
import edu.rosehulman.sws.protocol.IHttpResponse;
import edu.rosehulman.sws.server.Server;
import edu.rosehulman.sws.server.URLParser;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractPlugin implements IPlugin {
	
	protected String domain = ""; //This will be some constant in all concrete plugin implementations
	protected Map<String, IServlet> servletMap;
	protected String servletDir;
	
	public AbstractPlugin() {
		loadServlets("servlets");
	}
	
	public static String getServeltRouteKey(String method, String uri) {
		return (method + uri).toLowerCase();
	}
	
	public void loadServlets(String servletDir) {
		this.servletDir = servletDir;
		System.out.println("LOAD SERVLETS");
		
		// lookup route file for specific plugin domain
		InputStream in = getClass().getResourceAsStream(IPlugin.ROUTE_FILE_NAME); 
		this.servletMap = new HashMap<String, IServlet>();
		 
		try {
			// load each line entry of route file into map
			BufferedReader br = new BufferedReader(new InputStreamReader(in));  
			String line = null;
			while ((line = br.readLine()) != null)  
			{  
				System.out.println("ROUTE LOADED: " + line);
				parseRouteLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String s: this.servletMap.keySet()) {
			System.out.println("KEY: " + s + " - " + servletMap.get(s).getClass().toString());
		}
	}
	
	private void parseRouteLine(String line) {
		// split line on any amount of white space in between parts
		String[] routeParts = line.split("\\s+");
		String path = routeParts[0].replaceAll(".*[/\\\\].*", File.separator);
		String routeKey = getServeltRouteKey(path, routeParts[1]);
		String routeServletClass = routeParts[2];
		
		
		// create new servlet instance frome routeServlet name
		Class<?> servletClass;
		try {
			URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
			ClassLoader classLoader = new URLClassLoader(new URL[]{new File(location.getFile()).toURI().toURL()});
			servletClass = classLoader.loadClass(routeServletClass);
			Constructor<?> servletConstructor = servletClass.getConstructor();
			Object servletObj = servletConstructor.newInstance();
			IServlet servlet = (IServlet) servletObj;
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
		String servletMapKey = getServeltRouteKey(request.getMethod(), servletName);
		IServlet s = findServlet(servletMapKey);

		if(s != null) {
			System.out.println("FOUND SERVLET = " + servletMapKey);
			s.process(request, new Response200OK());
		} else {
			System.out.println("SERVLET NOT FOUND!!! - "+ servletMapKey);
			System.out.println("LOOKING FOR static resource");
			request.handleRequest();
		}
	}

	
	private IServlet findServlet(String servletKey) {
		List<String> keyParts = Arrays.asList(servletKey.split("/"));
		IServlet servlet = null;
		for (int i = keyParts.size(); i > 0; i--) {
			String key = String.join("/", keyParts.subList(0, i));
			System.out.println("KEY CHECK: " + key);
			servlet = this.servletMap.get(key);
			if (servlet != null) break;
		}
		return servlet;
	}

}
