/*
   Perform a meta-query on a particular search engine:

   

   

   query:         Text you are looking for
   cgi:           The full path of the CGI you are going to execute against.
   query_string:  The http GET parameters required by the CGI
   target:        The frame to display this in.

   Else this script returns false.

*/



//*******************************************************************

function jetspeedMetaSearch( search_string, query, target ) {





   if (search_string.length < 1) {

      alert('Please enter a search string');

      return false;
   }



   eval(target+ ".location='" + query + search_string + "';");

   return false;
}





//*******************************************************************
function setimage( img ) {

	if (document.images) {

      document.images["SEARCH_IMG"].src = img;

	}

}