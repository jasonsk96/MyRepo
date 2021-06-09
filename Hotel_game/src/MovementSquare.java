import java.awt.*;
import javax.swing.*;


public class MovementSquare extends Square{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int next_i;
	private int next_j;
	private String right_neighbour;
	private String left_neighbour;
	private String up_neighbour;
	private String down_neighbour;
	private JLabel player1Label;
	private JLabel player2Label;
	private JLabel player3Label;
	private boolean otherPlayer = false;
	
	public MovementSquare(String type, String right_neighbour, String left_neighbour, String up_neighbour, String down_neighbour, int i, int j){
		super(type, i ,j);
		this.right_neighbour = right_neighbour;
		this.left_neighbour = left_neighbour;
		this.up_neighbour = up_neighbour;
		this.down_neighbour = down_neighbour;
	}
	
	/**
	 * player 1 label setter
	 * @param playerLabel passed and set at a square, representing player 1
	 */
	public void setPlayer1Label(JLabel playerLabel){
		player1Label = playerLabel; 
		add(player1Label, BorderLayout.SOUTH);
		this.revalidate();
	}
	
	/**
	 * player 2 label setter
	 * @param playerLabel passed and set at a square, representing player 2
	 */
	public void setPlayer2Label(JLabel playerLabel){
		player2Label = playerLabel;
		add(player2Label, BorderLayout.WEST);
		this.revalidate();
	}
	
	/**
	 * player 3 label setter
	 * @param playerLabel passed and set at a square, representing player 3
	 */
	public void setPlayer3Label(JLabel playerLabel){
		player3Label = playerLabel;
		add(player3Label, BorderLayout.EAST);
		this.revalidate();
	}
	
	/**
	 * remove a player's label
	 * @param tempPlayer passed to remove the appropriate label
	 */
	public void removeLabel(Player tempPlayer){
		if(tempPlayer.getName().equals("P1")) remove(player1Label);
		else if(tempPlayer.getName().equals("P2")) remove(player2Label);
		else remove(player3Label);
		this.revalidate();
	}

	/**
	 * @param flag passed to update square's state, when a player reaches or leaves the square
	 */
	public void someoneHere(boolean flag){
		otherPlayer = flag;
	}
	
	/**
	 * checks if there is someone else in this square
	 */
	public boolean whoIsHere(){
		return otherPlayer;
	}
	
	/**
	 * @return next square's row position on the game board
	 */
	public int getNextI(){
		return next_i;
	}
	
	/**
	 * @param i passed and set next square's row position on the game board
	 */
	public void setNextI(int i){
		next_i=i;
	}
	
	/**
	 * @param j passed and set next square's column position on the game board
	 */
	public void setNextJ(int j){
		next_j=j;
	}
	
	/**
	 * @return next square's column position on the game board
	 */
	public int getNextJ(){
		return next_j;
	}
	
	/**
	 * @return square's right neighbor's type 
	 */
	public String getRightNeighbour(){
		return right_neighbour;
	}
	
	/**
	 * @return square's left neighbor's type 
	 */
	public String getLeftNeighbour(){
		return left_neighbour;
	}
	
	/**
	 * @return square's up neighbor's type 
	 */
	public String getUpNeighbour(){
		return up_neighbour;
	}
	
	/**
	 * @return square's down neighbor's type 
	 */
	public String getDownNeighbour(){
		return down_neighbour;
	}
}