package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import uk.ac.ic.doc.jpair.pairing.Point;

public class Demo {

	public static void main(String[] args) throws NoSuchAlgorithmException {
	String ID = "David Smith";
	String message ="Hello, how are you ? My name is David";
	
	Long startTime = System.nanoTime();
	Long bfStartTime = System.nanoTime();
    System.out.println("--------BFCryptoSystem----------");
	PublicParameter pp =BFCryptoSystem.BFSetup1(1024);
	Point S_ID = BFCryptoSystem.extraction(ID, pp);
	ArrayList triple= BFCryptoSystem.encryption(message, ID, pp);
	BFCryptoSystem.decryption(S_ID, triple, pp);
	Long bfEndTime = (System.nanoTime()-bfStartTime)/1000000;
	System.out.println("Time taken for the Boneh-Boyen Cryptosystem (In MS): " + bfEndTime.toString());
	
	System.out.println("");
	System.out.println("");
	
	Long bbStartTime = System.nanoTime();
	System.out.println("--------BBCryptoSystem----------");
	PublicParameter ppbb = BBCryptoSystem.BFSetup1(1024);
	ArrayList privatekey = BBCryptoSystem.extraction(ID, ppbb);
	ArrayList quad = BBCryptoSystem.encryption(message, ID, ppbb);
	BBCryptoSystem.decryption(quad, ppbb, privatekey);
	Long bbEndTime = (System.nanoTime()-bbStartTime)/1000000;
	System.out.println("Time taken for the Boneh-Boyen Cryptosystem (In MS): " + bbEndTime.toString());
	
	Long endTime = (System.nanoTime()-startTime)/1000000;
	System.out.println(" ");
	System.out.println("Entire time taken for both CryptoSystems to Setup, Extract, Encrypt and Decrypt (In MS): " + endTime.toString());
	}
}
