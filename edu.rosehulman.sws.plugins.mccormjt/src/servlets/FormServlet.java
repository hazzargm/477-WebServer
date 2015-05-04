package servlets;


import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class FormServlet extends AbstractServlet implements IServlet {
	
	public FormServlet() {
		
	}

	public void serve() {
		this.writer.write("<h1>Welcome to John McCormack's Home Page!</h1>");
		this.writer.write("<hr/>");
		this.writer.write("<p>I cannot wait to graduate :)</p>");
	}
}
