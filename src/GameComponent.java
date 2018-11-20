import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import sun.audio.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameComponent extends JComponent implements MouseListener {//this class will implement the MouseListener because we will check if the user clicked a certain coordinate on the component.
  public BufferedImage backgroundImage; //we name three images: one for the background image, one for the blackjack logo that will be located in the center, and for the chip.
  public BufferedImage logo;
  public BufferedImage chip;
  private ArrayList<Card> dealerHand; //as usual, we have to card arraylists which will serve as hands: one for the dealer and for the player.
  private ArrayList<Card> playerHand;
  private int dealerScore; //we have two integers: one for dealer's score and the other for player's score.
  private int playerScore;
  public boolean faceDown = true; //this boolean value will tell the program if we have the card facedown or faceup.
  public static boolean betMade = false; //this boolean will tell the program if the player entered the bet.
  private int currentBalance; //this integer will store the value for the current Balance of the player.
  public static int currentBet; //this integer will store the value for the current bet.

  public GameComponent(ArrayList<Card> dH, ArrayList<Card> pH) { //this will be the constructor for this class which will accept two hands as a parameter.
    dealerHand = dH; //we initalize the instant fields.
    playerHand = pH;
    dealerScore = 0; //the scores start as 0.
    playerScore = 0;
    currentBalance = 1000; //the balance starts as 1000.
    addMouseListener(this);//we add MouseListener to the component.
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g; //we first cast Graphics g to Graphics2D g2 in order to use a more powerful brush.

    try {
      backgroundImage = ImageIO.read(new File("images/background.png")); //we read a file which is the png image of a poker table for our background image.
      logo = ImageIO.read(new File("images/blackjackLogo.png")); //we read a file which is the png image of a blackjack logo for the logo on the poker table.
      chip = ImageIO.read(new File("images/chip.png")); //we read a file which is the png image of a poker chip for the chip on the poker table.
    }
    catch(IOException e) {}

    g2.drawImage(backgroundImage, 0, 0, null); //we draw these images to the component.
    g2.drawImage(logo, 510, 200, null);
    g2.drawImage(chip, 50, 300, null);
    g2.setColor(Color.WHITE); //we set the colors.
    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30)); //we change their fonts.
    g2.drawString("DEALER", 515, 50); //we draw these strings which visualize the game.
    g2.drawString("PLAYER", 515, 380);
    g2.drawString("DEALER WON: ", 50, 100);
    g2.drawString(Integer.toString(dealerScore), 300, 100); //we draw the dealer's score accordingly.
    g2.drawString("PLAYER WON: ", 50, 150);
    g2.drawString(Integer.toString(playerScore), 300, 150); //we draw the player's score accordingly.
    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15)); //we set the font again.
    g2.drawString("To start each round, you have to first", 50, 250);
    g2.drawString("bet an amount by clicking the chip below.", 50, 270);
    g2.drawString("The best gaming experience is ", 830, 550);
    g2.drawString("when you play with sound on!", 830, 570);
    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
    g2.drawString("CURRENT BALANCE: " + currentBalance, 50, 570); //we write the current balance on the component.

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    g2.drawString(sdf.format(cal.getTime()), 1020, 20); //we write the time in hh:mm:ss to the screen to inform the user.

    try { //we need to have the try and catch blocks here because the method printCard of a Card object draws images chopped off a spritesheet image from a 2D-array.
      for (int i = 0; i < dealerHand.size(); i++) {//we go through dealer's hand
        if (i == 0) { //if it is the first card,
          if(faceDown) { //we check if will be facedown or not.
            dealerHand.get(i).printCard(g2, true, true, i); //we then draw each individual card.
          }
          else {
            dealerHand.get(i).printCard(g2, true, false, i); //if it is not face down, we write the 3rd parameter as false and then draw each individual card in the hand again.
          }
        }
        else {
          dealerHand.get(i).printCard(g2, true, false, i); //if it is not the first card, we write the 3rd parameter as false and then draw each individual card in the hand again.
        }
      }
    }
    catch (IOException e) {}

    try {
      for (int i = 0; i < playerHand.size(); i++) { //we do the same thing for the user hand with a foor loop again: we go through each of the cards in user's hand.
        playerHand.get(i).printCard(g2, false, false, i); //we then draw each of the card on the component(screen). Extra information about parameters can be found in the Card class.
      }
    }
    catch (IOException e) {}
  }

  public void refresh(int cB, int uS, int dS, boolean fD) { //this refresh method will refresh the GameComponent when it is called.
    currentBalance = cB;
    playerScore = uS;
    dealerScore = dS;
    faceDown = fD;
    this.repaint();
  }

  public void mousePressed(MouseEvent e) {//in this void method, we will control mouse events of the user.
    int mouseX = e.getX(); //we first get the x and y coordinates of the user's mouse's current position.
    int mouseY = e.getY();

    if(mouseX>= 50 && mouseX<=200 && mouseY>=300 && mouseY<=450) {//we will only do something if the x and y coordinates fall on top of the chip image. The coordinates you see below give the end points of the chip image.
      betMade = true; //if the user clicks on the chip image, then the bet is made.
      String[] options = new String[] {"1", "5", "10", "25", "100"}; //we declare an array of options that will be given when the user clicks the chip and and JOptionPane dialog comes up.
      int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount!", "BETTING", //This is the dialog that will popup when user clicks the chip.
              JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
      if(response == 0) {//if the user selects the first response, he selected the bet as 1.
        currentBet = 1; //we assign 1 to the current bet.
        currentBalance -= 1; //we decrement the current balance by the current bet.
      }
      else if(response == 1) {//the same thing goes on like this. The second respoonse is 5 so the bet is 5 and it is decreased from the current balance.
        currentBet = 5;
        currentBalance -= 5;
      }
      else if(response == 2) {
        currentBet = 10;
        currentBalance -= 10;
      }
      else if(response == 3) {
        currentBet = 25;
        currentBalance -= 25;
      }
      else if(response == 4) {
        currentBet = 100;
        currentBalance -= 100;
      }
      else { //if none of the options are selected, and if the user closes the option dialog without selecting, then we assign the bet as 1 because this is our default value.
        currentBet = 1;
        currentBalance -= 1;
        System.out.println("You haven't selected a proper bet. Thus, the bet is taken as 1."); //we inform the reader from console that we have selected bet as 1 because it is the default bet.
      }

      System.out.println("You have made your bet: " + currentBet + "." + " If you beat the dealer, you will increase your current balance by " + currentBet*2 +
              "; if the dealer beats you you will decrease your current balance by " + currentBet + "."); //each time the player bets, we inform the consequences from the console.
      playChipsSettle(); //we then play this music which gives you the sound of poker chips settling on a poker table.
      Tester.newGame.startGame(); //then we start the game.
    }

  }
  public void mouseExited(MouseEvent e) {//these methods will not be used in this project but it is necessary to write them.

  }
  public void mouseEntered(MouseEvent e) {

  }
  public void mouseReleased(MouseEvent e) {

  }
  public void mouseClicked(MouseEvent e) {

  }

  public static void playChipsSettle() {//this is a static method that plays a wav file when it is called.

    try{
      InputStream in = new FileInputStream("sounds/chipsSettle.wav"); //we first read the wav file.
      AudioStream audio = new AudioStream(in); //then store it as an audio stream.

      AudioPlayer.player.start(audio); //then, we basically 'play' this sound through AudioPlayer.
    }
    catch(IOException e) {}
  }
}