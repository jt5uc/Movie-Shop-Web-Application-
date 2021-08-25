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
@WebServlet(name = "insertstarsevelet", urlPatterns = "/api/insertstar")
public class insertstarservelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("starname");
        String year = request.getParameter("birthyear");
      
        System.out.println("name is "+name);
        System.out.println("year is "+year);
       
        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        
            
           /** do database fetch here */ 
            try {
                // Get a connection from dataSource
            	 Connection dbcon = dataSource.getConnection();

                // Declare our statement
              

                String query = "select max(id) as id from stars;";
                
                PreparedStatement statement = dbcon.prepareStatement(query);
                
              
                // Perform the query
                ResultSet rs = statement.executeQuery();

             
                
                // Iterate through each row of rs
                int intid=0;
                while (rs.next()) {
                	String id = rs.getString("id");
                	
                	id= id.substring(2, id.length());
                	intid= Integer.valueOf(id);
                	intid++;
                	
                }
                	String idAsString = Integer.toString(intid);
                	idAsString="nm"+idAsString;
                	if(year=="")
                	{
                		String iquery = "INSERT INTO stars (id, name) VALUES('"+idAsString+"', '"+name+"');";
                		System.out.print(iquery);
                		PreparedStatement s = dbcon.prepareStatement(iquery);
        				s.executeUpdate();
                	}
                	else
                	{
                		String iquery = "INSERT INTO stars (id, name, birthYear) VALUES('"+idAsString+"', '"+name+"',"+ year +");";
                		System.out.print(iquery);
                		PreparedStatement s = dbcon.prepareStatement(iquery);
        				s.executeUpdate();
                	}
                	 
                	 
                	 
                  JsonObject responseJsonObject = new JsonObject();
                  responseJsonObject.addProperty("status", "success");
                  responseJsonObject.addProperty("message", "check out success");
  	            
                 
                  response.getWriter().write(responseJsonObject.toString());
                  System.out.println("reached here");
              /*} else {
            	  System.out.print("check out  falied!");
            	  JsonObject responseJsonObject = new JsonObject();
            	  responseJsonObject.addProperty("message", "incorrect credit card");
            	  response.getWriter().write(responseJsonObject.toString());
                  // Login fails
                  JsonObject responseJsonObject = new JsonObject();
                  responseJsonObject.addProperty("status", "fail");
                  if (typeUsername.equals("")) {
                      responseJsonObject.addProperty("message", "email " + username + " doesn't exist");
                  } else {
                      responseJsonObject.addProperty("message", "incorrect password");
                  }
                  response.getWriter().write(responseJsonObject.toString());
              }
                
         */
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
