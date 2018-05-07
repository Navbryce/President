package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;
import Cards.Deck;
import Cards.RoundInfo;
import Cards.RoundStart;

public class StrategyTwo extends ComputerPlayer{

	public StrategyTwo(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	@Override
	public int requestCard (ArrayList<Card> cardsAlreadyRequested) {
		// TODO Auto-generated method stub
		return (int)(Math.random() * 13);
	}
	
	@Override
	protected int playCardProtected(RoundInfo roundInfo,
			ArrayList<Card> protectedHand) {
		// Set important variable values based on roundInfo and protectedHand
		int minimumValue = roundInfo.cardOnTop;
		int numberOfCardsOnTop = roundInfo.numberOfCardOnTop; // gives the number of that value of card on the top (in a row)
		int numberOfCards = roundInfo.numberOfCards;
		HashMap<Integer, ArrayList<Integer>> cardMap = getCardMap(protectedHand);

		int indexOfCardToPlay;
		System.out.println("TOP CARD: " + minimumValue + "; numberOfCards: " + numberOfCards);
		System.out.println("HAND: ");
		Player.printHand(protectedHand);
		if (numberOfCardsOnTop + cardMap.get(minimumValue).size() == 4) { // the strategy can complete the set 
			indexOfCardToPlay = cardMap.get(minimumValue).get(0);
		} else { // try to play the smallest value of card possible			
			while ((cardMap.get(minimumValue).size() < numberOfCards && minimumValue != 2)) { // Keep searching until you get a minimum value with the right number of cards or a two
				minimumValue = getMinimumCardValue(minimumValue + 1); // For some reason the previous minimum value didn't work, so it up it by 1.
				System.out.println("MINIMUM VALUE: " + minimumValue);
			}
			System.out.println("SELECTED:" + minimumValue + " with " + cardMap.get(minimumValue).size());
			indexOfCardToPlay = cardMap.get(minimumValue).get(0); // returns the index of one of the cards you want to play
		}
		return indexOfCardToPlay;
		

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

	private int getRandomCard (ArrayList<Card> hand) {
		// System.out.println("Hand size: " + hand.size());
		return (int)(Math.random() * hand.size());
	}

	@Override
	protected int giveCardProtected(ArrayList<Card> hand) {
		// TODO Auto-generated method stub
		return getRandomCard(hand);
	}
	public static void main (String args[]) {
		ArrayList<Card> hand = new ArrayList();
		Deck deck = new Deck();
		for (int cardCounter = 0; cardCounter < 5; cardCounter++) {
			hand.add(deck.getCard());
		}
		hand = Card.sortHand(hand);
		Player.printHand(hand);
		StrategyTwo strategy = new StrategyTwo("test", hand);
		System.out.println();
		System.out.println("MIN Selected:" + strategy.getMinimumCardValue(2));
		
	}


}
