package uk.ac.ic.doc.jpair.ibe.supportingalgorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Complex;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;

/**
 * This class contains several supporting algorithms for both the Boneh-Boyen
 * and Boneh-Franklin Cryptoschemes. These methods involve encoding
 * and hashing to various data types.
 * 
 * These methods use code from the underlying JPair project created by Changyu Dong
 * 
 * @author David Smith
 * @version 1.0
 * 
 */

public class SupportingAlgorithms {

	static MessageDigest messageDigest;

	/**
	 * This method returns a point Q_id of order
     * q in E(F_p) that is calculated using the cryptographic hash function
     * hashfcn.
	 * 
	 * @param e - Elliptic Curve
	 * @param p - Prime integer
	 * @param q - Prime integer
	 * @param id - User ID string
	 * @param hashfcn - Cryptographic hash function
	 * 
	 * @return  Q_id = (x, y) of order q n E(F_p)
	 *
	 * @throws NoSuchAlgorithmException
	 */
	static public Point HashToPoint(EllipticCurve e, BigInt p, BigInt q,
			String id, String hashfcn) throws NoSuchAlgorithmException {
		
		BigInt x= HashToRange(id.getBytes(), p, hashfcn);
		
		Point P =e.getPoint(x);
		
		while(P==null){
			x=x.add(BigInt.ONE);
			P=e.getPoint(x);
		}
		BigInt temp = p.add(BigInt.ONE);
		temp = temp.divide(q);

		P = e.multiply(P, temp);
		
		return P;
		
//		// y = HashToRange(id, p, hashfcn)
//		BigInt y = HashToRange(id.getBytes(), p, hashfcn);
//		// x = (y^2 - 1)^((2 * p - 1) / 3) mod p
//		// (y^2 - 1)
//		BigInt base = ((y.multiply(y)).subtract(BigInt.ONE));
//		// ((2 * p - 1) / 3)
//		BigInt exp = p.add(p).subtract(BigInt.ONE);
//		//exp = exp.divide(BigInt.valueOf(3));
//		exp= exp.divide(BigInt.valueOf(3));
//		// x = (y^2 - 1)^((2 * p - 1) / 3) mod p
//		BigInt x = base.modPow(exp, p);
//		// Q' = (x, y)
//		Point Q_ = new Point(x, y);
//		
//		if(e.isOnCurve(Q_)){
//		System.out.println("Q_ is correct");
//	    }else{
//			System.out.println("Q_ is not correct");
//		}
//		// Q = [(p + 1) / q ]Q'
//		BigInt temp = p.add(BigInt.ONE);
//		temp = temp.divide(q);
//
//		Point Q = e.multiply(Q_, temp);
//		return Q;
	
	}

