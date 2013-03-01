package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import uk.ac.ic.doc.jpair.pairing.Point;

public class Demo {

	public static void main(String[] args) throws NoSuchAlgorithmException {
	String ID = "David Smith";
	String message ="Hello, how are you my name is David";
	BFCryptoSystem bf = new BFCryptoSystem();
	PublicParameter pp =bf.BFSetup1(1024);
	bf.derivation(ID, pp);
	Point S_ID = bf.extraction(ID, pp);
	ArrayList triple= bf.encryption(message, ID, pp);
	bf.decryption(S_ID, triple, pp);
	}
}
