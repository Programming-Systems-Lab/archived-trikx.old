<?xml version="1.0"?>

<!--
Author: Kevin A Burton (burton@apache.org)
Version:  $Id$
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

                              
    <xsl:template match="portletlist">

        <xsl:processing-instruction name="cocoon-format">type="text/html"</xsl:processing-instruction>

        <div>
            
            
            <ul>
                <br>
                <p>
                Please enter a search criteria.  You can search with the 
                Title, Description or URL of the Channel.
                
                </p>


                <p>
                <form action="{./search}">
                    <input type="text" name="query" value="{./query}"></input>
                    <br>
                    <input type="submit" name="submit" value="Search"></input>
                    </br>
                    <input type="hidden" name="url" value="{./url}"></input>
                </form>
                </p>
                </br>
            </ul>

<xsl:choose>
<xsl:when test="total > 0">            
                <p>
                <xsl:value-of select="./start"/>
                through 
                <xsl:value-of select="./end"/>
                of <xsl:value-of select="./total"/> channels...
                </p>        
               
            <ul>
                <xsl:apply-templates select="./entry"/>
<xsl:if test="total > end">
                <xsl:apply-templates select="./nextpage"/>                
</xsl:if>
            </ul>

</xsl:when>    
<xsl:otherwise>
<xsl:if test="query != ''">
	<b>No results found!</b>
</xsl:if>
</xsl:otherwise>    
</xsl:choose>
        </div>

    </xsl:template>


    <xsl:template match="entry">
        <li>
            <p>

            <xsl:choose>
                <xsl:when test="./link != ''">
                    <b>
                    <a href="{./link}">
                    <xsl:value-of select="./title"/>
                    </a>
                    :</b>
                    &#160;
                </xsl:when>
    
                <xsl:otherwise>
                    <b>
                    <xsl:value-of select="./title"/>
                    :</b>
                    
                </xsl:otherwise>
    
            </xsl:choose>
            
            <xsl:value-of select="./description"/>
            
            <br>
                <a href="{./preview}">Preview</a>
            </br>
            
            </p>
        </li>
    </xsl:template>

    <xsl:template match="nextpage">


            <form>

                <xsl:attribute name="action">
                <xsl:value-of disable-output-escaping="yes" select="./url"/>
                </xsl:attribute>
                
                <input type="hidden" name="start" value="{./start}"></input>
                <input type="hidden" name="end" value="{./end}"></input>
                <input type="submit" value="Next page"></input>

                <xsl:apply-templates select="./parameter"/>
                
            </form>

    
    </xsl:template>


    <xsl:template match="parameter">
        <input type="hidden" name="{./@name}" value="{./@value}"></input>
    </xsl:template>
    
</xsl:stylesheet>

