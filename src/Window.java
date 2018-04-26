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

import Cards.Card;
import Players.Player;

import java.util.ArrayList;

public class Window {
	private int a;
	private int handSize;
	private ArrayList<Card> playedDeckDraw1 = new ArrayList();;
	private JFrame windowBackground;
	private JLayeredPane pictures = new JLayeredPane();
	private ArrayList<JLabel> componentList = new ArrayList();
	private ArrayList<JLabel> playedDeckPictures = new ArrayList();
	private int numberOfCards;
	private JButton skipTurn;
	private JTextField nameInput1;
	private JTextField nameInput2;
	private JTextField nameInput3;
	private JTextField nameInput4;
	private Window main = this;
	private Round roundBeingPlayed;
	private boolean continueSelect;
	private boolean continueEtre;
	private AtomicBoolean enteredNames;
	private AtomicBoolean continueSelectAtomic = new AtomicBoolean();
	private AtomicBoolean continueEtreAtomic = new AtomicBoolean();
	AtomicBoolean clickedCard = new AtomicBoolean();
	boolean messageBox;
	private ArrayList<JButton> otherButtons = new ArrayList();
	private int cardClicked;
	private ArrayList<JButton> buttonList = new ArrayList();
	private AtomicInteger numberOfCardsAtomic;
	private String rootPath;
	private boolean humanPlayers;
	private boolean continueScreen;
	/**
	 * 
	 * @param size
	 * @param rootPathString
	 * @param humanplayersValue - true if the game has human players
	 */
	Window(int size, String rootPathString, boolean humanPlayersValue) {	
		rootPath = rootPathString;
		a = size;
		humanPlayers = humanPlayersValue;
		continueScreen = size == 3 || size == 4;
		if (humanPlayers || continueScreen) {
			windowBackground = new JFrame("Etre");

			if (size == 1) {
				windowBackground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowBackground.setPreferredSize(new Dimension(1440, 720));
				pictures.setPreferredSize(new Dimension(1440, 720));
				draw("backgroundPres.jpg", 0, 0, 0, true);
			} else {
				windowBackground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowBackground.setPreferredSize(new Dimension(1200, 720));
				pictures.setPreferredSize(new Dimension(1200, 720));
				if (size == 0) {
					draw("backgroundPres1.jpg", 0, 0, 0, true);
				} else if (size == 3) {
					draw("completedReal.png", 0, 0, 0, true);
				} else if (size == 4) {
					draw("president.jpg", 0, 0, 0, true);
				}
			}
		}
	}

	Window(int size, Round roundSent, String rootPathString, boolean humanPlayersValue) {
		rootPath = rootPathString;
		a = size;
		humanPlayers = humanPlayersValue;
		continueScreen = size == 3 || size == 4;
		if (humanPlayers || continueScreen) {
			windowBackground = new JFrame("Etre");

			if (size == 1) {
				windowBackground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowBackground.setPreferredSize(new Dimension(1440, 720));
				pictures.setPreferredSize(new Dimension(1440, 720));
				draw("backgroundPres.jpg", 0, 0, 0, true);
			} else {
				windowBackground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowBackground.setPreferredSize(new Dimension(1200, 720));
				pictures.setPreferredSize(new Dimension(1200, 720));
				if (size == 0) {
					draw("backgroundPres1.jpg", 0, 0, 0, true);
				} else if (size == 3) {
					draw("completed.png", 0, 0, 0, true);
				} else if (size == 4) {
					draw("president.jpg", 0, 0, 0, true);
				}
			}
			roundBeingPlayed = roundSent;
		}

	}

