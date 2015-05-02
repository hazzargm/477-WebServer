/*
 * AbstractServlet.java
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

import java.io.PrintWriter;

import edu.rosehulman.sws.impl.Protocol;
import edu.rosehulman.sws.protocol.IHttpRequest;
import edu.rosehulman.sws.protocol.IHttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public abstract class AbstractServlet implements IServlet {
	protected IHttpRequest request;
	protected IHttpResponse response;
	protected PrintWriter writer;
	
	public void process(IHttpRequest request, IHttpResponse response) {
        response.put(Protocol.CONTENT_TYPE, ("text/html;charset=UTF-8"));
        this.writer = new PrintWriter(request.getClientOutputStream());
    }
	
	

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(IHttpRequest request, IHttpResponse response) {
        process(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(IHttpRequest request, IHttpResponse response) {
        process(request, response);
    }
    
    /**
     * Handles the HTTP <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPut(IHttpRequest request, IHttpResponse response) {
        process(request, response);
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doDel(IHttpRequest request, IHttpResponse response) {
        process(request, response);
    }

}
