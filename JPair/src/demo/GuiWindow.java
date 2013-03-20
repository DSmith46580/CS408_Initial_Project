package demo;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

public class GuiWindow {

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 * @param cont 
	 */
	private static void createAndShowGUI(Controller cont) {
		// Create and set up the window.
		JFrame frame = new JFrame("Encrypted Email");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension preferredSize = new Dimension(1024, 900);
		frame.setPreferredSize(preferredSize);
		frame.setLayout(new GridLayout(2, 1));
		
		EmailWindow ew = new EmailWindow(cont);
		ExplanationWindow expw = new ExplanationWindow(cont);
		
		frame.add(ew);
		frame.add(expw);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		final Controller cont = new Controller();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(cont);
			}
		});
	}

}
