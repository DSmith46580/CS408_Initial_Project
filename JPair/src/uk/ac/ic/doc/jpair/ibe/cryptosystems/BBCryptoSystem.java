package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
 * This class implements the Boneh-Boyen Identity-Base Encryption (IBE) scheme
 * based on the RFC 5091 Standard. The scheme consists of five main algorithms
 * and several supporting algorithms, the main algorithms are as follows: (1)
 * <b>Setup</b> Randomly selects a master secret and corresponding public
 * parameters based on security parameter 'n' (2) <b>Public Key Derivation</b>
 * Derives a Public key from an identity and public parameters (3)
 * <b>Extract</b> Extracts the private key from an identity, public parameters
 * and the master secret (4) <b>Encrypt</b> encrypts messages using the public
 * key ID and public parameters, (5) <b>Decrypt</b> decrypts messages using the
 * corresponding private key.
 * 
 * This Implementation uses the underlying JPair project for cryptographic
 * primitives developed by Changyu Dong.
 * 
 * @author David Smith
 * @version 1.0
 */

public class BBCryptoSystem {

	static ArrayList<BigInt> secret = new ArrayList<BigInt>();

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
		Point P_prime;
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
		p = fp_p.getP();

		r = new BigInt(n_p, rnd);
		// P_ = sstate.getCurve2().randomPoint(rnd);
		// P = sstate.getCurve2().multiply(P_, BigInt.valueOf(12).multiply(r));
		P = sstate.RandomPointInG1(rnd);
		P_prime= sstate.RandomPointInG2(rnd);
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
		System.out.println("P_1 is on curve : " + sstate.getCurve().isOnCurve(P_1));
		P_2 = sstate.getCurve2().multiply(P_prime, beta);
		System.out.println("P_2 is on curve : " + sstate.getCurve2().isOnCurve(P_2));
		P_3 = sstate.getCurve().multiply(P, gamma);
		System.out.println("P_3 is on curve : " + sstate.getCurve().isOnCurve(P_3));

		secret.add(alpha);
		secret.add(beta);
		secret.add(gamma);

