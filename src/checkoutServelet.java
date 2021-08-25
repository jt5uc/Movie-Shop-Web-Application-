import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.PreparedStatement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "checkoutServelet", urlPatterns = "/api/checkoutss")
public class checkoutServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cardnumber = request.getParameter("cardnumber");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String expiredate = request.getParameter("expiredate");
        String typeUsername = "";
        String typePassword="";
        String customerid="";
        System.out.println("card is "+cardnumber);
        System.out.println("firstname is "+firstname);
        System.out.println("lastname is "+lastname);
        System.out.println("expiredate is "+expiredate);
       
        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        
            
           /** do database fetch here */ 
            try {
                // Get a connection from dataSource
            	 Connection dbcon = dataSource.getConnection();

                // Declare our statement
              

                String query = "SELECT * FROM creditcards\n" + 
                		"WHERE id=? AND firstName=?\n" + 
                		"AND lastName=? AND expiration=?;";

                PreparedStatement statement = dbcon.prepareStatement(query);
                
                statement.setString(1, cardnumber);
                statement.setString(2, firstname);
                statement.setString(3, lastname);
                statement.setString(4, expiredate);
                // Perform the query
                ResultSet rs = statement.executeQuery();

             

                // Iterate through each row of rs
               
                if (rs.next()) {
                	System.out.print("check out  success!");
                      // Login succeeds
                      // Set this user into current session
                	 HttpSession session = request.getSession();
                	 Date date = new Date();
                	 String modifiedDate= new SimpleDateFormat("yyyy/MM/dd").format(date);
                	 System.out.println(modifiedDate);
                	 String cid=(String)session.getAttribute("customerid");
                	 System.out.println(cid);
                	 
                	 ArrayList<Map> previousItems = (ArrayList<Map>) session.getAttribute("previousItems");
                	 for(Map m:previousItems)
                 	{
                		 System.out.println(m.get("number"));
                		 System.out.println(m.get("mid"));
                		 
                		 String query1="INSERT INTO sales (customerId, movieId, saleDate)VALUES("+
                 	cid+","+"\""+m.get("mid")+"\","+"\""+modifiedDate+"\");";
                		 int quant = Integer.parseInt((String) m.get("number"));
                		 System.out.println(query1);
                		
                			for(int i=0; i<quant; i++)
                			{
                				PreparedStatement s = dbcon.prepareStatement(query1);
                				s.executeUpdate();
                				System.out.println("excuted ");
                			}
                		 
                 	}
                	 
                  JsonObject responseJsonObject = new JsonObject();
                  responseJsonObject.addProperty("status", "success");
                  responseJsonObject.addProperty("message", "check out success");
  	            
                 
                  response.getWriter().write(responseJsonObject.toString());
                  System.out.println("reached here");
              } else {
            	  System.out.print("check out  falied!");
            	  JsonObject responseJsonObject = new JsonObject();
            	  responseJsonObject.addProperty("message", "incorrect credit card");
            	  response.getWriter().write(responseJsonObject.toString());
                  // Login fails
                  /*JsonObject responseJsonObject = new JsonObject();
                  responseJsonObject.addProperty("status", "fail");
                  if (typeUsername.equals("")) {
                      responseJsonObject.addProperty("message", "email " + username + " doesn't exist");
                  } else {
                      responseJsonObject.addProperty("message", "incorrect password");
                  }
                  response.getWriter().write(responseJsonObject.toString());*/
              }
                
         
                // set response status to 200 (OK)
                response.setStatus(200);

                rs.close();
                statement.close();
                dbcon.close();
            } catch (Exception e) {
            	
    			// write error message JSON object to output
    			JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("errorMessage", e.getMessage());
       			// set reponse status to 500 (Internal Server Error)
    			response.setStatus(500);

            }
  
      
    }
}
