package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.TatePairing;

public class PublicParameter {

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
	
	

}