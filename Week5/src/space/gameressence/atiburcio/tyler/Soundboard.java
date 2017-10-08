package space.gameressence.atiburcio.tyler;

//External Imports
import edu.hpu.epier.sound.*;
import space.gameressence.atiburcio.tyler.util.GUITemplate;
import space.gameressence.atiburcio.tyler.util.Config;

//Internal Java Imports
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;

/**
 * Sound board application.
 * USAGE: Tools-New Button; This will make a new JButton with the choosen audio file assoicated to the button when button is clicked.
 *    File-load: Load a java properties file with a past layout
 *    File-save: Saves the current layout to a java properties file
 * Soundboard class is extended from the GUITemplate class which implements basic GUI functionality
 * CHANGELOG: Added Save/Load feature
 * @see space.gameressence.atiburcio.tyler.util.GUITemplate
 * @author Tyler Atiburcio
 * @version 2
 */
public class Soundboard extends GUITemplate
{

    //JButtons
    ArrayList<SamplerButton> buttons;
    JScrollPane frame;
    JButton test;
    JPanel buttonPanel;

    //JMenu addons
    JMenuItem newButton;
      //Save/Load
    JMenu tools;
    JMenuItem load;
    JMenuItem save;

    public static void main (String[] args)
    {
      new Soundboard();
    }
    /**
     * Makes a GUI window called Soundboard from the GUI template class
     * @see space.gameressence.atiburcio.tyler.util.GUITemplate#GUITemplate(String)
     */
    public Soundboard()
    {
      super("Soundboard");
    }

    /**
     * Implemented method from GUITemplate class that for this application
     * makes the panel that will hold the buttons
     */
    public void makeContainers()
    {
      //Button Panel
      this.buttonPanel = new JPanel();
      this.buttonPanel.setLayout(new GridLayout(2,2));
      this.frame = new JScrollPane(this.buttonPanel);
      this.frame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      this.frame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      this.buttons = new ArrayList<SamplerButton>();
      INSTANCE.add(this.frame);



      //Save/Load toolbar addons
      this.load = new JMenuItem("Load");
      INSTANCE.getJMenuBar().getMenu(0).add(this.load);
      this.load.addActionListener(new loadState());
      this.save = new JMenuItem("Save");
      INSTANCE.getJMenuBar().getMenu(0).add(this.save);
      this.save.addActionListener(new saveState());

      //Toolbar addons
      this.tools = new JMenu("Tools");
      INSTANCE.getJMenuBar().add(this.tools);
      this.newButton = new JMenuItem("New Button");
      this.newButton.addActionListener(new newSampleButton());
      this.tools.add(this.newButton);

    }

    /**
     * This action listener is attached to the "New Button" in the toolbar.
     * When called it makes a new JButton and asks the user what audio file it is assoicated to the button
     */
    protected class newSampleButton implements ActionListener, Runnable
    {

      public void actionPerformed(ActionEvent e)
      {
        new Thread(new newSampleButton()).start();
      }

      public void run()
      {
        try
        {
          SwingUtilities.invokeAndWait(new Runnable()
          {
            public void run() {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread

              //Ask the user what to name the button
              String name = JOptionPane.showInputDialog(INSTANCE, "What would you like to call your new Button?");
              //Make a new instance of SamplerButton (JButton) and store into an array
              buttons.add(new SamplerButton(name.trim()));
              //Add the button to the frame
              buttonPanel.add(buttons.get(buttons.size()-1));
              //Update the frame
              INSTANCE.revalidate();
              INSTANCE.repaint();
            }
          });
        }
        catch(Exception ex)
        {
          System.err.println("Error makeing new Button!");
          JOptionPane.showMessageDialog(INSTANCE, "Error Making new Button");
          ex.printStackTrace();
        }
      }
    }

    /**
     * Saves all the buttons information to a java properties file
     * First asks the user where to store the config file, then saves the buttons
     */
    protected class saveState implements Runnable, ActionListener
    {
      public void actionPerformed(ActionEvent e)
      {
        new Thread(new saveState()).start();
      }
      public void run()
      {
        try
        {
          JFileChooser chooser = new JFileChooser();
          SwingUtilities.invokeAndWait(new Runnable()
          {
            public void run() {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread
              try
              {
                //Set the default directory to where the program was launched
                File currDir = new File(new File("./config.properties").getCanonicalPath());
                chooser.setCurrentDirectory(currDir);
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("properties file","properties"));
                int result = chooser.showSaveDialog(INSTANCE);
                if(result != JFileChooser.APPROVE_OPTION) return;
              }
              catch(Exception ec)
              {
                ec.printStackTrace();
              }

            }
          });

          //Make a new Config instance and save the name of the button and the assoicated audio file as a property
          Config config = new Config(chooser.getSelectedFile().getPath());
          for(int i = 0; i < buttons.size(); i++)
            config.add(buttons.get(i).getName(),buttons.get(i).getAudioFile().getPath());
          config.save();
          JOptionPane.showMessageDialog(INSTANCE, "Saved layout to " + chooser.getSelectedFile().getPath());

        }
        catch(Exception ex)
        {
          System.err.println("Error saving!");
          ex.printStackTrace();
        }
      }
    }

    /**
     * Loads a java configuration file that contains information about the buttons (name, and audiofile)
     */
    protected class loadState implements Runnable, ActionListener
    {
      public void actionPerformed(ActionEvent e)
      {
        new Thread(new loadState()).start();
      }
      public void run()
      {
        try
        {
          SwingUtilities.invokeAndWait(new Runnable()
          {
            public void run() {
              // Here, we can safely update the GUI
              // because we'll be called from the
              // event dispatch thread

              //Ask the user where this java property file is located
              JFileChooser chooser = new JFileChooser();
              try
              {
                //Set the default directory to where the program was launched
                File currDir = new File(new File("./config.properties").getCanonicalPath());
                chooser.setCurrentDirectory(currDir);
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("properties file","properties"));
                int result = chooser.showOpenDialog(INSTANCE);
                if(result != JFileChooser.APPROVE_OPTION) return;
              }
              catch(Exception ec)
              {
                ec.printStackTrace();
              }

              //Clear all buttons from the panel
              buttons.clear();
              buttonPanel.removeAll();
              INSTANCE.revalidate();
              INSTANCE.repaint();


              //Begin opening the config file
              Config config = Config.Import(chooser.getSelectedFile().getPath());
              for(int i = 0; i < config.getKeys().size(); i ++)
              {
                //Make a new instance of SamplerButton (JButton) and store into an array
                buttons.add(new SamplerButton(config.getKeys().get(i),new File(config.getValues().get(i))));
                //Add the button to the frame
                buttonPanel.add(buttons.get(buttons.size()-1));
              }

              //Update the frame
              INSTANCE.revalidate();
              INSTANCE.repaint();
            }
          });
        }
        catch(Exception ex)
        {
          System.err.println("Error loading!");
          JOptionPane.showMessageDialog(INSTANCE, "Error loading layout!");
          ex.printStackTrace();
        }
      }
    }

}
