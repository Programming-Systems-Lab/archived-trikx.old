import java.io.*;
import java.net.*;

/**
A code to download a file from a specified URL
The file will be downloaded to a location specified 
by "detination" parameter
*/
public class FileDownload
{
    public FileDownload()
    {
    }
    
    //public static void main (String[] args)
    public String download(URL url, String destination)
    {
        int dataread = 0;
        int count = 0;
        int CHUNK_SIZE = 8192; // TCP/IP packet size
        byte[] dataChunk = new byte[CHUNK_SIZE]; // byte array for
        HttpURLConnection huc = null;
        String msg =  null;
        
        try
        {
            huc = (HttpURLConnection) url.openConnection();
            File destinationFile = new File(destination);
            FileOutputStream fos = new FileOutputStream(destinationFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
 
            // receive the file using a BufferedReader
            BufferedInputStream bis = new BufferedInputStream(
            huc.getInputStream());
            InputStreamReader isr = new InputStreamReader(bis);
            BufferedReader br = new BufferedReader(isr);
            System.out.print("Downloading..");
            while (dataread >= 0)
            {
                count++;
                dataread = bis.read(dataChunk,0,CHUNK_SIZE);
                System.out.print(".");
                if (dataread > 0 )
                bos.write(dataChunk,0,dataread);
            }
            System.out.println(count*CHUNK_SIZE + " bytes done.");
 
        bis.close();
        bos.close();
      }
      catch(Exception e)
      {
        System.out.println(e);
        msg = "" + e;
      }
      return msg;
    }
}