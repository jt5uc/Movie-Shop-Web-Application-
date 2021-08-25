import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class singleMovieServlet extends HttpServlet {
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

		// Retrieve parameter id from url request.
		String t = request.getParameter("title");
		System.out.println("NAME here: cnm "+t);

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			// Get a connection from dataSource
			Connection dbcon = dataSource.getConnection();

			// Construct a query with parameter represented by "?"
			String query = "SELECT movies.id AS mvovie_id, title, year, director, rating,  genres.name AS genre,  stars.name AS name, birthyear\n" + 
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
			}
			
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			statement.close();
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