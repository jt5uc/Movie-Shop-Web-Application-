
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// server endpoint URL
@WebServlet("/autocompletes")

public class autocomplete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	 // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	/*
	 * populate the Super hero hash map.
	 * Key is hero ID. Value is hero name.
	 */
	public static HashMap<Integer, String> superHeroMap = new HashMap<>();
	
	/*static {
		superHeroMap.put(1, "Blade");
		superHeroMap.put(2, "Ghost Rider");
		superHeroMap.put(3, "Luke Cage");
		superHeroMap.put(4, "Silver Surfer");
		superHeroMap.put(5, "Beast");
		superHeroMap.put(6, "Thing");
		superHeroMap.put(7, "Black Panther");
		superHeroMap.put(8, "Invisible Woman");
		superHeroMap.put(9, "Nick Fury");
		superHeroMap.put(10, "Storm");
		superHeroMap.put(11, "Iron Man");
		superHeroMap.put(12, "Professor X");
		superHeroMap.put(13, "Hulk");
		superHeroMap.put(14, "Cyclops");
		superHeroMap.put(15, "Thor");
		superHeroMap.put(16, "Jean Grey");
		superHeroMap.put(17, "Wolverine");
		superHeroMap.put(18, "Daredevil");
		superHeroMap.put(19, "Captain America");
		superHeroMap.put(20, "Spider-Man");

		superHeroMap.put(101, "Superman");
		superHeroMap.put(102, "Batman");
		superHeroMap.put(103, "Wonder Woman");
		superHeroMap.put(104, "Flash");
		superHeroMap.put(105, "Green Lantern");
		superHeroMap.put(106, "Catwoman");
		superHeroMap.put(107, "Nightwing");
		superHeroMap.put(108, "Captain Marvel");
		superHeroMap.put(109, "Aquaman");
		superHeroMap.put(110, "Green Arrow");
		superHeroMap.put(111, "Martian Manhunter");
		superHeroMap.put(112, "Batgirl");
		superHeroMap.put(113, "Supergirl");
		superHeroMap.put(114, "Black Canary");
		superHeroMap.put(115, "Hawkgirl");
		superHeroMap.put(116, "Cyborg");
		superHeroMap.put(117, "Robin");
	}
    */
    public autocomplete() {
        super();
    }

    /*
     * 
     * Match the query against superheroes and return a JSON response.
     * 
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     * 	{ "value": "Superman", "data": { "heroID": 101 } },
     * 	{ "value": "Supergirl", "data": { "heroID": 113 } }
     * ]
     * 
     * The format is like this because it can be directly used by the 
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *   
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     * 
     * 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json"); // Response mime type
		String input = request.getParameter("query");
		PrintWriter out = response.getWriter();
		 try {
			// setup the response json arrray
				JsonArray jsonArray = new JsonArray();
				
				// get the query string from parameter
				// return the empty json array if query is null or empty
				if (input == null || input.trim().isEmpty()) {
					response.getWriter().write(jsonArray.toString());
					return;
				}	
				else
				{
					
					String[] splited = input.split("\\s+");
		        	String items="";
		        	for(String s: splited)
		        	{
		        		items+="+"+s+"*,";
		        	}
		        	items=items.substring(0, items.length()-1);
		        	
		        	
		        	String query=
		        			"SELECT title,id  FROM movies WHERE MATCH(title) \n" + 
		        			"		AGAINST('"+items+"' IN BOOLEAN MODE) LIMIT 10;";
		        	
					
					
					// Get a connection from dataSource
					Connection dbcon = dataSource.getConnection();
					
					// Declare our statement
					Statement statement = dbcon.createStatement();
					 // Perform the query
		            ResultSet rs = statement.executeQuery(query);
		           
		            while (rs.next()) {
		            	String title = rs.getString("title");
		            	String id = rs.getString("id");
		            	jsonArray.add(generateJsonObject(id, title));
		               
		            }
		            
		            response.getWriter().write(jsonArray.toString());
		            response.setStatus(200);

		            rs.close();
		            statement.close();
		            dbcon.close();
					return;
		            

	            
				}
			}
		 catch (Exception e) {
	        	
				// write error message JSON object to output
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("errorMessage", e.getMessage());
				out.write(jsonObject.toString());

				// set reponse status to 500 (Internal Server Error)
				response.setStatus(500);

	        }
	        out.close();
		
		
		
		
		
		
		
		
		//System.out.println(input);
		/*try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			// search on superheroes and add the results to JSON Array
			// this example only does a substring match
			// TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars
			
			for (Integer id : superHeroMap.keySet()) {
				String heroName = superHeroMap.get(id);
				if (heroName.toLowerCase().contains(query.toLowerCase())) {
					jsonArray.add(generateJsonObject(id, heroName));
				}
			}
			
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}*/
	}
	
	/*
	 * Generate the JSON Object from hero to be like this format:
	 * {
	 *   "value": "Iron Man",
	 *   "data": { "heroID": 11 }
	 * }
	 * 
	 */
	private static JsonObject generateJsonObject(String heroID, String heroName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", heroName);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("ID", heroID);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
