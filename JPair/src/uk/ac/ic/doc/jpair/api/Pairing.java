package uk.ac.ic.doc.jpair.api;


import java.math.BigInteger;
import java.util.Random;

import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.EllipticCurve;
import uk.ac.ic.doc.jpair.pairing.Point;
/**
 * Define abstract pairing e: G1 X G2 ->Gt.
 * @author Changyu Dong
 * @version 1.0
 */
public interface Pairing {

    /**
     * Compute pairing.
     * @param P a point from group G1.
     * @param Q a point from group G2.
     * @return an element from Gt which represent the result of e(P,Q).
     */
	public FieldElement compute(Point P, Point Q);
	
    /**
     * Get the cofactor of the Elliptic curve on which G1 is defined. If the order of G1 is {@code q} and the order of the curve is {@code #E}, then the cofactor = {@code #E/q}.
     * @return the cofactor
     */
	public BigInt getCofactor();

    /**
     * Get the elliptic curve on which G1 is defined.
     * @return the Elliptic curve
     */
	public EllipticCurve getCurve();
	
    /**
     * Get the elliptic curve on which G2 is defined.
     * @return the Elliptic curve
     */
	public EllipticCurve getCurve2();
	
    /**
     * Get a random element in G1. 
     * @return a random element in G1
     */
	public Point RandomPointInG1(Random rnd);

    /**
     * Get a random element in G2. 
     * @return a random element in G2
     */
	public Point RandomPointInG2(Random rnd);

    /**
     * Get the order of G1.Usually it is a large prime number. 
     * @return the order of G1.
     */
	public BigInt getGroupOrder();
    /**
     * Get the extension field where the group Gt is taken.
     * @return the extension field
     */
	public Field getGt();
	

}