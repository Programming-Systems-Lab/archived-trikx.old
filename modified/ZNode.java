/**
 * Copyright (C) 1998-2000 by University of Maryland, College Park, MD 20742, USA
 * All rights reserved.
 */
package edu.umd.cs.jazz;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.event.*;

import edu.umd.cs.jazz.io.*;
import edu.umd.cs.jazz.util.*;
import edu.umd.cs.jazz.event.*;

/**
 * <b>ZNode</b> is the common superclass of all objects in a
 * Jazz scenegraph. It has very limited functionality, and
 * primarily exists to support sub-classes.
 *
 * <P>
 * <b>Warning:</b> Serialized and ZSerialized objects of this class will not be
 * compatible with future Jazz releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Jazz. A future release of Jazz will provide support for long
 * term persistence.
 *
 * @author Ben Bederson
 */
public class ZNode extends ZSceneGraphObject implements ZSerializable, Serializable {
				// Default values
    static public final boolean savable_DEFAULT = true;      // True if this node gets saved
    static public final boolean pickable_DEFAULT = true;     // True if this node is pickable
    static public final boolean findable_DEFAULT = true;     // True if this node is findable
    static final boolean hasNodeListener_DEFAULT = false; // True if this node has a global bounds listener
    
    
				// Create the default editor factory
    static private ZSceneGraphEditorFactory editorFactory = new ZSceneGraphEditorFactory() {
	public ZSceneGraphEditor createEditor(ZNode node) {
	    return new ZSceneGraphEditor(node);
	}
    };

    /**
     * This node's parent.
     */
    ZGroup parent;

    /**
     *  True if this node should be saved
     */
    private boolean savable = savable_DEFAULT;

    /**
     *  True if this node is pickable
     */
    private boolean pickable = pickable_DEFAULT;

    /**
     *  True if this node is findable
     */
    private boolean findable = findable_DEFAULT;

    /**
     * True if this node has a global bounds listener
     * (package private for access in ZGroup)
     */
    boolean hasNodeListener = hasNodeListener_DEFAULT;

    /**
     * Set of client-specified properties for this node.
     */
    private ZProperty[] clientProperties = null;

    /**
     * Number of client properties for this node.
     */
    private int numClientProperties = 0;

    /**
     * A list of event listeners for this node.
     */
    protected transient EventListenerList listenerList = null;

	// Added by Kanan Naik

	protected String nodeName = null;
	protected String nodeLink = null;
      protected boolean scaled = false;

	public void setNodeName(String name)
	{
		this.nodeName = name;	
		return;
	}

	public String getNodeName()
	{
		return this.nodeName;
	}

	public void setNodeLink(String name)
	{
		this.nodeLink = name;	
		return;
	}

	public String getNodeLink()
	{
		return this.nodeLink;
	}

	public void setScaled(boolean value)
	{
		this.scaled = value;
	}

	public boolean isScaled()
	{
		return this.scaled;
	}

	// End  



    //****************************************************************************
    //
    //                Constructors
    //
    //***************************************************************************

    /**
     * Constructs a new ZNode.  The node must be attached to a live scenegraph (a scenegraph that is
     * currently visible) order for it to be visible.
     */
    public ZNode () {
	parent = null;
    }

    /**
     * Returns a clone of this object.
     *
     * @see ZSceneGraphObject#duplicateObject
     */
    protected Object duplicateObject() {
	ZNode newNode = (ZNode)super.duplicateObject();

				// Do a deep copy of client properties (if some)
	if (numClientProperties > 0) {
	    newNode.clientProperties = new ZProperty[numClientProperties];
	    for (int i=0; i<numClientProperties; i++) {
		newNode.clientProperties[i] = (ZProperty)clientProperties[i].clone();
	    }
	}

	newNode.parent = null;  // No parent - but see ZGroup.duplicateObject.

	return newNode;

	// JM - don't think we should copy listeners.
	//if (refNode.listenerList != null) {
	//    Object[] listeners = refNode.listenerList.getListenerList();
	//    for (int i=0; i<listeners.length/2; i+=2) {
	//	if (listenerList == null) {
	//	    listenerList = new EventListenerList();
	//	}
	//	listenerList.add((Class)listeners[i], (EventListener)listeners[i+1]);
	//    }
	//}
    }

    /**
     * Called to update internal object references after a clone operation 
     * by {@link edu.umd.cs.jazz.ZSceneGraphObject#clone}.
     *
     * @see ZSceneGraphObject#updateObjectReferences
     */
    protected void updateObjectReferences(ZObjectReferenceTable objRefTable) {
	super.updateObjectReferences(objRefTable);
	
	// Update client properties
	ZProperty prop;
	for (int i=0; i<numClientProperties; i++) {
	    prop = clientProperties[i];
	    clientProperties[i].updateObjectReferences(objRefTable);
	}
    }