	/**
	 * This method takes a byte array representing a string bs, an integer p, and a
     * cryptographic hash function hashfcn as input and returns an integer
     * in the range 0 to n - 1 by cryptographic hashing.
	 * 
	 * @param bs - Byte Array representing a string
	 * @param p - Positive Integer p
	 * @param hashfcn - Cryptographic hashfunction
	 * 
	 * @return A positive integer v in the range 0 to n - 1
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static BigInt HashToRange(byte[] bs, BigInt p, String hashfcn)
			throws NoSuchAlgorithmException {
		
		// Let hashlen be the number of octets comprising the output of
		// hashfcn
		messageDigest = MessageDigest.getInstance(hashfcn);
		int hashlen = messageDigest.getDigestLength();
		
		// v_0 = 0
		BigInt v_0 = BigInt.ZERO;
		
		BigInt a_i;
		
		// h_0 = 0x00...00, a string of null octets with a length of
		// hashlen
		byte[] h_0 = new byte[hashlen];
		
		byte[] t_i;

		for (int j = 1; j < 2; j++) {

			byte[] id_ = bs;
			
			//Let t_i = h_0 || s
			t_i = new byte[h_0.length + id_.length];
			System.arraycopy(h_0, 0, t_i, 0, h_0.length);
			System.arraycopy(id_, 0, t_i, h_0.length, id_.length);
			
			//h_0 = hashfcn(t_i)
			h_0 = messageDigest.digest(t_i);
			
			//a_i = Value(h_i)
			a_i = new BigInt(1, h_0);
			
			//v_0 = 256^hashlen * v_(i - 1) + a_i
			BigInt v_0_temp = BigInt.valueOf(256).pow(hashlen);
            v_0_temp = v_0_temp.multiply(v_0);
            v_0_temp = v_0_temp.add(a_i);
            v_0 = v_0_temp;

		}

		//v = v_l (mod p)
		return v_0.mod(p);

	}

	/**
	 * This method takes an integer b, a byte array rho, and a
     * cryptographic hash function as input and returns a b-byte
     * pseudo-random byte array as output.
	 * 
	 * @param b - Integer b
	 * @param rho - byte array representing a string
	 * @param hash - Cryptographic hashfunction
	 * 
	 * @return A byte array out comprising b bytes
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static byte[] HashBytes(int b, byte[] rho, String hash)
			throws NoSuchAlgorithmException {
		// Let hashlen be the number of octets comprising the output of
		// hashfcn
		messageDigest = MessageDigest.getInstance(hash);
		int hashlen = messageDigest.getDigestLength();
		
		//K = hashfcn(rho)
		byte[] k = messageDigest.digest(rho);
		
		//h_0 = 0x00...00, a string of null octets with a length of
	    //hashlen
		byte[] h_0 = new byte[hashlen];
		
		byte[] t_i;
		
		byte[] r_i = null;
		//l = Ceiling(b / hashlen)
		int l = (int) Math.ceil((double)b / hashlen);
		
		byte[] out =new byte[b];
		int start= 0;
		
		if (l == 0) {
			l = 1;
		}

		for (int i = 0; i < l; i++) {
			//h_0 = hashfcn(h_0)
			h_0 = messageDigest.digest(h_0);
			
			//r_i = hashfcn(h_i || K)
			t_i = new byte[h_0.length + k.length];
			System.arraycopy(h_0, 0, t_i, 0, h_0.length);
			System.arraycopy(k, 0, t_i, h_0.length, k.length);
			r_i = messageDigest.digest(t_i);
			
			if(start+hashlen<b){
				System.arraycopy(r_i, 0, out, start, hashlen);
			}else{
				System.arraycopy(r_i, 0, out, start, b-start);
			}
			start+=hashlen;
		}

		return out;

	}

	/**
	 * This method takes an element v in F_p^2 and returns a
     * canonical representation of v as an octet string s of fixed size.
     * The parameter o MUST be either 0 or 1, and specifies the ordering of
     * the encoding.
	 * 
	 * @param p - Integer p
	 * @param i  - Ordering parameter i, either 0 or 1
	 * @param theta_ - element theta_ (Here defined as a complex from the JPair Project) in F_p^2
	 * 
	 * @return - Byte Array of size 2 * Ceiling(lg(p) / 8) octets
	 * 
	 */
	public static byte[] Canonical(BigInt p, int i, Complex theta_) {

		byte[] s = new byte[(p.bitLength() / 8) * 2];
		//l = Ceiling(lg(p) / 8)
		BigInt l = BigInt
				.valueOf((long) Math.floor(Math.log(p.divide(BigInt.valueOf(8)).doubleValue())));
		
		// Let a_(256^l) be the big-endian zero-padded fixed-length octet
		// string representation of a in Z_p
		// Let b_(256^l) be the big-endian zero-padded fixed-length octet
		// string representation of b in Z_p
		BigInt a = theta_.getReal();
		BigInt b = theta_.getImag();
		byte[] a_array = new byte[p.bitLength() / 8];
		byte[] b_array = new byte[p.bitLength() / 8];
		a_array = a.toByteArray();
		b_array = b.toByteArray();
		
		if (i == 0) {
			//s = a_(256^l) || b_(256^l)
			s = new byte[a_array.length + b_array.length];
			System.arraycopy(a_array, 0, s, 0, a_array.length);
			System.arraycopy(b_array, 0, s, a_array.length, b_array.length);
		}
		if (i == 1) {
			//s = b_(256^l) || a_(256^l)
			s = new byte[b_array.length + a_array.length];
			System.arraycopy(b_array, 0, s, 0, b_array.length);
			System.arraycopy(a_array, 0, s, b_array.length, a_array.length);
		}

		return s;
	}

	public static byte[] xorTwoByteArrays(byte[] ba1, byte[] ba2) {
		byte[] result = new byte[ba1.length];
		for (int i = 0; i < ba1.length; i++) {
			result[i] = (byte) (ba1[i] ^ ba2[i]);
		}
		return result;
	}
}
