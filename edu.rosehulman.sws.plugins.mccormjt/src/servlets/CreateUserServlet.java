package servlets;


import java.util.Map;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;
import edu.rosehulman.sws.extension.User;
import edu.rosehulman.sws.server.URLParser;

public class CreateUserServlet extends AbstractServlet implements IServlet {
	
	public CreateUserServlet() {
		
	}

	public void serve() {
		Map<String, String> postParams = URLParser.getPostParameters(this.request.getBody());
		User user = new User(postParams.get("name"), Integer.parseInt(postParams.get("age")));
		String id = postParams.get("id");
		if (id != "") {
			user.setId(id);
		}
		this.getPlugin().getUsers().put(user.getId(), user);
		this.writer.write("success");
	}
}
