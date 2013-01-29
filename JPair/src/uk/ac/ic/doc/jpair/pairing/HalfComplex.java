package uk.ac.ic.doc.jpair.pairing;


import java.io.Serializable;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;

/**
 * Represents a partial complex number output by Compressed pairing. It contains only the real part of
 * the complex number. 
 * @author changyu Dong
 * @version 1.0
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplex
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplexField 
 */

public class HalfComplex implements FieldElement,Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -9030404520636293251L;
	//public class HalfComplex extends Complex {
	private BigInt real;
	private Fp field;
	
	public HalfComplex(Fp field, BigInt real) {
		this.field=field;
		this.real=real;
		// TODO Auto-generated constructor stub
	}




	HalfComplex add(HalfComplex p) {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.add(this.real, p.real));
	}


	HalfComplex conjugate() {
		// TODO Auto-generated method stub
		return this;
	}

	public Field getField() {
		// TODO Auto-generated method stub
		return this.getField();
	}


	public BigInt getReal() {
		// TODO Auto-generated method stub
		return this.real;
	}


//	public boolean isOne() {
//		// TODO Auto-generated method stub
//		return this.real.equals(BigInt.ONE);
//	}
//
//
//	public boolean isZero() {
//		// TODO Auto-generated method stub
//		return this.real.equals(BigInt.ZERO);
//	}

	 HalfComplex multiply(BigInt p) {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.multiply(this.real, p));
	}


	 HalfComplex multiply(int val) {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.multiply(this.real, val));
	}


	 HalfComplex negate() {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.negate(this.real));
	}


	 HalfComplex pow(BigInt n) {
		// TODO Auto-generated method stub
			BigInt p = this.real.shiftLeft(1);
			BigInt two =BigInt.valueOf(2);
			
			FieldElement v0=two;
			FieldElement v1=p;
			
			int t =n.bitLength()-1;
			
			for(int j=t;j>=0;j--){
				if(n.testBit(j)){
					v0=this.field.subtract(this.field.multiply(v0, v1), p);
					v1= this.field.subtract(this.field.square(v1), two);
				}
				else{
					v1 =this.field.subtract(this.field.multiply(v0, v1), p);
					v0=this.field.subtract(this.field.square(v0), two);
				}
			}
			if(((BigInt) v0).testBit(0)){
			return new HalfComplex(this.field,(BigInt) this.field.multiply(v0,this.field.inverse2));
			}
			else{
				return new HalfComplex(this.field,((BigInt) v0).shiftRight(1));
			}
	}



	 HalfComplex square() {
		// TODO Auto-generated method stub
		return this.pow(BigInt.valueOf(2));
	}


	 HalfComplex subtract(HalfComplex p) {
		// TODO Auto-generated method stub
		return new HalfComplex(this.field,(BigInt) this.field.subtract(this.real, p.real));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+this.real.toString()+"]";
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return this.real.toByteArray();
	}

	@Override
	public String toString(int radix) {
		// TODO Auto-generated method stub
		return "["+this.real.toString(radix)+"]";
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((real == null) ? 0 : real.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HalfComplex other = (HalfComplex) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (real == null) {
			if (other.real != null)
				return false;
		} else if (!real.equals(other.real))
			return false;
		return true;
	}
	

}
