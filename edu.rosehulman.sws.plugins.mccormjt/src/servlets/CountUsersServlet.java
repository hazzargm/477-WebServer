package servlets;


import java.util.Collection;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;
import edu.rosehulman.sws.extension.User;

public class CountUsersServlet extends AbstractServlet implements IServlet {
	
	public CountUsersServlet() {
		
	}

	public void serve() {
		Collection<User> allUsers = this.getPlugin().getUsers().values();
		this.writer.write(allUsers.size() + "");
	}
}
