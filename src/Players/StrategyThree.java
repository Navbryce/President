package Players;

import java.util.ArrayList;
import java.util.HashMap;

import Cards.Card;
import Cards.RoundInfo;
import Cards.RoundStart;

public class StrategyThree extends ComputerPlayer{

	public StrategyThree(String nameValue, ArrayList<Card> hand) {
		super(nameValue, hand);
	}

	@Override
	protected int giveCardProtected(ArrayList<Card> hand) {
		HashMap<Integer, ArrayList<Integer>> cards = getCardMap(hand);
		int answer = 0;
		boolean end = false;
		for(int i=3;i<10 && end;i++){
			if(cards.get(i).size() == 1){
				answer = cards.get(i).get(0);
				end = true;
			}
		}
		
		return (int)(Math.random() * 13);
	}
	protected int playCardProtected(RoundInfo roundInfo,ArrayList<Card> protectedHand) {
		HashMap<Integer, ArrayList<Integer>> cards = getCardMap(hand);
		for(int i=0;i<protectedHand.size();i++){
			System.out.print(protectedHand.get(i).getValue() + " ");
		}
		System.out.println();
		//check for a card that is the same as the last played card
		int lastCard = roundInfo.lastCardsPlayed.get(roundInfo.lastCardsPlayed.size()-1).getValue();
		System.out.println("LAST CARD: "+lastCard);
		int answerIndex = 0;
		if(cards.get(lastCard).size()!=0 && cards.get(lastCard).size() >= roundInfo.numberOfCards){
			answerIndex = cards.get(lastCard).get(0);
		}
		
		if(answerIndex == 0){
			boolean end = false;
			for(int i=14;i>=lastCard && !end;i--){
				if(cards.get(i).size() !=0 && cards.get(i).size() >= roundInfo.numberOfCards){
					answerIndex = cards.get(i).get(0);
					end = true;
				}
			}
		}
		if(answerIndex == 0){
			if(hand.get(0).getValue() == 2){
				return 0;
			}
		}
		if(answerIndex == 0){
			return protectedHand.size();
		}
		return answerIndex;
	}

	@Override
	protected RoundStart startRoundProtected(ArrayList<Card> protectedHand) {
		HashMap<Integer, ArrayList<Integer>> cards = getCardMap(hand);
		RoundStart answer = null;
		int largestNum = cards.get(3).size();
		int largestValue = 3;
		for(int i=4;i<15;i++){
			if(cards.get(i).size() > largestNum){
				largestNum = cards.get(i).size();
				largestValue = i;
			}
		}
		if(largestNum == 0){
			return new RoundStart(1,0);
		}
		answer = new RoundStart(largestNum, cards.get(largestValue).get(0));
		return answer;
	}

	@Override
	public int requestCard(ArrayList<Card> cardsAlreadyRequested) {
		return (int)(Math.random() * 13);
	}

}
