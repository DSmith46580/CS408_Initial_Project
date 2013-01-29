package uk.ac.ic.doc.jpair.pairing;

import java.util.Random;


import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
/**
 * The extension field Fp^2, effectively represented as a field of complex numbers over Fp.
 * @author Changyu Dong
 * @version 1.0
 */
public class ComplexField implements Field {
	Fp field;
    /**
     * Creates a finite field Fp^2
     * @param base the base field Fp.
     * 
     */
	public ComplexField(Fp base){
		this.field=base;
	}

	@Override
	public FieldElement add(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		if (!(val1 instanceof Complex)||(!(val2 instanceof Complex)))
			throw new IllegalArgumentException("The inputs must be Complex numbers");
		return ((Complex)val1).add((Complex )val2);
	}

	@Override
	public FieldElement divide(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		if (!(val1 instanceof Complex)||(!(val2 instanceof Complex)))
			throw new IllegalArgumentException("The inputs must be Complex numbers");
		if(((Complex) val2).isZero())
			throw new IllegalArgumentException("Divide by Zero");
		return ((Complex)val1).divide((Complex )val2);
	}

	@Override
	public FieldElement getOne() {
		// TODO Auto-generated method stub
		return new Complex(this.field,BigInt.ONE);
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
		return new Complex(this.field,BigInt.ZERO);
	}

	@Override
	public FieldElement inverse(FieldElement val) {
		// TODO Auto-generated method stub
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The input must be a Complex number");
		if(((Complex) val).isZero())
			throw new IllegalArgumentException("Zero cannot be inversed");
		return ((Complex)val).inverse();
	}

	@Override
	public boolean isOne(FieldElement val) {
		if(val instanceof Complex)
		// TODO Auto-generated method stub
		return ((Complex)val).isOne();
		else
			return false;
	}

	@Override
	public boolean isValidElement(FieldElement val) {
		if(val instanceof Complex)
		// TODO Auto-generated method stub
		return ((Complex)val).getField().equals(this.field);
		else
			return false;
	}

	@Override
	public boolean isZero(FieldElement val) {
		// TODO Auto-generated method stub
		if(val instanceof Complex)
			return ((Complex)val).isZero();
		else 
			return false;
	}

	@Override
	public FieldElement multiply(FieldElement val1, FieldElement val2) {
		if (!(val1 instanceof Complex)||(!(val2 instanceof Complex)))
			throw new IllegalArgumentException("The inputs must be Complex numbers");
		// TODO Auto-generated method stub
		return ((Complex)val1).multiply((Complex )val2);
	}

	@Override
	public FieldElement multiply(FieldElement val, int val2) {
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The first input must be a Complex number");
		// TODO Auto-generated method stub
		return ((Complex)val).multiply(val2);
	}

	@Override
	public FieldElement negate(FieldElement val) {
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The inputsmust be a Complex number");
		// TODO Auto-generated method stub
		return ((Complex)val).negate();
	}

	@Override
	public FieldElement pow(FieldElement val, BigInt exp) {
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The first input must be a Complex number");
		// TODO Auto-generated method stub
		return ((Complex)val).pow((BigInt) exp);
	}

	@Override
	public FieldElement randomElement(Random rnd) {
		// TODO Auto-generated method stub
		return new Complex(this.field,(BigInt)this.field.randomElement(rnd),(BigInt)this.field.randomElement(rnd));
	}

	@Override
	public FieldElement square(FieldElement val) {
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The inputsmust be a Complex number");
		// TODO Auto-generated method stub
		return ((Complex)val).square();
	}

	@Override
	public FieldElement squareRoot(FieldElement val) {
		if (!(val instanceof Complex))
			throw new IllegalArgumentException("The inputsmust be a Complex number");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldElement subtract(FieldElement val1, FieldElement val2) {
		if (!(val1 instanceof Complex)||(!(val2 instanceof Complex)))
			throw new IllegalArgumentException("The inputs must be Complex numbers");
		// TODO Auto-generated method stub
		return ((Complex)val1).subtract((Complex )val2);
	}

}
