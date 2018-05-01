package Players;
import java.util.ArrayList;

import Cards.Card;

public abstract class Player {
	public ArrayList<Card> hand;
	
	private String name;
	public Player (String nameValue, ArrayList <Card> hand) {
		this.hand = hand;
		name = nameValue;
	}
	
	public String getName () {
		return name;
	}
	
	public abstract boolean useGUI ();
	
	/**
	 * 
	 * @param cardValue
	 * @return an arraylist of all indexes for the card value
	 */
	public ArrayList<Integer> cardIndexes (int cardValue) {
		ArrayList<Integer> cardIndexes = new ArrayList();
		for (int cardCounter = 0; cardCounter < hand.size() && (cardIndexes.size() < 4); cardCounter++) { 
			Card card = hand.get(cardCounter);
			if (cardValue == card.getValue()) {
				cardIndexes.add(cardCounter);
			}
		}
		return cardIndexes;
	}
}
