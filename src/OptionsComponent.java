import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import sun.audio.*;

public class OptionsComponent extends JComponent implements ActionListener{

  private JButton btnPlay = new JButton("PLAY"); //we initialize 4 buttons for the menu component, which will open up first in our game.
  private JButton btnExit = new JButton("EXIT");
  private JButton btnHelp = new JButton("HELP");
  private JButton btnInfo = new JButton("INFO");
  private static BufferedImage backgroundImage; //we initialize an image which will serve as the background image.

  public OptionsComponent() { //in the default constructor of this class,
    btnPlay.addActionListener(this); //we add action listeners to all buttons to control what will happen when the user clicks them.
    btnExit.addActionListener(this);
    btnHelp.addActionListener(this);
    btnInfo.addActionListener(this);
  }

  public void paintComponent(Graphics g) {//we will decorate the component with this method.

    Graphics2D g2 = (Graphics2D) g; //we first cast Graphics g to Graphics2D g2 in order to use a more powerful brush.

    try {
      backgroundImage = ImageIO.read(new File("images/background.png")); //we read a file which is the png image of a poker table for our background image.
    }
    catch(IOException e) {}

    g2.drawImage(backgroundImage, 0, 0, null); //we draw the background image to the component.

    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 100)); //In these codes, we will add the title of our game and its font and color.
    g2.setColor(Color.WHITE);
    g2.drawString("Welcome", 380, 100);
    g2.drawString("to", 530, 180);
    g2.drawString("BLACKJACK!", 290, 260);

    g2.setFont(new Font("Arial", Font.BOLD, 30));
    g2.drawString("This game is brought to you by Ongun Uzay Macar", 220, 580); //Here, we add a small description to the component.

    btnPlay.setBounds(500, 300, 150, 80); //we set the bounds of the buttons.
    btnExit.setBounds(500, 400, 150, 80);
    btnHelp.setBounds(80, 75, 150, 80);
    btnInfo.setBounds(900, 75, 150, 80);

    btnPlay.setFont(new Font("Comic Sans MS", Font.BOLD, 40)); //we set the fonts of writings on the buttons.
    btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
    btnHelp.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
    btnInfo.setFont(new Font("Comic Sans MS", Font.BOLD, 40));

    super.add(btnPlay); //super refers to the JComponent. Thus, with these codes, we add the four buttons to the component.
    super.add(btnExit);
    super.add(btnHelp);
    super.add(btnInfo);
  }

  public void actionPerformed(ActionEvent e) {//we will control the button actions here.
    JButton selectedButton = (JButton)e.getSource();//we will assign the clicked button which created an action event to the JButton selectedButton. It is the selected button.

    if(selectedButton == btnExit) { //if the selected button is the exit button (btnExit), we will exit the game.
      System.exit(0); //this line is to exit the program.
    }
    else if(selectedButton == btnPlay) {//if the selected button is the play button (btnPlay), we start the game.
      Tester.currentState = Tester.STATE.GAME; //we equalize the current state to STATE.GAME where STATE was the enum declared in the Tester class. Because we will no longer be in the menu, we will jump to the game.
      Tester.menuFrame.dispose(); //we first get rid of the menu frame and the components it has.
      Tester.gameRefreshThread.start(); //then, simultaneously, we start our two threads and these run at the same time.
      Tester.gameCheckThread.start();
      //playAmbienceMusic(); //we play the ambience music which gives a sweet background casino noise.
    }
    else if(selectedButton == btnHelp) {//if the selected button is the help button (btnHelp), we open up a J Option Pane that will contain information about the game.
      JOptionPane.showMessageDialog(this, "The goal of blackjack is to beat the dealer's hand without going over 21." +
                      "\nFace cards are worth 10. Aces are worth 1 or 11, whichever makes a better hand." +
                      "\nEach player starts with two cards, one of the dealer's cards is hidden until the end." +
                      "\nTo 'Hit' is to ask for another card. To 'Stand' is to hold your total and end your turn." +
                      "\nIf you go over 21 you bust, and the dealer wins regardless of the dealer's hand." +
                      "\nIf you go over 21 you bust, and the dealer wins regardless of the dealer's hand." +
                      "\nIf you are dealt 21 from the start (Ace & 10), you got a blackjack.", "QUICK&EASY BLACKJACK HELP",
              JOptionPane.INFORMATION_MESSAGE);
    }
    else if(selectedButton == btnInfo) {//if the selected button is the info button (btnInfo), we open up a J Option Pane that will contain information about the program.
      JOptionPane.showMessageDialog(this, "This project was done by Ongun Uzay Macar as a final project for" +
              "\nAdvanced Programming in January 2016 in the guidance of Cengiz Agalar.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public static void playAmbienceMusic() {//this is a static method that plays a wav file when it is called.

    try{
      InputStream in = new FileInputStream("sounds/casinoAmbience.wav"); //we first read the wav file.
      AudioStream audio = new AudioStream(in); //then store it as an audio stream.

      AudioPlayer.player.start(audio); //then, we basically 'play' this sound through AudioPlayer.
    }
    catch(IOException e) {}
  }
}