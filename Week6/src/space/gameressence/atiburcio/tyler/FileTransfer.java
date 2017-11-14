package space.gameressence.atiburcio.tyler;

//Internal Java imports
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;

//External imports
import space.gameressence.atiburcio.tyler.util.*;

/**
 * A simple file transfer program; Server to Client
 * CHANGELOG:
 *  Version 1:Adjusted usage of protected access vars
 *  Version 2:Refactored Server/client codes to seperate classes
 * @author Tyler Atiburcio
 * @version 2
 */
public class FileTransfer
{

  //Defaults Declarations
  public final static int DEFAULTLISTENPORT = 2121;
  public final static int FILE_SIZE_LIMIT = 1000000;
  public final static Config CONFIG = Config.Import("./config.properties");
  /**
   * Pre-runtime arguments determines whereter to run the client or server part of the program
   * Typing "c" or "Client" will start client
   * Typing "s" or "Server" will start server
   * No runtime arguments will run client by default
   * @param args Pre-runtime server/client selection
   */
  public static void main (String[] args)
  {
    try
    {
      if(args.length != 0)
        switch(args[0].trim().toUpperCase())
        {
          case("SERVER"):
          case("S"):
            {
              new space.gameressence.atiburcio.tyler.server.FileTransferServer();
              break;
            }
          case("C"):
          case("CILENT"):
          {
              new space.gameressence.atiburcio.tyler.client.FileTransferClient();
          }
        }
      else
        new space.gameressence.atiburcio.tyler.client.FileTransferClient();
    }
    catch(Exception e)
    {
      System.out.print("\n\n\n");
      System.err.println("I don't know what your want.");
      System.out.println("To Start a Server:");
      System.out.println("Enter \"S\" or \"SERVER\" as jar arguments!");
      System.out.println("To Start a Client:");
      System.out.println("Enter \"C\" or \"Client\" as jar arguments");
      System.out.print("\n\n\n");
    }
  }
}
