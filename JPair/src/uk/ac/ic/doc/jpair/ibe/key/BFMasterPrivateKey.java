package uk.ac.ic.doc.jpair.ibe.key;



import java.security.PrivateKey;

import uk.ac.ic.doc.jpair.pairing.BigInt;
/**
 * This class defines structure for representing the master private key in the Boneh-Franklin 
 * Identity-Base Encryption (IBE) scheme. The private key is s, a random big integer.   
 * @author Changyu Dong
 * @version 1.0
 */
public class BFMasterPrivateKey implements PrivateKey {
	
	final BigInt s;
	
	public BFMasterPrivateKey(BigInt s){
		this.s=s;
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
	 * @return the big integer s
	 */
	public BigInt getKey(){
		return this.s;
	}

}
