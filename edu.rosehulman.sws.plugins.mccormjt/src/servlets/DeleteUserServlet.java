package servlets;


import org.apache.commons.lang3.StringUtils;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class DeleteUserServlet extends AbstractServlet implements IServlet {
	
	public DeleteUserServlet() {
		
	}

	public void serve() {
		String[] urlParts = StringUtils.split(this.request.getUri(), "/");
		String userId = urlParts[urlParts.length-1];
		System.out.println("REMOVE: " + userId);
		this.getPlugin().getUsers().remove(userId);
		this.writer.write("success");
	}
}
