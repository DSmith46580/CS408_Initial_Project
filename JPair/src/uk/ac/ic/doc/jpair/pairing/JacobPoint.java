package uk.ac.ic.doc.jpair.pairing;




class JacobPoint {
	public static final JacobPoint INFINITY= new JacobPoint();
	private JacobPoint(){
		this.x=BigInt.ONE;
		this.y=BigInt.ONE;
		this.z=BigInt.ZERO;
	}

	private BigInt x;
	private BigInt y;
	private BigInt z;
	
	public JacobPoint(BigInt x, BigInt y,BigInt z ){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public BigInt getX(){
		return this.x;
	}
	
	public BigInt getY(){
		return this.y;
	}
	
	public BigInt getZ(){
		return this.z;
	}
	
	public boolean isInfinity(){
		//return this.equals(JacobPoint.INFINITY);
		return this.z.equals(BigInt.ZERO);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
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
		JacobPoint other = (JacobPoint) obj;
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
		if (z == null) {
			if (other.z != null)
				return false;
		} else if (!z.equals(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + x +"," + y + "," + z + "]";
	}
	
	public void setX(BigInt newX){
		this.x=newX;
	}
	
	public void setY(BigInt newY){
		this.y=newY;
	}
	
	public void setZ(BigInt newZ){
		this.z=newZ;
	}
	

}
