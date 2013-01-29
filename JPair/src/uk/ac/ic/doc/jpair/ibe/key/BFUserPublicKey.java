package uk.ac.ic.doc.jpair.ibe.key;

import java.security.PublicKey;


/**
 * This class defines structure for representing the user public key in the Boneh-Franklin 
 * Identity-Base Encryption (IBE) scheme. The public key consists of a string, i.e. the ID of the user  and a copy
 * of the master public key.  
 * @author Changyu Dong
 * @version 1.0
 */
public class BFUserPublicKey implements PublicKey {
	final String id;
	final BFMasterPublicKey param;
	
	public BFUserPublicKey(String id,BFMasterPublicKey param){ 
		this.id=id;
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
	 * @return the id
	 */
	public String gerKey(){
		return this.id;
	}
	/**
	 * @return the public parameters, i.e. the master public key
	 */
	public BFMasterPublicKey gerParam(){
		return this.param;
	}

}
