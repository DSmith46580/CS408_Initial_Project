package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;

import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
import uk.ac.ic.doc.jpair.api.Pairing;
/**
 * A Tate pairing implementation. 
 * This implementation uses the pairing friendly curve {@code Y^2 = X^3 + aX + b} defined over GF(p)
 * where {@code p = 3 mod 4}. G1 is taken as an order-q subgroup of the group formed by all the points on the curve.  
 * The curve has an embedding degree of 2. It has a corresponding twisted curve {@code Y^2 = X^3 + aX - b}. 
 *  Points from the twisted curve are used in the computation as elements in G2 to avoid operations in the extension field. 
 *  The algorithm is taken from "Efficient Computation of Tate Pairings in Projective Coordinates over General Characteristic Fields",
 *Proc. 7th Int. Conference on Inf Security and Cryptology (ICISC 2004), Eds. C.Park and S. Chee,
 *LNCS 3506, Springer 2005,  168-181.
 *      
 * @author Changyu Dong
 * @version 1.0
 */

public  class TatePairing implements Serializable, Pairing{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5083901786301376826L;
	 EllipticCurve ec;
	 EllipticCurve twisted;
	//private EllipticCurve ec2;
	 Field field;
	 Field gt;
	//private FiniteField field2;
	 BigInt order;
	 BigInt finalExponent;
	 BigInt cof;
	//private Point S;
	//can use optimised double method, namely a4 of the curve is -3

	
    /**
     * Define the parameters for computing Tate paring 
     * @param curve the elliptic curve on which G1 is defined
     * @param groupOrder the order of G1
     * @param coFactor a BigInteger such that {@code coFactor*groupOrder = #E}, where #E is the number of points on the curve.
     */
	public TatePairing(EllipticCurve curve,BigInt groupOrder, BigInt coFactor){
		this.ec=curve;	
		this.field=curve.getField();
		//this.ec2=curve2;
		//this.field2=new ExtensionField((GFP) field);
		this.order=groupOrder;
		//(p+1)/r
		this.finalExponent=(this.field.getOrder().add(BigInt.ONE)).divide(groupOrder);
		//this.twist=twist;
		this.cof=coFactor;
		this.twisted=twist(curve);
		this.gt=new ComplexField((Fp) this.field);
		//this.S = randomPointEc2(this.twisted);
		//this.finalExponent=(this.field2.getOrder().multiply(this.field2.getOrder()).subtract(BigInt.ONE)).divide(pointOrder);
	}
	
