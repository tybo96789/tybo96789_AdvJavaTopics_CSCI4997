package edu.hpu.epier.sound;

import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;

/**************************************************************************
* A class that holds a bunch of static methods to demonstrate the Java
* sound API. All of these methods are a bit sloppy with their error
* handling and just throw any exception.
* @author E. Pier 
**************************************************************************/
public class Main {

/**************************************************************************
* Lists the input (target) an output (source) data lines available
* on your system. These are essentially microphones and speakers.
**************************************************************************/
public static void listAudioDevices() throws Exception {

    /*********************************************************
    * loop over all the mixers. In this sense, a "mixer" is
    * a piece of hardware, like a sound card that has inputs
    * and outputs. On a regular PC it's never really a literal
    * mixer, that takes multiple inputs and combines them into
    * a single output. You may have virtual devices on your
    * system, that are software layers over the actual hardware.
    * So you may have more than one Java Mixer even if you only
    * have one sound card.
    **********************************************************/
    Mixer.Info[] mixer_info_list = AudioSystem.getMixerInfo();
    for(Mixer.Info mixer_info : mixer_info_list) {

        /**********************************
        * print a desription of the mixer *
        **********************************/
        System.out.println("__________________________________________");
        System.out.println(mixer_info.getName()+" "+
                           mixer_info.getVersion()+" "+
                           mixer_info.getVendor());

        System.out.println(mixer_info.getDescription());
        System.out.println();

        /***********************
        * get the actual mixer *
        ***********************/
        Mixer mixer = AudioSystem.getMixer(mixer_info);

        /****************************************************
        * get the input lines. A "line" is a channel over
        * which sound data can pass.
        ****************************************************/
        Line.Info[] line_info_list = mixer.getTargetLineInfo();
        for(Line.Info line_info : line_info_list) {

            /**************************
            * only look at data lines *
            **************************/
            if(! (line_info instanceof TargetDataLine.Info)) continue;


            /***************************
            * get a decriptive string *
            ***************************/
            System.out.println("    Input:  "+line_info);
            System.out.println();


        } // end of loop over input lines

        /*************************************
        * now loop over all the output lines *
        *************************************/
        line_info_list = mixer.getSourceLineInfo();
        for(Line.Info line_info : line_info_list) {

            /**************************
            * only look at data lines *
            **************************/
            Line line = mixer.getLine(line_info);
            if(! (line instanceof SourceDataLine)) continue;

            /***************************
            * get a decriptive string *
            ***************************/
            System.out.println("    Output: "+line.getLineInfo());
            System.out.println();

        } // end of loop over input lines

        /**************
        * skip a line *
        **************/
        System.out.println();

    } // end of loop over mixers

} // end of listInputsAndOutputs method

/**************************************************************************
* A utility for getting a particular input (target) line.
* In a fully developed application you should present a menu to the
* user so they can choose a different input. For simple demonstration
* applications like these it is enough to just pick a more or less
* arbitrary one, as long as there is the ability to pick a different
* one, if the first one doesn't work.
* @param index The arbitrary index of the line, counting from zero.
* @throws IndexOutOfBoundsException if there is no line for that index.
**************************************************************************/
public static TargetDataLine getInput(int index) throws Exception {

    /***************************
    * loop over all the mixers *
    ***************************/
    int i=0;
    Mixer.Info[] mixer_info_list = AudioSystem.getMixerInfo();
    for(Mixer.Info mixer_info : mixer_info_list) {
        /***********************
        * get the actual mixer *
        ***********************/
        Mixer mixer = AudioSystem.getMixer(mixer_info);

        /**********************
        * get the input lines *
        **********************/
        Line.Info[] line_info_list = mixer.getTargetLineInfo();
        for(Line.Info line_info : line_info_list) {

            /**************************
            * only look at data lines *
            **************************/
            if(! (line_info instanceof TargetDataLine.Info)) continue;


            /**********************************************
            * see if we found the line we are looking for *
            **********************************************/
            if(i == index) {
                return (TargetDataLine)mixer.getLine(line_info);
            }

            /***************************
            * increment the line count *
            ***************************/
            ++i;

        } // end of loop over input lines

    } // end of loop over mixers

    throw new IndexOutOfBoundsException("No such line");

} // end of getInput method


/**************************************************************************
* A utility for getting a particular output (source) line.
* In a fully developed application you should present a menu to the
* user so they can choose a different input. For simple demonstration
* applications like these it is enough to just pick a more or less
* arbitrary one, as long as there is the ability to pick a different
* one, if the first one doesn't work.
* @param index The arbitrary index of the line, counting from zero.
* @throws IndexOutOfBoundsException if there is no line for that index.
**************************************************************************/
public static SourceDataLine getOutput(int index) throws Exception {

    /***************************
    * loop over all the mixers *
    ***************************/
    int i=0;
    Mixer.Info[] mixer_info_list = AudioSystem.getMixerInfo();
    for(Mixer.Info mixer_info : mixer_info_list) {
        /***********************
        * get the actual mixer *
        ***********************/
        Mixer mixer = AudioSystem.getMixer(mixer_info);

        /**********************
        * get the input lines *
        **********************/
        Line.Info[] line_info_list = mixer.getSourceLineInfo();
        for(Line.Info line_info : line_info_list) {

            /**************************
            * only look at data lines *
            **************************/
            Line line = mixer.getLine(line_info);
            if(! (line instanceof SourceDataLine)) continue;

            /**********************************************
            * see if we found the line we are looking for *
            **********************************************/
            if(i == index) {
                return (SourceDataLine)line;
            }

            /***************************
            * increment the line count *
            ***************************/
            ++i;

        } // end of loop over input lines

    } // end of loop over mixers

    throw new IndexOutOfBoundsException("No such line");

} // end of getOutput method

/**************************************************************************
* Creates an AudioFormat that represents "CD quality" sound. The
* sample rate is sufficient to encode the full frequency response of
* the human ear, sampes are 16 bit signed integers (i.e. Java shorts),
* and we have chosen big endian byte order, because that is what Java
* uses internally. The encoding is "Linear PCM", which means the samples
* are proportional to the actual signal. "PCM" stands for
* "Pulse Code Modulated". We have chosen mono, single channel, sound
* because it is simpler for the sake of demonstration.
**************************************************************************/
public static AudioFormat createCDQualityFormat() {

    return new AudioFormat(44100.f, // samples per second
                           16,      // bits per sample
                           1,       // 1=mono, 2=stereo
                           true,    // signed
                           true     // big endian
                          );

} // end of createCDQualityFormat method

/**************************************************************************
* Demonstrates reading sound from your sound card.
* This method displays the wave form to the terminal in a cheap sort
* of ASCII art display. It runs forever until you kill it.
**************************************************************************/
public static void readFromMicrophone() throws Exception {

    /*************
    * get a line *
    *************/
    TargetDataLine line = getInput(0);

    /*****************************************************************
    * open the line and start it. Opening reserves the line for your
    * application's use and configures the format. Starting the line
    * begins the data flow.
    *****************************************************************/
    AudioFormat format = createCDQualityFormat();
    line.open(format);
    line.start();

    /************************************
    * read data from the line in chunks *
    ************************************/
    byte[] buffer = new byte[1024];
    while(true) {
        /***********************
        * read a chunk of data *
        ***********************/
        int nread = line.read(buffer, 0, buffer.length);

        /*********************************************
        * loop over the samples. Each one is 2 bytes *
        *********************************************/
        for(int i=0; i<nread; i+=2) {

            /*********************************************
            * put the sample together into a 2 byte int
            * we could store this in a short, but we use an
            * int because in a little bit we will do some
            * math that would overflow a short.
            * The &0xff is because Java bytes are signed
            * integers, so when you convert them to ints
            * they can have extraneous signed bits to the
            * left of the first 8.
            *********************************************/
            int sample = (short)((buffer[i  ]&0xff)<<8 |
                                 (buffer[i+1]&0xff));

            /*****************************************************
            * this is a really cheap way to plot sound waves
            * without cluttering up the example code with a GUI
            * we convert the sample to a number of spaces
            * to print before some character.
            *****************************************************/
            int nspaces = (sample- Short.MIN_VALUE)*
                           80/(Short.MAX_VALUE - Short.MIN_VALUE);

            StringBuilder builder = new StringBuilder();
            for(int j=0; j<nspaces; ++j) {
                builder.append(" ");
            }
            builder.append("*");

            System.out.println(builder.toString());

        } // end of loop over smaples
    } // end of loop over chunks of data

    /***************************************************************
    * this application runs forever, but a real app should have a
    * way of stopping and should stop and close the line
    ***************************************************************/

} // end of showInput method


/**************************************************************************
* Demonstrates sending data to your sound card, i.e. playing a sound.
* We create our own sound wave. It plays at full volume, so turn down
* your speaker volme before running this.
**************************************************************************/
public static void playSineWave() throws Exception {

    /**************************
    * get an output data line *
    **************************/
    SourceDataLine line = getOutput(0);

    /*********************************************************
    * open the line and start it. Opening reserves the line
    * for your use and sets the format. Starting the line
    * begins the data flow.
    *********************************************************/
    AudioFormat format = createCDQualityFormat();
    line.open(format);
    line.start();

    /**************************************************
    * set the frequence in Hz. Perfect human hearing
    * ranges from 20 to 20,000 Hz. In practice your
    * speakers and ear probably don't.
    **************************************************/
    double frequency = 1000.0;

    /*************************
    * allocate a data buffer *
    *************************/
    byte[] buffer = new byte[1024];

    /***********************************************************
    * loop over chunks of data.
    * The index variable holds a running count of the samples
    * that increases monotonically
    ***********************************************************/
    int index = 0;
    while(true) {
        /***********************************************************
        * Check how much data the sound card is ready to receive
        * we can only feed data to it as fast as it plays it,
        * which is determined by the sample rate
        **********************************************************/
        int available = line.available();
        if(available == 0) {
            /***************************************************
            * the sound card is full, so lets pause for
            * a bit to let it play some sound. We picked
            * a 10 ms sleep time because it worked for
            * our sample rate, but in general you might
            * have to be more clever about picking the right
            * time to keep up the steady flow of sound, but
            * not use mre CPU than you have to.
            **************************************************/
            try { Thread.sleep(10l);}
            catch(InterruptedException e) {}

        } // end if the soud card is full

        /**********************************
        * loop over samples in the buffer
        * and fill them with a running sin function
        **********************************/
        if(available > buffer.length) available = buffer.length;
        for(int i=0; i<available; i+=2, ++index) {

            /******************************************************
            * compute the angle that we will take the sign of.
            * This has to continually increase, so we calculate
            * it off of the index variable. The sample rate lets
            * us rescale the index to seconds. Then we need to
            * do a full 2pi radians cycle every 1/frequency seconds.
            *****************************************************/
            double theta = index/format.getSampleRate()*
                           2.0*Math.PI*frequency;

            /********************************************
            * the sin function cycles between -1 and 1.
            * we need to scale this to the range of our
            * 16 bit signed sample. This makes it
            * maximally loud.
            *********************************************/
            short sample = (short)(Short.MAX_VALUE*Math.sin(theta));

            /****************************************
            * pull the bytes out of the sample and
            * put them into the buffer
            ****************************************/
            buffer[i]   = (byte)(sample>>8);
            buffer[i+1] = (byte)(sample&0xff);

        } // end of loop over samples

        /************************************
        * send the buffer to the sound card *
        ************************************/
        line.write(buffer, 0, available);


    } // end of loop over buffers

    /***************************************************************
    * this application runs forever, but a real app should have a
    * way of stopping and should stop and close the line
    ***************************************************************/

} // end of playSineWave method


/**************************************************************************
* Prints the different audio file formats supported by your system.
* By default Java only supports a few uncompressed formats, the most
* common of which is .wav files. You can find sample wav files on the
* internet, or you can google up a way to convert your .mp3 or other
* format files. Java has a "service provider interface" that lets you
* add support for other formats. Google "java sound mp3 spi"
* for some possible options.
**************************************************************************/
public static void listSupportedFileFormats() {

    AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();

    for(AudioFileFormat.Type type : types) {
        System.out.println(type);
    }

} // end of listSupportedFileFormats method

/**************************************************************************
* Reads data from an audio file and plays it to your sound card.
**************************************************************************/
public static void readAudioFile() throws Exception {

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
                                  new FileInputStream("chewy_roar.wav")));

    /************************************************************
    * print the format of the audio - like the sampe rate, etc. *
    ************************************************************/
    System.out.println(in.getFormat());

    /**********************************************************
    * get a data line and for playing sound on the sound card *
    **********************************************************/
    SourceDataLine line = getOutput(0);

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

/**************************************************************************
*
**************************************************************************/
public static void main(String[] args) throws Exception {

    listAudioDevices();
    //readFromMicrophone();
    //playSineWave();
    //listInputsAndOutputs();

    //readAudioFile();

} // end of main method

} // end of Main class
