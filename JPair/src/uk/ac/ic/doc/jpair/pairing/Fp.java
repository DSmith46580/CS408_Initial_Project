package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;
import java.util.Random;


import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;





/**
 * This class defines finite fields of characteristics p, where p is a prime number. All the arithmetic operations are done modulo p.
 * @author Changyu Dong
 * @version 1.0
 */
public class Fp implements Serializable, Field {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2015625977771021797L;
	private BigInt r;
	BigInt inverse2;
	
	//for Barrett reduction
	//private BigInteger u;
	//private int k;
    /**
     * Creates a finite field
     * @param p the characteristics of this field.
     * @exception IllegalArgumentException if <code>p</code> is not prime.
     */

	public Fp(BigInt p){
		if (!p.isProbablePrime(40))
			throw new IllegalArgumentException("The modulus must be a prime number.");
		this.r=p;
		this.inverse2=BigInt.valueOf(2).modInverse(p);
		
		//for Barrett reduction
		//k = ((p.bitLength()-1)>>>5)+1;
		//int[] b2kI = new int[2*k+1];
		//b2kI[0]=1;
		
		//BigInteger b2k = new BigInteger(b2kI,1);
		
		//u=b2k.divide(p);
		
		
	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#add(java.math.BigInteger, java.math.BigInteger)
	 */
	public FieldElement add(FieldElement val1, FieldElement val2) {
		if (!(val1 instanceof BigInt)||(!(val2 instanceof BigInt)))
			throw new IllegalArgumentException("The inputs must be BigInts");
		if(val1.equals(BigInt.ZERO))
			return val2;
		if(val2.equals(BigInt.ZERO))
			return val1;
		BigInt result =((BigInt) val1).add((BigInt) val2);
		//if (result.bitLength()<this.r.bitLength())
		//	return result;
		//else
		return (result).mod(r);
	}
	
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#inverse(java.math.BigInteger)
	 */

	public FieldElement inverse(FieldElement val) {
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The input must be a BigInt");
		if(val.equals(BigInt.ZERO))
			throw new IllegalArgumentException("Zero cannot be inversed");
		if(val.equals(BigInt.ONE))
			return val;
		

		return ((BigInt) val).modInverse(r);
	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#multiply(java.math.BigInteger, java.math.BigInteger)
	 */
	public FieldElement multiply(FieldElement val1, FieldElement val2) {
		if (!(val1 instanceof BigInt)||(!(val2 instanceof BigInt)))
			throw new IllegalArgumentException("The inputs must be BigInts");		
		if(((BigInt) val1).signum()==0||((BigInt) val2).signum()==0)
			return BigInt.ZERO;
		if(val1.equals(BigInt.ONE))
			return val2;
		if(val2.equals(BigInt.ONE))
			return val1;

		BigInt result =((BigInt) val1).multiply((BigInt) val2);
		//if (result.bitLength()<this.r.bitLength())
		//	return result;
		//else
		return (result).mod(r);
	}
	
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#divide(java.math.BigInteger, java.math.BigInteger)
	 */
	
	public FieldElement divide(FieldElement val1, FieldElement val2) {
		// TODO Auto-generated method stub
		if (!(val1 instanceof BigInt)||(!(val2 instanceof BigInt)))
			throw new IllegalArgumentException("The inputs must be BigInts");
		return (((BigInt) val1).multiply((BigInt) this.inverse(val2))).mod(r);
	}
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#substract(java.math.BigInteger, java.math.BigInteger)
	 */

	public FieldElement subtract(FieldElement val1, FieldElement val2) {
		if (!(val1 instanceof BigInt)||(!(val2 instanceof BigInt)))
			throw new IllegalArgumentException("The inputs must be BigInts");
		// TODO Auto-generated method stub
		if(val2.equals(BigInt.ZERO))
			return ((BigInt) val1).mod(r);
		if(val1.equals(BigInt.ZERO))
			return this.negate(val2);

		BigInt result =((BigInt) val1).subtract((BigInt) val2);
		//if(result.bitLength()<this.r.bitLength())
		//	return result;
		//else
		return result.mod(r);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#randomElement(java.util.Random)
	 */
	public FieldElement randomElement(Random rnd) {
		// TODO Auto-generated method stub
		BigInt rd=new BigInt(this.r.bitLength(),rnd);
		while(rd.compareTo(r)>=0)
			rd=rd.shiftRight(1);
		
		return rd;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#getP()
	 */
	public BigInt getP() {
		// TODO Auto-generated method stub
		return this.r;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#isValidElement(java.math.BigInteger)
	 */
	public boolean isValidElement(FieldElement val) {
		// TODO Auto-generated method stub
		if(val instanceof BigInt){
			if(r.compareTo((BigInt) val)>0)
				return true;
			else
				return false;
			
		}
		else 
			return false;
	}

	private BigInt reduce(BigInt val) {
		// TODO Auto-generated method stub

			if (val.signum() <0){
				return val.mod(this.r);
			}
			if (r.compareTo(val) > 0)
				return val;
			else
				return val.mod(r);

	}
//
//	  /**
//     *  
//     * @return val1*val2
//     */
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#multiply(java.math.BigInteger, int)
	 */
	public FieldElement multiply(FieldElement val, int val2) {
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The first input must be a BigInt");
		// TODO Auto-generated method stub
		if(val2 <0)
			throw new IllegalArgumentException ("oprand must not be negative");

			BigInt result= ((BigInt) val).multiply(BigInt.valueOf(val2));
			//if (result.bitLength()<this.r.bitLength())
			//	return result;
			//else
			return (result).mod(r);

	
	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#negate(java.math.BigInteger)
	 */
	public FieldElement negate(FieldElement val) {
		// TODO Auto-generated method stub
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The input must be a BigInt");

		//return val.negate();
		if(((BigInt) val).signum()==0)
			return val;
		if(((BigInt) val).signum()==-1)
			return ((BigInt) val).negate();
			val =this.reduce((BigInt) val);
			return this.subtract(this.r, val);
		
	}
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#getOne()
	 */
	public FieldElement getOne() {
		// TODO Auto-generated method stub
		return BigInt.ONE;
	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#getZero()
	 */
	public FieldElement getZero() {
		// TODO Auto-generated method stub
		return BigInt.ZERO;
	}
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#isOne(java.math.BigInteger)
	 */
	public boolean isOne(FieldElement val) {
		// TODO Auto-generated method stub

		if(val instanceof BigInt)
			return val.equals(BigInt.ONE);
		else
			return false;

	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#isZero(java.math.BigInteger)
	 */
	public boolean isZero(FieldElement val) {
		// TODO Auto-generated method stub

		if(val instanceof BigInt)
			return val.equals(BigInt.ZERO);
		else
			return false;
	}
	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#square(java.math.BigInteger)
	 */
	public FieldElement square(FieldElement val) {
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The input must be a BigInt");
		// TODO Auto-generated method stub
		if(((BigInt) val).signum()==0)
			return val;
		if(val.equals(BigInt.ONE))
			return val;
		BigInt result =((BigInt) val).multiply((BigInt) val);
		//if (result.bitLength()<this.r.bitLength())
		//	return result;
		//else
		return (result).mod(r);
		
	}
    /**
     * @return the square root of val in Fp, or null if the square root does not exist.
	 */
	public FieldElement squareRoot(FieldElement val) {
		// TODO Auto-generated method stub
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The input must be a BigInt");
		BigInt p =this.r;

		
		BigInt g=(BigInt) val;
		
		//g==0 || g==1
		if(g.equals(BigInt.ZERO)|| g.equals(BigInt.ONE))
			return g;
		

		BigInt[] result =p.divideAndRemainder(BigInt.valueOf(4));
		//p=3 mod 4, i.e. p=4k+3, output val^{k+1} mod p
		if(result[1].equals(BigInt.valueOf(3))){
			//System.out.println("case1");
			BigInt k= result[0];
			//System.out.println("k:");
			//System.out.println(k.toString());
			BigInt z =g.modPow(k.add(BigInt.ONE), p);
			if(this.square(z).equals(g))
				return z;
			else 
				return null;
			
		}
		
		
		result =p.divideAndRemainder(BigInt.valueOf(8));
		BigInt k=result[0];
		BigInt remainder=result[1];
		//p=5 mod 8 i.e. p=8k+5
		if(remainder.equals(BigInt.valueOf(5))){
			//System.out.println("case2");
			//gamma=(2g)^k mod p
			BigInt g2=g.multiply(BigInt.valueOf(2));
			BigInt gamma = g2.modPow(k, p);
			//i=2g*gamma^2 mod p
			BigInt i=(g2.multiply(gamma.modPow(BigInt.valueOf(2), p))).mod(p);
			//output g*gamma(i-1) mod p
			BigInt z= (g.multiply(gamma.multiply(i.subtract(BigInt.ONE))).mod(p));
			if(this.square(z).equals(g))
				return z;
			else 
				return null;
			
		}
		else if(remainder.equals(BigInt.ONE)){
			//System.out.println("case3");
			BigInt q=g;
			do{
			BigInt P=(BigInt) this.randomElement(new Random());
			BigInt K= (p.add(BigInt.ONE)).divide(BigInt.valueOf(2));
			BigInt re[] =this.LucasSequence(P, q, K);
			
			BigInt V=re[0];			
			BigInt Q0=re[1];
			
			BigInt z= (V.divide(BigInt.valueOf(2))).mod(p);
			
			if((this.square(z)).equals(g))
				return z;
			if(Q0.compareTo(BigInt.ONE)>0){
				if(Q0.compareTo(p.subtract(BigInt.ONE))<0)
					return null;
			}
			} while (true);
			
		}
		
		return null;
	
	}
	
	private BigInt[] LucasSequence(BigInt p, BigInt q,BigInt k){
		if(k.signum()<0)
			throw new IllegalArgumentException(
			"k must be a positive integer");
			
		//BigInt n=this.r;
		BigInt v0=BigInt.valueOf(2);
		BigInt v1=p;
		BigInt q0=BigInt.ONE;
		BigInt q1=BigInt.ONE;
		
		
		
		//byte [] kByte =k.toByteArray();
		int r =k.bitLength()-1;
		
		for(int i=r;i>=0;i--){
			//q0=q0q1 mod n
			q0=(BigInt) this.multiply(q0, q1);
			
			if (k.testBit(i)){
				//q1=q0q mod n
				q1=(BigInt) this.multiply(q0, q);
				//v0=v0v1-pq0 mod n
				v0=(BigInt) this.multiply(v0, v1);
				v0=(BigInt) this.subtract(v0, this.multiply(p, q0));
				//v1=v1^2-2q1 mod n
				
				v1 = (BigInt) this.square(v1);
				v1=(BigInt) this.subtract(v1, this.multiply(q1, 2));

			}
			else{
				//q1=q0
				q1=q0;
				//v1=v0v1-pq0 mod n
				v1 =(BigInt) this.multiply(v0, v1);
				v1=(BigInt) this.subtract(v1, this.multiply(p, q0));
				//v0=v0^2-2q0 mod n
				v0=(BigInt) this.square(v0);
				v0=(BigInt) this.subtract(v0, this.multiply(q0, 2));
				
			}
			
		}
		
		BigInt[] result={v0,q0};
		return result;
		
		
	}
	

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#getOrder()
	 */
	public BigInt getOrder() {
		// TODO Auto-generated method stub
		return this.r;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fp))
			return false;
		Fp other = (Fp) obj;
		if (r == null) {
			if (other.r != null)
				return false;
		} else if (!r.equals(other.r))
			return false;
		return true;
	}

	  /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.Field#pow(java.math.BigInt, java.math.BigInt)
	 */
	public FieldElement pow(FieldElement val, BigInt exp) {
		// TODO Auto-generated method stub
		if (!(val instanceof BigInt))
			throw new IllegalArgumentException("The input FieldElement must be a BigInt");

		
		return ((BigInt)val).modPow((BigInt) exp, this.r);
	}

//	public BigInt bMod(BigInt val){
//		if(val.signum<0){
//			System.out.println("mod negative");
//			val =this.negate(val);
//		}
//		
//		BigInt q1 = this.shiftRight32(val, k-1);
//		BigInt q2 = q1.multiply(u);
//		BigInt q3 = this.shiftRight32(q2, k+1);
//		
//		int[] b2kI = new int[k+2];
//		b2kI[0]=1;
//		
//		BigInt bkp1 = new BigInt(b2kI,1);
//		
//		BigInt r1 = this.modbybase(val, k+1);
//		BigInt r2 = this.modbybase(q3.multiply(this.r),k+1);
//		BigInt r =r1.subtract(r2);
//		
//		if(r.signum<0){
//			r=r.add(bkp1);
//		}
//		while(r.compareTo(this.r)>=0)
//			r=r.subtract(this.r);
//		
//		return r;
//
//		
//		
//	}
//	
//	BigInt shiftRight32(BigInt x, int n){
//		if(n>x.mag.length)
//			return BigInt.ZERO;
//		int length = x.mag.length-n;
//		
//		int [] newMag = new int[length];
//		
//		System.arraycopy(x.mag, 0, newMag, 0, length);
//		return new BigInt(newMag,1);
//	}
//	
//	BigInt modbybase(BigInt x, int n){
//		if (x.mag.length<n)
//			return x;
//
//			int offset = x.mag.length-n;
//			int [] newMag = new int[n];
//			System.arraycopy(x.mag, offset, newMag, 0, n);
//			return new BigInt(newMag,1);
//
//
//	}
//	
//	public static void main(String[] args){
//		GFP field = Predefined.nssTate().getCurve().getField();
//		
//		BigInt a = new BigInt(1000,new Random());
//		BigInt r1 = null;
//		
//		long all= 0;
//		long startTime;
//		long endTime;
//		for(int i=0;i<1000;i++){
//		startTime = System.nanoTime();
//		r1 = a.mod(field.getP());
//		endTime = System.nanoTime()-startTime;
//		all=all+endTime;
//		}
//		System.out.println(all/1000000);
//		
//		
//		BigInt r2 = null;
//		
//		all= 0;
//		for(int i=0;i<1000;i++){
//			startTime = System.nanoTime();
//		r2 =field.bMod(a);
//		endTime = System.nanoTime()-startTime;
//		all=all+endTime;
//		}
//		System.out.println(all/1000000);
//		System.out.println(r1);
//		System.out.println(r2);
//	}

}
