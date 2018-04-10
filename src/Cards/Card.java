package Cards;
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
		System.out.println(fileName);

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
}
