import java.io.*;

/**
This will allow a user to add a portlet in the .psml file.
Not fully functional yet.
*/
public class AddPortal
{
    public AddPortal()
    {
    }
    
    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Usage: java AddPortal <portal name> <account name>");           
            System.exit(1);
        }
        String portal = args[0];
        String accountName = args[1];
        String fileName = "..\\psml\\" + accountName + ".psml";
        System.out.println(fileName);
        File layoutFile = new File(fileName);
        if(!layoutFile.exists())
        {
            System.out.println("Account "+ accountName + " does not exist.");
            System.exit(2);                            
        }
        
        BufferedReader input = null;        
        try
        {   
            input = new BufferedReader(new FileReader(layoutFile));
        }
        catch( Exception e )
        {
            System.out.println("Exception: " + e);
        }
        BufferedWriter output = null;       
        String temp = "..\\psml\\temp.psml";
        File tempFile = new File(temp);
                
        try
        {   
            output = new BufferedWriter(new FileWriter(tempFile));
        }
        catch( Exception e )
        {
            System.out.println("Exception: " + e);
        }

       // char cbuf = new char[80];
        String line1 = null;
        try
        {
            line1 = input.readLine();
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
        }
        int index = line1.lastIndexOf("position=");
        String temp1 = line1.substring(index, index + 8);
        int index1 = line1.indexOf('"', index+10);
        String num = line1.substring(index+9, index1);
        System.out.println(temp1 +" "+ num);
        
        try
        {
            input.close();       
            output.close();
        }
        catch(Exception ex1)
        {
            System.out.println("Exception: " + ex1);
        }
    }
}