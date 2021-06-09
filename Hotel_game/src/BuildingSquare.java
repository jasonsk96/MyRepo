public class BuildingSquare extends Square{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean freeSquare;
	public BuildingSquare(String type, int i, int j){
		super(type, i, j);
		freeSquare = true;
	}
	
	/**
	 * @return false if a square is built, true otherwise
	 */
	public boolean getSquareState(){
		return freeSquare;
	}
	
	/**
	 * called when a square is built
	 * sets freeSquare value to false
	 */
	public void setSquareState(){
		freeSquare=false;
	}
	
}