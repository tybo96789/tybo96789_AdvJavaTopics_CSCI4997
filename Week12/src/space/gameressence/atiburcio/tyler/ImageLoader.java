package space.gameressence.atiburcio.tyler;

//External Imports
import space.gameressence.atiburcio.tyler.util.GUITemplate;

//Internal imports SWING
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.*;

//Interal Imports
import java.net.URL;

/**
 * Program thats loads an image from URL
 * @author Tyler ATiburcio
 * @version 1
 * @see space.gameressence.atiburcio.tyler.GUITemplate
 */
public class ImageLoader extends GUITemplate
{
  //Default URL to load if no arguments are passed before runtime
  public static final String DEFAULTFILE = "http://gameressence.space/WebsiteIcon.png";
  public static void main(String args[])
  {
    try
    {
        //Pharse the first arugment for the URL to load
        if(args[0] != null)
          {
            System.out.println(args[0]);
            if(!args[0].equals(""))
              new ImageLoader(args[0]);
          }

    }catch(Exception e)
    {
      new ImageLoader(DEFAULTFILE);
      e.printStackTrace();
    }

  }
  //Used to reference variables in class
  protected final ImageLoader INSTANCE = this;

  //GUI variables
  private JScrollPane scrollPane;
  private JPanel mainContent;
  private JLabel image;
  private JPanel inputPanel;
  private JButton query;
  private JTextField textField;

  //Class variables
  private String link;

  /**
   * Loads the image based from the URL
   * @param link String; URL of the photo to load
   */
  public ImageLoader(String link)
  {
    super("Image Loader");
    this.link = link.trim();
    downloadPhoto();
  }

  /**
   * This method pharses the text field for the URL and loads the image to JPanel
   */
  protected void downloadPhoto()
  {

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          // Here, we can safely update the GUI
          // because we'll be called from the
          // event dispatch thread
          try
          {
            INSTANCE.setStatus("Downloading!");
            textField.setText(link);
            image = new JLabel(new ImageIcon(ImageIO.read(new URL(textField.getText().trim()))));
            mainContent.add(image);
            mainContent.validate();
            mainContent.repaint();
            INSTANCE.setStatus("Ready!");
            link = textField.getText().trim();
          }
          catch(Exception e)
          {
            System.err.println("Error loading picture!");
            e.printStackTrace();
            setStatus("Error loading picture!");
            return;
          }
          }
      });

  }

  /**
   * Inherited method from GUITemplate
   */
  public void makeContainers()
  {
    //Panel creations
    //Content Panel
    this.mainContent = new JPanel();
    this.mainContent.setLayout(new BorderLayout());
    this.scrollPane = new JScrollPane(this.mainContent);
    //Input panel
    this.inputPanel = new JPanel();
    this.inputPanel.setLayout(new BorderLayout());
    this.query = new JButton("Querry");
    this.query.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          INSTANCE.downloadPhoto();
        }
      });
    this.textField = new JTextField();
    this.inputPanel.add(this.textField);
    this.inputPanel.add(this.query,BorderLayout.EAST);

    //Add main panels to Parent JPanel
    super.addCompoment(this.inputPanel, BorderLayout.NORTH);
    super.addCompoment(this.scrollPane);

  }
}
