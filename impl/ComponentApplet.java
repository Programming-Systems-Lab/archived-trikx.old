package psl.trikx.impl;

/**
 * Title: ComponentApplet
 * Description: Use Component.html to execute this applet
 *              An applet for upper left frame of FrontView.html
 *              It reads component names from nodefile.txt and associates
 *              link with each of them. By selecting any of the components,
 *              it will take us to the associated web page.
 * Copyright (c) 2000: The Trustees of Columbia University and the City of New York. 
  *                              All Rights Reserved.
 * @author Kanan Naik
 * @version 1.0
 */


import java.awt.*;
import java.applet.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

/**
 * Reads nodefile.txt and stores nodes name plus associated links
 * with them in a property file. It also creates a JList for these
 * nodes.
 */

public class ComponentApplet extends JApplet implements ListSelectionListener
{
    Properties props = new Properties();

    public void init()
    {
        String[][] input = InputList.getInputList();
	  String node = input[0][0];
	  int i = 1;
        Vector v= new Vector();
	  try
	  {
		  while(node != null)
		  {
			v.add(node);
			node = input[i][0];
			i++;
	  	  }
        }
	  catch(Exception e)
	  {
	  }
	
	  /*int index = -1;
	  int beginIndex = -1;
	  int endIndex = -1;
	  int space = -1;
	  int j = -1;
	  String spaceString = "";
  	  int i = 0;
	  String name = null;
	  
	  try
        {
	  	while(node != null)
	  	{
			endIndex = node.indexOf('-');
			space = 0;
			beginIndex = 0;
			spaceString = "";
			if(endIndex != -1)
			{
				beginIndex = 0;
				index = node.indexOf('-', beginIndex); 
				while(index != -1)
				{
					space++;
					beginIndex = index + 1; 
					index = node.indexOf('-', beginIndex); 
				}
				index = node.lastIndexOf('-');
				name = node.substring(index+1, node.length());
				j = 0;
				spaceString = "";
				while( j < space)
				{
					spaceString = spaceString.concat("    ");
					j++;	
				}
				v.add(spaceString + name);
			}
			else
			{	
				v.add(node);
			}
				i++;
				node = input[i][1];
	  	}
	  }
	  catch(Exception e)
	  {
	  }*/

        JList list = new JList(v);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellWidth(100);
        list.setFixedCellHeight(20);
        JScrollPane scroll = new JScrollPane(list);
        JPanel p = new JPanel();
        p.setBackground(Color.white);
        p.add(scroll);
        list.addListSelectionListener(this);
        getContentPane().add(p, "Center");
    }


    /**
     * When any component of JList is selected this method is called.
     * ValueChanged calls scale(String name) method of TreeApplet to scale 
     * an associated node. 	
     */

    public void valueChanged(ListSelectionEvent e)
    {
        if(e.getValueIsAdjusting()) return;
        JList source = (JList)e.getSource();
        String value = (String)source.getSelectedValue();
	  TreeApplet tree = new TreeApplet();
	  try
	  {
		  tree.scale(value);
	  }
	  catch(NodeNotFoundException ex)
	  {
	  }
    }
}
