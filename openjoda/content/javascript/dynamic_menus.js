/* **********************************************************************
 * MODULE	: dynamic_menus.js
 * AUTHOR	: Brian P. King
 * CREATED	: May 3, 1999
 * COMPATIBILITY: Requires JavaScript 1.1.
 * RCS		: $Id$
 * DESCRIPTION	: Implements dynamic pulldown menus.
 * COPYRIGHT    : Copyright (c) 1999 Symantec Corporation.
 * DISTRIBUTION : Freely redistributable.  Use at your own risk.                              
 * **********************************************************************/

if ( ! versions_loaded ) {

    // Determine Browser Version
    var bV   = parseInt( navigator.appVersion );
    var NS4  = (document.layers)         ? true : false;
    var NS3  = (document.images)         ? true : false;
    var IE4  = ((document.all)&&(bV>=4)) ? true : false;
    var ver4 = (NS4 || IE4)              ? true : false;

    // Determine Browser Platform
    var MAC   = (navigator.userAgent.indexOf("Mac")      != -1) ? true : false;
    var Opera = (navigator.userAgent.indexOf("Opera")    != -1) ? true : false;
    var IE5   = (navigator.userAgent.indexOf("MSIE 5.0") != -1) ? true : false;

    var versions_loaded = true;
}


/* **********************************************************************
 * Create a MenuItem object.
 * **********************************************************************/

function MenuItem( miname, mivalue ) {
    if ( ! Opera ) {
	this.name  = miname;
	this.value = mivalue;
	this.bold  = false;
	return( this );
    }
}

    
    
    
/* **********************************************************************
 * Create the Menu object.
 * **********************************************************************/
    
var allmenus    = new Array(); // tracks all created menus for later printing.
allmenus.length = 0;
    
function getMenuImage( menuname ) {
    var imgname = null;
    if ( allmenus[menuname] )
	imgname = allmenus[menuname].imagename;
    return( imgname );
}

function Menu( mname, _imagename ) {
    // Set up properties for this instance
    this.name                   = mname;
    this.imagename              = _imagename;
    Menu.prototype.items        = new Array();
    Menu.prototype.items.length = 0;
    // If the _width parameter isn't supplied, make an attempt
    // to determine the menu width based on the image's width.
    var imgWidth      = null;
    if ( _imagename ) {
	var img  = getImage( _imagename );
	if ( img ) {
	    imgWidth = getImageWidth( img );
	}
    }
    if ( imgWidth ) this.width = imgWidth;
    // If borderstyle is "image", make sure leftimage, leftimagewidth and
    // leftimagecolor are specified
    if ( this.borderstyle == "image" ) {
	if ( ! this.leftimagewidth || ! this.leftimage ) {
	    this.borderstyle = "solid";
	    this.leftimage = null;
	}
	else if ( ! this.leftimagecolor ) {
	    this.leftimagecolor = "#ffffff";
	}
    }
    // Add the menu to the list of menus if it's not the prototype
    if ( mname != "discard" ) {
	allmenus[allmenus.length++] = this;
	allmenus[mname]             = this;
    }
    return( this );
}

function Menu_addMenuItem( miname, mivalue ) {
    var newitem      = new MenuItem( miname, mivalue );
    newitem.index    = this.items.length;
    newitem.menuname = this.name;
    this.items[this.items.length++] = newitem;
    return( newitem );
}

function Menu_addMenuItemHeader( miname, mivalue ) {
    var newitem = this.addMenuItem( miname, mivalue );
    newitem.bold = true;
    return( newitem );
}

function Menu_addMenuSeparator( hidden ) {
    var newitem = new MenuItem( "separator", "---" );
    newitem.hidden = hidden;
    this.items[this.items.length++] = newitem;
    return( newitem );
}

