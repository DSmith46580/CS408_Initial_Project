import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.Pairing;
import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Complex;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.Predefined;


public class TEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BigInteger s= new BigInteger("c467281144fc70d76b54befa588490ea8da866d2",16);
		BigInteger q= new BigInteger ("8000000000000000000000000000000000020001",16);
		BigInteger rho= new BigInteger ("3dd4b3a7ef879b3d8994f8eaf89a5a38ac4445f3",16);
		
		System.out.println("q is prime: "+ q.isProbablePrime(80));
		
		BigInteger u= (s.add(rho));
		
		System.out.println("u="+u.toString(16));
		
		BigInteger s2 = (u.subtract(rho));
		System.out.println("s="+s2.toString(16));
		
		

	}

}
