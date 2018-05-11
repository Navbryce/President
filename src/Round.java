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
import Cards.RoundInfo;
import Cards.RoundStart;
import Players.ComputerPlayer;
import Players.Player;
public class Round {
	private int numberOfPlayers=4;
	private ArrayList <Card> playedDeck=new ArrayList();
	private boolean debug;
	private int currentTurnNumber=1;
	private Card lastCardPlayed;
	ArrayList<Card> everyValueHand;
	PictureJLayeredPane eventLogHolder;
	private Player[] players;
	private EventLog mainLog;
	private boolean previousPlayerFinished=false;
	private AtomicBoolean reselectMB = new AtomicBoolean(true);
	private int[] finishedArray = {-1, -1, -1, -1};
	private int[] previousFinishedArray;
	private boolean skipTurn;
	private int finishedCounter=0;
	private int numberOfCards=1; //For example, 1 represents singles
	private Deck mainDeck = new Deck();
	private Window main;
	private int cardPlayedOnTop = -1;
	private int numberOfSetOnTop = 0;
	

	/**
	 * 
	 * @param previousFinished
	 * @param playerObjects
	 * @param rootPath
	 * @param debug - if true, will also print game to console
	 */
	Round(int[] previousFinished, Player[] playerObjects, String rootPath, boolean debugParameter){
		debug = debugParameter;
		players = playerObjects;
		main = new Window(0, this, rootPath, playerNeedsGUI());
		main.addMenu();
		createHands();
		mainLog = new EventLog(playerNeedsGUI());
		eventLogHolder=new PictureJLayeredPane(825, 25, mainLog, rootPath);
		eventLogHolder.draw("logBackground.jpg", 0, 0, 1);
		main.addStaticObject(eventLogHolder);
		previousFinishedArray=previousFinished;
		printPreviousFinished();
		do{
			if (debug)
				System.out.println(currentTurnNumber);
			if(playedDeck.size()==0){
				main.clearPlayedDeck();
				main.drawTurnNumber(currentTurnNumber, findName(currentTurnNumber));
				roundStart();
			}else if(lastCardPlayed.getValue()==2 || completeSet(true) || (!previousPlayerFinished && currentTurnNumber==(lastCardPlayed.getTurnPlayedOn()))){
				main.clearWindow();
				mainLog.clearLog();
				eventLogHolder.updateSize();
				clearTable();
			}else if(hasPlayableCardInHand(currentTurnNumber)){
				playedDeckPrint();
				main.clearWindow();
				main.drawPlayedDeck(playedDeck);
				main.drawTurnNumber(currentTurnNumber, findName(currentTurnNumber));
				// System.out.println(currentTurnNumber);
				playerCardSelect();
			}
			if(previousPlayerFinished){ // This variable is set to true when a player finishes. It will become false after the next player's t urn
				previousPlayerFinished=false;
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
			if(isPlayableCard(hand.get(elementCounter), turnNumber, false, player(turnNumber))){
				playableCard=true;
			}
		}
		if(!playableCard){
			if (debug)
				System.out.println(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName());
			//new MessageBox(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName(), false, main, 1);
			mainLog.addLine(findName(turnNumber) + " does not have anything in his or her hand that can beat a " + numberOfCardsToPlay() + " of " + lastCardPlayed.getName());
			eventLogHolder.updateSize();
		}
		return playableCard;

	}
	public boolean isPlayableCard(Card cardPlayed, int turnNumber, boolean infoA, Player player){
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
			if (cardCounter>=numberOfCards){
				playableCard=true;
			} else if (cardPlayed.getValue() == cardPlayedOnTop && player.cardIndexes(cardPlayed.getValue()).size() + numberOfSetOnTop == 4){ // potential complete if the player is trying to play the same card as a complete
				/*
				System.out.println();
				System.out.println("PLayable because of completion");
				System.out.println("TRYING TO PLAY: " + cardPlayed.getName());
				System.out.println("ON TOP VALUE: " + cardPlayedOnTop);
				System.out.println("NUMBER OF SET ON TOP: " + numberOfSetOnTop);
				Player.printHand(player.hand);
				*/
				playableCard = true;
			} else if(infoA){
				if (debug)
					System.out.println("You do not have enough of this card to play.");
				if (player.useGUI()) {
					new MessageBox("You do not have enough of this card to play. Please try again.", false, main, 1);
				}

			}
		}else if(infoA){
			if (debug)
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
			String message = cardPlayed.getName() + " is not high enough. Remember, it must be greater than or equal to " + lastCardPlayed.getName() +"." ;
			if (debug)
				System.out.println(message);
			if (player.useGUI()) {
				new MessageBox(message, false, main, 2);
			}
		}
		return playableCard;
	}
	/**
	 * 
	 * @param turnNumber - index of player + 1. player 4 could be turn number 4 or 0
	 * @return  player
	 */
	public Player player(int turnNumber){
		if (turnNumber <= 0 || turnNumber > 3) {
			turnNumber = 4;
			// System.out.println("WARNING: Default to player 4.");
		}
		return players[turnNumber - 1];
	}
	/**
	 * 
	 * @param turnNumber - index of player + 1. player 4 could be turn number 4 or 0
	 * @return hand of player
	 */
	public ArrayList playerFind(int turnNumber){
		return(player(turnNumber).hand);
	}

