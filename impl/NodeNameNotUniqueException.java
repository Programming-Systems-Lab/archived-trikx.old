package psl.trikx.impl;

/**
 * Title: NodeNameNotUniqueException
 * Description: This exception is thrown if node name given for
 *              a new node is not unique.
 * Copyright (c) 2000: The Trustees of Columbia University and the City of New York. 
  *                              All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */


public class NodeNameNotUniqueException extends Throwable
{

  public NodeNameNotUniqueException(String msg)
  {
    System.out.println("Node name specified " + msg + "already exist.");
  }

}
