/*
 * IHTTPResponse.java
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
import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public interface IHttpResponse {

	public void write(OutputStream outStream) throws Exception;
	public void put(String key, String value);
	public void fillGeneralHeader();
	
	public String getVersion();
	public int getCode();
	public String getPhrase();
	public File getFile();
	public Map<String, String> getHeader();
	public boolean isError();
	public void setFile(File createTempFile);
	public long getExpiresAt();
	public void setExpiresAt(long time);
	public void setConnection(String connection);
}