/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/
package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * Constants for the view of the frame.
	 */
	public final int LocationX = 200;
    public final int LocationY = 50;
    public final int width = 1000;
    public final int height = 600;
	
	/**
	 * Constructor.
	 */
	public MainFrame(){
		
		// Set the parameters of this frame.
		this.setTitle("Twitter Pagerank");
		this.setLocation(LocationX,LocationY);
		this.setSize(width,height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set maximun and minimun sizes.
		//this.setMinimumSize(new Dimension(width,height));
		//this.setMaximumSize(new Dimension(width,height));
		this.setSize(new Dimension(width,height));
		
		// Create the Panel and add it to the frame.
		this.setContentPane(new PanelTabbed());
		
		// Make the frame visitble.
		//this.pack();
		this.setVisible(true);		 
		
	}
}
