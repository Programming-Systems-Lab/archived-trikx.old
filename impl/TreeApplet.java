package psl.trikx.impl;

/**
 * Title: TreeApplet
 *
 * Description: Use Tree.html to execute this applet.  A main applet
 * file to create a tree using jazz tool. It requires jazz.jar file
 * associated with it. It reads a user input (nodename= parent name,
 * link) from nodefile.txt and creates a tree depending upon that. A
 * user can click twice on a node to open an associated link of right
 * frame. He can click a right mouse button and drag a mouse left and
 * right to zoom a tree.
 *
 * Copyright (c) 2000: The Trustees of Columbia University and the
 * City of New York.  All Rights Reserved.
 *
 * @author Kanan Naik
 * @version 1.0 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.applet.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.component.*;
import edu.umd.cs.jazz.event.*;
import edu.umd.cs.jazz.util.*;
import edu.umd.cs.jazz.io.*;

public class TreeApplet extends JApplet implements ITreeApplet
{
  protected ZNode startNode = null;
  ZLayoutGroup mainLayout = null;
  ZTreeLayoutManager manager = new ZTreeLayoutManager();
  protected ZCanvas canvas;
  Font font = new Font("", Font.PLAIN, 15);
  Font highlightFont = new Font("", Font.BOLD, 25);
  Color color = null;
  Vector highlightedNode = new Vector(10);

  /**
   * Shutdown the server flag.
   */
  boolean stopServer = false;

  public TreeApplet() { ; }

  /**
   * Intializes an applet and set pan and zoom event handlers.
   * It also adds a tool bar to provide features like - changing
   * link layout and orientation of tree.
   */
  public void init() {
    //    setBackground(null);
    setBackground(Color.white);
    setVisible(true);

    // Set up the basic setup
    getContentPane().setLayout(new BorderLayout());

    // Create a basic Jazz scene
    //    ScrollPane sp = new ScrollPane();
    canvas = new ZCanvas();
    //    sp.add(canvas);
    //    getContentPane().add(sp, BorderLayout.CENTER);
    getContentPane().add(canvas);
    ZoomEventHandler ze = (ZoomEventHandler)canvas.getZoomEventHandler();
    ze.setActive(true);
    ze.setMaxMagnification(3);
    ze.setMinMagnification(0.6);
    Panel toolBar = buildToolBar();
    getContentPane().add(toolBar, BorderLayout.NORTH);
    validate();

    // Set up event handlers
    ZSelectionEventHandler selectionHandler =
      new ZSelectionEventHandler(canvas.getCameraNode(), canvas, 
				 canvas.getLayer());
    selectionHandler.setActive(true);
    //setLinkStyle(ZTreeLayoutManager.LINK_ANGLEDLINE);

    canvas.addMouseListener(new MouseAdapter() {
	public void mousePressed(MouseEvent me) {
	  canvas.requestFocus();
	}
      });

    initScreen();
    startServer();

    /*while(true)
      {
      System.out.println("enter a node name");
      int i;
      String input = "";
      boolean finish = false;
      while (!finish)
      {
      try
      {
      i = System.in.read();
      if (i == '\n')
      finish = true;
      else
      input = input + (char) i;
      }
      catch(java.io.IOException e)
      {
      finish = true;
      }
      }
      input = input.trim();
      try
      {
      focusNode("root");
      clearFocusNode();
      }
      catch(NodeNotFoundException ex)
      {
      }
      }*/
  }
    
  /**
   * Builds a tool bar. It provides features like - changing a link
   * representation in a tree (angled or straight)and changing an
   * orientation of tree (horizontal or vertical)
   */
  public Panel buildToolBar()
  {
    Panel toolBar = new Panel();
    toolBar.setLayout(new GridLayout(1,4));
    CheckboxGroup angleGroup = new CheckboxGroup();
    Checkbox angleLinkButton = new Checkbox("Angled", angleGroup, false);
    angleLinkButton.addItemListener(new ItemListener() {
	public void itemStateChanged(ItemEvent e) {
	  if(e.getStateChange() == ItemEvent.SELECTED) {
	    setLinkStyle(ZTreeLayoutManager.LINK_ANGLEDLINE);
	    repaint();
	  }
	}
      });
    Checkbox straightLinkButton = new Checkbox("Straight", angleGroup, true);
    straightLinkButton.addItemListener(new ItemListener() {
	public void itemStateChanged(ItemEvent e) {
	  if(e.getStateChange() == ItemEvent.SELECTED) {
	    setLinkStyle(ZTreeLayoutManager.LINK_STRAIGHTLINE);
	    repaint();
	  }
	}
      });
    
    toolBar.add(straightLinkButton);
    toolBar.add(angleLinkButton);

    CheckboxGroup orientGroup = new CheckboxGroup();
    Checkbox horizontal = new Checkbox("Horizontal", orientGroup, false);
    horizontal.addItemListener(new ItemListener() {
	public void itemStateChanged(ItemEvent e) {
	  if(e.getStateChange() == ItemEvent.SELECTED) {
	    setOrientation(ZTreeLayoutManager.ORIENT_HORIZONTAL);
	    repaint();
	  }
	}
      });
    Checkbox vertical = new Checkbox("Vertical", orientGroup, true);
    vertical.addItemListener(new ItemListener() {
	public void itemStateChanged(ItemEvent e) {
	  if(e.getStateChange() == ItemEvent.SELECTED) {
	    setOrientation(ZTreeLayoutManager.ORIENT_VERTICAL);
	    repaint();
	  }
	}
      });

    toolBar.add(vertical);
    toolBar.add(horizontal);

    return toolBar;
  }

  public void setLinkStyle(int wanted) {
    if (wanted != manager.getLinkStyle())
      {
	manager.setLinkStyle(mainLayout, wanted);
	canvas.getDrawingSurface().repaint();
      }
  }



  public void setOrientation(int wanted)
  {
    if (wanted != manager.getCurrentOrientation())
      {
	manager.setCurrentOrientation(mainLayout, wanted);
	canvas.getDrawingSurface().repaint();
      }
  }

  /**
   * Accepts a request to add a node to scenegraph. Node name
   * must be unique or it will throw a NodeNameNotUnique exception.
   * The node which should be set as a parent node must already be
   * added to a tree or it will throw a NodeNotPresent exception.
   */
  public void addNode(String node1, String parent, String link)
    throws NodeNotFoundException, NodeNameNotUniqueException
  {
    ZNode zn = null;
    if(NodeGroup.getNode(node1) != null)
    {
	throw new NodeNameNotUniqueException(node1);
    }
    if(parent != null)
    {
	zn = NodeGroup.getNode(parent);
	if (zn == null)
	{
	    throw new NodeNotFoundException(parent);
	}
	ZGroup grp = (ZGroup)zn;
	grp.setHasOneChild(false);
    }
    else if(startNode != null)
    {
	throw new NodeNotFoundException();
    }
    /*
      try { 
      Thread.currentThread().sleep(50);
      } catch(InterruptedException e) { ; }
    */
    addAChild(zn, node1, link);
  }

  /**
   * Accepts a node name, selects it and calls removeAChild(). It throws
   * NodeNotFound exception if requested node is not present in a tree.
   */
  public void removeNode(String node1)
    throws NodeNotFoundException
  {
    ZNode zn = NodeGroup.getNode(node1);
    if (zn == null)
      {
	throw new NodeNotFoundException(node1);
      }
    ZSelectionGroup.select(zn);
    removeAChild();
  }

  /**
   * Opens nodefile.txt file and reads nodename, parent name
   * and associated link from it. If parent node is not present
   * and tree is not intialized yet, it will add that node as a
   * root node.
   */
  public void initScreen()
  {
    //Properties props= new Properties();
    String[][] input = InputList.getInputList();
    String name = input[0][0];
    int i = 0;
    String  parent= null;
    String link = null;
    //String text = null;
    try
      {
	while(name != null)
	  {
	    parent = input[i][1];
	    //text = input[i][2];
	    link = input[i][2];
	    if(parent.equals("null"))
	      {
		parent = null;
	      }
	    System.out.println("ADDING:   " + name+" "+parent+" "+link);
	    try
	      {
		addNode(name, parent, link);
	      }
	    catch (NodeNotFoundException e) { ; }
	    catch(NodeNameNotUniqueException ex) { ; }
	    i++;
	    name = input[i][0];
	  }
      }
    catch(Exception e) {
    
    }
  }

  /**
   * This is method  is called by addNode method. It will add a root
   * node and intializes a tree if no node was selected  else
   * it will add a new node as a child of a selected node. It will set
   * a node name and a link given by addNode.
   */
  public void addAChild(ZNode parent, String name, String link)
  {
    ZVisualComponent vis = null;
    ZVisualGroup child = null;
    ZNode node= null;
    ZGroup group=null;
    ZLayoutGroup layout = null;
    ZText zt = null;
    String textValue = name;

    //add a root node
    if(parent == null)
      {
	zt = new ZText(textValue, font);
	color = zt.getPenColor();
	group = new ZVisualGroup(zt, null);
	mainLayout = group.editor().getLayoutGroup();
	mainLayout.setLayoutChild(group);
	mainLayout.setLayoutManager(manager);
	canvas.getLayer().addChild(group.editor().getTop());
	startNode = group;
	ZTransformGroup t =startNode.editor().getTransformGroup();
	t.translate(150,50);
	node = startNode;
      }

    // add a child node
    else
      {
	group = (ZGroup)parent;
	zt = new ZText(textValue, font);
	vis = zt;
	child = new ZVisualGroup(vis, null);
	layout = child.editor().getLayoutGroup();
	layout.setLayoutChild(child);
	layout.setLayoutManager(manager);
	node = child;
	group.addChild(node.editor().getTop());
      }

    //set node name and link for future references
    node.setNodeName(name);
    node.setNodeLink(link);
    NodeGroup.putNode(node);
    NodeGroup.putComponent(node, zt);
    ZSelectionGroup.unselectAll(canvas.getLayer());
    node.setVolatileBounds(true);

    //add a mouse event handler with a node. A page associated with a node
    //is opened if a node is clicked twice.
    ZSelectionEventHandler sh =
      new ZSelectionEventHandler(node, canvas, canvas.getLayer());
    sh.setActive(true);
    node.addMouseListener(new ZMouseAdapter() 
    {
	public void mouseClicked(ZMouseEvent me) 
      {
	  int cnt = me.getClickCount();
	  if(cnt == 2)
	    {
	      ZNode node2 = me.getNode();
	      String url = node2.getNodeLink();
		try
		{
			AppletContext context = getAppletContext();
			URL u = new URL(url);
			context.showDocument(u, "Summary");
		}
		catch(Exception ex)
		{
			showStatus("error" + ex);
		}
		/*
	      try 
		{
			int port = 31337;
			Socket newSock = new Socket("localhost", port);
			DataOutputStream out = new 
			DataOutputStream(newSock.getOutputStream());
			out.writeBytes(node2.getNodeName());
	      }
	      catch(Exception e) 
		{
			System.out.println("Error occured while sending a 'click' event " + e);
	      }*/
	    }
	}
      });
  }

  /**
   * This method is called by removeNode method. It takes a node
   * selected by removeNode and removes it from a tree.
   */
  public void removeAChild()
  {
    ZNode node1 = null;
    ZNode handle = null;
    ArrayList selection = ZSelectionGroup.getSelectedNodes(canvas.getLayer());

    for (Iterator i = selection.iterator(); i.hasNext();) {
      node1 = (ZNode)i.next();
      handle = node1.editor().getTop();

      // Always keep start node
      if (handle != startNode) {
	handle.getParent().removeChild(handle);
      }
    }

    //removes entry from hashtable at NodeGroup
    NodeGroup.deleteNode(node1.getNodeName());
    NodeGroup.deleteComponent(node1);
    ZSelectionGroup.unselectAll(canvas.getLayer());
  }

  /**
   * Called by scale(String). It will scale a specified node by
   * a factor of 2.
   */
  public void scalePlus(String name)
  {
    ZNode node = NodeGroup.getNode(name);
    ZTransformGroup t =node.editor().getTransformGroup();
    t.scale(2);
    //canvas.getDrawingSurface().repaint();
    repaint();
  }

  /**
   * Called by scale(String). It will scale a specified node by
   * a factor of 0.5.
   */
  public void scaleMinus(String name)
  {
    ZNode node = NodeGroup.getNode(name);
    ZTransformGroup t =node.editor().getTransformGroup();
    t.scale(0.5);
    //canvas.getDrawingSurface().repaint();
    repaint();
  }

  /**
   * Called by valueChanged(String) method in ComponentApplet.
   * If given node (and its group) is already scaled by a factor of 2,
   * this method will restore it to its original size else it will
   * scale it by a factor of 2.
   */
  public void scale(String name)throws NodeNotFoundException
  {
    ZNode node = NodeGroup.getNode(name);
    if(node == null)
      {
	throw new NodeNotFoundException(name);
      }
    if (node.isScaled() == false)
      {
	node.setScaled(true);
	scalePlus(name);
      }
    else
      {
	node.setScaled(false);
	scaleMinus(name);
      }
  }

  /**
   *	This method will highlight a given node.
   */
  public void focusNode(String name, java.awt.Color highlightColor) 
    throws NodeNotFoundException
  {
    ZNode node = NodeGroup.getNode(name);
    if(node == null)
      {
	throw new NodeNotFoundException(name);
      }
	
    highlightedNode.add(name);
    ZText zt = NodeGroup.getComponent(node);
    zt.setFont(highlightFont);
    zt.setPenColor(highlightColor);
  }

  /**
   *	This method will return a vector of highlighted nodes.
   */
  public Vector getFocusNode() throws NodeNotFoundException
  {
    return highlightedNode;
  }

  /**
   *	This method will clear a highlighted text in a node  and 
   *  restore a normal text.
   */
  public void clearFocusNode(String name) throws NodeNotFoundException
  {
    ZNode node = NodeGroup.getNode(name);

    if(node == null)
      {
	throw new NodeNotFoundException(name);
      }
    highlightedNode.remove(name);
    ZText zt = NodeGroup.getComponent(node);
    zt.setFont(font);
    zt.setPenColor(color);
  }

  /**
   *	This method will clear highlighted text for all nodes.
   */
  public void clearFocusNode()
  {
    int count = highlightedNode.size() - 1;
    while(count >= 0)
      {
	ZNode node1 = NodeGroup.getNode((String)highlightedNode.get(count));
	ZText zt = NodeGroup.getComponent(node1);
	zt.setFont(font);
	zt.setPenColor(color);
	count--;
      }
    highlightedNode.removeAllElements();
  } 
	
  /** 
   * Handle stop requests by appletViewer, browser, etc.
   */
  public void stop() {
    System.err.println("stop called");
    // Raise the stopServer flag
    stopServer = true;
    // Now wait...
  }
  
  public void startServer()
  { 
    // We must run in a server so that stop actually happens
    new Thread(new TAServer()).start();
  }

  /**
   * TreeApplet server to allow remote requests to the tree via a
   * socket interface.
   */  
  class TAServer implements Runnable{
    public void run() {
      
      String nodeName = null;
      Socket clientSocket = null;
      ObjectInputStream obj = null;
      ServerSocket serverSocket = null;
      
      try {
	serverSocket = new ServerSocket(31338);
	// Set timeout to 250 milliseconds, i.e. will only wait for 250
	// milliseconds.  This implicitly means there must be a loop
	// that contains accept.
	serverSocket.setSoTimeout(250);
      } catch(Exception e) { e.printStackTrace(); return; }
      
      while(true) { // Accept 1 connection at a time
	try {
	  clientSocket = serverSocket.accept();
	} catch(InterruptedIOException e) {
	  // Check to see if it's time to leave, else continue the loop
	  if(stopServer == true) {
	    return;
	  } else {
	    //	    System.err.println("polling for clients");
	    continue;
	  }
	} catch(Exception e) {
	  e.printStackTrace();
	  return;
	}
	// Now set up the streams
	try {
	  obj = new ObjectInputStream(clientSocket.getInputStream());
	} catch(Exception e) { 
	  System.err.println("error in setting up stream: garbage "+
			     "connection? " + e.toString());
	  try {
	    clientSocket.close();
	  } catch(Exception ex) { 
	    System.err.println("Java is hopeless: " + ex);
	  }
	  continue;
	}
      
	// Should not need this
	//BufferedReader inbound = new BufferedReader(
	//new InputStreamReader(clientSocket.getInputStream()) );
	//InputStream is =
	// new InputStream(new 
	// InputStreamReader(clientSocket.getInputStream()) );
	
	while(true) { // While connection is active...
	  TriKXUpdateObject query;
	  
	  try {
	    query = (TriKXUpdateObject)obj.readObject();
	  } catch(Exception e) {
	    System.err.println("Error in readObject, closing connection");
	    break; // Go back to main while loop
	  }
	  
	  try {
	    nodeName = query.getNodename();
	    if(query.getNodename() == null) clearFocusNode();
	    else if(query.getColor() == null) clearFocusNode(nodeName);
	    else focusNode(nodeName, query.getColor());
	  } catch(NodeNotFoundException e) { 
	    System.err.println("Node not found:" + e.toString()); 
	  }
	}
	// Now, go for another connection.
	
      } // End while loop
    } // End run method
  } // End server inner class
}
