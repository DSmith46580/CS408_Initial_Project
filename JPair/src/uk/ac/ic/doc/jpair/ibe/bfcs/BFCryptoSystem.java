package uk.ac.ic.doc.jpair.ibe.bfcs;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
import uk.ac.ic.doc.jpair.ibe.supportingalgorithms.SupportingAlgorithms;
import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Fp;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.TatePairing;

/**
 * This class implements the Boneh-Franklin Identity-Base Encryption (IBE)
 * scheme based on the RFC 5091 Standard. The scheme consists of five main
 * algorithms and several supporting algorithms, the main algorithms are as
 * follows: (1) <b>Setup</b> Randomly selects a master secret and corresponding
 * public parameters based on security parameter 'n' (2) <b>Public Key
 * Derivation</b> Derives a Public key from an identity and public parameters
 * (3) <b>Extract</b> Extracts the private key from an identity, public
 * parameters and the master secret (4) <b>Encrypt</b> encrypts messages using
 * the public key ID and public parameters, (5) <b>Decrypt</b> decrypts messages
 * using the corresponding private key.
 * 
 * This Implementation uses the underlying JPair project for cryptographic
 * primitives developed by Changyu Dong.
 * 
 * @author David Smith
 * @version 0.1
 */

public class BFCryptoSystem {

	private int n_p;
	private int n_q;
	private MessageDigest messageDigest;
	private SupportingAlgorithms sa = new SupportingAlgorithms();

	// Public Parameters
	private int q;
	private int p;
	private EllipticCurve E;
	private String hashfcn;
	private Point P;
	private Point P_pub;

	private BigInt s; // Master Secret
	private int r;

	private Point Q_ID;
	private Point S_ID;

	// Used for checking for primes
	private boolean IsPrime1;
	private boolean IsPrime2;
	

	/**
	 * The Setup algorithm.
	 * 
	 * @param n
	 *            - Security Parameter which must be equal to 1024, 2048, 3072,
	 *            7680 or 15360
	 * @return A set of public parameters (version, E, p, q, P, P-pub, hashfcn)
	 *         and the corresponding Master Secret 's'
	 * @throws NoSuchAlgorithmException 
	 */
	public void BFSetup1(int n) throws NoSuchAlgorithmException {

		if (n == 1024) {
			n_p = 512;
			n_q = 160;
			hashfcn = "SHA-1";
		}

//      SHA-224 is listed in the RFC standard but has not yet been implemented in java.
//		else if (n == 2048) {
//			n_p = 1024;
//			n_q = 224;
//			hashfcn = "SHA-224";
//		}

		else if (n == 3072) {
			n_p = 1536;
			n_q = 256;
			hashfcn = "SHA-256";
		}

		else if (n == 7680) {
			n_p = 3840;
			n_q = 384;
			hashfcn = "SHA-384";
		}

		else if (n == 15360) {
			n_p = 7680;
			n_q = 512;
			hashfcn = "SHA-512";
		}

	    messageDigest = MessageDigest.getInstance(hashfcn);
		determinevariables();

		// Create the elliptic curve
		BigInteger f = BigInteger.valueOf(q);
		BigInt field = new BigInt(f);
		BigInteger fe = BigInteger.ZERO;
		BigInt fe1 = new BigInt(fe);
		BigInteger fielde = BigInteger.ONE;
		BigInt fe2 = new BigInt(fielde);
		E = new EllipticCurve(new Fp(field), fe1, fe2);

		BigInteger random1 = BigInteger.valueOf((long) Math.random());
		BigInt pointx = new BigInt(random1);
		BigInteger random2 = BigInteger.valueOf((long) Math.random());
		BigInt pointy = new BigInt(random2);

		Point point_ = new Point(pointx, pointy);

		P = E.multiply(point_, BigInt.valueOf(12 * r));

		int s1 = 2 + (int) (Math.random() * (((q - 1) - 1 - 2) + 1));
		s = new BigInt(BigInteger.valueOf(s1));
		P_pub = E.multiply(P, s);

	}

