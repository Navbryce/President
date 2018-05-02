package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;
import Cards.RoundStart;

public class StrategyTwo extends ComputerPlayer{

	public StrategyTwo(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	@Override
	protected int giveCardProtected(ArrayList<Card> hand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int playCardProtected(ArrayList<Card> cardsAlreadyPlayed, int numberOfCards,
			ArrayList<Card> protectedHand) {
		// TODO Auto-generated method stub
		HashMap<Integer, ArrayList<Integer>> cardMap = getCardMap(protectedHand);
		
		int minimumValue = cardsAlreadyPlayed.get(cardsAlreadyPlayed.size() - 1).getValue();
		System.out.println("min: " + minimumValue);
		while (cardMap.get(minimumValue).size() < numberOfCards) { // Keep searching until you get a minimum value with the right number of cards
			minimumValue = getMinimumCardValue(minimumValue + 1); // For some reason the previous minimum value didn't work, so it up it by 1.
		}
		System.out.println("SELECTED:" + minimumValue);
		return cardMap.get(minimumValue).get(0); // returns the index of one of the cards you want to play
	}

	@Override
	protected RoundStart startRoundProtected(ArrayList<Card> protectedHand) {
		// TODO Auto-generated method stub
		HashMap<Integer, ArrayList<Integer>> cardMap = getCardMap(protectedHand);
		int minimumValue = getMinimumCardValue(-1);
		ArrayList<Integer> indexes = cardMap.get(minimumValue);
		
		RoundStart roundStart = new RoundStart(indexes.size(), indexes.get(0));
		System.out.println("ROUND START for "+ getName() + ": " + roundStart.toString(protectedHand));

		return roundStart;
	}

	@Override
	public int requestCard(ArrayList<Card> cardsAlreadyRequested) {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
