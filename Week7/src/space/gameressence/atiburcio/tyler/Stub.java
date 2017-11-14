package space.gameressence.atiburcio.tyler;

//Internal Java imports
//Java rmi
import java.rmi.*;
import java.rmi.registry.*;
//Java swing
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//External java imports
import space.gameressence.atiburcio.tyler.util.*;

/**
 * The stub of the RMI concept. This implementation is a GUI which the user can
 * enter a string to compute and the hash is displayed in the text area
 * @see space.gameressence.atiburcio.tyler.util.GUITemplate#GUITemplate(String)
 * @author Tyler Atiburcio
 * @version 1
 */
public class Stub extends GUITemplate
{
  //GUI variables
  protected JScrollPane scrollPane;
  protected JTextArea textArea;
  protected JPanel inputPanel;
  protected JButton sendButton;
  protected JTextField inputField;
  protected JMenu tools;
  protected JMenuItem stats;

  //RMI stub variables
  private Common server;
  private Registry registry;

  /**
   * Attempts to locate the "common" interface from the RMI registry
   */
  public Stub()
  {
    //Create a GUI named Stub
    super("Stub");
    //Connect to local RMI registry and locate the common interface
    try
    {
      this.registry = LocateRegistry.getRegistry("localhost");
      this.server = (Common) registry.lookup("Common");
      System.out.println("lookup okay");
    }
    catch(RemoteException e)
    {
      System.err.println("Unable to lookup RMI server!");
      e.printStackTrace();
    }
    catch(NotBoundException ea)
    {
      System.err.println("Common code not found in REI Registry!");
      ea.printStackTrace();
    }

  }

  /**
   * Creates the buttons and containers that go with the GUI
   */
  public void makeContainers()
  {
    //Results window
    this.setLayout(new GridLayout(2,1));
    this.textArea = new JTextArea("Results Window");
    this.scrollPane = new JScrollPane(this.textArea);
    this.add(this.scrollPane);

    //Input panel (Query Button/ input)
    this.inputPanel = new JPanel();
    this.inputPanel.setLayout(new GridLayout(1,2));
    this.sendButton = new JButton("Send");
    this.sendButton.addActionListener(new Query());
    this.inputPanel.add(this.sendButton);
    this.inputField = new JTextField();
    this.inputPanel.add(this.inputField);
    this.add(this.inputPanel);

    //JMenu Bar addons
    this.tools = new JMenu("Tools");
    this.stats = new JMenuItem("Stats");
    this.tools.add(this.stats);
    //Add menu item that querys the server stats
    this.stats.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          JOptionPane.showMessageDialog(null, Server.query());
        }
        catch(RemoteException ex)
        {
          System.err.println("Failed to get server stats!");
        }
      }
    });
    this.getJMenuBar().add(this.tools);
  }

  /**
   * Takes user input and calls on the server to compute the string, and finally
   * display the results to the text area
   * @see space.gameressence.atiburcio.tyler.Server#compute(String)
   */
  private class Query implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      SwingUtilities.invokeLater(new Runnable()
      {
      public void run()
      {
        try
        {
          //Take user input and send string off to server
          textArea.setText(textArea.getText() + "\n" +inputField.getText().trim()+ " -> " +server.compute(inputField.getText().trim()));
          //Set input field to ""
          inputField.setText("");
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
          System.err.println("Query Failed!");
        }
      }
    });
    }
  }
}