	/**
	 * 
	 * @param turnNumber - index of player + 1
	 * @return name
	 */
	public String findName(int turnNumber){
		return(player(turnNumber).getName());
	}
	/**
	 * 
	 * @return will return true if one player needs GUI
	 */
	public boolean playerNeedsGUI () {
		boolean GUI = false;
		for (Player player: players) {
			GUI = player.useGUI();
			if (GUI) {
				break;
			}
		}
		return GUI;
	}
	public void printHand(int turnNumber){
		/*System.out.println("Your(" + findName(turnNumber)+"'s) hand is: ");
		ArrayList <Card> hand=playerFind(turnNumber);
		for(int elementCounter=0; elementCounter<hand.size(); elementCounter++){
			System.out.println(elementCounter + ". " + hand.get(elementCounter).getName());
		}
		*/
	}
	public void roundStart(){
		ArrayList<Card> hand;
		Card cardBeingPlayed;
		Scanner inputValue = new Scanner(System.in);
		int valueIn;
		boolean acceptableValue=false;
		Player player = player(currentTurnNumber);
		printHand(currentTurnNumber);
		hand=playerFind(currentTurnNumber);
		lastCardPlayed=new Card(1, 1);
		if (player.useGUI() || (playerNeedsGUI() && debug)) { // Draw everybody's hands EVEN if that player is computer if there is at LEAST one human player and DEBUG mode is on
			main.drawHand(player.hand);
		}
		if (player.useGUI()) {
			new MessageBox(findName(currentTurnNumber) + ", click the number of cards you would like to play. Click anywhere to close these dialogue boxes.", false, main, 1);
		} else if (playerNeedsGUI()){
			slowDown(player);

		}
		do{
			if (player.useGUI()) {
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
			}
			if(numberOfCards==4621){
				while(hand.size()!=0){
					hand.remove(0);
				}
				cardBeingPlayed=new Card(3, 4);
				hand.add(cardBeingPlayed);
				acceptableValue=true;
				numberOfCards=1;

			}else{
				if (player.useGUI()) {
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
				} else {
					RoundStart roundStart = ((ComputerPlayer)player).startRound();
					numberOfCards = roundStart.numberOfCards;
					valueIn = roundStart.card;
				}
				if(valueIn<0 || valueIn>=hand.size()){
					if (debug)
						System.out.println("You did not enter an acceptable value. please try again.\n");
					cardBeingPlayed=new Card(0, 1);
				}else if(hand.get(valueIn).getValue()==2 && hand.size()>numberOfCardsOfValue(2, hand)){
					if (debug)
						System.out.println("You cannot use a two when no other cards have been played.");
					if (player.useGUI()) {
						new MessageBox("You can not use a two when no other cards have been played and said two is not your last card..", false, main, 1);
					}
					cardBeingPlayed=new Card(0, 1);
				}else{
					cardBeingPlayed=hand.get(valueIn);
					acceptableValue=true;
					if(cardBeingPlayed.getValue()==2){
						numberOfCards=1;
					}
				}
			}
		}while(!acceptableValue || !isPlayableCard(cardBeingPlayed, currentTurnNumber, acceptableValue, player(currentTurnNumber)));
		playCard(cardBeingPlayed, currentTurnNumber);

	}
	public void playedDeckPrint(){
		if (debug) {
			System.out.println("\nThe following cards have been played: ");
			for(int elementCounter=playedDeck.size()-1; elementCounter>=0; elementCounter--){
				System.out.println(playedDeck.get(elementCounter).getName());
			}
		}
	}
	public void playCard(Card cardBeingPlayed, int turnNumber){
		int elementCounter=0;
		int cardRemoveCounter=0;
		ArrayList<Card> hand = playerFind(turnNumber);
		if(cardBeingPlayed.getValue()==2){ //When a 2 is played
			if (debug) {
				System.out.println("Note: A single two can be played at any time. You can't play double or triple twos.");
			}
			for(int elementTwoCounter=0; cardRemoveCounter<1; elementTwoCounter++){
				if(hand.get(elementTwoCounter).getValue()==2){
					hand.get(elementTwoCounter).setTurn(turnNumber);
					playedDeck.add(hand.remove(elementTwoCounter));	
					cardRemoveCounter++;
				}

			}
			cardPlayedOnTop = 2;
			numberOfSetOnTop = 1;

		}else{ //When any card other than a 2 is played
			Player player = player(turnNumber);
			

			ArrayList<Integer> cardIndexes = player.cardIndexes(cardBeingPlayed.getValue());  // arraylist of cards with cardBeingPlayed.value
			boolean potentialComplete = cardBeingPlayed.getValue() == cardPlayedOnTop; // cardPlayedOnTop is the last card played // It is possible the card will complete the set
			if (potentialComplete) {
				potentialComplete = potentialComplete && (cardIndexes.size() + numberOfSetOnTop == 4); // a complete set exists
			}
			int numberToRemove;
			if (potentialComplete) {
				numberToRemove = cardIndexes.size(); // Remove all the cards because it is going to try to complete
			} else {
				numberToRemove = numberOfCards;
			}

			// Remove the cards. Start from the bottom and go to the top because if you remove the lowest indexes first, all of the other indexes will shift down. If you start from the highest index and remove the cards, you won't have to account for the "shift down"
			for (int removeCounter = cardIndexes.size() - 1; removeCounter >= cardIndexes.size() - numberToRemove; removeCounter--) {
				int cardIndex = cardIndexes.get(removeCounter);

				Card card = hand.get(cardIndex);
				card.setTurn(turnNumber); // so the game knows who the card belongs to
				hand.remove(cardIndex);
				playedDeck.add(card); // play card
				if (cardPlayedOnTop == -1 || card.getValue() == cardPlayedOnTop) { // if the card played on top has not been set or 
					numberOfSetOnTop++;
				} else { // it's a new value that overrides an old value
					numberOfSetOnTop = 1;
				}
				cardPlayedOnTop = card.getValue();

			}
		}
		if (debug) 
			System.out.println("\n" + findName(turnNumber)+ " has played " + cardBeingPlayed.getName());
		//new MessageBox(findName(turnNumber)+ " has played " + numberOfCardsToPlay()+ " of " + cardBeingPlayed.getName(), false, main, 1);
		mainLog.addLine(findName(turnNumber)+ " has played " + numberOfCardsToPlay()+ " of " + cardBeingPlayed.getName());
		eventLogHolder.updateSize();
		if(cardBeingPlayed.getValue()==lastCardPlayed.getValue()){
			skipTurn=true;
			if (debug)
				System.out.println("\n" + findName(currentTurnNumber) + "'s turn has been skipped.");
		}
		lastCardPlayed=playedDeck.get(playedDeck.size()-1);
	}

