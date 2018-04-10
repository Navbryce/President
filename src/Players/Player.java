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
}
