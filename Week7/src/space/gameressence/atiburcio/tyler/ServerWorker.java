package space.gameressence.atiburcio.tyler;

//Internal java imports
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class actually does the work for the server in another thread
 * @see space.gameressence.atiburcio.tyler.Server#compute(String)
 * @author Tyler Atiburcio
 * @version 1
 */
public class ServerWorker implements Runnable
{
    //Class Variables
    private final String data;
    private String converted = "";

    /**
     * Creates and initalizes worker
     * @param data String; The string to run the hash on
     */
    public ServerWorker(String data)
    {
      this.data = data;
    }

    /**
     * Computes the hash of the data (string)
     */
    public void run()
    {
        MessageDigest digest = null;
        try
        {
          //Sepecify the hashing type
          digest = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e)
        {
          System.err.println("Java has gone mad");
        }
        //Compute the hash
        byte[] hash = digest.digest(data.getBytes());

        //Begin converting the hash to hex
        StringBuffer hexString = new StringBuffer();
      	for (int i=0;i<hash.length;i++) {
      		String hex=Integer.toHexString(0xff & hash[i]);
     	     	if(hex.length()==1) hexString.append('0');
     	     	hexString.append(hex);
      	}
        converted = hexString.toString();
    }

    /**
     * @return String; The hash of the data (string) in hex form
     */
    public String getConverted()
    {
      return this.converted;
    }
}
