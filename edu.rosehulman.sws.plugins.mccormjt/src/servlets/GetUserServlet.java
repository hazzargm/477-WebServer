package servlets;
 
import org.apache.commons.lang3.StringUtils;
 
import edu.rosehulman.sws.extension.AbstractServlet;
import edu.rosehulman.sws.extension.User;
 
public class GetUserServlet extends AbstractServlet {
      
       public GetUserServlet() {
             
       }
 
       public void serve() {
              String[] urlParts = StringUtils.split(this.request.getUri(), "/");
              String userId = urlParts[urlParts.length-1];
              System.out.println("GETTING: " + userId);
              User user = this.getPlugin().getUsers().get(userId);
             
              if (user == null) {
                     this.writer.write("<tr>");
                     this.writer.write("<td class='no-users'>Sorry, this user does not exist :(</td><td></td><td></td>");
                     this.writer.write("</tr>");
              } else {
                     String isOldClass = user.getAge() >= 100 ? "old" : "";
                     this.writer.write("<tr class='user " + isOldClass + "' id='" + user.getId() + "'>");
                     writeTableCellInput("name", user.getName());
                     writeTableCellInput("age", user.getAge() + "");
                     this.writer.write("<td class='delete'>X</td>");;
                     this.writer.write("</tr>");
              }
       }
      
       private void writeTableCellInput(String clazz, String value) {
              this.writer.write("<td>");
              this.writer.write("<input class='" + clazz + "' type='text' value='" + value + "'>");
              this.writer.write("</td>");
       }
 
}