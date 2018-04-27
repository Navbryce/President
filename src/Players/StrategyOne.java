package Players;

import java.util.ArrayList;

import Cards.Card;
import Cards.RoundStart;

public class StrategyOne extends ComputerPlayer {
	
	public StrategyOne(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	protected int giveCardProtected(ArrayList<Card> hand) {
		ArrayList<Integer>[] cardSets = new ArrayList[]{new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
		
		//sort hand into singles, doubles, triples, and quadrouples
		int previousCardValue = hand.get(0).getValue();
		int count = 1;
		for(int i=1;i<hand.size();i++) {
			int currentCardValue = hand.get(i).getValue();
			if(currentCardValue == previousCardValue) {
				count++;
			}else {
				cardSets[count-1].add(previousCardValue);
				previousCardValue = currentCardValue;
				count = 1;
				if(i==hand.size()-1){
					cardSets[0].add(currentCardValue);
				}
			}
		}
		
		int cardToGive = cardSets[0].get(0);
		int answer = 0;
		for(int i=0;i<hand.size();i++){
			if(cardToGive == hand.get(i).getValue()){
				answer = i;
			}
		}
		return answer;
	}

	@Override
	protected int playCardProtected(ArrayList<Card> cardsAlreadyPlayed, int numberOfCards,
			ArrayList<Card> protectedHand) {
		//if strategy has the first turn at the start of the round, it playes its lowest card that isn't a 2
		if(cardsAlreadyPlayed.size() == 0) {
			int answerIndex = 0;
			boolean end = false;
			for(int i=0;i<hand.size() && !end;i++) {
				if(hand.get(i).getValue() != 2){
					answerIndex = i;
					end = true;
				}
			}
			return answerIndex;
		}else {
			ArrayList<Integer>[] cardSets = new ArrayList[]{new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
					
			//sort hand into singles, doubles, triples, and quadrouples
			int previousCardValue = hand.get(0).getValue();
			int count = 1;
			for(int i=1;i<hand.size();i++) {
				int currentCardValue = hand.get(i).getValue();
				if(currentCardValue == previousCardValue) {
					count++;
				}else {
					cardSets[count-1].add(previousCardValue);
					previousCardValue = currentCardValue;
					count = 1;
					if(i==hand.size()-1){
						cardSets[0].add(currentCardValue);
					}
				}
			}
					
			//answerValue - value of the card that is being played
			//tries to find the closest card to the last one that was played
			//if there is no card equal to or higher than the last card that was played, the strategy will then try to play a 2
			//if there is no 2 in the hand, then it will skip the turn
			int answerValue = 0;
			boolean end = false;
			int lastCardPlayed = cardsAlreadyPlayed.get(cardsAlreadyPlayed.size()-1).getValue();
			while(answerValue == 0 && !end) {
				for(int i=0;i<cardSets[numberOfCards-1].size();i++) {
					if(cardSets[numberOfCards-1].get(i)==lastCardPlayed) {
						answerValue = cardSets[numberOfCards-1].get(i);
					}
				}
				if(lastCardPlayed == 14) {
					end = true;
				}
				lastCardPlayed++;
			}
			if(end) {
				for(int i=0;i<hand.size();i++){
					if(hand.get(i).getValue() == 2){
						answerValue = 2;
					}
				}
				if(answerValue==0){
					return hand.size();
				}
			}
			//answerIndex - index in the hand of the card being played 
			int answerIndex = 0;
			for(int i=0;i<hand.size();i++) {
				if(hand.get(i).getValue() == answerValue){
					answerIndex = i;
				}
			}	
			return answerIndex;
		}
	}

	@Override
	protected RoundStart startRoundProtected(ArrayList<Card> hand) {
		RoundStart answer = null;
		for(int i=0;i<hand.size();i++){
			if(hand.get(i).getValue() != 2){
				answer =  new RoundStart(1,i);
			}
		}
		if(answer==null){
			answer = new RoundStart(1,0);
		}
		return answer;
	}

	@Override
	public int requestCard(ArrayList<Card> cardsAlreadyRequested) {
		// TODO Auto-generated method st
		return (int)(Math.random() * 13);
	}

}
