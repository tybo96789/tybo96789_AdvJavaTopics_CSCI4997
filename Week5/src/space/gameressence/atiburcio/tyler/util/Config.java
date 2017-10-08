package space.gameressence.atiburcio.tyler.util;

//File/Util imports
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;

//Java swing imports
import java.awt.*;
import javax.swing.*;

/**
 * Config class contains methods to facilitate the creation and loading of java properties files
 * @author Tyler Atiburcio
 * @version 1
 */
public class Config
{
  //Propeties filepath/Name
  private String fileName;

  //Java properties variables
  private ArrayList<String> keys;
  private ArrayList<String> values;

  /**
   * Creates an empty java properties with the given name of "config.properties"
   * @see #Config(String)
   */
  public Config()
  {
    this("config.properties");
  }

  /**
   * Creates an empty java properties file with argument name
   * @param name The file name of the config file
   */
  public Config(String name)
  {
    this.fileName = name;
    this.keys = new ArrayList<String>();
    this.values = new ArrayList<String>();
  }

  /**
   * Uses a recursive call to find the associated value to the key
   * @param key The key to lookup
   * @return String; The value assoicated to the key
   * @see #getValue(int)
   */
  public String getValue(String key)
  {
    return this.getValue(this.findKey(key));
  }

  /**
   * @param keyLoc The index of the key in the keys ArrayList
   * @return String; The associated value to the key
   */
  public String getValue(int keyLoc)
  {
    return this.keys.get(keyLoc);
  }

  /**
   * Returns the index of the key in the arrayList of keys; Returns '-1' if not found
   * @param key The key to find in the arraylist of Keys
   * @return int; The index of the key; Returns '-1' if not found
   */
  public int findKey(String key)
  {
    for(int i = 0; i < this.keys.size(); i++) if(this.keys.get(i).equalsIgnoreCase(key)) return i;
    return -1;
  }

  /**
   * @return The arraylist of keys
   */
  public ArrayList<String> getKeys()
  {
    return this.keys;
  }

  /**
   * @return The arraylist of values
   */
  public ArrayList<String> getValues()
  {
    return this.values;
  }

  /**
   * Checks to see if key already exits in the properties (map)
   * @param key The key to Check
   * @return boolean; If key already exists in properties (map)
   */
  public boolean keyExists(String key)
  {
    return this.findKey(key) != -1 ? true: false;
  }

  /**
   * add given key, value pair to the properties map, but checks to see if key already exists
   * @param key The key
   * @param val The value of the key
   * @return boolean; If pair was added successfuly
   */
  public boolean add(String key, String val)
  {
    if(this.keyExists(key)) return false;
    else
    {
        this.keys.add(key);
        this.values.add(val);
        return true;
    }

  }

  /**
   * Saves the key, value pairs (properties) to a file
   * @return boolean; if operation is successful
   */
  public boolean save()
  {
    Properties prop =  new Properties();
    OutputStream out = null;
    try
    {
      out = new FileOutputStream(this.fileName);
      System.out.println(this.keys.size() + " "+ this.values.size());
      for(int i = 0; i < this.keys.size(); i++)
      {
        prop.setProperty(this.keys.get(i),this.values.get(i));
      }

      prop.store(out,null);
      out.flush();
      out.close();
    }catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Imports a config file and makes a new instance of java properties file.
   * Allows to save or modify or access properties
   * @param path The file path of the config file to load
   * @return Config File instance
   */
  public static Config Import(String path)
  {
    //Return if path is empty or null
    if(path == null || path.isEmpty()) return null;
    //Begin by making properties instance
    Properties prop = new Properties();
    //Ready the input streams
    InputStream in = null;
    //declare config instance to load properties from file
    Config loadedConfig = null;
    try
    {
      //Open and load the properties file
      loadedConfig = new Config(new File(path).getName());
      in = new FileInputStream(path);
      prop.load(in);

      //Make a enumeration of based off of the properties name in the file
      Enumeration<?> enums = prop.propertyNames();
      //Loop through the properties
      while(enums.hasMoreElements())
      {
        //Add key,values pairs to arrays
        String key = (String) enums.nextElement();
			  String value = prop.getProperty(key);
        loadedConfig.add(key,value);
      }
      //Close the input stream
      in.close();

    }catch(Exception e)
    {
      System.err.println("Error loading config file!");
      e.printStackTrace();
    }
    return loadedConfig;
  }

  /**
   * Overloaded version of import, used to ask user what config file to load then
   * calls import(String path) to import the properties
   * @return Config instance
   * @see #Import(String path)
   */
  public static Config Import()
  {
    JFileChooser fileChooser = new JFileChooser();
    try
    {
      //Sets the default path to where the program is being called
      File currDir = new File(new File("./config.properties").getCanonicalPath());
      fileChooser.setCurrentDirectory(currDir);
      fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Config File (.properties)","properties"));
      fileChooser.showOpenDialog(null);

    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.err.println("Error importing config file!");
    }
    finally
    {
      return Import(fileChooser.getSelectedFile().getPath());
    }

  }

}
