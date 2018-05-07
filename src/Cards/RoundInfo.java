package Cards;

import java.util.ArrayList;

public class RoundInfo { // a class to copy and store round info for strategies
	public final int cardOnTop;
	public final ArrayList<Card> lastCardsPlayed;
	public final int numberOfCardOnTop;
	public final int numberOfCards;
	public RoundInfo (ArrayList<Card> cardsPlayed, int cardOnTopValue, int numberOfCardOnTopCount, int numberOfCardsToPlay) {
		lastCardsPlayed = (ArrayList<Card>)cardsPlayed.clone(); // creates a protected version
		cardOnTop = cardOnTopValue;
		numberOfCardOnTop = numberOfCardOnTopCount;
		numberOfCards = numberOfCardsToPlay;
	}
}
