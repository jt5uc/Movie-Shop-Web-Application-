import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "metaservelet", urlPatterns = "/api/meta")
public class metaservelet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type
		
		//###################################

        String filename ="Desktop:ss.csv";
        try {
            FileWriter fw = new FileWriter(filename);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb", "root", "asdrtyjkl4110");
            String query = "select * from movies";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int cols = rs.getMetaData().getColumnCount();

            for(int i = 1; i <= cols; i ++){
               fw.append(rs.getMetaData().getColumnLabel(i));
               if(i < cols) fw.append(',');
               else fw.append('\n');
            }

            while (rs.next()) {

               for(int i = 1; i <= cols; i ++){
                   fw.append(rs.getString(i));
                   if(i < cols) fw.append(',');
               }
               fw.append('\n');
           }
            fw.flush();
            fw.close();
            conn.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //#################################
		
		
		
		PrintWriter out = response.getWriter();
		
		try {
			// Get a connection from dataSource
			Connection dbcon = dataSource.getConnection();
			ArrayList<String> tables= new ArrayList<String>();
			String getTables="show tables;";
			PreparedStatement statement_gtables = dbcon.prepareStatement(getTables);
			ResultSet tableset = statement_gtables.executeQuery();
			while (tableset.next()) {
				String tablename= tableset.getString("Tables_in_moviedb");
				tables.add(tablename);
				
			}
			PreparedStatement statement_tablemeta=null;
			ResultSet metaset=null;
			JsonArray jsonArray = new JsonArray();
			for(String s: tables)
			{
				
				
				String metaquery="describe "+s+";";
			//	System.out.println(metaquery);
				
				
				 statement_tablemeta = dbcon.prepareStatement(metaquery);
				 metaset = statement_tablemeta.executeQuery();
				while (metaset.next()) {
					String field= metaset.getString("Field");
					String type= metaset.getString("Type");
					System.out.println(field);
					System.out.println(type);
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("tablename", s);
					jsonObject.addProperty("field", field);
					jsonObject.addProperty("type", type);
					jsonArray.add(jsonObject);
				}
				
			}
			
			// Construct a query with parameter represented by "?"
			/*String query = "SELECT movies.id AS mvovie_id, title, year, director, rating,  genres.name AS genre,  stars.name AS name, birthyear\n" + 
					" FROM ratings, movies, genres_in_movies,genres,stars_in_movies,stars\n" + 
					"    WHERE  stars_in_movies.starId= stars.id AND\n" + 
					"    stars_in_movies.movieId= movies.id AND\n" + 
					"    genres_in_movies.genreId=genres.id AND\n" + 
					"    genres_in_movies.movieId=movies.id AND\n" + 
					"    ratings.movieId=movies.id  AND \n" + 
					"    title=?;";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, t);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String mvovie_id = rs.getString("mvovie_id");
				String title = rs.getString("title");
				String year = rs.getString("year");
				String director = rs.getString("director");
				String genre = rs.getString("genre");
				String name = rs.getString("name");
				String rating = rs.getString("rating");
				String birthyear = rs.getString("birthyear");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movie_id", mvovie_id);
				jsonObject.addProperty("title", title);
				jsonObject.addProperty("year", year);
				jsonObject.addProperty("director", director);
				jsonObject.addProperty("genre", genre);
				jsonObject.addProperty("name", name);
				jsonObject.addProperty("rating", rating);
				jsonObject.addProperty("birthyear", birthyear);
				

				jsonArray.add(jsonObject);
			}*/
			
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            metaset.close();
            tableset.close();
            statement_gtables.close();
            statement_tablemeta.close();
			dbcon.close();
		} catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();
}
}

