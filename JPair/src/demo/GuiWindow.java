package demo;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

public class GuiWindow {

	JFrame frame;

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 * @param cont 
	 */
	public GuiWindow () {
		// Create and set up the window.
		frame = new JFrame("Encrypted Email");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension preferredSize = new Dimension(1024, 900);
		frame.setPreferredSize(preferredSize);
		frame.setLayout(new GridLayout(2, 1));
		

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	
	

}
