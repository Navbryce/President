package Players;

import java.util.ArrayList;

import Cards.Card;

public class HumanPlayer extends Player{

	public HumanPlayer(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	@Override
	public boolean useGUI() {
		return true;
	}

}
