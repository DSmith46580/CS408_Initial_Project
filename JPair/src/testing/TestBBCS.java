package testing;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ic.doc.jpair.ibe.cryptosystems.BBCryptoSystem;
import uk.ac.ic.doc.jpair.ibe.cryptosystems.BFCryptoSystem;
import uk.ac.ic.doc.jpair.ibe.cryptosystems.PublicParameter;
import uk.ac.ic.doc.jpair.pairing.Point;

public class TestBBCS {

	static String ID = "David Smith";
	static String message ="Hello, how are you ? My name is David";
	static ArrayList privatekey;
	static PublicParameter pp;
	static String comparemessage;
	static int count = 1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pp = BBCryptoSystem.BFSetup1(1024);
		privatekey = BBCryptoSystem.extraction(ID, pp);
	}

	@Test
	public void test() {
		
		for (int i=0; i<1000;i++) {
		ArrayList quad;
		try {
			quad = BBCryptoSystem.encryption(message, ID, pp);
			comparemessage = BBCryptoSystem.decryption(quad, pp, privatekey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (message.equals(comparemessage)) {
			System.out.println("Correct " + count);
		}
		else {
			fail("Invalid cyhphercheck");
		}
		count++;
		}
		
		
	}
	
	 @After
	    public void tearDown() throws Exception {
	        message = "";   
	    }

}
