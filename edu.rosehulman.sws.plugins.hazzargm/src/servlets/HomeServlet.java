package servlets;


import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.IServlet;

public class HomeServlet extends AbstractServlet implements IServlet {
	
	public HomeServlet() {
		
	}

	public void serve() {
		this.writer.write("<h1>Welcome to Gordon Hazzard's Home Page!</h1>");
		this.writer.write("<hr/>");
		this.writer.write("<h3>The man below has killed my sleep schedule...</h3>");
		this.writer.write("<img src='http://www.rose-hulman.edu/~rupakhet/web/images/Chandan-R-Rupakheti.jpg'/>");
	}
}
