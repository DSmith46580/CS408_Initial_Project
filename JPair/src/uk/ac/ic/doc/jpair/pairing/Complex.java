package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;





/**
 * This immutable class defines complex numbers over a finite field, i.e. the real and imaginary parts of the complex number are taken over the finite field.
 * 
 * @author Changyu Dong
 * @version 1.0
 */

public class Complex implements Serializable, FieldElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4344206944117784465L;
	Fp field;
	BigInt real;
	BigInt imag;



	public Complex(Fp field, BigInt real) {
		this.field = field;
		if (!this.field.isValidElement(real))
			throw new IllegalArgumentException(
					"The input values are not in the field.");

//		if (this.field.isZero(real))
//			this.degree = -1;
//		else
//			this.degree = 0;
		this.real= real;
		this.imag=BigInt.ZERO;

	}
	
	public Complex(Fp field, BigInt real,BigInt imag) {

//		if (this.field.isZero(real)&&this.field.isZero(imag))
//			this.degree = -1;
//		else
//			this.degree = 0;
		this.field=field;
		this.real= real;
		this.imag=imag;


	}


	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#isZero()
	 */
	public boolean isZero() {
		return this.field.isZero(imag) & this.field.isZero(this.real);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#isOne()
	 */
	public boolean isOne() {
		return this.field.isZero(imag) & this.field.isOne(this.real);

	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#add(uk.ac.ic.doc.jpbc.pairing.Complex)
	 */
	public Complex add(Complex  p) {
		if (!((Complex) p).getField().equals(this.field))
			throw new IllegalArgumentException(
					"The input polynomial is over another field.");

		if (this.isZero())
			return p;
		else if (((Complex) p).isZero())
			return this;
		
		BigInt newReal = (BigInt) this.field.add(this.real, ((Complex)p).real);
		BigInt newImag = (BigInt) this.field.add(this.imag, ((Complex)p).imag);
		
		return new Complex(this.field,newReal,newImag);

	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#substract(uk.ac.ic.doc.jpbc.pairing.Complex)
	 */
	public Complex  subtract(Complex  p) {
		if (!((Complex) p).getField().equals(this.field))
			throw new IllegalArgumentException(
					"The input polynomial is over another field.");

		if (((Complex) p).isZero())
			return this;
		BigInt newReal = (BigInt) this.field.subtract(this.real, ((Complex)p).real);
		BigInt newImag = (BigInt) this.field.subtract(this.imag, ((Complex)p).imag);
		
		return new Complex(this.field,newReal,newImag);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#negate()
	 */
	public Complex  negate() {
		if (this.isZero())
			return this;



		return new Complex(this.field,(BigInt)this.field.negate(this.real), (BigInt)this.field.negate(this.imag));
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#multiply(uk.ac.ic.doc.jpbc.pairing.Complex)
	 */
	public Complex  multiply(Complex  p) {
		if (!((Complex) p).getField().equals(this.field))
			throw new IllegalArgumentException(
					"The input polynomial is over another field.");

		// shortcuts
		if (this.isZero())
			return this;

		if (((Complex) p).isZero())
			return p;

		if (this.isOne())
			return p;

		if (((Complex) p).isOne())
			return this;
		
		if(p.equals(this))
			return (Complex) this.square();

		//(a+bi)*(c+di)=ac+adi+bci-bd
		//(a + ib)(c + id) = ac - bd + i[(a + b)(c + d) - ac - bd)]
		
		//BigInt newReal =(BigInt) this.field.substract(this.field.multiply(this.real, p.real), this.field.multiply(this.imag, p.imag));
		//BigInt newImag = (BigInt) this.field.add(this.field.multiply(this.imag, p.real), this.field.multiply(this.real, p.imag));
		BigInt ac = this.real.multiply(((Complex)p).real);
		BigInt bd = this.imag.multiply(((Complex)p).imag);
		BigInt newReal =(BigInt) this.field.subtract(ac, bd);
		
		BigInt newImag = this.real.add(this.imag);
		newImag=newImag.multiply(((Complex)p).real.add(((Complex)p).imag));
		newImag = newImag.subtract(ac);
		newImag = (BigInt) this.field.subtract(newImag, bd);
		return new Complex(this.field,newReal,newImag);
	}


/* (non-Javadoc)
 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#square()
 */
public Complex  square() {
		// TODO Auto-generated method stub
	//0 or 1
	if(this.isOne()||this.isZero())
		return this;
	//real number
	if(this.imag.equals(BigInt.ZERO)){
		BigInt newReal = (BigInt) this.field.multiply(this.real, this.real);
		return new Complex(this.field,newReal,BigInt.ZERO);
	}
	//imaginary
	if(this.real.equals(BigInt.ZERO)){
		BigInt newReal = (BigInt) this.field.multiply(this.imag, this.imag);
		newReal=(BigInt) this.field.negate(newReal);
		return new Complex(this.field,newReal,BigInt.ZERO);
	}
	
	//(a+bi)^2=(a+b)(a-b)+2abi
		BigInt newReal = (BigInt) this.field.multiply(this.real.add(this.imag), this.real.subtract(this.imag));
		//BigInt newImag =(BigInt) this.field.multiply(this.real.multiply(BigInt.valueOf(2)), this.imag);
		BigInt newImag =(BigInt) this.field.multiply(this.field.add(this.real,this.real), this.imag);
		return new Complex(this.field,newReal,newImag);
	}




	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#divide(uk.ac.ic.doc.jpbc.pairing.Complex)
	 */
	public Complex  divide(Complex  p) {
		if (!((Complex) p).getField().equals(this.field))
			throw new IllegalArgumentException(
					"The input polynomial is over another field.");

		if (((Complex) p).isZero()) {
			throw new ArithmeticException("Divided by Zero.");
		}


		if (this.isZero()) {

			return new Complex(this.field,BigInt.ZERO,BigInt.ZERO);

		}

		if (((Complex) p).isOne()) {
			return  this;
		}
		
		Complex conj =((Complex)p).conjugate();
		
		Complex top =(Complex) this.multiply(conj);
		BigInt bottom = (BigInt) this.field.add(this.field.square(((Complex)p).real),this.field.square(((Complex)p).imag));
		
		return new Complex(this.field,(BigInt)this.field.divide(top.real, bottom),(BigInt)this.field.divide(top.imag, bottom));

	}
	/**
	 * Return the conjugate of this complex number. If this is {@code a+bi}, then the conjugate is {@code a-bi}. 
	 * @return the conjugate. 
	 */
	public Complex conjugate() {
		return new Complex(this.field,this.real,(BigInt)this.field.negate(this.imag));
	}


	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#pow(java.math.BigInt)
	 */
	public Complex  pow(BigInt k) {
		
    	if(k.equals(BigInt.ZERO))
    		return new Complex(this.field,(BigInt)this.field.getOne(),(BigInt)this.field.getZero());
    	if(k.equals(BigInt.ONE))
    		return this;
    	if(this.isOne()||this.isZero())
    		return this;


		boolean inverse = false;
		if (k.signum() == -1) {
			k = k.abs();
			inverse = true;
		}


		Complex u =this;
		int windowSize =5;
		int tLength = (int) Math.pow(2, windowSize-1);
		//prepare table for windowing
		FieldElement u2 = u.square();
		Complex[] t = new Complex [tLength];
		t[0]=u;
		
		for(int i=1;i<tLength;i++){
			t[i]=(Complex) ((Complex) u2).multiply(t[i-1]);
		}
		//left to right method -with windows
		
		int nb = k.bitLength();
		
		int[] nbw =new int[1];
		int[] nzs = new int[1];
		int n;
		if (nb>1)
			for(int i=nb-2;i>=0;){
				n=window(k,i,nbw,nzs,windowSize);
				for(int j=0;j<nbw[0];j++)
					u=(Complex) u.square();
				if (n>0)
					u=(Complex) u.multiply(t[n/2]);
				i-=nbw[0];
				if(nzs[0]!=0){
					for(int j=0;j<nzs[0];j++)
						u=(Complex) u.square();
					i-=nzs[0];
					
				}
			}

		if (inverse) {
			return u.inverse();
		}

		return u;

	}
	

	
	private int window(BigInt x, int i, int[] nbs, int[] nzs, int w) {
		// TODO Auto-generated method stub
		int j,r;
		nbs[0]=1;
		nzs[0]=0;
		byte[] xa =x.toByteArray();
		
		//check for leading 0 bit
		if(!ByteArrayUtil.getBitByDegree(i, xa))
			return 0;
		//adjust window if not enough bits left
		if(i-w+1<0)
			w=i+1;
		
		r=1;
		for(j=i-1;j>i-w;j--){
			nbs[0]++;
			r*=2;
			if(ByteArrayUtil.getBitByDegree(j, xa))
				r+=1;
			if(r%4==0){
				r/=4;
				nbs[0]-=2;
				nzs[0]=2;
				break;
			}
		}
		
		if(r%2==0){
			r/=2;
			nzs[0]=1;
			nbs[0]--;
		}
		return r;
		
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#inverse()
	 */
	public Complex  inverse() {
	// TODO Auto-generated method stub
	return new Complex(this.field,BigInt.ONE,BigInt.ZERO).divide(this);
}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#multiply(int)
	 */
	public Complex  multiply(int val){
		if(val <0)
			throw new IllegalArgumentException ("oprand must not be negative");
		if (val==0)
			return new Complex(this.field,BigInt.ZERO,BigInt.ZERO);
		if(val==1)
			return this;

		
		return new Complex(this.field,(BigInt)this.field.multiply(this.real, val),(BigInt)this.field.multiply(this.imag, val));
			
		
	}
	

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.FieldElement#getField()
	 */
	public Field getField(){
		return this.field;
	}
/**
 * Return a string in the format of [a,b] where a and b are the real and imaginary parts of the complex number a+bi.
 * a and b are in the given radix.
 */
	public String toString() {
		// TODO Auto-generated method stub
		return "["+this.real.toString()+","+this.imag.toString()+"]";

	}
	/**
	 * Return a string in the format of [a,b] where a and b are the real and imaginary parts of the complex number a+bi.
	 * a and b are in decimal format.
	 */
	public String toString(int radix) {
		// TODO Auto-generated method stub
		return "["+this.real.toString(radix)+","+this.imag.toString(radix)+"]";

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((imag == null) ? 0 : imag.hashCode());
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
		Complex other = (Complex) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (imag == null) {
			if (other.imag != null)
				return false;
		} else if (!imag.equals(other.imag))
			return false;
		if (real == null) {
			if (other.real != null)
				return false;
		} else if (!real.equals(other.real))
			return false;
		return true;
	}
	
	public BigInt getReal(){
		return this.real;
	}
	
	public BigInt getImag(){
		return this.imag;
	}
	
	/**
	 * Return a single byte array which is equivalent to this.real.toByteArray()||this.imag.toByteArray();
	 */
	public byte[] toByteArray(){
		byte[] r = this.real.toByteArray();
		byte[] i = this.imag.toByteArray();
		
		byte[] all = new byte[r.length+i.length];
		System.arraycopy(r, 0, all, 0, r.length);
		System.arraycopy(i, 0, all, r.length, i.length);
		return all;
	}
	HalfComplex toHalfComplex(){
		return new HalfComplex(this.field,this.real);
	}
	
//	BigInt powl(BigInt n){
//		BigInt p = this.real.shiftLeft(1);
//		BigInt two =BigInt.valueOf(2);
//		
//		FieldElement v0=two;
//		FieldElement v1=p;
//		
//		int t =n.bitLength()-1;
//		
//		for(int j=t;j>=0;j--){
//			if(n.testBit(j)){
//				v0=this.field.substract(this.field.multiply(v0, v1), p);
//				v1= this.field.substract(this.field.square(v1), two);
//			}
//			else{
//				v1 =this.field.substract(this.field.multiply(v0, v1), p);
//				v0=this.field.substract(this.field.square(v0), two);
//			}
//		}
//		if(((BigInt) v0).testBit(0)){
//		return (BigInt) this.field.multiply(v0,this.field.inverse2);
//		}
//		else{
//			return ((BigInt) v0).shiftRight(1);
//		}
//		
//		
//	}

}
