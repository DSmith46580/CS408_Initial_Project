package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.FieldElement;

/**
 * A wrapper class of the java.math.BigInteger class and implements FieldElement interface.
 * @author Changyu Dong
 * @version 1.0
 * @see java.math.BigInteger
 */

public class BigInt implements FieldElement,Serializable {



	private BigInteger value;
	
	public BigInt(BigInteger val){
		this.value=val;
	}
	
	public BigInt(byte[] val) {
		this.value=new BigInteger(val);
		// TODO Auto-generated constructor stub
	}

	public BigInt(String val) {
		this.value=new BigInteger(val);
		// TODO Auto-generated constructor stub
	}


	public BigInt(int signum, byte[] magnitude) {
		this.value=new BigInteger(signum, magnitude);
		// TODO Auto-generated constructor stub
	}

	public BigInt(String val, int radix) {
		this.value=new BigInteger(val, radix);
		// TODO Auto-generated constructor stub
	}


	public BigInt(int numBits, Random rnd) {
		this.value=new BigInteger(numBits, rnd);
		// TODO Auto-generated constructor stub
	}

	public BigInt(int bitLength, int certainty, Random rnd) {
		this.value=new BigInteger(bitLength, certainty, rnd);
		// TODO Auto-generated constructor stub
	}
		
	 public BigInt 	abs(){
		 return new BigInt(this.value.abs());
	 }
	 
	 public BigInt 	add(BigInt val){
		 return new BigInt(this.value.add(val.value));
	 }

	 public BigInt and(BigInt val){
		 return new BigInt(this.value.and(val.value));
	 }

	 public BigInt	andNot(BigInt val){
		 return new BigInt(this.value.andNot(val.value));
	 }

	 public int bitCount(){
		 return this.value.bitCount();
	 }

	 public int bitLength(){
		 return this.value.bitLength();
	 }

	 public BigInt clearBit(int n){
		 return new BigInt(this.value.clearBit(n));
	 }

	 public int compareTo(BigInt val){
		 return this.value.compareTo(val.value);
	 }

	 public BigInt divide(BigInt val){
		 return new BigInt(this.value.divide(val.value));
	 }

	 public BigInt[] divideAndRemainder(BigInt val){
		 BigInteger[] result =this.value.divideAndRemainder(val.value);
		 BigInt [] toReturn =new BigInt[result.length];
		 
		 for(int i=0;i<result.length;i++){
			 toReturn[i]=new BigInt(result[i]);
		 }
		 return toReturn;
	 }

	 public double 	doubleValue(){
		 return this.value.doubleValue();
	 }

	 public boolean equals(Object x) {
		if (x == this)
			return true;

		if (!(x instanceof BigInt))
			return false;
		BigInt xInt = (BigInt) x;
		return this.value.equals(xInt.value);
	}

	 public BigInt 	flipBit(int n){
		 return new BigInt(this.value.flipBit(n));
	 }

	 public float floatValue(){
		 return this.value.floatValue();
	 }

	 public BigInt 	gcd(BigInt val){
		 return new BigInt(this.value.gcd(val.value));
	 }

	 public int getLowestSetBit(){
		 return this.value.getLowestSetBit();
	 }

	 public int hashCode(){
		 return this.value.hashCode();
	 }

	 public int intValue(){
		 return this.value.intValue();
	 }

	 public boolean isProbablePrime(int certainty){
		 return this.value.isProbablePrime(certainty);
	 }

	 public long 	longValue(){
		 return this.value.longValue();
	 }

	 public BigInt 	max(BigInt val){
		 return new BigInt(this.value.max(val.value));
	 }
	 
	 public BigInt 	min(BigInt val){
		 return new BigInt(this.value.min(val.value));
	 }

	 public BigInt 	mod(BigInt m){
		 return new BigInt(this.value.mod(m.value));
	 }

	 public BigInt 	modInverse(BigInt m){
		 return new BigInt(this.value.modInverse(m.value));
	 }

	 public BigInt modPow(BigInt exponent, BigInt m){
		 return new BigInt(this.value.modPow(exponent.value, m.value));
	 }

	 public BigInt 	multiply(BigInt val){
		 return new BigInt(this.value.multiply(val.value));
	 }

	 public BigInt	negate(){
		 return new BigInt(this.value.negate());
	 }
 
	 public BigInt	nextProbablePrime(){
		 return new BigInt(this.value.nextProbablePrime());
	 }

	 public BigInt 	not(){
		 return new BigInt(this.value.not());
	 }

	 public BigInt 	or(BigInt val){
		 return new BigInt(this.value.or(val.value));
	 }

	 public BigInt pow(int exponent){
		 return new BigInt(this.value.pow(exponent));
	 }

	 public static BigInt 	probablePrime(int bitLength, Random rnd){
		 return new BigInt(BigInteger.probablePrime(bitLength, rnd));
	 }
	 

	    public BigInt 	remainder(BigInt val){
	    	return new BigInt(this.value.remainder(val.value));
	    }

	    public BigInt setBit(int n){
	    	return new BigInt(this.value.setBit(n));
	    }

	   public BigInt shiftLeft(int n){
		   return new BigInt(this.value.shiftLeft(n));
	   }

	    public BigInt 	shiftRight(int n){
	    	return new BigInt(this.value.shiftRight(n));
	    }

	    public int 	signum(){
	    	return this.value.signum();
	    }

	    public BigInt 	subtract(BigInt val){
	    	return new BigInt(this.value.subtract(val.value));
	    }

	    public boolean testBit(int n){
	    	return this.value.testBit(n);
	    }

	    public byte[] toByteArray(){
	    	return this.value.toByteArray();
	    }

	    public String 	toString(){
	    	return this.value.toString();
	    	
	    }

	   public String 	toString(int radix){
		   return this.value.toString(radix);
	   }
	      
	    static public BigInt	valueOf(long val){
	    	return new BigInt(BigInteger.valueOf(val));
	    }
	    public BigInt 	xor(BigInt val){
	    	return new BigInt(this.value.xor(val.value));
	    	
	    }
	    
	    public static final BigInt 	ONE=new BigInt(BigInteger.ONE);

	    public static final BigInt 	TEN=new BigInt(BigInteger.TEN);

        public static final BigInt 	ZERO=new BigInt(BigInteger.ZERO);

	         
 

}
