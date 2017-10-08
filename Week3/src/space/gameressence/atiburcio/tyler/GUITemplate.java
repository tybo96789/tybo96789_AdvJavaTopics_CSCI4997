package space.gameressence.atiburcio.tyler;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

/**
 * This is week 1 stuff for CSCI 4997. Previously Named AtiburcioTyler_Week1
 * This abstract class provides a basic foundation and function for a java GUI (JFrame) application
 * @see javax.swing.JFrame
 * @author Tyler Atiburcio
 * @version 0.3
 */
public abstract class GUITemplate extends JFrame
{

      //Declare Vars and constants
      //Init Stuff
      private final int WIDTH, HEIGHT;
      private String title;
      protected final GUITemplate INSTANCE = this;
      protected ArrayList<Container> containers = new ArrayList<Container>();
      protected GridLayout layout = new GridLayout();

      //Panel Stuff
      protected JPanel mainPanel = new JPanel();

      //JMenu Stuff
      protected JMenuBar menubar = new JMenuBar();
      protected JMenu file = new JMenu("File");
        //JMenu File Sub
        protected JMenuItem exit = new JMenuItem("Exit");


      /**
      * Customizable contructor with Width, height and title as parameters
      * @param WIDTH The inital width of the window in px
      * @param HEIGHT The inital width of the window in px
      * @param title The inital title of the window
      */
      public GUITemplate(int WIDTH, int HEIGHT, String title)
      {
          this.WIDTH = WIDTH;
          this.HEIGHT = HEIGHT;
          this.title = title;
          this.setSize(WIDTH,HEIGHT);
          this.setTitle(title);
          this.setLayout(this.layout);
          //Begin Making containers
          this.makeToolBar();
          this.makeContainers();
          //Final touchups
            //Exit On close
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Positions the application to be a free floating one
          this.setLocationRelativeTo(null);
          //Finally make the gui application visable
          this.setVisible(true);
      }

      /**
      * Create base java GUI application with 500px by 500px width and height and with the given String for the title of the window
      * @param title The title of the window
      * @see #GUITemplate(int WIDTH, int HEIGHT, String title)
      */
      public GUITemplate(String title)
      {
        this(500,500,title);
      }

      /**
      * Placeholder method so when extended should be called to make the containers that will go on the panel
      */
      public abstract void makeContainers();

      /**
      * Make a default toolbar with basic exit program function
      * File:Exit
      */
      protected void makeToolBar()
      {
        //Exit function Listener
        this.exit.addActionListener((ActionEvent event) ->
        {
          System.exit(0);
        });
        //Adds exit to file submenu
        this.file.add(this.exit);

        //Add file menu to menubar
        this.menubar.add(this.file);

        //Sets the JFrame menu bar to the menubar that we have just created
        INSTANCE.setJMenuBar(this.menubar);

      }

}
