package uk.ac.ic.doc.jpair.ibe.supportingalgorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;

/**
 * This class contains several supporting algorithms for both the Boneh-Boyen and Boneh-Franklin
 * Cryptoschemes. 
 * 
 * @author David Smith
 * @version 0.1
 */



public class SupportingAlgorithms {
	
	MessageDigest messageDigest;

	public Point HashToPoint(EllipticCurve e, int p, int q, String id,
			String hashfcn) throws NoSuchAlgorithmException {
		
		int y = HashToRange(id,p,hashfcn);
		int x = (y^2 -1)^((2*p-1)/3)%p;
		
		BigInteger xbig = BigInteger.valueOf(x);
		BigInt pointx = new BigInt(xbig);
		BigInteger ybig = BigInteger.valueOf(y);
		BigInt pointy = new BigInt(ybig);
		
		Point Q_ = new Point(pointx, pointy);
		
		int t =(p+1)/q;
		BigInteger te = BigInteger.valueOf(t);
		BigInt temp = new BigInt(te);
		Point Q = e.multiply(Q_,temp );
		
		
		return Q;
	}

	public int HashToRange(String id, int p, String hashfcn) throws NoSuchAlgorithmException {
	    MessageDigest messageDigest = MessageDigest.getInstance(hashfcn);

		int hashlen = hashfcn.length();
		int v_0 = 0;
		String h_0 = new String();
		for (int i=0; i<hashlen;i++) {
			h_0.concat("0x00");
		}
		
		for (int j=1;j<1;j++) {

			String t_i = h_0 + id;
			messageDigest.update(t_i.getBytes());
			h_0 = new String(messageDigest.digest());
			long a_i = Long.parseLong(h_0);
			v_0 = (int) (256^hashlen * v_0 + a_i);
		
		}
		
		for (int l=1;l<1;l++) {

			String t_i = h_0 + id;
			messageDigest.update(t_i.getBytes());
			h_0 = new String(messageDigest.digest());
			long a_i = Long.parseLong(h_0);
			v_0 = (int) (256^hashlen * v_0 + a_i);
		
		}
		
		return v_0%p;
		
	}


}
