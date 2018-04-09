import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import javax.swing.KeyStroke.*;

import java.awt.event.KeyEvent;

import javax.swing.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MessageBox {
	private JFrame messageBox = new JFrame("Message Box");
	private boolean messageBoxType;
	private boolean removeButtons;
	private Window main;
	private AbstractAction closeAction;
	private boolean selectedButton2=false;
	private boolean messageBoxVariable2=false;
	private AtomicBoolean messageBoxVariable=new AtomicBoolean(false);
	private AtomicBoolean selectedButton=new AtomicBoolean(false);
	MessageBox(String message, boolean a, Window windowSent, int size){
		messageBoxType=a;
		String close="Close";
		if(a){
			close="Apply Selected";
		}
		removeButtons=a;
		main=windowSent;
		messageBox.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		messageBox.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    	if(messageBoxType){
		    		selectedButton.set(true);
		    		selectedButton2=true;
		    	}else{
		    		selectedButton.set(true);
		    		selectedButton2=true;
		    		messageBoxVariable.set(true);
		    		messageBoxVariable2=true;

		    	}
		       
		    } 
		});
		messageBox.setPreferredSize(new Dimension(300,100));
		if(size==1){
			messageBox.setPreferredSize(new Dimension(600,100));
		}else if(size==2){
			messageBox.setPreferredSize(new Dimension(1000,100));
		}
		JPanel messageArea = new JPanel();
		messageArea.setLayout(new BorderLayout());
		JLabel text = new JLabel(message);
		text.setSize(new Dimension(300,50));
		messageArea.setSize(new Dimension(300,100));
		messageArea.add(text);
		JButton closeButton = new JButton(close);
	    closeButton.setBackground(Color.white);
	    closeButton.setBounds(100, 0, 50, 25);
	    JPanel buttons = new JPanel();
	    buttons.add(closeButton);
		messageArea.add(buttons, BorderLayout.PAGE_END);
		messageArea.setVisible(true);
		messageArea.repaint();
		messageBox.setAlwaysOnTop(true);
		messageBox.pack();
		messageBox.add(messageArea);
		messageBox.setVisible(true);
		messageBox.repaint();
		AbstractAction reSelectAction=new AbstractAction(){
	    	public void actionPerformed(ActionEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    	}
	    };
		if(a){
			JButton select = new JButton("Reselect");
			select.setBackground(Color.white);
			buttons.add(select);
			select.setBounds(100, 0, 50, 25);
		    select.addActionListener(reSelectAction);

	    }
		closeAction=new AbstractAction(){
	    	public void actionPerformed(ActionEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    		messageBoxVariable.set(true);
	    		messageBoxVariable2=true;
	    	}
	    };

	    closeButton.addActionListener(closeAction);
		main.getWindow().addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    		messageBoxVariable.set(true);
	    		messageBoxVariable2=true;
			}
		 });

	    closeButton.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,0), "A_pressed");
	    closeButton.getActionMap().put("A_pressed", reSelectAction);
	   
	    boolean applySelection=false;
	    do{
	    	//System.out.println(selectedButton);
	    }while(!selectedButton.get() && !selectedButton2);
	    System.out.println(messageBoxVariable2);
	    main.setMessageBox(messageBoxVariable.get());
	    main.setMessageBox(messageBoxVariable2);
	   	main.removeButtons();
	   	messageBox.dispose();
	}

	MessageBox(ArrayList<String> message, boolean a, Window windowSent){
		ArrayList<JLabel> textLabels = new ArrayList();
		String close="Close";
		if(a){
			close="Apply Selected";
		}
		removeButtons=a;
		main=windowSent;
		messageBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messageBox.setPreferredSize(new Dimension(400,30*message.size()));

		JPanel messageArea = new JPanel();
		messageArea.setLayout(new BoxLayout(messageArea, BoxLayout.PAGE_AXIS));

		while(message.size()>0){
			String messageString = message.remove(0);
			JLabel textString = new JLabel(messageString);
			messageArea.add(textString);
			textString.setVisible(true);
		}
		JButton closeButton = new JButton("Close");
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
	    closeButton.setBackground(Color.white);
	    closeButton.setBounds(100, 0, 50, 25);
	    buttons.add(closeButton, BorderLayout.PAGE_END);
	    messageArea.add(buttons);
		messageArea.setVisible(true);
		messageArea.repaint();
		messageBox.setAlwaysOnTop(true);
		messageBox.pack();
		messageBox.add(messageArea);
		messageBox.setVisible(true);
		messageBox.repaint();
		AbstractAction reSelectAction=new AbstractAction(){
	    	public void actionPerformed(ActionEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    	}
	    };
		if(a){
			JButton select = new JButton("Reselect");
			select.setBackground(Color.white);
			messageArea.add(select);
			select.setBounds(100, 0, 50, 25);
		    select.addActionListener(reSelectAction);
		    messageArea.getInputMap(JComponent.WHEN_FOCUSED).
	        put(KeyStroke.getKeyStroke("A"), "A_pressed");
		    select.getInputMap(JComponent.WHEN_FOCUSED).
	        put(KeyStroke.getKeyStroke("B"), "B_pressed");
		    messageArea.getActionMap().put("A_pressed", reSelectAction);
		    select.getActionMap().put("B_pressed", reSelectAction);

	    }
		closeAction=new AbstractAction(){
	    	public void actionPerformed(ActionEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    		messageBoxVariable.set(true);
	    		messageBoxVariable2=true;
	    	}
	    };

	    closeButton.addActionListener(closeAction);
		main.getWindow().addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent ae){
	    		selectedButton.set(true);
	    		selectedButton2=true;
	    		messageBoxVariable.set(true);
	    		messageBoxVariable2=true;
			}
		 });


	 	   
	    boolean applySelection=false;
	    do{
	    	//System.out.println(selectedButton);
	    }while(!selectedButton.get() && !selectedButton2);
	    System.out.println(messageBoxVariable2);
	    main.setMessageBox(messageBoxVariable.get());
	    main.setMessageBox(messageBoxVariable2);
	   	main.removeButtons();
	   	messageBox.dispose();

	}
	    
}