	public void draw(String fileName, int xLocation, int yLocation, int layer, boolean inHand) {
		if (humanPlayers || continueScreen) {
			String path = rootPath + fileName;
			// String path="F:\\Computer Science 3-AP\\Unit2Part3\\Pictures\\" +
			// fileName;
			ImageIcon picture = new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
			JLabel pictureAdd = new ImageLabel(picture, fileName);
			pictureAdd.setSize(new Dimension(picture.getIconWidth(), picture.getIconHeight()));
			pictures.add(pictureAdd, new Integer(layer));
			pictureAdd.setLocation(xLocation, yLocation);
			if (inHand) {
				componentList.add(pictureAdd);
			} else {
				playedDeckPictures.add(pictureAdd);
			}
	
			pictures.repaint();
			pictures.setVisible(true);
			windowBackground.add(pictures);
			
			windowBackground.pack();
			windowBackground.repaint();
			windowBackground.setVisible(true);
		}
	}

	public int numberOfCardsButtons(boolean additionalButton) {
		numberOfCardsAtomic = new AtomicInteger();
		numberOfCards = -1;
		numberOfCardsAtomic.set(-1);
		JButton singleCard = new JButton("Single");
		singleCard.setBackground(Color.white);
		singleCard.setBounds(50, 400, 100, 50);
		JButton doubleCard = new JButton("Doubles");
		doubleCard.setBackground(Color.white);
		doubleCard.setBounds(250, 400, 100, 50);
		JButton tripleCard = new JButton("Triples");
		tripleCard.setBackground(Color.white);
		tripleCard.setBounds(450, 400, 100, 50);
		JButton quadrupleCard = new JButton("Quadruples");
		quadrupleCard.setBackground(Color.white);
		quadrupleCard.setBounds(650, 400, 100, 50);
		pictures.add(singleCard, new Integer(2));
		singleCard.setVisible(true);
		pictures.add(doubleCard, new Integer(2));
		doubleCard.setVisible(true);
		pictures.add(tripleCard, new Integer(2));
		tripleCard.setVisible(true);
		pictures.add(quadrupleCard, new Integer(2));
		quadrupleCard.setVisible(true);
		if (additionalButton) {
			JButton clearHand = new JButton("Clear Hand");
			clearHand.setBackground(Color.white);
			clearHand.setBounds(850, 400, 100, 50);
			buttonList.add(clearHand);
			pictures.add(clearHand, new Integer(2));
			clearHand.setVisible(true);
			clearHand.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					numberOfCards = 4621;
					numberOfCardsAtomic.set(4621);
				}
			});
		}
		buttonList.add(singleCard);
		buttonList.add(doubleCard);
		buttonList.add(tripleCard);
		buttonList.add(quadrupleCard);
		windowBackground.add(pictures);
		windowBackground.pack();
		pictures.repaint();
		pictures.setVisible(true);
		windowBackground.repaint();
		windowBackground.setVisible(true);
		singleCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				numberOfCards = 1;
				numberOfCardsAtomic.set(1);
			}
		});
		doubleCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				numberOfCards = 2;
				numberOfCardsAtomic.set(2);

			}
		});
		tripleCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				numberOfCards = 3;
				numberOfCardsAtomic.set(3);
			}
		});
		quadrupleCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				numberOfCards = 4;
				numberOfCardsAtomic.set(4);
			}
		});

		do {
			// System.out.println(numberOfCards);
		} while (numberOfCards < 1 && numberOfCardsAtomic.get() < 1);
		singleCard.removeAll();
		doubleCard.removeAll();
		tripleCard.removeAll();
		quadrupleCard.removeAll();
		// System.out.println(numberOfCards);
		return numberOfCards;
	}

	public int getSize() {
		return a;
	}

	public void clearWindow() {
		if (humanPlayers || continueScreen) {
			for (int elementCounter = 1; elementCounter < componentList.size(); elementCounter++) {
				pictures.remove(pictures.getIndexOf(componentList.remove(elementCounter)));
				elementCounter -= 1;
	
			}
			for (int elementCounter = 0; elementCounter < playedDeckPictures.size(); elementCounter++) {
				pictures.remove(pictures.getIndexOf(playedDeckPictures.remove(elementCounter)));
				elementCounter -= 1;
			}
			for (int elementCounter = 0; elementCounter < otherButtons.size(); elementCounter++) {
				pictures.remove(pictures.getIndexOf(otherButtons.remove(elementCounter)));
			}
			pictures.repaint();
			pictures.setVisible(true);
			windowBackground.repaint();
			windowBackground.setVisible(true);
		}
	}

	public void removeButtons() {
		for (int elementCounter = 0; elementCounter < buttonList.size(); elementCounter++) {
			pictures.remove(pictures.getIndexOf(buttonList.remove(elementCounter)));
			elementCounter -= 1;
		}

		pictures.repaint();
		pictures.setVisible(true);
		windowBackground.repaint();
		windowBackground.setVisible(true);

	}

	public int mouseClick() {
		clickedCard.set(false);
		for (int elementCounter = 1; elementCounter < componentList.size(); elementCounter++) {
			componentList.get(elementCounter).addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent ae) {
					for (int elementCounter = 1; elementCounter < componentList.size(); elementCounter++) {
						if (ae.getSource() == componentList.get(elementCounter) && !clickedCard.get()) {
							cardClicked = elementCounter - 1;
							clickedCard.set(true);
						}
					}

				}
			});

		}
		if (playedDeckDraw1.size() > 0) {
			AbstractAction reSelectAction = new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					clickedCard.set(true);
					cardClicked = handSize;
				}
			};
			skipTurn.addActionListener(reSelectAction);
		}

		do {

		} while (!clickedCard.get());
		for (int elementCounter = 1; elementCounter < componentList.size(); elementCounter++) {
			componentList.get(elementCounter).removeAll();
		}
		return cardClicked;
	}

	// Boolean that can be accessed by other classes for a variety of
	public void setMessageBox(boolean message) {
		messageBox = message;

	}

	public boolean getMessageBox() {
		return messageBox;
	}

	public void drawPlayedDeck(ArrayList<Card> playedDeck) {
		if (humanPlayers || continueScreen) {
			ArrayList<Card> playedDeckDraw = new ArrayList();
			for (int elementCounter = 0; elementCounter < playedDeck.size(); elementCounter++) {
				playedDeckDraw.add(playedDeck.get(elementCounter));
				playedDeckDraw1.add(playedDeck.get(elementCounter));
			}
	
			int x;
			int layer = 3;
			if (a == 1) {
				x = 620;
			} else {
				x = 500;
			}
			int y = 200;
			while (playedDeckDraw.size() > 4) {
				draw(playedDeckDraw.remove(0).getFileName(), x, y, layer, false);
				x += 2;
				layer++;
			}
	
			for (int elementCounter = 0; elementCounter < playedDeckDraw.size(); elementCounter++) {
				draw(playedDeckDraw.remove(elementCounter).getFileName(), x, y, layer, false);
				layer++;
				elementCounter -= 1;
				x += 20;
				y += 40;
			}
		}
	}

	public JFrame getWindow() {
		return windowBackground;
	}

	public void drawTurnNumber(int turnNumber, String name) {
		if (humanPlayers || continueScreen) {
			JLabel playerName = new JLabel(name);
			playerName.setFont(new Font(playerName.getFont().getName(), Font.PLAIN, 45));
			playerName.setBounds(0, 25, (int) playerName.getPreferredSize().getWidth(), 63);
			playerName.setOpaque(true);
			playerName.setForeground(Color.BLACK);
			playerName.setBackground(Color.WHITE);
			pictures.add(playerName, new Integer(3));
			playerName.setVisible(true);
			playedDeckPictures.add(playerName);
			windowBackground.add(pictures);
			pictures.setVisible(true);
			windowBackground.setVisible(true);
			pictures.repaint();
			windowBackground.repaint();
		}
	}

	public void drawHand(ArrayList<Card> hand) {
		if (humanPlayers || continueScreen) {
			int start = 135;
			int x = 0;
			int multiplier = 90;
			handSize = hand.size();
			if (getSize() != 1) {
				start = 30;
				multiplier = 80;
			}
			int y = 500;
			Card cardInHand;
			for (int elementCounter = 0; elementCounter < hand.size(); elementCounter++) {
				x = start + elementCounter * multiplier;
				cardInHand = hand.get(elementCounter);
				draw(cardInHand.getFileName(), x, y, 1, true);
			}
			x += multiplier;
			if (playedDeckDraw1.size() > 0) {
				skipTurn = new JButton("Skip");
				otherButtons.add(skipTurn);
				skipTurn.setBackground(Color.white);
				skipTurn.setBounds(x, y, 75, 108);
				pictures.add(skipTurn, new Integer(3));
				windowBackground.add(pictures);
				pictures.repaint();
				windowBackground.repaint();
				skipTurn.setVisible(true);
				skipTurn.setVisible(true);
			}
		}

	}

	public void clearPlayedDeck() {
		playedDeckDraw1 = new ArrayList();
	}

	public void clearCardsPlayed() {
		if (humanPlayers || continueScreen) {
			for (int elementCounter = 0; elementCounter < playedDeckPictures.size(); elementCounter++) {
				pictures.remove(pictures.getIndexOf(playedDeckPictures.remove(elementCounter)));
				elementCounter -= 1;
				windowBackground.add(pictures);
				pictures.setVisible(true);
				pictures.repaint();
				windowBackground.repaint();
			}
		}
	}

	public void disposeWindow() {
		if (humanPlayers || continueScreen) {
			windowBackground.dispose();
		}
	}

	public boolean continueScreen(ArrayList<String> nameRankList) {
		continueSelect = false;
		continueSelectAtomic.set(false);
		continueEtre = false;
		continueEtreAtomic.set(false);
		JPanel continuePanel = new JPanel();
		continuePanel.setLayout(new BoxLayout(continuePanel, BoxLayout.PAGE_AXIS));
		continuePanel.setBounds(windowBackground.getWidth() / 2 - 200, windowBackground.getHeight() / 2, 800, 100);
		for (int elementCounter = 0; elementCounter < nameRankList.size(); elementCounter++) {
			JLabel text = new JLabel(nameRankList.get(elementCounter));
			continuePanel.add(text);
			text.setVisible(true);
		}
		JButton continueButton = new JButton("Continue Playing");
		JButton stopPlayingButton = new JButton("Stop Playing");
		continueButton.setBounds(windowBackground.getWidth() / 2 - 200, windowBackground.getHeight() / 2 + 100, 150,
				50);
		stopPlayingButton.setBounds(windowBackground.getWidth() / 2 + 50, windowBackground.getHeight() / 2 + 100, 150,
				50);
		continueButton.setBackground(Color.white);
		AbstractAction continuePlaying = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				continueSelect = true;
				continueSelectAtomic.set(true);
				continueEtre = true;
				continueEtreAtomic.set(true);
			}
		};
		AbstractAction stopPlaying = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				continueSelect = true;
				continueSelectAtomic.set(true);
			}
		};
		stopPlayingButton.setBackground(Color.red);
		pictures.add(continueButton, new Integer(5));
		pictures.add(stopPlayingButton, new Integer(5));
		pictures.add(continuePanel, new Integer(4));
		windowBackground.setVisible(true);
		windowBackground.add(pictures);
		pictures.repaint();
		pictures.setVisible(true);
		continuePanel.setVisible(true);
		windowBackground.repaint();
		continueButton.addActionListener(continuePlaying);
		stopPlayingButton.addActionListener(stopPlaying);
		do {
			// System.out.println(continueSelect);
		} while (!continueSelect && !continueSelectAtomic.get());
		System.out.println(continueEtre);
		return continueEtre;
	}

	public void addMenu() {
		if (humanPlayers || continueScreen) {
			JMenuBar menuList = new JMenuBar();
			String result = "";
			AbstractAction menuAction = new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					String selectedItem;
					JComboBox menu = (JComboBox) ae.getSource();
					selectedItem = (String) menu.getSelectedItem();
					selectedItem += ".jpg";
					if (selectedItem.equals("Default.jpg")) {
						selectedItem = "backgroundPres1.jpg";
					} else if (selectedItem.equals("Cats.jpg")) {
						selectedItem = "Cats.gif";
					} else if (selectedItem.equals("Snow.jpg")) {
						selectedItem = "Snow.gif";
					} else if (selectedItem.equals("Rain.jpg")) {
						selectedItem = "Rain.gif";
					}
					pictures.remove(pictures.getIndexOf(componentList.get(0)));
					componentList.remove(0);
					pictures.repaint();
					main.draw(selectedItem, 0, 0, new Integer(0), true);
					componentList.add(0, componentList.remove(componentList.size() - 1));
				}
			};
			AbstractAction reselectAbility = new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					boolean reselectOption;
					JComboBox menu = (JComboBox) ae.getSource();
					String selectedItem = (String) menu.getSelectedItem();
					if (selectedItem.equals("On")) {
						reselectOption = true;
					} else {
						reselectOption = false;
					}
					System.out.println("Reselect Option" + reselectOption);
					roundBeingPlayed.toggleReselect(reselectOption);
				}
			};
			for (int elementCounter = 0; elementCounter < 310; elementCounter++) {
				result += " ";
			}
			String[] backgroundOptions = { "Default", "Windows", "Puppy", "Cats", "Snow", "Rain", "Space", "Valley" };
			String[] reselectOptions = { "Off", "On" };
			roundBeingPlayed.toggleReselect(false); // Off by default
			JComboBox backgroundList = new JComboBox(backgroundOptions);
			JComboBox reselectMenu = new JComboBox(reselectOptions);
			reselectMenu.addActionListener(reselectAbility);
			backgroundList.addActionListener(menuAction);
			JLabel backgroundSelection = new JLabel("Background: ");
			JLabel reselectSelection = new JLabel("    Reselect Messages: ");
			JLabel spaceInMenu = new JLabel(result);
			menuList.add(backgroundSelection);
			backgroundSelection.setVisible(true);
			menuList.add(backgroundList);
			menuList.add(reselectSelection);
			menuList.add(reselectMenu);
			menuList.add(spaceInMenu);
			menuList.add(new JLabel("A Key-Reselect"));
			menuList.add(new JLabel("                    "));
			spaceInMenu.setVisible(true);
			backgroundList.setVisible(true);
			pictures.add(menuList, new Integer(10));
			menuList.setBounds(0, 0, 1200, 25);
			menuList.setVisible(true);
			windowBackground.add(pictures);
			pictures.repaint();
			windowBackground.repaint();
		}
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getNames(Player[] existingPlayers) {
		ArrayList<String> resultList = new ArrayList();
		enteredNames = new AtomicBoolean(false);
		JPanel namesButton = new JPanel(new BorderLayout());
		JPanel nameInputArea = new JPanel();
		nameInputArea.setLayout(new BoxLayout(nameInputArea, BoxLayout.Y_AXIS));
		windowBackground.getContentPane().setBackground(Color.white);
		nameInput1 = new JTextField(10);
		nameInput2 = new JTextField(10);
		nameInput3 = new JTextField(10);
		nameInput4 = new JTextField(10);
		for (int playerCounter = 0; playerCounter <= 3; playerCounter++) { // Never more than three players in a game
			Player player = existingPlayers[playerCounter];
			JTextField relevantField = getNameInput(playerCounter);
			if (player != null) {
				relevantField.setText(player.getName());
				relevantField.setEnabled(false);
			} else {
				relevantField.setText("Enter Player " + (playerCounter + 1) + "'s name here...");
			}
		}	

		JButton enter = new JButton("Enter Names");
		namesButton.add(enter, BorderLayout.PAGE_END);
		FocusListener nameInput1Clear = new FocusListener() {
			public void focusGained(FocusEvent ae) {
				JTextField source = (JTextField) ae.getSource();
				if (source.getText().equals("Enter Player 1's name here...")) {
					source.setText("");
				}

			}

			public void focusLost(FocusEvent ae) {

			}
		};
		FocusListener nameInput2Clear = new FocusListener() {
			public void focusGained(FocusEvent ae) {
				JTextField source = (JTextField) ae.getSource();
				if (source.getText().equals("Enter Player 2's name here...")) {
					source.setText("");
				}

			}

			public void focusLost(FocusEvent ae) {

			}
		};
		FocusListener nameInput3Clear = new FocusListener() {
			public void focusGained(FocusEvent ae) {
				JTextField source = (JTextField) ae.getSource();
				if (source.getText().equals("Enter Player 3's name here...")) {
					source.setText("");
				}

			}

			public void focusLost(FocusEvent ae) {

			}
		};
		FocusListener nameInput4Clear = new FocusListener() {
			public void focusGained(FocusEvent ae) {
				JTextField source = (JTextField) ae.getSource();
				if (source.getText().equals("Enter Player 4's name here...")) {
					source.setText("");
				}

			}

			public void focusLost(FocusEvent ae) {

			}
		};
		AbstractAction namesEnteredButton = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				boolean namesCompletelyEntered = true;
				main.clearCardsPlayed();
				if (nameInput1.getText().equals("Enter Player 1's name here...") || nameInput1.getText().equals("")) {
					namesCompletelyEntered = false;
					main.draw("redArrow.png", 780, 360, 34, false);
				}
				if (nameInput2.getText().equals("Enter Player 2's name here...") || nameInput2.getText().equals("")) {
					namesCompletelyEntered = false;
					main.draw("redArrow.png", 780, 380, 34, false);

				}
				if (nameInput3.getText().equals("Enter Player 3's name here...") || nameInput3.getText().equals("")) {
					namesCompletelyEntered = false;
					main.draw("redArrow.png", 780, 400, 34, false);

				}
				if (nameInput4.getText().equals("Enter Player 4's name here...") || nameInput4.getText().equals("")) {
					namesCompletelyEntered = false;
					main.draw("redArrow.png", 780, 420, 34, false);

				}
				if (namesCompletelyEntered) {
					enteredNames.set(true);
				}
			}
		};
		enter.addActionListener(namesEnteredButton);
		enter.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "ENTER_pressed");
		enter.getActionMap().put("ENTER_pressed", namesEnteredButton);
		nameInputArea.add(new JLabel("Enter Player Names Here:"));
		nameInputArea.add(nameInput1);
		nameInputArea.add(nameInput2);
		nameInputArea.add(nameInput3);
		nameInputArea.add(nameInput4);
		nameInputArea.add(namesButton);
		nameInput1.addFocusListener(nameInput1Clear);
		nameInput2.addFocusListener(nameInput2Clear);
		nameInput3.addFocusListener(nameInput3Clear);
		nameInput4.addFocusListener(nameInput4Clear);
		pictures.add(nameInputArea, new Integer(2));
		nameInputArea.setBounds(windowBackground.getWidth() / 2 - 200, windowBackground.getHeight() / 2, 400, 125);
		nameInputArea.setVisible(true);
		nameInputArea.repaint();
		nameInputArea.validate();
		nameInput1.setVisible(true);
		nameInput2.setVisible(true);
		nameInput3.setVisible(true);
		nameInput4.setVisible(true);
		windowBackground.add(pictures);
		pictures.setVisible(true);
		nameInput1.setVisible(true);
		pictures.repaint();
		windowBackground.repaint();
		do {

		} while (!enteredNames.get());
		resultList.add(nameInput1.getText());
		resultList.add(nameInput2.getText());
		resultList.add(nameInput3.getText());
		resultList.add(nameInput4.getText());
		return resultList;

	}
	/**
	 * 
	 * @param counter - a number that represents a player/turn (0-3)
	 * @return
	 */
	private JTextField getNameInput (int counter) {
		JTextField field;
		if (counter == 0) {
			field = nameInput1;
		} else if (counter == 1) {
			field = nameInput2;
		} else if (counter == 2) {
			field = nameInput3;
		} else {
			field = nameInput4;
		}
		return field;
	}

	public void addStaticObject(JComponent object) {
		if (humanPlayers || continueScreen) {
			pictures.add(object, new Integer(20));
			windowBackground.add(pictures);
			pictures.repaint();
			windowBackground.repaint();
		}
	}

}
