import java.util.ArrayList;
import java.*;

public class Deck {
	private ArrayList<Card> deck = new ArrayList();
	
	Deck(){
		Card addCard;
		for (int suitCounter=1; suitCounter<=4; suitCounter++){
			for (int cardCounter=2; cardCounter<=14; cardCounter++){
				addCard=new Card(cardCounter, suitCounter);
				deck.add(addCard);
			}
		}
	}
	public Card getCard(){
		int cardElement=(int)(deck.size()*Math.random());
		return (deck.remove(cardElement));
		
	}

}
