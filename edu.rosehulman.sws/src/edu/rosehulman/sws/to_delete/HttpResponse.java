package edu.rosehulman.sws.to_delete;
///*
// * HttpResponse.java
// * Oct 7, 2012
// *
// * Simple Web Server (SWS) for CSSE 477
// * 
// * Copyright (C) 2012 Chandan Raj Rupakheti
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
// */
// 
//package edu.rosehulman.sws.impl;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.OutputStream;
//import java.util.Collections;
//import java.util.Map;
//
//import edu.rosehulman.sws.protocol.IHTTPResponse;
//
///**
// * Represents a response object for HTTP.
// * 
// * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
// */
//public class HttpResponse implements IHTTPResponse {
//	private String version;
//	private int status;
//	private String phrase;
//	private Map<String, String> header;
//	private File file;
//
//	
//	/**
//	 * Constructs a HttpResponse object using supplied parameter
//	 * 
//	 * @param version The http version.
//	 * @param status The response status.
//	 * @param phrase The response status phrase.
//	 * @param header The header field map.
//	 * @param file The file to be sent.
//	 */
//	public HttpResponse(String version, int status, String phrase, Map<String, String> header, File file) {
//		this.version = version;
//		this.status = status;
//		this.phrase = phrase;
//		this.header = header;
//		this.file = file;
//	}
//
//
//	
//	/**
//	 * Writes the data of the http response object to the output stream.
//	 * 
//	 * @param outStream The output stream
//	 * @throws Exception
//	 */
//	public void write(OutputStream outStream) throws Exception {
////		BufferedOutputStream out = new BufferedOutputStream(outStream, Protocol.CHUNK_LENGTH);
////
////		// First status line
////		String line = this.version + Protocol.SPACE + this.status + Protocol.SPACE + this.phrase + Protocol.CRLF;
////		out.write(line.getBytes());
////		
////		// Write header fields if there is something to write in header field
////		if(header != null && !header.isEmpty()) {
////			for(Map.Entry<String, String> entry : header.entrySet()) {
////				String key = entry.getKey();
////				String value = entry.getValue();
////				
////				// Write each header field line
////				line = key + Protocol.SEPERATOR + Protocol.SPACE + value + Protocol.CRLF;
////				out.write(line.getBytes());
////			}
////		}
////
////		// Write a blank line
////		out.write(Protocol.CRLF.getBytes());
////
////		// We are reading a file
////		if(this.status == Protocol.OK_CODE && file != null) {
////			// Process text documents
////			FileInputStream fileInStream = new FileInputStream(file);
////			BufferedInputStream inStream = new BufferedInputStream(fileInStream, Protocol.CHUNK_LENGTH);
////			
////			byte[] buffer = new byte[Protocol.CHUNK_LENGTH];
////			int bytesRead = 0;
////			// While there is some bytes to read from file, read each chunk and send to the socket out stream
////			while((bytesRead = inStream.read(buffer)) != -1) {
////				out.write(buffer, 0, bytesRead);
////			}
////			// Close the file input stream, we are done reading
////			inStream.close();
////		}
////		
////		// Flush the data so that outStream sends everything through the socket 
////		out.flush();
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.rosehulman.sws.protocol.IHTTPResponse#fillGeneralHeader(java.lang.String)
//	 */
//	@Override
//	public void fillGeneralHeader(String close) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//	/* (non-Javadoc)
//	 * @see edu.rosehulman.sws.protocol.IHTTPResponse#put(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void put(String contentType, String mime) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}
