/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 * 
 * This example implements the basic features of the autocomplete search, features that are 
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 * 
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 * 
 */


/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */

var myMap = new Map();

//////////////////////////////////////////////////////////////////////////////
document.getElementById("searchbtn").addEventListener('click',
		function(){
			document.querySelector('.bg-model').style.display='flex';
		});

document.querySelector('.close').addEventListener('click',
		function(){
	document.querySelector('.bg-model').style.display='none';
})
let chars="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
let tbrowse= jQuery("#tbrowse");
let url="movieList.html?fletter=";
tbrowse.append("<div>")
for(let i=0;i<chars.length; i++)
{
	tbrowse.append("<a class=\"pl-1\" href=\""+url+chars.charAt(i)+"\">"+chars.charAt(i)+" </a>")
}
tbrowse.append("</div>");


let numbers="0123456789";
tbrowse.append("<div>")
for(let i=0;i<numbers.length; i++)
{
	tbrowse.append("<a class=\"pl-1\" href=\""+url+numbers.charAt(i)+"\">"+numbers.charAt(i)+" </a>")
}
tbrowse.append("</div><hr />");

//######################genres########################

var genres = ["Action","Adult","Adventure","Animation","Biography","Comedy","Crime","Documentary","Drama","Family","Fantasy",
	"History","Horror","Music", "Musical","Mystery","Reality-TV", "Romance","Sci-Fi","Sport","Thriller","War","Western"];
let gbrowse= jQuery("#gbrowse");
let url1="movieList.html?genre=";
gbrowse.append("<div>")
for(let i=0;i<genres.length; i++)
{
	gbrowse.append("<a class=\"pl-3\" href=\""+url1+genres[i]+"\">"+genres[i]+" </a>")
}
gbrowse.append("</div>");

/////////////////////////////////////////////////////////////

function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	if(myMap.get(query)!=null)
	{
		doneCallback( { suggestions: myMap.get(query) } );
		console.log("this data is already stoed in fron end. fetched cached data")
	}
	else
	{
		console.log("this query is new so go to data base to fetch data")
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "autocompletes?query=" + escape(query),  
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	console.log("query: "+query)
	// parse the string into JSON
	//var jsonData = JSON.parse(data);
	//console.log(data)
	
	 
	 myMap.set(query,data);
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: data } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["ID"])
	window.location.replace("singleMovies.html?title="+suggestion["value"]);
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#ftinput').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars: 3,
    
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#ftinput').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#ftinput').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button



