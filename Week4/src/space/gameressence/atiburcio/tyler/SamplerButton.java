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
 */
public class SamplerButton extends JButton implements Runnable, ActionListener
{
    File audioFile;

  public SamplerButton(String name)
  {
    super(name);

    //Ask User what audio file is assoicated to button
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Wave File","wav"));
    fileChooser.showOpenDialog(null);
    audioFile = fileChooser.getSelectedFile();
    //Finally add an ActionListener to the button, so when clicked it will play the specifed audio file
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e)
  {
      new Thread(this).start();
  }

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
