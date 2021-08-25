/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
    	console.log("inserted ");
        window.location.replace("result.html");
       
    } else {
        // If login fails, the web page will display 
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
       // console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

function handleInsertResult(resultDataString) {
   resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
    $("#message").text(resultDataJson["message"]);
    
    // If login succeeds, it will redirect the user to index.html
    /*if (resultDataJson["status"] === "success") {
    	console.log("inserted ");
        window.location.replace("result.html");*/
       
   /* } else {
        // If login fails, the web page will display 
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
       // console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }*/
}

function display(resultData) {

  /*  console.log("handleResult: populating movie info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"*/
    let displayElement = jQuery("#meta");

   /*
    let movieTableBodyElement = jQuery("#star_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows*/
   let tablename="";
   for (let i = 0; i < resultData.length; i++) {
	   
        /*let rowHTML = "<tr>";
        rowHTML+="<th><a class=\"text-warning\" href=\"singleStar.html?name="+ resultData[i]["name"]+"\">"  +resultData[i]["name"]+"</a></th>";
        rowHTML += "<th>" + resultData[i]["birthyear"] + "</th>";
        rowHTML += "</tr>";*/
	   let rowHTML="";
	   if(resultData[i]["tablename"]!=tablename)
		{
		   rowHTML+="<h2>"+resultData[i]["tablename"]+"</h2><p>";
	   	   rowHTML+=resultData[i]["field"]+"    ";
	       rowHTML+=resultData[i]["type"]+"</p>";
		}
	   else
		   {
		   rowHTML+="<p>";
	   	   rowHTML+=resultData[i]["field"]+"    ";
	       rowHTML+=resultData[i]["type"]+"</p>";
		   }
        // Append the row created to the table body, which will refresh the page
        displayElement.append(rowHTML);
        tablename=resultData[i]["tablename"];
      
	  
    }
    
}


/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/insertstar",
        // Serialize the login form to the data sent by POST request
        $("#insertstar_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    );
}

function submitInsertForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/insertmovie",
        // Serialize the login form to the data sent by POST request
        $("#insertmovie_form").serialize(),
        (resultDataString) => handleInsertResult(resultDataString)
    );
}

function showmeta(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();
    
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "api/meta", // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => display(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
    
}

// Bind the submit action of the form to a handler function
$("#insertstar_form").submit((event) => submitLoginForm(event));
$("#meta").submit((event) => showmeta(event));
$("#insertmovie_form").submit((event) => submitInsertForm(event));
