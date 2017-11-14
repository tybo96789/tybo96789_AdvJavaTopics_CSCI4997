package space.gameressence.atiburcio.tyler;

//internal Java imports
import java.util.*;

/**
 * Runner class that makes new instances of the Nope class.
 * The runner also monitors memory usage and removes elements from array when memory usage is high
 * @see space.gameressence.atiburcio.tyler.Nope
 * @author Tyler Atiburcio
 * @version 1
 */
public class Main
{
	//Static variables- Used for Main
		//Holds a list of nopes
	static ArrayList<Main> nopeList = new ArrayList<Main>();
		//Counter of number of instances created and that are active in the array
	static int index = 0;
		//Grab current runtime to get stats of Java VM
	static Runtime run = Runtime.getRuntime();
		//Used to pick random instance in an array to destory
	static Random rand = new Random();
	public static boolean haste = false;

	//Class variables used to wrap the data and current index together
	final Nope data;
	final int objectID;

	/**
	 * Runner part of the program makes new instances of the nope object and monitors memory and destorys objects when memory usage is high
	 * @param args using keyword of "haste" or "fast" will remove the sleep between interations
	 */
	public static void main(String args[])
	{
		try
		{
			//specify if the user wants the program to run faster
			switch(args[0].trim().toLowerCase())
			{
				case("fast"):
				case("haste"):
				{
					haste = true;
					System.out.println("\n\n----------HASTE MODE ENABLED----------\n\n");
					break;
				}

			}
		}//If there is no arguments resume normal operation
		catch(Exception ex){}

		for(;;)
		{
			index++;
			//make a new instance of nope object and add it to the list of nopes
			nopeList.add(new Main(index,new Nope("./primes1.txt")));
			//Get info about the Nope data in terms of amount of data it is currently holding
			System.out.println("DataID: " + index + " : " + nopeList.get(nopeList.size()-1).getData().getLengthOfBuffer() +" bytes in buffer");


			//Calculate the amount of memory being used in the Java VM 1-(free/total)
			double memUsage = 1-(run.freeMemory()/ (double) run.totalMemory());
			memUsage *= 100;
			//Print out the stats of the Java vm
			System.out.print("Instances: "+ nopeList.size() +"; Memory Usage: " + (memUsage));
			System.out.println(" free="+run.freeMemory()+ " total="+run.totalMemory()+ " max="+run.maxMemory());
			//If memory usage is over 90% pick a random element to remove from the array to lower memory usage
			if(memUsage > 90)
			{
				//Pick a random element in the array
				int banHammer =  rand.nextInt(nopeList.size());
				//Let the user know that the object with the id of the index is being removed in the array
				System.out.println("Ban Hammer Called on: " + banHammer);
				//Remove item from array
				nopeList.remove(banHammer);
			}
		}
	}

	/**
	 * Wrapper class to hold the nope object and the index of the object
	 * @param objectID Index of the object
	 * @param data The data to hold
	 */
	public Main(int objectID, Nope data)
	{
		this.objectID = objectID;
		this.data = data;
	}

	/**
	 * @return Nope instance that the wrapper is holding
	 */
	public Nope getData()
	{
		return this.data;
	}

}
