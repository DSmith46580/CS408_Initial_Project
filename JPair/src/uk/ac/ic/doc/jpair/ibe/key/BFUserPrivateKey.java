package uk.ac.ic.doc.jpair.ibe.key;

import java.security.PrivateKey;

import uk.ac.ic.doc.jpair.pairing.Point;
/**
 * This class defines structure for representing the user private key in the Boneh-Franklin 
 * Identity-Base Encryption (IBE) scheme. The private key consists of a copy of the master public key and
 *  a Point sQ where s is the 
 * master private key and Q is a point in G1 obtained by hashing the user's ID.   
 * @author Changyu Dong
 * @version 1.0
 */
public class BFUserPrivateKey implements PrivateKey {

	final Point key;
	final BFMasterPublicKey param;
	
	public BFUserPrivateKey(Point sk,BFMasterPublicKey param){ 
		this.key=sk;
		this.param=param;
	}
	/**
	 * @return "Boneh-Franklin IBE"
	 */
	@Override
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
	 * @return the point sQ
	 */
	public Point getKey(){
		return this.key;
		
	}
	/**
	 * @return the public parameters, i.e. the master public key
	 */
	public BFMasterPublicKey getParam(){
		return this.param;
		
	}

}
