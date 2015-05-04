package servlets;


import java.util.Map;

import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;
import edu.rosehulman.sws.server.URLParser;

public class FormResultServlet extends AbstractServlet implements IServlet {
	
	public FormResultServlet() {
		
	}

	public void serve() {
		Map<String, String> postParams = URLParser.getPostParameters(this.request.getBody());
		
		
		this.writer.write("<h1>Hello" + postParams.get("name") + "</h1>");
		this.writer.write("<hr/>");
		
		int hours = Integer.parseInt(postParams.get("hours"));
		String message;;
		if (hours > 5) {
			message = "You got " + (hours - 5) + "more hours of sleep than I did!";
		} else {
			message = "I got " + (hours - 5) + "more hours of sleep than you did!";
		}
		this.writer.write("<p>" + message + "</p>");
	}
}
