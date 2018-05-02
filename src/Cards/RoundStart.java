package Cards;

import java.util.ArrayList;

public class RoundStart {
	public int numberOfCards;
	public int card;
	
	public RoundStart (int number, int cardIndex) {
		this.numberOfCards = number;
		this.card = cardIndex;
	}
	
	public String toString (ArrayList<Card> hand) {
		return " Starting round with " + this.numberOfCards + " of " + hand.get(card).getName();
	}
}