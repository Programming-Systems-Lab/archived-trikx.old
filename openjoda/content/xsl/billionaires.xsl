<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="billionaires">
		<table>
    	<xsl:apply-templates/>		
		</table>
	  <form>
	  	<input type="hidden" name="mode" value="{@altMode}"/>
	    <input type="submit" value="{@altModeText}"/>
	  </form>
	</xsl:template>  		
	<xsl:template match="billionaire">
		<tr>          
			<td>
				<xsl:value-of select="@name"/>
			</td>
			<td>
				<a href="mailto:{@email}"><xsl:value-of select="@email"/></a>
			</td>
			<xsl:if test="@rmv = 'true'">
			  <td>
			    <form>
			  	<input type="hidden" name="rmvName" value="{@name}"/>
			  	<input type="hidden" name="mode" value="editMode"/>
			    <input type="submit" value="Remove"/>
			    </form>
			  </td>
			</xsl:if>
		</tr>
	</xsl:template>
	<xsl:template match="addable">
		<form>
			<td>
			 	<input type="text" name="addName"/>
			</td>
			<td> 
		  	<input type="text" name="addEmail"/>
		  </td>
		  <td>
		    <input type="hidden" name="mode" value="editMode"/>
		    <input type="submit" value="Add"/>	
			</td>		    
		</form>		
	</xsl:template>  			
	<xsl:template match="message">	
		<b>Error : <xsl:value-of select="@text"/></b>
	</xsl:template>  	
</xsl:stylesheet>

