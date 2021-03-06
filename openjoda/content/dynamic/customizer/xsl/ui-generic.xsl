<?xml version="1.0"?> 

<!--
   Copyright (c) 1998 The Java Apache Project.  All rights reserved.
 
   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:
  
   1. Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer. 
  
   2. Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
  
   3. Every modification must be notified to the Java Apache Project
      and redistribution of the modified code without prior notification
      is not permitted in any form.
  
   4. All advertising materials mentioning features or use of this
      software must display the following acknowledgment:
      "This product includes software developed by the Java Apache Project
      (http://java.apache.org/)."
  
   5. The names "Jetspeed", "Apache Jetspeed" and "Apache Jetspeed 
      Project" must not be used to endorse or promote products 
      derived from this software without prior written permission.
  
   6. Redistributions of any form whatsoever must retain the following
      acknowledgment:
      "This product includes software developed by the Java Apache Project
      (http://java.apache.org/)."
  
   THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
   EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
   PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
   ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
   HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
   STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
   OF THE POSSIBILITY OF SUCH DAMAGE.
   
   License version 1.0
  
-->


<!--
Author: Kevin A. Burton (burton@apache.org)
$Id$

Note:  This stylesheet provides a UI for uplevel (IE/Netscape) clients to customize 
their portlet experience.  It uses a PSML document to provide a UI for the user.

This is a simple server-based representation

The content that this stylesheet expects is detailed in ui-generic-example.xml

-->

<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform"
                version   = "1.0">

    <xsl:output method="html" 
                indent="yes"/>

                
    <xsl:template match="/portlets">
        
        <table width="100%" bgcolor="red">

            <tr>
                <xsl:apply-templates select="/portlets/portlets/portlets"/>
            </tr>
        
        </table>

    </xsl:template>


    <!--
    Assume that the second row of a PortletSet represents a column in this 
    scenario
    
    -->    
    <xsl:template match="/portlets/portlets/portlets">
        
        <td align="center" valign="top">
            <table width="100%" align="center">

                <xsl:apply-templates select="./entry"/>

            </table>
        </td>

    </xsl:template>
    
    <!--
    Assume that the second row of a PortletSet represents a column in this 
    scenario
    
    -->    
    <xsl:template match="entry">
        
        <tr>
        <td valign="top" align="center">
        
            <xsl:value-of select="./@parent"/>
        
        </td>
        </tr>
        
    </xsl:template>
    
    
</xsl:stylesheet>

