package space.gameressence.atiburcio.tyler.server;

//Interal java imports 
import java.io.*;
import java.net.*;

/**
 * This class facilitates the sending of data to the client
 * @author Tyler Atiburcio
 * @version 1
 */
public class FileTransferWorker implements Runnable
{
  //The client socket; This is final because the client socket should not change
  private final Socket client;
  //The wrapper object that will be sent to the client
  private final space.gameressence.atiburcio.tyler.SerialFileData serialFileData;

  /**
   * Initalizes the client and socket information and prepares them to be sent to client
   * @param serialFileData The wrapper object that will be send to client
   * @see space.gameressence.atiburcio.tyler.SerialFileData
   * @param client The client socket
   */
  public FileTransferWorker(Socket client, space.gameressence.atiburcio.tyler.SerialFileData serialFileData)
  {
    this.client = client;
    this.serialFileData = serialFileData;
  }

  /**
   * Writes the wrapper object to the client
   */
  public void run()
  {
    ObjectOutputStream out = null;
    try
    {
      out = new ObjectOutputStream(client.getOutputStream()); // get the output stream of client.
      //Write Object data to client
      out.writeObject(serialFileData);
      //Flush and close the data stream
      out.flush();
      out.close();
      client.close();

    }catch(Exception e)
    {
      e.printStackTrace();
    }

  }
}
