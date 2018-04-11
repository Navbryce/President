package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;

public abstract class ComputerPlayer extends Player {
	private HashMap <Integer, ArrayList<Card>> cardMap = new HashMap();
	public ComputerPlayer(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	public boolean useGUI () {
		return false;
	}
	/**
	 * 
	 * @param cardsAlreadyPlayed - the cards already played
	 * @param numberOfCards - the number of cards that needs to be played
	 * @return - the/a index of te card being played
	 */
	public abstract int playCard (ArrayList<Card> cardsAlreadyPlayed, int numberOfCards);
	
	/**
	 * 
	 * @return a protected version of the hand that can be modified
	 */
	private ArrayList<Card> getProtectedHand () {
		return (ArrayList<Card>)this.hand.clone();
	}
	/**
	 * 
	 * @param cardsInput - the cards to make the map from
	 * @return - each key in the map represents a value of card (so the keys should range from 2-14. The value is an ArrayList of all cards with that value
	 */
	private HashMap <Integer, ArrayList<Card>> getCardMap (ArrayList<Card> cardsInput) {
		ArrayList<Card> cards = (ArrayList<Card>) cardsInput.clone();
		HashMap <Integer, ArrayList<Card>> cardMap = new HashMap();
		for (int cardValueCounter = 2; cardValueCounter <= 14; cardValueCounter++) {
			cardMap.put(cardValueCounter, new ArrayList <Card>());
		}
		
		for (Card card: cardsInput) {
			ArrayList<Card> cardValueList = cardMap.get(card.getValue());
			cardValueList.add(card);
		}
		return cardMap;
	}
	
}
