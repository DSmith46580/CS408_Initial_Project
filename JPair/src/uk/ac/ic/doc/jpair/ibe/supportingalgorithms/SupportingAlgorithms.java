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
 * and Boneh-Franklin Cryptoschemes.
 * 
 * @author David Smith
 * @version 0.1
 */

public class SupportingAlgorithms {

	static MessageDigest messageDigest;

	static public Point HashToPoint(EllipticCurve e, BigInt p, BigInt q,
			String id, String hashfcn) throws NoSuchAlgorithmException {
		// y = HashToRange(id, p, hashfcn)
		BigInt y = HashToRange(id.getBytes(), p, hashfcn);
		// x = (y^2 - 1)^((2 * p - 1) / 3) mod p
		// (y^2 - 1)
		BigInt base = y.multiply(y).subtract(BigInt.ONE);
		// ((2 * p - 1) / 3)
		BigInt exp = p.add(p).subtract(BigInt.ONE);
		exp = exp.divide(BigInt.valueOf(3));
		// x = (y^2 - 1)^((2 * p - 1) / 3) mod p
		BigInt x = base.modPow(exp, p);
		// Q' = (x, y)
		Point Q_ = new Point(x, y);
		// Q = [(p + 1) / q ]Q'
		BigInt temp = p.add(BigInt.ONE);
		temp = temp.divide(q);
		Point Q = e.multiply(Q_, temp);
		return Q;
	}

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
		int l = b / hashlen;
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
		}
		//r = LeftmostOctets(b, r_1 || ... || r_l), i.e., r is formed as
	    //the concatenation of the r_i, truncated to the desired number of
	    //octets
		byte temp[] = new byte[b-hashlen];
		Random rand = new Random();
		rand.nextBytes(temp);
        byte[] r = new byte[r_i.length + temp.length];
		System.arraycopy(r_i, 0, r, 0, r_i.length);
		System.arraycopy(temp, 0, r, r_i.length, temp.length);
		return r;

	}

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
			System.arraycopy(b_array, 0, s, 0, a_array.length);
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
