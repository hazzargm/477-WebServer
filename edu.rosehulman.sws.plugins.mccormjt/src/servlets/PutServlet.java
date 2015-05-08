package servlets;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class PutServlet extends AbstractServlet implements IServlet {
	
	public PutServlet() {
		
	}

	public void serve() {
		List<String> urlParts = Arrays.asList(this.request.getUri().split("/"));
		String newUri = urlParts.get(1) + "/" + StringUtils.join(urlParts.subList(3, urlParts.size()), "/");
		this.request.setUri(newUri);
		this.request.handleRequest();
	}
}
