package psl.trikx.impl;

 //package chime.dataServer.services.demo.linux;

/**
 * This class contains a list of node names , parent names and
 * links associated with them.
 * format: <nodename>, <parent>,<link>
 * @author Kanan Naik
 *
 */

public class InputList {
  public static String [] [] list = 
  {
	{"psl", "null", "http://www.psl.cs.columbia.edu"},
	{"GC", "psl", "http://www.cs.columbia.edu"},
	{"MP", "psl", "http://www.columbia.edu"},
	{"oracle", "psl", "http://www.psl.cs.columbia.edu"},
	{"rolesGC", "GC", "http://www.psl.cs.columbia.edu"},
	{"implGC", "GC", "http://www.psl.cs.columbia.edu"},
	{"docMP", "MP", "http://www.psl.cs.columbia.edu"},
	{"implOra", "oracle", "http://www.psl.cs.columbia.edu"},
	{"excepOra", "oracle", "http://www.psl.cs.columbia.edu"},
  };


  public static String[][] getInputList()
  {
	return list;
  }

}
