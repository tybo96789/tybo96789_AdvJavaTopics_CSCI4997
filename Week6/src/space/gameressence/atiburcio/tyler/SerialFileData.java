package space.gameressence.atiburcio.tyler;

//Internal Java imports
import java.io.Serializable;
import java.sql.Timestamp;

//External imports
import space.gameressence.atiburcio.tyler.util.*;

/**
 * Wrapper class that wraps another Serializable object Type
 * @author Tyler Atiburcio
 * @version 1
 */
public class SerialFileData<T> implements Serializable
{

  private final T file;
  private final Timestamp TIMESTAMP;

  /**
   * Takes in an another Seriazlizable object to wrap around. As well as a timestamp.
   * @param file The serializable object that the wrapper will hold
   */
  public SerialFileData(T file)
  {
    this.file = file;
    this.TIMESTAMP = new Timestamp(System.currentTimeMillis());
  }

  /**
   * @return The object that the wrapper is holding
   */
  public T getFile()
  {
    return this.file;
  }

  /**
   * @return The timestamp of the inital time that the wrapper has the object
   */
  public String getTimeStamp()
  {
    return this.TIMESTAMP.toString();
  }
}
