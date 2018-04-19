package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;
import Cards.RoundStart;

public abstract class ComputerPlayer extends Player {
	private HashMap <Integer, ArrayList<Card>> cardMap = new HashMap();
	public ComputerPlayer(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}
	
	/**
	 * 
	 * @return the card from the player's hand to trade. giveCardProtected is sent a copy of the player's hand
	 */
	public int giveCard () {
		return giveCardProtected(getProtectedHand());
	}
	
	protected abstract int giveCardProtected(ArrayList<Card> hand);

	/**
	 * 
	 * @param cardsAlreadyPlayed - the cards already played
	 * @param numberOfCards - the number of cards that needs to be played
	 * @return - the/a index of te card being played
	 */
	public int playCard (ArrayList<Card> cardsAlreadyPlayed, int numberOfCards) {
		return playCardProtected(cardsAlreadyPlayed, numberOfCards, getProtectedHand());
	}
	/**
	 * same as above, but the hand parameter is protected meaning it is a clone of the actual hand
	 * @param cardsAlreadyPlayed
	 * @param numberOfCards
	 * @param protectedHand
	 * @return
	 */
	protected abstract int playCardProtected (ArrayList<Card> cardsAlreadyPlayed, int numberOfCards, ArrayList<Card> protectedHand);
	
	/**
	 * 
	 * @return the number of cards and the index of the card in hand that the AI wants to use to start the round
	 */
	public RoundStart startRound () {
		return startRoundProtected(getProtectedHand());
	}
	protected abstract RoundStart startRoundProtected (ArrayList<Card> protectedHand);
	
	/**
	 * return index in "everyValueHand" of card. Index must be between 0 and 13
	 */
	public abstract int requestCard(ArrayList<Card> cardsAlreadyRequested);
	
	public boolean useGUI () {
		return false;
	}
	
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