	/**
	 * The Derivation algorithm. This method calls upon one of the supporting algorithms
	 * 
	 * @param ID
	 *            - Identity String id
	 * @return Q_ID - Point in order q in E(F_p)
	 * @throws NoSuchAlgorithmException 
	 * 
	 *         
	 */
	public void derivation(String ID) throws NoSuchAlgorithmException {
		Q_ID = sa.HashToPoint(E, p, q, ID, hashfcn);
	}

	
	/**
	 * The Extraction algorithm. This method calls upon one of the supporting algorithms
	 * 
	 * @param ID
	 *            - Identity String ID
	 * @return S_ID - Point in order q in E(F_p)
	 * 
	 *         
	 */
	public void extraction(String ID) {

		S_ID = E.multiply(Q_ID, s);
	}


	/**
	 * The Encryption algorithm. This method calls the supporting algorithms
	 * 
	 * @param ID
	 *            - Identity String id
	 * @param PT -
	 *            Plaintext String m of size |m| octets
	 * @return Ciphertext tuple (U,V,W)
	 * @throws NoSuchAlgorithmException 
	 * 
	 *         
	 */
	public void encryption(String PT, String ID) throws NoSuchAlgorithmException {

		// Come back to this point
		//int hashlen = hashfcn.length();
		//
		
		messageDigest.update(PT.getBytes());
		String t = new String(messageDigest.digest());
		int l1 = sa.HashToRange(t, q, hashfcn);
		BigInteger l2 = BigInteger.valueOf(l1);
		BigInt l = new BigInt(l2);
		Point U = E.multiply(P, l);
		
	
		BigInteger p1 = BigInteger.valueOf(p);
		BigInt P = new BigInt(p1);
		BigInteger q1 = BigInteger.valueOf(q);
		BigInt Q = new BigInt(q1);
		TatePairing tatep = new TatePairing (E,P,Q);
		BigInt theta = (BigInt) tatep.compute(P_pub, Q_ID);
		int theta1 = theta.intValue();
		int theta_ = (int) Math.pow(theta1,1);
		String z = sa.Canonical(p,k,0,theta_);
		messageDigest.update(z.getBytes());
		String w = new String(messageDigest.digest());
        
		//V = w^rho;
		//W = sa.HashBytes(nID,rho,hashfcn)^m
		
		//Create Array and add U,V and W
	}


	/**
	 * The Decryption algorithm. This method calls the supporting algorithms
	 * 
	 * @param S_ID
	 *            - Private key point
	 * @param CT -
	 *            Ciphertext triple (U,V,W)
	 * @return Decrypted Plaintext or an invalid ciphertext flag
	 * 
	 *         
	 */
	public void decryption(Point S_ID, Array triple) {
		int hashlen = hashfcn.length();
		//theta = sa.Pairing(E, p, q, U, S_ID);
	    //z=Canonical(p,k.0,theta);
	    //messageDigest.update(z.getBytes());
	    //String w = new String(messageDigest.digest());
		//rho = x^V
		//m= sa.HashByte(W_,rho,hashfcn)^W;
		//messageDigest.update(m.getBytes());
	    //String t = new String(messageDigest.digest());
		// l1 = sa.HashToRange(rho || t,q,hashfcn);
		//BigInteger l2 = BigInteger.valueOf(l1);
		//BigInt l = new BigInt(l2);
		// if (U = E.multiply(P,l) {
		// return M
		//}
		//else {
		//System.out.println("Invalid Cyphercheck"):
		//}
	}

	/**
	 * This method is used by the set-up algorithm in order to determine some of
	 * the public variables q and p_
	 */
	public void determinevariables() {
		while (IsPrime1 = false) {
			q = 0 + (int) (Math.random() * ((2 ^ n_q - 1 - 0) + 1));
			// checks whether q is prime or not.
			// check if q is a multiple of 2
			if (q % 2 == 0)
				IsPrime1 = false;
			// if not, then just check the odds
			for (int i = 3; i * i <= q; i += 2) {
				if (q % i == 0)
					IsPrime1 = false;
			}
			IsPrime1 = true;
		}

		while (IsPrime2 = false) {

			r = (int) Math.random();
			p = 12 * r * q - 1;
			// checks whether p is prime or not.
			// check if p is a multiple of 2
			if (p % 2 == 0)
				IsPrime2 = false;
			// if not, then just check the odds
			for (int i = 3; i * i <= p; i += 2) {
				if (p % i == 0)
					IsPrime2 = false;
			}
			IsPrime2 = true;
		}

		if (IsPrime1 && IsPrime2 != true) {
			determinevariables();
		}

	}
}
