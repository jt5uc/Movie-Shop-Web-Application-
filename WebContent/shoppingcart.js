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
    let forms = jQuery("#forms");
    
    let go="";
    for (let i = 0; i < resultData.length; i++) {
    	go+="<form action=\"shoppingcart.html\" method=\"get\">"+
    	"<input type=\"hidden\" id=\"mid\" name=\"mid\" value=\""+resultData[i]["mid"]+"\">"+
  "<div class=\"form-group row\">"+
    "<label for=\"staticEmail\" class=\"col-sm-2 col-form-label\">Title: </label>"+
    "<div class=\"col-sm-10\">"+
      "<input type=\"text\" readonly class=\"form-control-plaintext \" name=\"title\"  value=\""+resultData[i]["title"]+"\">"+
    "</div>"+
  "</div>"+
  "<div class=\"form-group row\">"+
    "<label for=\"inputPassword\" class=\"col-sm-2 col-form-label\">Number</label>"+
    "<div class=\"col-sm-10\">"+
      "<input type=\"number\" class=\"form-control\" name=\"number\"  value=\""+resultData[i]["number"]+"\">"+
    "</div>"+
  "</div>"+
  "<button type=\"submit\" class=\"btn btn-primary\">Update</button>"+
  "</form>";
    }
    forms.append(go);
}





let title = getParameterByName('title');
let mid = getParameterByName('mid');
let number = getParameterByName('number');

if(!title)
{
	title="";
}
if(!mid)
{
	mid="";
}
if(!number)
{
	number="1";
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/shoppingchart?title="+title+"&mid="+mid+"&number="+number, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

