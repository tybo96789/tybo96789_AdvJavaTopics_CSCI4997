package space.gameressence.atiburcio.tyler;

//Java internal imports
import java.util.Scanner;
import java.io.*;


/**
 * Class that contains placeholders methods to be implemented in C++ in a different thread
 * @author Tyler Atiburcio
 * @version 1
 */
public class Useful implements Runnable
{
  //Loads the C++ library file
  static { System.loadLibrary("demo"); }

  static Scanner scan = new Scanner(System.in);

  public static void main(String args[])
  {
    Thread th = new Thread(new Useful(),"jniMethod");
    th.start();
    try
    {
      th.join();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.err.println("Error call C++ code!");
    }

  }

  /**
   * Runs the external method in another thread
   */
  public void run()
  {
    System.out.print("What do you feed to the computer?: ");
    String in = scan.nextLine();
    System.out.println();
    method(in);
  }

  /**
   * Method declaration, which will be implemented in C++
   *  Prints out the String argument
   *  Prints out the lenght of the string
   * @param s String; A string to passed to the method
   */
  public static native void method(String s);
}
