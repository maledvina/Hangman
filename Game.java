import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
* Author: Maya Ledvina
* Date:10/5/18
* Description: This is the gui and have the main in it. It also chooses a word from a file and puts it into the GUI.
* It produces the panel and shows all the components. It plays the game  "Hangman".
*/

public class Game extends JFrame {
	
	private JTextField textField;
	private JTextField guessField;
	private JTextField guessLeftField;
	private JTextField wrongGuessField;
	private Draw bg;
	public static String guessWord;
	private char[] wordArray;
	private char[] guessStringArray; 
	private String guessTextString = ""; 
	public int guessLeft = 7; 
	private String wrongGuessTextString = ""; 
	private boolean gameRunning; 
	ArrayList<String> wrongCharacterList;
		private ArrayList<String> words = new ArrayList<String>();
	
	
	
	
	/**
	* Description: This just goes to start which creates the panel
	*/
	public Game(Scanner filename) {
		process(filename);
		guessWord = chooseWord(words);
		start();

	}
	
	/**
	* 
	Description: This checks to see if its a single letter
	* and warns the user if it isn't
	* @param  String s: char user entered
	*/
	public boolean inputIsSuitable(String s) {
		boolean suitable = true;
		
		if(s.length() != 1) { 
			JOptionPane.showMessageDialog(null, "NOT A SINGLE CHARACTER");
			suitable = false;
		}
		
		char c = s.charAt(0);
		
		if(!Character.isLetter(c)) { 
			suitable = false;
			JOptionPane.showMessageDialog(null,"NOT A LETTER ");
		}
		
		return suitable;
	}
	
	/**
	* Description: the main class. sets up the panel and make sure it's visible. Makes sure it exits when
	* the window is closed.	
	*/
	public static void main(String[] args) throws IOException{
		 Scanner filename = new Scanner(new File (args[0])); // gets the file 
		JFrame frame = new Game(filename);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		frame.setVisible(true);

	}
		
		

	
	/**
	* Description: Sets up the entire panel includeing all the fields and button 
	* and shows the starting image.
	*/
	public void start() {
	
		bg = new Draw(); // Draw class
		
		textField = new JTextField(15);
		guessField = new JTextField(1);
		guessLeftField = new JTextField(2);
		wrongGuessField = new JTextField(15);
		gameRunning = true;
		
		JButton butGuess = new JButton("GUESS");
		
		JPanel imgPane = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel topPanel = new JPanel();		
		JPanel bottomPanel = new JPanel();	
		JPanel centerPanel = new JPanel();
		
		imgPane.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(480, 80));
		bottomPanel.setPreferredSize(new Dimension(480, 80));
		leftPanel.setPreferredSize(new Dimension(98, 200));
		rightPanel.setPreferredSize(new Dimension(98, 200));
		Box iconPanel = new Box(BoxLayout.Y_AXIS);
		JLabel rightGuessLabel = new JLabel();
		rightGuessLabel.setText("Correct guesses");	
		JLabel guessLabel = new JLabel();
		guessLabel.setText(" ^Guess Letter");	
	
		JLabel wrongGuessLabel = new JLabel();
		wrongGuessLabel.setText("Wrong guesses");
		textField.setText(getTextString());

		guessLeftField.setEditable(false);
		guessField.setHorizontalAlignment(JTextField.CENTER);
	
		wrongGuessField.setHorizontalAlignment(JTextField.CENTER);
		wrongGuessField.setForeground(Color.red);
		wrongGuessField.setEditable(false);
		
	
		guessLeftField.setHorizontalAlignment(JTextField.CENTER);
		
		topPanel.add(rightGuessLabel);
		topPanel.add(textField);
		leftPanel.add(guessField);
		leftPanel.add(guessLabel);	
		
		
		rightPanel.add(butGuess);
		bottomPanel.add(wrongGuessLabel);
		bottomPanel.add(wrongGuessField);
		
		
		