function Menu_print() {
    var menu = this;
    // write the header
    var output = "";
    if ( NS4 || ( IE4 && ! MAC ) ) {

	if ( navigator.appName == "Microsoft Internet Explorer" ) {
	    output += '<div id="' + menu.name + '" ';
	    output += 'onMouseOver="showMenu(\'' + menu.name + '\')" ';
	    output += 'onMouseOut="hideMenu(\'' + menu.name + '\')" ';
	    output += 'style="position:absolute;z-index:59600;visibility:hidden;';
	    output += 'width:' + menu.width + 'px;height:' + menu.height + ';"' ;
	    output += '>\n';
	}
	else {
	    output += '<layer name="' + menu.name + '" ';
	    output += 'onMouseOver="showMenu(\'' + menu.name + '\')" ';
	    output += 'onMouseOut="hideMenu(\'' + menu.name + '\')" ';
	    output += 'width="' + menu.width + '" height="' + menu.height + '" ';
	    output += 'visibility="hidden" zIndex="59000"';
	    output += '>\n';
	}

	if ( menu.borderstyle == 'shadow' ) {
	    output += '<table border="0" cellpadding="0" cellspacing="0"';
	    output += ' width="' + menu.width + '" bgcolor="' + menu.bgcolor + '">\n';
	    output += '<tr height="1">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="2" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="' + ( menu.width - 7 ) + '" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="5" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr>\n';
	    output += '<tr height="' + ( menu.height - 10 ) + '">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="4" height="' + ( menu.height - 10 ) + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td width="' + ( menu.width - 2 ) + '" height="' + ( menu.height - 10 ) + '" bgcolor="' + menu.bgcolor + '">';
	    output += '<table border="0" cellpadding="' + menu.padding + '" cellspacing="1"';
	    output += ' width="' + ( menu.width - 7 ) + '" height="' + ( menu.height - 10 ) + '">\n';
	    output += '<tr>\n';
	    output += '<td valign="top" >\n';
	} else if ( menu.borderstyle == 'image' ) {
	    output += '<table border="0" cellpadding="0" cellspacing="0"';
	    output += ' width="' + menu.width + '" bgcolor="' + menu.bgcolor + '">\n';
	    output += '<tr height="1">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="1" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="' + menu.leftimagewidth + '" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="' + ( menu.width - 7 ) + '" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="5" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr>\n';
	    output += '<tr height="' + ( menu.height - 10 ) + '">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="4" height="' + ( menu.height - 10 ) + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.leftimagecolor + '" background="' + menu.leftimage + '" width="' + menu.leftimagewidth + '" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="' + menu.leftimagewidth + '" height="1"></td>\n';
	    output += '<td width="' + ( menu.width - 2 ) + '" height="' + ( menu.height - 10 ) + '" bgcolor="' + menu.bgcolor + '">';
	    output += '<table border="0" cellpadding="' + menu.padding + '" cellspacing="1"';
	    output += ' width="' + ( menu.width - 7 ) + '" height="' + ( menu.height - 10 ) + '">\n';
	    output += '<tr>\n';
	    output += '<td valign="top" >\n';
	} else if ( menu.borderstyle == "solid" ) {
	    output += '<table border="0" cellpadding="0" cellspacing="0"';
	    output += ' width="' + menu.width + '" bgcolor="' + menu.bgcolor + '">\n';
	    output += '<tr height="1">\n';
	    output += '<td bgcolor="' + menu.bordercolor + '" height="' + menu.borderwidth + '" colspan="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr>\n';
	    output += '<td bgcolor="' + menu.bordercolor + '" width="' + menu.borderwidth + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td valign="top" bgcolor="' + menu.bgcolor + '">\n';
	    output += '<table border="0" cellspacing="0" cellpadding="' + menu.padding + '">';
	    output += '<tr><td>';
	} else {
	    output += '<table border="0" cellpadding="' + menu.padding + '" cellspacing="0"';
	    output += ' width="' + menu.width + '" bgcolor="' + menu.bgcolor + '">';
	    output += '<tr><td>\n';
	}
	
	// now add all the menu items
	for ( var j = 0; j < menu.items.length; j++ ) {
	    var mi = menu.items[j];
	    if ( mi.name == "separator" ) {
		if ( mi.hidden ) {
		    output += "<br>";
		}
		else {
		    output +=  '<hr class="magellan" size="1">' ;
		}
	    }
	    else {
		output += makeMenuItem( mi, false );
	    }
	    output += '\n';
	}
	
	if ( menu.borderstyle == 'shadow' ) {
	    // and add in the footer
	    output += '</tr>\n';
	    output += '</table></td>\n';
	    output += '</td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="5" height="' + ( menu.height - 10 ) + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr>\n';
	    output += '<tr height="3">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="2" height="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="' + ( menu.width - 7 ) + '" height="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="3" height="3">' ;
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr></table>\n';
	} else if ( menu.borderstyle == 'image' ) {
	    // and add in the footer
	    output += '</tr>\n';
	    output += '</table></td>\n';
	    output += '</td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="5" height="' + ( menu.height - 10 ) + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr>\n';
	    output += '<tr height="3">\n';
	    output += '<td bgcolor="' + menu.topshadow + '" width="2" height="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="' + menu.leftimagewidth + '" height="1">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="' + ( menu.width - 7 ) + '" height="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '<td bgcolor="' + menu.bottomshadow + '" width="3" height="3">' ;
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr></table>\n';
	} else if ( menu.borderstyle == 'solid' ) {
	    output += '</td></tr></table></td>';
	    output += '<td bgcolor="' + menu.bordercolor + '" width="' + menu.borderwidth + '">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td></tr>\n';
	    output += '<tr height="1">';
	    output += '<td bgcolor="' + menu.bordercolor + '" height="' + menu.borderwidth + '" colspan="3">';
	    output += '<img src="/content/images/spacer.gif" width="1" height="1"></td>\n';
	    output += '</tr></table>\n';
	} else {
	    output += '</td></tr></table>\n';
	}

	if ( navigator.appName == "Microsoft Internet Explorer" ) {
	    output += '</div>\n';
	}
	else {
	    output += '</layer>\n';
	}
	document.write( output );
    }
}

