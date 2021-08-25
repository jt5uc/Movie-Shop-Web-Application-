/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */



function handleResult(resultData) {

    console.log("handleResult: populating movie info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let movieInfoElement = jQuery("#movie_info");

    // append two html <p> created to the h3 body, which will refresh the page
    var s = new Set();
    for (let i = 0; i < resultData.length; i++) {
    	s.add(resultData[i]["genre"]);
    }
    var sum="";
    let arrayOf = Array.from(s);
    for (var i = 0; i < arrayOf.length; i++) 
    {
    	sum+=arrayOf[i]+" ";
    	
    }
    
     
    
   
   movieInfoElement.append(
   		"<h1 class=\"display-4 font-weight-bold \">" +resultData[0]["title"] + "</h1>" +
        "<p  class=\"my-4\">Movie ID: " + resultData[0]["movie_id"] + "</p>"
        +
        "<p  class=\"my-4\">Year: " + resultData[0]["year"] + "</p>" +
        "<p  class=\"my-4\">Director: " + resultData[0]["director"] + "</p>"
        +
        "<p  class=\"my-4\">Genresss: " + sum + "</p>"
        +
        "<p  class=\"my-4\">Rating: " + resultData[0]["rating"] + "</p>");

    console.log("handleResult: populating star table from resultData");
    
 // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#star_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    var n = new Set();
   for (let i = 0; i < resultData.length; i++) {
	   if(n.has(resultData[i]["name"])==false)
	   {
        let rowHTML = "<tr>";
        rowHTML+="<th><a class=\"text-warning\" href=\"singleStar.html?name="+ resultData[i]["name"]+"\">"  +resultData[i]["name"]+"</a></th>";
        rowHTML += "<th>" + resultData[i]["birthyear"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
        n.add(resultData[i]["name"]);
	   }
    }
    
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let title = getParameterByName('title');


// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?title=Andrés Vicente Gómez", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});