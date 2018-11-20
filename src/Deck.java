import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  
  private ArrayList<Card> deck; //this is the arraylist which we will store all of the 52 cards. We call it the deck.
  
  public Deck() {   
    deck = new ArrayList<Card>(); //we initialize this arraylist of cards.
    
    for (int i = 0; i < 4; i++) { //with a nested loop, we go through the 52 cards.
      for (int j = 0; j < 13; j++) {                                                  
        if (j == 0) { //if j is 0, this is an ace and we will give ace the value of 11 (look at the rules of Blackjack for further information) at the beginning.
          Card card = new Card(i, j, 11); //we create card with ith suit and jth rank which has the value of 11(ace.)
          deck.add(card); //we add the card to our deck.
        }
        else if (j >= 10) { //if j is bigger or equal than 10, this means that the card will be either Jack, Queen, or King.
          Card card = new Card(i, j, 10); //we create card with the ith suit and jth rank which has the value of 10(J, Q, K)
          deck.add(card); //we add the card to our deck.
        }
        else { //we do this for any other cards other than J,Q,K, and Ace.
          Card card = new Card(i, j, j+1); //when j is 1 (for example), we have a two of a suit and this has a value of two. So for the value, we increment j by one.
          deck.add(card); //we add the card to our deck.
        } 
      }
    }
  }
  
  public void shuffleDeck() { //This method shuffles the deck to make the alignment of cards random.
    Collections.shuffle(deck); 
  }
  public Card getCard(int i) { //This method returns the ith (index) card of the deck.
    return deck.get(i); 
  }
  public Card removeCard(int i) { //This method removes the ith (index) card of the deck.
    return deck.remove(i); 
  }
}