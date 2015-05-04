package servlets;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class DeleteServlet extends AbstractServlet implements IServlet {
	
	public DeleteServlet() {
		
	}

	public void serve() {
		List<String> urlParts = Arrays.asList(this.request.getUri().split("/"));
		String newUri = urlParts.get(1) + "/" + StringUtils.join(urlParts.subList(3, urlParts.size()), "/");
		System.out.println("URI - " + newUri);
		this.request.setUri(newUri);
		this.request.handleRequest();
	}
}
