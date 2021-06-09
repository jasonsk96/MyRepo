import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class NewGame{
	private Player[] sequence;
	private JPanel board_panel;
	private static Square[][] help_square;
	private Hotel_GUI myGUI;
	private static int start_i;
	private static int start_j;
	private NewTurn nt;
	private static int hotelNumber;
	private static File[] hotelList;
	private static File[] subfolders;
	private static int folderNumber;
	private int turn_number = 0;
	private Timer t;
	private Timer p1MoneyCounter;
	private Timer p2MoneyCounter;
	private Timer p3MoneyCounter;
	private Timer hotelAvCounter;
	private boolean winner = false;
	private static ArrayList<Entrance> hotelEntrances = new ArrayList<Entrance>();
	
	public NewGame(JPanel board_panel, Square[][] old_help_square){
		this.board_panel = board_panel;
		help_square = old_help_square;
		myGUI = Game.getGUI();
		myGUI.setTime(0);
		t = myGUI.getTimer();
		t.start();
		p1MoneyCounter = myGUI.getP1Timer();
		p1MoneyCounter.start();
		p2MoneyCounter = myGUI.getP2Timer();
		p2MoneyCounter.start();
		p3MoneyCounter = myGUI.getP3Timer();
		p3MoneyCounter.start();
		hotelAvCounter = myGUI.getHotelTimer();
		hotelAvCounter.start();
	}
	
	private void decideTurnSequence(){
		int first, second, third;
		sequence = new Player[3];
		first = (int) ( Math.random() * 3 + 1);
		setSequence(first,0);
        do{
        	second = (int) ( Math.random() * 3 + 1);
        }while(second==first);
        setSequence(second,1);
        do{
        	third = (int) ( Math.random() * 3 + 1);
        }while((third==first) || (third==second));
        setSequence(third,2);
	}
	
	private void setSequence(int p, int i){
		if(p==1) sequence[i] = new Player("P1", "blue", start_i, start_j, myGUI, hotelNumber, hotelList, subfolders, folderNumber, help_square, this);
		else if (p==2) sequence[i] = new Player("P2", "red", start_i, start_j, myGUI, hotelNumber, hotelList, subfolders, folderNumber, help_square, this);
		else sequence[i] = new Player("P3", "green", start_i, start_j, myGUI, hotelNumber, hotelList, subfolders, folderNumber, help_square, this);
	}
	
	public void initialization(){
		nt = null;
		hotelEntrances.clear();
		for(int i=0; i<12; i++){
           	for(int j=0; j<15; j++){
           		board_panel.remove(help_square[i][j]);
           		board_panel.revalidate();
           	}
		}
		try {
				initialize();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
		myGUI.setHotelList(hotelList, hotelNumber, subfolders, folderNumber, hotelEntrances);
	}
	
	/**
	* initializes game board and decides turn sequence when a new game starts
	*/
	public void new_game(){
		find_next_and_start(help_square);
		MovementSquare start = (MovementSquare) help_square[start_i][start_j];
		decideTurnSequence();
		for(int i=0; i<3; i++){
			if (sequence[i].getName().equals("P1")) start.setPlayer1Label(sequence[i].getLabel());
			else if (sequence[i].getName().equals("P2")) start.setPlayer2Label(sequence[i].getLabel());
			else start.setPlayer3Label(sequence[i].getLabel());
		}
 	
 	
		help_square[start_i][start_j] = start;
		for(int i=0; i<12; i++){
			for(int j=0; j<15; j++){
				help_square[i][j].setLabel(help_square[i][j].getType());
				board_panel.add(help_square[i][j]);
			}
		}
		board_panel.revalidate();
		myGUI.add(board_panel, BorderLayout.CENTER);
		myGUI.revalidate();

		String infoBox = "first: " + sequence[0].getName() + ", second: " + sequence[1].getName() + ", third: " + sequence[2].getName();
		JOptionPane.showMessageDialog(null, infoBox, "Turn sequence infos", JOptionPane.INFORMATION_MESSAGE);	            
     
		
		
		start_new_turn();
	}
	
	/**
	* starts a new turn, calling new_turn()
	*/
	public void start_new_turn(){
		if(!winner){
			new_turn();
		}
		else{
			myGUI.finishGame();
			gameOver();
		}
	}
	
	/**
	* creating a new NewTurn object for the new turn
	*/
	private void new_turn(){
		nt = null;
		turn_number++;
		nt = new NewTurn(sequence, help_square, myGUI, this, turn_number);
		nt.startTurn();
	}
	
	/**
	* @return current turn
	*/
	public NewTurn get_turn(){
		return nt;
	}
	
	private static void initialize() throws IOException {
	 	final String[][] temp_table;
		temp_table = reading();
		for (int i=0; i<12; i++){
			for (int j=0; j<15; j++){
				if (temp_table[i][j].equals("B") || temp_table[i][j].equals("H") || temp_table[i][j].equals("E") || temp_table[i][j].equals("S") || temp_table[i][j].equals("C")){
					if((i==0) && (j==0)) help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], null, null, temp_table[i+1][j], i, j);
					else if((i==0) && (j==14)) help_square[i][j] = new MovementSquare(temp_table[i][j], null, temp_table[i][j-1], null, temp_table[i+1][j], i, j);
					else if(i==0) help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], temp_table[i][j-1], null, temp_table[i+1][j], i, j);
					else if((i==11) && (j==0)) help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], null, temp_table[i-1][j], null, i, j);
					else if(j==0) help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], null, temp_table[i-1][j], temp_table[i+1][j], i, j);
					else if((i==11) && (j==14)) help_square[i][j] = new MovementSquare(temp_table[i][j], null, temp_table[i][j-1], temp_table[i-1][j], null, i, j);
					else if(i==11) help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], temp_table[i][j-1], temp_table[i-1][j], null, i, j);
					else if(j==14) help_square[i][j] = new MovementSquare(temp_table[i][j], null, temp_table[i][j-1], temp_table[i-1][j], temp_table[i+1][j], i, j);
					else help_square[i][j] = new MovementSquare(temp_table[i][j], temp_table[i][j+1], temp_table[i][j-1], temp_table[i-1][j], temp_table[i+1][j], i, j);
					if(temp_table[i][j].equals("E")){
						hotelEntrances.add(new Entrance(help_square[i][j]));
					}
				}
				else if (temp_table[i][j].equals("F")){
					help_square[i][j] = new FreeSquare(temp_table[i][j], i, j);		
				}
				else{
					help_square[i][j] = new BuildingSquare(temp_table[i][j], i, j);
				}
			}
		}
		
	}

