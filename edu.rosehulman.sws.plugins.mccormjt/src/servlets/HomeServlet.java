package servlets;


import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class HomeServlet extends AbstractServlet implements IServlet {
	
	public HomeServlet() {
		
	}

	public void serve() {
		System.out.println("HOME SERVLET");
		this.writer.write("<h1>Welcome to John McCormack's Home Page!</h1>");
		this.writer.write("<hr/>");
		this.writer.write("<p>I cannot wait to graduate :)</p>");
	}
}
