import java.awt.*;
import javax.swing.*;

/**
Author: Maya Ledvina
Date: 10/23/18
Description: This class draw the hangman and gallows by tracking how many guesses are left.
*/



/**
*Description: Establishes the variable, guessesLeft
*/

public class Draw extends JPanel{
	private int guessLeft;
	
	
	/**
	* Description: set guessLeft to 7 to track it accuratly
	*/
	
	public Draw(){
		
		guessLeft = 7;
		
	}
	/**
	* Description: Draws the Gallows first then the hangman based on the guessses left
	* @param Graphics g2 : the graphics needed to draw
	*/
	public void paintComponent(Graphics g2)
	{
		
	
		g2.setColor(Color.BLACK);
		g2.drawLine(50, 20, 50, 190);				// Gallows
		g2.drawLine(50, 20, 130, 20);
		g2.drawLine(130, 20, 130, 50);
			
		
		if(guessLeft <= 6){
			g2.drawOval(115, 50, 30, 30);				
		}												//draws the head
		if (guessLeft <= 5 ){
			g2.drawLine(130, 80, 130, 130);			
		}												//draws the body
		if (guessLeft <= 4 ){
			g2.drawLine(130, 90, 115, 120);		
		}												//draws left arm
		if (guessLeft <= 3 ){
			g2.drawLine(130, 90, 145, 120);				
		}												//draws right arm
		if (guessLeft <= 2 ){
			g2.drawLine(130, 130, 115, 160);		
		}												//draws left leg
		if (guessLeft <= 1){
			g2.drawLine(130, 130, 145, 160);
		}												//draws right leg
	}
	/**
	*Description: Descreases the guesses so the man is drawn correctly
	*/
	public void decreaseTries()
	{
		guessLeft--;
	}
}
