package Players;

import java.util.ArrayList;

import Cards.Card;
import Cards.RoundStart;

public class RandomStrategy extends ComputerPlayer{

	public RandomStrategy(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int playCardProtected(ArrayList<Card> cardsAlreadyPlayed, int numberOfCards,
			ArrayList<Card> protectedHand) {
		int cardIndex = getRandomCard(protectedHand);
		System.out.println("Random:" + cardIndex);
		return cardIndex;
	}

	@Override
	protected RoundStart startRoundProtected(ArrayList<Card> protectedHand) {
		// TODO Auto-generated method stub
		int cardIndex = getRandomCard(protectedHand);
		// System.out.println("Random:" + cardIndex);
		RoundStart start = new RoundStart(1, cardIndex);
		return start;
	}
	
	private int getRandomCard (ArrayList<Card> hand) {
		// System.out.println("Hand size: " + hand.size());
		return (int)(Math.random() * hand.size());
	}


}
