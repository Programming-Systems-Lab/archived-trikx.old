package psl.trikx.impl;

/**
 * Title: ITreeApplet
 * Description: Interface for TreeApplet. The methods, which
 *              TreeApplet has to implement are scale(String),   
 *              addNode(String node1, String parent, String link, String
 *              text), removeNode(String node1), focusNode(String), 
 *		    getFocusNode() and clearFocusNode().
 * Copyright (c) 2000: The Trustees of Columbia University and the City
 *			of New York. All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */

import java.util.*;

public interface ITreeApplet {

	/**
	*	This method will add a node with a given text and as a child of 
	*	a given parent node.
	*/
       public void addNode(String nodeName, String parent, String link)
				   throws NodeNotFoundException,
				   NodeNameNotUniqueException;
	
	/**
	*	This method will remove a node from a tree
	*/
	public void removeNode(String nodeName)throws NodeNotFoundException;

	/**
	*	This method will scale a node by a factor of 2 or 0.5 depending upon
	*	its current scaling factor.
	*/
	public void scale(String nodeName)throws NodeNotFoundException;

	/**
	*	This method will highlight a given node.
	*/
	public void focusNode(String nodeName, java.awt.Color color) throws NodeNotFoundException;

	/**
	*	This method will return a vector of highlighted nodes.
	*/
	public Vector getFocusNode() throws NodeNotFoundException;
	
	/**
	*	This method will clear a highlighted text in a node  and 
      *     restore a normal text.
	*/
	public void clearFocusNode(String name) throws NodeNotFoundException;

	/**
      *	This method will clear highlighted text for all nodes.
      */
      public void clearFocusNode();

                                            
  }
