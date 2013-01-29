package uk.ac.ic.doc.jpair.pairing;

import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
/**
 * A field for processing the outputs of the compressed Tate pairing. Technically this is not a field but for convenience
 * we call it s field. The elements in a halfComplexField are HalfComplexes, i.e. partial complex numbers with only the
 * real part value (a instead of a+bi). Since the elements contains only partial information, many operations cannot be
 * performed.
 * 
 *<p>The unsupported operations include: division, inversion, square root.</p>
 * <p>Supported operations include (output a HalfComplex): addtion of two HalfComplexes, substraction of two HalfComplexes,
 * multiplication a HalfComplex with a BigInt, negation, exponentiation (pow) a HalfComplex when it is a unitary (the output
 * of the Compressed Tate Pairing must be a unitary), square of a HalfComplex when it is a unitary. </p>
 * This field should only be used to process the outputs of the compressed Tate Pairing. More information about the compressed
 * Tate pairing, see Michael Scott, Paulo S. L. M. Barreto: Compressed Pairings. CRYPTO 2004: 140-156.
 * @author Changyu Dong
 * @version 1.0
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplex
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplexField
 *
 */
public class HalfComplexField implements Field {
	Fp field;
    /**
     * Creates a compressed finite field Fp^2
     * @param base the base field Fp.
     * 
     */
	public HalfComplexField(Fp base){
		this.field=base;
	}


	/**
	 * Return the sum of two HalfComplxes.
	 */
	@Override
	public FieldElement add(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		if (!(val1 instanceof HalfComplex)||(!(val2 instanceof HalfComplex)))
			throw new IllegalArgumentException("The inputs must be HalfComplex numbers");
		return ((HalfComplex)val1).add((HalfComplex )val2);
	}

	
	/**
	 * Not supported, always throws an ArithmeticException.
	 */
	@Override
	public FieldElement divide(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		throw new ArithmeticException("Division in a HalfCompressedField is not supported!");
	}

	@Override
	public FieldElement getOne() {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.getOne());
	}

	@Override
	public BigInt getOrder() {
		// TODO Auto-generated method stub
		return this.field.getOrder().multiply(this.field.getOrder());
	}

	@Override
	public BigInt getP() {
		// TODO Auto-generated method stub
		return this.field.getP();
	}

	@Override
	public FieldElement getZero() {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.getZero());
	}
	/**
	 * Not supported. Always throws an ArithmeticException.
	 */
	@Override
	public FieldElement inverse(FieldElement val) {
		// TODO Auto-generated method stub
		throw new ArithmeticException("Inversion in a HalfCompressedField is not supported!");
	}

	@Override
	public boolean isOne(FieldElement val) {
		// TODO Auto-generated method stub
		throw new ArithmeticException("isOne in a HalfCompressedField is not supported!");
	}

	@Override
	public boolean isValidElement(FieldElement val) {
		// TODO Auto-generated method stub
		return (val instanceof HalfComplex)&& ((HalfComplex)val).getField().equals(this.field);
	}

	@Override
	public boolean isZero(FieldElement val) {
		// TODO Auto-generated method stub
		throw new ArithmeticException("isZero in a HalfCompressedField is not supported!");
	}
	/**
	 * One input value must be a HalfComplex and the other one must be a BigInt.
	 * Assume that the input HalfComplex represents the real part of a complex number a+bi,
	 * the input BigInt is c, then the ouput is a*c mod p, where p is the characteristics 
	 * of this field.
	 */
	@Override
	public FieldElement multiply(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		HalfComplex c = null;
		BigInt i = null;
		if(val1 instanceof HalfComplex){
			if(val2 instanceof HalfComplex){
				throw new IllegalArgumentException("The inputs cannot be both HalfComplex numbers");
			}
			if(val2 instanceof BigInt){
				c=(HalfComplex) val1;
				i=(BigInt) val2;
			}
			else
				throw new IllegalArgumentException("The second input is of a unsupported type");				
		}
		
		if(val2 instanceof HalfComplex){
			if(val1 instanceof HalfComplex){
				throw new IllegalArgumentException("The inputs cannot be both HalfComplex numbers");
			}
			if(val1 instanceof BigInt){
				c=(HalfComplex) val2;
				i=(BigInt) val1;
			}
			else
				throw new IllegalArgumentException("The first input is of a unsupported type");				
		}
		
		return c.multiply(i);
	}

	@Override
	public FieldElement multiply(FieldElement val, int val2) {
		// TODO Auto-generated method stub
		if (!(val instanceof HalfComplex))
			throw new IllegalArgumentException("The first input must be of a HalfComplex");	
		return ((HalfComplex)val).multiply(val2);
	}
	/**
	 * Negate a HalfComplex. Assume that the input HalfComplex represents the real part of a complex number a+bi, the output is -a
	 */
	@Override
	public FieldElement negate(FieldElement val) {
		// TODO Auto-generated method stub
		if (!(val instanceof HalfComplex))
			throw new IllegalArgumentException("The input must be of a HalfComplex");	
		return ((HalfComplex)val).negate();
	}
	/**
	 * Raise {@code val} to the power of {@code exp} by computing the Lucas Sequence. {@code val} must be a unitary.
	 */
	@Override
	public FieldElement pow(FieldElement val, BigInt exp) {
		// TODO Auto-generated method stub
		if (!(val instanceof HalfComplex))
			throw new IllegalArgumentException("The input must be of a HalfComplex");	
		return ((HalfComplex)val).pow(exp);
	}

	@Override
	public FieldElement randomElement(Random rnd) {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.randomElement(rnd));
	}
	/**
	 * Sqaure {@code val} by computing the Lucas Sequence. {@code val} must be a unitary.
	 */
	@Override
	public FieldElement square(FieldElement val) {
		// TODO Auto-generated method stub
		if (!(val instanceof HalfComplex))
			throw new IllegalArgumentException("The input must be of a HalfComplex");
		return ((HalfComplex)val).square();
	}
	/**
	 * Not supported. Always throws an ArithmeticException.
	 */
	@Override
	public FieldElement squareRoot(FieldElement val) {
		// TODO Auto-generated method stub
		throw new ArithmeticException("squareRoot in a HalfCompressedField is not supported!");
	}
	/**
	 * Return val1.getReal()-val2.getReal().
	 */
	@Override
	public FieldElement subtract(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		if (!(val1 instanceof HalfComplex)||(!(val2 instanceof HalfComplex)))
			throw new IllegalArgumentException("The inputs must be HalfComplex numbers");
		return ((HalfComplex)val1).subtract((HalfComplex )val2);
	}

}
