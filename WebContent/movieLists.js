/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 *movielist will have maxim parameters: title, year, director, sname, sortingtype, sortingorder, genre, page

 * @param resultData jsonObject
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


function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
   
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");
   
    
    var intpage1 = parseInt(page, 10);
    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML+="<th scope=\"row\">"+((i+1)+(intpage1-1)*intLimit)+"</th>\n";
        rowHTML +=
            "<th>" +
        "<a class=\"text-warning\" href=\"singleMovies.html?title="+ resultData[i]["title"]+"\">"  +resultData[i]["title"]+"</a>"
            // Add a link to single-star.html with id passed with GET url parameter
            /*'<a href="single-star.html?id=' + resultData[i]['star_id'] + '">'
            + resultData[i]["star_name"] +     // display star_name for the link text
            '</a>' +*/+
            "</th>";
        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "<th>" + resultData[i]["genres"] + "</th>";
        rowHTML += "<th>";/*+ resultData[i]["star_name"] + "</th>";*/
        var str=resultData[i]["star_name"];
        var array = str.split(",");
        for(let i=0; i< array.length; i++ )
        	{
        		rowHTML+="<p><a class=\"text-warning\" href=\"singleStars.html?name="+ array[i]+"\">"  +array[i]+"</a></p>";
        	}
        rowHTML+="</th>";
        rowHTML+="<th>";
        rowHTML+="<a class=\"btn btn-primary\" href=\""+"shoppingcart.html?mid="+resultData[i]["mid"]+
        "&title="+resultData[i]["title"]+"\"role=\"button\">Add To Cart</a>";
        rowHTML+="</th>"
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
       
    }
    
    
    //
    
    
   
    let pagesection= jQuery("#pagen");
    let pp="";
    //let url="movieList.html?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&sortingtype="+sortingtype+"&sortingorder="
    //+sortingorder+"&genre="+genre+"&page=";
    /*pp+="<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"\">Previous</a></li>"+
    "<li class=\"page-item\"><a class=\"page-link\" href=\""+url+1+"\">1</a></li>"+
    "<li class=\"page-item\"><a class=\"page-link\" href=\""+url+2+"\">2</a></li>";
    pagesection.append(pp);*/
   
   
   
    let url="movieList.html?title="+title+"&year="+year+"&director="+director+"&ftinput="+ftinput+"&sname="+sname+"&fletter="+fletter+"&sortingtype="+sortingtype+"&sortingorder="
    +sortingorder+"&genre="+genre+"&limit="+limit+"&page=";
    let copy=url;
    let copy2=url;
    let prev=page;
    let next=page;
    var intpage = parseInt(page, 10); //convert page to int
    var current = parseInt(page, 10); 
    var intpage2 = parseInt(page, 10);
    if(intpage!=1)
    {
    	intpage--;  //decrease one 
    	prev= intpage.toString(); //convert back to string with -1
    }
    copy+=prev; //url for current's prev
    pp+="<li class=\"page-item\"><a class=\"page-link text-dark bg-warning border-dark rounded\" href=\""+copy+"\">Previous</a></li>";
    if(current<5)
    {
    	for(let i=1; i<8;i++)
    	{
    		let c3=url;
    		c3+=i; 
    		pp+="<li class=\"page-item  \"><a class=\"page-link text-dark bg-warning border-dark rounded\" href=\""+c3+"\">"+i+"</a></li>";
    	}
    }
    else
    {
    	for(let i=current-3; i<current+4;i++)
    	{
    		let c3=url;
    		c3+=i; 
    		pp+="<li class=\"page-item  \"><a class=\"page-link text-dark bg-warning border-dark rounded\" href=\""+c3+"\">"+i+"</a></li>";
    	}
    }
    if(intpage2!=50)
    {
    	intpage2++;
    	next= intpage2.toString(); //convert back to string with +1
    }
    copy2+=next;
    pp+="<li class=\"page-item\"><a class=\"page-link text-dark bg-warning border-dark rounded\" href=\""+copy2+"\">Next</a></li>";
    
    pagesection.append(pp);
    
 //################# view per page #########################
    let viewsection= jQuery("#view");
    let v="";
    let url_view="movieList.html?title="+title+"&year="+year+"&director="+director+"&ftinput="+ftinput+"&sname="+sname+"&sortingtype="+sortingtype+"&sortingorder="
    +sortingorder+"&genre="+genre+"&page="+page+"&fletter="+fletter+"&limit=";

    v+="<a class=\"dropdown-item bg-danger text-white\" href=\""+url_view+"100"+"\">100</a>";
    v+="<a class=\"dropdown-item bg-danger text-white\" href=\""+url_view+"10"+"\">10</a>";
    v+="<a class=\"dropdown-item bg-danger text-white\" href=\""+url_view+"25"+"\">25</a>";
    v+="<a class=\"dropdown-item bg-danger text-white\" href=\""+url_view+"35"+"\">35</a>";
    v+="<a class=\"dropdown-item bg-danger text-white\" href=\""+url_view+"50"+"\">50</a>";
    
    viewsection.append(v);
    
  //#################### titlesort ####################
    let titlesort= jQuery("#titleSort");
    let u1="movieList.html?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&sortingtype=title"+"&sortingorder=ASC"
    +"&genre="+genre+"&fletter="+fletter+"&limit="+limit+"&page="+page+"&ftinput="+ftinput;
    let u2="movieList.html?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&sortingtype=title"+"&sortingorder=DESC"
    +"&genre="+genre+"&fletter="+fletter+"&limit="+limit+"&page="+page+"&ftinput="+ftinput;
    titlesort.append("<a  href=\""+u1+"\" class=\"text-danger\"><i class=\"fas fa-sort-up fa-2x align-baseline ml-2\"></i></a>");
    titlesort.append("<a  href=\""+u2+"\" class=\"text-success\"><i class=\"fas fa-sort-down fa-2x align-baseline\"></i></a>");
   //#################### rating sort #####################
    let ratesort= jQuery("#ratingSort");
    let u3="movieList.html?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&sortingtype=rating"+"&sortingorder=ASC"
    +"&genre="+genre+"&fletter="+fletter+"&limit="+limit+"&page="+page+"&ftinput="+ftinput;
    let u4="movieList.html?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&sortingtype=rating"+"&sortingorder=DESC"
    +"&genre="+genre+"&fletter="+fletter+"&limit="+limit+"&page="+page+"&ftinput="+ftinput;
    ratesort.append("<a  href=\""+u3+"\" class=\"text-danger\"><i class=\"fas fa-sort-up fa-2x align-baseline ml-2\"></i></a>");
    ratesort.append("<a  href=\""+u4+"\" class=\"text-success\"><i class=\"fas fa-sort-down fa-2x align-baseline\"></i></a>");
    
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let sname = getParameterByName('sname');
let sortingtype = getParameterByName('sortingtype');
let sortingorder = getParameterByName('sortingorder');
let genre = getParameterByName('genre');
let page = getParameterByName('page');
let limit = getParameterByName('limit');
let fletter=getParameterByName('fletter');
let ftinput=getParameterByName('ftinput');

if(!page)
{
	page="1";
}
if(!limit)
{
	limit="30";
}
if(!sortingtype)
{
	sortingtype="";
}
if(!sortingorder)
{
	sortingorder="";
}
if(!fletter)
{
	fletter="";
}
if(!title)
{
	title="";
}
if(!year)
{
	year="";
}
if(!director)
{
	director="";
}
if(!sname)
{
	sname="";
}
if(!genre)
{
	genre="";
}
if(!ftinput)
{
	ftinput="";
}
var intLimit = parseInt(limit, 10); //int limit
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/MovieList?title="+title+"&year="+year+"&director="+director+"&sname="+sname+"&page="+page+"&limit="+limit+"&sortingtype="+sortingtype
    +"&sortingorder="+sortingorder+"&genre="+genre+"&fletter="+fletter+"&ftinput="+ftinput, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});