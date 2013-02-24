package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Demo {

	public static void main(String[] args) throws NoSuchAlgorithmException {
	String ID = "David Smith";
	String message ="Hello";
	BFCryptoSystem bf = new BFCryptoSystem();
	PublicParameter pp =bf.BFSetup1(1024);
	bf.derivation(ID, pp);
	bf.extraction(ID, pp);
	ArrayList triple= bf.encryption(message, ID, pp);
	bf.decryption(bf.getS_ID(), triple, pp);
	}
}
