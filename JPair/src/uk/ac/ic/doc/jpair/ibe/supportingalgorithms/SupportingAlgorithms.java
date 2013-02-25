package uk.ac.ic.doc.jpair.ibe.supportingalgorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	public Point HashToPoint(EllipticCurve e, int p, int q, String id,
			String hashfcn) throws NoSuchAlgorithmException {

		int y = HashToRange(id, p, hashfcn);
		int x = (y ^ 2 - 1) ^ ((2 * p - 1) / 3) % p;

		BigInteger xbig = BigInteger.valueOf(x);
		BigInt pointx = new BigInt(xbig);
		BigInteger ybig = BigInteger.valueOf(y);
		BigInt pointy = new BigInt(ybig);

		Point Q_ = new Point(pointx, pointy);

		int t = (p + 1) / q;
		BigInteger te = BigInteger.valueOf(t);
		BigInt temp = new BigInt(te);
		Point Q = e.multiply(Q_, temp);

		return Q;
	}

	public int HashToRange(String id, int p, String hashfcn)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(hashfcn);

		p = (int) Math.ceil(Math.log(p)/8);
		int hashlen = messageDigest.getDigestLength();
		int v_0 = 0;
		int v_1 = 0;
		int a_i;
		String h_0 = new String();
		String h_1;
		String t_i = "";
		
		for (int i = 0; i < hashlen; i++) {
			h_0.concat("0x00");
		}

		for (int j = 1; j <= 1; j++) {

			t_i = h_0.concat(id);
			messageDigest.update(t_i.getBytes());
			h_0 = new String(messageDigest.digest());
			a_i = Integer.valueOf(h_0);
			v_0 = (int) (Math.pow(256, hashlen) * v_0 + a_i);

		}

		

		return v_0 % p;

	}

	public String HashBytes(int b, String rho, String hash)
			throws NoSuchAlgorithmException {
		
		MessageDigest messageDigest = MessageDigest.getInstance(hash);
		String h_1 = "";
		String r_1 = "";

		int hashlen = messageDigest.getDigestLength();

		messageDigest.update(rho.getBytes());
		
		String K = new String(messageDigest.digest());
		
		String h_0 = new String();
		for (int i = 0; i < hashlen; i++) {
			h_0.concat("0x00");
		}

		int l = (int) Math.ceil(b/hashlen);
		
		for (int j =1; j<l; j++) {

			messageDigest.update(h_0.getBytes());
			h_1 = new String(messageDigest.digest());
			String temp = h_0.concat(K);
			messageDigest.update(temp.getBytes());
			r_1 = new String(messageDigest.digest());
		}
		
		String r = SupportingAlgorithms.cut(r_1, b);
		
	return r;

	}

	public String Canonical(int p, int i, int theta_) {
		String s = "";
		int l = (int) Math.ceil(p/8);
		int a_int = (int) Math.pow(256, l);
		String a = Integer.toString(a_int);
		int b_int = (int) Math.pow(256, l);
		String b = Integer.toString(b_int);
		
		if (i==0) {
			s =a.concat(b);
		}
		
		else if (i==1) {
			s= b.concat(a);
		}
		
		
		return s;
	}

	  public static String cut(String s, int n) {
		    byte[] utf8 = s.getBytes();
		    if (utf8.length < n) n = utf8.length;
		    int n16 = 0;
		    boolean extraLong = false;
		    int i = 0;
		    while (i < n) {
		      n16 += (extraLong) ? 2 : 1;
		      extraLong = false;
		      if ((utf8[i] & 0x80) == 0) i += 1;
		      else if ((utf8[i] & 0xC0) == 0x80) i += 2;
		      else if ((utf8[i] & 0xE0) == 0xC0) i += 3;
		      else { i += 4; extraLong = true; }
		    }
		    return s.substring(0,n16);
		  }

	  public String xorHex(String a, String b) {
		    // TODO: Validation
		    char[] chars = new char[a.length()];
		    for (int i = 0; i < chars.length; i++) {
		        chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
		    }
		    return new String(chars);
		}

		private static int fromHex(char c) {
		    if (c >= '0' && c <= '9') {
		        return c - '0';
		    }
		    if (c >= 'A' && c <= 'F') {
		        return c - 'A' + 10;
		    }
		    if (c >= 'a' && c <= 'f') {
		        return c - 'a' + 10;
		    }
		    throw new IllegalArgumentException();
		}

		private char toHex(int nybble) {
		    if (nybble < 0 || nybble > 15) {
		        throw new IllegalArgumentException();
		    }
		    return "0123456789ABCDEF".charAt(nybble);
		}
}
