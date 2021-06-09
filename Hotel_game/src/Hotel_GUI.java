import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

public class Hotel_GUI extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel board_panel  = new JPanel(new GridLayout(12,15));
	private static Square[][] help_square = new Square[12][15];
	private NewGame myGame;
	private NewTurn nt;
	private static File[] subfolders;
	static int folderNumber;
	private ArrayList<Hotel> hotels = new ArrayList<Hotel>();
	private boolean gameStarted = false;
	private  ArrayList<String> name = new ArrayList<String>();
	private ArrayList<JMenuItem> hotelItems = new ArrayList<JMenuItem>();
	private ArrayList<JMenuItem> profitItems = new ArrayList<JMenuItem>();
	private ArrayList<JMenuItem> entranceItems = new ArrayList<JMenuItem>();
	private ArrayList<JMenuItem> gameHotelItems = new ArrayList<JMenuItem>();
	private int dice=0;
	private ArrayList<Integer> hotelNames = new ArrayList<Integer>();
	final JMenu popupCards = new JMenu("Cards");
	final JMenu playerProfits = new JMenu ("Profits");
	final JMenu playerEntrances = new JMenu ("Entrances");
	private JMenu gameHotels = new JMenu ("Hotels");
	final JButton button2;
	ArrayList<Hotel> availableHotels = new ArrayList<Hotel>();
	ArrayList<Hotel> availableBuiltHotels = new ArrayList<Hotel>();
	ArrayList<Entrance> availableEntrances = new ArrayList<Entrance>();
	ArrayList<Entrance> availableBuiltEntrances = new ArrayList<Entrance>();
	ArrayList<Hotel> hotelsToBuild = new ArrayList<Hotel>();
	ArrayList<BuildingSquare> availableSquares = new ArrayList<BuildingSquare>();
	final JButton button3;
	final JButton button4;
	final JButton button5;
	final JButton button6;
	final JButton button7;
	final JButton button8;
	
	
	
	private JLabel p1Money = new JLabel();
	final Timer p1MoneyCounter = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	Player[] tmp_sequence = myGame.getPlayers();
        	for(int i=0; i<3; i++){
        		if (tmp_sequence[i].getName().equals("P1")){
            		p1Money.setText(format(tmp_sequence[i].getMoneyHeld()));
        		}
        	}
        }
    });
	
	private JLabel p2Money = new JLabel();
	final Timer p2MoneyCounter = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	Player[] tmp_sequence = myGame.getPlayers();
        	for(int i=0; i<3; i++){
        		if (tmp_sequence[i].getName().equals("P2")){
            		p2Money.setText(format(tmp_sequence[i].getMoneyHeld()));
        		}
        	}
        }
    });
	
	private JLabel p3Money = new JLabel();
	final Timer p3MoneyCounter = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	Player[] tmp_sequence = myGame.getPlayers();
        	for(int i=0; i<3; i++){
        		if (tmp_sequence[i].getName().equals("P3")){
            		p3Money.setText(format(tmp_sequence[i].getMoneyHeld()));           		
        		}
        	}
        }
    });
	private JLabel hotelAv = new JLabel();
	final Timer hotelAvCounter = new Timer(100, new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			if (hotels.size()>0){
				int hotelsAvailable = 0;
				for(int i=0; i<hotels.size(); i++){
					if(hotels.get(i).getBuildState()==0){
						hotelsAvailable++;
					}
				}
				hotelAv.setText(format(hotelsAvailable));
			}
				
		}
	});
	private boolean diceRoll = false;
	private boolean payRent = false;
	private boolean bankRequest = false;
	private boolean buyHotel = false;
	private boolean buyEntrance = false;
	private boolean buildHotel = false;
	private boolean councilRequest = false;
	private static File[] hotelList;
	private static int hotelNumber;
	private int time;
	private JLabel timer= new JLabel("00:00");
	final Timer t = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            time++;
            timer.setText(format(time / 3600) + ":" + format((time / 60) % 60));
        }
    });
	
	/**
	 * Constructor of Hotel_GUI class
	 * @throws IOException
	 * game's UI initialization
	 */
	Hotel_GUI() throws IOException {
    	setLayout(new BorderLayout()); 
    	
    	initialize();
                
        for(int i=0; i<12; i++){
        	for(int j=0; j<15; j++){
        		help_square[i][j].setLabel(help_square[i][j].getType());
        		board_panel.add(help_square[i][j]);
        	}
        }
    	
    	final JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	
    	createCards();
    	for(int i=0; i<hotelNumber; i++){
    		setPopUp(i);
    	}
    	
    	
		final JPopupMenu popup1 = new JPopupMenu();
	        popup1.add(new JMenuItem(new AbstractAction("Start") {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	            	popupCards.removeAll();	            		
	            	hotelItems.clear();
	            	
	            	button2.setEnabled(true);
			        button3.setEnabled(true);
			        button4.setEnabled(true);
			        button5.setEnabled(true);
			        button6.setEnabled(true);
			        button7.setEnabled(true);
			        button8.setEnabled(true);
	            	
			        myGame = null;
	            	myGame = new NewGame(board_panel, help_square);
	            	myGame.initialization();
	            	try {
						createCards();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	for(int i=0; i<hotelNumber; i++){
	            		setPopUp(i);
	            	}
	            	
	            	for(int i=0; i<3; i++){
	            		setProfitsPopUp(i);
	            		setEntrancesPopUp(i);
	            	}
	            	gameStarted = true;
	            	myGame.new_game();
	            }
	            		        }));
	        popup1.add(new JMenuItem(new AbstractAction("Stop") {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	               t.stop();
	               button2.setEnabled(false);
			        button3.setEnabled(false);
			        button4.setEnabled(false);
			        button5.setEnabled(false);
			        button6.setEnabled(false);
			        button7.setEnabled(false);
			        button8.setEnabled(false);
	            }
	        }));
	        popup1.add(popupCards);
	        popup1.add(new JMenuItem(new AbstractAction("Exit") {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	               System.exit(0);
	            }
	        }));
	        final JButton button1 = new JButton("Game");
	        button1.addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	                popup1.show(e.getComponent(), e.getX(), e.getY());
	            }
	        });
	        toolBar.add(button1);
	        
	        final JPopupMenu popup2 = new JPopupMenu();
	        popup2.add(gameHotels);
	        popup2.add(playerEntrances);
	        popup2.add(playerProfits);
	        
	        button2 = new JButton("Statistics");
	        button2.addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	                popup2.show(e.getComponent(), e.getX(), e.getY());
	            }
	        });
	        toolBar.add(button2);
    	
		JPanel infos1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infos1.add(new JLabel("Player1: "));
		infos1.add(p1Money);
		infos1.add(new JLabel("     "));
		infos1.add(new JLabel("Player2: "));
		infos1.add(p2Money);
		infos1.add(new JLabel("     "));
		infos1.add(new JLabel("Player3: "));
		infos1.add(p3Money);
		JPanel infos2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		infos2.add(new JLabel("Available Hotels: "));
		infos2.add(hotelAv);
		infos2.add(new JLabel("     "));
		infos2.add(new JLabel("Total Time: "));
		infos2.add(timer);
		JPanel infoPanel = new JPanel(new GridLayout(1,2));
		infoPanel.add(infos1);
		infoPanel.add(infos2);
		JPanel twoPanels = new JPanel(new GridLayout(2,1));
		twoPanels.add(toolBar);
		twoPanels.add(infoPanel);
		add(twoPanels, BorderLayout.NORTH);
		
		
		button3 = new JButton("Roll dice & result");
		button3.addActionListener(this);
        button4 = new JButton("Request building & result");
        button4.addActionListener(this);
        button5 = new JButton("Buy hotel");
        button5.addActionListener(this);
        button6 = new JButton("Buy entrance");
        button6.addActionListener(this);
        button7 = new JButton("Request +1000 from bank");
        button7.addActionListener(this);
        button8 = new JButton("Finish round");
        button8.addActionListener(this);
        
        final JPanel options = new JPanel(new GridLayout(6,1));
        options.add(button3);
        options.add(button4);
        options.add(button5);
        options.add(button6);
        options.add(button7);
        options.add(button8);
        add(options, BorderLayout.EAST);
        
        add(board_panel, BorderLayout.CENTER);
        
        
        
        
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
	/**
	* @return dice result
	*/
	public int getDice(){
		return dice;
	}	
	
	/**
	* enables dice rolling
	*/
	public void enableDiceRoll(){
		diceRoll = true;
	}		
			
	/**
	* enables rent payment
	*/
	public void enablePayRent(){
		payRent = true;
	}
	
	/**
	* enables bank request
	*/
	public void enableBankRequest(){
		bankRequest = true;
	}
	
	/**
	* disables bank request
	*/
	public void disableBankRequest(){
		bankRequest = false;
	}
	
	/**
	* disables council request
	*/
	public void disableCouncil(){
		councilRequest = false;
	}
	
	/**
	* enables hotel purchase
	*/
	public void enableBuyHotel(){
		buyHotel = true;
	}
	
	/**
	* disables hotel purchase
	*/
	public void disableBuyHotel(){
		buyHotel = false;
	}
	
	/**
	* enables council request
	*/
	public void enableCouncil(){
		councilRequest = true;
	}
	
	/**
	* enables entrance purchase and hotel building request
	*/
	public void enableBuyEntrance(){
		buyEntrance = true;
		buildHotel = true;
	}
	
	/**
	* disables entrance purchase
	*/
	public void disableBuyEntrance(){
		buyEntrance = false;
	}
	
	/**
	* disables hotel building request
	*/
	public void disableBuildHotel(){
		buildHotel = false;
	}		

			@Override
			public void actionPerformed(ActionEvent ev) {
				if (button3.equals(ev.getSource())){
					if (diceRoll){
						dice = (int) ( Math.random() * 6 + 1);
						diceRoll = false;
						nt = myGame.get_turn();
						JOptionPane.showMessageDialog(null, "Dice result: "+dice, "Round "+nt.getTurnNumber(), JOptionPane.INFORMATION_MESSAGE);	           
						Player[] tmp_sequence = myGame.getPlayers();
						Player myPlayer = tmp_sequence[nt.getPlayerCounter()];
						nt.setDiceRolled(true);
						myPlayer.playerMove();
					}
					else if(payRent){
						dice = (int) ( Math.random() * 6 + 1);
						payRent = false;
						Player[] tmp_sequence = myGame.getPlayers();
						nt = myGame.get_turn();
						JOptionPane.showMessageDialog(null, "You 're gonna stay "+dice+" days", "Round "+nt.getTurnNumber(), JOptionPane.INFORMATION_MESSAGE);	           
						Player myPlayer = tmp_sequence[nt.getPlayerCounter()];
						myPlayer.calculateRentCost(dice);
					}
				}
				else if (button4.equals(ev.getSource())){
					if (buildHotel){
						find_hotels_to_build();
						if(hotelsToBuild.size()==0){
							JOptionPane.showMessageDialog(null, "Sorry, no available hotels to build", "Round " + nt.getTurnNumber(), JOptionPane.INFORMATION_MESSAGE);	            
							buildHotel = false;
						}
						else{
							JButton[] hotelButtons = new JButton[hotelsToBuild.size()+1];
							find_available_building_squares();
							for(int i=0; i<hotelsToBuild.size(); i++){
								hotelButtons[i] = createButton(hotelButtons, i, hotelsToBuild.get(i).getBuildState());
							}
							hotelButtons[hotelsToBuild.size()] = createQuitBuildButton(hotelButtons, hotelsToBuild.size());	
							JOptionPane.showOptionDialog(null, "Choose one of your hotels:", "Round " + nt.getTurnNumber(), 
							        JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, hotelButtons, null);
							
						}
					}
				}
				else if (button5.equals(ev.getSource())){
					if (buyHotel){
						Player[] tmp_sequence = myGame.getPlayers();
						nt = myGame.get_turn();
						Player myPlayer = tmp_sequence[nt.getPlayerCounter()];
						ArrayList<Hotel> closeHotels = myPlayer.getCloseHotels(hotels);	
						if (closeHotels.size()==0){
							JOptionPane.showMessageDialog(null, "Sorry, no available hotels to buy", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);	            
							buyHotel = false;
						}
						else if (closeHotels.size()==1){
							Object[] options1 = { "Hotel " + closeHotels.get(0).getHotelName(), "Quit" };
							int result = JOptionPane.showOptionDialog(null, "Choose a hotel to buy", "Round " + nt.getTurnNumber(),
						                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
						                null, options1, null);
							if (result == JOptionPane.YES_OPTION){
								yes_no_function(closeHotels.get(0), myPlayer, tmp_sequence);
							}
						}
						else{
							Object[] options1 = { "Hotel " + closeHotels.get(0).getHotelName(), "Hotel " + closeHotels.get(1).getHotelName(), "Quit" };
							int result = JOptionPane.showOptionDialog(null, "Choose a hotel to buy", "Round " + nt.getTurnNumber(),
						                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
						                null, options1, null);
							if (result == JOptionPane.YES_OPTION){
								yes_no_function(closeHotels.get(0), myPlayer, tmp_sequence);
							}
							else if (result == JOptionPane.NO_OPTION){
								yes_no_function(closeHotels.get(1), myPlayer, tmp_sequence);

							}
							else{
								
							}
							
						}
						myPlayer.removeHotels();
					}
					
				}
				else if (button6.equals(ev.getSource())){
					if(buyEntrance){
						find_available_hotels();
						if (availableHotels.size()==0){
							buyEntrance = false;
							JOptionPane.showMessageDialog(null, "Sorry, no available entrances for your hotels!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);	            
						}
						else{
							JButton[] hotelButtons = new JButton[availableHotels.size()+1];
							find_available_entrances();
							for(int i=0; i<availableHotels.size(); i++){
								hotelButtons[i] = createButton(hotelButtons, i);
							}
							hotelButtons[availableHotels.size()] = createQuitButton(hotelButtons, availableHotels.size());	
							JOptionPane.showOptionDialog(null, "Choose one of your hotels:", "Round " + nt.getTurnNumber(), 
							        JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, hotelButtons, null);
							
						}
						
					}
					else if(councilRequest){		
						find_available_hotels();
						find_available_built_hotels();
						if (availableBuiltHotels.size()==0){
							councilRequest = false;
							JOptionPane.showMessageDialog(null, "Sorry, no available entrances for your built hotels!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);	            
						}
						else{
							JButton[] hotelButtons = new JButton[availableBuiltHotels.size()+1];
							find_available_built_entrances();
							for(int i=0; i<availableBuiltHotels.size(); i++){
								hotelButtons[i] = createCouncilButton(hotelButtons, i);
							}
							hotelButtons[availableBuiltHotels.size()] = createCouncilQuitButton(hotelButtons, availableBuiltHotels.size());	
							JOptionPane.showOptionDialog(null, "Choose one of your built hotels:", "Round " + nt.getTurnNumber(), 
							        JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, hotelButtons, null);
							
						}
					}
				}
				else if (button7.equals(ev.getSource())){
					if (bankRequest){
						Player[] tmp_sequence = myGame.getPlayers();
						nt = myGame.get_turn();
						tmp_sequence[nt.getPlayerCounter()].setMoneyHeld(1000);
						bankRequest=false;
						if(tmp_sequence[nt.getPlayerCounter()].getAlmostBankrupt()){
							tmp_sequence[nt.getPlayerCounter()].avoidBunkrupt();
						}
					}
				}
				else{
					if (gameStarted){
						nt = myGame.get_turn();
						if (nt.getDiceRolled() == true){
							nt.setDiceRolled(false);
							nt.nextPlayer();
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
			
			private static String format(int d) {
		        String result = String.valueOf(d);
		        if (result.length() == 1) {
		            result = "0" + result;
		        }
		        return result;
		    }
			
			/**
			* @return time counter timer
			*/
			public Timer getTimer(){
				return t;
			}
			
			/**
			* updates time counter timer
			*/
			public void setTime(int t){
				time = t;
			}
			
			/**
			* @return P1 money timer
			*/
			public Timer getP1Timer(){
				return p1MoneyCounter;
			}
			
			/**
			* @return P2 money timer
			*/
			public Timer getP2Timer(){
				return p2MoneyCounter;
			}
			
			/**
			* @return P3 money timer
			*/
			public Timer getP3Timer(){
				return p3MoneyCounter;
			}
			
			/**
			* @return hotel timer
			*/
			public Timer getHotelTimer(){
				return hotelAvCounter;
			}
			
			/**
			* sets the list with hotels of this game
			* @param myList, myNumber, myFolders, myFolderNumber, hotelEntrances
			*/
			public void setHotelList(File[] myList, int myNumber, File[] myFolders, int myFolderNumber, ArrayList<Entrance> hotelEntrances){
				subfolders = myFolders;
				folderNumber = myFolderNumber;
				hotelNumber = myNumber;
				hotelList = null;
				hotelList = new File[hotelNumber];
				hotelList = myList;
				hotels.clear();
				ArrayList<Entrance> thisHotelEntrances = new ArrayList<Entrance>();
				gameHotelItems.clear();
				gameHotels.removeAll();
				for(int i=0; i<hotelNumber; i++){
					String str = hotelList[i].getName(); 
	                List<String> niceList = Arrays.asList(str.split("\\."));
	                for(int j=0; j<hotelEntrances.size(); j++){
	                	if(((MovementSquare)help_square[hotelEntrances.get(j).getI()][hotelEntrances.get(j).getJ()]).getLeftNeighbour().equals(niceList.get(0))){
	                		thisHotelEntrances.add(hotelEntrances.get(j));
	                		continue;
	                	}
	                	if(((MovementSquare)help_square[hotelEntrances.get(j).getI()][hotelEntrances.get(j).getJ()]).getRightNeighbour().equals(niceList.get(0))){
	                		thisHotelEntrances.add(hotelEntrances.get(j));
	                		continue;
	                	}
	                	if(((MovementSquare)help_square[hotelEntrances.get(j).getI()][hotelEntrances.get(j).getJ()]).getUpNeighbour().equals(niceList.get(0))){
	                		thisHotelEntrances.add(hotelEntrances.get(j));
	                		continue;
	                	}
	                	if(((MovementSquare)help_square[hotelEntrances.get(j).getI()][hotelEntrances.get(j).getJ()]).getDownNeighbour().equals(niceList.get(0))){
	                		thisHotelEntrances.add(hotelEntrances.get(j));
	                		continue;
	                	}
	                }
	                int maxBuildingLevel = findMaxLevel(i);
					Hotel newHotel = new Hotel(help_square, niceList.get(0), thisHotelEntrances, maxBuildingLevel);
					hotels.add(newHotel);
					setHotelsPopUp(i);
					thisHotelEntrances.clear();
				}
			}
			
			private void createCards() throws FileNotFoundException{
				name.clear();
				hotelNames.clear();
				
				for(int i=0; i<hotelNumber; i++){
					String str = hotelList[i].getName();
					final Scanner s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+str)));
					String tmp_name = "";
					
    				while(s.hasNextLine()){
    					tmp_name = tmp_name + s.nextLine()+"\n";
    				}
    				name.add(tmp_name);
					final List<String> niceList = Arrays.asList(str.split("\\."));
					hotelNames.add(Integer.parseInt(niceList.get(0)));
					s.close();
            	}
			}
			
			private void setPopUp(final int i){
				hotelItems.add(new JMenuItem(new AbstractAction("Hotel "+hotelNames.get(i)){
        			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {          				
        				JOptionPane.showMessageDialog(null, name.get(i), "Hotel "+hotelNames.get(i)+" infos", JOptionPane.INFORMATION_MESSAGE);	            
        			}
        		}));
        		popupCards.add(hotelItems.get(i));
			}
			
			private void setProfitsPopUp(final int i){
				profitItems.add(new JMenuItem(new AbstractAction("P"+String.valueOf(i+1)){
        			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {  
        				Player[] tmp_sequence = myGame.getPlayers();
        				int j;
        				for(j=0; j<3; j++){
        	        		if (tmp_sequence[j].getName().equals("P"+String.valueOf(i+1))){
        	            		break;           		
        	        		}
        	        	}
        				JOptionPane.showMessageDialog(null, String.valueOf(tmp_sequence[j].getMaxMoney()), "P"+String.valueOf(i+1)+" max money:", JOptionPane.INFORMATION_MESSAGE);	            
        			}
        		}));
        		playerProfits.add(profitItems.get(i));
			}
			
			private void setEntrancesPopUp(final int i){
				entranceItems.add(new JMenuItem(new AbstractAction("P"+String.valueOf(i+1)){
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {  
        				Player[] tmp_sequence = myGame.getPlayers();
        				int j;
        				for(j=0; j<3; j++){
        	        		if (tmp_sequence[j].getName().equals("P"+String.valueOf(i+1))){
        	            		break;           		
        	        		}
        	        	}
        				int totalEntrances = tmp_sequence[j].findTotalEntrances();
        				
        				JOptionPane.showMessageDialog(null, "Total Entrances: "+String.valueOf(totalEntrances), "P"+String.valueOf(i+1)+" entrances", JOptionPane.INFORMATION_MESSAGE);	            
        			}
				}));
        		playerEntrances.add(entranceItems.get(i));
			}
			
			private void setHotelsPopUp(final int i){
				gameHotelItems.add(new JMenuItem(new AbstractAction("Hotel "+hotels.get(i).getHotelName()){
					

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {  
						Scanner s;
						try {
							s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[i].getName())));
							List<String> niceList = Arrays.asList(s.nextLine());
							String owner;
							if(hotels.get(i).getOwner()==null) owner = "-";
							else if(hotels.get(i).getOwner().isBankrupt()) owner = "Bank (last owner "+ hotels.get(i).getOwner().getName() +" is bankrupt)"; 
							else owner = hotels.get(i).getOwner().getName();
							String tmp ="Hotel name: " +niceList.get(0);
							String hotelInfos = tmp +"\n";
							tmp = "Owner: "+owner;
							hotelInfos = hotelInfos + tmp +"\n";
							tmp = "Maximum builbing level: "+hotels.get(i).getMaxLevel();
							hotelInfos = hotelInfos + tmp +"\n";
							tmp = "Current building level: "+hotels.get(i).getBuildState();
							hotelInfos = hotelInfos + tmp +"\n";

							JOptionPane.showMessageDialog(null, hotelInfos, "Hotel "+hotels.get(i).getHotelName()+" infos", JOptionPane.INFORMATION_MESSAGE);	            
	        	
						}catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
								}
				}));
				gameHotels.add(gameHotelItems.get(i));
			}
			
			private void find_available_hotels(){
				final Player[] tmp_sequence = myGame.getPlayers();
				nt = myGame.get_turn();
				availableHotels.clear();
				
				availableHotels = tmp_sequence[nt.getPlayerCounter()].checkForEntrance();
				
			}
			
			private void find_available_built_hotels(){
				availableBuiltHotels.clear();
				for(int i=0; i<availableHotels.size(); i++){
					if(availableHotels.get(i).getBuildState()>0){
						availableBuiltHotels.add(availableHotels.get(i));
					}
				}
			}
			
			
			private void find_hotels_to_build(){
				final Player[] tmp_sequence = myGame.getPlayers();
				nt = myGame.get_turn();
				hotelsToBuild.clear();
				hotelsToBuild = tmp_sequence[nt.getPlayerCounter()].getHotelsToBuild();
			}
			
			
			
			
			
			private void find_available_entrances(){
				availableEntrances.clear();
				for(int i=0; i<availableHotels.size(); i++){
					boolean entranceFound=false;
							while(!entranceFound){
								int entNum = (int) ( Math.random() * (availableHotels.get(i).getPossibleEntrances().size()));
								if (availableHotels.get(i).getPossibleEntrances().get(entNum).getHotel()==null){
									availableEntrances.add(availableHotels.get(i).getPossibleEntrances().get(entNum));
									entranceFound = true;
								}
							}
				}
			}
			
			private void find_available_built_entrances(){
				availableBuiltEntrances.clear();
				for(int i=0; i<availableBuiltHotels.size(); i++){
					
					for(int j=0; j<availableBuiltHotels.get(i).getPossibleEntrances().size(); j++){
						if (availableBuiltHotels.get(i).getPossibleEntrances().get(j).getHotel()==null){
							availableBuiltEntrances.add(availableBuiltHotels.get(i).getPossibleEntrances().get(j));
							break;
						}
					}
				}
			}
			
			private void find_available_building_squares(){
				availableSquares.clear();
				for(int i=0; i<hotelsToBuild.size(); i++){
					for(int j=0; j<hotelsToBuild.get(i).getBuildingSquares().size(); j++){
						if(hotelsToBuild.get(i).getBuildingSquares().get(j).getSquareState()==true){
							availableSquares.add(hotelsToBuild.get(i).getBuildingSquares().get(j));
							break;
						}
					}
				}
			}
			
			private JButton createButton(final JButton[] hotelButtons, final int i){
				final Player[] tmp_sequence = myGame.getPlayers();
				nt = myGame.get_turn();
				hotelButtons[i] = new JButton("Hotel "+availableHotels.get(i).getHotelName());
				hotelButtons[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[i]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		                
		                int money = tmp_sequence[nt.getPlayerCounter()].getMoneyHeld();
		                for(int j=0; j<hotelNumber; j++){
							if (hotelList[j].getName().equals(availableHotels.get(i).getHotelName()+".txt")){
								
								
									Scanner s;
									try {
										s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[j].getName())));
										s.nextLine();
										s.nextLine();
										List<String> niceList = Arrays.asList(s.nextLine());
										if(money>Integer.parseInt(niceList.get(0))){
											buyEntrance = false;
											buildHotel = false;
											tmp_sequence[nt.getPlayerCounter()].setMoneyHeld(-Integer.parseInt(niceList.get(0)));		
											availableHotels.get(i).setRealEntrances(availableEntrances.get(i));
											availableEntrances.get(i).setHotel(availableHotels.get(i));
							            	JOptionPane.showMessageDialog(null, tmp_sequence[nt.getPlayerCounter()].getName()+ ", you bought the entrance ("+availableEntrances.get(i).getI()+","+availableEntrances.get(i).getJ()+") for the hotel "+availableHotels.get(i).getHotelName()+"\nCongrats!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
							            	break;
										}
										else{
							            	JOptionPane.showMessageDialog(null, "Not enough money to buy entrance for hotel "+availableHotels.get(i).getHotelName()+"\nTry another hotel!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);

										}
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
		                	}
		            	}
		            }
		        );
				return hotelButtons[i];
			}
			
			private JButton createCouncilButton(final JButton[] hotelButtons, final int i){
				final Player[] tmp_sequence = myGame.getPlayers();
				nt = myGame.get_turn();
				hotelButtons[i] = new JButton("Hotel "+availableBuiltHotels.get(i).getHotelName());
				hotelButtons[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[i]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		                
		                int money = tmp_sequence[nt.getPlayerCounter()].getMoneyHeld();
		                for(int j=0; j<hotelNumber; j++){
							if (hotelList[j].getName().equals(availableBuiltHotels.get(i).getHotelName()+".txt")){
								
								
									Scanner s;
									try {
										s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[j].getName())));
										s.nextLine();
										s.nextLine();
										List<String> niceList = Arrays.asList(s.nextLine());
										if(money>Integer.parseInt(niceList.get(0))){
											councilRequest = false;
											tmp_sequence[nt.getPlayerCounter()].setMoneyHeld(-Integer.parseInt(niceList.get(0)));		
											availableBuiltHotels.get(i).setRealEntrances(availableBuiltEntrances.get(i));
											availableBuiltEntrances.get(i).setHotel(availableBuiltHotels.get(i));
							            	JOptionPane.showMessageDialog(null, tmp_sequence[nt.getPlayerCounter()].getName()+ ", you bought the entrance ("+availableBuiltEntrances.get(i).getI()+","+availableBuiltEntrances.get(i).getJ()+") for the hotel "+availableBuiltHotels.get(i).getHotelName()+"\nCongrats!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
							            	break;
										}
										else{
							            	JOptionPane.showMessageDialog(null, "Not enough money to buy entrance for built hotel "+availableBuiltHotels.get(i).getHotelName()+"\nTry another hotel!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);

										}
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
		                	}
		            	}
		            }
		        );
				return hotelButtons[i];
			}

			
			private JButton createQuitButton(final JButton[] hotelButtons, final int i){
				hotelButtons[availableHotels.size()] = new JButton("Quit");
				hotelButtons[availableHotels.size()].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[i]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		            	
		            }
		        });
				return hotelButtons[availableHotels.size()];
			}
			
			private JButton createCouncilQuitButton(final JButton[] hotelButtons, final int i){
				hotelButtons[availableBuiltHotels.size()] = new JButton("Quit");
				hotelButtons[availableBuiltHotels.size()].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[availableBuiltHotels.size()]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		            	
		            }
		        });
				return hotelButtons[availableBuiltHotels.size()];
			}
			
			private JButton createButton(final JButton[] hotelButtons, final int i, final int currentBuildingLevel ){
				final Player[] tmp_sequence = myGame.getPlayers();
				nt = myGame.get_turn();
				hotelButtons[i] = new JButton("Hotel "+hotelsToBuild.get(i).getHotelName());
				hotelButtons[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[i]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		                
		                int money = tmp_sequence[nt.getPlayerCounter()].getMoneyHeld();
		                for(int j=0; j<hotelNumber; j++){
							if (hotelList[j].getName().equals(hotelsToBuild.get(i).getHotelName()+".txt")){
								
								
									Scanner s;
									try {
										s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[j].getName())));
										for(int k=0; k<3; k++){
											s.nextLine();
										}
										for(int current=0; current<hotelsToBuild.get(i).getBuildState(); current++){
											s.nextLine();
										}
										
										List<String> niceList = Arrays.asList(s.nextLine().split(","));
										int result = (int) ( Math.random() * 100);
										buildHotel = false;
										buyEntrance = false;
										if(result<50){
											JOptionPane.showMessageDialog(null, "Your request was approved!\nNormal cost building", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
											if(money>Integer.parseInt(niceList.get(0))){
												tmp_sequence[nt.getPlayerCounter()].setMoneyHeld(-Integer.parseInt(niceList.get(0)));		
												hotelsToBuild.get(i).changeBuildState();
												availableSquares.get(i).setSquareState();
											
												availableSquares.get(i).changeColour(tmp_sequence[nt.getPlayerCounter()]);
										
												JOptionPane.showMessageDialog(null, tmp_sequence[nt.getPlayerCounter()].getName()+ ", you have a new building at ("+availableSquares.get(i).getI()+","+availableSquares.get(i).getJ()+") for the hotel "+hotelsToBuild.get(i).getHotelName()+"\nCongrats!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
										
											}
											else{
												JOptionPane.showMessageDialog(null, "Not enough money for new building at hotel "+hotelsToBuild.get(i).getHotelName()+"\nTry another hotel!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
											
											}
										}
										else if(result<70){
											buildHotel = false;
											JOptionPane.showMessageDialog(null, "Sorry, your request was rejected... ", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
										
										}
										else if(result<85){
											JOptionPane.showMessageDialog(null, "Your request was approved\nFree building! ", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
											buildHotel = false;
											hotelsToBuild.get(i).changeBuildState();
											availableSquares.get(i).setSquareState();
										
											availableSquares.get(i).changeColour(tmp_sequence[nt.getPlayerCounter()]);
											JOptionPane.showMessageDialog(null, tmp_sequence[nt.getPlayerCounter()].getName()+ ", you have a new building at ("+availableSquares.get(i).getI()+","+availableSquares.get(i).getJ()+") for the hotel "+hotelsToBuild.get(i).getHotelName()+"\nCongrats!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);

											break;
										}
										else{
											JOptionPane.showMessageDialog(null, "Your request was approved\nOvercost building...", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
											buildHotel = false;
											if(money>2*Integer.parseInt(niceList.get(0))){
												tmp_sequence[nt.getPlayerCounter()].setMoneyHeld(-2*Integer.parseInt(niceList.get(0)));		
												hotelsToBuild.get(i).changeBuildState();
												availableSquares.get(i).setSquareState();
											
												availableSquares.get(i).changeColour(tmp_sequence[nt.getPlayerCounter()]);
										
												JOptionPane.showMessageDialog(null, tmp_sequence[nt.getPlayerCounter()].getName()+ ", you have a new building at ("+availableSquares.get(i).getI()+","+availableSquares.get(i).getJ()+") for the hotel "+hotelsToBuild.get(i).getHotelName()+"\nCongrats!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
										
											}
											else{
												JOptionPane.showMessageDialog(null, "Not enough money for new building at hotel "+hotelsToBuild.get(i).getHotelName()+"\nTry another hotel!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);
											
											}
										}
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
		                	}
		            	}
		            }
		        );
				return hotelButtons[i];
			}
			
			private JButton createQuitBuildButton(final JButton[] hotelButtons, final int i){
				hotelButtons[hotelsToBuild.size()] = new JButton("Quit");
				hotelButtons[hotelsToBuild.size()].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	Window w = SwingUtilities.getWindowAncestor(hotelButtons[i]);

		                if (w != null) {
		                  w.setVisible(false);
		                }
		            	
		            }
		        });
				return hotelButtons[hotelsToBuild.size()];
			}
			
			private int findMaxLevel(final int i){
				Scanner s;
				int maxLevel = 0;
				try {
					s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[i].getName())));
					for(int j=0; j<3; j++){
						s.nextLine();
					}
					
					while(s.hasNext()){
						maxLevel++;
						s.nextLine();
					}
					s.close();
				}
				 catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return maxLevel;
					
			}
			
			/**
			* is called when the active game is over
			*/
			public void finishGame(){
				gameStarted = false;
			}
			
			private void yes_no_function(Hotel closeHotel, Player myPlayer, Player[] tmp_sequence){
				
				for(int i=0; i<hotelNumber; i++){
					if (hotelList[i].getName().equals(closeHotel.getHotelName()+".txt")){
						if (closeHotel.getBuildState()!=0){
							JOptionPane.showMessageDialog(null, "You can't buy hotel " + closeHotel.getHotelName(), "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);	            
							break;
						}
						else{
						
							Scanner s;
							try {
								s = new Scanner(new BufferedReader(new FileReader("boards\\"+subfolders[folderNumber].getName()+"\\"+hotelList[i].getName())));
								s.nextLine();
								List<String> niceList = Arrays.asList(s.nextLine().split(","));
								if(closeHotel.getOwner()==null){
									int money = myPlayer.getMoneyHeld();
									if(money>Integer.parseInt(niceList.get(0))){
										buyHotel = false;
										myPlayer.setMoneyHeld(-Integer.parseInt(niceList.get(0)));		
										myPlayer.setProperty(closeHotel);
										closeHotel.boughtBy(myPlayer);
									}
									else{
										JOptionPane.showMessageDialog(null, "Not enough money to buy hotel "+closeHotel.getHotelName()+"\nTry another hotel", "Round " + nt.getTurnNumber() , JOptionPane.INFORMATION_MESSAGE);	            
									}
								}
								
								else if(!(closeHotel.getOwner().isBankrupt())){
									String primaryOwner = closeHotel.getOwner().getName();
									for(int j=0; j<3; j++){
										Player aPlayer = tmp_sequence[j];
										if(aPlayer.getName().equals(primaryOwner)){
											if(!(myPlayer.getName().equals(primaryOwner))){
												int money = myPlayer.getMoneyHeld();
												if(money>Integer.parseInt(niceList.get(1))){
													buyHotel = false;
													myPlayer.setMoneyHeld(-Integer.parseInt(niceList.get(1)));		
													myPlayer.setProperty(closeHotel);
													aPlayer.setMoneyHeld(Integer.parseInt(niceList.get(1)));
													closeHotel.boughtBy(myPlayer);
													int prCounter = 0;
													while (prCounter < aPlayer.getProperty().size()){
														if(aPlayer.getProperty().get(prCounter).getHotelName().equals(closeHotel.getHotelName())){
															aPlayer.getProperty().remove(prCounter);
															break;
														}
														prCounter++;
													}
												}
												else{
													JOptionPane.showMessageDialog(null, "Not enough money to buy hotel "+closeHotel.getHotelName()+"\nTry another hotel", "Round " + nt.getTurnNumber() , JOptionPane.INFORMATION_MESSAGE);	            
												}
											}
											else{
													JOptionPane.showMessageDialog(null, "You already own the hotel!", "Round " + nt.getTurnNumber() , JOptionPane.INFORMATION_MESSAGE);	            
													
												}
											break;
										}
									
									}
								}
								else{
									JOptionPane.showMessageDialog(null, "You can't buy hotel " + closeHotel.getHotelName() + "\nPrevious owner is Bankrupt!", "Round " + nt.getTurnNumber() ,  JOptionPane.INFORMATION_MESSAGE);	            

								}
							}
							 catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							 }						
						}
					}
				}
			}
			
			/**
			* @return active game object
			*/
			public NewGame getGame(){
				return myGame;
			}
			
}
