import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.io.FileInputStream;
import sun.audio.*;
import java.io.*;

public class Game {

  ArrayList<Card> dealerHand; //this is the arraylist for the dealer's hand.
  ArrayList<Card> playerHand; //this is the arraylist for the player's hand.

  public boolean faceDown; //this boolean value will tell the program if we have the card facedown or faceup.
  public boolean dealerWon; //this boolean value will tell the program if dealer won the round.
  public volatile boolean roundOver; //this boolean value will tell the program if the round is over.
  //added keyword volatile because: "To ensure that changes made by one thread are visible to other
  //threads you must always add some synchronization between the threads.
  //The simplest way to do this is to make the shared variable volatile.
  //https://stackoverflow.com/questions/25425130/loop-doesnt-see-changed-value-without-a-print-statement


  JFrame frame; //we create a JFrame.
  Deck deck; //we have our deck.
  GameComponent atmosphereComponent; //Two GameComponents: one for the background images, buttons, and the overall atmosphere.
  GameComponent cardComponent; //Other for the cards printing out.

  JButton btnHit; //we have three buttons in this game.
  JButton btnStand;
  JButton btnDouble;
  JButton btnExit;

  public Game(JFrame f) {//with this constructor, we initialize the instant fields accordingly. This constructor gets a JFrame as a parameter.
    deck = new Deck();
    deck.shuffleDeck(); //we randomize the deck.
    dealerHand = new ArrayList<Card>();
    playerHand = new ArrayList<Card>();
    atmosphereComponent = new GameComponent(dealerHand, playerHand);
    frame = f;
    faceDown = true;
    dealerWon = true;
    roundOver = false;
  }

