package uk.ac.ic.doc.jpair.ibe.supportingalgorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;

/**
 * This class contains several supporting algorithms for both the Boneh-Boyen
 * and Boneh-Franklin Cryptoschemes.
 * 
 * @author David Smith
 * @version 0.1
 */

public class SupportingAlgorithms {

	MessageDigest messageDigest;

	static public Point HashToPoint(EllipticCurve e, BigInt p, BigInt q, String id,
			String hashfcn) throws NoSuchAlgorithmException {

		BigInt y = HashToRange(id.getBytes(), p, hashfcn);
		BigInt x = (y.pow(2).min(BigInt.valueOf(1))).pow((2 * p.intValue()- 1) / 3).mod(p);
        Point Q_ = new Point(x, y);
        Point Q = e.multiply(Q_, (p.add(BigInt.valueOf(1).divide(p))));
        return Q;
	}

	public static BigInt HashToRange(byte[] bs, BigInt p, String hashfcn)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(hashfcn);
        int hashlen = messageDigest.getDigestLength();
		BigInt v_0 = BigInt.ZERO;
		BigInt a_i;
		byte[] h_0 = new byte[hashlen];
		byte[] t_i;
		
		for (int j = 1; j < 2; j++) {

			byte[] id_ = bs;
			t_i = new byte[h_0.length + id_.length];
			System.arraycopy(h_0, 0, t_i, 0, h_0.length);
			System.arraycopy(id_, 0, t_i, h_0.length, id_.length);
			messageDigest.update(t_i);
			h_0 = messageDigest.digest();
			byte[] temp=messageDigest.digest();
			a_i = new BigInt(h_0);
			v_0 = BigInt.valueOf(256).pow(hashlen).multiply(v_0).add(a_i);

		}

		

		return v_0.mod(p);

	}

	
	public static byte[] HashBytes(int b, byte[] rho, String hash)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(hash);
        int hashlen = messageDigest.getDigestLength();
		
        messageDigest.update(rho);
        byte[] k = messageDigest.digest();
        byte[] h_0 = new byte[hashlen];
        byte[] t_i;
        byte[] r_i = null;
        int l= (int) Math.ceil(b/hashlen);
        
        for (int i=0; i<l; i++) {
        	h_0 = messageDigest.digest(h_0);
        	t_i = new byte[h_0.length + k.length];
			System.arraycopy(h_0, 0, t_i, 0, h_0.length);
			System.arraycopy(k, 0, t_i, h_0.length, k.length);
			 r_i = messageDigest.digest(t_i);
        }
		byte[] r = Arrays.copyOf(r_i, b) ;
	
		
		return r;

	}

	public static byte[] Canonical(BigInt p, int i, BigInt theta_) {
	
		BigInt l = BigInt.valueOf((long) Math.ceil(p.bitLength()/8));
		// 2. Let v = a + b * i, where i^2 = -1
		//
		// 3. Let a_(256^l) be the big-endian zero-padded fixed-length octet
		// string representation of a in Z_p
		//
		// 4. Let b_(256^l) be the big-endian zero-padded fixed-length octet
		// string representation of b in Z_p
		//
		// 5. Depending on the choice of ordering o:
		//
		// (a) If o = 0, then let s = a_(256^l) || b_(256^l), which is the
		// concatenation of a_(256^l) followed by b_(256^l)
		//
		// (b) If o = 1, then let s = b_(256^l) || a_(256^l), which is the
		// concatenation of b_(256^l) followed by a_(256^l)
		//
		// 6. Return s
		return null;
	}

	 

	  public static byte[] xorTwoByteArrays(byte[] ba1,byte[] ba2){
			byte [] result =new byte[ba1.length];
			for(int i=0;i<ba1.length;i++){
				 result[i]=(byte) (ba1[i]^ba2[i]);
			}
			return result;
		}
}
