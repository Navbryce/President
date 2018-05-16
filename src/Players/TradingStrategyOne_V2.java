package Players;

import java.util.ArrayList;

import Cards.Card;

/**
 * Same as trading TradingStrategyOne BUT tries to strategically take cards as well
 * @author bplunk0244
 *
 */
public class TradingStrategyOne_V2 extends TradingStrategyOne{

	public TradingStrategyOne_V2(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}
	@Override
	public int requestCard (ArrayList<Card> cardsAlreadyRequested) {
		// TODO Auto-generated method stub
		int max = -1;
		for (Card card: cardsAlreadyRequested) {
			if (max == -1 || card.getValue() >= max) {
				max = card.getValue();
			}
		}
		return max;
	}

}
