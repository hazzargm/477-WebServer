package servlets;


import java.util.Collection;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;
import edu.rosehulman.sws.extension.User;

public class AllUsersServlet extends AbstractServlet implements IServlet {
	
	public AllUsersServlet() {
		
	}

	public void serve() {
		Collection<User> allUsers = this.getPlugin().getUsers().values();
		for (User user : allUsers) {
			String isOldClass = user.getAge() >= 100 ? "old" : "";
			this.writer.write("<tr class='user " + isOldClass + "' id='" + user.getId() + "'>");
			writeTableCellInput("name", user.getName());
			writeTableCellInput("age", user.getAge() + "");
			this.writer.write("<td class='delete'>X</td>");;
			this.writer.write("</tr>");
		}
		
		if (allUsers.size() < 1) {
			this.writer.write("<tr>");
			this.writer.write("<td class='no-users'>Sorry, no users :(</td><td></td><td></td>");
			this.writer.write("</tr>");
		}
	}
	
	private void writeTableCellInput(String clazz, String value) {
		this.writer.write("<td>");
		this.writer.write("<input class='" + clazz + "' type='text' value='" + value + "'>");
		this.writer.write("</td>");
	}
}
