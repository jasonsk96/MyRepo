import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.PointerInfo;

import javax.swing.JLabel;
import java.util.ArrayList;

public class Canvas extends JLabel implements MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	private int x = -1;
	private int y = -1;
	private int rad = 1;
	public int color;
	public int undo=0;
	public int old_size=0;
	private boolean fromMouse = false;
	private ArrayList<Dot> arr = new ArrayList<Dot>();
	public ArrayList<Ar> areas = new ArrayList<Ar>();
	
	public Canvas(){
		//System.out.println("Hi apo canvas");
		setPreferredSize(new Dimension(500, 500));
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g)
	{
		if(fromMouse) {
			Dot cur = new Dot(this.x, this.y);
			arr.add(cur);
			if (areas.size()==0){
				if (color==0) g.setColor(Color.red);
				//System.out.println("Hi apo canvas paint");
				else if (color==1) g.setColor(Color.green);
				else g.setColor(Color.yellow);
				for (int i = 0; i < arr.size(); i++) {
					g.fillOval(arr.get(i).getX()-rad, arr.get(i).getY()-rad, 2*rad, 2*rad);
				}
				Ar cur_area = new Ar(color,old_size,arr.size());
				areas.add(cur_area);
				old_size = arr.size();
			}
			else{
				for (int j=0; j<areas.size(); j++){
					if (areas.get(j).color==0) g.setColor(Color.red);
					//System.out.println("Hi apo canvas paint");
					else if (areas.get(j).color==1) g.setColor(Color.green);
					else if (areas.get(j).color==2) g.setColor(Color.yellow);
					else g.setColor(Color.black);
					for (int i = areas.get(j).start; i < areas.get(j).end; i++) {
						g.fillOval(arr.get(i).getX()-rad, arr.get(i).getY()-rad, 2*rad, 2*rad);
					}
				}
				if (color==0) g.setColor(Color.red);
				//System.out.println("Hi apo canvas paint");
				else if (color==1) g.setColor(Color.green);
				else g.setColor(Color.yellow);
				for (int i = old_size; i < arr.size(); i++) {
					g.fillOval(arr.get(i).getX()-rad, arr.get(i).getY()-rad, 2*rad, 2*rad);
				}
				Ar cur_area = new Ar(color,old_size,arr.size());
				areas.add(cur_area);
				old_size = arr.size();
			}
			
		}
		else{
			if (areas.size()==0){
				g.setColor(Color.black);
				for (int i = 0; i < arr.size(); i++) {
					g.fillOval(arr.get(i).getX()-rad, arr.get(i).getY()-rad, 2*rad, 2*rad);
				}
			}
			else{
				for (int j=0; j<areas.size(); j++){
					if (areas.get(j).color==0) g.setColor(Color.red);
					//System.out.println("Hi apo canvas paint");
					else if (areas.get(j).color==1) g.setColor(Color.green);
					else if (areas.get(j).color==2) g.setColor(Color.yellow);
					else g.setColor(Color.black);
					for (int i = areas.get(j).start; i < areas.get(j).end; i++) {
						g.fillOval(arr.get(i).getX()-rad, arr.get(i).getY()-rad, 2*rad, 2*rad);
					}
				}
			}
		}
		fromMouse = false;
	}
	
	public void delc() {
		if (areas.size()!=0){
			undo++;
			areas.get(areas.size()-undo).color=5;
			repaint();
		}
	}
	
	public void reset(){
		for(int j=0; j<areas.size(); j++){
			areas.get(j).color=5;
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		fromMouse=true;
		//Point pnt = e.getLocationOnScreen();
		//Point pnt = MouseInfo.getPointerInfo().getLocation();
		this.x = (int) e.getX();
		this.y = (int) e.getY();
		//System.out.println("Opa mes sto keyevent" + x + " " + y);
		undo = 0;
		this.repaint();
	}

	
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
