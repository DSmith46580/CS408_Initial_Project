package demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class QueryButtonListener implements ActionListener {
	
	private Controller ctrl;
	
	public QueryButtonListener(Controller c) {
		this.ctrl = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("BACK")) {

			System.out.println("BACK button pressed.");
			try {
				ctrl.pressedBack();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

		if (e.getActionCommand().equals("NEXT")) {

			System.out.println("NEXT button pressed.");
			try {
				ctrl.pressedNext();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		if (e.getActionCommand().equals("RESTART")) {

			System.out.println("RESTART button pressed.");
			try {
				ctrl.pressedRestart();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		if (e.getActionCommand().equals("SEND")) {

			System.out.println("SEND button pressed.");
			try {
				ctrl.pressedSend();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

	}

}