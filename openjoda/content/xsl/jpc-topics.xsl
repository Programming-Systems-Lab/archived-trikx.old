<?xml version="1.0"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/content">

        <center>
        <table>
            <xsl:apply-templates select="/content/channel/topics/entry"/>
        </table>
        </center>
            
    </xsl:template>

    <xsl:template match="/content/channel/topics/entry">

        <xsl:variable name="link"          select="./image/link"/>
        <xsl:variable name="url"           select="./image/url"/>
        <xsl:variable name="title"         select="./image/title"/>

        <td>
        <a href="{$link}">
        <img src="{$url}" border="0" alt="{$title}"></img>
        </a>
        </td>

    </xsl:template>
    
</xsl:stylesheet>

