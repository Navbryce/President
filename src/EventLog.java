import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

import java.util.ArrayList;

public class EventLog extends JPanel{
	private boolean humanPlayers;
	private ArrayList<JLabel> linesAdded = new ArrayList();
	private ArrayList<String> logs = new ArrayList();
	
	EventLog(boolean humanPlayersValue){
		humanPlayers = humanPlayersValue;
		JLabel eventLogText = new JLabel("Event Log:");
		eventLogText.setForeground(Color.BLACK);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(375, 20);
		setOpaque(false);
		add(eventLogText);
		
	}
	public void addLine(String line){
		logs.add(line);
		if (humanPlayers) {
			String line2="";
			if(line.length()>65){
				line2=line.substring(63);
				line=line.substring(0, 63) + "-";
	
			}
			JLabel lineToAdd = new JLabel(line);
			JLabel lineToAdd2 = new JLabel(line2);
			lineToAdd2.setForeground(Color.BLACK);
			lineToAdd.setForeground(Color.BLACK);
			linesAdded.add(lineToAdd);
			if(line2.length()>0){
				linesAdded.add(lineToAdd2);
			}
			setSize(new Dimension(375, 17*(linesAdded.size()+1)));
			add(lineToAdd);
			if(line2.length()>0){
				add(lineToAdd2);
			}
			repaint();
			lineToAdd.setVisible(true);
		}
	}
	public void clearLog(){
		while(linesAdded.size()>0){
			remove(linesAdded.remove(0));
		}
		repaint();
	}

}
