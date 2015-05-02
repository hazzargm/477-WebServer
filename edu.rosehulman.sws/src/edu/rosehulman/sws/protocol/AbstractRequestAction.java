/*
 * AbstractRequestAction.java
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
import java.io.Reader;

import edu.rosehulman.sws.server.Server;

/**
 * 
 */
public abstract class AbstractRequestAction implements IRequestAction {
	protected IHttpResponse response;
	protected File file;
	protected char[] body;
	protected String uri;
	protected Server server;

	protected char[] getFileBody() { //TODO: currently this cuts off the last line of the body if it is a blank line
		String bodyString = charArrayToString(body, 0);
		System.out.println("##############" + bodyString + "##############\n");
		String[] lines = bodyString.split(System.getProperty("line.separator"));
		int index = 4;
		bodyString = "";
		System.out.println("LINES " + lines.length);
		while(index < lines.length -1 && !lines[index].startsWith(lines[0])) {
			if(index == lines.length - 2) { //The last line so don't add line separator
				bodyString += lines[index];
			} else {
				bodyString += lines[index] + System.getProperty("line.separator");
			}
			index++;
		}
//		System.out.println("$$$$$$$$$$$$$$");
//		System.out.print(bodyString);
//		System.out.println("$$$$$$$$$$$$$$");
		char[] fileBody = bodyString.toCharArray();
		return fileBody;
	}

	private String charArrayToString(char[] array, int start) {
		String s = "";
		for (int index = start; index < array.length; index++) {
			s += array[index];
		}
		return s;
	}
}
