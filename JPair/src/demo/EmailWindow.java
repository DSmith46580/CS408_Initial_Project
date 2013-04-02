package demo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class EmailWindow extends JPanel implements Observer {
	
	JComboBox<String> DropDown;
	JTextArea messageField;
	JTextField IDField;

	public EmailWindow(Controller cont) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		ActionListener ql = cont.getQueryButtonListener();
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		setBorder(b);
		
		DropDown = new JComboBox<String>();
		DropDown.setPreferredSize(new Dimension(400, 20));
	
		
		JPanel DropPanel = new JPanel();
		DropPanel.add(DropDown);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 0;
		add(DropPanel,g);
		DropDown.addItem(new String("Boneh-Franklin Cryptosystem"));
		DropDown.addItem(new String("Boneh-Boyen Cryptosystem"));
		
		
		JLabel cLabel1 = new JLabel(" Recipient ID :");
		JPanel cLabelPanel1 = new JPanel();
		cLabelPanel1.add(cLabel1);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 1;
		add(cLabelPanel1, g);
		
		IDField = new JTextField(30);
		JPanel IDFieldPanel = new JPanel();
		IDFieldPanel.add(IDField);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 2;
		add(IDFieldPanel,g);
		
		JLabel cLabel = new JLabel("Message :");
		JPanel cLabelPanel = new JPanel();
		cLabelPanel.add(cLabel);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 3;
		add(cLabelPanel, g);
		
		messageField = new JTextArea(15,30);
		messageField.setLineWrap(true);
		messageField.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(messageField);
		int x = messageField.getX();
		Dimension preferredSize = new Dimension(x, 200);
		messageField.setPreferredSize(preferredSize );
		JPanel messageFieldPanel = new JPanel();
		messageFieldPanel.add(scrollPane);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 4;
		add(messageFieldPanel,g);
		
		
		JButton SENDButton = new JButton("Send");
		SENDButton.addActionListener(ql);
		SENDButton.setActionCommand("SEND");
		JPanel SENDButtonPanel = new JPanel();
		SENDButtonPanel.add(SENDButton);
		g.weightx = 0;
		g.gridwidth = 2;
		g.gridx = 0;
		g.gridy = 5;
		add(SENDButtonPanel);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
