import java.io.*;
import java.net.*;
import java.lang.*;

import siena.*;

/**
This will be used to sunscribe to a Siena server and
to receive the events from various components of KX
*/
public class TriKXSienaInterface implements Runnable, Notifiable
{
    static Siena si = null;
    public TriKXSienaInterface(Siena s)
    {
        this.si = s;
    }

    public TriKXSienaInterface()
    {
    }


    public static void main(String[] args)
    {
        String master = "senp://localhost:31337";
        if (args.length > 0)
        {
            master = args[0];
        }
        HierarchicalDispatcher hd = new HierarchicalDispatcher();
        try
        {
            hd.setMaster(master);
            System.out.println("TriKX master is " + master);
        }
        catch(siena.InvalidHandlerException e)
        {
          e.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        TriKXSienaInterface tsi = new TriKXSienaInterface(hd);
        Thread t = new Thread(tsi);
        t.start();
    }


    public void run()//subscribe to "trikxEvent"
    {
        Filter f = new Filter();
        f.addConstraint("type", "trikxEvent");
        try
        {
            si.subscribe(f, this);
        }
        catch(siena.SienaException se)
        {
            se.printStackTrace();
        }
        System.out.println("TriKX is subscribed to " + f);
    }

    public void notify(Notification n)//called when event is received
    {
         AttributeValue av = n.getAttribute("trikxEvent");
         if (av != null)
         {
            String event = av.stringValue();
            System.out.println("TriKX got an event: " + event);
            PageRefresher ref = new PageRefresher();
            String msg = ref.refresh(event);
            if(msg == null)
            {
                System.out.println("Code download is successful");
            }
            else
            {
                System.out.println("" + msg);    
            }
         }
         else
         {
            System.out.println("TriKX error: Oracle event is null.");
         } 
    }



    public void notify(Notification[] no)

    {

    }

}