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
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import edu.rosehulman.sws.protocol.IHTTPRequest;
import edu.rosehulman.sws.protocol.IHTTPResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractPlugin implements IPlugin {
	
	private final String domain = ""; //This will be some constant in all conrete plugin implementations
	private Map<String, IServlet> servletMap;
	private String rootDir;
	
	public AbstractPlugin() {
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
	}
	
	public void process(IHTTPRequest request, IHTTPResponse response) {
		
	}
	
	public String getDomain() {
		return domain;
	}
	
	public IPlugin getPlugin() {
		return this;
	}
	
	public void route(IHTTPRequest request, IHTTPResponse response) {
		
	}


}
