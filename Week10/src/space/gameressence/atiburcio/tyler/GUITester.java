package space.gameressence.atiburcio.tyler;

//External Imports
import space.gameressence.atiburcio.tyler.util.GUITemplate;

//Internal imports
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;


/**
 * Tester class to test the revisions to the GUI Template class
 * @see space.gameressence.atiburcio.tyler.util.GUITemplate
 * @author Tyler Atiburcio
 * @version 1
 */
public class GUITester extends GUITemplate
{
  //Test menu item
  JMenuItem test = new JMenuItem("Test");

  //Text Area
  JTextArea text;
  JScrollPane scrollPane;

  public static void main(String[] args)
  {
    new GUITester();
  }

  public GUITester()
  {
    super("testing");
    //Add a menu item item before the exit
    super.getMenuBar().getMenu(0).insert(this.test,0);
  }

  public void makeContainers()
  {
    this.text = new JTextArea("Hello World!");
    this.scrollPane = new JScrollPane(this.text);
    super.addCompoment(this.scrollPane);
  }

}
