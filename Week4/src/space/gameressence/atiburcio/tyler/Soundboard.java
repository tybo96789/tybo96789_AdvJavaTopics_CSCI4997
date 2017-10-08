package space.gameressence.atiburcio.tyler;

//External Imports
import edu.hpu.epier.sound.*;
import space.gameressence.atiburcio.tyler.util.GUITemplate;

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
 * USAGE: File-New Button; This will make a new JButton with the choosen audio file assoicated to the button when button is clicked.
 * Soundboard class is extended from the GUITemplate class which implements basic GUI functionality
 * @see space.gameressence.atiburcio.tyler.util.GUITemplate
 * @author Tyler Atiburcio
 */
public class Soundboard extends GUITemplate
{

    //JButtons
    ArrayList<JButton> buttons;
    ArrayList<File> audioFiles;
    JScrollPane frame;
    JButton test;
    JPanel buttonPanel;

    //JMenu addons
    JMenuItem newButton;

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
      this.buttons = new ArrayList<JButton>();
      INSTANCE.add(this.frame);

      //Toolbar addons
      this.newButton = new JMenuItem("New Button");
      this.newButton.addActionListener(new newSampleButton());
      INSTANCE.getJMenuBar().getMenu(0).add(this.newButton);

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
          ex.printStackTrace();
        }
      }
    }

}
