package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import uk.ac.ic.doc.jpair.pairing.Point;

public class Demo {

	public static void main(String[] args) throws NoSuchAlgorithmException {
	String ID = "David Smith";
	String message ="Hello, how are you ? My name is David";
	PublicParameter pp =BFCryptoSystem.BFSetup1(1024);
	Point S_ID = BFCryptoSystem.extraction(ID, pp);
	ArrayList triple= BFCryptoSystem.encryption(message, ID, pp);
	BFCryptoSystem.decryption(S_ID, triple, pp);
	}
}
