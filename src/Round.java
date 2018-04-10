import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import Cards.Card;
import Cards.Deck;
import Players.Player;
public class Round {
private int numberOfPlayers=4;
	private ArrayList <Card> player1=new ArrayList();
	private ArrayList <Card> player2=new ArrayList();
	private ArrayList <Card> player3=new ArrayList();
	private ArrayList <Card> player4=new ArrayList();
	private ArrayList <Card> playedDeck=new ArrayList();
	private int currentTurnNumber=1;
	private Card lastCardPlayed;
	ArrayList<Card> everyValueHand;
	PictureJLayeredPane eventLogHolder;
	private String p1;
	private String p2;
	private String p3;
	private String p4;
	private Player[] players;
	private EventLog mainLog;
	private boolean finalCardPlayedByPlayer=false;
	private AtomicBoolean reselectMB = new AtomicBoolean(true);
	private int[] finishedArray = {-1, -1, -1, -1};
	private int[] previousFinishedArray;
	private boolean skipTurn;
	private int finishedCounter=0;
	private int numberOfCards=1; //For example, 1 represents singles
	private Deck mainDeck = new Deck();
	private Window main;
	
	Round(int[] previousFinished, String player1Name, String player2Name, String player3Name, String player4Name, Player[] playerObjects, String rootPath){
		main = new Window(0, this, rootPath);
		p1 = player1Name;
		p2 = player2Name;
		p3 = player3Name;
		p4 = player4Name;
		players = playerObjects;
		main.addMenu();
		createHands();
		mainLog = new EventLog();
		eventLogHolder=new PictureJLayeredPane(825, 25, mainLog, rootPath);
		eventLogHolder.draw("logBackground.jpg", 0, 0, 1);
		main.addStaticObject(eventLogHolder);
		previousFinishedArray=previousFinished;
		printPreviousFinished();
		do{

			if(playedDeck.size()==0){
				main.clearPlayedDeck();
				main.drawTurnNumber(currentTurnNumber, findName(currentTurnNumber));
				main.drawHand(playerFind(currentTurnNumber));
				roundStart();
			}else if((lastCardPlayed.getValue()==2 || completeSet(true) || currentTurnNumber==(lastCardPlayed.getTurnPlayedOn())) && !finalCardPlayedByPlayer){
				main.clearWindow();
				mainLog.clearLog();
				eventLogHolder.updateSize();
				clearTable();
			}else if(hasPlayableCardInHand(currentTurnNumber)){
				playedDeckPrint();
				main.clearWindow();
				main.drawPlayedDeck(playedDeck);
				main.drawTurnNumber(currentTurnNumber, findName(currentTurnNumber));
				System.out.println(currentTurnNumber);
				playerCardSelect();
			}
			if(finalCardPlayedByPlayer){
				finalCardPlayedByPlayer=false;
				lastCardPlayed.setTurn(currentTurnNumber);
			}
			if(playerFind(currentTurnNumber).size()==0 && !turnNumberCompleted(currentTurnNumber)){
				playerCompletedRound();
			}
			turnCalculate();

			if(skipTurn && !completeSet(false)){
				//new MessageBox(findName(currentTurnNumber) + "'s turn has been skipped.", false, main, 1);
				mainLog.addLine(findName(currentTurnNumber) + "'s turn has been skipped.");
				eventLogHolder.updateSize();
				turnCalculate();
				skipTurn=false;
			}else if(completeSet(false)){
				skipTurn=false;
			}

		}while(finishedCounter<3);
		completedGame();
		
	}
	public void addHand(ArrayList <Card> hand){
		for(int counter=0; counter<(52/4); counter++){
			hand.add(mainDeck.getCard());
		}
	}
	public boolean hasPlayableCardInHand(int turnNumber){
		boolean playableCard=false;
		ArrayList <Card> hand=playerFind(turnNumber);
		for(int elementCounter=0; (elementCounter<hand.size()) && !playableCard; elementCounter++){
			if(isPlayableCard(hand.get(elementCounter), turnNumber, false)){
				playableCard=true;
			}
		}
		if(!playableCard){
			System.out.println(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName());
			//new MessageBox(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName(), false, main, 1);
			mainLog.addLine(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName());
			eventLogHolder.updateSize();
		}
		return playableCard;
		
	}
	public boolean isPlayableCard(Card cardPlayed, int turnNumber, boolean infoA){
		boolean playableCard=false;
		boolean enoughCards=false;
		ArrayList <Card> hand=playerFind(turnNumber);
		if(cardPlayed.getValue()==2){
			playableCard=true;
		}else if((cardPlayed.getValue()>=lastCardPlayed.getValue()) || cardPlayed.getValue()==2){
			int cardCounter=0;
			for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
				if((hand.get(elementCounter)).getValue()==cardPlayed.getValue()){
					cardCounter++;
				}
			}
			if(cardCounter>=numberOfCards){
				playableCard=true;
			}else if(infoA){
				System.out.println("You do not have enough of this card to play.");
				new MessageBox("You do not have enough of this card to play. Please try again.", false, main, 1);

			}
		}else if(infoA){
			System.out.println(cardPlayed.getName() + " is not high enough. Remember, it must be greater than or equal to " + lastCardPlayed.getValue());
			int cardCounter=0;
			for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
				if((hand.get(elementCounter)).getValue()==cardPlayed.getValue()){
					cardCounter++;
				}
			}
			String numberOfCardsInHandResult="";
			if(cardCounter<numberOfCards){
				numberOfCardsInHandResult=" And, you do not have enough of this card";
			}
			new MessageBox(cardPlayed.getName() + " is not high enough. Remember, it must be greater than or equal to " + lastCardPlayed.getName() +"." + numberOfCardsInHandResult, false, main, 2);
		}
		return playableCard;
	}
	public ArrayList playerFind(int turnNumber){
		ArrayList <Card> handArray;
		if(turnNumber==1){
			handArray=player1;
		}else if(turnNumber==2){
			handArray=player2;
		}else if(turnNumber==3){
			handArray=player3;
		}else{
			handArray=player4;
		}
		return handArray;
	}
	public String findName(int turnNumber){
		String name;
		if(turnNumber==1){
			name=p1;
		}else if(turnNumber==2){
			name=p2;
		}else if(turnNumber==3){
			name=p3;
		}else{
			name=p4;
		}
		return name;
	}
	public void printHand(int turnNumber){
		System.out.println("Your(" + findName(turnNumber)+"'s) hand is: ");
		ArrayList <Card> hand=playerFind(turnNumber);
		for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
			System.out.println(elementCounter + ". " + hand.get(elementCounter).getName());
		}
	}
	public void roundStart(){
		ArrayList<Card> hand;
		Card cardBeingPlayed;
		Scanner inputValue = new Scanner(System.in);
		int valueIn;
		boolean acceptableValue=false;
		printHand(currentTurnNumber);
		hand=playerFind(currentTurnNumber);
		lastCardPlayed=new Card(1, 1);
		new MessageBox(findName(currentTurnNumber) + ", click the number of cards you would like to play. Click anywhere to close these dialogue boxes.", false, main, 1);
			do{
				System.out.print("Please select the number of cards you want to play, " + findName(currentTurnNumber) + ": ");
				boolean messageBoxVariable=false;
				main.setMessageBox(messageBoxVariable);
				do{
					numberOfCards=main.numberOfCardsButtons(true);
					if(reselectMB.get()){
						new MessageBox("You selected: " + numberOfCardsToPlay(), true, main, 0);
					}else{
						main.setMessageBox(true);
					}
				}while(!main.getMessageBox());
			if(numberOfCards==4621){
				while(hand.size()!=0){
					hand.remove(0);
				}
				cardBeingPlayed=new Card(3, 4);
				hand.add(cardBeingPlayed);
				acceptableValue=true;
				numberOfCards=1;
				
			}else{

				System.out.print("Please select which card(s) you want to play, using its corresponding number: ");
				new MessageBox("Please select the card you want to play. You only need to select one", false, main, 1);
				main.setMessageBox(false);
				do{
					valueIn=main.mouseClick();
					if(reselectMB.get()){
						new MessageBox("You selected: " + hand.get(valueIn).getName(), true, main, 1);
					}else{
						main.setMessageBox(true);
					}
				}while(!main.getMessageBox());
				if(valueIn<0 || valueIn>=hand.size()){
					System.out.println("You did not enter an acceptable value. please try again.\n");
					cardBeingPlayed=new Card(0, 1);
				}else if(hand.get(valueIn).getValue()==2 && hand.size()>numberOfCardsOfValue(2, hand)){
					System.out.println("You cannot use a two when no other cards have been played.");
					new MessageBox("You can not use a two when no other cards have been played and said two is not your last card..", false, main, 1);
					cardBeingPlayed=new Card(0, 1);
				}else{
					cardBeingPlayed=hand.get(valueIn);
					acceptableValue=true;
					if(cardBeingPlayed.getValue()==2){
						numberOfCards=1;
					}
				}
			}
		}while(!acceptableValue || !isPlayableCard(cardBeingPlayed, currentTurnNumber, acceptableValue));
		playCard(cardBeingPlayed, currentTurnNumber);
		
	}
	public void playedDeckPrint(){
		System.out.println("\nThe following cards have been played: ");
		for(int elementCounter=playedDeck.size()-1; elementCounter>=0; elementCounter--){
			System.out.println(playedDeck.get(elementCounter).getName());
		}
	}
	public void playCard(Card cardBeingPlayed, int turnNumber){
		int elementCounter=0;
		int cardRemoveCounter=0;
		ArrayList<Card> hand=playerFind(turnNumber);
		if(cardBeingPlayed.getValue()==2){ //When a 2 is played
			System.out.println("Note: A single two can be played at any time. You can't play double or triple twos.");
			for(int elementTwoCounter=0; cardRemoveCounter<1; elementTwoCounter++){
				if(hand.get(elementTwoCounter).getValue()==2){
					hand.get(elementTwoCounter).setTurn(turnNumber);
					playedDeck.add(hand.remove(elementTwoCounter));	
					cardRemoveCounter++;
				}

			}

		}else{ //When any card other than a 2 is played
			do{
				if(hand.get(elementCounter).getValue()==cardBeingPlayed.getValue()){
					hand.get(elementCounter).setTurn(turnNumber);
					playedDeck.add(hand.remove(elementCounter));
					cardRemoveCounter++;
					elementCounter=-1;
				}
				elementCounter++;
			}while((cardRemoveCounter<numberOfCards) && elementCounter<hand.size());
		}
		System.out.println("\n" + findName(turnNumber)+ " has played " + cardBeingPlayed.getName());
		//new MessageBox(findName(turnNumber)+ " has played " + numberOfCardsToPlay()+ " of " + cardBeingPlayed.getName(), false, main, 1);
		mainLog.addLine(findName(turnNumber)+ " has played " + numberOfCardsToPlay()+ " of " + cardBeingPlayed.getName());
		eventLogHolder.updateSize();
		if(cardBeingPlayed.getValue()==lastCardPlayed.getValue()){
			skipTurn=true;
			System.out.println("\n" + findName(currentTurnNumber) + "'s turn has been skipped.");
		}
		lastCardPlayed=playedDeck.get(playedDeck.size()-1);
	}
	public ArrayList sortHand(ArrayList<Card> hand){ //Sorts the hand
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
	public ArrayList sortPivots(ArrayList<Card> hand){ //Bubble Sort
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
	public void playerCardSelect(){ //It is called when the player gets to play a card
		boolean skipTurn=false;
		ArrayList<Card> hand = new ArrayList();
		Card cardBeingPlayed;
		int valueIn;
		boolean acceptableValue=false;
		hand=playerFind(currentTurnNumber);
		Scanner inputValue = new Scanner(System.in);
		playedDeckPrint();
		printHand(currentTurnNumber);
		System.out.println(hand.size() + ". Skip Turn");
		new MessageBox(findName(currentTurnNumber) + ", it is your turn. Please select " + numberOfCardsToPlay() + " to play. Close the box to see your hand.", false, main, 1);
		main.drawHand(playerFind(currentTurnNumber));
		do{
			System.out.print(findName(currentTurnNumber) + ", please select " + numberOfCardsToPlay() + " to play: ");
			boolean messageBoxVariable=false;
			main.setMessageBox(messageBoxVariable);
			do{
				valueIn=main.mouseClick();
				if(valueIn<hand.size()){
					if(reselectMB.get()){
						new MessageBox("You selected: " + hand.get(valueIn).getName(), true, main, 0);
					}else{
						main.setMessageBox(true);
					}
				}else{
					main.setMessageBox(true);
				}
			}while(!main.getMessageBox());
			if(valueIn==hand.size()){
				skipTurn=true;
				mainLog.addLine(findName(currentTurnNumber) + ", has chosen to pass their turn. They did not play anything.");
				eventLogHolder.updateSize();
				cardBeingPlayed=new Card(0, 14);
			}else if(valueIn>=0 && valueIn<(hand.size()+1)){
				cardBeingPlayed=new Card(2,4);
				cardBeingPlayed=hand.get(valueIn);
				acceptableValue=true;
			}else{
				System.out.println("You did not enter an acceptable value. please try again.\n");
				cardBeingPlayed=new Card(0, 1);
			}
		}while(!skipTurn && (!acceptableValue || !isPlayableCard(cardBeingPlayed, currentTurnNumber, acceptableValue)));
		if(skipTurn){
			System.out.println("\n" + findName(currentTurnNumber) + " has chosen to skip his or her turn.");
		}else{
			playCard(cardBeingPlayed, currentTurnNumber);	
		}
	}
	public String numberOfCardsToPlay(){ //Returns a string that tells how many cards to play
		String numberOfCardsToPlay;
		if(numberOfCards==1){
			numberOfCardsToPlay="a Single";
		}else if(numberOfCards==2){
			numberOfCardsToPlay="Doubles";
		}else if(numberOfCards==3){
			numberOfCardsToPlay="Triples";
		}else{
			numberOfCardsToPlay="Quadruples";
		}
		return numberOfCardsToPlay;
	}
	public boolean completeSet(boolean cInfo){
		boolean setComplete=false;
		if(playedDeck.size()>=4 && playedDeck.get(playedDeck.size()-1).getValue()==playedDeck.get(playedDeck.size()-2).getValue()&&playedDeck.get(playedDeck.size()-3).getValue()==playedDeck.get(playedDeck.size()-4).getValue() && playedDeck.get(playedDeck.size()-1).getValue()==playedDeck.get(playedDeck.size()-4).getValue()){
			setComplete=true;
			if(cInfo){
				System.out.println("A set of " + playedDeck.get(playedDeck.size()-1).getName() + " has been complete.");
			}
		}
		return setComplete;
	}

	public void turnCalculate(){
		do{
			currentTurnNumber=(currentTurnNumber+1)%4;
		}while(turnNumberCompleted(currentTurnNumber));
		
	}
	public void completedGame(){ //Is called when the round is completed; it announces the ranks
		int highestUnfilledLocation=-1;
		for(int elementCounter=0; elementCounter<finishedArray.length && highestUnfilledLocation==-1; elementCounter++){
			if(finishedArray[elementCounter]==-1){
				highestUnfilledLocation=elementCounter;
				
			}
		}
		finishedArray[highestUnfilledLocation]=currentTurnNumber;
		main.disposeWindow();
		
	}
	public int[] getFinishedArray(){ //Returns the finished array from the round(Is called in the etre class)
		return finishedArray;
	}
	public boolean turnNumberCompleted(int turnNumber){ //Returns true if the player has gotten rid of all of their cards. It is used to determine if a turn should be skipped.
		boolean completed=false;
		for(int elementCounter=0; elementCounter<finishedArray.length; elementCounter++){
			if(turnNumber==finishedArray[elementCounter]){
				completed=true;
			}
		}
		return completed;
	}
	public void printPreviousFinished(){ //Prints the ranks at the start of the round
		if(previousFinishedArray[0]!=0){
			System.out.println("\nThe ranks at the start of this round are:");
			System.out.println("President: " + findName(previousFinishedArray[0]));
			System.out.println("Vice-President: " + findName(previousFinishedArray[1]));
			System.out.println("Vice-President's Helper: " + findName(previousFinishedArray[2]));
			System.out.println("President's Helper: " + findName(previousFinishedArray[3]));
			System.out.println("\nThe President Goes First: ");
			currentTurnNumber=previousFinishedArray[0];
			ArrayList<String> messageList = new ArrayList();
			messageList.add("\nThe ranks at the start of this round are:");
			messageList.add("President: " + findName(previousFinishedArray[0]));
			messageList.add("Vice-President: " + findName(previousFinishedArray[1]));
			messageList.add("Vice-President's Helper: " + findName(previousFinishedArray[2]));
			messageList.add("President's Helper: " + findName(previousFinishedArray[3]));
			messageList.add("\nTrading will now begin; the President will trade first. ");
			new MessageBox(messageList, false, main);
			playersTradeCards(previousFinishedArray);

;
		}

	}
	public void createHands(){ //Runs the various methods for hand generation
		/*for (Player player: players) {
			ArrayList<Card> hand = new ArrayList();
			addHand(hand);
			player.hand = sortHand(hand);
		}
		*/
		addHand(player1);
		player1=sortHand(player1);
		addHand(player2);
		player2=sortHand(player2);
		addHand(player3);
		player3=sortHand(player3);
		addHand(player4);
		player4=sortHand(player4);
	}
	public void clearTable(){ //Is called when a player clears the table
		String clearedTheTableString = findName(lastCardPlayed.getTurnPlayedOn()) + "'s " + numberOfCardsToPlay() + " of " + lastCardPlayed.getNameWithoutSuit() + " clears the table";
		String reason="";
		if(lastCardPlayed.getValue()==2){
			clearedTheTableString = findName(lastCardPlayed.getTurnPlayedOn()) + "'s two clears the table";
			reason+="It is a two";
		}if(lastCardPlayed.getTurnPlayedOn()==currentTurnNumber){
			if(lastCardPlayed.getFinalCardPlayed()){
				clearedTheTableString="Because the card was played by somebody who finished, it clears to the next person";
			}else{
				reason+="The turns did a complete rotation without anyone playing anything except for " + findName(currentTurnNumber);
			}
		}if(completeSet(false)){
			reason+="A set has been complete.";
		}
		//new MessageBoxs(clearedTheTableString + "(" + reason + ")", false, main, 2);
		mainLog.addLine(clearedTheTableString + "(" + reason + ")" + ".");
		eventLogHolder.updateSize();
		while(playedDeck.size()!=0){
			playedDeck.remove(0);
		}
		if(!turnNumberCompleted(lastCardPlayed.getTurnPlayedOn())){
			currentTurnNumber=lastCardPlayed.getTurnPlayedOn()-1;
		}else{
			System.out.println(findName(lastCardPlayed.getTurnPlayedOn())+ "'s card has cleared the table. Because they completed the game, it will go to the player after them");
			currentTurnNumber=lastCardPlayed.getTurnPlayedOn();
		}
		
	}
	public void playerCompletedRound(){ //Adds the player to the appropriate ran
		if(playedDeck.size()>0 && playedDeck.get(playedDeck.size()-1).getValue()==2){
			int lowestUnfilledLocation=-1;
			for(int elementCounter=finishedArray.length-1; elementCounter>=0 && lowestUnfilledLocation==-1; elementCounter--){
				if(finishedArray[elementCounter]==-1){
					lowestUnfilledLocation=elementCounter;
					
				}
			}
			finishedArray[lowestUnfilledLocation]=currentTurnNumber;
			//new MessageBox("Because " + findName(currentTurnNumber) + " ended on a two, they were placed in the lowest unfilled rank.", false, main, 1);
			mainLog.addLine("Because " + findName(currentTurnNumber) + " ended on a two, they were placed in the lowest unfilled rank.");
			eventLogHolder.updateSize();
		}else{
			int highestUnfilledLocation=-1;
			for(int elementCounter=0; elementCounter<finishedArray.length && highestUnfilledLocation==-1; elementCounter++){
				if(finishedArray[elementCounter]==-1){
					highestUnfilledLocation=elementCounter;
					
				}
			}
			finishedArray[highestUnfilledLocation]=currentTurnNumber;
			//new MessageBox(findName(currentTurnNumber) + " has gotten rid of all of their cards. Congratulations!", false, main, 1);
			mainLog.addLine(findName(currentTurnNumber) + " has gotten rid of all of their cards. Congratulations!");
			eventLogHolder.updateSize();
			
		}
		finalCardPlayedByPlayer=true;
		lastCardPlayed.finalCardPlayed();
		System.out.println(findName(currentTurnNumber) + " has gotten rid of all of their cards. Congratulations!");
		finishedCounter++;
	}
	public boolean hasCardInHand(Card cardRequested, int turnNumber, boolean message){
		ArrayList<Card> hand = playerFind(turnNumber);
		boolean hasCardInHand=false;
		for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
			if(hand.get(elementCounter).getValue()==cardRequested.getValue()){
				hasCardInHand=true;

			}
		}

		if(!hasCardInHand){
			new MessageBox(findName(turnNumber) + " does not have a " + cardRequested.getNameWithoutSuit() + " in his hand. Please try again. (Note suit does not matter)", false, main, 1);
		}
		return hasCardInHand;
	}
	public int getIndexOfCard(Card cardRequested, int turnNumber){
		ArrayList<Card> hand = playerFind(turnNumber);
		int cardIndex=-1;
		if(hasCardInHand(cardRequested, turnNumber, false)){
			for(int elementCounter=0; elementCounter<hand.size() &&  cardIndex==-1; elementCounter++){
				if(hand.get(elementCounter).getValue()==cardRequested.getValue()){
					cardIndex=elementCounter;
				}
			}
		}
		return cardIndex;
	}
	public void tradeCards(int turnNumber1, int turnNumber2, int numberOfCardsBeingTraded){
		Card selectedCard;
		int cardsTraded=0;
		drawEveryValueOfCard();
		main.drawTurnNumber(turnNumber1, findName(turnNumber1));
		do{
			drawEveryValueOfCard();
			main.drawTurnNumber(turnNumber1, findName(turnNumber1));
			if(cardsTraded>0){
				new MessageBox("Now " + findName(turnNumber1) + " select ANOTHER card you want from " + findName(turnNumber2) + ".", false, main, 1);
			}else{
				new MessageBox(findName(turnNumber1) + ", select the card you want from " + findName(turnNumber2) + ". Note, he or she might not have this card in their hand.", false, main, 2);	
			}
			Card cardSelected1;
			ArrayList<Card> handOfTrader;
			ArrayList<Card> handOfReceiver;
			handOfTrader=playerFind(turnNumber1);
			handOfReceiver=playerFind(turnNumber2);
			int valueIn;
			int indexOfCardWanted;
			int indexOfBeingGiven;
			do{
				main.setMessageBox(false);
				valueIn=main.mouseClick();
				if(reselectMB.get()){
					new MessageBox("You selected a(n): " + everyValueHand.get(valueIn).getNameWithoutSuit(), true, main, 0);
				}else{
					main.setMessageBox(true);
				}
			}while(!main.getMessageBox() || !hasCardInHand(everyValueHand.get(valueIn), turnNumber2, true));
			cardSelected1=everyValueHand.get(valueIn);
			indexOfCardWanted=getIndexOfCard(cardSelected1, turnNumber2);
			main.clearWindow();
			main.drawTurnNumber(turnNumber1, findName(turnNumber1));
			new MessageBox(findName(turnNumber1) + ", now select the card you want to give to " + findName(turnNumber2) + " in exchange for the " + cardSelected1.getNameWithoutSuit() + " that you took.", false, main, 1);
			main.drawHand(playerFind(turnNumber1));
			do{
				main.setMessageBox(false);
				valueIn=main.mouseClick();
				if(reselectMB.get()){
					new MessageBox("You selected a(n): " + handOfTrader.get(valueIn).getNameWithoutSuit(), true, main, 0);
				}else{
					main.setMessageBox(true);
				}
			}while(!main.getMessageBox()); 
			new MessageBox(findName(turnNumber1) + " took " + findName(turnNumber2) + "'s " + cardSelected1.getNameWithoutSuit() + ", in exchange for a(n) " + handOfTrader.get(valueIn).getNameWithoutSuit(), false, main, 1);
			handOfTrader.add(handOfReceiver.remove(indexOfCardWanted));
			handOfReceiver.add(handOfTrader.remove(valueIn));
			cardsTraded++;
			main.clearWindow();
		}while(cardsTraded<numberOfCardsBeingTraded);
	}
	public void playersTradeCards(int[] ranks){
		tradeCards(ranks[0], ranks[3], 2);
		mainLog.addLine("The president, " + findName(ranks[0]) + ", has traded two cards with his or her helper, " + findName(ranks[3]));
		eventLogHolder.updateSize();
		tradeCards(ranks[1], ranks[2], 1);
		mainLog.addLine("The vice-president, " + findName(ranks[1]) + ", has traded two cards with his or her helper, " + findName(ranks[2]));
		mainLog.addLine("The president, " + findName(ranks[0]) + ", goes first.");
		eventLogHolder.updateSize();
		player1=sortHand(player1);
		player2=sortHand(player2);
		player3=sortHand(player3);
		player4=sortHand(player4);
	}
	public void drawEveryValueOfCard(){
		main.clearWindow();
		everyValueHand = new ArrayList();
		for(int elementCounter=2; elementCounter<=14; elementCounter++){
			everyValueHand.add(new Card(elementCounter, 2));
		}
		main.drawHand(everyValueHand);

	}
	public int numberOfCardsOfValue(int value, ArrayList<Card> hand){
		int cardCounter=0;
		for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
			if(hand.get(elementCounter).getValue()==value){
				cardCounter++;
			}
		}
		return cardCounter;
	}
	public void toggleReselect(boolean toggle){
		reselectMB.set(toggle);
	}



}
