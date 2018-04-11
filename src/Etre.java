import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Players.HumanPlayer;
import Players.Player;
public class Etre {
	private int[] finishedArray={0,0,0,0};
	private String p1 = "Player 1";
	private String p2 = "Player 2";
	private String p3 = "Player 3";
	private String p4 = "Player 4";
	private Player[] players = new Player[4];
	private String rootPath = "Z:\\Computer Science 3-AP\\President\\Pictures\\";
	public static void main(String args[]){
		new Etre();
	}
	Etre(){
		Round currentRound;
		Window continueWindow;
		boolean completedGame=false;
		Window nameInput = new Window(4, rootPath);
		setNames(nameInput.getNames());
		nameInput.disposeWindow();
		do{
			currentRound=new Round(finishedArray, players, rootPath);
			finishedArray=currentRound.getFinishedArray();
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
	public String getName(int turnNumber){
		String returnString;
		if(turnNumber==1){
			returnString = p1;
		}else if(turnNumber==2){
			returnString = p2;
		}else if(turnNumber==3){
			returnString = p3;
		}else{
			returnString = p4;
		}
		return returnString;
	}
	public ArrayList<String> getNamesRanksList(){
		ArrayList<String> resultList = new ArrayList();
		resultList.add("The Ranks are:");
		resultList.add("The President is: " + getName(finishedArray[0]));
		resultList.add("The Vice-President is: " + getName(finishedArray[1]));
		resultList.add("The Vice-President's Helper is: " + getName(finishedArray[2]));
		resultList.add("The President's Helper: " + getName(finishedArray[3]));
		return resultList;
	}
	public void setNames(ArrayList<String> names){
		p1=names.get(0);
		p2=names.get(1);
		p3=names.get(2);
		p4=names.get(3);
		
		int playerCounter = 0;
		for (String name: names) {
			HumanPlayer player = new HumanPlayer(name, null);
			players[playerCounter] = player;
			playerCounter++;
		}
	}


}
