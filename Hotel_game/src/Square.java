import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Square extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int i;
	private int j;
	private String type;
	private JLabel square_label = new JLabel();
	
	public Square(String type, int i, int j){ 
		setLayout(new BorderLayout());
		this.type = type;
		
		setBorder(LineBorder.createGrayLineBorder());
		this.i = i;
		this.j = j;
	}
	
	/**
	 * @return square's row position on the game board
	 */
	public int getI(){
		return i;
	}
	
	/**
	 * @return square's column position on the game board
	 */
	public int getJ(){
		return j;
	}
	
	/**
	 * @return square's type ("E", "H", "B", "C", "S", "F" or number)
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * square label setter
	 * @param type passed and set as label, representing square's type
	 */
	public void setLabel(String type){
		switch (type){
			case "H": 
			setBackground(Color.yellow);
			break;
			
			case "E": 
			setBackground(Color.lightGray);	
			break;
			
			case "F": 
			setBackground(Color.white);
			break;
			
			case "B": 
			setBackground(Color.pink);
			break;
			
			case "S": 
			setBackground(Color.cyan);
			break;
			
			case "C": 
			setBackground(Color.magenta);
			break;
			
			default:
		
			setBackground(Color.white);
		
		}
		square_label.setText(type);
		add(square_label, BorderLayout.NORTH);
	}
	
	/**
	 * changes background color of a building square, when there is a new building
	 * @param player passed so as the method to decide what the square background color will be
	 * (depends on who the owner of building is)
	 */
	public void changeColour(Player aPlayer){
		if(aPlayer.getName().equals("P1")){
			setBackground(Color.blue);
		}
		else if(aPlayer.getName().equals("P2")){
			setBackground(Color.red);
		}
		else setBackground(Color.green);
		square_label.setText(type);
		add(square_label, BorderLayout.NORTH);
		revalidate();
		
	}
}