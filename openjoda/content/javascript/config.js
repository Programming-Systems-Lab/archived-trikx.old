function moveItem(element,moveUp)
{

  index = element.selectedIndex;
  if (index != -1 && element.options[index].value > "") {
    aText = element.options[index].text;
    aValue = element.options[index].value;
    if (element.options[index].value > "" && index > 0 && moveUp) 
    {
     	element.options[index].text = element.options[index-1].text;
      	element.options[index].value = element.options[index-1].value;
      	element.options[index-1].text = aText;
      	element.options[index-1].value = aValue;
      	element.selectedIndex--;           
    } 
    else if (index < element.length-1 && element.options[index+1].value > "" && !moveUp) 
    {          	
      	element.options[index].text = element.options[index+1].text;
      	element.options[index].value = element.options[index+1].value;
      	element.options[index+1].text = aText;
      	element.options[index+1].value = aValue;
      	element.selectedIndex++;
    }
  } 
  else 
  {
    alert("Please select a portlet");
  }
}


function movePortlet(fromElement,toElement)
{

	index = fromElement.selectedIndex;

	for(var i=0; i< fromElement.options.length; i++)
	{
		if(i == index)
		{	
								
			toElement.options[toElement.options.length] = new Option( fromElement.options[index].text,fromElement.options[index].value, false, true);			
			fromElement.options[index] = null;
		}
	}	   	   	
	
}

function removePortlet(element)
{
	
	index = element.selectedIndex;
	
	for(var i=0; i< element.options.length; i++)
	{
		if(i == index)
		{								 
			element.options[i] = null;				
		}
	}	   	   	
}

function cancel()
{
	document.portletConfig.jetspeedLyoutAction.value = "CANCEL";
	document.portletConfig.submit();
}

function saveConfig()
{
	
	leftColumn = "";
	rightColumn = "";
	leftElement = document.portletConfig.leftColumn
	rightElement = document.portletConfig.rightColumn

	for(var i=0; i< leftElement.options.length; i++)
	{							
		leftColumn = leftColumn + leftElement.options[i].text + ":"  + leftElement.options[i].value + ";"; 		
	}	
	
	for(var i=0; i<rightElement.options.length; i++)
	{					
		rightColumn = rightColumn + rightElement.options[i].text + ":"  + rightElement.options[i].value + ";"; 		
	}	 	
	document.portletConfig.jetspeedLyoutAction.value = "save"
	document.portletConfig.jetspeedLyoutLeftportlets.value = leftColumn
	document.portletConfig.jetspeedLyoutRightportlets.value = rightColumn;				
	document.portletConfig.submit();
	
	
}