		Complex v = (Complex) sstate.compute(P_1, P_2);
		return new PublicParameter(sstate, p, q, P,P_prime, P_1, P_2, P_3, v, hashfcn);
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
	public static BigInt derivation(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {

		BigInt H_ID = SupportingAlgorithms.HashToRange(ID.getBytes(),
				pp.getqBB(), pp.getHashfcnBB());
		return H_ID;
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
	public static ArrayList<Point> extraction(String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		Random rnd = new Random();
		BigInt r;
		do {
			r = new BigInt(pp.getqBB().bitLength(), rnd);
		} while (r.subtract(pp.getqBB()).signum() == -1);
		BigInt H_ID = derivation(ID, pp);
		System.out.println("Extraction Hid: "+H_ID.toString(16));
		//BigInt y_temp1 = secret.get(0).multiply(secret.get(1)).add(r);
		//BigInt y_temp2 = secret.get(0).multiply(H_ID).add(secret.get(2));
		//BigInt y = y_temp1.multiply(y_temp2);
		BigInt y = secret.get(0).multiply(secret.get(1)).mod(pp.getqBB());
		BigInt y_temp= secret.get(0).multiply(H_ID).mod(pp.getqBB());
		y_temp=y_temp.add(secret.get(2));
		y_temp=y_temp.multiply(r).mod(pp.getqBB());
		y=(y.add(y_temp)).mod(pp.getqBB());
		
		Point D_0 = pp.sstateBB.getCurve2().multiply(pp.getPointBB2(), y);
		System.out.println("D_O is on curve : " + pp.sstateBB.getCurve2().isOnCurve(D_0));
		Point D_1 = pp.sstateBB.getCurve2().multiply(pp.getPointBB2(), r);
		System.out.println("D_1 is on curve : " + pp.sstateBB.getCurve2().isOnCurve(D_1));
		ArrayList<Point> privateKey = new ArrayList<Point>();
		privateKey.add(D_0);
		privateKey.add(D_1);
		return privateKey;
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
	public static  ArrayList encryption(String PT, String ID, PublicParameter pp)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(pp.hashfcnBB);
		Random rnd = new Random();
		BigInt s;
		
		do {
			s = new BigInt(pp.getqBB().bitLength(), rnd);
		} while (s.subtract(pp.getqBB()).signum() == -1);

		Complex w = pp.getVbb().pow(s);
		System.out.println("Enc w: "+ w.toString(16));

		Point C_0 = pp.sstateBB.getCurve().multiply(pp.PointBB, s);
		System.out.println("Enc C_0: "+C_0.toString(16));
		System.out.println("C_O is on curve : " + pp.sstateBB.getCurve().isOnCurve(C_0));


		BigInt h_id = SupportingAlgorithms.HashToRange(ID.getBytes(), pp.qBB,
				pp.hashfcnBB);
		System.out.println("Enc h_id: "+h_id.toString(16));
		
		BigInt y = s.multiply(h_id);
		System.out.println("Enc y: "+y.toString(16));
		
		Point C_1_temp = pp.sstateBB.getCurve().multiply(pp.P_1, y);
		Point C_1_temp2 = pp.sstateBB.getCurve().multiply(pp.P_3, s);
		Point C_1 = pp.sstateBB.getCurve().add(C_1_temp, C_1_temp2);
		System.out.println("Enc C_1: "+C_1.toString(16));
		System.out.println("C_1 is on curve : " + pp.sstateBB.getCurve().isOnCurve(C_1));
		
		byte[] psi = SupportingAlgorithms.Canonical(pp.pBB, 1, w);
		System.out.println("Enc psi: "+new BigInt(1,psi).toString(16));
		
		int l = (int) Math.ceil(pp.pBB.bitLength() / 8);
		
		byte[] x_0 = C_0.getX().toByteArray();
		System.out.println("Enc x_0: "+new BigInt(1,x_0).toString(16));
		byte[] y_0 = C_0.getY().toByteArray();
		System.out.println("Enc y_0: "+new BigInt(1,y_0).toString(16));
		byte[] x_1 = C_1.getX().toByteArray();
		System.out.println("Enc x_1: "+new BigInt(1,x_1).toString(16));
		byte[] y_1 = C_1.getY().toByteArray();
		System.out.println("Enc y_1: "+new BigInt(1,y_1).toString(16));
		
		byte[] zeta = messageDigest.digest(psi);
		System.out.println("Enc zeta: "+new BigInt(1,zeta).toString(16));
		
		byte[] xi = new byte[zeta.length + psi.length];
		System.arraycopy(zeta, 0, xi, 0, zeta.length);
		System.arraycopy(psi, 0, xi, zeta.length, psi.length);
		
		xi=messageDigest.digest(xi);
		System.out.println("Enc xi: "+new BigInt(1,xi).toString(16));
		
		byte[] h_ = new byte[xi.length + zeta.length];
		System.arraycopy(xi, 0, h_, 0, xi.length);
		System.arraycopy(zeta, 0, h_, xi.length, zeta.length);
		System.out.println("Enc h_: "+new BigInt(1,h_).toString(16));

		byte[] pt = PT.getBytes();
		byte[] ybyte =SupportingAlgorithms.xorTwoByteArrays(pt,SupportingAlgorithms.HashBytes(pt.length, h_,
				pp.hashfcnBB));
		System.out.println("Enc ybyte: "+new BigInt(1,ybyte).toString(16));

		byte[] sigma = createByteArray(y_1, x_1, y_0, x_0, ybyte, psi);
		System.out.println("Enc sigma: "+new BigInt(1,sigma).toString(16));
		
		byte[] eta = messageDigest.digest(sigma);
		System.out.println("Enc eta: "+new BigInt(1,eta).toString(16));
		
		byte[] temp_mu = new byte[eta.length + sigma.length];
		System.arraycopy(eta, 0, temp_mu, 0, eta.length);
		System.arraycopy(sigma, 0, temp_mu, eta.length, sigma.length);
		byte[] mu = messageDigest.digest(temp_mu);
		System.out.println("Enc mu: "+new BigInt(1,mu).toString(16));
		
		byte[] h__ = new byte[mu.length + eta.length];
		System.arraycopy(mu, 0, h__, 0, mu.length);
		System.arraycopy(eta, 0, h__, mu.length, eta.length);
		System.out.println("Enc h__: "+new BigInt(1,h__).toString(16));
		
		BigInt rho = SupportingAlgorithms.HashToRange(h__, pp.qBB, pp.getHashfcnBB());
		System.out.println("Enc rho: "+rho.toString(16));
		
		BigInt u = s.add(rho);
		//BigInt u = u_temp.mod(pp.qBB);
		//System.out.println("Enc u_temp: "+u_temp.toString(16));
		System.out.println("Enc u: "+u.toString(16));
		System.out.println("Enc s: "+s.toString(16));
		System.out.println("Enc q_bb: "+pp.qBB.toString(16));
		ArrayList quad = new ArrayList();
		quad.add(u);
		quad.add(C_0);
		quad.add(C_1);
		quad.add(ybyte);
		
	
		return quad;

	}

	private static byte[] createByteArray(byte[] y_1, byte[] x_1, byte[] y_0,
			byte[] x_0, byte[] ybyte, byte[] psi) {
		int length = y_1.length + x_1.length + y_0.length + x_0.length
				+ ybyte.length + psi.length;
		byte[] array = new byte[length];
		System.arraycopy(y_1, 0, array, 0, y_1.length);
		int temp = 0+y_1.length;
		System.arraycopy(x_1, 0, array, (temp), x_1.length);
		temp = temp+x_1.length;
		System.arraycopy(y_0, 0, array, (temp), y_0.length);
		temp = temp+y_0.length;
		System.arraycopy(x_0, 0, array, (temp), x_0.length);
		temp = temp+x_0.length;
		System.arraycopy(ybyte, 0, array, (temp), ybyte.length);
		temp = temp+ybyte.length;
		System.arraycopy(psi, 0, array, (temp), psi.length);
		
		return array;
	}
	
	public static String decryption(ArrayList quad, PublicParameter pp, ArrayList privatekey) throws NoSuchAlgorithmException {
		MessageDigest messageDigest =  MessageDigest.getInstance(pp.hashfcnBB);
		BigInt u = (BigInt) quad.get(0);
		System.out.println("Dec u: "+u.toString(16));
		
		Point C_0 = (Point) quad.get(1);
		System.out.println("Dec C0: "+C_0.toString(16));
		Point C_1 = (Point) quad.get(2);
		System.out.println("Dec C1: "+C_1.toString(16));
		byte[] ybyte = (byte[]) quad.get(3);
		
		Point D_0 = (Point) privatekey.get(0);
		Point D_1 = (Point) privatekey.get(1);
		Complex c = (Complex) pp.sstateBB.compute(C_0, D_0);
		Complex d = (Complex) pp.sstateBB.compute(C_1, pp.getSstateBB().getCurve2().negate(D_1));
		Complex w = c.multiply(d);
		System.out.println("Dec w: "+ w.toString(16));
		
		byte[] psi = SupportingAlgorithms.Canonical(pp.pBB, 1, w);
		System.out.println("Dec psi: "+new BigInt(1,psi).toString(16));

		
		byte[] x_0 = C_0.getX().toByteArray();
		System.out.println("dec x_0: "+new BigInt(1,x_0).toString(16));
		byte[] y_0 = C_0.getY().toByteArray();
		System.out.println("dec y_0: "+new BigInt(1,y_0).toString(16));
		byte[] x_1 = C_1.getX().toByteArray();
		System.out.println("dec x_1: "+new BigInt(1,x_1).toString(16));
		byte[] y_1 = C_1.getY().toByteArray();
		System.out.println("dec y_1: "+new BigInt(1,y_1).toString(16));
		
		byte[] zeta = messageDigest.digest(psi);
		System.out.println("Dex zeta: "+new BigInt(1,zeta).toString(16));

		
		byte[] xi_temp = new byte[zeta.length + psi.length];
		System.arraycopy(zeta, 0, xi_temp, 0, zeta.length);
		System.arraycopy(psi, 0, xi_temp, zeta.length, psi.length);
		byte[] xi = messageDigest.digest(xi_temp);
		System.out.println("Dec xi: "+new BigInt(1,xi).toString(16));

		
		byte[] h_ = new byte[xi.length + zeta.length];
		System.arraycopy(xi, 0, h_, 0, xi.length);
		System.arraycopy(zeta, 0, h_, xi.length, zeta.length);
		System.out.println("dec h_: "+new BigInt(1,h_).toString(16));
		


		
		byte[] m = SupportingAlgorithms.xorTwoByteArrays(ybyte,SupportingAlgorithms.HashBytes(ybyte.length, h_, pp.hashfcnBB));
		
		byte[] sigma = createByteArray(y_1, x_1, y_0, x_0, ybyte, psi);
		System.out.println("dec sigma: "+new BigInt(1,sigma).toString(16));

		
		byte[] eta = messageDigest.digest(sigma);
		System.out.println("dec eta: "+new BigInt(1,eta).toString(16));

		
		byte[] temp_mu = new byte[eta.length + sigma.length];
		System.arraycopy(eta, 0, temp_mu, 0, eta.length);
		System.arraycopy(sigma, 0, temp_mu, eta.length, sigma.length);
		byte[] mu = messageDigest.digest(temp_mu);
		System.out.println("dec mu: "+new BigInt(1,mu).toString(16));
		
		byte[] h__ = new byte[mu.length + eta.length];
		System.arraycopy(mu, 0, h__, 0, mu.length);
		System.arraycopy(eta, 0, h__, mu.length, eta.length);
		System.out.println("Dec h__: "+new BigInt(1,h__).toString(16));

		
		BigInt rho = SupportingAlgorithms.HashToRange(h__, pp.getqBB(), pp.hashfcnBB);
		System.out.println("Dec rho: "+rho.toString(16));
		
		BigInt s = u.subtract(rho);
		System.out.println("Dec U: "+u.toString(16));
		System.out.println("Dec rho: "+rho.toString(16));
		System.out.println("Dec s: "+s.toString(16));
		System.out.println("Dec q_bb: "+pp.getqBB().toString(16));
		//BigInt s = s_temp.mod(pp.getqBB());
		System.out.println("Dec s: "+s.toString(16));
		
		String message = new String(m);
		
		Boolean test1 = false;
		Boolean test2 = false;
		if ( w.equals(pp.getVbb().pow(s))){
			test1=true;
			
		}
		if(C_0.equals( pp.sstateBB.getCurve().multiply(pp.getPointBB(), s))) {
			test2=true;
		}
		System.out.println("test1 "+test1);
		System.out.println("test2 "+test2);
		if(test1&&test2 == true) {
			message = new String(m);
			System.out.println(message);
		}
		else {
			System.out.println("Invalid Cyphercheck");
		}
		
		
		return message;
		
	}

}
