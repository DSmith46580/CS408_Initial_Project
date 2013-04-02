package demo;

import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Observable;

import uk.ac.ic.doc.jpair.ibe.cryptosystems.BBCryptoSystem;
import uk.ac.ic.doc.jpair.ibe.cryptosystems.BFCryptoSystem;
import uk.ac.ic.doc.jpair.ibe.cryptosystems.PublicParameter;
import uk.ac.ic.doc.jpair.pairing.Point;

public class Controller extends Observable {

	EmailWindow emailw;
	ExplanationWindow explanw;
	private ButtonListener listener;
	int count;

	public Controller(GuiWindow gw) {
		emailw = new EmailWindow(this);
		explanw = new ExplanationWindow(this);

		gw.frame.add(explanw);
		gw.frame.add(emailw);

		gw.frame.validate();
		gw.frame.repaint();

	}

	public ActionListener getQueryButtonListener() {
		if (listener == null) {
			listener = new ButtonListener(this);
		}
		return listener;
	}

	public void setText(String text) throws NoSuchAlgorithmException {
		count = explanw.getCount();

		if (text == "BACK" && count == 0) {
			explanw.textArea
					.setText("Cannot go back any further, please hit next.");
		}

		if (text == "BACK" && count == 1) {
			explanw.textArea
					.setText("Hello and welcome to the Encyrpted Email demonstration, this Demonstration will teach you the history "
							+ "of Identity Based Encryption and show you the Implementation of two IBE schemes, the Boneh-Franklin scheme and the Boneh-Boyen"
							+ " scheme. Please press next to Continue.");
			explanw.setCount(count - 1);
		}

		if (text == "RESTART") {
			explanw.textArea
					.setText("Hello and welcome to the Encyrpted Email demonstration, this Demonstration will teach you the history "
							+ "of Identity Based Encryption and show you the Implementation of two IBE schemes, the Boneh-Franklin scheme and the Boneh-Boyen"
							+ " scheme. Please press next to Continue.");
			explanw.setCount(0);
		}

		if (text == "NEXT" && count == 0) {
			explanw.textArea
					.setText("Identity Based Encryption an important primitive of ID-based cryptography. As such it is a type of public-key encryption in which the public key of a user is some unique information about the identity of the user (e.g. a user's email address). "
							+ "This can use the text-value of the name or domain name as a key or the physical IP address it translates to. The first implementation of an email-address based PKI was developed by Adi Shamir in 1984, which allowed users to verify digital "
							+ "signatures using only public information such as the user's identifier. ID-based encryption was proposed by Adi Shamir in 1984. He was however only able to give an instantiation of identity-based signatures. Identity-based encryption remained an open problem for many years. "
							+ "One example of the research leading up to identity-based encryption is provided in Maurer. The pairing-based Boneh–Franklin scheme (Which is implemented in this demonstration) and Cocks's encryption scheme based on quadratic residues both solved the IBE problem in 2001."
							+ "\n" + "\n" + "Please hit next to continue.");
			explanw.setCount(count + 1);
		}

		if (text == "NEXT" && count == 1) {
			explanw.textArea
					.setText("This Demonstration has two IBE schemes, the Boneh-Franklin scheme and the Boneh-Boyen scheme, in order to use one please: "
							+ "\n"
							+ "\n"
							+ "1) Select which one from the dropdown menu,"
							+ "\n"
							+ "2) Enter the recipient ID. I.E. Who you would send this message to. (Which here is as an email address, don't worry it can be a fictional one) "
							+ "\n"
							+ "3) Enter your message and then hit the send button");
			explanw.setCount(count + 1);
		}

		if (text == "SEND") {
			String choice = (String) emailw.DropDown.getSelectedItem();

			if (choice.equals("Boneh-Franklin Cryptosystem")) {
				String id = emailw.IDField.getText();
				String message = emailw.messageField.getText();

				PublicParameter pp = BFCryptoSystem.BFSetup1(1024);
				Point S_ID = BFCryptoSystem.extraction(id, pp);
				ArrayList triple = BFCryptoSystem.encryption(message, id, pp);
				String decmessage = BFCryptoSystem.decryption(S_ID, triple, pp);
				byte[] messageenc = (byte[]) triple.get(2);
				String encryptedmessage = new String(messageenc);

				if (id.isEmpty() == true && message.isEmpty() == true) {
					explanw.textArea.setText("Please enter an ID and Message");
					emailw.IDField.cut();
					emailw.messageField.cut();
				}

				else if (id.isEmpty() == true && message.isEmpty() == false) {
					explanw.textArea.setText("Please enter an ID");
					emailw.messageField.cut();
				}

				else if (id.isEmpty() == false && message.isEmpty() == true) {
					explanw.textArea.setText("Please enter a Message");
					emailw.IDField.cut();
				}

				else {
					explanw.textArea
							.setText("You have chosen the "
									+ choice
									+ ". The Boneh-Franklin scheme is an Identity based encryption system proposed by Dan Boneh and Matthew K. Franklin in 2001. It is an application of "
									+ "pairings (Weil pairing) over elliptic curves and finite fields."
									+ " The Scheme has five main methods: "
									+ "\n"
									+ "\n"
									+ "(1) Setup - Randomly selects a master secret and corresponding public parameters based on security parameter 'n'"
									+ "\n"
									+ "(2) Public Key Derivation - Derives a "
									+ "Public key from the users ID and their public parameters"
									+ "\n"
									+ "(3) Extract - Extracts the private key from the users ID, public parameters and the master secret"
									+ "\n"
									+ "(4) Encrypt - Encrypts the messages using the public "
									+ "key, the users ID and public parameters."
									+ "\n"
									+ "(5) Decrypt - Decrypts the messages using the corresponding private key."
									+ "\n"
									+ "\n"
									+ "In this case your encrypted message is shown as: "
									+ encryptedmessage
									+ "\n"
									+ "\n"
									+ " And your decrypted message: "
									+ decmessage
									+ "\n"
									+ "\n"
									+ "Please hit restart in order to view this demonstration from the beginning or change your options and hit send again.");
				}

			}

			if (choice.equals("Boneh-Boyen Cryptosystem")) {
				String id = emailw.IDField.getText();
				String message = emailw.messageField.getText();

				PublicParameter ppbb = BBCryptoSystem.BFSetup1(1024);
				ArrayList privatekey = BBCryptoSystem.extraction(id, ppbb);
				ArrayList quad = BBCryptoSystem.encryption(message, id, ppbb);
				String messageenc = BBCryptoSystem.decryption(quad, ppbb,
						privatekey);
				byte[] enc = (byte[]) quad.get(3);
				String encryptedmessage = new String(enc);
				String decmessage = new String(messageenc);

				if (id.isEmpty() == true && message.isEmpty() == true) {
					explanw.textArea.setText("Please enter an ID and Message");
					emailw.IDField.cut();
					emailw.messageField.cut();
				}

				else if (id.isEmpty() == true && message.isEmpty() == false) {
					explanw.textArea.setText("Please enter an ID");
					emailw.messageField.cut();
				}

				else if (id.isEmpty() == false && message.isEmpty() == true) {
					explanw.textArea.setText("Please enter a Message");
					emailw.IDField.cut();
				}

				else {
					explanw.textArea
							.setText("You have chosen the "
									+ choice
									+ " The Boneh-Boyen scheme is an Identity based encryption system proposed by Dan Boneh and Xavier Boyen. The scheme is an example of the communitative"
									+ " blinding schemes called this due to the commuting of coefficients that occurs when computing the ratio of two pairings."
									+ " The Scheme has five main methods: "
									+ "\n"
									+ "\n"
									+ "(1) Setup - Randomly selects a master secret and corresponding public parameters based on security parameter 'n'"
									+ "\n"
									+ "(2) Public Key Derivation - Derives a "
									+ "Public key from an identity and public parameters"
									+ "\n"
									+ "(3) Extract - Extracts the private key from an identity, public parameters and the master secret"
									+ "\n"
									+ "(4) Encrypt - Encrypts the messages using the public "
									+ "key ID and public parameters."
									+ "\n"
									+ "(5) Decrypt - Decrypts messages using the corresponding private key."
									+ "\n"
									+ "\n"
									+ "In this case your encrypted message is shown as: "
									+ encryptedmessage
									+ "\n"
									+ "\n"
									+ "And your decrypted message: "
									+ decmessage
									+ "\n"
									+ "\n"
									+ "Please hit restart in order to view this demonstration from the beginning or change your options and hit send again.");
				}
			}
		}

	}

	public void pressedBack() throws NoSuchAlgorithmException {
		setText("BACK");

	}

	public void pressedNext() throws NoSuchAlgorithmException {
		setText("NEXT");

	}

	public void pressedRestart() throws NoSuchAlgorithmException {
		setText("RESTART");

	}

	public void pressedSend() throws NoSuchAlgorithmException {
		setText("SEND");

	}

}
