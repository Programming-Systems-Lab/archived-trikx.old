<?xml version="1.0"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/content">

        <table border="0" cellspacing="0" cellpadding="0">


            <!--
            <xsl:call-template name="newest-topics"/>
            -->
            <xsl:apply-templates select="/content/channel/item"/>
        </table>
    </xsl:template>

    <xsl:template match="/content/channel/item">

	<xsl:if test="not(position() = 1)">
	  <tr width="100%">
	    <td width="100%" colspan="2">
	      <hr noshade="noshade"><!--breaker--></hr>
	    </td>
	  </tr>
	</xsl:if>
	
        <tr width="100%">
        <!--
        BEGIN Add the topic icon
        -->

        
        <td  align="left" valign="top"> 
        <xsl:call-template name="topics">
            <xsl:with-param name="topic"><xsl:value-of select="./topic"/></xsl:with-param>
        </xsl:call-template>
     <!--   </td> -->
        <!--
        END Add the topic icon
        -->

     <!--   <td width="100%" align="left" valign="top"> -->
        

        <a href="{link}">
        <b>
        <xsl:value-of select="./title"/>
        </b>
        </a>
        

        <!--
        Add the quote if any...
        -->

        <xsl:apply-templates select="./quote"/>
        

        <p align="left" clear="left">    
        <xsl:value-of select="./description"/>
        </p>
        </td>
        </tr>

    </xsl:template>

    <xsl:template match="/content/channel/item/quote">

        <p align="left">    
        from: 
        <a href="{./link}" target="_new">
        <xsl:value-of select="./author"/>        
        </a>
        </p>
    
        <xsl:apply-templates select="./p"/>

    </xsl:template>

    <xsl:template match="p">
      <p>
          <i>
              <xsl:value-of select="."/>
          </i>
      </p>
    </xsl:template>
    
    
    <xsl:template name="topics">

        <xsl:variable name="link"          select="/content/channel/topics/entry[@name=$topic]/image/link"/>
        <xsl:variable name="url"           select="/content/channel/topics/entry[@name=$topic]/image/url"/>
        <xsl:variable name="title"         select="/content/channel/topics/entry[@name=$topic]/image/title"/>
        <a href="{$link}">
        <img src="{$url}" border="0" alt="{$title}" align="left" />
        </a>
    </xsl:template>


    <!--
    Get an index of the most recent topics
    -->
    <xsl:template name="newest-topics">

        <tr width="100%">
        <td colspan="2">
        <table>
        <tr width="100%" align="right">
        <td width="100%"><!-- align --> </td>
        
        <xsl:call-template name="get-entry-topic">
            <xsl:with-param name="itemId">0</xsl:with-param>
        </xsl:call-template>
    
        <xsl:call-template name="get-entry-topic">
            <xsl:with-param name="itemId">2</xsl:with-param>
        </xsl:call-template>


        </tr>
        </table>
        </td>
        </tr>
    </xsl:template>
    
    <!--
    Given an id... get the image entry for a specific topic.
    -->
    <xsl:template name="get-entry-topic">

        <!-- first get the topic name of the iten you requested -->
        
        <xsl:variable name="topic" select="/content/channel/item[$itemId]/topic"/>

        <td>

        <xsl:call-template name="topics">
            <xsl:with-param name="topic"><xsl:value-of select="$topic"/></xsl:with-param>
        </xsl:call-template>

        </td>
        

    </xsl:template>
    
</xsl:stylesheet>

