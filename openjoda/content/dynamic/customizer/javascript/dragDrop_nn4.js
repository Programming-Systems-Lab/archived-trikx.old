
/*
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
  
*/


    IE4  = (document.all)    ? 1 : 0; 
    NS4  = (document.layers) ? 1 : 0; 
    ver4 = (NS4 || IE4)      ? 1 : 0;
    
    currentX = currentY = 0;
    whichEl = null;
    
    function grabEl(e) {
        mouseX = e.pageX;
        mouseY = e.pageY;
    
        for ( i=0; i<document.layers.length; i++ ) {
        tempLayer = document.layers[i];
            if ( tempLayer.id.indexOf("DRAG") == -1 ) { continue }
            if ( (mouseX > tempLayer.left) && (mouseX < (tempLayer.left + tempLayer.clip.width)) && (mouseY > tempLayer.top) && (mouseY < (tempLayer.top + tempLayer.clip.height)) ) {
                whichEl = tempLayer;
            }
        } 
    
        if (whichEl == null) { return}
    
        if (whichEl != activeEl) {
            whichEl.moveAbove(activeEl);
            activeEl = whichEl;
        }
    
        currentX = e.pageX;
        currentY = e.pageY;
    
        document.captureEvents(Event.MOUSEMOVE);
        document.onmousemove = moveEl;
        // added by bartle and jones
        clear_slot(whichEl.name);
        clear_pslot(whichEl.name);
    }
    
    function moveEl(e) {
        if (whichEl == null) { return };
    
        newX = e.pageX;
        newY = e.pageY;

        distanceX = (newX - currentX);
        distanceY = (newY - currentY);
        currentX = newX;
        currentY = newY;

        // added by bartle and jones
        check_highlight(get_slot(currentX,currentY));

        whichEl.moveBy(distanceX,distanceY); 
    }
    
    function checkEl() {
        if (whichEl!=null) { return false }
    }
    
    function dropEl(e) {
        // added by bartle and jones
        drop_element(e.pageX,e.pageY,whichEl);
    
        document.releaseEvents(Event.MOUSEMOVE);
        whichEl = null;
    }
    
    function cursEl() {
        if (event.srcElement.id.indexOf("DRAG") != -1) {
            event.srcElement.style.cursor = "move"
        }
    }
    
    if (ver4) {
        document.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
        document.onmousedown = grabEl;
        document.onmouseup = dropEl;
    }
    

