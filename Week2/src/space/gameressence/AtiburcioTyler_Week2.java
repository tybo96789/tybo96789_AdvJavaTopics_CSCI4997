package space.gameressence;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* This is week 2 stuff of CSCI 4977; Threads
*
* @author Tyler Atiburcio
* @version 0.1
*/
public class AtiburcioTyler_Week2
{
  public static void main(String args[])
  {
    new ThreadedGUI();
  }

  /**
   * Nested class that extends GUITemplate, which creates a basic Java swing GUI
   * that runs user specified commands in a different thread
   * @see AtiburcioTyler_Week1.GUITemplate
   */
  protected static class ThreadedGUI extends AtiburcioTyler_Week1.GUITemplate
  {
      //Begin Containers
      protected JButton runButton;
      protected JTextField textArea;
      protected JLabel statusLabel;

      /**
       * Creates a instace of the GUITemplate and sets the title to "ThreadedGUI"
       * Also Overrides the template's JFrame layout to a 1 row 2 column layout
       *
       * @see AtiburcioTyler_Week1.GUITemplate
       */
      public ThreadedGUI()
      {
        super("ThreadedGUI");
        INSTANCE.layout = new GridLayout(1,2);
        INSTANCE.setLayout(INSTANCE.layout);
      }

      /**
       * Method is called in super's class constructor to make the containers
       * that go into the JFrame
       */
      @Override
      public void makeContainers()
      {
        //Begin Left side of Frame
        this.runButton = new JButton("Run!");
        this.runButton.addActionListener(new DoSomething());

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(3,1));
        left.add(new JLabel("<html>Enter your command into the text box and hit \"Run\"</html>"));
        left.add(runButton);
        left.add(new JLabel("Program Status:"));
        INSTANCE.add(left);
        //End left side of Frame

        //Begin right side of frame
        this.statusLabel = new JLabel("Ready!");
        this.textArea = new JTextField("");
        this.textArea.setEditable(true);

        JPanel right = new JPanel();
        right.setLayout(new GridLayout(3,1));
        right.add(new JLabel(""));
        right.add(this.textArea);
        right.add(this.statusLabel);
        INSTANCE.add(right);
        //End Right side of frame
      }


      /**
       * This class is used as a action Listener for the "Run" button but also
       * provides the functionality to run the user command in a different thread
       *
       *
       */
      protected class DoSomething implements Runnable, ActionListener
      {
        //Runnable variables
          //Sleeptime is need to prevent user from spamming the "run" button
        private final int SLEEPTIME = 3000;
        protected ProcessBuilder cmd;
        //Future functionality if Process standard input and output needs to be
        //Redirected
        protected Process proc;

        /**
         * This method starts the new thread instance of this class and
         * calls "start" method.
         *
         * Used for ActionListerner
         */
        public void actionPerformed(ActionEvent e)
        {
           new Thread(new DoSomething()).start();
        }

        /**
         * This method phrases the userinput from the text field and runs that
         * command in a separated thread. At the same time it updates the GUI
         * with the state of running the program.
         *
         * Used for Runnable
         */
        public void run()
        {
          try
          {
            //Grab what the user have typed into the text field and trim
            cmd = new ProcessBuilder(textArea.getText().trim());

            //safely update the status label in the GUI and disable "run" button
            SwingUtilities.invokeAndWait(new Runnable() {
              public void run() {
                // Here, we can safely update the GUI
                // because we'll be called from the
                // event dispatch thread
                runButton.setEnabled(false);
                statusLabel.setText("Running!");
              }
            });
            //Run the command
            //Stored in proc for any future expansion of the program where
            //standard input and output needs to be piped else where
            proc = cmd.start();
            //Sleep thread; Reason to prevent user from hitting the button multiple times
            Thread.sleep(SLEEPTIME);
          }
          catch(Exception e)
          {
            System.err.println("Thread Sad :(");
            try
            {
            SwingUtilities.invokeAndWait(new Runnable() {
              public void run() {
                // Here, we can safely update the GUI
                // because we'll be called from the
                // event dispatch thread
                statusLabel.setText("Error Running Command!.");
              }
            });
            }
            catch(Exception ie)
            {
              System.err.println("Error Running Command");
            }
          }
          finally
          {
            try
            {
            //safely update the status label in the GUI and re-enable "run" button
            SwingUtilities.invokeAndWait(new Runnable() {
              public void run() {
                // Here, we can safely update the GUI
                // because we'll be called from the
                // event dispatch thread
                runButton.setEnabled(true);
                statusLabel.setText("Idle.");
              }
            });
            }
            catch(Exception ie)
            {
              System.err.println("Something is wrong with main Frame!");
            }
          }
        }
      }
  }
}