  public void formGame() {//this method will help us create the background of our game.

    System.out.println("GAME FORMED");
    frame.setTitle("BLACKJACK!"); //we make the title of the JFrame 'BLACKJACK!'
    frame.setSize(1130, 665);
    frame.setLocationRelativeTo(null); //this centers the JFrame on the screen.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false); //we make it impossible for the computer user to resize the Frame.

    btnHit = new JButton("HIT"); //In the following code snippet, we basically initialize our buttons and design them in the way we want them to be.
    btnHit.setBounds(390, 550, 100, 50); //We set their bounds.
    btnHit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));  //We set their font.
    btnStand = new JButton("STAND");
    btnStand.setBounds(520, 550, 100, 50);
    btnStand.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
    btnDouble = new JButton("DOUBLE");
    btnDouble.setBounds(650, 550, 100, 50);
    btnDouble.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
    btnExit = new JButton("EXIT CASINO");
    btnExit.setBounds(930, 240, 190, 50);
    btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

    frame.add(btnHit); //we add the buttons the JFrame
    frame.add(btnStand);
    frame.add(btnDouble);
    frame.add(btnExit);

    btnExit.addActionListener(new ActionListener() { //we add a action listener to the exit button. 
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, "You have left the casino with " +  Tester.currentBalance + "."); //we print out the message by getting our current balance from the Tester class.
        System.exit(0); //we exit the program.
      }
    });

    atmosphereComponent = new GameComponent(dealerHand, playerHand); //we initialize the GameComponent that will be the overall atmosphere of our game.
    atmosphereComponent.setBounds(0, 0, 1130, 665);  //we set the bounds of the component.
    frame.add(atmosphereComponent); //we add the component to the frame.
    frame.setVisible(true); //we make the frame visible.
  }

  public void startGame() { //this method starts the game: the cards are drawn and are printed out.

    for(int i = 0; i<2; i++) { //we add the first two cards on top of the deck to dealer's hand.
      dealerHand.add(deck.getCard(i));
    }
    for(int i = 2; i<4; i++) { //we add the third and fourth card on top of the deck to the player's hand.
      playerHand.add(deck.getCard(i));
    }
    for (int i = 0; i < 4; i++) { //we then remove these cards from the game. This way, we literally 'drew' the cards and those four cards are no longer in the deck.
      deck.removeCard(0);
    }

    cardComponent = new GameComponent(dealerHand, playerHand); //we initialize our component which accepts two card arraylists.
    cardComponent.setBounds(0, 0, 1130, 665); //we set the bounds of our component.
    frame.add(cardComponent); //we add the component to the grame.
    frame.setVisible(true); //we make the frame visible.

    checkHand(dealerHand); //at the beginning of the game, we first check the hand of the dealer and the hand of the player to look for a blackjack. (ther can't be a bust)
    checkHand(playerHand);

    btnHit.addActionListener(new ActionListener() { //we add a action listener to the hit button. When the user clicks this button,
      public void actionPerformed(ActionEvent e) {
        playCardDraw();
        addCard(playerHand); //we will first add a card to player's hand.
        checkHand(playerHand); //then we check the player's hand because it could be round over.
        if (getSumOfHand(playerHand)<17 && getSumOfHand(dealerHand)<17){ //if the round is not over, and if the total value of dealer's hand is smaller than 17, we add a card to dealer's hand.                                                 
          addCard(dealerHand);
          checkHand(dealerHand); //as usual, we check his hand for any potential round over situation.
        }
      }
    });

    btnDouble.addActionListener(new ActionListener() {//When user clicks this button, we add two cards to the player's hand. We then do the same things we do above.
      public void actionPerformed(ActionEvent e) {
        playCardDraw(); //we play the card drawing music here.
        addCard(playerHand); //we add a card to the player hand.
        addCard(playerHand); //we add the second card to the player hand.
        checkHand(playerHand); //we check the player's hand.
        if (getSumOfHand(playerHand)<17 && getSumOfHand(dealerHand)<17){ ///if the round is not over, and if the total value of dealer's hand is smaller than 17 and the total value of player's hand is smaller than 17, we add a card to dealer's hand.                                                  
          addCard(dealerHand);
          checkHand(dealerHand); //we check his hand as usual.
        }
      }
    });

    btnStand.addActionListener(new ActionListener() {//When user clicks this button, the player stands.
      public void actionPerformed(ActionEvent e) {
        while (getSumOfHand(dealerHand)<17) { //if it is appropraite, the dealer draws a card.                                                 
          addCard(dealerHand);
          checkHand(dealerHand);
        }
        if ((getSumOfHand(dealerHand)<21) && getSumOfHand(playerHand)<21) {//if both hands didn't reach 21, we check which hand is better and print out the result.
          if(getSumOfHand(playerHand) > getSumOfHand(dealerHand)) {
            faceDown = false;
            dealerWon = false;
            JOptionPane.showMessageDialog(frame, "PLAYER HAS WON BECAUSE OF A BETTER HAND!");
            rest();
            roundOver = true;
          }
          else {
            faceDown = false;
            JOptionPane.showMessageDialog(frame, "DEALER HAS WON BECAUSE OF A BETTER HAND!");
            rest();
            roundOver = true;
          }
        }
      }
    });
  }

  public void checkHand (ArrayList<Card> hand) {//this method literally checks the hand for a blackjack or bust.
    if (hand.equals(playerHand)) { //checks if the parameter is player's hand.
      if(getSumOfHand(hand) == 21){ //if it is 21, player has done blackjack and the game is over.
        faceDown = false;
        dealerWon = false; //we set it to false because user won.
        JOptionPane.showMessageDialog(frame, "PLAYER HAS DONE BLACKJACK! PLAYER HAS WON!"); //we print out the result ot JOptionPane.
        rest();
        roundOver = true;
      }
      else if (getSumOfHand(hand) > 21) { //if it is bigger than 21, then the player hand has busted, dealer has won.
        faceDown = false; JOptionPane.showMessageDialog(frame, "PLAYER HAS BUSTED! DEALER HAS WON!");
        rest();
        roundOver = true;
      }
    }
    else { //else condition checks if it is dealer's hand.
      if(getSumOfHand(hand) == 21) { //we basically look for the same things we looked for the player's hand.
        faceDown = false;
        JOptionPane.showMessageDialog(frame, "DEALER HAS DONE BLACKJACK! DEALER HAS WON!");
        rest();
        roundOver = true;
      }
      else if (getSumOfHand(hand) > 21) {
        faceDown = false;
        dealerWon = false;
        JOptionPane.showMessageDialog(frame, "DEALER HAS JUST BUSTED! PLAYER HAS WON!");
        rest();
        roundOver = true;
      }
    }
  }

  public void addCard(ArrayList<Card> hand) {//this method adds a card to the hand.
    hand.add(deck.getCard(0)); //gets a card from the deck to the hand.
    deck.removeCard(0); //removes the card from the deck.
    faceDown = true;
  }

  public boolean hasAceInHand(ArrayList<Card> hand) {//this method checks if the hand has ace.
    for (int i = 0; i < hand.size(); i++){ //we go through the hand that is given as a parameter and check for a card with a value of 11(Ace.)
      if(hand.get(i).getValue() == 11) {
        return true; //we return true if there is any.
      }
    }
    return false; //we return false if not.
  }

  public int aceCountInHand(ArrayList<Card> hand){//this method finds the total aces found in the hand. This is important for us to decide whether we will take ace's value as 1 or 11.
    int aceCount = 0; //we initialize an integer which will store the total ace count as 0.
    for (int i = 0; i < hand.size(); i++) { //we go through the hand.
      if(hand.get(i).getValue() == 11) { //each time we see a card with a value of 11,
        aceCount++; //we add one to the ace count.
      }
    }
    return aceCount; //we then return this ace count.   
  }

  public int getSumWithHighAce(ArrayList<Card> hand) {//this method gives the total value of the hand where the ace is counted as having a value of 11.
    int handSum = 0; //we initialize the integer in which the sum of hand is stored.
    for (int i = 0; i < hand.size(); i++){ //we go through the hand,
      handSum = handSum + hand.get(i).getValue(); //we add the values we encounter to the integer.
    }
    return handSum; //we return the integer.
  }

  public int getSumOfHand (ArrayList<Card> hand) {//this method gives you the sum of the hand, for all cases.
    if(hasAceInHand(hand)) { //if there is an ace,
      if(getSumWithHighAce(hand) <= 21) {
        return getSumWithHighAce(hand); //we get the sum with the high ace case (taking aces as 11) if the total sum is smaller than 24 and return this sum.
      }
      else{
        for (int i = 0; i < aceCountInHand(hand); i++) { //if we take aces as 11 and the total value exceeds 21, we then do some calculation to count ace as 1.
          int sumOfHand = getSumWithHighAce(hand)-(i+1)*10; //for each ace there is in hand, we subtract then to get 1 from 11.
          if(sumOfHand <= 21) {
            return sumOfHand; //we return this sum if it is smaller than 21.
          }
        }
      }
    }
    else { //if there is no ace in hand, we will directly go through the hand and sum up all of the values.
      int sumOfHand = 0;
      for (int i = 0; i < hand.size(); i++) {
        sumOfHand = sumOfHand + hand.get(i).getValue();
      }
      return sumOfHand;
    }
    return 22; //we basically set it to the 'bust' case if the method returns nothing up to this point.
  }

  public static void rest() {//this method sleeps the program. It basically serves as a time duration between events.
    try {
      Thread.sleep(500);//this sleeps the program for 1000 miliseconds which is equal to 1 second.
    }
    catch (InterruptedException e) {}
  }

  public static void playCardDraw() { //this is a static method that plays a wav file when it is called.

    try{
      InputStream in = new FileInputStream("sounds/cardDraw.wav"); //we first read the wav file.
      AudioStream audio = new AudioStream(in); //then store it as an audio stream.

      AudioPlayer.player.start(audio); //then, we basically 'play' this sound through AudioPlayer.
    }
    catch(IOException e) {}
  }
}