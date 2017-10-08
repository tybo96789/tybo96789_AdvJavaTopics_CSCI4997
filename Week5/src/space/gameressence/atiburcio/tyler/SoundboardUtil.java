package space.gameressence.atiburcio.tyler;

import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;

import edu.hpu.epier.sound.*;

/**
 * A Modfied readAudioFile method from edu.hpu.epier.sound.Main, which now accepts
 * a file as an argument and plays that audio file to the computer's speakers
 * CHANGELOG: Added Version tag
 * @author E. Pier, Tyler Atiburcio
 * @version 1.01
 */
public class SoundboardUtil
{
  /**************************************************************************
  * Reads data from an audio file and plays it to your sound card.
  * Modfied Version to attempt to read a file given its path
  * @author E. Pier, Tyler Atiburcio
  * @see edu.hpu.epier.sound.Main#readAudioFile()
  * @version 2
  * @param file The audio file to be played
  **************************************************************************/
  public static void readAudioFile(File file) throws Exception {


     /********************************************************************
     * open the sound file. By default Java can only read WAV format
     * files and a couple other obscure uncompressed formats.
     * Java has a way to plug in support for other audio formats,
     * like MP3. Google "java sound mp3 spi" for some options.
     * An AudioInputStream is just a regular IO InputStream that
     * also figures out the AudioFormat of the stream.
     ********************************************************************/
     AudioInputStream in = AudioSystem.getAudioInputStream(
                                    new BufferedInputStream(
                                    new FileInputStream(file)));

      /************************************************************
      * print the format of the audio - like the sampe rate, etc. *
      ************************************************************/
      System.out.println(in.getFormat());

      /**********************************************************
      * get a data line and for playing sound on the sound card *
      **********************************************************/
      SourceDataLine line = Main.getOutput(0);

      /************************************************************
      * Open the data line with the format of the data in the
      * AudioInputStream. There is a good chance that your sound
      * card will support this format, but it might not. Better
      * code would catch the appropriate exception and then use
      * one of the methods in AudioSystem to convert the stream
      * to a format that you know your sound card supports.
      ************************************************************/
      line.open(in.getFormat());
      line.start();

      /*******************************************************
      * create a buffer to hold the data we will read from
      * the file and send to the sound card.
      *******************************************************/
      byte[] buffer = new byte[1024];

      /***************************
      * loop over chunks of data *
      ***************************/
      while(true) {
          /*************************************
          * read a chunk of data from the file *
          *************************************/
          int nread = in.read(buffer, 0, buffer.length);

          /**************************
          * check for end of stream *
          **************************/
          if(nread == -1) break;

          /**************************************************************
          * send the data to the sound card. The sound card might not
          * be able to take all the data that we send it, so we have
          * to loop until it takes it all.
          *************************************************************/
          for(int nsent=0; nsent<nread; ) {
              /************************************************
              * send as much data as the sound card will take *
              ************************************************/
              int n = line.write(buffer, nsent, nread-nsent);

              if(n == 0) {
                  /****************************************
                  * the sound card is full, so pause for
                  * a little bit to let it drain
                  ****************************************/
                  try { Thread.sleep(10l); }
                  catch(InterruptedException e) {}
              }
              /*********************************************
              * increment the number of bytes we have sent *
              *********************************************/
              nsent += n;
          } // end of loop to send data
      } // end of loop over chunks of data

  } // end of readAudioFile method
}
