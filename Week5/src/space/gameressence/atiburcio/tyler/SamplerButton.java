package space.gameressence.atiburcio.tyler;

//External Imports
import edu.hpu.epier.sound.*;

//Internal Java imports
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;

/**
 * SamplerButton is an extension of the JButton. The only difference is that
 * the SamplerButton has an added data field to point to the audio file to be played.
 *
 * When making a new instance of the Sampler Button it first asks the user what
 * audio file is assoicated to the button when clicked. Then an action listener
 * is added to the button so when clicked it will play the specifed audio file.
 * CHANGELOG: added author,version and see tag; Added Constuctor which also takes in the audio file as well as the name;
 *    Add getters for associated audio file and name
 * @author Tyler Atiburcio
 * @version 2
 * @see javax.swing.JButton#JButton(String)
 * @see space.gameressence.atiburcio.tyler.SoundboardUtil#readAudioFile(File)
 */
public class SamplerButton extends JButton implements Runnable, ActionListener
{
    //Variables that define a sample button
    File audioFile;
    String name;
  /**
   * Makes a new sampler button with the given name, but ask the user what audio file it should play
   * @param name The name of the button
   */
  public SamplerButton(String name)
  {
    super(name);
    this.name = name;
    //Ask User what audio file is assoicated to button
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Wave File","wav"));
    fileChooser.showOpenDialog(null);
    this.audioFile = fileChooser.getSelectedFile();
    //Finally add an ActionListener to the button, so when clicked it will play the specifed audio file
    this.addActionListener(this);
  }

  /**
   * Makes a new sampler button with the given name and audio file
   * @param name The name of the button
   * @param audioFile THe audio file that the sampler button will play
   */
  public SamplerButton(String name, File audioFile)
  {
    super(name);
    this.name = this.name;
    this.audioFile = audioFile;
    //Finally add an ActionListener to the button, so when clicked it will play the specifed audio file
    this.addActionListener(this);
  }

  /**
   * @return the audio file assoicated to the sampler button
   */
  public File getAudioFile()
  {
    return this.audioFile;
  }

  /**
   * @return The name given for the sampler button
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Will make a new thread and call the run method which will play the associated audio file
   */
  public void actionPerformed(ActionEvent e)
  {
      new Thread(this).start();
  }

  /**
   * Call the sound board utilties to play the associted audio file
   */
  public void run()
  {
    try
    {
        SoundboardUtil.readAudioFile(audioFile);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.err.println("Error playing sound!");
    }

  }


}