		add(bg);
		add(topPanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);	
	
	
	

	
	/**
	* 
	* Description: This is for when the confirm button is pressed
	* It will inform the user if they have lost and then exits.
	* @param ActionEvent e: the button being pressed
	*/
		butGuess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameRunning = checkGameState();
				if(guessField.getText().length() != 0 && inputIsSuitable(guessField.getText()) && gameRunning) { 
					checkGuess(guessField.getText());
					textField.setText(getTextString());
					
					wrongGuessField.setText(getWrongTextString());
				}
				if(!checkVictory() && guessLeft == 0) {
					gameRunning = false;
				    JOptionPane.showMessageDialog(null, "YOU LOSE\nTHE WORD WAS\n\"" + guessWord);				
					System.exit(0);
					}
				
				}
				
			});
		wrongCharacterList = new ArrayList<String>();
		wordArray = new char[guessWord.length()];
		guessStringArray = new char[guessWord.length()];

		for(int i = 0; i < guessWord.length(); i++) {
			wordArray[i] = guessWord.charAt(i);
			guessStringArray[i] = '_'; 
		}
		
		updateGuessTextString();
		gameRunning = true;
	}
	/**
	* Description: This just makes sure that the guess box is empty
	*/
	private void updateGuessTextString() {
		guessTextString = "";
		for(int i = 0; i < guessWord.length(); i++) {
			guessTextString += guessStringArray[i] + " ";
		}
	}
	
	/**
	* Description: this gets the text string 
	*/
	public String getTextString() {
		return guessTextString;
	}	
	
	/**
	Description: checks to see if the game is running or not
	*/
	public boolean checkGameState() {
		return gameRunning;
	}	
	
	/**
	* Description: This collects the wrong letter guessed text
	*/
	public String getWrongTextString() {
		return wrongGuessTextString;
	}

	
	/**
	* 
	* Description: This checks the guess that the player makes
	* if its wrong it will lower the guesses left
	* if its right then it will replace an underscore and then check 
	* to see if the player won or not
	* this also checks if they used a character or not
	* Will draw the correct body part if guessed wrong
	* @Param inputString = letter guessed
	*/
	
	public void checkGuess(String inputString) {
		if(gameRunning) {
			char inputChar = Character.toLowerCase(inputString.charAt(0)); // letter guessed by player to lower case
			boolean hit = false; // got the letter or not
			
			for(int i = 0; i < guessWord.length(); i++) {
				if(wordArray[i] == inputChar) {
					hit = true;
					guessStringArray[i] = inputChar;
				}
			}
			
			if(hit) { 
				updateGuessTextString();
				
			} else {
				if(!wrongCharacterList.contains(inputString)) { // wrong letter
					wrongCharacterList.add(inputString);
					guessLeft--;
					wrongGuessTextString += inputChar + " ";
				if (guessLeft == 6){ // draw head
					
					bg.decreaseTries(); // goes to draw class
					repaint();
					revalidate();
				}
				if (guessLeft == 5){ // draw body
					
					bg.decreaseTries();
					repaint();
					revalidate();
				}
				if (guessLeft == 4){ // draw right arm
					
					bg.decreaseTries();
					repaint();
					revalidate();
				}
				if (guessLeft == 3){ // draw left arm
					
					bg.decreaseTries();
					repaint();
					revalidate();
				}
				if (guessLeft == 2){ // draw right leg
					
					bg.decreaseTries();
					repaint();
					revalidate();
				}
				if (guessLeft == 1){ // draw left leg
					
					bg.decreaseTries();
					repaint();
					revalidate();
				}
				} else {
					JOptionPane.showMessageDialog(null,"Letter already guessed");
					
				}
			}
			
			if(checkVictory()) { // see if they've won
				gameRunning = false;
		        JOptionPane.showMessageDialog(null, "YOU WIN");	
				System.exit(0);
			}
		}
	}
	/**
	* Description: checks if the player wins by seeing if there
	* are underscores there or not
	*/
	
	public boolean checkVictory() {
		boolean victory = true;
		
		for(int i = 0; i < guessWord.length(); i++) { // if there is a underscore then they haven't won
			if(guessStringArray[i] == '_') {
				victory = false;
			}
		}
		
		return victory;
	}	
		/**
		*Description: This method processes the file given 
		@param Scanner file is the file given 
		*/
	   private void process(Scanner file) {
        while (file.hasNextLine()) {
          String word = file.nextLine();
          words.add(word);
        }
    }

    /**
    * Description: This chooses a random word from the file and puts in a new array ArrayList
    * It also checks if the guesses letter is in the word to guess.
    * @param ArrayList<String> words the array list with all the words from the file
    */
    private String chooseWord(ArrayList<String> words) {
      System.out.println("words "+words.size());
      Random r = new Random();
      String guessWord = words.get(r.nextInt(words.size()));
		  //System.out.println("guess" + guessWord);  // easier to test
      return guessWord;
    }
	

	}

