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
 * @version 0.1
 */

public class BFCryptoSystem {

	static BigInt secret;

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
	static public PublicParameter BFSetup1(int n) throws NoSuchAlgorithmException {

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

		// The Following are not implemented based on the curve used from the JPair Project
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

		MessageDigest messageDigest = MessageDigest.getInstance(hashfcn);
        Random rnd =new Random();
		TatePairing sstate = Predefined.ssTate();
		do{
		q = new BigInt(n_p, 100, rnd);
		r = new BigInt(n_p, rnd );
		p = determinevariables(r, q, n_p, rnd);
		P_ = sstate.getCurve().randomPoint(rnd);
		P =  sstate.getCurve().multiply(P_, BigInt.valueOf(12).multiply(r));
		} while (P !=null);
		int range = q.intValue() -1;
		secret = new BigInt(range,rnd);
		Point P_Pub = sstate.getCurve().multiply(P, secret);
		
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
	public Point derivation(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {

		 Point Q_ID = SupportingAlgorithms.HashToPoint(pp.getSstate().getCurve2(), pp.getP(), pp.getQ(), ID,
				pp.getHash());
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
	public Point extraction(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		Point Q_ID = SupportingAlgorithms.HashToPoint(pp.getSstate().getCurve2(), pp.getP(), pp.getQ(), ID,
				pp.getHash());
		Point S_ID = pp.sstate.getCurve().multiply(Q_ID, secret);
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
	public ArrayList encryption(String PT, String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(pp.getHash());
		int hashlen = messageDigest.getDigestLength();
		Point Q_ID = SupportingAlgorithms.HashToPoint(pp.getSstate().getCurve2(), pp.getP(), pp.getQ(), ID,
				pp.getHash());

		byte[] rho = createRho(hashlen);
		messageDigest.update(PT.getBytes());
		byte[] t = messageDigest.digest();
		byte[] rho_t = new byte[rho.length + t.length];
		System.arraycopy(rho, 0, rho_t, 0, rho.length);
		System.arraycopy(t, 0, rho_t, rho.length, t.length);
		BigInt l = SupportingAlgorithms.HashToRange(rho_t, pp.getQ(), pp.getHash());
		Point U = pp.sstate.getCurve().multiply(pp.getPoint(), l);

		//Hit a small brick wall with this part of the encryption implementation, the standards says 'Let theta 
		//= Pairing(E, p, q, P_pub, Q_id), which is an element of
	    // the extension field F_p^2 obtained using the modified Tate pairing
	    //of Algorithm 4.5.1 (Pairing) So just wondering if what I'm doing here is correct
		TatePairing tatep = new TatePairing(pp.sstate.getCurve2(), pp.getP(), pp.getQ());
		Complex theta_fp = (Complex) tatep.compute(pp.getPublic_point(), Q_ID);
		BigInt theta = theta_fp.getReal();

		BigInt theta_ = theta.pow(l.intValue());
		
		//Another question, I'm not exactly sure what I'm doing in the Canonical method, please see comments in that.
		byte[] z = SupportingAlgorithms.Canonical(pp.getP(),0,theta_);
		
		messageDigest.update(z);
		byte[] w = messageDigest.digest();
		byte[] V = SupportingAlgorithms.xorTwoByteArrays(w,rho);
		
		byte[] pta = PT.getBytes();
		int length_pta = pta.length;
		
		byte[] W = SupportingAlgorithms.HashBytes(length_pta,rho,pp.getHash());
		
		
		ArrayList tuple = new ArrayList();
		tuple.add(U);
		tuple.add(V);
		tuple.add(W);
		return tuple;
	}

	public byte[] createRho(int hashlen) {
		System.out.println(hashlen);
		Random rand = new Random();
		byte[] rho = null;
		for (int i = 0; i < hashlen; i++) {
			System.out.println(i);
			rho[i] = (byte) rand.nextInt();
		}
		
		
		return rho;
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
		MessageDigest messageDigest = MessageDigest.getInstance(pp.getHash());
		int hashlen = messageDigest.getDigestLength();
		
		//Same question remains here with regards to pairing.
		TatePairing tatep = new TatePairing(pp.sstate.getCurve2(), pp.getP(), pp.getQ());
		Point U = (Point) triple.get(0);
		Complex theta_fp = (Complex) tatep.compute(U, S_ID);
		BigInt theta_ = theta_fp.getReal();

		byte[] z = SupportingAlgorithms.Canonical(pp.getP(),0,theta_);
		
		byte[] w = messageDigest.digest(z);
		byte[] V = (byte[]) triple.get(1);
		byte[] rho = SupportingAlgorithms.xorTwoByteArrays(w, V);
		
		
		byte[] W = (byte[]) triple.get(2);
		int w_length = w.length;

		byte[] m = SupportingAlgorithms.HashBytes(w_length,rho,pp.getHash());
		byte[] t = messageDigest.digest(m);
		
		byte[] rho_t = new byte[rho.length + t.length];
		System.arraycopy(rho, 0, rho_t, 0, rho.length);
		System.arraycopy(t, 0, rho_t, rho.length, t.length);
		
		
		
		BigInt l = SupportingAlgorithms.HashToRange(rho_t, pp.getQ(), pp.getHash());
		
		if (U == pp.sstate.getCurve().multiply(pp.getPoint(), l)) {
			return m.toString();
		} else {
			System.out.println("Invalid Cyphercheck");
		}
		System.out.println(m);
		return null;
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