	EllipticCurve twist(EllipticCurve e){
		Field f = e.getField();
		FieldElement b = f.negate(e.getB());
		
		return new EllipticCurve(f,e.getA(),b);
	}
	/*
	 * Compute e(P,Q)
	 */

	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#compute(uk.ac.ic.doc.jpbc.Point, uk.ac.ic.doc.jpbc.Point)
	 */
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#compute(uk.ac.ic.doc.jpbc.Point, uk.ac.ic.doc.jpbc.Point)
	 */
	public FieldElement compute(Point P, Point Q){
		FieldElement f=new Complex((Fp)this.field,BigInt.ONE);
		JacobPoint V =this.ec.aToJ(P);
		BigInt n =this.order.subtract(BigInt.ONE);
		Point nP= ec.negate(P);
		

		//byte[] ba =n.toByteArray();
		byte[] r =naf(this.order, (byte)2);
		//Point T;
		//BigInt [] lamda= new BigInt[1];;
		FieldElement u;
		for(int i=r.length-2;i>=0;i--){
			u=encDbl(V, Q);
			//f=f.square().multiply(u);
			f= this.gt.multiply(this.gt.square(f),u);
			if(r[i]==1){
				u=encAdd(V, P, Q);
				f=this.gt.multiply(f,u);
			}
			if(r[i]==-1){
				u=encAdd(V, nP, Q);
				f=this.gt.multiply(f,u);
			}
		}

		f=((Complex) f).conjugate().divide((Complex )f);
		
		return this.gt.pow(f,this.finalExponent);
		//return f;

		
	}

	
	//used by tate pairing, point doubling in Jacobian coordinates, and return the value of f 
	Complex encDbl(JacobPoint P,Point Q){
		//if(P.isInfinity())
		//	return;
		
		BigInt x =P.getX();
		BigInt y= P.getY();
		BigInt z= P.getZ();
		//t1=y^2
		FieldElement t1 =this.field.square(y);
		//t2=4xt1
		FieldElement t2=this.field.multiply(x, t1);
		//t2=this.field.multiply(t2, 4);

		t2=this.field.add(t2, t2);
		t2=this.field.add(t2, t2);

		//t3=8t1^2
		FieldElement t3 =this.field.square(t1);
		//t3 = this.field.multiply(t3, 8);

		t3 = this.field.add(t3, t3);
		t3 = this.field.add(t3, t3);
		t3 = this.field.add(t3, t3);

		//t4=z^2
		FieldElement t4 =this.field.square(z);
		FieldElement t5; 
		//if a==-3
		if(this.ec.opt){
			t5 = this.field.multiply(this.field.subtract(x,t4), this.field.add(x,t4));
			t5 =this.field.add(t5, this.field.add(t5, t5));		
		}
		else{
		//t5=3x^2+aZ^4		
		t5 = this.field.square(x);
		t5 =this.field.add(t5, this.field.add(t5, t5));	
		t5=this.field.add(t5, this.field.multiply(this.ec.getA4(), this.field.square(t4)));
		
//		FieldElement temp =this.field.square(this.field.square(z));
//		temp=this.field.multiply(this.ec.getA4(), temp);
//		
//		t5=this.field.add(t5,temp);
		}
		//x3=t5^2-2t2
		FieldElement x3 =this.field.square(t5);
		x3=this.field.subtract(x3, this.field.add(t2,t2));
		
		//y3=t5(t2-x3)-t3
		FieldElement y3 =this.field.multiply(t5, this.field.subtract(t2, x3));
		y3=this.field.subtract(y3, t3);
		
		//z3=2y1z1
		FieldElement z3 =this.field.multiply(y, z);
		z3 = this.field.add(z3, z3);
		
		P.setX((BigInt) x3);
		P.setY((BigInt) y3);
		P.setZ((BigInt) z3);
		
		//Z3t4yQi-(2t1-t5(t4Xq+x1))
		FieldElement real = this.field.multiply(t4, Q.getX());
		real=this.field.add(real, x);
		real=this.field.multiply(t5, real);
		real=this.field.subtract(real, t1);
		real=this.field.subtract(real, t1);
		
		FieldElement imag= this.field.multiply(z3, t4);
		imag=this.field.multiply(imag, Q.getY());
		

		return new Complex((Fp)this.field,(BigInt)real,(BigInt)imag);
			
	}
	
	//used by Tate paring, add two point, save result in the first argument, return the value of f
	Complex encAdd(JacobPoint A, Point P, Point Q){
//		if(P.isInfinity()){
//			P.setX(Q.getX());
//			P.setY(Q.getY());
//			P.setZ(this.field.getOne());
//			return;
//		}
//		if(Q.isInfinity())
//			return;
		
		
		BigInt x1 =A.getX();
		BigInt y1=A.getY();
		BigInt z1=A.getZ();
		
		FieldElement x=P.getX();
		FieldElement y=P.getY();
		
		//t1=z1^2
		FieldElement t1 =this.field.square(z1);
		//t2=z1t1
		FieldElement t2 =this.field.multiply(z1, t1);
		//t3=xt1
		FieldElement t3=this.field.multiply(x, t1);
		//t4=Yt2
		FieldElement t4=this.field.multiply(y, t2);
		//t5=t3-x1
		FieldElement t5=this.field.subtract(t3, x1);
		//t6=t4-y1
		FieldElement t6=this.field.subtract(t4, y1);
		//t7=t5^2
		FieldElement t7 =this.field.square(t5);
		//t8=t5t7
		FieldElement t8=this.field.multiply(t5, t7);
		//t9=x1t7
		FieldElement t9=this.field.multiply(x1, t7);
		
		//x3=t6^2-(t8+2t9)
		FieldElement x3 =this.field.square(t6);
		x3=this.field.subtract(x3, this.field.add(t8, this.field.add(t9, t9)));
		
		//y3=t6(t9-x3)-y1t8
		FieldElement y3 =this.field.multiply(t6, this.field.subtract(t9, x3));
		y3=this.field.subtract(y3, this.field.multiply(y1, t8));
		
		//z3=z1t5
		FieldElement z3 =this.field.multiply(z1, t5);
		
		A.setX((BigInt) x3);
		A.setY((BigInt) y3);
		A.setZ((BigInt) z3);
		
		//z3yqi -(z3Y-t6(xq+x))
		FieldElement imag = this.field.multiply(z3, Q.getY());
		
		FieldElement real= this.field.add(Q.getX(), x);
		real=this.field.multiply(real, t6);
		real=this.field.subtract(real, this.field.multiply(z3, y));

		return new Complex((Fp)this.field,(BigInt)real,(BigInt)imag);
	}
	

