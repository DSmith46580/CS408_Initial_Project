package uk.ac.ic.doc.jpair.pairing;

import java.io.Serializable;
import java.math.BigInteger;

import uk.ac.ic.doc.jpair.api.FieldElement;


/**
 * A point on elliptic curves in Affine coordinates. The point at Infinity is represented as (null,null).
 *      
 * @author Changyu Dong
 * @version 1.0
 */
public class Point implements Serializable{
    /**
	 * 
	 */
	public static final Point INFINITY = new Point();
	private static final long serialVersionUID = 7194178130760372864L;
	private Point(){
		this.x=null;
		this.y=null;
	}
	private FieldElement x;
	private FieldElement y;
	
    /**
     * Create a Point.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
	public Point (FieldElement x,FieldElement y){
		this.x=x;
		this.y=y;
	}

    /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.abstractPoint#getX()
	 */
	public FieldElement getX() {
		// TODO Auto-generated method stub
		return x;
	}
    /* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.abstractPoint#getY()
	 */
	public FieldElement getY() {
		// TODO Auto-generated method stub
		return y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		if (!(obj instanceof Point))
			return false;
		Point other = (Point) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.abstractPoint#isInfinity()
	 */
	public boolean isInfinity() {
		// TODO Auto-generated method stub
		return this.x==null&&this.y==null;
	}

	@Override
    /**
     * Return a string representation of the point "(x,y)". x,y are in decimal formal.
     */
	public String toString() {
		if(this.isInfinity())
			return "(null,null)";
		return "(" + x + "," + y + ")";
	}
	
    /**
     * Return a string representation of the point "(x,y)".x,y are the given radix.
     */
	public String toString(int radix) {
		if(this.isInfinity())
			return "(null,null)";
		return "(" + x.toString(radix) + "," + y.toString(radix) + ")";
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.abstractPoint#setX(java.math.BigInteger)
	 */
	public void setX(FieldElement newX){
		this.x=newX;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ic.doc.jpbc.pairing.abstractPoint#setY(java.math.BigInteger)
	 */
	public void setY(FieldElement newY){
		this.y=newY;
	}
	
	
}
