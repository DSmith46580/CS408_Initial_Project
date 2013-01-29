package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;


/**
 * This class defines elliptic curves over GFP.
 * The Weierstrass equation of the curves is: Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6 or Y^2=X^3+aX+b.
 * @author Changyu Dong
 * @version 1.0
 */

public class EllipticCurve implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004154489620230755L;
	private Field field;
	private FieldElement a1;
	private FieldElement a3;
	private FieldElement a2;
	private FieldElement a4;
	private FieldElement a6;
	final boolean  opt;
    /**
     * Creates an elliptic curve with the specified finite field
     * <code>field</code> and the coefficients <code>a1,a3,a2,a4,a6</code>.
     * @param field the finite field that this elliptic curve is over.
     * @param a1 the coefficient of term XY.
     * @param a3 the coefficient of term Y.
     * @param a2 the coefficient of term X^2.
     * @param a4 the coefficient of term X.
     * @param a6 the constant term.
     * @exception NullPointerException if <code>field</code>,
     * or any one of<code>a1,a3,a2,a4,a6</code> is null.
     * @exception IllegalArgumentException if any one of 
     * <code>a1,a3,a2,a4,a6</code> is not in <code>field</code>.
     */
	public EllipticCurve(Field field, FieldElement a1, FieldElement a2, FieldElement a3,
			FieldElement a4, FieldElement a6) {
		if (field == null)
			throw new NullPointerException("field is null");
		if (a1 == null)
			throw new NullPointerException("a1 is null");
		if (a3 == null)
			throw new NullPointerException("a3 is null");

		if (a2 == null)
			throw new NullPointerException("a2 is null");

		if (a4 == null)
			throw new NullPointerException("a4 is null");

		if (a6 == null)
			throw new NullPointerException("a6 is null");

		if (!field.isValidElement(a1))
			throw new IllegalArgumentException(
					"a1 is not an element in the field");
		if (!field.isValidElement(a3))
			throw new IllegalArgumentException(
					"a3 is not an element in the field");

		if (!field.isValidElement(a2))
			throw new IllegalArgumentException(
					"a2 is not an element in the field");
		if (!field.isValidElement(a4))
			throw new IllegalArgumentException(
					"a4 is not an element in the field");
		if (!field.isValidElement(a6))
			throw new IllegalArgumentException(
					"a6 is not an element in the field");
		
		initialise(field,a1,a3,a2,a4,a6);
		if (a4.equals(this.field.negate(BigInt.valueOf(3))))
			this.opt=true;
		else 
			this.opt=false;

	}
	
    /**
     * Creates an elliptic curve with the specified finite field
     * <code>field</code> and the coefficients <code>a,b</code>.
     * <code>a,b</code> are coefficients for the simplified Weierstrass equations.
     * the simplified Weierstrass equation is
     * Y^2=X^3+aX+b;
     * @param field the finite field that this elliptic curve is over.
     * @param a the first coefficient.
     * @param b the second coefficient.
     * @exception NullPointerException if <code>field</code>,
     * or any one of<code>a,b</code> is null.
     * @exception IllegalArgumentException if any one of 
     * <code>a,b</code> is not in <code>field</code>.
     */
	
	public EllipticCurve(Field field, FieldElement a, FieldElement b){
		if (field == null)
			throw new NullPointerException("field is null");
		if (a == null)
			throw new NullPointerException("a is null");
		if (b == null)
			throw new NullPointerException("b is null");
		
		if (!(field).isValidElement(a))
			throw new IllegalArgumentException(
					"a is not an element in the field");
		if (!field.isValidElement(b))
			throw new IllegalArgumentException(
					"b is not an element in the field");
		
		if (field instanceof Fp){
			initialise(field, BigInt.ZERO,BigInt.ZERO,BigInt.ZERO,a,b);
			
		}

		if (a4.equals(this.field.negate(BigInt.valueOf(3))))
			this.opt=true;
		else 
			this.opt=false;
	}


	private void initialise(Field field, FieldElement a1,
			FieldElement a3, FieldElement a2, FieldElement a4,
			FieldElement a6) {
		// TODO Auto-generated method stub
		this.field=field;
		this.a1=a1;
		this.a3=a3;
		this.a2=a2;
		this.a4=a4;
		this.a6=a6;


		
	}
	
    /**
     * Point addition. Returns a new Point whose value is {@code p1 + p2}.  
     *
     * @param p1 first point.
     * @param p2 second point.
     * @return {@code p1 + p2}
     * @exception IllegalArgumentException if the x- or y-coordinate of p1 or p2 is not valid field element.
     */
	
	public Point add(Point p1, Point p2){ 
		return basicAdd(p1,p2);
	}

	
	private Point basicAdd(Point p1, Point p2) {
		// TODO Auto-generated method stub
		
		//if either one is INFINITY, return the other point
		if(p1.isInfinity())
			return p2;
		else if(p2.isInfinity())
			return p1;
		
		
		//check inputs
		if(!(this.field.isValidElement(p1.getX())) || !(this.field.isValidElement(p1.getY()))
			||!(this.field.isValidElement(p2.getX())) || !(this.field.isValidElement(p2.getY()))	
		)
			throw new IllegalArgumentException("The input points must be taken over the field.");
//		//p1=p2, doubling
//		if (p1.equals(p2))
//			return dbl(p1);
		

		//x2=x1
		if(p1.getX().equals(p2.getX())){
			//p1 = p2
			if (p1.getY().equals(p2.getY()))
				return dbl(p1);
			//p1 != p2
			else
				return Point.INFINITY;
		}
		
		//lamda=(y2-y1)/(x2-x1)
		
		FieldElement top=this.field.subtract(p2.getY(),p1.getY());
		FieldElement bottom=this.field.subtract(p2.getX(),p1.getX());
		FieldElement lamda =this.field.multiply(top, this.field.inverse(bottom));
		

		//x3=lamda^2+a1lamda-a2-x1-x2
		
		FieldElement x3=this.field.square(lamda);
		//x3=this.field.add(x3, this.field.multiply(a1, lamda));
		//x3=this.field.substract(x3, a2);
		x3=this.field.subtract(x3, p1.getX());
		x3=this.field.subtract(x3, p2.getX());
		
		//y3=lamdax1-y1-a1x3-a3-lamdax3
		//y3=lamdax1-y1-a1x3-a3-lamdax3
		FieldElement y3 =this.field.multiply(lamda, p1.getX());
		y3=this.field.subtract(y3, p1.getY());
		//y3=this.field.substract(y3, this.field.multiply(a1, x3));
		//y3=this.field.substract(y3, a3);
		y3=this.field.subtract(y3, this.field.multiply(lamda, x3));
	
		return new Point(x3,y3);
		
	}
    /**
     * Point doubling. Returns a new Point whose value is {@code p1 + p1}.  
     *
     * @param p1 the point to be doubled.
     * @return {@code p1 + p1}
     * @exception IllegalArgumentException if the x- or y-coordinate of p1 is not valid field element.
     */

	public Point dbl(Point p1) {
		
		return basicDbl(p1);
	}
	
	private Point basicDbl(Point p1) {
		// TODO Auto-generated method stub
		if (p1.isInfinity())
			return p1;
		
		if(!(this.field.isValidElement(p1.getX())) || !(this.field.isValidElement(p1.getY()))
			)
				throw new IllegalArgumentException("The input point must be taken over the field.");
		//lamda = (3x^2+2a2x+a4-a1y)/(2y1+a1x+a3)
		FieldElement bottom =this.field.multiply(p1.getY(), 2);
		//bottom=this.field.add(bottom, this.field.multiply(a1, p1.getX()));
		//bottom=this.field.add(bottom, a3);
		
		if (this.field.isZero(bottom))
			return Point.INFINITY;
		FieldElement top=this.field.multiply(this.field.square(p1.getX()), 3);
		//top=this.field.add(top, this.field.multiply(this.field.multiply(a2, p1.getX()), 2));
		top=this.field.add(top, a4);
		//top=this.field.substract(top, this.field.multiply(a1, p1.getY()));
		
		FieldElement lamda = this.field.multiply(top, this.field.inverse(bottom));
		
		//x3= lamda^2+lamdaa1-a2-2x
		FieldElement x3= this.field.square(lamda);
		x3=this.field.add(x3, this.field.multiply(lamda, a1));
		//x3=this.field.substract(x3, a2);
		x3=this.field.subtract(x3, this.field.multiply(p1.getX(), 2));
		
		//y3=lamdax-y-a1x3-a3-lamdax3
		FieldElement y3= this.field.multiply(lamda, p1.getX());
		y3=this.field.subtract(y3, p1.getY());
		//y3=this.field.substract(y3, this.field.multiply(a1, x3));
		//y3=this.field.substract(y3, a3);
		y3=this.field.subtract(y3, this.field.multiply(lamda, x3));
		
		return new Point(x3,y3);
		
	}
    /**
     * Point negation. Returns a new Point whose value is {@code -p1}.  
     *
     * @param p1 the point to be negated.
     * @return {@code -p1}
     * @exception IllegalArgumentException if the x- or y-coordinate of p1 is not valid field element.
     */

	public Point negate(Point p1){
		return basicNegate(p1);
	}
	
	private Point basicNegate(Point p1) {
		// TODO Auto-generated method stub
		if (p1.isInfinity())
			return p1;
		
		if(!(this.field.isValidElement(p1.getX())) || !(this.field.isValidElement(p1.getY()))
			)
				throw new IllegalArgumentException("The input point must be taken over the field.");
		//x2=x1;
		//y2=-(a1x1+a3+y1)
		//FieldElement y2=this.field.add(this.field.multiply(a1, p1.getX()), a3);
		//y2=this.field.add(y2, p1.getY());
		FieldElement  y2=this.field.negate(p1.getY());
		
		return new Point(p1.getX(),y2);
		
	}
	
    /**
     * Point subtraction. Returns a new Point whose value is {@code p1 - p2}.  
     *
     * @param p1 the minuend.
     * @param p2 the subtrahend.
     * @return {@code p1 - p2}
     * @exception IllegalArgumentException if the x- or y-coordinate of p1 or p2 is not valid field element.
     */
	
	public Point subtract(Point p1, Point p2){
		if (p1.isInfinity())
			return negate(p2);
		if(p2.isInfinity())
			return p1;
		
		return add(p1,negate(p2));
	}
	
    /**
     * Scalar multiplication. Returns a new Point whose value is {@code kp}.  
     *
     * @param p a point.
     * @param k a biginteger.
     * @return {@code kp}
     * @exception IllegalArgumentException if the input point is not taken over the field.
     */
	public Point multiply(Point p, BigInt k){
		return this.jToA(jMultiplyMut(p,k));
	}
	
	//a reference multiplication method
	
