import java.io.*;
import java.net.*;

/**
The "refresh" method will be called when TriKX
server will receive an event from any component 
of the KX. It will specify an event name that is
related to a specific component
*/
public class PageRefresher
{
    public PageRefresher()
    {
    }
    
    public String refresh(String event)
    //public static void main(String [] args)
	{
	    String msg1 = null;
		//if(args.length != 1)
		//{
			//System .out.println("Usage: java PageRefresher <page name>. Valid <page name> is : 'oracle'");
		//	System.exit(1);
		//}
		//String pageName = args[0];
		if (event.compareTo("oracle") == 0)
		{
		    URL url = null;
		    try
		    {
		    url = new URL("http://localhost/psl/oracle/trikx/oracle.html");
		    }
		    catch(Exception e)
		    {
		        msg1 = "Exception: "+ e;
		    }
		    //harcoded for now - should be removed after packaging 
		    //of TriKX server files with the psl package
		    String destination  = "d:\\kanan\\project\\openjoda\\tomcat\\webapps\\root\\content\\static\\oracle.html";
		    //String destination  = "..//static//oracle.html";
		    FileDownload fd = new FileDownload();
		    String msg = fd.download(url, destination);
		    if (msg == null)
		    {
		        //System.out.println("Download is successful.");
		    }
		    else
		    {
		        msg1 = "Exception while downloading: " + msg;
		    }
			//paste code of file download here
		}
		else //modify later for other components
		{
			msg1 = "Usage: java PageRefresher <page name>. Valid <page name> is : 'oracle'";
			//System.exit(1);
		}
		return msg1;
	}
}