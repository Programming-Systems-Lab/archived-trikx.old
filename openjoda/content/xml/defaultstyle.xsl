<?xml version="1.0"?>

<!-- Written by Stefano Mazzocchi "stefano@apache.org" -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="portlets">
  <table align="center" width="{@width}" cellpadding="0" cellspacing="0">
  	<tr>
	  	<td width="50%" valign="top">
  			<xsl:apply-templates select="portlet[position() mod 2 = 1]"/>
	  	</td>
	  	<td width="5"></td>
	  	<td width="50%" valign="top">
	  		<xsl:apply-templates select="portlet[position() mod 2 = 0]"/> 
	  	</td>
  	</tr>
  </table>
  </xsl:template>

  <xsl:template match="portlet">
	<table width="{skin/width}" cellspacing="0" cellpadding="0">
		<tr><td width="100%" bgcolor="{./skin/color}">
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr height="30">
			<td height="30" width="90%">
				<font color="{./skin/titlecolor}"><xsl:value-of select=".//title"/></font>
			</td>
			<td height="30" width="10%" align="center"><b>Edit</b></td>
		</tr>
		</table>
		</td></tr>
		<tr>
			<td valign="top" bgcolor="{skin/backgroundcolor}">
			<xsl:apply-templates/>
			</td>
		</tr>
	</table>
	<table width="{skin/width}" cellspacing="0" cellpadding="0"><tr height="10"><td height="10"></td></tr></table>

  </xsl:template>

  <xsl:template match="channel">
	<xsl:apply-templates select="image"/>
	<xsl:if test="item">
	<ul>
		<xsl:apply-templates select="item"/>
	</ul>
	</xsl:if>
  </xsl:template>

  <xsl:template match="item">
	<li> <a href="{link}"><xsl:value-of select="title"/></a></li>
  </xsl:template>

  <xsl:template match="image">
	<a href="{link}"><img src="{url}" align="right" vspace="5" hspace="5" alt="{title}" border="0"/></a>
  </xsl:template>

  <!-- remove all skin elements, they should not be displayed -->
  <xsl:template match="skin">
  </xsl:template>

  <!-- copy all unknown elements, they may be pre-formatted text -->
  <xsl:template match="*|@*|text()">
	<xsl:copy>
		<xsl:apply-templates/>
	</xsl:copy>
  </xsl:template>

</xsl:stylesheet>
