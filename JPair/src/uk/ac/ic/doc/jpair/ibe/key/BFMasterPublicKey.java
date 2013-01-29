package uk.ac.ic.doc.jpair.ibe.key;

import java.security.PublicKey;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.Pairing;
import uk.ac.ic.doc.jpair.pairing.Point;

/**
 * This class defines structure for representing the global parameters (master public key) in the Boneh-Franklin 
 * Identity-Base Encryption (IBE) scheme. It include a paring e: G1 X G2 ->Gt, 
 * a random point P in G2, another point Ppub = sP in G2 where s is the master private key.  
 * @author Changyu Dong
 * @version 1.0
 */
public class BFMasterPublicKey implements PublicKey {
	final Pairing e;
	final Point P;
	final Point Ppub;
	
	public BFMasterPublicKey(Pairing e, Point P, Point Ppub){
		this.e=e;
		this.P=P;
		this.Ppub=Ppub;	
	}

	@Override
	/**
	 * @return "Boneh-Franklin IBE"
	 */
	public String getAlgorithm() {
		// TODO Auto-generated method stub
		return "Boneh-Franklin IBE";
	}

	/**
	 * @return null
	 */
	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return null
	 */
	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the pairing
	 */
	public Pairing getPairing(){
		return this.e;
	}
	/**
	 * @return the point P
	 */
	public Point getP(){
		return this.P;
	}
	/**
	 * @return the point Ppub
	 */
	public Point getPpub(){
		return this.Ppub;
	}

}
