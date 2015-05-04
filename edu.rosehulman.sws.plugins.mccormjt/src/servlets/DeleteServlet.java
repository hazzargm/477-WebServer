package servlets;


import java.util.Arrays;
import java.util.List;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class DeleteServlet extends AbstractServlet implements IServlet {
	
	public DeleteServlet() {
		
	}

	public void serve() {
		this.writer.write("Successfuly Deleted :)");
		List<String> urlParts = Arrays.asList(this.request.getUri().split("/"));
		String newUri = String.join("/", urlParts.subList(3, urlParts.size()));
		System.out.println("URI - " + newUri);
		this.request.setUri(newUri);
		this.request.handleRequest();
	}
}
