package uk.ac.ic.doc.jpair.ibe.bfcs;

import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;

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

public class BFCryptoSystem implements Field, FieldElement {

	private int n_p;
	private int n_q;
	private int q;
	private int r;
	private int p_;
	
	private int s; //Master Secret
	
	private Field field;
	private FieldElement fe1;
	private FieldElement fe2;
	private String hashfcn;
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
	 */
	public void BFSetup1(int n) {

		if (n == 1024) {
			n_p = 512;
			n_q = 160;
			hashfcn = "1.3.14.3.2.26";
		}

		else if (n == 2048) {
			n_p = 1024;
			n_q = 224;
			hashfcn = "2.16.840.1.101.3.4.2.4";
		}

		else if (n == 3072) {
			n_p = 1536;
			n_q = 256;
			hashfcn = "2.16.840.1.101.3.4.2.1";
		}

		else if (n == 7680) {
			n_p = 3840;
			n_q = 384;
			hashfcn = "2.16.840.1.101.3.4.2.2";
		}

		else if (n == 15360) {
			n_p = 7680;
			n_q = 512;
			hashfcn = "2.16.840.1.101.3.4.2.3";
		}
		determinevariables();
		s = 2 + (int) (Math.random() * (((q-1) - 1 - 2) + 1));
		
	}
	
	/**
	 * The Derivation algorithm.
	 * 
	 * @param ID - Identity String id
	 * @return Q_ID - Point in order q in E(F_p)
	 * 
	 * This method calls upon one of the supporting algorithms
	 */
	public void derivation(String id) {
		Point Q_ID = HashToPoint(E, p_, q, id, hashfcn);
	}

	
	/**
	 * This method is used by the set-up algoritm in order to determine some of the public variables
	 * q,p_ and P
	 */
	public void determinevariables() {
		while (IsPrime1 = false) {
			q = 0 + (int) (Math.random() * ((2 ^ n_q - 1 - 0) + 1));
			// checks whether int is prime or not.
			// check if n is a multiple of 2
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
			p_ = 12 * r * q - 1;
			// checks whether int is prime or not.
			// check if n is a multiple of 2
			if (p_ % 2 == 0)
				IsPrime2 = false;
			// if not, then just check the odds
			for (int i = 3; i * i <= p_; i += 2) {
				if (p_ % i == 0)
					IsPrime2 = false;
			}
			IsPrime2 = true;
		}

		Point p = new Point(fe1, fe2);
		// Select random point p
		// p.setX();
		// p.setY(newY)

		// Point P = [12*r]*p
		// if(p=0) {
		// determinevariables();
		// }
	}
}
