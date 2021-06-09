import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Player {

	private String name;
	private String colour;
	private JLabel player_label;
	private int moneyHeld;
	private int pos_i;
	private int pos_j;
	private int i;
	private boolean isBankrupt = false;
	private int maxMoney;
	private boolean passedBank = false;
	private boolean passedCouncil = false;
	private Hotel_GUI myGUI;
	private int neighboursFound = 0;
	private ArrayList<Hotel> closeHotels= new ArrayList<Hotel>();
	private ArrayList<Hotel> property = new ArrayList<Hotel>();
	private NewTurn nt;
	private ArrayList<Entrance> myEntrances = new ArrayList<Entrance>();
	private int hotelNumber;
	private File[] hotelList;
	private File[] subfolders;
	private int folderNumber;
	private boolean almostBankrupt = false;
	private List<String> niceList;
	private int dice;
	private Square[][] help_square;
	private MovementSquare next;
	private NewGame myGame;
	
	/**
	 * Constructor of Player class
	 * @param name passed to be assigned as a player's name
	 * @param colour passed to be assigned as a player's label's color
	 * @param start_i passed to be assigned as player's initial row position
	 * @param start_j passed to be assigned as player's initial column position
	 * @param thisGUI passed to be assigned as the GUI object of the application
	 * @param hotelNumber passed to be assigned as the number of available hotels in this game
	 * @param hotelList passed to be assigned as the list of hotel files in this game
	 * @param subfolders passed to be assigned as the list of folders with hotel files for each game
	 * @param folderNumber passed to be assigned as the number of folders with hotel files
	 * moneyHeld and maxMoney set to 12000
	 * 
	 * all other fields have default values at the start of the game
	 */
	public Player(String playerName, String color, int start_i, int start_j, Hotel_GUI thisGUI, int hotelNumber, File[] hotelList, File[] subfolders, int folderNumber, Square[][] game_square, NewGame theGame) {
		name = playerName;
		colour = color;
		player_label = new JLabel("<html><font color='"+this.colour+"'>"+this.name+"</font></html>");
		moneyHeld = 12000;
		maxMoney = 12000;
		pos_i = start_i;
		pos_j = start_j;
		myGUI = thisGUI;
		this.hotelNumber = hotelNumber;
		this.hotelList = hotelList;
		this.subfolders = subfolders;
		this.folderNumber= folderNumber;
		help_square = game_square;
		myGame = theGame;
	}
	
	/**
	 * @return name of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return current player's label
	 */
	public JLabel getLabel(){
		return player_label;
	}
	
	/**
	 * @return current player's balance
	 */
	public int getMoneyHeld() {
		return moneyHeld;
	}

	/**
	 * moneyHeld setter
	 * @param update is added to the current balance
	 */
	public void setMoneyHeld(int update) {
		this.moneyHeld += update;
		setMaxMoney();
	}
	
	/**
	 * @return current player's max money
	 */
	public int getMaxMoney(){
		return maxMoney;
	}
	
	/**
	 * maxMoney setter
	 * sets maxMoney equal to moneyHeld, if current moneyHeld is greater than current maxMoney 
	 */
	public void setMaxMoney(){
		if (this.moneyHeld > this.maxMoney) this.maxMoney = this.moneyHeld;
	}
	
	/**
	 * @return player's current row position on the game board
	 */
	public int getPositionI() {
		return pos_i;
	}
	
	/**
	 * @return player's current column position on the game board
	 */
	public int getPositionJ() {
		return pos_j;
	}
	
	/**
	 * sets player's current position on the game board
	 * @param dice is passed and defines the square that the player should move to 
	 * checks if player passed bank or council, and if the final square is occupied by another player,
	 * so as current player to move to next available square
	 * otherPlayer variable of final square is set to true 
	 */
	public void setPosition(int dice) {
		MovementSquare tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
		tmpSquare.someoneHere(false);
		//tmpSquare.someoneGone();
		while (dice!=0){
			pos_i = tmpSquare.getNextI();
			pos_j = tmpSquare.getNextJ();
			tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
			if (tmpSquare.getType().equals("B")){
				passedBank = true;
			}
			if (tmpSquare.getType().equals("C")){
				passedCouncil = true;
			}
			dice--;
		}
		for(int i=0; i<2; i++){
			if(tmpSquare.whoIsHere()){
				pos_i = tmpSquare.getNextI();
				pos_j = tmpSquare.getNextJ();
				tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
				if (tmpSquare.getType().equals("B")){
					passedBank = true;
				}
				if (tmpSquare.getType().equals("C")){
					passedCouncil = true;
				}
			}
			else{
				break;
			}
		}
		
		tmpSquare.someoneHere(true);
	}
	
	/**
	 * @return true, if player is bankrupt, false otherwise
	 */
	public boolean isBankrupt() {
		return isBankrupt;
	}
	
	/**
	 * @return true, if player is not bankrupt, because he just passed the bank
	 */
	public boolean getAlmostBankrupt(){
		return almostBankrupt;
	}
	
	/**
	 * @param isBankrupt is passed (true), if the Player is to be bankrupt
	 */
	public void setBankrupt(boolean isBankrupt) {
		this.isBankrupt = isBankrupt;
	}
	
	/*
	calls enableBankRequest() if current player just passed the bank
	*/
	private void checkBank(){
		if (passedBank == true){
			passedBank = false;
			JOptionPane.showMessageDialog (null, this.name +", you may request money from the bank","You just passed the bank", JOptionPane.INFORMATION_MESSAGE);
			myGUI.enableBankRequest();			
		}
	}
	
	/*
	calls enableBuyHotel() if current player's square is a buy-hotel-square
	*/
	private void checkHotel(){
		MovementSquare tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
		if (tmpSquare.getType().equals("H")){
			myGUI.enableBuyHotel();
		}
	}
	
	/*
	 calls enableCouncil() if current player just passed the council
	*/
	private void checkCouncil(){
		if (passedCouncil == true){
			passedCouncil = false;
			JOptionPane.showMessageDialog (null, this.name +", you may request buying an entrance from the council","You just passed the council", JOptionPane.INFORMATION_MESSAGE);
			myGUI.enableCouncil();			
		}
	}
	
	/* checks if current player is on another player's entrance-square, and if so, decides 
	if player must pay rent to hotel owner, and how much he should pay 
	*/
	private void checkEntrance(){
		MovementSquare tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
		nt = myGUI.getGame().get_turn();
		if (tmpSquare.getType().equals("E")){
			
			
			myEntrances = myGUI.getGame().getEntrances();
			for(i=0; i<myEntrances.size(); i++){
				if((myEntrances.get(i).getI()==pos_i) && (myEntrances.get(i).getJ()==pos_j)){
					break;
				}
			}
			
			if((myEntrances.get(i).getHotel()!=null) && (myEntrances.get(i).getHotel().getBuildState()!=0)){
				Player owner = myEntrances.get(i).getHotel().getOwner();
				if(!(owner.getName().equals(name)) && !(owner.isBankrupt())){
					
					myGUI.enablePayRent();
					nt.setDiceRolled(false);
					JOptionPane.showMessageDialog(null, "How long will you stay at hotel "+myEntrances.get(i).getHotel().getHotelName()+"?\nPlease roll the dice!" , "Rent bill", JOptionPane.INFORMATION_MESSAGE);	            
					
				}
				else myGUI.enableBuyEntrance();
				restMove();
			}
			else myGUI.enableBuyEntrance();
			restMove();

		}
		else restMove();
	}
	
	/**
	 * @return hotels that can be bought (hotels with no buildings), neighboring to current player's square 
	 */
	public ArrayList<Hotel> getCloseHotels(ArrayList<Hotel> myHotels){
		MovementSquare tmpSquare = (MovementSquare) help_square[pos_i][pos_j];
		String leftNeighbour = tmpSquare.getLeftNeighbour();
		String rightNeighbour = tmpSquare.getRightNeighbour();
		String upNeighbour = tmpSquare.getUpNeighbour();
		String downNeighbour = tmpSquare.getDownNeighbour();
		for(int i=0; i<myHotels.size(); i++){
			Hotel newHotel = myHotels.get(i);
			if(leftNeighbour.equals(newHotel.getHotelName())){
				if(newHotel.getBuildState()==0) closeHotels.add(newHotel);
				neighboursFound++;
				if (neighboursFound==2) break;
				continue;
			}
			if(rightNeighbour.equals(newHotel.getHotelName())){
				if(newHotel.getBuildState()==0) closeHotels.add(newHotel);
				neighboursFound++;
				if (neighboursFound==2) break;
				continue;
			}
			if(upNeighbour.equals(newHotel.getHotelName())){
				if(newHotel.getBuildState()==0) closeHotels.add(newHotel);
				neighboursFound++;
				if (neighboursFound==2) break;
				continue;
			}
			if(downNeighbour.equals(newHotel.getHotelName())){
				if(newHotel.getBuildState()==0) closeHotels.add(newHotel);
				neighboursFound++;
				if (neighboursFound==2) break;
				continue;
			}
		}
		return closeHotels;
	}
	
	/**
	 * removes neighboring hotels from closeHotels list
	 */
	public void removeHotels(){
		closeHotels.clear();
	}
	
	/**
	 * @param h is passed and added to player's property list
	 */
	public void setProperty(Hotel h){
		property.add(h);
	}
	
	/**
	 * @param h is passed and removed from player's property list
	 */
	public void updateProperty(int h){
		property.remove(h);
	}
	
	/**
	 * @return player's property
	 */
	public ArrayList<Hotel> getProperty(){
		return property;
	}
	
	/**
	 * @return hotels with not max building level from player's property
	 */
	public ArrayList<Hotel> getHotelsToBuild(){
		ArrayList<Hotel> hotelsToBuild = new ArrayList<Hotel>();
		for(int i=0; i<property.size(); i++){
			Hotel myHotel = property.get(i);
			for(int j=0; j<myHotel.getBuildingSquares().size(); j++){
				if(myHotel.getBuildState()<myHotel.getMaxLevel()){
					hotelsToBuild.add(myHotel);
					break;
				}
			}
		}
		return hotelsToBuild;
	}
	
	/**
	 * @return hotels with at least one entrance 
	 * not owned by any other hotel from player's property
	 */
	public ArrayList<Hotel> checkForEntrance(){
		ArrayList<Hotel> availableHotels = new ArrayList<Hotel>();
		for(int i=0; i<property.size(); i++){
			Hotel myHotel = property.get(i);
			for(int j=0; j<myHotel.getPossibleEntrances().size(); j++){
				if(myHotel.getPossibleEntrances().get(j).getHotel()==null){
					availableHotels.add(myHotel);
					break;
				}
			}
		}
		return availableHotels;
	}
	
	/**
	 * @return all entrances bought by current player
	 */
	public int findTotalEntrances(){
		int totalEntrances = 0;
		for(int i=0; i<property.size(); i++){
			for(int j=0; j<property.get(i).getRealEntrances().size(); j++){
				totalEntrances++;					
			}
		}
		return totalEntrances;
	}
	
	/**
	 * @param dice passed and represents how many days is current going to
	 * stay in hotel
	 * then the amount of rent that must be paid is calculated
	 */
	public void calculateRentCost(int dice){
			int buildLevel;
			buildLevel = myEntrances.get(i).getHotel().getBuildState();
			Player owner = myEntrances.get(i).getHotel().getOwner();
			int j;
			for(j=0; j<hotelNumber; j++){
				if(hotelList[j].getName().equals(myEntrances.get(i).getHotel().getHotelName()+".txt")){
					Scanner s;
					try {
						s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[j].getName())));
						int k;
						for(k=0; k<3; k++){
							s.nextLine();
						}
						for(k=0; k<buildLevel-1; k++){
							s.nextLine();
						}
						niceList = Arrays.asList(s.nextLine().split(","));
						int rentCost = dice*Integer.parseInt(niceList.get(1));
						JOptionPane.showMessageDialog(null, "Pay "+rentCost+" to "+owner.getName()+", owner of hotel "+myEntrances.get(i).getHotel().getHotelName() , "Rent payment", JOptionPane.INFORMATION_MESSAGE);	            

						if(moneyHeld>rentCost){
							moneyHeld-=rentCost;
							owner.setMoneyHeld(rentCost);
							myGUI.enableBuyEntrance();
							restMove();
						}
						else{
							if((passedBank) && (moneyHeld+1000>rentCost)){
								almostBankrupt = true;
								JOptionPane.showMessageDialog(null, "You must request money from Bank" , "Rent payment", JOptionPane.INFORMATION_MESSAGE);	            
								passedBank=false;
								myGUI.enableBankRequest();
							}
							else{
								owner.setMoneyHeld(moneyHeld);
								moneyHeld=0;
								isBankrupt = true;
								JOptionPane.showMessageDialog(null, name +", you 've gone bankrupt!\nGame Over!" , "Rent payment", JOptionPane.INFORMATION_MESSAGE);	            
								restMove();
							}
						}
						s.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			
			nt.setDiceRolled(true);
		
	}
		
	/**
	 * current player's and hotel owners balances updated
	 * player avoids bankruptcy
	 */
	public void avoidBunkrupt(){
		almostBankrupt = false;
		Player owner = myEntrances.get(i).getHotel().getOwner();
		moneyHeld-=Integer.parseInt(niceList.get(1));
		owner.setMoneyHeld(Integer.parseInt(niceList.get(1)));
		myGUI.enableBuyEntrance();
		restMove();
	}
	
	/**
	 * update current player's position on game board
	 */
	public void playerMove(){
		dice = myGUI.getDice();
		MovementSquare previous = (MovementSquare) help_square[pos_i][pos_j];
		setPosition(dice);
		next = (MovementSquare) help_square[pos_i][pos_j];
		previous.removeLabel(this);
		previous.revalidate();
		previous.repaint();
		if(name.equals("P1")) next.setPlayer1Label(player_label);
		else if(name.equals("P2")) next.setPlayer2Label(player_label);
		else next.setPlayer3Label(player_label);
		next.revalidate();
		next.repaint();
		checkEntrance();
	}
	
	private void restMove(){
		if(isBankrupt){
			next.removeLabel(this);
			next.revalidate();
			next.repaint();
			nt = myGame.get_turn();
			nt.nextPlayer();
		}
		else{
			checkBank();
			checkHotel();
			checkCouncil();
		}
		
	}
}