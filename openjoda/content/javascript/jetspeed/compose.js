

//WARNING... this will be hard to internationalize...

//***********************************************************************
function verifyComposeForm () {

   if (document.forms['compose'].elements['to'].value.length == 0) {
      alert('Please specify a recipient.');
      return false;
   } else {
   
      if (document.forms['compose'].elements['to'].value.indexOf("@") == -1 ) {
         alert('This is an invalid address.  Use the format of user@domain');
         return false;
      }

   }
   

   if (document.forms['compose'].elements['cc'].value.length > 0) {
   
      if (document.forms['compose'].elements['cc'].value.indexOf("@") == -1 ) {
         alert('This is an invalid address.  Use the format of user@domain');
         return false;
      }

   }

   return true;
}


