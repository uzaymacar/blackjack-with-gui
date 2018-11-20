import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D; 

public class Card {  
  private int suit; //suit of the card (Clubs[0], Diamonds[1], Hearts[3], or Spades[4]) 
  private int rank; //rank of the card (Ace[0], 2[1], 3[2], 4[3], 5[4], 6[5], 7[6], 8[7], 9[8], 10[9], Jack[10], Queen[11], or King[12])
  private int value; //value of the card in blackjack (from 1 to 11)
  private int xPosition; //x position of the card image that you will draw to the screen
  private int yPosition; //y position of the card image that you will draw to the screen
  
  public Card() { //a default constructor of Card
    suit = 0;
    rank = 0; 
    value = 0; 
  }
  
  public Card(int s, int r, int v) { //a constructor of Card that initializes the main attributes
    suit = s;
    rank = r;
    value = v;
  }
  
  public int getSuit() { //this method returns you the suit of the card.
    return suit; 
  }
  public int getRank() { //this method returns you the rank of the card.
    return rank;
  }
  public int getValue() { //this method returns you the value of the card.
    return value; 
  }
  
  public void printCard(Graphics2D g2, boolean dealerTurn, boolean faceDown, int cardNumber) throws IOException {//this method draws the card accordingly (looking to the parameters). It throws a IOException because we will be reading some image files.
    //The first parameter is the g2[powerful brush] since we will draw some images. The second parameter tells the method if it is dealer's turn. The third method checks if the card drawn will be face down or face up. The fourth parameter tells the method which card on the line will be drawn so that each line could be drawn next to each other in a horizontal line.
    BufferedImage deckImg = ImageIO.read(new File("images/cardSpriteSheet.png")); //we read the sprite sheet image.
    int imgWidth = 950; //this is the width of the sprite sheet image in pixels.
    int imgHeight = 392; //this is the height of the sprite sheet image in pixels.
    
    BufferedImage[][] cardPictures = new BufferedImage[4][13]; //we create this two-dimensional array to store the individiual card pictures.
    BufferedImage backOfACard = ImageIO.read(new File("images/backsideOfACard.jpg")); //this image will be the back of a card.
    
    for (int c = 0; c < 4; c++) { 
      for (int r = 0; r < 13; r++) {
        cardPictures[c][r] = deckImg.getSubimage(r*imgWidth/13, c*imgHeight/4, imgWidth/13, imgHeight/4);  //we assign the relative card pictures to the 2-D array.
      }
    }
    
    if (dealerTurn) { //if it is dealer's turn, then the card will be printed to the top where the dealer's hand is located.
      yPosition = 75;
    }
    else { //if it is the player's turn, then the card will be printed to the below where the player's hand is located.
      yPosition = 400;
    }
    
    xPosition = 500 + 75*cardNumber; // we want the drawn cards to come side by side so we shift the x position of the cards accordingly.
    
    if (faceDown) { //this boolean parameter will tell the method if the printed card will be face down or face up.
      g2.drawImage(backOfACard, xPosition, yPosition, null ); //if it is facedown, we draw the image of a back of a card.
    }
    else {
      g2.drawImage(cardPictures[suit][rank], xPosition, yPosition, null); //if it is not face up, we draw the image from the 2-D array.
    }
  }
}