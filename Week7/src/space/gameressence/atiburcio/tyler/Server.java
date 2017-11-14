package space.gameressence.atiburcio.tyler;

//Internal Java imports
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;


/**
 * The Server part of the RMI concept, which implements the Common Interface
 * @see space.gameressence.atiburcio.tyler.Common
 * @author Tyler Atiburcio
 * @version 1
 */
public class Server implements Common
{
  //Static Variables used for stats
  private static int querys = 0;
  private static long computeTime = 0;

  //Constant to convert Miliseconds to Seconds
  private static final double MILITOSEC = 0.001;

  /**
   * Binds the server to RMI Registry
   */
  public Server()
  {
    Common host = this;
    Remote stub = null;
    try
    {
      stub = UnicastRemoteObject.exportObject(host, 0);
      Registry registry = LocateRegistry.getRegistry("localhost");
      registry.rebind("Common", stub);
      System.out.println("bind okay!");
    }catch(RemoteException e)
    {
      System.err.println("Failure binding to RMI server!");
      e.printStackTrace();
    }

  }

  /**
   * @return String; Stats about the server
   */
  public static String query() throws RemoteException
  {
    return "Querys: " + querys + " ComputeTime: " + (computeTime * MILITOSEC) +"s";
  }

  /**
   * Takes in a string and computes the SHA-256 hash of the passed string
   * @param s String; The string to compute the hash for
   * @return String; The hash of the passed string (s)
   */
  public String compute(String s) throws RemoteException
  {
    querys++;
    //Collect start time
    long startTime = System.currentTimeMillis();
    //start a new thread and compute and return back to sender
    ServerWorker worker = new ServerWorker(s);
    Thread thread = new Thread(worker,"Query: " +s);
    thread.start();
    try
    {
      //Wait for the thread to finish computing the hash
      thread.join();
    }
    catch(InterruptedException e)
    {
      System.err.println("Failed to join worker thread");
    }
    //Compute time it took for server to process hash
    computeTime += (startTime-System.currentTimeMillis());
    return worker.getConverted();
  }

}