	/**
	 * Sorts everybody's hands
	 */
	public void sortHands () {
		for (Player player: players) {
			player.hand = Card.sortHand(player.hand);
		}
	}

	public void playerCardSelect(){ //It is called when the player gets to play a card
		boolean skipTurn=false;
		ArrayList<Card> hand = new ArrayList();
		Card cardBeingPlayed;
		int valueIn;
		boolean acceptableValue=false;
		Player currentPlayer = player(currentTurnNumber);
		hand=playerFind(currentTurnNumber);
		Scanner inputValue = new Scanner(System.in);
		playedDeckPrint();
		printHand(currentTurnNumber);
		if (debug) {
			System.out.println(hand.size() + ". Skip Turn");
		}

		if (currentPlayer.useGUI() || (playerNeedsGUI() && debug)) { // If at least one player needs a GUI, draw all players' hands (even if they're a computer)
			main.drawHand(currentPlayer.hand);
		}
		if (currentPlayer.useGUI()) {
			new MessageBox(findName(currentTurnNumber) + ", it is your turn. Please select " + numberOfCardsToPlay() + " to play. Close the box to see your hand.", false, main, 1);
		} else if (playerNeedsGUI()) {
			slowDown(currentPlayer);
		}

		do{
			if (debug) 
				System.out.print(findName(currentTurnNumber) + ", please select " + numberOfCardsToPlay() + " to play: ");
			if (currentPlayer.useGUI()) { // a human player needs to use the GUI
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
			} else {
				currentPlayer = (ComputerPlayer) currentPlayer;
				RoundInfo roundInfo = new RoundInfo(playedDeck, cardPlayedOnTop, numberOfSetOnTop, numberOfCards);
				valueIn = ((ComputerPlayer)currentPlayer).playCard(roundInfo);
			}

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
				if (debug)
					System.out.println("You did not enter an acceptable value. please try again.\n");
				cardBeingPlayed=new Card(0, 1);
			}
		}while(!skipTurn && (!acceptableValue || !isPlayableCard(cardBeingPlayed, currentTurnNumber, acceptableValue, player(currentTurnNumber))));
		if(skipTurn){
			if (debug)
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
				if (debug)
					System.out.println("A set of " + playedDeck.get(playedDeck.size()-1).getName() + " has been complete.");
			}
		}
		return setComplete;
	}

	public void turnCalculate(){
		currentTurnNumber = nextTurn(currentTurnNumber);
	}
	public int nextTurn (int turnNumber) {
		do{
			turnNumber=(turnNumber+1)%4;
		}while(turnNumberCompleted(turnNumber));
		return turnNumber;
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
		if(previousFinishedArray[0] != -1){
			currentTurnNumber=previousFinishedArray[0];
			ArrayList<String> messageList = new ArrayList();
			messageList.add("\nThe ranks at the start of this round are:");
			messageList.add("President: " + findName(previousFinishedArray[0]));
			messageList.add("Vice-President: " + findName(previousFinishedArray[1]));
			messageList.add("Vice-President's Helper: " + findName(previousFinishedArray[2]));
			messageList.add("President's Helper: " + findName(previousFinishedArray[3]));
			messageList.add("\nTrading will now begin; the President will trade first. ");
			
			if (debug) {
				for (String message: messageList) {
					System.out.println(message);
				}
			}
			if (playerNeedsGUI()) {
				new MessageBox(messageList, false, main);
			}
			playersTradeCards(previousFinishedArray);

		
		}

	}
	public void createHands(){ //Runs the various methods for hand generation
		for (Player player: players) {
			ArrayList<Card> hand = new ArrayList();
			addHand(hand);
			player.hand = Card.sortHand(hand);
		}
	}
	public void clearTable(){ //Is called when a player clears the table
		String clearedTheTableString = findName(lastCardPlayed.getTurnPlayedOn()) + "'s " + numberOfCardsToPlay() + " of " + lastCardPlayed.getNameWithoutSuit() + " clears the table";
		String reason="";
		
		// Change the last card played and the number of set on top
		cardPlayedOnTop = -1;
		numberOfSetOnTop = 0;
		if (previousPlayerFinished) { // if it is a card from the player who just completed the round, treat it like the next player's card
			int turnNumber = lastCardPlayed.getTurnPlayedOn();
			turnNumber = (turnNumber+1)%4;
		}
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
			if (debug)
				System.out.println(findName(lastCardPlayed.getTurnPlayedOn())+ "'s card has cleared the table. Because they completed the game, it will go to the player after them");
			currentTurnNumber=lastCardPlayed.getTurnPlayedOn();
		}

	}
	public void playerCompletedRound(){ //Adds the player to the appropriate ran
		if (playedDeck.size()>0 && playedDeck.get(playedDeck.size()-1).getValue()==2) {
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
		} else {
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
		previousPlayerFinished=true; // set to false after the next turn
		lastCardPlayed.finalCardPlayed(); // Sets the finalCardPlayedVariable to true
		lastCardPlayed.setTurn(nextTurn(currentTurnNumber)); // treat the card like the next player played it
		if (debug)
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

		if(!hasCardInHand && message){
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
		Player player1 = player(turnNumber1);
		main.drawTurnNumber(turnNumber1, findName(turnNumber1));
		do{
			drawEveryValueOfCard();
			main.drawTurnNumber(turnNumber1, findName(turnNumber1));
			if (player1.useGUI()) {
				if(cardsTraded>0){
					new MessageBox("Now " + findName(turnNumber1) + " select ANOTHER card you want from " + findName(turnNumber2) + ".", false, main, 1);
				}else{
					new MessageBox(findName(turnNumber1) + ", select the card you want from " + findName(turnNumber2) + ". Note, he or she might not have this card in their hand.", false, main, 2);	
				}
			}
			Card cardSelected1;
			ArrayList<Card> handOfTrader;
			ArrayList<Card> handOfReceiver;
			handOfTrader=playerFind(turnNumber1);
			handOfReceiver=playerFind(turnNumber2);
			int valueIn = -1;
			int indexOfCardWanted;
			int indexOfBeingGiven;
			ArrayList<Card> cardsAlreadyRequested = new ArrayList();
			do{	
				if (valueIn != -1) {
					cardsAlreadyRequested.add(everyValueHand.get(valueIn));
				}
				
				if (player1.useGUI()) {
					main.setMessageBox(false);
					valueIn=main.mouseClick();
				} else {
					valueIn = ((ComputerPlayer)player1).requestCard(cardsAlreadyRequested);
				}
				if(reselectMB.get() && player1.useGUI()){
					new MessageBox("You selected a(n): " + everyValueHand.get(valueIn).getNameWithoutSuit(), true, main, 0);
				}else{
					main.setMessageBox(true);
				}
			}while(!main.getMessageBox() || !hasCardInHand(everyValueHand.get(valueIn), turnNumber2, player1.useGUI()));
			cardSelected1=everyValueHand.get(valueIn);
			indexOfCardWanted=getIndexOfCard(cardSelected1, turnNumber2);
			main.clearWindow();
			main.drawTurnNumber(turnNumber1, findName(turnNumber1));
			if (player1.useGUI()) {
				new MessageBox(findName(turnNumber1) + ", now select the card you want to give to " + findName(turnNumber2) + " in exchange for the " + cardSelected1.getNameWithoutSuit() + " that you took.", false, main, 1);
			}
			if (player1.useGUI()) {
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
			} else {
				valueIn = ((ComputerPlayer)player1).giveCard(); // Does not need to be checked because the player knows the cards in his/her hands
			}
			String message = findName(turnNumber1) + " took " + findName(turnNumber2) + "'s " + cardSelected1.getNameWithoutSuit() + ", in exchange for a(n) " + handOfTrader.get(valueIn).getNameWithoutSuit();
			if (player1.useGUI()) {	
				new MessageBox(message, false, main, 1);
			} else if (debug) {
				System.out.println(message);
			}
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

		sortHands();
	}
	public void drawEveryValueOfCard(){
		main.clearWindow();
		everyValueHand = Card.getEveryValueHand();
		if (playerNeedsGUI()) {
			main.drawHand(everyValueHand);
		}

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
	/**
	 * 
	 * @param turnNumber
	 * @return checks to see if the player is still in the game
	 */
	public boolean playerCompletedGame (int turnNumber) { 
		boolean playerCompleted = false;
		for (int turnCounter: finishedArray) {
			playerCompleted = turnCounter == turnNumber;
			if (playerCompleted) {
				break;
			}
		}
		return playerCompleted;
	}
	/**
	 * adds a dela
	 * @param the player of the current turn
	 */
	public void slowDown (Player player) {
		try {
			Thread.sleep(1500); // sleep to artificially slow down the gui
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public void toggleReselect(boolean toggle){
		reselectMB.set(toggle);
	}



}
