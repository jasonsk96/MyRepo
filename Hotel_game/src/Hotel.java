import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Hotel{
	private ArrayList<BuildingSquare> hotelSquares = new ArrayList<BuildingSquare>();
	private String hotelName;
	private Player owner;
	private int currentBuildingLevel = 0;
	private int maxBuildingLevel;
	private ArrayList<Entrance> hotelPossibleEntrances = new ArrayList<Entrance>();
	private ArrayList<Entrance> hotelRealEntrances = new ArrayList<Entrance>();
	
	Hotel(Square[][] help_square, String hotelName, ArrayList<Entrance> entrances, int maxLevel){
		this.hotelName = hotelName;
		for(int i=0; i<entrances.size(); i++){
			hotelPossibleEntrances.add(entrances.get(i));
		}
		owner = null;
		maxBuildingLevel = maxLevel;
		for(int i=0; i<12; i++){
			for(int j=0; j<15; j++){
				if(help_square[i][j].getType().equals(hotelName)){
					hotelSquares.add((BuildingSquare) help_square[i][j]);
				}
			}
		}
	}
	
	/**
	 * @return hotel's name
	 */
	public String getHotelName(){
		return hotelName;
	}
	
	/**
	 * @param p passed to set owner of hotel
	 */
	public void boughtBy(Player p){
		owner = p;
		JOptionPane.showMessageDialog(null, owner.getName()+" is the new owner of hotel "+hotelName , "Hotel transaction complete", JOptionPane.INFORMATION_MESSAGE);	            
	}
	
	/**
	 * @return hotel owner
	 */
	public Player getOwner(){
		return owner;
	}
	
	/**
	 * increases current building state
	 */
	public void changeBuildState(){
		currentBuildingLevel++;
	}
	
	/**
	 * @return current building state
	 */
	public int getBuildState(){
		return currentBuildingLevel;
	}
	
	/**
	 * @return maximum building state
	 */
	public int getMaxLevel(){
		return maxBuildingLevel;
	}
	
	/**
	 * @return a list with all possible entrances of a hotel
	 */
	public ArrayList<Entrance> getPossibleEntrances(){
		return hotelPossibleEntrances;
	}
	
	/**
	 * @return a list with all possible building squares
	 */
	public ArrayList<BuildingSquare> getBuildingSquares(){
		return hotelSquares;
	}
	
	/**
	 * @param newEntrance passed and added to entrances bought for a hotel
	 */
	public void setRealEntrances(Entrance newEntrance){
		hotelRealEntrances.add(newEntrance);
	}
	
	/**
	 * @return the list of all entrances bought for a hotel
	 */
	public ArrayList<Entrance> getRealEntrances(){
		return hotelRealEntrances;
	}
	
}