function Menu_getHeight() {
    var height = 0;
    var i;
    for ( i = 0; i < this.items.length; i++ ) {
	height += getObjHeight( this.items[i].name );
    }
    return( height );
}

function makeMenuItem( mi ) {
    var output = "";
    output += '<div class="magellandiv">';
    if ( mi.bold ) output += '<b>';
    output += '<a href="' + mi.value + '" class="magellanlink"';
    output += ' id="' + mi.menuname + mi.index + 'link"';
    output += ' onMouseOver="highlightLink(\'' + mi.menuname + '\',' + mi.index + ',true)"';
    output += ' onMouseOut="highlightLink(\'' + mi.menuname + '\',' + mi.index + ',false)"';
    output += '>';
    output +=  mi.name + '</a><br>' ;
    if ( mi.bold ) output += '</b>';
    output += '</div>';
    return( output );
}

if ( ! Opera ) {
    new MenuItem( "", "" );
    new Menu( 'discard' );
    Menu.prototype.width             = 110;
    Menu.prototype.height            = 10;
    Menu.prototype.imagename         = null;
    Menu.prototype.borderstyle       = "shadow";
    Menu.prototype.bordercolor       = "#000000";
    Menu.prototype.bgcolor           = "#ffffff";
    Menu.prototype.bottomshadow      = "#666666";
    Menu.prototype.topshadow         = "#cccccc";
    Menu.prototype.leftOffset        = 0;
    Menu.prototype.topOffset         = 0;
    Menu.prototype.padding           = 0;
    Menu.prototype.delay             = 50;
    Menu.prototype.rollover          = false;
    Menu.prototype.leftimage         = null;
    Menu.prototype.leftimagewidth    = 0;
    Menu.prototype.leftimagecolor    = '';
    Menu.prototype.addMenuItem       = Menu_addMenuItem;
    Menu.prototype.addMenuItemHeader = Menu_addMenuItemHeader;
    Menu.prototype.addMenuSeparator  = Menu_addMenuSeparator;
    Menu.prototype.getHeight         = Menu_getHeight;
    Menu.prototype.print             = Menu_print;
}

/* **********************************************************************
 * Create a function to print all menus.
 * **********************************************************************/

function printMenus() {
    var i = 0;
    for ( ; i < allmenus.length; i++ ) {
	allmenus[i].print();
    }
    return( i );
}

/* **********************************************************************
 * The following functions operate the menus.
 * **********************************************************************/


var timers     = new Object();

function highlightLink( menuName, itemidx, highlight ) {
    if ( ver4 ) {
	var anchor = getImage( menuName + itemidx + "link" );
	if ( anchor && IE4 ) {
	    anchor.className = ( highlight ? 'magellanlinkhi' : 'magellanlink' );
	}
    }
    return( false );
}

function showMenu( menuName ) {
    var imgName = getMenuImage( menuName );
    if ( imgName )
	setImage( imgName, "active" );
    if ( NS4 || ( IE4 && ! MAC ) ) {
	// Move the menu to a location near the image
	var layer = getLayer( menuName );
	if ( imgName && isHidden(layer) ) {
	    var img = getImage( imgName );
	    var x   = getImageLeft(img) + allmenus[menuName].leftOffset;
	    var y   = getImageTop(img) + getImageHeight(img) + allmenus[menuName].topOffset + 1;
	    moveObjTo( layer, x, y, true );
	    // Now, adjust the bottom of the menu with respect to the bottom
	    // of the window to make sure the entire menu is displayed.
	    // This is done after the initial move so the height of the
	    // object can be properly determined.
	    var lbottom = getObjTop( layer ) + getObjHeight( layer );
	    var wbottom = getInsideWindowHeight();
	    var wtop    = getWindowTop();
	    if ( lbottom > ( wbottom + wtop )) {
		y -= ( lbottom - ( wbottom + wtop ) );
		if ( y < wtop ) y = wtop;
		moveObjTo( layer, x, y, true );
		
	    }
	}
	// Display the menu
	showLayer( layer );
	clearTimeout( timers[menuName] );
    }
}

function hideMenu( menuName ){
    var cmd = 'hideMenu_delayed("' + menuName + '")';
    var delay = 50;
    if ( allmenus[menuName] )
	delay = allmenus[menuName].delay;
    if ( ver4 )
	timers[menuName] = setTimeout( cmd, delay );
    else
	eval( cmd );
}

function hideMenu_delayed( menuName ) {
    var imgName = getMenuImage( menuName );
    if ( imgName )
	setImage( imgName, "inactive" );
    if ( NS4 || ( IE4 && ! MAC ) )
	hideLayer( getLayer(menuName) );
}
