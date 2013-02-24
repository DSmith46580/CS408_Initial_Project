package uk.ac.ic.doc.jpair.ibe.cryptosystems;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;

public class PublicParameter {

	EllipticCurve ec;
	BigInt p;
	BigInt q;
	Point point;
	Point public_point;
	String hash;
	int p_int;
	int q_int;

	public PublicParameter(EllipticCurve e, int p1, int q1, Point p2,
			Point p_pub, String hashfcn) {
		// TODO Auto-generated constructor stub
		ec = e;
		p = BigInt.valueOf(p1);
		p_int = p1;
		q = BigInt.valueOf(q1);
		q_int = q1;
		point = p2;
		public_point = p_pub;
		hash = hashfcn;
		
	}

	public int getP_int() {
		return p_int;
	}

	public int getQ_int() {
		return q_int;
	}

	public EllipticCurve getEc() {
		return ec;
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
