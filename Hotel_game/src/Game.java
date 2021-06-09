import javax.swing.*;

import java.awt.*;
import java.io.IOException;

public class Game extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static Hotel_GUI newGUI;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Game();
					Game.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Constructor of Game class
	 * @throws HeadlessException, IOException
	 * game's UI creation
	 */
	public Game() throws HeadlessException, IOException {
		createAndShowUI();
	}
	
	private static void createAndShowUI() throws IOException {
	    frame = new JFrame("MediaLab Hotel");
	    frame.setPreferredSize(new Dimension(1000, 700));
	    frame.setMinimumSize(new Dimension(1000, 700));
	    frame.setMaximumSize(new Dimension(1000, 700));
	    newGUI = new Hotel_GUI();
	    frame.getContentPane().add(newGUI);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
	
	/**
	 * @return application's GUI
	 */
	public static Hotel_GUI getGUI(){
		return newGUI;
	}

}