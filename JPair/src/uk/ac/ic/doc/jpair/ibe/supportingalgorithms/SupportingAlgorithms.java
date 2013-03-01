package uk.ac.ic.doc.jpair.ibe.supportingalgorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Complex;
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
		
		BigInt base = y.multiply(y).subtract(BigInt.ONE);
		BigInt exp = p.add(p).subtract(BigInt.ONE);
		exp = exp.divide(BigInt.valueOf(3));
		BigInt x = base.modPow(exp, p);
		
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
			h_0 = messageDigest.digest(t_i);
			a_i = new BigInt(1, h_0);
			v_0 = BigInt.valueOf(256).pow(hashlen).multiply(v_0).add(a_i);

		}

		

		return v_0.mod(p);

	}

	
	public static byte[] HashBytes(int b, byte[] rho, String hash)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(hash);
        int hashlen = messageDigest.getDigestLength();
		byte[] k = messageDigest.digest(rho);
        byte[] h_0 = new byte[hashlen];
        byte[] t_i;
        byte[] r_i = null;
        int l= (int) Math.ceil(b/hashlen);
        if (l==0){
        	l=1;
        }
        
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

	public static byte[] Canonical(BigInt p, int i, Complex theta_) {
	
		byte[]s = new byte[(p.bitLength()/8)*2];
		BigInt l = BigInt.valueOf((long) Math.ceil(Math.log(p.bitLength())/8));
		BigInt a = theta_.getReal();
		BigInt b = theta_.getImag();
		byte[] a_array = new byte[p.bitLength()/8];
		byte[] b_array = new byte[p.bitLength()/8];
		a_array = a.toByteArray();
		b_array = b.toByteArray();
		if (i==0) {
			s = new byte[a_array.length + b_array.length];
			System.arraycopy(a_array, 0, s, 0, a_array.length);
			System.arraycopy(b_array, 0, s, a_array.length, b_array.length);
		}
		if (i==1) {
			s = new byte[b_array.length + a_array.length];
			System.arraycopy(b_array, 0, s, 0, a_array.length);
			System.arraycopy(a_array, 0, s, b_array.length, a_array.length);
		}
		
		return s;
	}

	 

	  public static byte[] xorTwoByteArrays(byte[] ba1,byte[] ba2){
			byte [] result =new byte[ba1.length];
			for(int i=0;i<ba1.length;i++){
				 result[i]=(byte) (ba1[i]^ba2[i]);
			}
			return result;
		}
}
