package space.gameressence.atiburcio.tyler.client;

//Internal java imports
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;

//External imports
import space.gameressence.atiburcio.tyler.util.*;


/**
 * Client GUI of the program; Download file from the server
 * CHANGELOG: Modifed process of receiving data from server
 * @author Tyler Atiburcio
 * @version 2
 */
public class FileTransferClient extends GUITemplate
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
  protected final static String FILEEXTENSION = ".txt";

  /**
   * Make a Client gui that connects the specifed address and port
   * @param addr The IP address of the server to connect to
   * @param port The port number to connect to
   */
  public FileTransferClient(String addr, int port)
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
  public FileTransferClient(String addr)
  {
    this(addr,space.gameressence.atiburcio.tyler.FileTransfer.DEFAULTLISTENPORT);
  }

  /**
   * Creates a client using program defaults (Localhost:DEFAULTLISTENPORT)
   */
  public FileTransferClient()
  {
    this("",space.gameressence.atiburcio.tyler.FileTransfer.DEFAULTLISTENPORT);
  }


  @Override
  public void makeContainers()
  {
    //Main panel
    mainPanel.setLayout(new GridLayout(2,1));
    //JTextField
    this.fileNameField = new JTextField();
    mainPanel.add(this.fileNameField);
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
    mainPanel.add(this.buttonPanel);
    add(mainPanel);
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
     * Establish the connection to the server and download the Wrapper object from server
     * and output the contents of the wrapper unboxed data to a user defined file
     * @see space.space.gameressence.atiburcio.tyler.util.Config
     */
    public void run()
    {
      try
      {
        //Connect to the server
        Socket client = new Socket("localhost",space.gameressence.atiburcio.tyler.FileTransfer.DEFAULTLISTENPORT);
        //Get the input stream
        ObjectInputStream in = new ObjectInputStream(client.getInputStream());
        //Read the wrapper data
        space.gameressence.atiburcio.tyler.SerialFileData serialFileData = (space.gameressence.atiburcio.tyler.SerialFileData) in.readObject();
        //Unbox wrapper data
        Config data = (Config) serialFileData.getFile();

        BufferedWriter fileout = null;

    		try {

          //Write the contents of the unboxed data to a file, which the location of that file is user defined
          File dataOut = new File(filePath+"/"+data.getName()+ FILEEXTENSION);
          //Touch the file so there is no errors are thrown becase the file does not exist
          dataOut.createNewFile();
          //Begin write unboxed data
          fileout = new BufferedWriter(new FileWriter(dataOut));
          //Write the timestamp
          fileout.write(serialFileData.getTimeStamp()+"\n");
          //Write the contents of the unboxed wrapper data
          for(int i = 0; i < data.getKeys().size(); i ++)
          {
            fileout.write(data.getKeys().get(i)+"="+data.getValues().get(i)+"\n");
          }

          //Let the user know that the file has been downloaded
          JOptionPane.showMessageDialog(INSTANCE, "File downloaded: " + filePath +"/"+ dataOut.getName()
                  + "\ndownloaded (");
          //Flush and close the output streams
          fileout.flush();
          fileout.close();

    		} catch (Exception ex)
        {
    			ex.printStackTrace();
    		}
        //Finally close the socket to the server
        client.close();


      }catch(Exception e)
      {
        e.printStackTrace();
      }

    }
  }
}
