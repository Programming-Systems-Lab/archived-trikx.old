<?xml version="1.0"?> 

<!--
Author:  Kevin A Burton (burton@apache.org)
$Id$
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:downlevel="http://my.netscape.com/rdf/simple/0.9/"
                exclude-result-prefixes="downlevel rdf"
                version="1.0">

    <xsl:output indent="yes" 
                method="xml"
                omit-xml-declaration="yes"/>

        <!--
        <!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
        -->
    
<!--
     Santiago Gala: This parameter controls the max number of
     items that will be displayed in a channel.
-->
    <xsl:param name="itemdisplayed" select="number(5)"/>


    
    <!-- BEGIN /document node support for RSS-->
    <xsl:template match="/rss">
            
        <card id="init" title="{./title}">        
            <xsl:apply-templates select="channel"/>
            <xsl:apply-templates select="channel/item[$itemdisplayed>=position()]"/>
        </card>

    </xsl:template>


    <xsl:template match="/rdf:RDF">

        <xsl:apply-templates select="downlevel:channel"/>
            
    </xsl:template>

    <!-- END /document node support for RSS-->    
    
    <xsl:template match="item[$itemdisplayed>=position()]">

        <xsl:variable name="description"     select="description"/>
    
        <p><b><a href="{link}">
            <xsl:value-of select="title"/>
           </a></b>
        </p>

        <xsl:if test="$description != ''">
           <p>
               <xsl:value-of select="$description"/>            
           </p>
       </xsl:if>
        
    </xsl:template>
    

    <xsl:template match="downlevel:item[$itemdisplayed>=position()]">

        <xsl:variable name="description"     select="downlevel:description"/>
    
        <p><b>
           <a href="{downlevel:link}"><xsl:value-of select="downlevel:title"/></a></b>
        </p>

        <xsl:if test="$description != ''">
            <p>
                <xsl:value-of select="$description"/>            
            </p>
        </xsl:if>

    </xsl:template>


   <xsl:template match="channel">

        <p>
            <xsl:value-of select="./title"/>
        </p>

   </xsl:template>
    
    <xsl:template match="downlevel:channel">

        <card id="init" title="{./downlevel:title}">

            <p>
                <xsl:value-of select="./downlevel:title"/> 
            </p>

            <xsl:apply-templates select="../downlevel:item[$itemdisplayed>=position()]"/>
        
        </card>
       
    </xsl:template>
    

    <!-- 
    FIX ME:
    
    Add <image> support here through wbmp support.
    Add <textinput> support
    -->
    
</xsl:stylesheet>


