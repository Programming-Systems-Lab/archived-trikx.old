package psl.trikx.impl;

/**
 * Title: NodeGroup
 * Description: Contains hastables which stores information regarding
 *              all nodes and text components active on scenegraph.
 *              It keeps references of ZNode and ZText objects with
 *              their names.
 * Copyright (c) 2000: The Trustees of Columbia University and the City of New York. 
  *                              All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */

import java.util.*;

import edu.umd.cs.jazz.*;
import edu.umd.cs.jazz.component.*;


public class NodeGroup extends Object {

    private static Hashtable nameHash = null;
    private static Hashtable component = null;
    

    /** Creates new NodeGroup */
    public NodeGroup()
    {

    }

    /**
     * Creates a hashtable for nodes if it was not intialized.
     * Store a reference of name and ZNode object in hashtable.
     */
   
    public static void putNode(ZNode zn)
    {
        if(nameHash == null)
            nameHash = new Hashtable();

        nameHash.put(zn.getNodeName(), zn);
        return;
    }

    /**
     * Given a node name it returns a ZNode object.
     */

    public static ZNode getNode(String name)
    {
	if(nameHash == null)
            nameHash = new Hashtable();
      if(nameHash.containsKey(name) == false)
	   {	
          return null;
	   }	
      else
         {
            ZNode nameNode = (ZNode)nameHash.get(name);
	      return nameNode;
         }
    }

    public static void deleteNode(String name)
    {
        nameHash.remove(name);
    }

   public static void putComponent(ZNode node, ZText text)
   {
	if(component == null)
            component = new Hashtable();
	component.put(node, text);
      return;
  }


  public static ZText getComponent(ZNode node)
    {
	if(component == null) 
	   component = new Hashtable();
      if(component.containsKey(node) == false)
      {	
          return null;
	}	
      else
      {
         ZText text = (ZText)component.get(node);
	   return text;
      }
    }

    public static void deleteComponent(ZNode node)
    {
        component.remove(node);
    }
  }
