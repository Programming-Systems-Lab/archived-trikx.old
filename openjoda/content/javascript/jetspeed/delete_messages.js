
//to this in an object

//***********************************************************************
function deleteSelectedMessages () {
   

   var display_subjects = new String("");
   var messages = new String("");
   var found = false;
   

   for (var i = 0; i < document.forms["messages"].elements.length;++i) {
      if ( (document.forms["messages"].elements[i].type == 'checkbox') &&
           (document.forms["messages"].elements[i].status == true) )  {
         
         found = true;
         //alert(document.forms("messages").elements[i].value);
         messages += document.forms["messages"].elements[i].value + ",";
         display_subjects += unescape(subjects[document.forms["messages"].elements[i].value] + "\n");
      
      }
   }

   //at this point "messages" should have an extra "," at the end... remove it.
   messages = messages.substring(0, messages.length - 1);

   
   
   if (found == false) {
      alert("You did not select any messages to delete.");
   } else {
      if ( confirm("Are you sure you want to delete the following?\n\n" + display_subjects) ) {
         var url = new String("");
         
         
         url = document.location.href;
         url = url.substring( 0, url.indexOf("?") );

         //client should now have the current servlet URL... add the message 
         //numbers and the action to perform.
         
         //hard code the action here... note that if this changes in the 
         //servlet you will have a problem.
         
         //message will contain a comma separated list of messages to delete...
         url = url + "?" + "action=13&message=" + messages;
         document.location =  url;
      
      }
   }


}


//global variables..
var subjects = new Array();

