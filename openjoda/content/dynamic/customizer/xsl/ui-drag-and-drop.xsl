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

-->

<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform"
                version   = "1.0">

    <xsl:output method="html" indent="yes"/>

                
    <xsl:template match="/portlets">
        
        
        <div>


        <!-- add the styleshee for this content. -->
        <link href="/content/dynamic/customizer/stylesheets/default.css"
              type="text/css"
              rel="stylesheet"/>

        <form action="submit.php3" method="post" name="selected_mods">
            <input type="hidden" value="" name="slot_number_0"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_1"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_2"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_3"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_4"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_5"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_6"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_7"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_8"><!-- break --></input>
            <input type="hidden" value="" name="slot_number_9"><!-- break --></input>
            <input type="hidden" name="f_num_rows" value="2"><!-- break --></input>
            <input type="hidden" name="f_num_cols" value="2"><!-- break --></input>
        </form>
        

        <p>
            This is where you can choose the content for your Today page!  Each
            box represents a space that you can place your custom content. A 
            default selection is provided for you. Your content choices are on the 
            left.  To add content, simply click "Add A Row" and then drag your 
            content choice into any open box. To change the default content simply
            drag a different content choice over any existing content you would
            like to change.
        </p>

        
        <center>
        <div id="bottomTable">
        <form method="POST">
        <table width="98%"  cellpadding="1" cellspacing="0" bgcolor="#FFFFFF" border="0">
          <tr>
            <td width="300"></td>
            <td align="center">

                <input type="button" value="Remove A Row" onClick="del_row()"><!-- break --></input>
                <input type="button" value="Add A Row" onClick="add_row()"><!-- break --></input>

            </td>
            <td align="center">

                <input type="button" value="Revert" onClick="set_menu()"><!-- break --></input>
                <input type="button" value="Default" onClick="default_menu()"><!-- break --></input>
                <input type="button" value="Clear" onClick="clear_menu()"><!-- break --></input>

            </td>
            <td align="center"  bgcolor="#FFFFFF">

                <input type="button" value="Save" onClick="submit_query()"><!-- break --></input>
                <input type="button" value="Cancel" onClick="window.close()"><!-- break --></input>

            </td>
          </tr>
          <tr>
            <td></td>
            <td colspan="3" align="center">
                <script language="Javascript">
        
                <![CDATA[
        
                  function display_help(topic) {
                      window.open('/help/module.php3?topic=' + topic, 'helpwin','width=300,height=300,scrollbars=yes,resizable=yes');
                  }

                ]]>

                </script>

                <!--
                
                FIX ME:  add this help item information back in.
                <a href="javascript:void(0);" 
                   onClick="display_help('today')" 
                   onMouseOver="window.defaultStatus = 'Help on Today Page Content Customization'" 
                   onMouseOut="window.defaultStatus = '';">
                <img src="/images/help_btn.gif" border="0"></img>
                </a>
                -->
        
            </td>
          </tr>
        </table>
        </form>
        </div>
        
        </center>
        
        <div id="workSpace">
          <div id="slot0Out">
            <div id="slot0In"></div>
            <div id="slot0Light"></div>
          </div>
          <div id="slot1Out">
            <div id="slot1In"></div>
            <div id="slot1Light"></div>
          </div>
          <div id="slot2Out">
            <div id="slot2In"></div>
            <div id="slot2Light"></div>
          </div>
          <div id="slot3Out">
            <div id="slot3In"></div>
            <div id="slot3Light"></div>
          </div>
          <div id="slot4Out">
            <div id="slot4In"></div>
            <div id="slot4Light"></div>
          </div>
          <div id="slot5Out">
            <div id="slot5In"></div>
            <div id="slot5Light"></div>
          </div>
          <div id="slot6Out">
            <div id="slot6In"></div>
            <div id="slot6Light"></div>
          </div>
          <div id="slot7Out">
            <div id="slot7In"></div>
            <div id="slot7Light"></div>
          </div>
          <div id="slot8Out">
            <div id="slot8In"></div>
            <div id="slot8Light"></div>
          </div>
          <div id="slot9Out">
            <div id="slot9In"></div>
            <div id="slot9Light"></div>
          </div>
        </div>
        
        
        <div id="mouseweatherDRAG">
          <div id="mouseweatherCont">
            Weather
          </div>
        </div>
        
        <div id="mousehoroscopesDRAG">
          <div id="mousehoroscopesCont">
            Horoscopes
          </div>
        </div>
        
        <div id="mousemappingDRAG">
          <div id="mousemappingCont">
            Mapping
          </div>
        </div>
        
        <div id="mouseypDRAG">
          <div id="mouseypCont">
            Yellow Pages
          </div>
        </div>
        
        <div id="mousewebsitesDRAG">
          <div id="mousewebsitesCont">
            Favorite Websites
          </div>
        </div>
        
        <div id="mousesearchDRAG">
          <div id="mousesearchCont">
            Web Search
          </div>
        </div>
        
        <div id="mouseurlentryDRAG">
          <div id="mouseurlentryCont">
            Go to Website
          </div>
        </div>
        
        <div id="mousefreshmeatDRAG">
          <div id="mousefreshmeatCont">
            FreshMeat News
          </div>
        </div>
        
        <div id="mouseslashdotDRAG">
          <div id="mouseslashdotCont">
            Slashdot News
          </div>
        </div>
        
        <div id="mousetopstoriesDRAG">
          <div id="mousetopstoriesCont">
            Top Stories
          </div>
        </div>

        <div id="mousetopbusinessDRAG">
          <div id="mousetopbusinessCont">
            Top Business Stories
          </div>
        </div>
        
        <div id="mouseuspoliticsDRAG">
          <div id="mouseuspoliticsCont">
            US Politics
          </div>
        </div>
        
        <div id="mousebasketballDRAG">
          <div id="mousebasketballCont">
            Sports - Basketball
          </div>
        </div>
        
        <div id="mousegolfDRAG">
          <div id="mousegolfCont">
            Sports - Golf 
          </div>
        </div>
        
        <div id="mouseentertainmentgeneralDRAG">
          <div id="mouseentertainmentgeneralCont">
            Entertainment
          </div>
        </div>
        
        <div id="mousehealthDRAG">
          <div id="mousehealthCont">
            Consumer - Health
          </div>
        </div>
        
        <div id="mouseartsandcultureDRAG">
          <div id="mouseartsandcultureCont">
            Arts and Culture
          </div>
        </div>
        
        <div id="mousebookpublishingDRAG">
          <div id="mousebookpublishingCont">
            Book Publishing
          </div>
        </div>
        
        <div id="mouseparentingDRAG">
          <div id="mouseparentingCont">
            Consumer Parenting
          </div>
        </div>
        
        <div id="mouseznewsDRAG">
          <div id="mouseznewsCont">
            Zkey News
          </div>
        </div>
                
        
        <script language="Javascript">
        
          <![CDATA[

          // activeEl must be set to something, so we can just use the stock module
          // because it will most likely always be there.
          activeEl = document.mousestockDRAG;
        
          // GLOBAL variables:  Change these
          elementheight = 20;
          elementwidth  = 140;
          
          rows         = 2;
          cols         = 2;
          max_rows     = 5; 
          min_rows     = 1; 
        
          menu_width   = 160;
          right_margin = 10;
          extra_height = 200;
          slotspacing  = 5;
          startX       = menu_width;
          startY       = 95;
          
          what_slot_we_are_in = -1;
        
          slot         = Array(rows*cols);
          pslot        = Array(2);
        
          positions    = Array();
        
          positions[0] = 'mousestockDRAG';
          positions[1] = 'mouseweatherDRAG';
          positions[2] = 'mousehoroscopesDRAG';
          positions[3] = 'mousemappingDRAG';
          positions[4] = 'mouseypDRAG';
          positions[5] = 'mousewebsitesDRAG';
          positions[6] = 'mousesearchDRAG';
          positions[7] = 'mouseurlentryDRAG';
          positions[8] = 'mousefreshmeatDRAG';
          positions[9] = 'mouseslashdotDRAG';
          positions[10] = 'mousetopstoriesDRAG';
          positions[11] = 'mousetopbusinessDRAG';
          positions[12] = 'mouseuspoliticsDRAG';
          positions[13] = 'mousebasketballDRAG';
          positions[14] = 'mousegolfDRAG';
          positions[15] = 'mouseentertainmentgeneralDRAG';
          positions[16] = 'mousehealthDRAG';
          positions[17] = 'mouseartsandcultureDRAG';
          positions[18] = 'mousebookpublishingDRAG';
          positions[19] = 'mouseparentingDRAG';
          positions[20] = 'mouseznewsDRAG';

          
          function draw() {
            head = document.workSpace;
            // First, set size of workspace div
            head.clip.height = window.innerHeight - extra_height;
            head.clip.width = window.innerWidth - menu_width - right_margin;
            head.moveTo(startX,startY);
        
            document.bottomTable.moveTo(13,window.innerHeight - (extra_height-startY));
        
            var i = Number();
            var j = Number();
            for(i = 0 ; i < rows ; i++) {
              for(j = 0 ; j < cols ; j++) {
                draw_slot(i,j);
              }
            }
        
            for(i = 0 ; i < slot.length ; i++) {
              if(slot[i]) {
                set_slot(i,slot[i]);
              }
            }
          }
        
          function submit_query() {
            for(i = 0 ; i < slot.length ; i++) {
              if(slot[i]) {
                document.selected_mods.elements[i].value = slot[i].slice(5,-4);
              }
            }
            document.selected_mods.f_num_rows.value = rows;
            document.selected_mods.f_num_cols.value = cols;
            document.selected_mods.submit();
          }
        
          function set_menu() {
            user_rows         = 2;
            user_cols         = 2;
            
            var i = Number();
            
            if(user_rows < rows) {
              iter = rows - user_rows;
              for(i = 0 ; i < (iter) ; i++) {
                del_row();
              }
            } else if(user_rows > rows) {
              iter = user_rows - rows;
              for(i = 0 ; i < (iter) ; i++) {
                add_row();
              }
            }
        
            for(i = 0 ; i < positions.length ; i++) {
              recycle(positions[i]);
            }
            for(i = 0 ; i < slot.length ; i++) {
              slot[i] = '';
              unhighlight(i);
            }
                set_slot(0,'mouseweatherDRAG');
            highlight(0);
                  set_slot(1,'mousesearchDRAG');
            highlight(1);
                  set_slot(2,'mousestockDRAG');
            highlight(2);
                  set_slot(3,'mousehoroscopesDRAG');
            highlight(3);
                }
        
          function default_menu() {
            def_rows = 2;
            def_cols = 2;
        
            var i = Number();
            
            if(def_rows < rows) {
              iter = rows - def_rows;
              for(i = 0 ; i < (iter) ; i++) {
                del_row();
              }
            } else if(def_rows > rows) {
              iter = def_rows - rows;
              for(i = 0 ; i < (iter) ; i++) {
                add_row();
              }
            }
        
            for(i = 0 ; i < positions.length ; i++) {
              recycle(positions[i]);
            }   
            for(i = 0 ; i < slot.length ; i++) {
              slot[i] = '';
              unhighlight(i);
              }
                  set_slot(0,'mouseweatherDRAG');
              highlight(0);
                  set_slot(1,'mousesearchDRAG');
              highlight(1);
                  set_slot(2,'mousestockDRAG');
              highlight(2);
                  set_slot(3,'mousehoroscopesDRAG');
              highlight(3);
              }
        
          function clear_menu() {
            for(i = 0 ; i < positions.length ; i++) {
              recycle(positions[i]);
            }
            for(i = 0 ; i < slot.length ; i++) {
              slot[i] = '';
              unhighlight(i);
            }
          }
        ]]>
        
        </script>

        <script language="Javascript1.2" src="/content/dynamic/customizer/javascript/nn4.js">
        </script>
        
        <script language="Javascript1.2" src="/content/dynamic/customizer/javascript/dragDrop_nn4.js">
        </script>

        <!--
        We need to initialized the menu here...
        -->
        <script language="Javascript">
            set_menu();
            draw();        
        </script>
        

        
        </div>
        
   </xsl:template>


</xsl:stylesheet>