    /**
     * Trims the capacity of the array that stores the clientProperties list points to
     * the actual number of points. Normally, the clientProperties list arrays can be
     * slightly larger than the number of points in the clientProperties list.
     * An application can use this operation to minimize the storage of a
     * clientProperties list.
     */
    public void trimToSize() {
	ZProperty[] newClientProperties = new ZProperty[numClientProperties];
	for (int i=0; i<numClientProperties; i++) {
	    newClientProperties[i] = clientProperties[i];
	}
	clientProperties = newClientProperties;
    }

    
    //****************************************************************************
    //
    // Convenience methods to manage node decorators
    //
    //***************************************************************************

    /**
     * Define how editors should be created.  This specifies a factory
     * that is used whenever an editor needs to be created.
     * @param factory The new factory to create editors with.
     * @see #editor
     */
    static public void setEditorFactory(ZSceneGraphEditorFactory factory) {
	editorFactory = factory;
    }
    
    /**
     * This returns a new instance of a ZSceneGraphEditor for this node.
     * ZSceneGraphEditor provides a convenience mechanism used to locate
     * and create instances of several node types.  The specific nodes
     * supported are defined by the particular editor being used.
     *
     * @see edu.umd.cs.jazz.util.ZSceneGraphEditor
     * @see edu.umd.cs.jazz.util.ZSceneGraphEditorFactory
     */
    public ZSceneGraphEditor editor() {
        return editorFactory.createEditor(this);
    }

    //****************************************************************************
    //
    // Event methods
    //
    //***************************************************************************

    /**
     * Determines if this node has any kind of mouse listener (i.e., mouse or mouse motion listener.)
     * @return true if this nodes does have at least one mouse listener
     */
    public boolean hasMouseListener() {
	boolean found = false;
	if (listenerList != null) {
				// Guaranteed to return a non-null array
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
		if ((listeners[i]==ZMouseListener.class) || (listeners[i]==ZMouseMotionListener.class)) {
		    found = true;
		    break;
		}
	    }
	}

