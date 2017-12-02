package space.gameressence.atiburcio.tyler;

//external imports
import space.gameressence.atiburcio.tyler.util.*;

//internal imports
import java.io.*;

/**
 * Tester class to attempt to resolve missing librarys
 * @author Tyler Atiburcio
 * @version 1
 */
public class DownloadTester
{
  //Dependency reslover
  static
  {
    try
    {
      File lib = new File("./lib/AtiburcioTyler_Util.jar");
      if(!lib.exists())
      {
        System.out.println("Dependency file missing! Resolving!");
        FileDownload.downloadFile("https://github.com/tybo96789/tybo96789_AdvJavaTopics_CSCI4997/raw/master/Util/AtiburcioTyler_Util.jar","./lib/AtiburcioTyler_Util.jar");
      }
    }catch(Exception e)
    {
      e.printStackTrace();
      System.err.println("PROGRAM COULD NOT RESLOVE MISSING LIBRARYS! HALTING!");
      System.exit(1);
    }
  }
  public static void main(String args[])
  {
    // //FileDownload.downloadFile("https://github.com/tybo96789/tybo96789_AdvJavaTopics_CSCI4997/raw/master/Util/AtiburcioTyler_Util.jar","/Users/Tyler_Atiburcio/Desktop/test.jar");
    // String test = "https://github.com/tybo96789/tybo96789_AdvJavaTopics_CSCI4997/raw/master/Util/AtiburcioTyler_Util.jar";
    // System.out.println(test.split("/")[test.split("/").length-1]);
    // FileDownload.downloadFile(test);
    new GUItest();
  }
  /**
   * Test if the reslover can successfully download the jar and be able to run the entire program without errors
   */
  private static class GUItest extends GUITemplate
  {
    public GUItest()
    {
      super("Dependency test");
    }
    public void makeContainers()
    {

    }
  }
}
