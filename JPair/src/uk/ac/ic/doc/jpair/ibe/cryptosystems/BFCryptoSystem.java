package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
	int p;
	int q;

	private BigInt s; // Master Secret
	private int r;

	private Point Q_ID;
	private Point S_ID;

	
	

	/**
	 * The Setup algorithm.
	 * 
	 * @param n
	 *            - Security Parameter which must be equal to 1024, 2048, 3072,
	 *            7680 or 15360
	 * @return
	 * @return A set of public parameters (version, E, p, q, P, P-pub, hashfcn)
	 *         and the corresponding Master Secret 's'
	 * @throws NoSuchAlgorithmException
	 */
	public PublicParameter BFSetup1(int n) throws NoSuchAlgorithmException {

		String hashfcn = "";
		EllipticCurve E;

		if (n == 1024) {
			n_p = 512;
			n_q = 160;
			hashfcn = "SHA-1";
		}

		// SHA-224 is listed in the RFC standard but has not yet been
		// implemented in java.
		// else if (n == 2048) {
		// n_p = 1024;
		// n_q = 224;
		// hashfcn = "SHA-224";
		// }

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
		BigInt field = BigInt.valueOf(p);
		BigInt fe1 = BigInt.ZERO;
		BigInt fe2 = BigInt.ONE;
		E = new EllipticCurve(new Fp(field), fe1, fe2);

		BigInt pointx = BigInt.valueOf((long) Math.random());
		BigInt pointy = BigInt.valueOf((long) Math.random());
		Point point_ = new Point(pointx, pointy);

		Point P = E.multiply(point_, BigInt.valueOf(12 * r));

		int s1 = 2 + (int) (Math.random() * (((q - 2)) + 1));
		s = BigInt.valueOf(s1);
		Point P_pub = E.multiply(point_, s);

		return new PublicParameter(E, p, q, P, P_pub, hashfcn);
	}

	/**
	 * The Derivation algorithm. This method calls upon one of the supporting
	 * algorithms
	 * 
	 * @param ID
	 *            - Identity String id
	 * @return Q_ID - Point in order q in E(F_p)
	 * @throws NoSuchAlgorithmException
	 * 
	 * 
	 */
	public Point derivation(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {

		return Q_ID = sa.HashToPoint(pp.ec, pp.getP_int(), pp.getQ_int(), ID,
				pp.getHash());
	}

	/**
	 * The Extraction algorithm. This method calls upon one of the supporting
	 * algorithms
	 * 
	 * @param ID
	 *            - Identity String ID
	 * @return S_ID - Point in order q in E(F_p)
	 * @throws NoSuchAlgorithmException
	 * 
	 * 
	 */
	public Point extraction(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		Q_ID = sa.HashToPoint(pp.ec, pp.getP_int(), pp.getQ_int(), ID,
				pp.getHash());
		return S_ID = pp.ec.multiply(Q_ID, s);
	}

	/**
	 * The Encryption algorithm. This method calls the supporting algorithms
	 * 
	 * @param ID
	 *            - Identity String id
	 * @param PT
	 *            - Plaintext String m of size |m| octets
	 * @return
	 * @return Ciphertext tuple (U,V,W)
	 * @throws NoSuchAlgorithmException
	 * 
	 * 
	 */
	public ArrayList encryption(String PT, String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {

		int hashlen = messageDigest.getDigestLength();
		Q_ID = sa.HashToPoint(pp.ec, pp.getP_int(), pp.getQ_int(), ID,
				pp.getHash());
		int rho_int = (int) (Math.random() * ((hashlen / 8 - 0) + 1));
		BigInt rho_bigint = BigInt.valueOf(rho_int);
		String rho = rho_bigint.toString();
		messageDigest.update(PT.getBytes());
		String t = new String(messageDigest.digest());
		int l1 = sa.HashToRange(t, pp.getQ_int(), pp.getHash());
		BigInt l = BigInt.valueOf(l1);
		Point U = pp.ec.multiply(pp.getPoint(), l);

		BigInt P = pp.getP();
		BigInt Q = pp.getQ();
		TatePairing tatep = new TatePairing(pp.getEc(), P, Q);
		BigInt theta = (BigInt) tatep.compute(pp.getPublic_point(), Q_ID);
		int theta1 = theta.intValue();
		int theta_ = (int) Math.pow(theta1, 1);
		String z = sa.Canonical(p, 0, theta_);
		messageDigest.update(z.getBytes());
		String w = new String(messageDigest.digest());

		String V = sa.xorHex(w, rho);

		String temp = sa.HashBytes(PT.length() * 8, rho, pp.getHash());

		String W = sa.xorHex(temp, PT);

		ArrayList tuple = new ArrayList();
		tuple.add(U);
		tuple.add(V);
		tuple.add(W);
		return tuple;
	}

	/**
	 * The Decryption algorithm. This method calls the supporting algorithms
	 * 
	 * @param S_ID
	 *            - Private key point
	 * @param CT
	 *            - Ciphertext triple (U,V,W)
	 * @return
	 * @return Decrypted Plaintext or an invalid ciphertext flag
	 * @throws NoSuchAlgorithmException
	 * 
	 * 
	 */
	public String decryption(Point S_ID, ArrayList triple, PublicParameter pp)
			throws NoSuchAlgorithmException {
		int hashlen = messageDigest.getDigestLength();
		BigInt P = pp.getP();
		BigInt Q = pp.getQ();
		TatePairing tatep = new TatePairing(pp.getEc(), P, Q);
		Point U = (Point) triple.get(0);
		BigInt theta_bi = (BigInt) tatep.compute(U, S_ID);
		int theta = theta_bi.intValue();
		String z = sa.Canonical(p, 0, theta);
		messageDigest.update(z.getBytes());
		String w = new String(messageDigest.digest());
		String V = (String) triple.get(1);
		String rho = sa.xorHex(w, V);
		String W = (String) triple.get(2);
		String m = sa.HashBytes(W.length() / 8, rho, pp.getHash());
		messageDigest.update(m.getBytes());
		String t = new String(messageDigest.digest());
		String rho_t = rho.concat(t);
		int l_temp = sa.HashToRange(rho_t, q, pp.getHash());
		BigInt l = BigInt.valueOf(l_temp);
		if (U == pp.getEc().multiply(pp.getPoint(), l)) {
			return m;
		} else {
			System.out.println("Invalid Cyphercheck");
		}
		return null;
	}

	public Point getS_ID() {
		return S_ID;
	}

	/**
	 * This method is used by the setup algorithm in order to determine some of
	 * the public variables q and p
	 */
	public void determinevariables() {
		System.out.println("Hello");

	System.out.println("Hello 1");
	q = 0 + (int) (Math.random() * ((2 ^ n_q - 0) + 1));
	boolean check1 = this.isPrime(q);
	// checks whether q is prime or not.
	// check if q is a multiple of 2

		

	System.out.println("Hello 2");
	r = 2;
	p = 12 * r * q - 1;
	boolean check2 = this.isPrime(q);
		if (check1 && check2 == true) {
			
		}
		else {
			determinevariables();
		}
		}
	
	boolean isPrime(int n) {
	    for(int i=2;i<n;i++) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}

	}

