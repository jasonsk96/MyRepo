import javax.swing.JOptionPane;

class NewTurn{
	private Player[] sequence;
	private Player winner;
	private int turn_number;
	private Hotel_GUI myGUI;
	private NewGame myGame;
	private int i;
	private boolean diceRolled = false;

	
	NewTurn(Player[] game_sequence, Square[][] game_square, Hotel_GUI theGUI, NewGame theGame, int turnNumber){

		sequence = game_sequence;
		turn_number = 0;
		myGUI = theGUI;
		myGame = theGame;
		turn_number = turnNumber;
	}
	
	/**
	* starts a new round
	*/
	public void startTurn(){
		
		i=0;
		myGUI.enableDiceRoll();
		String whoPlays = sequence[i].getName() + ", it's your turn, please roll the dice";
		JOptionPane.showMessageDialog(null, whoPlays, "Round "+turn_number, JOptionPane.INFORMATION_MESSAGE);	            
	}
	
	/**
	* next player's turn, check for bankruptcies or winner
	*/
	public void nextPlayer(){
		int bankruptcies = 0;
		for(int j=0; j<3; j++){
			if(sequence[j].isBankrupt()) bankruptcies++;
		}
		if(bankruptcies==2){
			diceRolled = false;
			int j;
			for(j=0; j<3; j++){
				if(!(sequence[j].isBankrupt())) break;
			}
			winner = sequence[j];
			myGame.setWinner();
			myGame.start_new_turn();
		}
		else{
			myGUI.disableBankRequest();
			myGUI.disableBuyHotel();
			myGUI.disableBuyEntrance();
			myGUI.disableBuildHotel();
			myGUI.disableCouncil();
			i++;
			if(i<3){
				if(sequence[i].isBankrupt()){
					ignoreBankrupted();
				}
				else{
					myGUI.enableDiceRoll();
					String whoPlays = sequence[i].getName() + ", it's your turn, please roll the dice";
					JOptionPane.showMessageDialog(null, whoPlays, "Round "+turn_number, JOptionPane.INFORMATION_MESSAGE);	            	

				}
			}
			else{
				myGame.start_new_turn();
			}
			
		}
		
	}
	
	/**
	* @return current player's index from table of Players
	*/
	public int getPlayerCounter(){
		return i;
	}
	
	/**
	* @return turn number
	*/
	public int getTurnNumber(){
		return turn_number;
	}
	
	/**
	* @return true if dice is rolled, false otherwise
	*/
	public boolean getDiceRolled(){
		return diceRolled;
	}
	
	/**
	* @param flag passed to set diceRolled to true if dice is rolled, false otherwise
	*/
	public void setDiceRolled(boolean flag){
		diceRolled = flag;
	}
	
	/**
	* @return the winner of the game
	*/
	public Player getWinner(){
		return winner;
	}
	
	/**
	* next player's turn, if current player is bankrupted
	*/
	public void ignoreBankrupted(){
		nextPlayer();
	}
}