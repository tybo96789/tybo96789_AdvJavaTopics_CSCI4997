package space.gameressence.atiburcio.tyler.util;

import java.io.*;

/**
 * Interface implenmts Serializable which overrides the default write and read object methods
 * @author Tyler ATiburcio
 * @version 1
 */
public abstract interface SerializationRW extends Serializable
{
  public abstract void writeObject(ObjectOutputStream out) throws IOException;
  public abstract void readObject(ObjectInputStream in) throws IOException;
}