	//windowed naf form of BigInt k, w is the window size
	byte[] naf(BigInt k, byte w){

        // The window NAF is at most 1 element longer than the binary
        // representation of the integer k. byte can be used instead of short or
        // int unless the window width is larger than 8. For larger width use
        // short or int. However, a width of more than 8 is not efficient for
        // m = log2(q) smaller than 2305 Bits. Note: Values for m larger than
        // 1000 Bits are currently not used in practice.
        byte[] wnaf = new byte[k.bitLength() + 1];

        // 2^width as short and BigInteger
        short pow2wB = (short)(1 << w);
        BigInt pow2wBI = BigInt.valueOf(pow2wB);

        int i = 0;

        // The actual length of the WNAF
        int length = 0;

        // while k >= 1
        while (k.signum() > 0)
        {
            // if k is odd
            if (k.testBit(0))
            {
                // k mod 2^width
                BigInt remainder = k.mod(pow2wBI);

                // if remainder > 2^(width - 1) - 1
                if (remainder.testBit(w - 1))
                {
                    wnaf[i] = (byte)(remainder.intValue() - pow2wB);
                }
                else
                {
                    wnaf[i] = (byte)remainder.intValue();
                }
                // wnaf[i] is now in [-2^(width-1), 2^(width-1)-1]

                k = k.subtract(BigInt.valueOf(wnaf[i]));
                length = i;
            }
            else
            {
                wnaf[i] = 0;
            }

            // k = k/2
            k = k.shiftRight(1);
            i++;
        }

        length++;

        // Reduce the WNAF array to its actual length
        byte[] wnafShort = new byte[length];
        System.arraycopy(wnaf, 0, wnafShort, 0, length);
        return wnafShort;
		
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#getCofactor()
	 */
	public BigInt getCofactor(){
		return this.cof;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#getCurve()
	 */
	public EllipticCurve getCurve(){
		return this.ec;
	}
	
	/**
	 * Return the twisted curve.
	 */
	public EllipticCurve getCurve2(){
		return this.twisted;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#RandomPointInG1(java.util.Random)
	 */
	public Point RandomPointInG1(Random rnd){
		Point P;
		do{
			P = ec.randomPoint(rnd);
			
			P= ec.multiply(P, this.cof);
			
		}while(!ec.multiply(P, this.order).equals(Point.INFINITY));
		
		return P;
		
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#RandomPointInG2(java.util.Random)
	 */
	public Point RandomPointInG2(Random rnd){
		return this.twisted.randomPoint(rnd);
	}


	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.Pairing#getGroupOrder()
	 */
	@Override
	public BigInt getGroupOrder() {
		// TODO Auto-generated method stub
		return this.order;
	}
	
	
//	public static void main(String[] args){
//		TatePairing e = Predefined.nssTate();
//		Random rnd = new Random();
//		Point P = e.RandomPointInG1(rnd);
//		
//		Point Q = e.RandomPointInG2(rnd);
//		
//		Complex f = (Complex) e.computeF(P, Q);
//		Complex r=null;
//		long all= 0;
//		long startTime;
//		long endTime;
//		for(int i=0;i<1000;i++){		
//			startTime = System.nanoTime();
//		r =  (Complex) f.pow(e.finalExponent);
//		endTime = System.nanoTime()-startTime;
//		all=all+endTime;
//		}
//			
//		System.out.println(all/1000000);
//		BigInt r1=null;
//		all= 0;
//		for(int i=0;i<1000;i++){		
//			startTime = System.nanoTime();
//		r1 = f.powl(e.finalExponent);
//		endTime = System.nanoTime()-startTime;
//		all=all+endTime;
//		}
//			
//		System.out.println(all/1000000);
//		
//		
//		System.out.println(r1);
//		System.out.println(r.getReal());
//	}
	@Override
	public Field getGt() {
		// TODO Auto-generated method stub
		return this.gt;
	}
	
}

