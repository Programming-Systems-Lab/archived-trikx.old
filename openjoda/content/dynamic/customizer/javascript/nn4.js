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

  //***************************************************************************
  function draw_slot(row,col) {
    // determine which slot we are in
    slotnum = col+cols*row;
    // alert(slotnum);
    curslot = slotin = slotlight = null;

    // then step through all the layers and find the current slot
    for(i = 0 ; i < document.workSpace.layers.length ; i++) {
      if(document.workSpace.layers[i].name == 'slot' + slotnum + 'Out') {
        curslot = document.workSpace.layers[i];
      }
    }
    // then step through all the layers in the current slot 
    // and find the inner area and the hightlight
    for(i = 0 ; i < curslot.layers.length ; i++) {
      if(curslot.layers[i].name == 'slot' + slotnum + 'In') {
        slotin = curslot.layers[i];
      }
      if(curslot.layers[i].name == 'slot' + slotnum + 'Light') {
        slotlight = curslot.layers[i];
      }
    }
    
    //alert("SlotNum: " + slotnum + "\nName: " + curslot.name + "\nIn: " + slotin.name + "\nLight: " + slotlight.name);

    slotheight = ((window.innerHeight - extra_height) - 
                 (rows+1)*slotspacing)/rows;
    slotwidth  = ((window.innerWidth - menu_width - right_margin) - 
                 (cols+1)*slotspacing)/cols;

    curslot.clip.width  = slotwidth;
    curslot.clip.height = slotheight;
    curslot.left        = slotspacing + col * (slotwidth + slotspacing);
    curslot.top         = slotspacing + row * (slotheight+ slotspacing);

    slotin.clip.width  = slotlight.clip.width  = slotwidth  - 2;
    slotin.clip.height = slotlight.clip.height = slotheight - 2;
    slotin.left        = slotlight.left        = 1;
    slotin.top         = slotlight.top         = 1;

  }

  //***************************************************************************
  function hide_slot(row,col) {
    // determine which slot we are in
    slotnum = col+cols*row;
    // alert(slotnum);
    curslot = slotin = slotlight = null;
    // then step through all the layers and find the current slot
    for(i = 0 ; i < document.workSpace.layers.length ; i++) {
      if(document.workSpace.layers[i].name == 'slot' + slotnum + 'Out') {
        curslot = document.workSpace.layers[i];
      }
    }
    // then step through all the layers in the current slot 
    // and find the inner area and the hightlight
    for(i = 0 ; i < curslot.layers.length ; i++) {
      if(curslot.layers[i].name == 'slot' + slotnum + 'In') {
        slotin = curslot.layers[i];
      }
      if(curslot.layers[i].name == 'slot' + slotnum + 'Light') {
        slotlight = curslot.layers[i];
      }
    }
    
    //alert("SlotNum: " + slotnum + "\nName: " + curslot.name + "\nIn: " + slotin.name + "\nLight: " + slotlight.name);
    curslot.clip.width  = 0;
    curslot.clip.height = 0;
    curslot.left        = 0;
    curslot.top         = 0;

    slotin.clip.width   = 0;
    slotin.clip.height  = 0;
    slotin.left         = 0;
    slotin.top          = 0;
  }

  //***************************************************************************
  function get_slot(coordX,coordY) {
    X = coordX - startX;
    Y = coordY - startY;

    ws = document.workSpace.layers;
    
    var i = Number();
    
    for(i = 0 ; i < ws.length ; i++) {
      if(X > ws[i].left &&
         X < (ws[i].left + ws[i].clip.width) &&
         Y > ws[i].top  &&
         Y < (ws[i].top + ws[i].clip.height)) {
         return i;
       }
    }
    return (-1);
  }

  //***************************************************************************
  function get_slot_row(slotid) {
    return (parseInt(((slotid) / cols)+1)) - 1;
  }

  //***************************************************************************
  function get_slot_col(slotid) {
    return ((slotid) % cols);
  }

  //***************************************************************************
  function unhighlight(slotid) {
    ws = document.workSpace.layers;
    var i = Number();
    for(i = 0 ; i < ws.length ; i++) {
      if(ws[i].name == 'slot' + slotid + 'Out'){
        slot_to_highlight = ws[i];
        for(j = 0 ; j < slot_to_highlight.layers.length ; j++) {
          if(slot_to_highlight.layers[j].name == 'slot' + slotid + 'Light'){
            slot_to_highlight.layers[j].visibility = 'hidden';
          }
        }
      }
    }
  }

  //***************************************************************************
  function unhighlight_all() {
    var i = Number();
    for(i = 0 ; i < (rows*cols) ; i++) {
      if(!slot[i]) { unhighlight(i); }
    }
  }

  //***************************************************************************
  function highlight(slotid) {
    ws = document.workSpace.layers;
    var i = Number();
    for(i = 0 ; i < ws.length ; i++) {
      if(ws[i].name == 'slot' + slotid + 'Out'){
        slot_to_highlight = ws[i];
        for(j = 0 ; j < slot_to_highlight.layers.length ; j++) {
          if(slot_to_highlight.layers[j].name == 'slot' + slotid + 'Light'){
            slot_to_highlight.layers[j].visibility = 'visible';
            last_highlighted = j;
          }
        }
      }
    }
  }

  //***************************************************************************
  function check_highlight(slotid) {
    if(slotid == -1) {
      if(what_slot_we_are_in != -1) {
        if(!slot[what_slot_we_are_in]) {
          clear_slot(what_slot_we_are_in);
          unhighlight(what_slot_we_are_in);
        }
        unhighlight_all();
      }
      what_slot_we_are_in = -1;
    } else {
      unhighlight_all();
      highlight(slotid);
      what_slot_we_are_in = slotid;
    }
  }

  //***************************************************************************
  function drop_element(coordX, coordY, element) {
    if(!element) { return; }
    set_slot(get_slot(coordX, coordY),element.name);
  }

  //***************************************************************************
  function recycle(elementname) {
    for(i = 0 ; i < positions.length ; i++) {
      if(positions[i] == elementname) {
        pslot[i] = elementname;
        break;
      }
    }
    for(j = 0 ; j < document.layers.length ; j++) {
      if(document.layers[j].name == elementname){
        document.layers[j].left = 10;
        document.layers[j].top = startY + ((elementheight + 2) * (i));
      }
    }
  }

  //***************************************************************************
  function clear_slot(elementname) {
    for(i = 0 ; i < slot.length ; i++) {
      if(slot[i] == elementname) {
        slot[i] = '';
      }
    }
  }

  //***************************************************************************
  function get_slot_num(elname) {
    for(i = 0 ; i < slot.length ; i++) {
      if(slot[i] == elname) {
        return i;
      }
    }
  }

  //***************************************************************************
  function clear_pslot(elementname) {
    for(i = 0 ; i < pslot.length ; i++) {
      if(pslot[i] == elementname) {
        pslot[i] = '';
      }
    }
  }

  //***************************************************************************
  function set_slot(slotid, elementname) {
    if(slotid == -1) {
      clear_slot(elementname);
      recycle(elementname);
    } else {
      // alert("Slotid: " + slotid + "\nElement Name: " + elementname);
      for(i = 0 ; i < document.layers.length ; i++) {
        if(document.layers[i].name == elementname) {
          element = document.layers[i];
        }
      }
      // now find out the row and column of the slot we are dropping it in
      slotheight = ((window.innerHeight - extra_height) - 
                   (rows+1)*slotspacing)/rows;
      slotwidth  = ((window.innerWidth - menu_width - right_margin) - 
                   (cols+1)*slotspacing)/cols;
      
      slotrow = get_slot_row(slotid);
      slotcol = get_slot_col(slotid);
      
      // alert("Row: " + slotrow + "\nCol: " + slotcol);
      // move the element to where it belongs
      element.moveTo(startX + slotspacing 
                            + slotcol*(slotwidth + slotspacing)
                            + slotwidth/2
                            - elementwidth/2,
                     startY + slotspacing 
                            + slotrow*(slotheight + slotspacing)
                            + slotheight/2
                            - elementheight/2);

      if(slotid != get_slot_num(elementname)) {
        clear_slot(elementname);
      }

      if(slot[slotid] && (slot[slotid] != elementname)) {
        recycle(slot[slotid]);
      }
      slot[slotid] = elementname;
      
      clear_pslot(elementname);
      what_slot_we_are_in = -1;
    }
  }

  //***************************************************************************
  function print_slots() {
    var text = '';
    for(i = 0 ; i < slot.length ; i++) {
      text += "Slot " + i + " : " + slot[i] + "\n";
    }
    alert(text);
  }

  //***************************************************************************
  function print_pslots() {
    var text = '';
    for(i = 0 ; i < pslot.length ; i++) {
      text += "pSlot " + i + " : " + pslot[i] + "\n";
    }
    alert(text);
  }
  
  //***************************************************************************
  function add_row() {
    if (rows == max_rows) {
      alert("We're sorry. This is the maximum content you can add.");
      return false;
    }
    rows += 1;
    slot.length += cols;
    draw();
  }

  //***************************************************************************
  function del_row() {
    if (rows == min_rows) {
      alert("Can't remove any more rows");
      return false;
    }
    rows -= 1;
    for(k=0;k<cols;k++) {
      if (slot[((rows*cols)+k)]) {
        recycle(slot[((rows*cols)+k)]);
        unhighlight(((rows*cols)+k));
      }
      hide_slot(rows,k);
    }
    slot.length -= cols;
    draw();
  }
