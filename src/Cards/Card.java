package Cards;

import java.util.ArrayList;

public class Card {
	final int hearts=1;
	final int spades=2;
	final int clubs=3;
	final int diamonds=4;
	final int jack=11;
	final int queen=12;
	final int king=13;
	final int ace=14;
	private int value;
	private int suit;
	private int turn;
	private int xLocation=0;
	private int yLocation=0;
	private boolean isFinalCardPlayed=false;
	
	/**
	 * 
	 * @return a hand with every value of card
	 */
	public static ArrayList getEveryValueHand () {
		ArrayList<Card> hand = new ArrayList();
		for(int elementCounter=2; elementCounter<=14; elementCounter++){
			hand.add(new Card(elementCounter, 2));
		}
		return hand;
		
	}
	
	public Card(int cardValue, int suitValue){
		value=cardValue;
		suit=suitValue;
	}
	public int getValue(){
		return value;
	}
	public String getName(){
		String name;
		if(value==2){
			name="Two";
		}else if(value==3){
			name="Three";
		}else if(value==4){
			name="Four";
		}else if(value==5){
			name="Five";
		}else if(value==6){
			name="Six";
		}else if(value==7){
			name="Seven";
		}else if(value==8){
			name="Eight";
		}else if(value==9){
			name="Nine";
		}else if(value==10){
			name="Ten";
		}else if(value==11){
			name="Jack";
		}else if(value==12){
			name="Queen";
		}else if(value==13){
			name="King";
		}else{
			name="Ace";
		}
		name+=" of ";
		if(suit==1){
			name+="Hearts";
		}else if(suit==2){
			name+="Spades";
		}else if(suit==3){
			name+="Clubs";
		}else if(suit==4){
			name+="Diamonds";
		}
		return name;
	}
	public void setTurn(int turnNumber){
		turn=turnNumber;
	}
	public int getTurnPlayedOn(){
		return turn;
	}
	public String getFileName(){
		String fileName=getName();
		int spaceOne=fileName.indexOf("of")-1;
		int spaceTwo=spaceOne+3;
		fileName=fileName.substring(0,spaceOne) + "_" + fileName.substring(spaceOne+1, spaceTwo) + "_" + fileName.substring(spaceTwo+1);
		if (getValue()>10){
			fileName="cards\\" + fileName + ".png";
		}else{
			fileName="cards\\" + getValue() + fileName.substring(spaceOne) + ".png";
		}
		// System.out.println(fileName);

		return fileName;
		
	}
	public void setX(int x){
		xLocation=x;
	}
	public void setY(int y){
		yLocation=y;
	}
	public int getX(){
		return xLocation;
	}
	public int getY(){
		return yLocation;
	}
	public String getNameWithoutSuit(){
		String result=getName();
		result=result.substring(0, result.indexOf(" "));
		return result;
		
	}
	public void finalCardPlayed(){
		isFinalCardPlayed=true;
	}
	public boolean getFinalCardPlayed(){
		return isFinalCardPlayed;
	}
	
	public static ArrayList sortHand(ArrayList<Card> hand){ //Sorts the hand
		ArrayList<Card> leftOfPivot=new ArrayList();
		ArrayList<Card> rightOfPivot=new ArrayList();
		ArrayList<Card> sortedHand =new ArrayList();
		int handSize=hand.size();
		Card pivotCard=hand.get(0);
		int pivotPoint=pivotCard.getValue();
		for(int elementCounter=0; elementCounter<handSize; elementCounter++){
			if(hand.get(elementCounter).getValue()<pivotPoint){
				leftOfPivot.add(hand.get(elementCounter));
			}else{
				rightOfPivot.add(hand.get(elementCounter));
			}
		}
		leftOfPivot=sortPivots(leftOfPivot); //After dividing the hand, it uses bubble sort
		rightOfPivot=sortPivots(rightOfPivot); 

		for(int elementCounter=0; elementCounter<leftOfPivot.size(); elementCounter++){ //Adds the cards back to the hand
			sortedHand.add(leftOfPivot.get(elementCounter));
		}
		for(int elementCounter=0; elementCounter<rightOfPivot.size(); elementCounter++){ //Adds the cards back to the hands
			sortedHand.add(rightOfPivot.get(elementCounter));
		}
		return sortedHand;

	}
	public static ArrayList sortPivots(ArrayList<Card> hand){ //Bubble Sort
		for(int elementCounter1=0; elementCounter1<hand.size(); elementCounter1++){
			for(int elementCounter2=0; elementCounter2<hand.size()-1; elementCounter2++){
				if(hand.get(elementCounter2).getValue()>hand.get(elementCounter2+1).getValue()){
					Card holder = hand.get(elementCounter2+1);
					hand.set(elementCounter2+1, hand.get(elementCounter2));
					hand.set(elementCounter2, holder);
				}
			}
		}
		return hand;
	}
}
