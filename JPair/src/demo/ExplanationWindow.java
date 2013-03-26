package demo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class ExplanationWindow extends JPanel implements Observer {

	int count = 0;
	Controller control;
	JTextArea textArea;

	public ExplanationWindow(Controller cont) {

		control = cont;
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		ActionListener ql = cont.getQueryButtonListener();
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		setBorder(b);

		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		textArea.setFont(new Font("Serif", Font.ITALIC, 20));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText("Hello and welcome to the Encyrpted Email demonstration, this Demonstration will teach you the history "
				+ "of Identity Based Encryption and show you the Implementation of two IBE systems, the Boneh-Franklin system and the Boneh-Boyen"
				+ " system."
				+ "\n"
				+ "\n"
				+ "This cryptosystems used in this demo were developed by David Smith and were based on the RFC 5091 Standard."
				+ " (Supersingular Curve Implementations of the BF and BB1 Cryptosystems)"
				+ "\n"
				+ "\n"
				+ "This demonstration also uses the JPair project developed by Changyu Dong as the underlying implemtentation of bilnear pairing."
				+ "\n" + "\n" + "Please press next to Continue.");
		JScrollPane scrollPane = new JScrollPane(textArea);
		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		JButton NEXTButton = new JButton("Next");
		NEXTButton.addActionListener(ql);
		NEXTButton.setActionCommand("NEXT");
		JPanel NEXTButtonPanel = new JPanel();
		NEXTButtonPanel.add(NEXTButton);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 1;
		add(NEXTButtonPanel, g);

		JButton BACKButton = new JButton("Back");
		BACKButton.addActionListener(ql);
		BACKButton.setActionCommand("BACK");
		JPanel BACKButtonPanel = new JPanel();
		BACKButtonPanel.add(BACKButton);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 2;
		g.gridy = 1;
		add(BACKButtonPanel, g);

		JButton RESTARTButton = new JButton("Restart");
		RESTARTButton.addActionListener(ql);
		RESTARTButton.setActionCommand("RESTART");
		JPanel RESTARTButtonPanel = new JPanel();
		RESTARTButtonPanel.add(RESTARTButton);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 4;
		g.gridy = 1;
		add(RESTARTButtonPanel, g);
		//

	}

	private void setField(String text) {

	}

	@Override
	public void update(Observable o, Object arg) {

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
