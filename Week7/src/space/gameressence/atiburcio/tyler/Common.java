package space.gameressence.atiburcio.tyler;

//Internal Java imports
import java.rmi.*;

/**
 * Common shared interface used to follow the RMI concept
 * @author Tyler Atiburcio
 * @version 1
 */
public interface Common extends Remote
{
  /**
   * @param s The string to compute
   * @return String; Server will compute SHA-256 hash of the given string (s)
   *
   */
  public String compute(String s) throws RemoteException;
}
