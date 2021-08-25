import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.PreparedStatement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/logins")
public class LoginServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String typeUsername = "";
        String typePassword="";
        String customerid="";
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        System.out.print("sdasd");
        
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            System.out.println("verift failed");
            return;
        }
        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        
            
           /** do database fetch here */ 
            try {
                // Get a connection from dataSource
            	 Connection dbcon = dataSource.getConnection();

                // Declare our statement
              

                String query = "SELECT email, password, id \n" + 
                		"FROM customers\n" + 
                		"WHERE email=?;";

                PreparedStatement statement = dbcon.prepareStatement(query);
                
                statement.setString(1, username);
                //System.out.print(query);
                // Perform the query
                System.out.print("aa");
                ResultSet rs = statement.executeQuery();
                System.out.print(rs);
             

                // Iterate through each row of rs
                boolean success =false;
                while (rs.next()) {
                	System.out.print("yoyoyoyo");
                	typeUsername = rs.getString("email");
        			typePassword = rs.getString("password");
        			System.out.print(typeUsername);
        			System.out.print(typePassword);
        			customerid=rs.getString("id");
        			success = new StrongPasswordEncryptor().checkPassword(password, typePassword);
        			System.out.print("this thssssss  "+success);
        		
                }
                
                if (username.equals(typeUsername) && success) {
                      // Login succeeds
                      // Set this user into current session
                	System.out.print("yeah success !");
                      String sessionId = ((HttpServletRequest) request).getSession().getId();
                      Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
                      request.getSession().setAttribute("user", new User(username));
                      request.getSession().setAttribute("customerid", customerid);
                  JsonObject responseJsonObject = new JsonObject();
                  responseJsonObject.addProperty("status", "success");
                  responseJsonObject.addProperty("message", "success");
                 
                  response.getWriter().write(responseJsonObject.toString());
              } else {
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
