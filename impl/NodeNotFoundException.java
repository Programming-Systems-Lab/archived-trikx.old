package psl.trikx.impl;

/**
 * Title: NodeNotFoundException
 * Description: This exception is thrown if node name used (as a parent)
 *               is not available in a tree.
 * Copyright (c) 2000: The Trustees of Columbia University and the City of New York. 
  *                              All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */


public class NodeNotFoundException extends Throwable
{

  public NodeNotFoundException()
  {
    System.out.println("Node name is not specified - can't find a parent.");
  }

  public NodeNotFoundException(String msg)
  {
    System.out.println("Node name specified as a parent " + msg + " is not present in a tree");
  }

}
