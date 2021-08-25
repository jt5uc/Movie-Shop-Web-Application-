import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/api/shoppingchart")
public class scServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * handles POST requests to store session information
     */
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
       
        Long lastAccessTime = session.getLastAccessedTime();
        String customerid=(String)session.getAttribute("customerid");
        System.out.println(customerid);
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("sessionID", sessionId);
        responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());
        responseJsonObject.addProperty("customerid", customerid);
        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }*/

    /**
     * handles GET requests to add and show the item list information
     */
 
    
    
    
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String number = request.getParameter("number");
        String mid = request.getParameter("mid");
       
        response.setContentType("application/json"); // Response mime type
        HttpSession session = request.getSession();
        System.out.println("title: "+title);
        System.out.println("number: "+number);
        System.out.println("mid: "+mid);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", title);
        map.put("number", number);
        map.put("mid", mid);
        PrintWriter out = response.getWriter();
        
        ArrayList<Map> previousItems = (ArrayList<Map>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            previousItems.add(map);
            session.setAttribute("previousItems", previousItems);
        }else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
        	synchronized (previousItems) {
        	boolean changed=false;
        	Map<String, String> d=new HashMap<String, String>();;
        	for(Map m:previousItems)
        	{
        		if(m.get("mid").equals(mid))
        		{
        			if(number.equals("0"))
        				d=m;
        			else
        			{
        				m.put("number", number);
        				
        			}
        			changed=true;
        		}
        		
        	}
        	previousItems.remove(d);
        	if(changed==false)
        	{
        		previousItems.add(map);
        	}
        	}
        }
        
        JsonArray jsonArray = new JsonArray();
        for(Map m:previousItems)
        {
        	System.out.println("title is "+m.get("title"));
        	System.out.println("number is "+m.get("number"));
        	System.out.println("mid is "+m.get("mid"));
        	
        	 JsonObject jsonObject = new JsonObject();
        	 
        	 jsonObject.addProperty("title", (String)m.get("title"));
             jsonObject.addProperty("number", (String)m.get("number"));
             jsonObject.addProperty("mid", (String)m.get("mid"));
            
            
             jsonArray.add(jsonObject);
             
             
        }
     // write JSON string to output
        out.write(jsonArray.toString());
        // set response status to 200 (OK)
        response.setStatus(200);
        out.close();
        
        
        
        
        
        
        
        
        
        
       
    }
}
