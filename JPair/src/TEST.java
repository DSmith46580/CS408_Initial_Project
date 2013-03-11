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
		Pairing pair = Predefined.ssTate(); 
		
		Random rnd = new SecureRandom();
		
		Point P= pair.RandomPointInG2(rnd);
		BigInt s=new BigInt(160, rnd);
		Point Ppub= pair.getCurve2().multiply(P, s);
		
		Point QID= pair.RandomPointInG1(rnd);
		Point SID=pair.getCurve().multiply(QID, s);
		
		Complex a = (Complex) pair.compute(QID, Ppub);
		System.out.println(a.toString(16));
		
		Complex b=(Complex) pair.compute(SID, P);
		System.out.println(b.toString(16));
		
		

	}

}
