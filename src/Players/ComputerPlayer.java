package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;
import Cards.RoundInfo;
import Cards.RoundStart;

public abstract class ComputerPlayer extends Player {
	private HashMap <Integer, ArrayList<Card>> cardMap = new HashMap();
	public ComputerPlayer(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}
	
	/**
	 * 
	 * @param cardsInput - the cards to make the map from
	 * @return - each key in the map represents a value of card (so the keys should range from 2-14. The value is an ArrayList of all cards' indexes with that value
	 */
	public HashMap <Integer, ArrayList<Integer>> getCardMap (ArrayList<Card> cardsInput) {
		ArrayList<Card> cards = (ArrayList<Card>) cardsInput.clone();
		HashMap <Integer, ArrayList<Integer>> cardMap = new HashMap();
		for (int cardValueCounter = 2; cardValueCounter <= 14; cardValueCounter++) {
			cardMap.put(cardValueCounter, new ArrayList <Integer>());
		}
		
		int cardIndexCounter = 0;
		for (Card card: cards) {
			ArrayList<Integer> cardValueList = cardMap.get(card.getValue()); // get the arraylist stored in the map associated with the value
			cardValueList.add(cardIndexCounter); // Add the index of the card in the hand
			cardIndexCounter++;
		}
		return cardMap;
	}
	
	/**
	 * @param minimum - the MINIMUM value the card returned has to be (includes this value)
	 * @return the value of the least significant card in your hand (NOT the index)
	 */
	public int getMinimumCardValue (int minimum) { // assumes hand is sorted
		int minimumValue = -1;
		int cardIndexCounter = 0;
		while (cardIndexCounter < this.hand.size() && (minimumValue == -1 || minimumValue == 2)) { // break loop when it finds the minimum possible card
			// all hands are sorted, so the lowest value of card should be at the start UNLESS the card is a bomb. If it is a bomb, keep searching
			int cardValue = this.hand.get(cardIndexCounter).getValue();
			if ((minimum != 2 && cardValue >= minimum) || cardValue == 2) {
				minimumValue = cardValue;
			}
			cardIndexCounter++;
		}
		return minimumValue;
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
	public int playCard (RoundInfo roundInfo) {
		return playCardProtected(roundInfo, getProtectedHand());
	}
	/**
	 * same as above, but the hand parameter is protected meaning it is a clone of the actual hand
	 * @param cardsAlreadyPlayed
	 * @param numberOfCards
	 * @param protectedHand
	 * @return
	 */
	protected abstract int playCardProtected (RoundInfo roundInfo, ArrayList<Card> protectedHand);
	
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
	public static void mian (String args[]) {
		
	}
}
