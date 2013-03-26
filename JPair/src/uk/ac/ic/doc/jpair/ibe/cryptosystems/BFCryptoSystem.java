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
import uk.ac.ic.doc.jpair.pairing.Complex;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Fp;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.Predefined;
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
 * @version 1.0
 */

public class BFCryptoSystem {

	static BigInt secret;

	/**
	 * The Setup algorithm. This algorithm generates psuedo random
	 * PublicParameters and the Master Secret.
	 * 
	 * @param n
	 *            - Security Parameter which must be equal to 1024, 2048, 3072,
	 *            7680 or 15360
	 * @return PP - A set of public parameters (sstate, p, q, P, P-pub, hashfcn)
	 *         and the corresponding Master Secret 'S'. 'S' is represented as a
	 *         BigInteger.
	 * @throws NoSuchAlgorithmException
	 */
	static public PublicParameter BFSetup1(int n)
			throws NoSuchAlgorithmException {

		String hashfcn = "";
		EllipticCurve E;
		int n_p = 0;
		int n_q = 0;
		Point P;
		Point P_;
		BigInt q;
		BigInt r;
		BigInt p;

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

		// The Following are not implemented based on the curve used from the
		// JPair Project
		// else if (n == 3072) {
		// n_p = 1536;
		// n_q = 256;
		// hashfcn = "SHA-256";
		// }
		//
		// else if (n == 7680) {
		// n_p = 3840;
		// n_q = 384;
		// hashfcn = "SHA-384";
		// }
		//
		// else if (n == 15360) {
		// n_p = 7680;
		// n_q = 512;
		// hashfcn = "SHA-512";
		// }

		Random rnd = new Random();
		TatePairing sstate = Predefined.ssTate();

		// This can be used if you are not implementing a predefined curve in
		// order to determine the variables p and q;
		// do{
		// q = new BigInt(n_p, 100, rnd);
		// r = new BigInt(n_p, rnd );
		// p = determinevariables(r, q, n_p, rnd);
		// P_ = sstate.getCurve().randomPoint(rnd);
		// P = sstate.getCurve().multiply(P_, BigInt.valueOf(12).multiply(r));
		// } while (P !=null);

		q = sstate.getGroupOrder();
		Fp fp_p = (Fp) sstate.getCurve().getField();
		p = fp_p.getP();

		r = new BigInt(n_p, rnd);
		// P_ = sstate.getCurve2().randomPoint(rnd);
		// P = sstate.getCurve2().multiply(P_, BigInt.valueOf(12).multiply(r));
		P = sstate.RandomPointInG2(rnd);

		do {
			secret = new BigInt(q.bitLength(), rnd);
		} while (secret.subtract(q).signum() == -1);

		Point P_Pub = sstate.getCurve2().multiply(P, secret);
		// System.out.println("secret: "+ secret.toString(16));

		return new PublicParameter(sstate, p, q, P, P_Pub, hashfcn);
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
	public static Point derivation(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {

		Point Q_ID = SupportingAlgorithms.HashToPoint(
				pp.getSstate().getCurve(), pp.getP(), pp.getQ(), ID,
				pp.getHash());
		// if (pp.getSstate().getCurve().isOnCurve(Q_ID)) {
		// System.out.println("Q_ID is correct");
		// } else {
		// System.out.println("Q_ID is not correct");
		// }
		return Q_ID;
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
	public static Point extraction(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		Point Q_ID = derivation(ID, pp);
		Point S_ID = pp.sstate.getCurve().multiply(Q_ID, secret);
		// System.out.println("secret: "+ secret.toString(16));

		// Complex a= (Complex) pp.sstate.compute(Q_ID, pp.getPublic_point());
		// System.out.println(a.toString(16));
		// Complex b= (Complex) pp.sstate.compute(S_ID, pp.getPoint());
		// System.out.println(b.toString(16));
		return S_ID;
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
	public static ArrayList encryption(String PT, String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		// Let hashlen be the length of the output of the cryptographic hash
		// function hashfcn from the public parameters.
		MessageDigest messageDigest = MessageDigest.getInstance(pp.getHash());
		int hashlen = messageDigest.getDigestLength();

		// Q_id = derivation
		Point Q_ID = derivation(ID, pp);
		// System.out.println("Enc Q_ID: "+Q_ID.toString(16));

		// Select a random hashlen-bit vector rho
		byte[] rho = createRho(hashlen);
		// System.out.println("Enc rho: "+new BigInt(1,rho).toString(16));

		// Let t = hashfcn(pt)
		byte[] t = messageDigest.digest(PT.getBytes());
		// System.out.println("Enc t: "+new BigInt(1,t).toString(16));

		// l = HashToRange(rho || t, q, hashfcn)
		// rho || t is the concatenation of rho and t
		byte[] rho_t = new byte[rho.length + t.length];
		System.arraycopy(rho, 0, rho_t, 0, rho.length);
		System.arraycopy(t, 0, rho_t, rho.length, t.length);
		// System.out.println("Enc rho_t: "+new BigInt(1,rho_t).toString(16));
		BigInt l = SupportingAlgorithms.HashToRange(rho_t, pp.getQ(),
				pp.getHash());
		// System.out.println("Enc l: "+l.toString(16));

		// U = [l]P
		Point U = pp.sstate.getCurve2().multiply(pp.getPoint(), l);
		// System.out.println("Enc U: "+U.toString(16));

		// theta = Pairing(E, P_pub, Q_id)
		Complex theta_fp = (Complex) pp.sstate.compute(Q_ID,
				pp.getPublic_point());
		// System.out.println("Enc theta_fp: " + theta_fp.toString(16));

		// theta = theta_fp^l
		Complex theta = theta_fp.pow(l);
		// System.out.println("Enc theta: " + theta.toString(16));

		// z = Canonical(p, 0, theta)
		byte[] z = SupportingAlgorithms.Canonical(pp.getP(), 0, theta);
		// System.out.println("Enc z: " + new BigInt(1, z).toString(16));

		// w = hashfcn(z)
		byte[] w = messageDigest.digest(z);
		// System.out.println("Enc w: " + new BigInt(1, w).toString(16));

		// V = w XOR rho
		byte[] V = SupportingAlgorithms.xorTwoByteArrays(w, rho);
		// System.out.println("Enc V: " + new BigInt(1, V).toString(16));

		// W = HashBytes(length of pt in bytes, rho, hashfcn) XOR m
		byte[] pt = PT.getBytes();
		// System.out.println("Enc m: " + new BigInt(1, pt).toString(16));
		int length_pt = pt.length;
		byte[] temp_W = SupportingAlgorithms.HashBytes(length_pt, rho,
				pp.getHash());
		byte[] W = SupportingAlgorithms.xorTwoByteArrays(pt, temp_W);
		// System.out.println("Enc W: " + new BigInt(1, W).toString(16));

		// String temp_Ws = new String(W);
		// System.out.println(temp_Ws);

		// Create an arraylist and add U,V and W and return it.
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
	public static String decryption(Point S_ID, ArrayList triple,
			PublicParameter pp) throws NoSuchAlgorithmException {
		// Let hashlen be the length of the output of the cryptographic hash
		// function hashfcn from the public parameters.
		// System.out.println("Dec S_ID: " + S_ID.toString(16));
		MessageDigest messageDigest = MessageDigest.getInstance(pp.getHash());
		int hashlen = messageDigest.getDigestLength();

		// take U from triple
		Point U = (Point) triple.get(0);
		// System.out.println("Dec U: " + U.toString(16));

		// theta = Pairing(E, U, S_id)
		Complex theta = (Complex) pp.sstate.compute(S_ID, U);
		// System.out.println("Dec theta: " + theta.toString(16));

		// z = Canonical(p, 0, theta)
		byte[] z = SupportingAlgorithms.Canonical(pp.getP(), 0, theta);
		// System.out.println("Dec z: " + new BigInt(1, z).toString(16));

		// w = hashfcn(z)
		byte[] w = messageDigest.digest(z);
		// System.out.println("Dec w: " + new BigInt(1, w).toString(16));

		// get V from triple
		byte[] V = (byte[]) triple.get(1);
		// System.out.println("Dec V: " + new BigInt(1, V).toString(16));

		// rho = w XOR V
		byte[] rho = SupportingAlgorithms.xorTwoByteArrays(w, V);
		// System.out.println("Dec rho: " + new BigInt(1, rho).toString(16));

		// get W from triple
		byte[] W = (byte[]) triple.get(2);
		// System.out.println("Dec W: " + new BigInt(1, W).toString(16));

		// m = HashBytes(length of W in bytes, rho, hashfcn) XOR W
		byte[] temp_m = SupportingAlgorithms.HashBytes(W.length, rho,
				pp.getHash());
		byte[] m = SupportingAlgorithms.xorTwoByteArrays(temp_m, W);
		// System.out.println("Dec m: " + new BigInt(1, m).toString(16));

		// t = hashfcn(m)
		byte[] t = messageDigest.digest(m);
		// System.out.println("Dec t: " + new BigInt(1, t).toString(16));

		// l = HashToRange(rho || t, q, hashfcn)
		// Concatenation of rho and t
		byte[] rho_t = new byte[rho.length + t.length];
		System.arraycopy(rho, 0, rho_t, 0, rho.length);
		System.arraycopy(t, 0, rho_t, rho.length, t.length);
		// System.out.println("Dec rho_z: " + new BigInt(1,
		// rho_t).toString(16));
		BigInt l = SupportingAlgorithms.HashToRange(rho_t, pp.getQ(),
				pp.getHash());
		// System.out.println("Dec l: " + l.toString(16));

		String message = "";

		// Verify that U = [l]P
		if (U.equals(pp.sstate.getCurve2().multiply(pp.getPoint(), l))) {
			message = new String(m);
			System.out.println(message);
		} else {
			System.out.println("Invalid Cyphercheck");
		}

		return message;
	}

	/**
	 * Used by the encryption and decryption algorithms to create a byte array
	 * rho with a fixed length
	 * 
	 * @param hashlen
	 *            - Length of the hash chosen in the setup algorithm
	 * @return rho - A byte array of hashlen length
	 */
	public static byte[] createRho(int hashlen) {
		Random rand = new Random();
		byte[] rho = new byte[hashlen];
		rand.nextBytes(rho);

		return rho;
	}

	/**
	 * This method is used by the setup algorithm in order to determine some of
	 * the public variables q and p
	 * 
	 * @param q2
	 * @param q
	 * @param n_p
	 * @param rnd
	 * @return
	 */
	public static BigInt determinevariables(BigInt r, BigInt q, int n_p,
			Random rnd) {
		r = new BigInt(n_p, rnd);
		BigInt p = (BigInt.valueOf(12).multiply(r).multiply(q).min(BigInt
				.valueOf(1)));
		boolean prime = p.isProbablePrime(100);
		if (prime = true) {
			return p;
		} else {
			determinevariables(r, q, n_p, rnd);
			return null;

		}

	}
}