	return found;
    }

    /**
     * Adds the specified mouse listener to receive mouse events from this node
     *
     * @param l the mouse listener
     */
    public void addMouseListener(ZMouseListener l) {
	if (listenerList == null) {
	    listenerList = new EventListenerList();
	}
        listenerList.add(ZMouseListener.class, l);
    }

    /**
     * Removes the specified mouse listener so that it no longer
     * receives mouse events from this mouse.
     *
     * @param l the mouse listener
     */
    public void removeMouseListener(ZMouseListener l) {
        listenerList.remove(ZMouseListener.class, l);
	if (listenerList.getListenerCount() == 0) {
	    listenerList = null;
	}
    }

    /**
     * Adds the specified mouse motion listener to receive mouse motion events from this node
     *
     * @param l the mouse motion listener
     */
    public void addMouseMotionListener(ZMouseMotionListener l) {
	if (listenerList == null) {
	    listenerList = new EventListenerList();
	}
        listenerList.add(ZMouseMotionListener.class, l);
    }

    /**
     * Removes the specified mouse motion listener so that it no longer
     * receives mouse motion events from this mouse.
     *
     * @param l the mouse motion listener
     */
    public void removeMouseMotionListener(ZMouseMotionListener l) {
        listenerList.remove(ZMouseMotionListener.class, l);
	if (listenerList.getListenerCount() == 0) {
	    listenerList = null;
	}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.  The listener list is processed in last to
     * first order.
     * <p>
     * If the event is consumed, then the event will not be passed
     * event listeners on the Component that the event came through.
     * @param e The mouse event
     * @see EventListenerList
     */
    public void fireMouseEvent(ZMouseEvent e) {
	if (listenerList == null) {
	    return;
	}

				// Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

				// Process the listeners last to first, notifying
				// those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ZMouseListener.class) {
		switch (e.getID()) {		    
		    
		case MouseEvent.MOUSE_PRESSED:
		    ((ZMouseListener)listeners[i+1]).mousePressed(e);
		    break;
		case MouseEvent.MOUSE_RELEASED:
		    ((ZMouseListener)listeners[i+1]).mouseReleased(e);
		    break;
		case MouseEvent.MOUSE_ENTERED:
		    ((ZMouseListener)listeners[i+1]).mouseEntered(e);
		    break;
		case MouseEvent.MOUSE_EXITED:
		    ((ZMouseListener)listeners[i+1]).mouseExited(e);
		    break;
		case MouseEvent.MOUSE_CLICKED:
		    ((ZMouseListener)listeners[i+1]).mouseClicked(e);
		    break;
		}
	    }
				// Don't process any more listeners if event was consumed
	    if (e.isConsumed()) {
		break;
	    }

            if (listeners[i]==ZMouseMotionListener.class) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_DRAGGED:
		    ((ZMouseMotionListener)listeners[i+1]).mouseDragged(e);
		    break;
		case MouseEvent.MOUSE_MOVED:
		    ((ZMouseMotionListener)listeners[i+1]).mouseMoved(e);
		    break;
		}
            }
				// Don't process any more listeners if event was consumed
	    if (e.isConsumed()) {
		break;
	    }
        }
    }

    /**
     * Adds the specified node listener to receive node events from this node.
     * Also updates the hasNodeListener bit.
     *
     * @param l the node listener
     */
    public void addNodeListener(ZNodeListener l) {
	if (listenerList == null) {
	    listenerList = new EventListenerList();
	}
        listenerList.add(ZNodeListener.class, l);

	if (!hasNodeListener) {
	    hasNodeListener = true;
	    if (parent != null) {
		parent.updateHasNodeListener();
	    }
	}
    }

    /**
     * Removes the specified node listener so that it no longer
     * receives node events from this node. Also updates the hasNodeListener
     * bit and notifies its parent of the change, if necessary.
     *
     * @param l the node listener
     */
    public void removeNodeListener(ZNodeListener l) {	
        listenerList.remove(ZNodeListener.class, l);
	if (listenerList.getListenerCount() == 0) {
	    listenerList = null;
	}

	if (listenerList == null || 
	    listenerList.getListenerCount(ZNodeListener.class) == 0) {
	    hasNodeListener = false;
	    if (parent != null) {
		parent.updateHasNodeListener();
	    }
	}
    }

    /**
     * fire a ZNodeEvent event on any listener listenening for it on this node.
     * Stop when event is consumed.
     */
    protected void fireEvent(ZNodeEvent e, int id, ZNode node) {
	                        // Guaranteed to return a non-null array
	if (node.listenerList != null) {
	    Object[] listeners = node.listenerList.getListenerList();
	    
				// Process the listeners last to first, notifying
				// those that are interested in this event
	    for (int i = listeners.length-2; i>=0; i-=2) {
		if (listeners[i]==ZNodeListener.class) {
		    switch (id) {
		    case ZNodeEvent.BOUNDS_CHANGED:
			((ZNodeListener)listeners[i+1]).boundsChanged(e);
			break;
		    case ZNodeEvent.GLOBAL_BOUNDS_CHANGED:
			((ZNodeListener)listeners[i+1]).globalBoundsChanged(e);
		    }
		}
		if (e.isConsumed()) {
		    break;
		}
	    }
	}
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type, percolate up the scenegraph
     * looking for listeners. Stop when the event is consumed. The event 
     * instance is lazily created using the parameters passed into
     * the fire method.  The listener list is processed in last to
     * first order.
     * @param id The event id (BOUNDS_CHANGED or GLOBAL_BOUNDS_CHANGED)
     * @param node The node being affected.
     * @see EventListenerList
     */
    protected void fireNodeEvent(int id, ZNode node) {
	ZNodeEvent e = new ZNodeEvent(node, id);
	do {
	    if (node.hasNodeListener) {
		fireEvent(e, id, node);
	    }
	    node = node.getParent();
	} while ((node != null) && (! e.isConsumed()));
    }

    //****************************************************************************
    //
    //                  Other Methods
    //
    //****************************************************************************

    /**
     * Remove this node, and any subtree, from the scenegraph.
     */
    public void remove() {
	if (parent != null) {
	    getParent().removeChild(this);
	}
    }

    /**
     * Extract this node from the tree, merging back in any children. As ZNode's
     * do not have any children, ZNode.extract() has the same function as ZNode.remove().
     * However, extract() on subclasses such as ZGroup truly extract a node, merging 
     * any children back into the scenegraph.
     * @see ZGroup#extract()
     */
    public void extract() {
	remove();
    }

    /**
     * Swaps this node out of the scenegraph tree, and replaces it with the specified
     * replacement node.  This node is left dangling, and it is up to the caller to
     * manage it.  The replacement node will be added to this node's parent in the same
     * position as this was.  That is, if this was the 3rd child of its parent, then
     * after calling replaceWith(), the replacement node will also be the 3rd child of its parent.
     * If this node has no parent when replace is called, then nothing will be done at all.
     *
     * @param replacement the new node that replaces the current node in the scenegraph tree.
     */
    public void replaceWith(ZNode replacement) {
	ZGroup parent = getParent();
	ZCamera camera;

	if (parent != null) {
	    repaint();		// Need to repaint old bounds of node.

				// Then, find out the position of this node in its parent child list,
				// and swap it with its replacement
	    ZNode[] cousins = parent.children;
	    for (int i=0; i<cousins.length; i++) {
		if (cousins[i] == this) {
		    setParent(null);
		    cousins[i] = replacement;
		    replacement.setParent(parent);
		    break;
		}
	    }
	}
    }

    /**
     * Raises this node within the drawing order of its siblings,
     * so it gets rendered above (after) all of its siblings.
     * This is done by moving this node to the end of its parent's child list.
     */
    public void raise() {
	if (parent != null) {
	    parent.raise(this);
	}
    }

    /**
     * Raises this node within the drawing order of its siblings,
     * so it gets rendered above (after) the specified node.
     * This is done by moving this node just after the specified node in its parent's child list.
     * If the specified node is not a sibling of this, then this call does nothing.
     * <p>
     * If the specified node is null, then this node is raised to be the
     * last node rendered of its siblings (i.e., equivalent to calling {@link #raise}
     *
     * @param afterNode The node to raise this node after.
     */
     public void raiseTo(ZNode afterNode) {
	 if (parent != null) {
	     if (afterNode == null) {
		 parent.raise(this);
	     } else {
		 parent.raiseTo(this, afterNode);
	     }
	 }
    }

    /**
     * Lowers this node within the drawing order of its siblings,
     * so it gets rendered below (before) all of its siblings.
     * This is done by moving this node to the beginning of its parent's child list.
     */
    public void lower() {
	if (parent != null) {
	    parent.lower(this);
	}
    }

    /**
     * Lowers this node within the drawing order of its siblings,
     * so it gets rendered below (before) the specified node.
     * This is done by moving this node just before the specified node in its parent's child list.
     * If the specified node is not a sibling of this, then this call does nothing.
     * <p>
     * If the specified node is null, then this node is lowered to be the
     * first node rendered of its siblings (i.e., equivalent to calling {@link #lower}
     *
     * @param beforeNode The node to lower this node before.
     */
     public void lowerTo(ZNode beforeNode) {
	 if (parent != null) {
	     if (beforeNode == null) {
		 parent.lowerTo(this);
	     } else {
		 parent.lowerTo(this, beforeNode);
	     }
	 }
    }

    //****************************************************************************
    //
    // Get/Set pairs
    //
    //***************************************************************************

    /**
     * Set the parent of this node.  If it already was in the tree, then
     * this moves the node to a new place in the tree specified by the new parent.
     * This is equivalent to removing this from its original parent, and adding
     * it to its new parent.
     * @param newParent The new parent of this node.
     */
    public void setParent(ZGroup newParent) {
	if (parent != null) {
	    parent.removeChild(this);
	}
	if (newParent != null) {
	    newParent.addChild(this);
	}
    }

    /**
     * Set the parent of this node, and transform the node in such a way that it
     * doesn't move in global coordinates.  If the node does not already have a
     * transform node associated with it, then one will be created.
     * This method operates on the handle (top) node of a decorator chain.     
     * <P>
     * This method may fire NODE_ADDED or NODE_REMOVED ZGroupEvents.
     * ZGroupEvents now contain a method <code>isModificationEvent()</code> to
     * distinguish a modification event from a <bold>true</bold> node addition
     * or removal.  A modification event is one in which a node changes
     * position in a single scenegraph or between two different scenegraphs.
     * A true addition or removal event is one in which a node is first
     * added to or removed from a scenegraph.     
     * @param newParent The new parent of this node.
     * @see ZGroupEvent
     */
    public void reparent(ZGroup newParent) {
	AffineTransform origAT;
	AffineTransform newParentAT;
	AffineTransform newAT = null;
	ZNode node = editor().getNode();
	ZTransformGroup transform;

	origAT = node.getLocalToGlobalTransform();
	newParentAT = newParent.getLocalToGlobalTransform();

	try {
	    newAT = newParentAT.createInverse();
	} catch (NoninvertibleTransformException exc) {
	    System.out.println("ZNode.reparent: Can't invert transform");
	    return;
	}
	newAT.concatenate(origAT);

	ZGroup origParent = parent;	
	if (parent != null) {
	    parent.removeChild(this,false);
	}
	if (newParent != null) {
	    newParent.addChildImpl(this,false);
	}

	// If the new parent is null - then this was just a normal
	// removeChild and not a modification
	if (origParent != null &&
	    newParent == null) {
	    origParent.fireGroupEvent(ZGroupEvent.NODE_REMOVED,this,false);
	}
	else if (origParent != null) {
	    origParent.fireGroupEvent(ZGroupEvent.NODE_REMOVED,this,true);
	}

	// If the original parent was null - then this was just a normal
	// addChild and not a modification
	if (newParent != null &&
	    origParent == null) {
	    parent.fireGroupEvent(ZGroupEvent.NODE_ADDED,this,false);
	}
	else if (newParent != null) {
	    parent.fireGroupEvent(ZGroupEvent.NODE_ADDED,this,true);
	}
	
	transform = node.editor().getTransformGroup();
	transform.setTransform(newAT);
    }

    /**
     * Get the node's parent.
     */
    public final ZGroup getParent() {
	return parent;
    }

    /**
     * Internal method to compute and cache the volatility of a node,
     * to recursively call the parents to compute volatility.
     * All parents of this node are also volatile when this is volatile.
     * @see #setVolatileBounds(boolean)
     * @see #getVolatileBounds()
     */
    protected void updateVolatility() {
				// Update parent's volatility
	if (parent != null) {
	    parent.updateVolatility();
	}
    }

    /**
     * Determine if this node gets saved when written out.
     * @return true if this node gets saved.
     */
    public final boolean isSavable() {
	return savable;
    }

    /**
     * Specify if this node should be saved.  If not, then all references to this
     * will be skipped in saved files.
     * @param s true if node should be saved
     */
    public void setSavable(boolean s) {
	savable = s;
    }

    /**
     * Specifies whether this node is pickable.
     * When a node is not pickable, it will be ignored by
     * the ZNode pick methods that determine which object is under a point.
     * @param pickable True if this node should be pickable.
     */
    public void setPickable(boolean pickable) {
	this.pickable = pickable;
    }

    /**
     * Determines if this node is pickable.
     * When a node is not pickable, it will be ignored by
     * the ZNode pick methods that determine which object is under a point.
     * @return True if this node is pickable
     */
    public final boolean isPickable() {
	return pickable;
    }

    /**
     * Specifies whether this node is findable.
     * When a node is not findable, it will be ignored by
     * the ZNode find methods that search the scenegraph for nodes that satisfy
     * some criteria.
     * @param findable True if this node should be findable.
     */
    public void setFindable(boolean findable) {
	this.findable = findable;
    }

    /**
     * Determines if this node is findable.
     * When a node is not findable, it will be ignored by
     * the ZNode find methods.
     * @return True if this node is findable
     */
    public final boolean isFindable() {
	return findable;
    }

    /**
     * Determines if this node has a node listener.
     * If this node does not have a node listener, it will not
     * receive global bounds events.
     */
    public final boolean hasNodeListener() {
	return hasNodeListener;
    }

    //****************************************************************************
    //
    // Painting related methods
    //
    //***************************************************************************

    /**
     * Renders this node.
     * <p>
     * The transform, clip, and composite will be set appropriately when this object
     * is rendered.  It is up to this object to restore the transform, clip, and composite of
     * the Graphics2D if this node changes any of them. However, the color, font, and stroke are
     * unspecified by Jazz.  This object should set those things if they are used, but
     * they do not need to be restored.
     *
     * @param renderContext The graphics context to use for rendering.
     */
    public void render(ZRenderContext renderContext) {
    }

    /**
     * Return a copy of the bounds of the subtree rooted at this node in global coordinates.
     * Note that global bounds are not cached, and this method involves some computation.
     * @return The bounds of the subtree rooted at this node in global coordinates.
     */
    public ZBounds getGlobalBounds() {
	ZBounds globalBounds = getBounds();
	if (parent != null) {
	    AffineTransform at = parent.getLocalToGlobalTransform();
	    globalBounds.transform(at);
	}

	return globalBounds;
    }

    /**
     * Transform the specified point (in global coordinates) to local coordinates
     * in this node's coordinate system.
     * The input point is modified by this method.
     * It also returns the change in scale from the global coordinate system
     * to the node's local coordinate system.
     * @return dz The change in scale from global coordinates to the node's local coordinate system.
     * @see #localToGlobal
     */
    public double globalToLocal(Point2D pt) {
	double dz = 1.0;
	AffineTransform at = getLocalToGlobalTransform();
	try {
	    AffineTransform inverse = at.createInverse();
	    inverse.transform(pt, pt);
	    dz = Math.max(inverse.getScaleX(), inverse.getScaleY());
	} catch (NoninvertibleTransformException e) {
	    System.out.println(e);
	}

	return dz;
    }

    /**
     * Transform the specified rectangle (in global coordinates) to local coordinates
     * in this node's coordinate system.
     * The input rectangle is modified by this method.
     * It also returns the change in scale from the global coordinate system
     * to the node's local coordinate system.
     * @return dz The change in scale from global coordinates to the node's local coordinate system.
     * @see #localToGlobal
     */
    public double globalToLocal(Rectangle2D rect) {
	double dz = 1.0;
	AffineTransform at = getLocalToGlobalTransform();
	try {
	    AffineTransform inverse = at.createInverse();
	    ZTransformGroup.transform(rect, inverse);
	    dz = Math.max(inverse.getScaleX(), inverse.getScaleY());
	} catch (NoninvertibleTransformException e) {
	    System.out.println(e);
	}

	return dz;
    }

    /**
     * Transform the specified point (in this node's local coordinates) to global coordinates.
     * The input point is modified by this method.
     * It also returns the change in scale from the local coordinate system
     * to global coordinates.
     * @return dz The change in scale from global coordinates to the node's local coordinate system.
     * @see #globalToLocal
     */
    public double localToGlobal(Point2D pt) {
	double dz;
	AffineTransform at = getLocalToGlobalTransform();
	at.transform(pt, pt);
	dz = Math.max(at.getScaleX(), at.getScaleY());

	return dz;
    }

    /**
     * Transform the specified rectangle (in this node's local coordinates) to global coordinates.
     * The input rectangle is modified by this method.
     * It also returns the change in scale from the local coordinate system
     * to global coordinates.
     * @return dz The change in scale from global coordinates to the node's local coordinate system.
     * @see #globalToLocal
     */
    public double localToGlobal(Rectangle2D rect) {
	double dz;
	AffineTransform at = getLocalToGlobalTransform();
	ZTransformGroup.transform(rect, at);
	dz = Math.max(at.getScaleX(), at.getScaleY());

	return dz;
    }

    /**
     * Return the transform that converts local coordinates at this node to global coordinates
     * at the root node.
     * @return The concatenation of transforms from the root down to this node.
     */
    public AffineTransform getLocalToGlobalTransform() {
	AffineTransform result = new AffineTransform();

	if (parent != null) {
	    result.preConcatenate(parent.getLocalToGlobalTransform());
	}

	return result;
    }

    /**
     * Return the transform that converts global coordinates at the root node
     * to local coordinates at this node.
     * @return The inverse of the concatenation of transforms from the root down to this node.
     */
    public AffineTransform getGlobalToLocalTransform() {
	AffineTransform at = getLocalToGlobalTransform();
	AffineTransform globalToLocal = null;

	try {
	    globalToLocal = at.createInverse();
	} catch (NoninvertibleTransformException exc) {
	    System.out.println("ZNode.getGlobalToLocalTransform: Can't invert transform");
	}

	return globalToLocal;
    }

    /**
     * Repaint causes the portions of the surfaces that this object
     * appears in to be marked as needing painting, and queues events to cause
     * those areas to be painted. The painting does not actually
     * occur until those events are handled.
     * If this object is visible in multiple places because more than one
     * camera can see this object, then all of those places are marked as needing
     * painting.
     * <p>
     * Scenegraph objects should call repaint when their internal
     * state has changed and they need to be redrawn on the screen.
     * <p>
     * Important note : Scenegraph objects should call reshape() instead
     * of repaint() if the internal state change effects the bounds of the
     * shape in any way (e.g. changing penwidth, selection, transform, adding
     * points to a line, etc.)
     *
     * @see #reshape()
     */
    public void repaint() {
	if (ZDebug.debug && ZDebug.debugRepaint) {
	    System.out.println("ZNode.repaint: this = " + this);
	    if (parent != null) {
		System.out.println("ZNode.repaint: bounds = " + getBounds());
	    }
	}

	if (parent != null) {
	    parent.repaint(this, new AffineTransform(), null);
	}
    }

    /**
     * Method to pass repaint methods up the tree.  Repaints only the
     * sub-portion of this object specified by the given ZBounds.
     * Note that the input parameter may be modified as a result of this call.
     * @param repaintBounds The bounds to repaint
     */
    public void repaint(ZBounds repaintBounds) {
	if (ZDebug.debug && ZDebug.debugRepaint) {
	    System.out.println("ZNode.repaint(ZBounds): this = " + this);
	    System.out.println("ZNode.repaint(ZBounds): bounds = " + repaintBounds);
	}

	if (parent != null) {
	    parent.repaint(repaintBounds);
	}
    }

    /**
     * Method to pass repaint methods up the tree.  Repaints the area
     * of the surface that is covered by the bounds of this object.
     * Note that the transform and clipBounds parameters may be modified as a result of this call.
     * @param obj The object to repaint
     * @param at  The affine transform
     * @param clipBounds The bounds to clip to when repainting
     */
    public void repaint(ZSceneGraphObject obj, AffineTransform at, ZBounds clipBounds) {
	if (ZDebug.debug && ZDebug.debugRepaint) {
	    System.out.println("ZNode.repaint(obj, at, bounds): this = " + this);
	    System.out.println("ZNode.repaint(obj, at, bounds): obj = " + obj);
	    System.out.println("ZNode.repaint(obj, at, bounds): at = " + at);
	    System.out.println("ZNode.repaint(obj, at, bounds): clipBounds = " + clipBounds);
	}

	if (parent != null) {
	    parent.repaint(obj, at, clipBounds);
	}
    }

    /**
     * Internal method that causes this node and all of its ancestors
     * to recompute their bounds.
     */
    protected void updateBounds() {
	computeBounds();
	fireNodeEvent(ZNodeEvent.BOUNDS_CHANGED, this);
	fireNodeEvent(ZNodeEvent.GLOBAL_BOUNDS_CHANGED, this);
	if (parent != null) {
	    boolean hadListener = hasNodeListener;
	    hasNodeListener = false;
	    parent.updateBounds();
	    hasNodeListener = hadListener;
	}
    }

    //****************************************************************************
    //
    //			Other Methods
    //
    //****************************************************************************

    /**
     * Returns the first object under the specified rectangle (if there is one)
     * in the subtree rooted with this as searched in reverse (front-to-back) order.
     * Only returns "pickable" nodes.
     * @param rect Coordinates of pick rectangle in local coordinates
     * @param path The path through the scenegraph to the picked node. Modified by this call.
     * @return The picked node, or null if none
     * @see ZDrawingSurface#pick(int, int)
     */

   public boolean pick(Rectangle2D rect, ZSceneGraphPath path) {
	if (pickable) {
	    if (getBoundsReference().intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())) {
		path.setObject(this);
		return true;
	    }
	}

	return false;
    }

    /**
     * Internal method used to return the list of nodes that are accepted by the specified filter in the
     * subtree rooted with this.  If this node is not "findable", then neither
     * this node, nor any of its descendants will be included.
     * The filter specifies whether or not this node should be accepted by the
     * search, and whether the node's children should be searched.
     * @param filter The filter that decides whether or not to include individual nodes in the find list
     * @param nodes the accumulation list (results will be place here).
     * @return the number of nodes searched
     * @see #isFindable()
     * @see ZFindFilter
     */
    protected int findNodes(ZFindFilter filter, ArrayList nodes) {
	int nodesSearched = 1;

				// Only search if node is findable.
	if (findable) {
				// Check if this node is accepted by the filter
	    if (filter.accept(this)) {
		nodes.add(this);
	    }
	}

	return nodesSearched;
    }

    /**
     * Traverse the tree, find the root node, and return it.
     * @return The root node of this scenegraph
     * @see ZRoot
     */
    public ZRoot getRoot() {
	ZNode node = this;

	do {
	    if (node instanceof ZRoot) {
		return (ZRoot)node;
	    }
	    node = node.getParent();
	} while (node != null);

	return null;
    }

    /**
     * Method to determine if this is a descendent of queryNode.
     * @param queryNode a possible ancenstor of node
     * @return true of queryNode is an ancestor of node.
     */
    public boolean isDescendentOf(ZNode queryNode) {
	ZNode node = parent;
	boolean found = false;

	while (node != null) {
	    if (node == queryNode) {
		found = true;
		break;
	    }
	    node = node.getParent();
	}
	return found;
    }

    /**
     * Method to determine if this is an ancenstor of queryNode.
     * @param queryNode a possible descendent of node
     * @return true of queryNode is an descendent of node.
     */
    public boolean isAncestorOf(ZNode queryNode) {
	return queryNode.isDescendentOf(this);
    }

    /**
     * Add an arbitrary key/value "client property" to this component.
     * <p>
     * The <code>get/putClientProperty<code> methods provide access to
     * a small per-instance hashtable. Callers can use get/putClientProperty
     * to annotate components that were created by another module.
     * <p>
     * If value is null this method will remove the property.
     *
     * @see #getClientProperty
     */
    public void putClientProperty(Object key, Object value) {
	int i;
	ZProperty prop;

	if (value == null) {
				// Null value - try to remove property
	    for (i=0; i<numClientProperties; i++) {
		prop = clientProperties[i];
		if (prop.getKey().equals(key)) {
				// Remove property by sliding down array elements.
		    for (int j=i; j<(numClientProperties - 1); j++) {
			clientProperties[j] = clientProperties[j+1];
		    }
		    numClientProperties--;

				// If no properties left, then remove data
		    if (numClientProperties == 0) {
			clientProperties = null;
		    }
		    break;
		}
	    }
	} else {
				// Add property
				// First check to see if property already exists in which case we just update it
	    boolean found = false;
	    for (i=0; i<numClientProperties; i++) {
		prop = clientProperties[i];
		if (prop.getKey().equals(key)) {
		    prop.set(key, value);
		    found = true;
		    break;
		}
	    }
	    if (!found) {
				// Or, last case - add new property
		prop = new ZProperty(key, value);
		addClientProperty(prop);
	    }
	}
    }

    /**
     * Internal method to add the specified property.
     * @param prop The new property.
     */
    protected void addClientProperty(ZProperty prop) {
	if (clientProperties == null) {
	    clientProperties = new ZProperty[1];
	}

				// Possibly allocate space.
	try {
	    clientProperties[numClientProperties] = prop;
	} catch (ArrayIndexOutOfBoundsException e) {
	    ZProperty[] newProps = new ZProperty[(numClientProperties == 0) ? 1 : (2 * numClientProperties)];
	    System.arraycopy(clientProperties, 0, newProps, 0, numClientProperties);
	    clientProperties=  newProps;
	    clientProperties[numClientProperties] = prop;
	}
	numClientProperties++;
    }

    /**
     * Returns the value of the property with the specified key.  Only
     * properties added with <code>putClientProperty</code> will return
     * a non-null value.
     *
     * @return the value of this property or null
     * @see #putClientProperty
     */
    public Object getClientProperty(Object key) {
	Object value = null;
	ZProperty prop;

	for (int i=0; i<numClientProperties; i++) {
	    prop = clientProperties[i];
	    if (prop.getKey().equals(key)) {
		value = prop.getValue();
		break;
	    }
	}

	return value;
    }

    /**
     * Generate a string that represents this object for debugging.
     * @return the string that represents this object for debugging
     * @see ZDebug#dump
     */
    public String dump() {
	String str = super.dump();
	ZProperty prop;

	for (int i=0; i<numClientProperties; i++) {
	    prop = clientProperties[i];
	    str += "\n Property '" + prop.getKey() + "': " + prop.getValue();
	}

	return str;
    }

    /////////////////////////////////////////////////////////////////////////
    //
    // Saving
    //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Write out all of this object's state.
     * @param out The stream that this object writes into
     */
    public void writeObject(ZObjectOutputStream out) throws IOException {
	super.writeObject(out);

	if (pickable != pickable_DEFAULT) {
	    out.writeState("boolean", "pickable", pickable);
	}
	if (findable != findable_DEFAULT) {
	    out.writeState("boolean", "findable", findable);
	}
	if (clientProperties != null) {
				// Array can have some empty slots, so copy them over
	    ZProperty[] copyClientProperties = new ZProperty[numClientProperties];
	    System.arraycopy(clientProperties, 0, copyClientProperties, 0, numClientProperties);
	    out.writeState("List", "properties", Arrays.asList(copyClientProperties));
	}
    }

    /**
     * Specify which objects this object references in order to write out the scenegraph properly
     * @param out The stream that this object writes into
     */
    public void writeObjectRecurse(ZObjectOutputStream out) throws IOException {
	super.writeObjectRecurse(out);

				// Add properties
	for (int i=0; i<numClientProperties; i++) {
	    out.addObject(clientProperties[i]);
	}
    }

    /**
     * Set some state of this object as it gets read back in.
     * After the object is created with its default no-arg constructor,
     * this method will be called on the object once for each bit of state
     * that was written out through calls to ZObjectOutputStream.writeState()
     * within the writeObject method.
     * @param fieldType The fully qualified type of the field
     * @param fieldName The name of the field
     * @param fieldValue The value of the field
     */
    public void setState(String fieldType, String fieldName, Object fieldValue) {
	super.setState(fieldType, fieldName, fieldValue);

 	if (fieldName.compareTo("pickable") == 0) {
	    setPickable(((Boolean)fieldValue).booleanValue());
	} else if (fieldName.compareTo("findable") == 0) {
	    setFindable(((Boolean)fieldValue).booleanValue());
	} else if (fieldName.compareTo("properties") == 0) {
	    ZProperty prop;
	    for (Iterator i=((Vector)fieldValue).iterator(); i.hasNext();) {
		prop = (ZProperty)i.next();
		addClientProperty(prop);
	    }
	}
    }

    /**
     * Node doesn't get written out if save property is false
     */
    public ZSerializable writeReplace() {
	if (savable) {
	    return this;
	} else {
	    return null;
	}
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
	trimToSize();   // Remove extra unused array elements
	out.defaultWriteObject();
    }
}
