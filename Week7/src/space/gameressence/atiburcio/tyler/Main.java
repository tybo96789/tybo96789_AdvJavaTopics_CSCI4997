package space.gameressence.atiburcio.tyler;

/**
 * Runner class to bind to rmi registry and start the stub client
 * @author Tyler ATiburcio
 * @version 1
 */
public class Main
{
  public static void main(String args[])
  {
    System.out.println("Binding to registry");
    new Server();
    System.out.println("Starting!");
    new Stub();
  }
}
