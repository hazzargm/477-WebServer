package edu.rosehulman.sws.to_delete;
///*
// * WriteHandler.java
// * Apr 23, 2015
// *
// * Simple Web Server (SWS) for EE407/507 and CS455/555
// * 
// * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
// * 
// * This program is free software: you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public License 
// * as published by the Free Software Foundation, either 
// * version 3 of the License, or any later version.
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// * You should have received a copy of the GNU Lesser General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
// * 
// * Contact Us:
// * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
// * Department of Electrical and Computer Engineering
// * Clarkson University
// * Potsdam
// * NY 13699-5722
// * http://clarkson.edu/~rupakhcr
// */
// 
//package edu.rosehulman.sws.impl;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.FileInputStream;
//import java.io.OutputStream;
//import java.util.Map;
//
//import edu.rosehulman.sws.protocol.IHTTPResponse;
//
///**
// * 
// */
//public class WriteHandler {
//	
//	private OutputStream outStream;
//	private long startTime;
//
//	public WriteHandler(OutputStream outStream, long startTime){
//		this.outStream = outStream;
//		this.startTime = startTime;
//	}
//	
//	public void write(IHTTPResponse response) {
//		BufferedOutputStream out = new BufferedOutputStream(outStream, Protocol.CHUNK_LENGTH);
//
//		// First status line
//		String line = response.version + Protocol.SPACE + response.status + Protocol.SPACE + response.phrase + Protocol.CRLF;
//		out.write(line.getBytes());
//		
//		// Write header fields if there is something to write in header field
//		if(response.header != null && !response.header.isEmpty()) {
//			for(Map.Entry<String, String> entry : response.header.entrySet()) {
//				String key = entry.getKey();
//				String value = entry.getValue();
//				
//				// Write each header field line
//				line = key + Protocol.SEPERATOR + Protocol.SPACE + value + Protocol.CRLF;
//				out.write(line.getBytes());
//			}
//		}
//
//		// Write a blank line
//		out.write(Protocol.CRLF.getBytes());
//
//		// We are reading a file
//		if(response.getStatus() == Protocol.OK_CODE && response.file != null) {
//			// Process text documents
//			FileInputStream fileInStream = new FileInputStream(response.file);
//			BufferedInputStream inStream = new BufferedInputStream(fileInStream, Protocol.CHUNK_LENGTH);
//			
//			byte[] buffer = new byte[Protocol.CHUNK_LENGTH];
//			int bytesRead = 0;
//			// While there is some bytes to read from file, read each chunk and send to the socket out stream
//			while((bytesRead = inStream.read(buffer)) != -1) {
//				out.write(buffer, 0, bytesRead);
//			}
//			// Close the file input stream, we are done reading
//			inStream.close();
//		}
//		
//		// Flush the data so that outStream sends everything through the socket 
//		out.flush();
//	}
//
//}
