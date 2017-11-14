package space.gameressence.atiburcio.tyler.server;

//Internal Imports
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;

//External Imports
import space.gameressence.atiburcio.tyler.util.*;


/**
 * ServerGUI; Hosts a file to serve to clients
 * Used GUI template template
 * CHANGLOG: Rewrote Server Listner to accept Multi Client connections
 * @author Tyler Atiburcio
 * @version 2
 * @see GUITemplate
 */
public class FileTransferServer extends GUITemplate
{

  //GUI Declarations
  protected JTextArea consoleArea;
  protected JButton toggleStatusButton;
  protected JPanel buttonPanel;

  //Server vars
  protected int port;
  protected static boolean keepRunning = false;
  protected static ServerSocket serverSock;
  protected File fileToTransfer;

  /**
   * Make a Server using the DEFAULTLISTENPORT
   */
  public FileTransferServer()
  {
    this(space.gameressence.atiburcio.tyler.FileTransfer.DEFAULTLISTENPORT);
  }

  /**
   * Makes a server using the specifed port
   * @param port The port number that the server should bind to
   */
  public FileTransferServer(int port)
  {
    super("Server");
    this.port = port;
  }


  /**
   * Used to make all the container that make up the ServerGUI
   */
  public void makeContainers()
  {
      //
      INSTANCE.setLayout(new GridLayout());
      mainPanel.setLayout(new GridLayout(2,1));
      this.consoleArea = new JTextArea();
      this.consoleArea.setEditable(false);
      mainPanel.add(this.consoleArea);
      this.toggleStatusButton = new JButton("Start");
      this.toggleStatusButton.addActionListener(new ServerListner());
      mainPanel.add(this.toggleStatusButton);
      add(mainPanel);

  }

  /**
   * Allows the server to start listening to the specifed port, as well as asking the user what file to host
   */
  private class ServerListner implements ActionListener, Runnable
  {
    protected boolean isStopped = false;
    protected Thread serverThread = null;
    /**
     * Handles the startup and shutdown of the server
     */
    public void actionPerformed(ActionEvent e)
    {

      if(!keepRunning)
      {
        keepRunning = true;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Here, we can safely update the GUI
            // because we'll be called from the
            // event dispatch thread
            consoleArea.setText("\nStarting!");
          }
        });
        new Thread(new ServerListner(),"ServerListner").start();
      }
      else //If the server is already running shutdown the server and update the toggle button
      {
        try
        {
          keepRunning = false;
          this.stop();
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread
              consoleArea.setText(consoleArea.getText() + "\nStopping!");
              toggleStatusButton.setText("Start");
            }
          });
        }
        catch(Exception er)
        {
          er.printStackTrace();
        }

      }
    }

    /**
     * @return Checks to see if 'stop' is called
     */
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Signals stop to the server
     */
    public synchronized void stop(){
       this.isStopped = true;
       try {
           serverSock.close();
       } catch (IOException e) {
           throw new RuntimeException("Error closing server", e);
       }
   }
    /**
     * Asks the user what file they would like to host and begin adveristing the file
     */
    public void run()
    {
      synchronized(this){
            this.serverThread = Thread.currentThread();
        }
      try{

        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            // Here, we can safely update the GUI
            // because we'll be called from the
            // event dispatch thread
            toggleStatusButton.setText("Stop");
          }
        });
        }
        catch(Exception ie)
        {
          ie.printStackTrace();
        }
      //Begin Listening for clients to connect
      try{
        serverSock = new ServerSocket(port);
        while(!isStopped()) //BEGIN Server listening loop
        {
          if(!keepRunning || serverSock.isClosed()) break;
          Socket client = null;
          try
          {

            //Update the console area that the server is listening
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                // Here, we can safely update the GUI
                // because we'll be called from the
                // event dispatch thread
                consoleArea.setText(consoleArea.getText() + "\nListening.....");
                consoleArea.setText(consoleArea.getText() + "\nhosting local config file ");
              }
            });

            //Wait for client to connect
            client = serverSock.accept();
            final String clientAddr = client.getRemoteSocketAddress().toString();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                // Here, we can safely update the GUI
                // because we'll be called from the
                // event dispatch thread
                consoleArea.setText(consoleArea.getText() + "\nJust connected to " + clientAddr);
              }
            });
            new Thread(new FileTransferWorker(client,new space.gameressence.atiburcio.tyler.SerialFileData(space.gameressence.atiburcio.tyler.FileTransfer.CONFIG)),clientAddr).start();
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }

        } //END Server listening loop
        serverSock.close();
      }
      //If something broke send error message to console Area
      catch(IOException ex)
      {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Here, we can safely update the GUI
            // because we'll be called from the
            // event dispatch thread
            consoleArea.setText(consoleArea.getText() + "\n" +ex.getMessage());
          }
        });
        System.err.println("Server Sad :(");
        ex.printStackTrace();
      }
      //Update the console area that let the user know that the server has stopped listening
      finally
      {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Here, we can safely update the GUI
            // because we'll be called from the
            // event dispatch thread
            consoleArea.setText(consoleArea.getText() + "\nStop Listening");
          }
        });
      }
    }

    /**
     * Asks the user what file the server should host
     * @@deprecated Not used for HW 6
     */
    private class SelectFile implements ActionListener, Runnable
    {
      public void actionPerformed(ActionEvent e)
      {
        new Thread(new SelectFile(),"SelectFile").start();
      }

      public void run()
      {
        System.out.println("Selecting....");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file to host");
        fileChooser.showOpenDialog(INSTANCE);
        fileToTransfer = fileChooser.getSelectedFile();
        if(fileToTransfer == null) return;
      }
    }

  }
}
