package psl.trikx.impl;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Little applet to show event updates.  Part of the TriKX family,
 * brought to you from PSL!
 *
 * Copyright (c) 2000: The Trustees of Columbia University and the
 * City of New York.  All Rights Reserved.
 *
 * @author Janak J Parekh
 * @version 0.5
 */
public class EventUpdateApplet extends JApplet {
  JTextArea messageArea = null;
  JScrollPane jsp = null;
  int listeningPort = 31339;
  boolean shutdownServer = false;

  /**
   * "CTOR."
   */
  public void init() {
    setBackground(Color.white);
    setVisible(true);

    //    this.listeningPort = listeningPort;
    messageArea = new JTextArea();
    jsp = new JScrollPane(messageArea);
    jsp.setAutoscrolls(true);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jsp);
    validate();
  }

  public void start() {
    repaint();
    // Start up server
    runServer(listeningPort);
  }

  public void stop() {
    System.err.println("Attempting stop");
    shutdownServer = true;
    // now wait..
  }

  public void runServer(int port) {
    shutdownServer = false;
    new Thread(new EventUpdateServer(port)).start();
  }

  class EventUpdateServer implements Runnable {
    int port;

    public EventUpdateServer(int port) {
      this.port = port;
    }

    public void run() {
      ServerSocket ss;
      try {
	ss = new ServerSocket(port);
	ss.setSoTimeout(250);
      } catch(Exception e) {
	e.printStackTrace();
	return;
      }
      while(true) {
	Socket clientSocket = null;
	try {
	  clientSocket = ss.accept();
	  new Thread(new EventUpdateThread(clientSocket)).start();
	} catch(InterruptedIOException e) {
	  if(shutdownServer == true) {
	    System.err.println("Shutting down event update applet server");
	    try {
	      if(clientSocket != null) clientSocket.close();
	      if(ss != null) ss.close();
	    } catch(Exception ex) { ; }
	    return;
	  }
	  else continue;
	} catch(Exception e) {
	  e.printStackTrace();
	  return;
	}
      }
    }
  }

  /**
   * Forked-off client.
   */
  class EventUpdateThread implements Runnable {
    private Socket s = null;
    private BufferedReader in = null;
    
    public EventUpdateThread(Socket clientSocket) {
      this.s = clientSocket;
      try {
	in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      } catch(Exception e) {
	e.printStackTrace();
      }
    }

    public void run() {
      while(true) {
	try {
	  String input = in.readLine();
	  if(input == null) {
	    System.err.println("Client connection error, closing");
	    return;
	  }
	  messageArea.append(input + "\n");
	  // Scroll to bottom
	  int max = jsp.getVerticalScrollBar().getMaximum();
	  jsp.getVerticalScrollBar().setValue(max);
	} catch(SocketException e) {
	  System.err.println("Client connection error, closing");
	} catch(Exception e) {
	  e.printStackTrace();
	}
      }
    }
  }
}      
