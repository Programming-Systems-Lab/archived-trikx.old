<?xml version="1.0"?> 
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:aocs= "http://alchemy.openjava.org/ocs/ocs-syntax#"
                xmlns:ocs = "http://alchemy.openjava.org/ocs/directory#"
                xmlns:dc  = "http://purl.org/dc/elements/1.0/"
                version   = "1.0">

    <xsl:output indent="yes"/>
                
    <xsl:template match="/rdf:RDF">

        <registry>
            <portlets>
                <xsl:apply-templates select="./rdf:description/rdf:description"/>
            </portlets>
        </registry>

    </xsl:template>
    
    <xsl:template match="/rdf:RDF/rdf:description/rdf:description">

        <!--
        Only known formats are RSS 0.90 and RSS 0.91
        -->

        <xsl:variable name="format" select="./rdf:description/aocs:format"/>
        <xsl:variable name="format2" select="./rdf:description/ocs:format"/>


        <xsl:variable name="url" select="./rdf:description/@about"/>        
        <xsl:variable name="tit" select="./dc:title"/>        
        <xsl:variable name="desc" select="./dc:description"/>

        
        <xsl:comment>
        Format: <xsl:value-of select="$format"/>
        </xsl:comment>

        <xsl:if test="$format = 'http://my.netscape.com/rdf/simple/0.9/'">
            <entry type="ref" parent="RSS" name="{$url}">
                <url><xsl:value-of select="$url"/></url>
                <parameter name="stylesheet" value="/content/xsl/rss.xsl"/>
<!-- uncomment to take metainfo from the ocs description
            <metainfo>
                <xsl:if test="$tit != ''">
                  <title><xsl:value-of select="$tit"/></title>
                </xsl:if>
                <xsl:if test="$desc != ''">
                  <description><xsl:value-of select="$desc"/></description>
                </xsl:if>
                <xsl:if test="updatePeriod != ''">
                  <updatePeriod><xsl:value-of select="updatePeriod"/></updatePeriod>
                </xsl:if>
            </metainfo>
-->
            </entry>
        </xsl:if>

        
        <xsl:if test="$format = 'http://my.netscape.com/publish/formats/rss-0.91.dtd'">
            <entry type="ref" parent="RSS" name="{$url}">
                <url><xsl:value-of select="$url"/></url>
                <parameter name="stylesheet" value="/content/xsl/rss.xsl"/>
<!-- uncomment to take metainfo from the ocs description
            <metainfo>
                <xsl:if test="$tit != ''">
                  <title><xsl:value-of select="$tit"/></title>
                </xsl:if>
                <xsl:if test="$desc != ''">
                  <description><xsl:value-of select="$desc"/></description>
                </xsl:if>
                <xsl:if test="updatePeriod != ''">
                  <updatePeriod><xsl:value-of select="updatePeriod"/></updatePeriod>
                </xsl:if>
            </metainfo>
-->
            </entry>
        </xsl:if>


        <xsl:if test="$format2 = 'http://my.netscape.com/rdf/simple/0.9/'">
            <entry type="ref" parent="RSS" name="{$url}">
                <url><xsl:value-of select="$url"/></url>
                <parameter name="stylesheet" value="/content/xsl/rss.xsl"/>
<!-- uncomment to take metainfo from the ocs description
               <metainfo>
                <xsl:if test="$tit != ''">
                  <title><xsl:value-of select="$tit"/></title>
                </xsl:if>
                <xsl:if test="$desc != ''">
                  <description><xsl:value-of select="$desc"/></description>
                </xsl:if>
                <xsl:if test="updatePeriod != ''">
                  <updatePeriod><xsl:value-of select="updatePeriod"/></updatePeriod>
                </xsl:if>
            </metainfo>
-->
            </entry>
        </xsl:if>

        <xsl:if test="$format2 = 'http://my.netscape.com/publish/formats/rss-0.91.dtd'">
            <entry type="ref" parent="RSS" name="{$url}">
                <url><xsl:value-of select="$url"/></url>
                <parameter name="stylesheet" value="/content/xsl/rss.xsl"/>
<!-- uncomment to take metainfo from the ocs description
                <metainfo>
                <xsl:if test="$tit != ''">
                  <title><xsl:value-of select="$tit"/></title>
                </xsl:if>
                <xsl:if test="$desc != ''">
                  <description><xsl:value-of select="$desc"/></description>
                </xsl:if>
                <xsl:if test="updatePeriod != ''">
                  <updatePeriod><xsl:value-of select="updatePeriod"/></updatePeriod>
                </xsl:if>
            </metainfo>
-->
            </entry>
        </xsl:if>

        
            
    </xsl:template>


</xsl:stylesheet>

