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
		
		this.writer.write("<h1>Hello" + postParams.get("name") + ", because of your age...</h1>");
		this.writer.write("<hr/>");
		
		int age = Integer.parseInt(postParams.get("age"));
		String message;;
		if (age > 70) {
			message = "you're going to die fairly soon :/";
		} else {
			message = "you have a bright young future ahead of you!!!!!";
		}
		this.writer.write("<p>" + message + "</p>");
	}
}
