package space.gameressence.atiburcio.tyler.util;

//Java internal Imports
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * This is week 1 stuff for CSCI 4997. Previously Named AtiburcioTyler_Week1
 * This abstract class provides a basic foundation and function for a java GUI (JPanel) application
 * Comes with a status bar
 * CHANGELOG: Changed extended class from JFrame to JPanel
 * @see javax.swing.JPanel
 * @author Tyler Atiburcio
 * @version 0.5
 */
public abstract class GUITemplate extends JPanel
{

      //Declare Vars and constants
      //Init Stuff
      private final int WIDTH, HEIGHT;
      private String title;
      protected final GUITemplate INSTANCE = this;
      protected JFrame frame = new JFrame();

      //Stats bar stuff
      protected JPanel statusPanel = new JPanel();
      protected JLabel statusLabel = new JLabel("Status: OK");

      //JMenu Stuff
      protected JMenuBar menubar = new JMenuBar();
      protected JMenu file = new JMenu("File");
        //JMenu File Sub
        protected JMenuItem exit = new JMenuItem("Exit");


      /**
      * Customizable contructor with Width, height, title, and layout manger as parameters
      * @param WIDTH The inital width of the window in px
      * @param HEIGHT The inital width of the window in px
      * @param title The inital title of the window
      * @param layout The layout to be set for the JFrame
      */
      public GUITemplate(int WIDTH, int HEIGHT, String title, LayoutManager layout)
      {
          this.WIDTH = WIDTH;
          this.HEIGHT = HEIGHT;
          this.title = title;
          frame.setSize(WIDTH,HEIGHT);
          frame.setTitle(title);
          this.setLayout(layout);
          //Begin Making containers
          this.makeToolBar();
          this.makeStatusBar();
          this.makeContainers();
          //Final touchups
            //Exit On close
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Positions the application to be a free floating one
          frame.setLocationRelativeTo(null);
          //Finally make the gui application visable
          frame.setVisible(true);
      }

      /**
      * Create base java GUI application with 500px by 500px width and height and with the given String for the title of the window
      * @param title The title of the window
      * @see #GUITemplate(int WIDTH, int HEIGHT, String title)
      */
      public GUITemplate(String title)
      {
        this(500,500,title,new BorderLayout());
      }

      /**
       * Creates a base java GUI application with the given width and height and String title, while not specifying a layout manager
       * @param WIDTH int; GUITemplateThe width of the JFrame in pixels
       * @param HEIGHT int; The height of the JFrame in pixels
       * @param title String; The title of the JFrame
       */
      public GUITemplate(int WIDTH, int HEIGHT, String title)
      {
        this(500,500,title,new BorderLayout());
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
        INSTANCE.frame.setJMenuBar(this.menubar);

      }

      /**
       * @return JMenuBar; Return AccessibleAWTContainerhe Menu bar of the frame
       */
      public JMenuBar getMenuBar()
      {
        return this.menubar;
      }

      /**
       * Makes the botton status bar on the frame and adds it to the frame using south constraint
       */
      public void makeStatusBar()
      {
        //Makes status bar have an seporator look from the rest of the content
        this.statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        //Set the size of the status panel so it doesnt use the whole frame
        this.statusPanel.setPreferredSize(new Dimension(this.frame.getWidth(), 16));
        this.statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        //Left alignment of text
        this.statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        //Adds the status label to the status panel
        this.statusPanel.add(this.statusLabel);
        //Adds the statusbar to the frame, using 'south' constraint
        this.getMainPanel().add(this.statusPanel,BorderLayout.SOUTH);
      }

      /**
       * @return JPanel; The panel holding the status bar
       */
      public JPanel getStatusBar()
      {
        return this.statusPanel;
      }

      /**
       * @return JLabel; The status label that is attached to the status panel
       */
      public JLabel getStatusLabel()
      {
        return this.statusLabel;
      }

      /**
       * Sets the JLabel of the status bar to the passed argument
       * Label is updated via swing utilities invoke later
       * @param s String; The text to set the status bar to
       */
      public void setStatus(String s)
      {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Here, we can safely update the GUI
            // because we'll be called from the
            // event dispatch thread
            statusLabel.setText(s);
          }
        });

      }

      /**
       * @return JPanel; Return the main content panel of the frame
       */
      public JPanel getMainPanel()
      {
        return (JPanel) frame.getContentPane();
      }

      /**
       * Helper method to add containers to the main content panel
       * NOTE: Pack is NOT called after adding componet
       * @param container The compoent to add to the main content panel
       * @param constraints The constraint to add to the assiocated compoent
       */
      public void addCompoment(Component container, Object constraints)
      {
        this.getMainPanel().add(container, constraints);
      }

      /**
       * Helper method to add content to the main panel to the cetner to the main panel
       * NOTE: Pack is NOT called after adding componet
       * @see #addCompoment(Component, Object)
       * @param container The component to add to the main panel
       */
      public void addCompoment(Component container)
      {
        this.addCompoment(container, BorderLayout.CENTER);
      }

}
