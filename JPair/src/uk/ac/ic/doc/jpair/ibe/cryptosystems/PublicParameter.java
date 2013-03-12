package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Complex;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.TatePairing;

public class PublicParameter {

	//Used for the BF CryptoSystem
	TatePairing sstate;
	BigInt p;
	BigInt q;
	Point point;
	Point public_point;
	String hash;
	
	public PublicParameter(TatePairing sstate2, BigInt p2, BigInt q2, Point p3,
			Point p_Pub, String hashfcn) {
		sstate = sstate2;
		p = p2;
		q = q2;
		point = p3;
		public_point = p_Pub;
		hash = hashfcn;
	}

	public TatePairing getSstate() {
		return sstate;
	}


	public BigInt getP() {
		return p;
	}


	public BigInt getQ() {
		return q;
	}


	public Point getPoint() {
		return point;
	}


	public Point getPublic_point() {
		return public_point;
	}


	public String getHash() {
		return hash;
	}

	//Used for BB CryptoSystem
	
	TatePairing sstateBB;
	BigInt pBB;
	BigInt qBB;
	Point PointBB;
	Point P_1;
	Point P_2;
	Point P_3;
	Complex vbb;
	String hashfcnBB;
	
	public PublicParameter(TatePairing sstate2, BigInt p2, BigInt q2, Point p3,
			Point p_1, Point p_2, Point p_3, Complex v, String hashfcn) {
		sstateBB = sstate2;
		pBB = p2;
		qBB = q2;
		PointBB = p3;
		P_1 = p_1;
		P_2 = p_2;
		P_3 = p_3;
		vbb = v;
		hashfcnBB = hashfcn;
	}

	public TatePairing getSstateBB() {
		return sstateBB;
	}

	public BigInt getpBB() {
		return pBB;
	}

	public BigInt getqBB() {
		return qBB;
	}

	public Point getPointBB() {
		return PointBB;
	}

	public Point getP_1() {
		return P_1;
	}

	public Point getP_2() {
		return P_2;
	}

	public Point getP_3() {
		return P_3;
	}

	public Complex getVbb() {
		return vbb;
	}

	public String getHashfcnBB() {
		return hashfcnBB;
	}


	
	
	

}