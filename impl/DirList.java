package psl.trikx.impl;

 //package chime.dataServer.services.demo.linux;

/**
 * This class contains a list of all the source and doc files from the linux 
 * 2.0.36 kernel.  The DBLoader class will use this list to create a chime DB
 * for visualization.
 *
 * The list array is read as a series of triples, specifying the name, URI, 
 * and mimeType fields of the dataElement to be created for each file.
 *
 * @author Steve Dossick (sdossick@cs.columbia.edu)
 *
 * Hacked to high heaven by Phil Gross and Kanan Naik  8 Sep 2000
 * 
 * $Log$
 * Revision 1.1  2000-09-13 19:52:49  ktn10
 * Initial revision
 *
 * Revision 1.6  2000/09/09 18:17:40  jjp32
 *
 * Lots of fixes and updates for demo
 *
 * Revision 1.5  2000/09/08 20:48:19  jjp32
 *
 * Packaged up everything
 *
 * Revision 1.4  2000/09/08 18:58:40  png3
 * Modified for September 2000 DASADA demo
 *
 *
 */

public class DirList {
  public static String [] [] list = 
  {
	{"root", "root", "root"},
	{"root", "fs", "fs"},
	/*	{"root", "net", "net"},*/
	{"root", "drivers", "drivers"},
	/*	{"root", "include", "include"},*/
	{"fs", "fs/ext", "ext"},
	{"fs", "fs/msdos", "msdos"},
	{"fs", "fs/isofs", "isofs"},
	{"fs", "fs/nfs", "nfs"},
	/*	{"net", "net/unix", "unix"},
		{"net", "net/802", "802"},
		{"net", "net/core", "core"},
		{"net", "net/appletalk", "appletalk"}, */
	{"drivers", "drivers/net", "net"},
	{"drivers", "drivers/block", "block"},
	{"drivers", "drivers/isdn", "isdn"},
	{"drivers/net", "drivers/net/soundmodem", "soundmodem"},
	/*	{"drivers/isdn", "drivers/isdn/icn", "icn"},
		{"drivers/isdn", "drivers/isdn/pcbit", "pcbit"},*/
	{"drivers/block", "drivers/block/paride", "paride"},
	

	/*{"net/appletalk", "include", "include"},*/
	/*	{"include", "include/linux", "linux"}*/
  };


  public static String[][] getInputList()
  {
	return list;
  }

}
