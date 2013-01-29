package uk.ac.ic.doc.jpair.ibe;

import uk.ac.ic.doc.jpair.pairing.Point;
/**
 * This class defines the structure of the ciphertext in the Boneh-Franklin Identity-Base Encryption (IBE) scheme. 
 * The ciphertext has three parts (U,V,W) as described in the original paper.
 * @author Changyu Dong
 * @version 1.0
 */
public class BFCtext {
	final Point U;
	final byte [] V;
	final byte [] W;
	
	public BFCtext(Point u, byte[] v, byte [] w){
		this.U=u;
		this.V=v;
		this.W=w;
	}
	
	public Point getU(){
		return this.U;
	}
	
	public byte[] getV(){
		return this.V;
	}
	
	public byte[] getW(){
		return this.W;
	}

}
