
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
 * WINDOW METHODS
 * **********************************************************************/

function getInsideWindowWidth() {
    var width = -1;
    if ( NS4 )
	width = window.innerWidth;
    else if ( IE4 )
	width = document.body.clientWidth;
    return( width );
}

function getInsideWindowHeight() {
    var height = -1;
    if ( NS4 )
	height = window.innerHeight;
    else if ( IE4 )
	height = document.body.clientHeight;
    return( height );
}

function getWindowLeft() {
    var x = -1;
    if ( NS4 )
	x = window.pageXOffset;
    else if ( IE4 )
	x = document.body.scrollLeft;
    return( x );
}

function getWindowTop() {
    var y = -1;
    if ( NS4 )
	y = window.pageYOffset;
    else if ( IE4 )
	y = document.body.scrollTop;
    return( y );
}

/* **********************************************************************
 * GENERIC OBJECT LOCATOR METHODS
 * **********************************************************************/

function getObject( objectName, container ) {
    var object = null;
    if ( typeof(objectName) != "string" ) {
	object = objectName;
    } else if ( ver4 ) {
	if ( ! container )
	    container = document;
	if ( IE4 ) {
	    if ( container.all[objectName] )
		object = container.all[objectName];
	} else if ( NS4 ) {
	    if ( container.anchors[objectName] )
		object = container.anchors[objectName];
	    else if ( container.forms[objectName] )
		object = container.forms[objectName];
	    else if ( container.images[objectName] )
		object = container.images[objectName];
	    else if ( container.layers[objectName] )
		object = container.layers[objectName];
	}
    }
    return( object );
}
		 
function findObjectContainer( objectName, container ) {
    var objectContainer = null;
    if ( NS4 ) {
	if ( ! container ) container = window.document;
	if ( getObject(objectName,container) ) {
	    objectContainer = container;
	} else {
	    var layers = getLayers( new RegExp(".*"), container );
	    for ( var i = 0; i < layers.length; i++ ) {
		objectContainer = findObjectContainer( objectName, layers[i].document );
		if ( objectContainer ) break;
	    }
	}
    }
    else if ( IE4 ) {
	objectContainer = getObject(objectName,document) ? document : null;
    }
    return( objectContainer );
}

function findObject( objectName ) {
    var object = null;
    if ( ver4 ) {
	var container = findObjectContainer( objectName );
	if ( container ) object = getObject( objectName, container );
    }
    return( object );
}

/* **********************************************************************
 * LAYER METHODS
 * **********************************************************************/

function getLayer( name ) {
    var obj = null;
    if ( IE4 ) {
	obj = document.all[name];
    } else if ( NS4 ) {
	obj = document.layers[name];
    }
    return( obj );
}

function getLayers( regex, doc ) {
    var layers = new Array();
    layers.length = 0;
    if ( ver4 ) {
	if ( ! doc ) doc = document;
	if ( NS4 ) {
	    for ( var i = 0; i < doc.layers.length; i++ ) {
		var obj = doc.layers[i];
		if ( ! regex || regex.exec(obj.id) ) {
		    layers[layers.length] = obj;
		}
	    }
	} else if ( IE4 ) {
	    var divColl = doc.all.tags("DIV");
	    for ( i = 0; i < divColl.length; i++ ) {
		if ( ! regex || regex.exec(divColl[i].id) ) {
		    layers[layers.length] = divColl[i];
		}
	    }
	}
    }
    return( layers );
}

function isHidden( obj ) {
    if ( IE4 ) {
	return( obj.style.visibility == "hidden" );
    } else if ( NS4 ) {
	return( obj.visibility == "hide" );
    } else {
	return( false );
    }
}

function hideLayer( obj ) {
    if ( IE4 )
	obj.style.visibility = "hidden";
    else if ( NS4 )
	obj.visibility = "hide";
}

function showLayer( obj ) {
    if ( IE4 ) {
	obj.style.visibility = "visible";
    } else if ( NS4 ) {
	obj.visibility = "show";
    }
}

function getObjTop( obj ) {
    if ( ver4 )
	return( IE4 ? obj.style.pixelTop  : obj.top );
    else
	return( 0 );
}

function getObjLeft( obj ) {
    if ( ver4 )
	return( IE4 ? obj.style.pixelLeft : obj.left );
    else
	return( 0 );
}

function getObjHeight( obj ) {
    if ( ver4 )
	return( IE4 ? obj.clientHeight : (obj.clip ? obj.clip.height : -1 ) );
    else
	return( 0 );
}

function getObjWidth( obj ) {
    if ( ver4 )
	return( IE4 ? obj.clientWidth  : (obj.clip ? obj.clip.width : -1 ) );
    else
	return( 0 );
}

function moveObjTo( obj, x, y, yesIfHidden ) {
    if ( ! isHidden(obj) || yesIfHidden ) {
	if ( IE4 ) {
	    if ( x >= 0 ) obj.style.pixelLeft = x;
	    if ( y >= 0 ) obj.style.pixelTop  = y;
	} else if ( NS4 ) {
	    if ( x < 0 ) x = getObjLeft( obj );
	    if ( y < 0 ) y = getObjTop( obj );
	    obj.moveTo( x, y );
	}
    }
    return( 1 );
}

/* **********************************************************************
 * IMAGE METHODS
 * **********************************************************************/

function getImage( imgNm ) {
    return( findObject(imgNm) );
}

function getImageContainer( imgNm ) {
    return( findObjectContainer(imgNm) );
}

function getParentObject( obj ) {
    var parent = null;
    if ( IE4 ) {
	if ( obj.offsetParent )
	    parent = obj.offsetParent;
    } else if ( NS4 ) {
	// If we're looking at a layer object (or a window), get
	// the parentWindow value, otherwise search for the container
	if ( obj.parentLayer ) {
	    parent = obj.parentLayer || null;
	} else {
	    parent = findObjectContainer( obj );
	    if ( parent ) parent = parent.parentWindow;
	}
    }
    return( parent );
}

function findOffsetValue( obj, value ) {
    var offset = 0;
    if ( ver4 ) {
	// Requested value is specified as an MSIE offset value.
	// Perform any translations necessary for other browsers.
	if ( NS4 ) {
	    if ( value == "offsetLeft" )
		value = "pageX";
	    else if ( value == "offsetTop" )
		value = "pageY";
	}
	var parent = getParentObject( obj );
	while ( parent ) {
	    offset += ( parent[value] || 0 );
	    parent = getParentObject( parent );
	}
    }
    return( offset || 0 );
}

function getImageLeft( img ) {
    if ( typeof(img) == "string" ) img = getImage( img );
    var left   = 0;
    var offset = findOffsetValue( img, "offsetLeft" );
    if ( NS4 ) {
	left = img.x;
    } else if ( IE4 ) {
	left = img.offsetLeft;
    }
    return( left + offset );
}

function getImageTop( img ) {
    if ( typeof(img) == "string" ) img = getImage( img );
    var top    = 0;
    var offset = findOffsetValue( img, "offsetTop" );
    if ( NS4 ) {
	top = img.y;
    } else if ( IE4 ) {
	top = img.offsetTop;
    }
    return( top + offset );
}

function getImageHeight( img ) {
    if ( typeof(img) == "string" ) img = getImage( img );
    return( img.height );
}

function getImageWidth( img ) {
    if ( typeof(img) == "string" ) img = getImage( img );
    return( img.width );
}
