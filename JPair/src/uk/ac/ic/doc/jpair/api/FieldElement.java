package uk.ac.ic.doc.jpair.api;

import java.math.BigInteger;
/**
 *  
 * An interface for field element.
 * @author Changyu Dong
 * @version 1.0
 */
public interface FieldElement {

	public byte[] toByteArray();

	public String toString(int radix);

}