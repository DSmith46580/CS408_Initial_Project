package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import uk.ac.ic.doc.jpair.ibe.supportingalgorithms.SupportingAlgorithms;
import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Complex;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Fp;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.Predefined;
import uk.ac.ic.doc.jpair.pairing.TatePairing;



/**
 * This class implements the Boneh-Boyen Identity-Base Encryption (IBE)
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


public class BBCryptoSystem {

	

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
	static public PublicParameter BFSetup1(int n)
			throws NoSuchAlgorithmException {

		String hashfcn = "";
		EllipticCurve E;
		int n_p = 0;
		int n_q = 0;
		Point P;
		Point P_1;
		Point P_2;
		Point P_3;
		BigInt q;
		BigInt r;
		BigInt p;
		BigInt alpha;
		BigInt beta;
		BigInt gamma;

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
		p=fp_p.getP();

		r = new BigInt(n_p, rnd);
		//P_ = sstate.getCurve2().randomPoint(rnd);
		//P = sstate.getCurve2().multiply(P_, BigInt.valueOf(12).multiply(r));
		P=sstate.RandomPointInG2(rnd);

		do {
			alpha = new BigInt(q.bitLength(), rnd);
		} while (alpha.subtract(q).signum() == -1);

		do {
			beta = new BigInt(q.bitLength(), rnd);
		} while (beta.subtract(q).signum() == -1);
		
		do {
			gamma = new BigInt(q.bitLength(), rnd);
		} while (beta.subtract(q).signum() == -1);
		
		P_1 = sstate.getCurve().multiply(P, alpha);
		P_2 = sstate.getCurve2().multiply(P, beta);
		P_3 = sstate.getCurve2().multiply(P, gamma);
		
		Complex v = (Complex) sstate.compute(P_1, P_2);
		return new PublicParameter(sstate, p, q, P, P_1, P_2, P_3, v, hashfcn);
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

		Point H_ID = SupportingAlgorithms.HashToPoint(pp.getSstate()
				.getCurve(), pp.getP(), pp.getQ(), ID, pp.getHash());
		if(pp.getSstate().getCurve().isOnCurve(H_ID)){
			System.out.println("H_ID is correct");
		}else{
			System.out.println("H_ID is not correct");
		}
		return H_ID;
	}

	
	
}
