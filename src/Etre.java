import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;
import Players.RandomStrategy;
import Players.StrategyOne;
import Players.StrategyTwo;
import Players.TradingStrategyOne;
public class Etre {
	private int[] finishedArray={-1,0,0,0};
	private int[] scores = {0, 0, 0, 0};
	private String rootPath = "Z:\\Computer Science 3-AP\\President\\Pictures\\"; // Path to pictures (include \\Pictures\\ in the path)
	private int numberOfGamesPlayed = 0;
	private Player[] players;
	private int gamesIncrement;
	public static void main(String args[]){
		Player randomStrategy1 = new RandomStrategy("Random Strategy 1", null);
		Player randomStrategy2 = new RandomStrategy("Random Strategy 2", null);
		Player randomStrategy3 = new RandomStrategy("Random Strategy 3", null);
		Player randomStrategy4 = new RandomStrategy("Random Strategy 4", null);
		Player strategyTwo1 = new StrategyTwo("Bryce Strategy 1", null);
		Player strategyTwo2 = new StrategyTwo("Bryce Strategy 2", null);
		Player tradingStrategy1 = new TradingStrategyOne("Bryce Trading Strategy 1", null);
		Player tradingStrategy2 = new TradingStrategyOne("Bryce Trading Strategy 2", null);



		
		LinkedList<String> gameResults = new LinkedList<String>();
		int numberOfGames = 100;
		
		// Random game #1
		Player[] players1 = {randomStrategy1, randomStrategy2, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players1, numberOfGames, false)).rankString());
		
		// Game #2 (Player #1 improvement?)
		Player[] players2 = {strategyTwo1, randomStrategy2, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players2, numberOfGames, false)).rankString());
		
		// Game #3 (Player #1 improvement)
		Player[] players3 = {randomStrategy1, strategyTwo1, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players3, numberOfGames, false)).rankString());
		
		// Game #4
		Player[] players4 = {strategyTwo1, strategyTwo2, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players4, numberOfGames, false)).rankString());
		
		// Game #5
		Player[] players5 = {strategyTwo2, strategyTwo1, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players5, numberOfGames, false)).rankString());
		
		// Game 6 (Improved trading)
		Player[] players6 = {randomStrategy1, tradingStrategy1, randomStrategy3, randomStrategy4};
		gameResults.add((new Etre(players6, numberOfGames, false)).rankString());
		
		// Game 7 (Trading Strategies vs nonTrading Strategiess)
		Player[] players7 = {strategyTwo1, tradingStrategy1, strategyTwo2, tradingStrategy2};
		gameResults.add((new Etre(players7, numberOfGames, false)).rankString());
		
		
		
		// Print every games results
		System.out.println("\n");
		int gameCounter = 1;
		for (String result: gameResults) {
			System.out.println("Game #" + gameCounter + " with " + numberOfGames + " sub-games: ");
			System.out.println(result);
			System.out.println("\n"); // two lines down
			gameCounter++;
		}
		
	}
	Etre(Player[] playersArray, int numberOfGamesInARound, boolean useContinueScreen){
		// Strategies (Set a player equal to null to use a human strategy)
		players = playersArray;
		gamesIncrement = numberOfGamesInARound;
		
		Round currentRound;
		Window continueWindow;
		boolean completedGame=false;
		
		if (useContinueScreen || nullPlayer(players)) {
			Window nameInput = new Window(4, rootPath, playerNeedsGUI());
			setNames(nameInput.getNames(players));
			nameInput.disposeWindow();
		}

		int numberOfGamesToPlay = gamesIncrement;
		do{
			numberOfGamesPlayed++;
			numberOfGamesToPlay--;
			currentRound=new Round(finishedArray, players, rootPath, false); // set debug here
			finishedArray=currentRound.getFinishedArray();
			processScores(finishedArray); // Update scores
			if (numberOfGamesToPlay <= 0) {
				if (useContinueScreen) { // if they don't want to use the continue screen, assume they're done once the game increment ends
					continueWindow = new Window(3, rootPath, playerNeedsGUI());
					completedGame=continueWindow.continueScreen(getNamesRanksList());
					if (completedGame) {
						numberOfGamesToPlay = gamesIncrement;
					}
					continueWindow.disposeWindow();
				} else {
					completedGame = false; // stop playing
				}

			} else {
				completedGame = true; // keep playing
			}
		}while(completedGame);
		System.out.println("An entire game with " + numberOfGamesInARound + " sub-games has completed.");
		
	}
	public static boolean continueEtre(){
		Scanner userInput=new Scanner(System.in);
		System.out.print("Would you like to play another round? ");
		return userInput.nextBoolean();
		
	}
	/**
	 * 
	 * @param turnNumber
	 * @return converts turn number to index
	 */
	public int convertTurnNumber (int turnNumber) {
		if(turnNumber <= 0 || turnNumber > 4){
			turnNumber = 4;
		}
		return turnNumber - 1; // player 1 is at index 0
	}
	public Player player (int turnNumber) {
		return players[convertTurnNumber(turnNumber)];
	}
	public boolean playerNeedsGUI () { // players who are null at the start of the game are human players
		boolean GUI = false;
		for (Player player: players) {
			if (player == null || player.useGUI()) {
				break;
			}
		}
		return GUI;
	}
	public String getName (int turnNumber) {
		return(player(turnNumber).getName());
	}
	public int getScore (int turnNumber) {
		return scores[convertTurnNumber(turnNumber)];
	}
	/**
	 * 
	 * @return a sorted array where each element is a turn number. 0th index represents 1st place
	 */
	public int[] rankPlayers() { // ranks players from best to worse by turnNumber
		ArrayList<Integer> turnNumbers = new ArrayList<Integer>();
		turnNumbers.add(0);
		turnNumbers.add(1);
		turnNumbers.add(2);
		turnNumbers.add(3);
		int[] playerRanks = {-1, -1, -1, -1}; // 0th index - 1st place
		for (int rankCounter = 0; rankCounter < playerRanks.length; rankCounter++) {
			int minimum = -1;
			int minimumTurnIndex = -1;
			for (int turnCounter = 0; turnCounter < turnNumbers.size(); turnCounter++) {
				int turnNumber = turnNumbers.get(turnCounter);
				if (getScore(turnNumber) < minimum || minimum == -1) {
					minimum = getScore(turnCounter);
					minimumTurnIndex = turnCounter;
				}
			}
			// minimum has been found. removed it from the array and add it to player ranks
			int minimumTurnNumber = turnNumbers.remove(minimumTurnIndex);
			playerRanks[rankCounter] = minimumTurnNumber;
		}
		return playerRanks;

	}
	
	public String rankString () {
		int[] ranks = rankPlayers();
		String result = "Scores (Lower is better): \n";
		int rankCounter = 1;
		for (int turnNumber: ranks) {
			result += rankCounter + ". " + getName(turnNumber) + ": " + getScoreString(turnNumber) + "\n";
			rankCounter++;
		}
		return result;
	}
	public ArrayList<String> getNamesRanksList(){
		ArrayList<String> resultList = new ArrayList();
		resultList.add("The Ranks (from the most recently played game) and Scores after " + numberOfGamesPlayed + " game(s) are:");
		resultList.add("The President is: " + getName(finishedArray[0])+ ". " +  getScoreString(finishedArray[0]));
		resultList.add("The Vice-President is: " + getName(finishedArray[1])+ ". " + getScoreString(finishedArray[1]));
		resultList.add("The Vice-President's Helper is: " + getName(finishedArray[2])+ ". " + getScoreString(finishedArray[2]));
		resultList.add("The President's Helper: " + getName(finishedArray[3]) + ". " + getScoreString(finishedArray[3]));
		return resultList;
	}
	public void setNames(ArrayList<String> names){
		for (int indexCounter = 0; indexCounter < players.length; indexCounter++) {
			if (players[indexCounter] == null) { // if the player was not defined programatically, initialize it here with the name entered
				players[indexCounter] = new HumanPlayer(names.get(indexCounter), null);
			}
		}
	}
	
	public boolean nullPlayer (Player[] players) {
		boolean nullPlayer = false;
		for (Player player: players) {
			if (player == null) {
				nullPlayer = true;
				break;
			}
		}
		return nullPlayer;
	}
	
	private String getScoreString (int playerNumber) {
		double score = getScore(playerNumber);
		double average = score / (double) numberOfGamesPlayed;
		return "The player's overall score is " + score +". His/her average position is " + average + ".";
	}
	/**
	 * 
	 * @param finishedArray
	 * Processes finished array and updates scores
	 */
	private void processScores (int[] finishedArray) {
		for (int place = 0; place <= 3; place++) { // Iterate through the 4 possible places (pres, vp...)
			int turnNumber = finishedArray[place];
			int playerIndex = convertTurnNumber(turnNumber);
			scores[playerIndex] += place; // 1st place is at 0 index. 2nd place is at 1 index. The lower the score, the better
		}
	}


}
