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


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/MovieList")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        String titl = request.getParameter("title");
        String yea = request.getParameter("year");
        String directo = request.getParameter("director");
        String snam = request.getParameter("sname");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        String fletter = request.getParameter("fletter");
        String sortingtype = request.getParameter("sortingtype");
        String genre = request.getParameter("genre");
        String ftinput = request.getParameter("ftinput");
        System.out.println("sortingtype: "+sortingtype);
        String sortingorder = request.getParameter("sortingorder");
        System.out.println("sortingorder: "+sortingorder);
        int limitInt = Integer.parseInt(limit);	
       
     
        String appen="";
        String offset="";
        String order="";
        String genrepre="";
        String genreafter="";
        String snamepre="";
        String snameafter="";
        String ftquery="";
        if(!ftinput.equals(""))
        {
        	System.out.println("ftinput is"+ftinput);
        	String[] splited = ftinput.split("\\s+");
        	String items="";
        	for(String s: splited)
        	{
        		items+="+"+s+"*,";
        	}
        	items=items.substring(0, items.length()-1);
        	
        	
        	appen=
        			"AND MATCH(title) \n" + 
        			"		AGAINST('"+items+"' IN BOOLEAN MODE)";
        	System.out.println(appen);
        }
        if(!titl.equals(""))
        {
        	appen+="AND title like \"%"+titl+"%\"";
        	System.out.println("ttile is"+titl);
        }
        if(!yea.equals(""))
        {
        	appen+="AND year LIKE \"%"+yea+"%\"";
        	System.out.println("year not null");
        }
        if(!directo.equals(""))
        {
        	appen+="AND director LIKE \"%"+directo+"%\"";
        	System.out.println("director not null");
        }
        if(!snam.equals(""))
        {
        	System.out.println("sname not null");
        	snamepre+="SELECT * FROM\n" + 
        	   		"(";
        	snameafter+=") a\n" + 
        			" JOIN \n" + 
        			"(\n" + 
        			"SELECT movies.title as pt, movies.director as pd FROM\n" + 
        			"movies, stars, stars_in_movies\n" + 
        			"WHERE movies.id=stars_in_movies.movieId\n" + 
        			"AND stars_in_movies.starId=stars.id\n" + 
        			"AND stars.name LIKE \"%"+snam+"%\") c\n" + 
        			"ON c.pt=a.title AND c.pd=a.director";
        }
        if(!sortingtype.equals("") && !sortingorder.equals(""))
        {
        	System.out.println("sortingtype and order not null");
        	order+="ORDER BY "+sortingtype+" "+sortingorder+"\n";
        }
        System.out.println("order: "+order);
        if(!fletter.equals(""))
        {
        	System.out.println("fletter is "+fletter);
        	appen+="AND title like \""+fletter+"%\"";
        }
       if(!page.equals(""))
        {
        	System.out.println("page: "+page);
        	int pn = Integer.parseInt(page);	
        	offset+="offset "+(pn-1)*limitInt;
        }
       if(!genre.equals(""))
       {
    	   System.out.println("genre: "+genre);
    	   genrepre+="SELECT * FROM\n" + 
    	   		"(";
    	   genreafter+=") a\n" + 
    	   		" JOIN \n" + 
    	   		"(\n" + 
    	   		"SELECT movies.title as t, movies.director as g FROM \n" + 
    	   		"movies, genres, genres_in_movies\n" + 
    	   		"WHERE movies.id=genres_in_movies.movieId\n" + 
    	   		"AND genres_in_movies.genreId=genres.id\n" + 
    	   		"AND genres.name=\""+genre+"\") b\n" + 
    	   		"ON b.t=a.title AND b.g=a.director";
       }
       
        
    
       
       
        System.out.println(appen);
        System.out.print("adadad");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            
            		String query = snamepre+genrepre+"SELECT movies.id as mid,title, year, director, rating,  GROUP_CONCAT( DISTINCT genres.name) AS genres, GROUP_CONCAT(DISTINCT stars.name) AS star_name\n" + 
            		" FROM ratings, movies, genres_in_movies,genres,stars_in_movies,stars\n" + 
            		"    WHERE  stars_in_movies.starId= stars.id AND\n" + 
            		"    stars_in_movies.movieId= movies.id AND\n" + 
            		"    genres_in_movies.genreId=genres.id AND\n" + 
            		"    genres_in_movies.movieId=movies.id AND\n" + 
            		"    ratings.movieId=movies.id \n" + 
            		"\n" + appen+
            		"    GROUP BY title, year, director, rating, mid\n" +genreafter+snameafter+" "+order+
            		"    LIMIT "+limit+"\n" + offset+
            		"  ; ";
        
            System.out.println(query);
            System.out.println("heheheheheh");
            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
            	String title = rs.getString("title");
    			String year = rs.getString("year");
    			String director = rs.getString("director");
    			String rating = rs.getString("rating");
    			String genres = rs.getString("genres");
    			String star_name = rs.getString("star_name");
    			String mid = rs.getString("mid");
    			
                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", title);
                jsonObject.addProperty("year", year);
                jsonObject.addProperty("director", director);
                jsonObject.addProperty("rating", rating);
                jsonObject.addProperty("genres", genres);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("mid", mid);
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