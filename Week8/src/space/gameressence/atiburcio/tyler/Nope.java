package space.gameressence.atiburcio.tyler;

//Java internal imports
import java.io.*;
import java.util.*;

/**
 * Reads in a file in the project, loads the data into a buffer but the buffer has random extra padding
 * @author Tyler Atiburcio
 * @version 1
 */
public class Nope
{
	//Class variables
	private File file;
	private FileInputStream fileIN;
	private byte[] buffer;

	//Used for Java vm stats
	public static int destoryed = 0;
	public static double mbFreed = 0;
	public static double mbUsed = 0;

	//Used to pick a random amount of buffer padding
	Random rand = new Random();

	//Used to define the minmum about the time the program should
	private final static int SLEEPBASE = 1000;

	//Convertion from byte to MegaByte
	private final static double BYTETOMEGABYTE = 0.000001;

	/**
	 * @param path File path of where the file to be loaded is located
	 */
	public Nope(String path)
	{
		try
		{
			this.file = new File(path);
			this.fileIN = new FileInputStream(this.file);
			//Add a random extra amount of padding to the buffer
			this.buffer = new byte[(int) (this.file.length() * Math.abs(1+ rand.nextDouble()))];
			//Read in the file and store into the buffers
			this.fileIN.read(this.buffer);
			this.fileIN.close();
			mbUsed += BYTETOMEGABYTE * this.getLengthOfBuffer();
			//Changes the rate of execution of the program
			if(!Main.haste)Thread.sleep(SLEEPBASE * (long) (1 + rand.nextDouble()));
		}catch(Exception e)
		{
			System.err.println("Error reading file!");
			e.printStackTrace();
		}

	}

	/**
	 * @return  byte[] The buffer with data from the file, including the padding
	 */
	public byte[] getData()
	{
		return this.buffer;
	}

	/**
	 * @return Int; the lenght of the buffer
	 */
	public int getLengthOfBuffer()
	{
		return this.buffer.length;
	}

	/**
	 * Overrided method from the object class.
	 * Whats new: shows and calculate the number of bytes the program has used loading the specifed file
	 * 	and the amount of bytes freed;
	 */
	protected void finalize()
	{
		System.out.println("\n------------");
		System.out.println("Clearing: " + this.getLengthOfBuffer() + " amount of bytes!");
		mbFreed += BYTETOMEGABYTE * this.getLengthOfBuffer();
		destoryed++;
		System.out.println("Destoryed: " + destoryed +" instances;\n Total MB used for instances: "+ mbUsed + " Total MB freed: "+ mbFreed +" freed/consumed: "+ 100*(mbFreed/mbUsed));
		System.out.println("------------\n");

	}
}
