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
                 method="html"
                 omit-xml-declaration="yes"/>                

<!--
     Santiago Gala: This parameter controls the max number of
     items that will be displayed in a channel.
-->
    <xsl:param name="itemdisplayed" select="number(15)"/>



    <!-- BEGIN /document node support for RSS-->
    <xsl:template match="/rss">

        <div>

            <xsl:apply-templates select="channel" />
       <ul>
            <xsl:apply-templates select="channel/item[$itemdisplayed>=position()]" />
    <!-- BEGIN hacked /rss node support for network54-->    
            <xsl:apply-templates select="/rss/item[$itemdisplayed>=position()]"/>
    <!-- END hacked /rss node support for network54-->    
       </ul>
            <xsl:apply-templates select="channel/textinput" />            
            <xsl:apply-templates select="textinput"/>            
        </div>

        
    </xsl:template>


    <xsl:template match="/rdf:RDF">
        <div>

            <xsl:apply-templates select="downlevel:channel"/>
<ul>
            <xsl:apply-templates select="downlevel:item[$itemdisplayed>=position()]" />
</ul>
            <xsl:apply-templates select="downlevel:textinput"/>

        </div>

    <!-- BEGIN hacked /document node support for Slashdot-->    
            <xsl:apply-templates select="downlevel:rss" mode="hacked"/>
    <!-- END hacked /document node support for Slashdot-->    


    </xsl:template>

    <!-- BEGIN hacked /document node support for Slashdot-->    
    <xsl:template match="downlevel:rss" mode="hacked">

        <div>

            <xsl:apply-templates select="./downlevel:channel"/>
<ul>
            <xsl:apply-templates select="./downlevel:item[$itemdisplayed>=position()]" />
</ul>
            <xsl:apply-templates select="./downlevel:textinput"/>

        </div>
    </xsl:template>

    <!-- END hacked /document node support for Slashdot-->    




    <!-- END /document node support for RSS-->    

    
    <xsl:template match="item">

        <xsl:variable name="description"     select="description"/>
    
        <li>
                <a href="{link}">
                    <xsl:value-of select="title"/>
                </a>
            <xsl:if test="$description != ''">
                <br/><xsl:value-of select="$description"/>
            </xsl:if>
        </li>
        
    </xsl:template>
    
    <xsl:template match="channel">

        <xsl:variable name="description"     select="description"/>    
        
        <xsl:if test="$description != ''">
            <p>

            <xsl:apply-templates select="image" mode="channel"/>
            <!-- some channels mix namespaces -->
            <xsl:apply-templates select="../image" mode="channel"/>

            <xsl:value-of select="$description"/>            

            </p>
        </xsl:if>
        
    </xsl:template>


    <xsl:template match="downlevel:item" >
        <xsl:variable name="desc2"     select="/item/downlevel:description"/>
    
        <li>
                <a href="{downlevel:link}">
                    <xsl:value-of select="downlevel:title"/>
                </a>
            <xsl:if test="$desc2 != ''">
                <p><xsl:value-of select="$desc2"/></p>
            </xsl:if>
        </li>
        
    </xsl:template>
    
    <xsl:template match="downlevel:channel">

        <xsl:variable name="descript"     select="downlevel:description"/>
        
            <xsl:if test="$descript != ''">
                <p>
                <xsl:apply-templates select="../downlevel:image" mode="channel"/>
                <xsl:value-of select="$descript"/>
                </p>
            </xsl:if>
    </xsl:template>
    
    <!-- BEGIN TEXTINPUT SUPPORT -->

    <xsl:template match="downlevel:textinput">
        
        <form action="{./downlevel:link}">
        <xsl:value-of select="./downlevel:description"/>
        <br>
        <input type="text" name="{./downlevel:name}" value=""></input>
        <input type="submit" name="submit" value="{./downlevel:title}"></input>
        </br>
        </form>
        
    </xsl:template>

    <xsl:template match="textinput">
        
        <form action="{./link}">
        <xsl:value-of select="./description"/>
        <br>
        <input type="text" name="{./name}" value=""></input>
        <input type="submit" name="submit" value="{./title}"></input>
        </br>
        </form>
        
    </xsl:template>

    
    <!-- END TEXTINPUT SUPPORT -->    
    
    <!-- BEGIN IMAGE SUPPORT -->        

    <xsl:template match="image" mode="channel">

        <a href="{./link}" align="right">
            <img alt="{./title}" 
                 src="{./url}" 
                 width="{./width}" 
                 height="{./height}" 
                 align="right" 
                 border="0"></img>
        </a>
        
    </xsl:template>

    <xsl:template match="downlevel:image" mode="channel">

        <a href="{./downlevel:link}" align="right">
            <img alt="{./downlevel:title}" 
                 src="{./downlevel:url}" 
                 align="right" 
                 border="0"></img>
        </a>  
    </xsl:template>

    <!-- We ignore images unless we are inside a channel -->    
    <xsl:template match="downlevel:image">
    </xsl:template>
    
    <!-- END IMAGE SUPPORT -->        

    
</xsl:stylesheet>



