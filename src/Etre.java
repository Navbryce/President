import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;
import Players.RandomStrategy;
public class Etre {
	private int[] finishedArray={0,0,0,0};
	private Player[] players = new Player[4];
	private int[] scores = {0, 0, 0, 0};
	private String rootPath = "Z:\\Computer Science 3-AP\\President\\Pictures\\";
	private int numberOfGamesPlayed = 0;
	public static void main(String args[]){
		new Etre();
	}
	Etre(){
		// Strategies
		players[0] = new RandomStrategy("Random Strategy", null);
		players[1] = new RandomStrategy("Random Strategy1", null);
		players[2] = new RandomStrategy("Random Strategy2", null);
		players[3] = new RandomStrategy("Random Strategy3", null);

		
		Round currentRound;
		Window continueWindow;
		boolean completedGame=false;
		Window nameInput = new Window(4, rootPath);
		setNames(nameInput.getNames(players));
		nameInput.disposeWindow();
		do{
			numberOfGamesPlayed++;
			currentRound=new Round(finishedArray, players, rootPath);
			finishedArray=currentRound.getFinishedArray();
			processScores(finishedArray); // Update scores
			continueWindow = new Window(3, rootPath);
			completedGame=continueWindow.continueScreen(getNamesRanksList());
			continueWindow.disposeWindow();
		}while(completedGame);
		
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
	public Player player (int turnNumber){
		return players[convertTurnNumber(turnNumber)];
	}
	public String getName (int turnNumber){
		return(player(turnNumber).getName());
	}
	public ArrayList<String> getNamesRanksList(){
		ArrayList<String> resultList = new ArrayList();
		resultList.add("The Ranks and Scores after " + numberOfGamesPlayed + " game(s) are:");
		resultList.add("The President is: " + getName(finishedArray[0]));
		resultList.add("The Vice-President is: " + getName(finishedArray[1]));
		resultList.add("The Vice-President's Helper is: " + getName(finishedArray[2]));
		resultList.add("The President's Helper: " + getName(finishedArray[3]));
		return resultList;
	}
	public void setNames(ArrayList<String> names){
		for (int indexCounter = 0; indexCounter < players.length; indexCounter++) {
			if (players[indexCounter] == null) { // if the player was not defined programatically, initialize it here with the name entered
				players[indexCounter] = new HumanPlayer(names.get(indexCounter), null);
			}
		}
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
