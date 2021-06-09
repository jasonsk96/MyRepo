import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;

public class MyFrame extends JFrame implements ActionListener {

	Canvas canvas = new Canvas();
	JComboBox cb;
	JButton undoBut;
	JButton reset;
	//MyThread timer;
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public void setCanvas(Canvas canvas){
		this.canvas=canvas;
	}
	public static void main(String[] args){
		new MyFrame();
	}
	
	public MyFrame() throws HeadlessException {
		super("Painting");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().add(canvas);
		
		//timer = new MyThread(canvas);
		//timer.start();
		
	    getContentPane().setBackground(Color.black);
	    JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    String colors[]={"red","green","yellow"};        
	    cb=new JComboBox(colors);    
	    cb.setBounds(50, 50,90,20); 
	    cb.addActionListener(this);
	    panel.add(cb);
	    undoBut = new JButton("Undo");
	    undoBut.setBounds(50,100,95,30);
	    undoBut.addActionListener(this);
		panel.add(undoBut); 
		reset = new JButton("Reset");
	    reset.setBounds(50,100,95,30);
	    reset.addActionListener(this);
	    panel.add(reset);
	    getContentPane().add(panel, BorderLayout.NORTH);
		//new MyThread(canvas).start();
		pack();
		}
	
	public void actionPerformed(ActionEvent ev){
		if (undoBut.equals(ev.getSource())){
			canvas.delc();
		}
		else if (reset.equals(ev.getSource())){
			canvas.reset();
			canvas.repaint();
		}
		else{
			String s = (String) cb.getSelectedItem();//get the selected item

			switch (s) {//check for a match
				case "red":
					canvas.color = 0;
					break;
				case "green":
					canvas.color = 1;
					break;
				case "yellow":
					canvas.color = 2;
					break;
			}
		}
	}
}

