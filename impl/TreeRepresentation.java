package psl.trikx.impl;

/**
 * Title: TreeRepresentation
 * Description: A main application file to create a tree using jazz tool. It requires
 *              jazz.jar file associated with it. It reads a user input (nodename=
 *              parent name, link) from nodefile.txt and creates a tree depending
 *              upon that. A user can click twice on a node to open an associated
 *              link of right frame. He can click a right mouse button and drag
 *              a mouse left and right to zoom a tree.
 * Copyright (c) 2000: The Trustees of Columbia University and the City of New York. 
  *                              All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.component.*;
import edu.umd.cs.jazz.event.*;
import edu.umd.cs.jazz.util.*;
import edu.umd.cs.jazz.io.*;

public class TreeRepresentation extends JFrame
{
    protected ZNode startNode    = null;
    ZLayoutGroup mainLayout = null;
    ZTreeLayoutManager manager = new ZTreeLayoutManager();
    protected ZCanvas canvas;
    Font font = new Font("", Font.PLAIN, 15);

    public TreeRepresentation()
    {
        // Support exiting application
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        // Set up basic frame
        setBounds(100, 100, 800, 400);
        setResizable(true);
        setBackground(null);
        setVisible(true);
        canvas = new ZCanvas();
        ZoomEventHandler ze = (ZoomEventHandler)canvas.getZoomEventHandler();
	ze.setActive(true);
	ze.setMaxMagnification(4);
	ze.setMinMagnification(0.6);
        getContentPane().add(canvas);
	JToolBar toolBar = buildToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);
        validate();

        // Set up event handlers
        ZSelectionEventHandler selectionHandler =
        new ZSelectionEventHandler(canvas.getCameraNode(), canvas, canvas.getLayer());
        selectionHandler.setActive(true);

        canvas.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                canvas.requestFocus();
            }
        });

        initScreen();
   }


/**
  * Builds a tool bar. It provides features like - changing a link
  * representation in a tree (angled or straight)and changing an
  * orientation of tree (horizontal or vertical)
  */

  public JToolBar buildToolBar()
  {
        JToolBar toolBar = new JToolBar();
        JButton button;

	JToggleButton angleLinkButton = new JToggleButton("Angled");
        angleLinkButton.setToolTipText("Angled Link Style");
        angleLinkButton.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                setLinkStyle(ZTreeLayoutManager.LINK_ANGLEDLINE);
            }
        });

        JToggleButton straighLinkButton = new JToggleButton("Straight");
        straighLinkButton.setSelected(true);
        straighLinkButton.setToolTipText("Straight Line Link Style");
        straighLinkButton.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                setLinkStyle(ZTreeLayoutManager.LINK_STRAIGHTLINE);
            }
        });

        ButtonGroup linkGroup = new ButtonGroup();
        linkGroup.add(straighLinkButton);
        linkGroup.add(angleLinkButton);

        toolBar.add(angleLinkButton);
        toolBar.add(straighLinkButton);
        toolBar.addSeparator();

       // Head orientation choices
        JToggleButton orientationButton = new JToggleButton("Horizontal");
        orientationButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setOrientation(ZTreeLayoutManager.ORIENT_HORIZONTAL);
            }
        });
        orientationButton.setToolTipText("Tree from Left to Right");

        ButtonGroup orientGroup = new ButtonGroup();
        orientGroup.add(orientationButton);
        toolBar.add(orientationButton);

        orientationButton = new JToggleButton("Vertical");
        orientationButton.setSelected(true);
        orientationButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setOrientation(ZTreeLayoutManager.ORIENT_VERTICAL);
            }
        });
        orientationButton.setToolTipText("Tree from Top Down");
        orientGroup.add(orientationButton);
        toolBar.add(orientationButton);

	button = new JButton("Exit");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(1);
            }
        });

        button.setToolTipText("Exit an application");
        toolBar.add(button);

        return toolBar;
    }


	void setLinkStyle(int wanted) {
        if (wanted != manager.getLinkStyle()) {
            manager.setLinkStyle(mainLayout, wanted);
            canvas.getDrawingSurface().repaint();
        }
    }

	    public void setOrientation(int wanted) {
        if (wanted != manager.getCurrentOrientation()) {
            manager.setCurrentOrientation(mainLayout, wanted);
            canvas.getDrawingSurface().repaint();
        }
    }


    /**
     * Accepts a request to add a node to scenegraph. Node name
     * must be unique or it will throw a NodeNameNotUnique exception.
     * The node which should be set as a parent node must be already
     * added to a tree or it will throw a NodeNotPresent exception.
     */

    public void addNode(String node1, String parent, String link)
                                    throws NodeNotFoundException,
                                    NodeNameNotUniqueException
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
            ZSelectionGroup.select(zn);
	 }
        else if(startNode != null)
        {
            throw new NodeNotFoundException();
        }
        addAChild(node1, link);
    }

    /**
     * Accepts a node name remove that node from tree. It throws
     * NodeNotFound exception if requested node is not present in tree.
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
     * parent node.
     */

    public void initScreen()
    {
        //Properties props= new Properties();
        BufferedReader inLine=null;
        String line = null;
        try
        {
            String fileName = "nodeFile.txt";
            inLine = new BufferedReader(new FileReader(fileName));
            line = inLine.readLine();
        }
        catch(Exception e)
        {
            System.out.println("Exception occured: " + e);
        }

        while(line != null)
        {
         int index1 = line.indexOf('=');
	   
	   if(index1 != -1)
	   {
		String name = line.substring(0, index1);
	      int index2 = line.indexOf(',', index1+1);
	      String parent = null;
	      String link = null;
	      if(index2 != -1)
		{
		    parent = line.substring(index1+1, index2);
		    link = line.substring(index2+1, line.length());
		}
	      else
		{
		     link = line.substring(index1+1, line.length());
		}
	      System.out.println("ADDING:   " + name+" "+parent+" "+link);
            try
            {
              addNode(name, parent, link);
            }
            catch (NodeNotFoundException e)
            {
            }
            catch(NodeNameNotUniqueException ex)
            {
            }
	   }	
         try
         {
            line = inLine.readLine();
         }
         catch(Exception e)
         {
            System.out.println("Exception occured: " + e);
          }
        }
    }


    /**
     * This is method  is called by addNode method. It will add a root
     * node and intializes a tree if no node was selected ny addNode or
     * it will add a new node as a child of a selected node. It will set
     * a node name and a link given by addNode.
     */

    public void addAChild(String name, String link)
    {
        ZVisualComponent vis = null;
        ZVisualGroup child = null;
        ZNode node= null;
        ZGroup group=null;
        ZLayoutGroup layout = null;
        ZText zt = null;

        //add a root node
        if(startNode == null)
        {
            zt = new ZText(name, font);
            group = new ZVisualGroup(zt, null);
            mainLayout = group.editor().getLayoutGroup();

            mainLayout.setLayoutChild(group);
            mainLayout.setLayoutManager(manager);
            canvas.getLayer().addChild(group.editor().getTop());
            startNode = group;
            ZTransformGroup t =startNode.editor().getTransformGroup();
            t.translate(400,50);
	    node = startNode;

        }
        // add a child node
        else
        {
            ArrayList selection = ZSelectionGroup.getSelectedNodes(canvas.getLayer());
            for (Iterator i = selection.iterator() ; i.hasNext() ;)
            {
                node = (ZNode)i.next();
                if (node instanceof ZGroup)
                {
                    group = (ZGroup)node;
                    zt = new ZText(name, font);
                    vis = zt;
                    child = new ZVisualGroup(vis, null);
                    layout = child.editor().getLayoutGroup();
                    layout.setLayoutChild(child);
                    layout.setLayoutManager(manager);
                    node = child;
                    group.addChild(node.editor().getTop());
                }
            }
        }

        //set node name and link for future references
        node.setNodeName(name);
        node.setNodeLink(link);
        NodeGroup.putNode(node);
        //NodeGroup.putComponent(zt);
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
                               /*try
				 {
					System.out.println("link:  " + url);
				 }
				catch(Exception ex)
				 {
					showStatus("error" + ex);
				 }*/
                                System.out.println(url);
                           }
                     }
             });
    }


    /**
     * This method is called by removeNode method. It takes a node
     * selected by removeNode and removes from a tree.
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
        //NodeGroup.deleteComponent(node1.getNodeName());
        ZSelectionGroup.unselectAll(canvas.getLayer());
    }


    public static void main(String[] argv) {
        new TreeRepresentation();
    }
}


