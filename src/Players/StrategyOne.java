package Players;

import java.util.ArrayList;

import Cards.Card;
import Cards.RoundInfo;
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
	protected int playCardProtected(RoundInfo roundInfo,
			ArrayList<Card> protectedHand) {
		ArrayList<Card> cardsAlreadyPlayed = roundInfo.lastCardsPlayed;
		int numberOfCards = roundInfo.numberOfCards;
		for(int i=0;i<protectedHand.size();i++){
			System.out.print(protectedHand.get(i).getValue() + " ");
		}
		System.out.println();
		//if strategy has the first turn at the start of the round, it playes its lowest card that isn't a 2
		if(cardsAlreadyPlayed.size() == 0) {
			int answerIndex = 0;
			boolean end = false;
			for(int i=0;i<protectedHand.size() && !end;i++) {
				if(protectedHand.get(i).getValue() != 2){
					answerIndex = i;
					end = true;
				}
			}
			return answerIndex;
		}else {
			ArrayList<Integer>[] cardSets = new ArrayList[]{new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
					
			//sort hand into singles, doubles, triples, and quadrouples
			int previousCardValue = protectedHand.get(0).getValue();
			int count = 1;
			for(int i=1;i<protectedHand.size();i++) {
				int currentCardValue = protectedHand.get(i).getValue();
				if(currentCardValue == previousCardValue) {
					count++;
				}else {
					cardSets[count-1].add(previousCardValue);
					previousCardValue = currentCardValue;
					count = 1;
					if(i==protectedHand.size()-1){
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
				boolean stop = false;
				for(int i=0;i<cardSets[numberOfCards-1].size() && !stop;i++) {
					if(cardSets[numberOfCards-1].get(i)==lastCardPlayed) {
						answerValue = cardSets[numberOfCards-1].get(i);
						stop = true;
					}
				}
				if(lastCardPlayed == 14) {
					end = true;
				}
				lastCardPlayed++;
			}
			if(end) {
				for(int i=0;i<protectedHand.size();i++){
					if(protectedHand.get(i).getValue() == 2){
						answerValue = 2;
					}
				}
				if(answerValue==0){
					return protectedHand.size();
				}
			}
			//System.out.println("STRATEGY ONE ANSWER: "+answerValue);
			//answerIndex - index in the hand of the card being played 
			int answerIndex = 0;
			for(int i=0;i<protectedHand.size();i++) {
				if(protectedHand.get(i).getValue() == answerValue){
					answerIndex = i;
				}
			}	
			return answerIndex;
		}
	}

	@Override
	protected RoundStart startRoundProtected(ArrayList<Card> hand) {
		RoundStart answer = null;
		boolean end = false;
		for(int i=0;i<hand.size() && !end;i++){
			if(hand.get(i).getValue() != 2){
				answer =  new RoundStart(1,i);
				end = true;
			}
		}
		return answer;
	}

	@Override
	public int requestCard(ArrayList<Card> cardsAlreadyRequested) {
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
		int answer = 0;
		boolean end = false;
		int counter = 3;
		while(!end && counter >=0){
			for(int i=0;i<cardSets[counter].size() && !end;i++){
				boolean equal = false;
				for(int x=0;x<cardsAlreadyRequested.size();x++){
					if(cardSets[counter].get(i) == cardsAlreadyRequested.get(i).getValue()){
						equal = true;
					}
				}
				if(!equal){
					end = true;
					answer = i;
				}
			}
		}
		int answerIndex = 0;
		for(int i=0;i<hand.size();i++){
			if(answer == hand.get(i).getValue()){
				answer = i;
			}
		}
		return answerIndex;
	}

}