private Point simpleMultiply(Point p, BigInt k){
		
		if(!(this.field.isValidElement(p.getX())) || !(this.field.isValidElement(p.getY()))
		)
			throw new IllegalArgumentException("The input point must be taken over the field.");

		if(p.isInfinity())
			return p;
		if(k.equals(BigInt.ZERO))
			return Point.INFINITY;
		if(k.equals(BigInt.ONE))
			return p;
		
		
		if (k.signum() == -1) {
			k = k.abs();
			p=this.negate(p);
		}
		
		byte [] ba =k.toByteArray();
		
		int degree =ByteArrayUtil.degreeOf(ba)-1;
		
		Point x=p;
		
		for(int i=degree;i>=0;i--){
			x=this.dbl(x);
			if(ByteArrayUtil.getBitByDegree(i, ba))
				x=this.add(p, x);
		}
		return x;
		
		
	}

//negate a Jacobian-point 
private JacobPoint negate(JacobPoint p){
	return new JacobPoint(p.getX(),(BigInt) this.field.negate(p.getY()),p.getZ());
}


//multiplication using Jacobian coordinates
JacobPoint jMultiplyMut(Point p, BigInt k){
	
	if(!(this.field.isValidElement(p.getX())) || !(this.field.isValidElement(p.getY()))
	)
		throw new IllegalArgumentException("The input point must be taken over the field.");

	if(p.isInfinity())
		return this.aToJ(p);
	if(k.equals(BigInt.ZERO))
		return JacobPoint.INFINITY;
	if(k.equals(BigInt.ONE))
		return this.aToJ(p);
	
	
	if (k.signum() == -1) {
		k = k.abs();
		p=this.negate(p);
	}
	
	//byte [] ba =k.toByteArray();
	
	int degree =k.bitLength()-2;
	
	JacobPoint result=this.aToJ(p);
	
	for(int i=degree;i>=0;i--){
		this.jDblMut(result);
		if(k.testBit(i))
			this.jAddMut(result,p);
	}
	return result;
	
}

