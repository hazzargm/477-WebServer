package servlets;


import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class SecretServlet extends AbstractServlet implements IServlet {
	
	public SecretServlet() {
		this.setUsername("john");
		this.setPassword("s3cr3t");
	}

	public void serve() {
		this.writer.write("<h1>You have correctly authenticated for this secret page!</h1>");
		this.writer.write("<hr/>");
		this.writer.write("<p>The answer is 42!!!!!!</p>");
	}
}
