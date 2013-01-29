package uk.ac.ic.doc.jpair.api;

import java.math.BigInteger;
import java.util.Random;

import uk.ac.ic.doc.jpair.pairing.BigInt;
/**
 *  
 * An abstract finite field.
 * @author Changyu Dong
 * @version 1.0
 */
public interface Field {

	/**
	 *  
	 * @return val1+val2
	 */
	public FieldElement add(FieldElement val1, FieldElement val2);

	/**
	 *  
	 * @return 1/val
	 */

	public FieldElement inverse(FieldElement val);

	/**
	 *  
	 * @return val1*val2
	 */
	public FieldElement multiply(FieldElement val1, FieldElement val2);

	/**
	 *  
	 * @return val1/val2
	 */

	public FieldElement divide(FieldElement val1, FieldElement val2);

	/**
	 *  
	 * @return val1-val2
	 */

	public FieldElement subtract(FieldElement val1, FieldElement val2);

	/**
	 *  
	 * @return a random element in this field
	 */
	public FieldElement randomElement(Random rnd);
	/**
	 *  
	 * @return the characteristics of this field
	 */
	public  BigInt getP();

	/**
	 *  
	 * @return true if val is a valid element in this field, otherwise false
	 */
	public boolean isValidElement(FieldElement val);

	//
	//	  /**
	//     *  
	//     * @return val1*val2
	//     */
	public FieldElement multiply(FieldElement val, int val2);

	/**
	 *  
	 * @return -val1
	 */
	public FieldElement negate(FieldElement val);

	/**
	 *  
	 * @return 1
	 */
	public FieldElement getOne();

	/**
	 *  
	 * @return 0
	 */
	public FieldElement getZero();

	/**
	 *  
	 * @return true is val==1, false otherwise
	 */
	public boolean isOne(FieldElement val);

	/**
	 *  
	 * @return true is val==0, false otherwise
	 */
	public boolean isZero(FieldElement val);

	/**
	 *  
	 * @return val^2
	 */
	public FieldElement square(FieldElement val);

	/**
	 * Returns the square root of val or null 
	 * <code>FieldElement</code> value.
	 * 
	 * @param   val   an element in this field.
	 * @return  the square root of <code>val</code> if one exists.
	 *          Otherwise return <code>null</code>.
	 */
	public FieldElement squareRoot(FieldElement val);
	
	/**
	 *  
	 * @return the order of this field
	 */

	public BigInt getOrder();

	/**
	 *  
	 * @return val^exp
	 */
	public FieldElement pow(FieldElement val, BigInt exp);

}