private static String[][] reading() throws IOException{
				 final String[][] myChars = new String[12][15];
		    	 Scanner s = null;
		    	 subfolders = countDirectories();
		    	 int tmp = (int) ( Math.random() * subfolders.length + 1);
		    	 File[] hotelTmp = null;
		    	 
		    	 
		         try {
		        	 for(folderNumber=0; folderNumber<subfolders.length; folderNumber++){
		        		 if(tmp==folderNumber+1){
		        			 hotelTmp = countFiles(subfolders[folderNumber]);
		        			 hotelNumber = hotelTmp.length-1;
		        			 hotelList = new File[hotelNumber];
		        			 hotelList = hotelTmp;
		        			 break;	 
		        		 }
		        	 }
		        	 s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelTmp[hotelNumber].getName())));
		        	 int i=0;
		             int j=0;
		             while (s.hasNext())
		             {
		                String str = s.next(); 
		                List<String> niceList = Arrays.asList(str.split(","));
		                for (int k = 0; k < 15; k++){
		                	myChars[i][j] = niceList.get(k);
		                	j++;
		                }
		                j=0;
		                i++;
		             }
		        }
		        finally {
		       	 if (s != null) {
		                s.close();
		            }
		        }
		        return myChars;
		            
			}
					
		private static void find_next_and_start(Square[][] findThem){
			int i;
			int j=0;
			int help_i, help_j;
			String type = null;
			MovementSquare previous;
			MovementSquare next;
			for(i=0; i<12; i++){
				for(j=0; j<15; j++){
					type = help_square[i][j].getType();
					if(type.equalsIgnoreCase("H") || type.equalsIgnoreCase("E") || type.equalsIgnoreCase("S") || type.equalsIgnoreCase("C") || type.equalsIgnoreCase("B")) break;
				}
				if(type.equalsIgnoreCase("H") || type.equalsIgnoreCase("E") || type.equalsIgnoreCase("S") || type.equalsIgnoreCase("C") || type.equalsIgnoreCase("B")) break;
			}
			previous = (MovementSquare) help_square[i][j];
			help_i = i; help_j = j;  
			if(type.equals("S")){
				start_i = i; 
				start_j = j;
			}
			j++;
			previous.setNextI(i);
			previous.setNextJ(j);
			help_square[i][j-1] = previous;
			next = (MovementSquare) help_square[i][j];
			
			while((i!=help_i) || (j!=help_j)){
				
				String next_neighbour = next.getLeftNeighbour();
				if(next_neighbour.equals("S")){
					start_i = i; 
					start_j = j-1;
				}
				if(next_neighbour.equals("E") || next_neighbour.equals("H") || next_neighbour.equals("S") || next_neighbour.equals("B") || next_neighbour.equals("C")){
					if ((i!=previous.getI()) || (j-1!=previous.getJ())){
						previous = next;
						j--;
						previous.setNextI(i);
						previous.setNextJ(j);
						help_square[i][j+1] = previous;
						next = (MovementSquare) help_square[i][j];

						continue;
					}
				}
				next_neighbour = next.getRightNeighbour();
				if(next_neighbour.equals("S")){
					start_i = i; 
					start_j = j+1;
				}
				if(next_neighbour.equals("E") || next_neighbour.equals("H") || next_neighbour.equals("S") || next_neighbour.equals("B") || next_neighbour.equals("C")){
					if ((i!=previous.getI()) || (j+1!=previous.getJ())){
						previous = next;
						j++;
						previous.setNextI(i);
						previous.setNextJ(j);
						help_square[i][j-1] = previous;
						next = (MovementSquare) help_square[i][j];

						continue;
					}
				}
				next_neighbour = next.getUpNeighbour();
				if(next_neighbour.equals("S")){
					start_i = i-1; 
					start_j = j;
				}
				if(next_neighbour.equals("E") || next_neighbour.equals("H") || next_neighbour.equals("S") || next_neighbour.equals("B") || next_neighbour.equals("C")){
					if ((i-1!=previous.getI()) || (j!=previous.getJ())){
						previous = next;
						i--;
						previous.setNextI(i);
						previous.setNextJ(j);
						help_square[i+1][j] = previous;
						next = (MovementSquare) help_square[i][j];
						continue;
					}
				}
				next_neighbour = next.getDownNeighbour();
				if(next_neighbour.equals("S")){
					start_i = i+1; 
					start_j = j;
				}
				if(next_neighbour.equals("E") || next_neighbour.equals("H") || next_neighbour.equals("S") || next_neighbour.equals("B") || next_neighbour.equals("C")){
					if ((i+1!=previous.getI()) || (j!=previous.getJ())){
						previous = next;
						i++;
						previous.setNextI(i);
						previous.setNextJ(j);
						help_square[i-1][j] = previous;
						next = (MovementSquare) help_square[i][j];
						continue;
					}
				}
			}
		}
		
		private static File[] countDirectories(){
			File file = new File("boards");
			File[] files = file.listFiles(new FileFilter() {
			    @Override
			    public boolean accept(File f) {
			        return f.isDirectory();
			    }
			});
			return files;
		}
		
		private static File[] countFiles(File myFolder){
			File file = new File("boards\\"+myFolder.getName());
			File[] files = file.listFiles(new FileFilter() {
			    @Override
			    public boolean accept(File f) {
			        return f.isFile();
			    }
			});
			return files;
		}
		
		/**
		* @return table of Players
		*/
		public Player[] getPlayers(){
			return sequence;
		}
		
		/**
		* @return list of all entrances
		*/
		public ArrayList<Entrance> getEntrances(){
			return hotelEntrances;
		}
		
		/**
		* sets winner value to true when a game is over and we have a winner
		*/
		public void setWinner(){
			winner = true;
		}
		
		private void gameOver(){
			JOptionPane.showMessageDialog(null, "Everyone else has gone bankrupt!\n"+nt.getWinner().getName()+", you are the great winner" , "Congrats", JOptionPane.INFORMATION_MESSAGE);	            	
			JOptionPane.showMessageDialog(null, "Game Over!\nThanks for playing!" , "That's all!", JOptionPane.INFORMATION_MESSAGE);	            	
		}
}