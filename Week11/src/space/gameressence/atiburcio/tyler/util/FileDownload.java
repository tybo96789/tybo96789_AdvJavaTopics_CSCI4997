package space.gameressence.atiburcio.tyler.util;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * Utilties class that downloads files from the web using Java NIO
 * @author Tyler Atiburcio
 * @version 1
 */
public class FileDownload
{
  /**
   * Downloads the specified file and takes the name of the file based from the URL
   * Stores download in current directory of where program is run
   * @param url String; The full url of the file to be downloaded
   * @return File; Reference to the downloaded file
   * @see #downloadFile(URL,String)
   */
  public static File downloadFile(String url)
  {
    //Assumes the name of the file is the last bit of the URL
    downloadFile(url,"./" + url.split("/")[url.split("/").length-1]);
    return new File("./" + url.split("/")[url.split("/").length-1]);
  }

  /**
   * Downloads the file from the url and names the file with the 'fileName' arguement and with the file extension with the 'fileExtension' argument
   * @param url String; The URL of the file to be downloaded
   * @param exportPath String; The path on where the file should be saved to
   * @param fileName String; Save the file as the given FileName
   * @param fileExtension String; Save the file as the given fileExtension
   * @return boolean; If operation is successful
   * @see #downloadFile(String,String)
   */
  public static boolean downloadFile(String url, String exportPath, String fileName, String fileExtension)
  {
    String fullPath = exportPath +"/"+ fileName +"."+ fileExtension;
    return downloadFile(url,fullPath);
  }

  /**
   * Downloads the file and store the file in the given path
   * IMPORTANT: fullPath (String) Must include the full path of the file (filename and file extension)
   * @param url String; The URL of the file to be downloaded
   * @param fullPath String; The full file path of where the file should be saved and named
   * @return boolean; Return true of operation is successful
   * @see #downloadFile(URL,String)
   */
  public static boolean downloadFile(String url, String fullPath)
  {
    URL dwnURL  = null;
    try
    {
      dwnURL = new URL(url);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return downloadFile(dwnURL,fullPath);
  }

  /**
   * Downloads the file and stores in the given path
   * IMPORTANT: fullPath (String) Must include the full path of the file (filename and file extension)
   * @param url URL; The URL of the file to be downloaded
   * @param fullPath String; The full file path of where the file should be saved and named
   * @return boolean; If operation is successful
   */
  public static boolean downloadFile(URL url, String fullPath)
  {
    ReadableByteChannel rbc = null;
    FileOutputStream fos = null;
    try
    {
      //Open new channel to the URL
      rbc = Channels.newChannel(url.openStream());
      //Create a new fileoutputstream to pipe the data to the specified file path
      fos = new FileOutputStream(fullPath);
      //Start transfering the file and pipe the data to the specified file path
      fos.getChannel().transferFrom(rbc,0, Long.MAX_VALUE);

    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        rbc.close();
        fos.close();
      }
      catch(IOException ex)
      {
        System.err.println("Failed to close output stream!");
      }
    }
    if(new File(fullPath).isFile()) return true;
    else return false;
  }
}