/**
 * Test whether the input point is on this curve.   
 *
 * @param p the point to be tested.
 * @return {@code true} if the point is on the curve, {@code false} otherwise
 */
	
	public boolean isOnCurve(Point p) {
		if (p.isInfinity())
			return true;

		
		FieldElement x=p.getX();
		FieldElement y= p.getY();
		if(!(this.field.isValidElement(x)) || !(this.field.isValidElement(y))
		)
			return false;
		//left hand side y^2+a_1xy+a_3y

		FieldElement lhs= this.field.square(y);
		lhs= this.field.add(lhs, this.field.multiply(this.field.multiply(a1, x),y));
		lhs=this.field.add(lhs, this.field.multiply(a3, y));
		
		
		
		//right hand side x^3+a_2x^2+a_4x+a_6
		FieldElement x2=this.field.square(x);
		FieldElement rhs= this.field.multiply(x, x2);
		rhs=this.field.add(rhs, this.field.multiply(a2, x2));
		rhs=this.field.add(rhs, this.field.multiply(a4, x));
		rhs=this.field.add(rhs, a6);
		
		return lhs.equals(rhs);
		
		
	}
	
	/**
	 * Return the finite field on which this curve is defined.   
	 * @return this.field
	 */
	
	public Field getField(){
		return this.field;
	}
	
	
	/**
	 * Return a random point on this curve   
	 *@param r the source of randomness.
	 * @return a random point 
	 */
	
	public Point randomPoint(Random r){

			return randomPointP(r);

	}
	

	//y^2=x^3+ax+b
	private Point randomPointP(Random r) {
		FieldElement x;
		FieldElement y;
		
		do{
			x=this.field.randomElement(r);

			
			//temp =x^3+ax+b
			//FieldElement temp= this.field.pow(x, BigInt.valueOf(3));
			FieldElement temp=this.field.multiply(x, x);
			temp=this.field.multiply(x, temp);
			temp=this.field.add(temp, this.field.multiply(this.getA(), x));
			temp=this.field.add(temp, this.getB());
			
			FieldElement sqr=this.field.squareRoot(temp);
			
			if(sqr!=null){
				//BigInteger negB=this.field.negate(b);
				
				if(r.nextBoolean()){
					y=sqr;
				}
				else{
					y=this.field.negate(sqr);
				}
				
				return new Point(x,y);
				
			}			
		}while (true);
	}
	
	/**
	 * Return a point on this curve given the x-coordinate   
	 *@param x the x-coordinate.
	 * @return a point, or null if no point exists for the x. 
	 */
	public Point getPoint(FieldElement x) {		
		//
		//FieldElement temp= this.field.pow(x, BigInt.valueOf(3));
		FieldElement temp= this.field.multiply(x, x);
		temp=this.field.multiply(temp, x);
		temp=this.field.add(temp, this.field.multiply(this.getA(), x));
		temp=this.field.add(temp, this.getB());
		
		FieldElement sqr=this.field.squareRoot(temp);
		
		if(sqr!=null){
			
			return new Point(x,sqr);
		}
			return null;
	}
	

	


	

	/**
	 *  Get the coefficient a of the simplified Weierstrass equation 
     * Y^2=X^3+aX+b;
	 * @return the coefficient a.
	 */
	
	public FieldElement getA(){
		if (this.field instanceof Fp){
			return this.a4;
			
		}
		else{
			return this.a2;
		}
	}
	
	/**
	 *  Get the coefficient b of the simplified Weierstrass equation 
     * Y^2=X^3+aX+b;
	 * @return the coefficient b.
	 */
	public FieldElement getB(){

			return this.a6;

	}

	/**
	 *  Get the coefficient A1 of the simplified Weierstrass equation 
     * Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6.
	 * @return the coefficient A1.
	 */

	public FieldElement getA1(){
		return this.a1;
	}
	
	/**
	 *  Get the coefficient a2 of the simplified Weierstrass equation 
     * Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6.
	 * @return the coefficient a2.
	 */
	
	public FieldElement getA2(){
		return this.a2;
	}
	/**
	 *  Get the coefficient a3 of the simplified Weierstrass equation 
     * Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6.
	 * @return the coefficient a3.
	 */
	public FieldElement getA3(){
		return this.a3;
	}
	
	/**
	 *  Get the coefficient a4 of the simplified Weierstrass equation 
     * Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6.
	 * @return the coefficient a4.
	 */
	
	public FieldElement getA4(){
		return this.a4;
	}
	/**
	 *  Get the coefficient a6 of the simplified Weierstrass equation 
     * Y^2+a_1XY+a_3Y=X^3+a_2X^2+a_4X+a_6.
	 * @return the coefficient a6.
	 */
	public FieldElement getA6(){
		return this.a6;
	}


	
	//point doubling in Jacobian coordinates, result is saving in the input point
	void jDblMut(JacobPoint P){
		if(P.isInfinity())
			return;
		
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
		if(opt){
			t5 = this.field.multiply(this.field.subtract(x,t4), this.field.add(x,t4));
			t5 =this.field.add(t5, this.field.add(t5, t5));		
		}
		else{
		//t5=3x^2+aZ^4	=3x^2+at4^2	
		t5 = this.field.square(x);
		t5 =this.field.add(t5, this.field.add(t5, t5));	
		t5=this.field.add(t5, this.field.multiply(getA4(), this.field.square(t4)));
		}
//		//t5=3x^2+aZ^4
//		FieldElement t5 = this.field.square(x);
//		t5=this.field.add(t5, this.field.add(t5,t5));
		
//		FieldElement temp =this.field.square(this.field.square(z));
//		temp=this.field.multiply(this.a4, temp);
//		
//		t5=this.field.add(t5, temp);
		//x3=t5^2-2t2
		FieldElement x3 =this.field.square(t5);
		x3=this.field.subtract(x3, this.field.add(t2, t2));
		
		//y3=t5(t2-x3)-t3
		FieldElement y3 =this.field.multiply(t5, this.field.subtract(t2, x3));
		y3=this.field.subtract(y3, t3);
		
		//z3=2y1z1
		FieldElement z3 =this.field.multiply(y, z);
		z3 = this.field.add(z3, z3);
		
		P.setX((BigInt) x3);
		P.setY((BigInt) y3);
		P.setZ((BigInt) z3);
		return;
			
	}
	


	
	//add two point, save result in the first argument
	void jAddMut(JacobPoint P, Point Q){
		if(P.isInfinity()){
			P.setX((BigInt) Q.getX());
			P.setY((BigInt) Q.getY());
			P.setZ((BigInt) this.field.getOne());
			return;
		}
		if(Q.isInfinity())
			return;
		
		
		FieldElement x1 =P.getX();
		FieldElement y1=P.getY();
		FieldElement z1=P.getZ();
		
		FieldElement x=Q.getX();
		FieldElement y=Q.getY();
		
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
		
		P.setX((BigInt) x3);
		P.setY((BigInt) y3);
		P.setZ((BigInt) z3);
		return;
	}
	



	// convert Jacobian to Affine	
	Point jToA(JacobPoint P){
		if(P.isInfinity())
			return Point.INFINITY;
		FieldElement bi=P.getZ();
		FieldElement zInverse = this.field.inverse(P.getZ());
		FieldElement square = this.field.square(zInverse);
		//x =X/Z^2
		FieldElement x =this.field.multiply(P.getX(), square);
		//y=Y/Z^3
		FieldElement y= this.field.multiply(P.getY(), this.field.multiply(square, zInverse));
		
		return new Point(x,y);
	}
	//convert Affine to Jacobian
	JacobPoint aToJ(Point P){
		return new JacobPoint((BigInt)P.getX(),(BigInt)P.getY(),BigInt.ONE);
	}
	/**
	 * Return a random base point (generator of an order-q subgroup). cof*q must equal to the number of points on the curve. Otherwise it will loop
	 * forever.
	 * @param rnd the source of randomness
	 * @param q the order of the subgroup (the order of the base point)
	 * @param cof the cofactor
	 * @return a base point 
	 */

	public Point getBasePoint(Random rnd,BigInt q,BigInt cof){
		Point p;
		do{
		p= this.randomPoint(rnd);
		p=this.multiply(p,cof);
		}while(!p.equals(Point.INFINITY)&&!this.multiply(p,q).equals(Point.INFINITY));
		
		return p;
	}

}
