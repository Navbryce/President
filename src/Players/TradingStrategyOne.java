package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;

/**
 * 
 * Class tests the effectiveness of varying trading strategies.
 * This one focuses on giving the smallest possible card
 *
 */
public class TradingStrategyOne extends StrategyTwo{

	public TradingStrategyOne(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}
	@Override
	protected int giveCardProtected(ArrayList<Card> hand) {
		HashMap<Integer, ArrayList<Integer>> cardMap = getCardMap(hand);
		int cardValue = getMinimumCardValue(-1);
		System.out.println("Card Value to Give: " + cardValue);
		return cardMap.get(cardValue).get(0);
	}
}
