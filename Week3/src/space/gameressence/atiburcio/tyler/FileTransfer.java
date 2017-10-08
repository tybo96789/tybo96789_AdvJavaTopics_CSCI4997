package space.gameressence.atiburcio.tyler;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;

/**
 * A simple file transfer program; Server to Client
 * @author Tyler Atiburcio
 * @version 0.1
 */
public class FileTransfer
{

  //Defaults Declarations
  protected final static int DEFAULTLISTENPORT = 2121;
  protected final static int FILE_SIZE_LIMIT = 1000000;

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
              new ServerGUI();
              break;
            }
          case("C"):
          case("CILENT"):
          {
              new ClientGUI();
          }
        }
      else
        new ClientGUI();
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

  /**
   * ServerGUI; Hosts a file to serve to clients
   * Used GUI template template
   * @see GUITemplate
   */
  protected static class ServerGUI extends GUITemplate
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
    public ServerGUI()
    {
      this(DEFAULTLISTENPORT);
    }

    /**
     * Makes a server using the specifed port
     * @param port The port number that the server should bind to
     */
    public ServerGUI(int port)
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
        INSTANCE.mainPanel.setLayout(new GridLayout(2,1));
        this.consoleArea = new JTextArea();
        this.consoleArea.setEditable(false);
        INSTANCE.mainPanel.add(this.consoleArea);
        this.toggleStatusButton = new JButton("Start");
        this.toggleStatusButton.addActionListener(new ServerListner());
        INSTANCE.mainPanel.add(this.toggleStatusButton);
        INSTANCE.add(INSTANCE.mainPanel);

    }

    /**
     * Allows the server to start listening to the specifed port, as well as asking the user what file to host
     */
    private class ServerListner implements ActionListener, Runnable
    {
      //Server Listener related vars
      private FileInputStream fileInStream;
      private BufferedInputStream buffInStream;
      private OutputStream out;

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
              consoleArea.setText(consoleArea.getText() + "\nStarting!");
            }
          });
          new Thread(new ServerListner(),"ServerListner").start();
        }
        else //If the server is already running shutdown the server and update the toggle button
        {
          try
          {
            keepRunning = false;
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
       * Asks the user what file they would like to host and begin adveristing the file
       */
      public void run()
      {
        try{
          //Ask user what they would like to host
          new Thread(new SelectFile()).start();
          // JFileChooser fileChooser;
          //   // fileChooser = new JFileChooser();
    			// 	// fileChooser.setDialogTitle("Choose a file to host");
          //   // fileChooser.showOpenDialog(INSTANCE);
          //   // fileToTransfer = fileChooser.getSelectedFile();
          //   // if(fileToTransfer == null) JOptionPane.showMessageDialog(INSTANCE, "Invalid file selected!");

          SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread
              // fileChooser = new JFileChooser();
      				// fileChooser.setDialogTitle("Choose a file to host");
              // fileChooser.showOpenDialog(INSTANCE);
              // fileToTransfer = fileChooser.getSelectedFile();
              // if(fileToTransfer == null) JOptionPane.showMessageDialog(INSTANCE, "Invalid file selected!");
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
          while(true) //BEGIN Server listening loop
          {
            if(!keepRunning || serverSock.isClosed()) break;
            try
            {
              //Update the console area that the server is listening
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  // Here, we can safely update the GUI
                  // because we'll be called from the
                  // event dispatch thread
                  consoleArea.setText(consoleArea.getText() + "\nListening.....");
                }
              });
              //Wait for client to connect
              Socket server = serverSock.accept();

              //Update console area
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  // Here, we can safely update the GUI
                  // because we'll be called from the
                  // event dispatch thread
                  consoleArea.setText(consoleArea.getText() + "\nJust connected to " + server.getRemoteSocketAddress());
                  consoleArea.setText(consoleArea.getText() + "\nhosting file: "+ fileToTransfer.getAbsolutePath());
                }
              });

              byte [] data  = new byte [(int)fileToTransfer.length()];
              fileInStream = new FileInputStream(fileToTransfer);
              buffInStream = new BufferedInputStream(fileInStream);
              //Read the file to buffer
              buffInStream.read(data,0,data.length);
              out = server.getOutputStream();

              //Update console area that the server is sending the file
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  // Here, we can safely update the GUI
                  // because we'll be called from the
                  // event dispatch thread
                  consoleArea.setText(consoleArea.getText() + "\nSending " + fileToTransfer.getName() + "(" + data.length + " bytes)");
                }
              });
              //transfer file to client
              out.write(data,0,data.length);
              //Clear buffer
              out.flush();
              //Properly close all connections
              buffInStream.close();
              out.close();
              server.close();

            }
            catch(IOException e)
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
  				//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fileChooser.showOpenDialog(null);
          fileToTransfer = fileChooser.getSelectedFile();
          if(fileToTransfer == null) return;
          //fileNameField.setText(fileToTransfer.getAbsolutePath());
          //filePath = fileToTransfer.getAbsolutePath();
  			}
  		}

    }
  }

  /**
   * Client GUI of the program; Download file from the server
   */
  protected static class ClientGUI extends GUITemplate
  {

    //GUI Vars
    protected JButton selectFile;
    protected JButton downloadFile;
    protected JTextField fileNameField;
    protected JPanel buttonPanel;

    //Client Vars
    protected int port;
    protected String addr;
    protected File fileToDownload;
		protected String filePath;


    //Client Constants
    protected final static String fileName = "/Recv.file";

    /**
     * Make a Client gui that connects the specifed address and port
     * @param addr The IP address of the server to connect to
     * @param port The port number to connect to
     */
    public ClientGUI(String addr, int port)
    {
      super("Client");
      this.addr = addr.trim();
      this.port = port;
      INSTANCE.setLayout(new GridLayout());
    }

    /**
     * Creates a CLient that connects to a specifed address at the default server listening port
     * @param addr The address to connect to
     */
    public ClientGUI(String addr)
    {
      this(addr,DEFAULTLISTENPORT);
    }

    /**
     * Creates a client using program defaults (Localhost:DEFAULTLISTENPORT)
     */
    public ClientGUI()
    {
      this("",DEFAULTLISTENPORT);
    }


    @Override
    public void makeContainers()
    {
      //Main panel
      INSTANCE.mainPanel.setLayout(new GridLayout(2,1));
      //JTextField
      this.fileNameField = new JTextField();
      INSTANCE.mainPanel.add(this.fileNameField);
      //Button Panel
      this.buttonPanel = new JPanel();
      this.buttonPanel.setLayout(new GridLayout(1,2));
      this.selectFile = new JButton("Select Save Directory");
      this.buttonPanel.add(this.selectFile);
      this.selectFile.addActionListener(new SelectFile());
      this.downloadFile = new JButton("Download File");
      this.buttonPanel.add(this.downloadFile);
      this.downloadFile.addActionListener(new DownloadFile());
      //FInally add panels to Frame
      INSTANCE.mainPanel.add(this.buttonPanel);
      INSTANCE.add(INSTANCE.mainPanel);
    }

    /**
     * Action class that asks the user where to save the recieved file
     */
		private class SelectFile implements ActionListener, Runnable
		{
			public void actionPerformed(ActionEvent e)
			{
				new Thread(new SelectFile(),"SelectFile").start();
			}

			public void run()
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose a directory to save your file");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showSaveDialog(null);
        fileToDownload = fileChooser.getSelectedFile();
        if(fileToDownload == null) return;
        fileNameField.setText(fileToDownload.getAbsolutePath());
        filePath = fileToDownload.getAbsolutePath();
			}
		}

    /**
     * Action class that download the hosted file from the server and save it to a user define directory
     */
    private class DownloadFile implements ActionListener, Runnable
    {
      public void actionPerformed(ActionEvent e)
      {
          if(fileToDownload != null)
            new Thread(new DownloadFile(),"DownloadFile").start();
          else
          {
            JOptionPane.showMessageDialog(INSTANCE, "File save location not specified!, Please hit Select File First!");
						new Thread(new SelectFile()).start();
          }
      }


      /**
       * Establish the connection to the server and download the file from server
       * and save the file to the user specifed directory
       */
      public void run()
      {
        try
        {
          Socket client = new Socket("localhost",DEFAULTLISTENPORT);
          int bytesRead = 0;
          int current = 0;
          int fileSize = 0;
          FileOutputStream fileOutStream;
          BufferedOutputStream buffoutStream;
          InputStream inStream = client.getInputStream();
          //Touch the file in the directory so java does not throw an error that it doesnt exist
          new File(filePath+fileName).createNewFile();
          fileOutStream = new FileOutputStream(filePath+fileName);
          buffoutStream = new BufferedOutputStream(fileOutStream);
          byte[] data = new byte[FILE_SIZE_LIMIT];
          bytesRead = inStream.read(data,0,data.length);
          current = bytesRead;
          //Calculate the actual amount of space the file actually uses
          do
          {
             bytesRead = inStream.read(data, current, (data.length-current));
             if(bytesRead >= 0) current += bytesRead;
          }
          while(bytesRead > -1);

            //Write file to touched file
            buffoutStream.write(data, 0 , current);
            //Flush buffer
            buffoutStream.flush();
            //Let the user know that the file has been downloaded
            JOptionPane.showMessageDialog(INSTANCE, "File downloaded: " + filePath + fileName
                + "\ndownloaded (" + current + " bytes read)");

            //safely close the connection to the server
            fileOutStream.close();
            buffoutStream.close();
            client.close();
        }
        catch(IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
