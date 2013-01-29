package uk.ac.ic.doc.jpair.ibe;

import java.io.UnsupportedEncodingException;


import java.security.KeyPair;
import java.util.Random;

import uk.ac.ic.doc.jpair.api.Field;
import uk.ac.ic.doc.jpair.api.FieldElement;
import uk.ac.ic.doc.jpair.api.Pairing;
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPrivateKey;
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPublicKey;
import uk.ac.ic.doc.jpair.ibe.key.BFUserPrivateKey;
import uk.ac.ic.doc.jpair.ibe.key.BFUserPublicKey;
import uk.ac.ic.doc.jpair.pairing.BigInt;
import uk.ac.ic.doc.jpair.pairing.Point;
import uk.ac.ic.doc.jpair.pairing.Predefined;

/**
 * This class implements the Boneh-Franklin Identity-Base Encryption (IBE) scheme. The scheme consists of four algorithms: 
 * (1) <b>Setup</b> generates global system parameters (master public key) and a master private key, 
 * (2) <b>Extract</b> uses the master key to generate the private key corresponding to an arbitrary public key string ID, 
 * (3) <b>Encrypt</b> encrypts messages using the public key ID, 
 * and (4) <b>Decrypt</b> decrypts messages using the corresponding private key.
 * This is the CCA secure version. For more information, see "Identity based encryption from the Weil pairing" by D. Boneh and M. Franklin 
SIAM J. of Computing, Vol. 32, No. 3, pp. 586-615, 2003. 
Extended abstract in proc. of Crypto '2001, LNCS Vol. 2139, Springer-Verlag, pp. 213-229, 2001.
 * @author Changyu Dong
 * @version 1.0
 */

public class BFCipher {
	static final int sigmaBitLength=512;
	static final String charSet ="UTF-8";

	/**
	 * The Setup algorithm. The pairing needs to be generated before hand. 
	 * @param e pre-configured pairing
	 * @param  rnd source of randomness
	 * @return a master key pair
	 */
	
	public static KeyPair setup(Pairing e,Random rnd){
		Point P = e.getCurve2().randomPoint(rnd);
		BigInt s = new BigInt(e.getGroupOrder().bitLength(),rnd);
		
		while(s.compareTo(e.getGroupOrder())>=0)
			s=s.shiftRight(1);
		
		Point Ppub = e.getCurve2().multiply(P, s);
		
		BFMasterPublicKey pk = new BFMasterPublicKey(e, P, Ppub);
		
		BFMasterPrivateKey sk = new BFMasterPrivateKey(s);
		
		return new KeyPair(pk,sk);
		
	}
	/**
	 * The Extract algorithm which generates a user's key pair given his ID.
	 * @param masterKey the master key generated when setting up the system
	 * @param ID a arbitrary string which is used as the user's ID
	 * @param  rnd source of randomness
	 * @return a user key pair related to the ID
	 */
	public static KeyPair extract(KeyPair masterKey, String ID,Random rnd){
		//user public key is ID+ public parameters
		
		BFUserPublicKey pk =new BFUserPublicKey(ID, (BFMasterPublicKey) masterKey.getPublic());
		
		
		Pairing e= ((BFMasterPublicKey)masterKey.getPublic()).getPairing();
		//user private key: hash(ID)->point Q
		//sQ, s is the master private key
		byte[] bid = null;
		try {
			bid = ID.getBytes(charSet);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Point Q = Util.hashToPoint(bid,e.getCurve(), e.getCofactor());
		BigInt s =((BFMasterPrivateKey)masterKey.getPrivate()).getKey();
		Q= e.getCurve().multiply(Q, s);
		
		BFUserPrivateKey sk = new BFUserPrivateKey(Q,(BFMasterPublicKey) masterKey.getPublic());
		
		return new KeyPair(pk,sk);
}
	/**
	 * The Encrypt algorithm which encrypts data using the public key of a user.
	 * @param pk a user's public key
	 * @param m the data to be encrypted, the size of the data is arbitrary
	 * @param  rnd source of randomness
	 * @return the ciphertext generated from the data
	 */

	public static BFCtext encrypt(BFUserPublicKey pk, byte[] m,Random rnd){
		Pairing e=pk.gerParam().getPairing();
		byte [] sigma = new byte[BFCipher.sigmaBitLength/8];
		
		rnd.nextBytes(sigma);
		
		//sigma||m
		byte [] toHash = new byte[sigma.length+m.length];
		System.arraycopy(sigma, 0, toHash, 0, sigma.length);
		System.arraycopy(m, 0, toHash, sigma.length, m.length);
		
		//hash(sigma||m) to biginteger r;
		Field field =e.getCurve2().getField();
		BigInt r =Util.hashToField(toHash, field);
		
		//hash(ID) to point
		byte[] bid = null;
		String ID =pk.gerKey();
		try {
			bid = ID.getBytes(charSet);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Point Q = Util.hashToPoint(bid,e.getCurve(), e.getCofactor());
		
		//gID = e(Q, sP), sP is Ppub
		FieldElement gID = e.compute(Q, pk.gerParam().getPpub());
		
		//U=rP
		Point U = e.getCurve2().multiply(pk.gerParam().getP(), r);
		//gID^r
		FieldElement gIDr = e.getGt().pow(gID,r);
		
		//V=sigma xor hash(gID^r)
		byte[] hash = Util.hashToLength(gIDr.toByteArray(), sigma.length);
		byte [] V = Util.xorTwoByteArrays(sigma,hash);
		
		//W =m xor hash(sigma)
		
		hash = Util.hashToLength(sigma, m.length);
		byte [] W =Util.xorTwoByteArrays(m,hash);
		
		return new BFCtext(U,V,W);
		
	}
	/**
	 * The Decrypt algorithm which decrypt a ciphertext using the corresponding private key.
	 * @param c the ciphertext to be decrypted
	 * @param sk the user's private key
	 * @return the plaintext data or null if the decryption fails.
	 */
	public static byte[] decrypt(BFCtext c, BFUserPrivateKey sk){
		BFMasterPublicKey msk =sk.getParam();
		Pairing e= msk.getPairing();
		
		//e(sQ,U), sQ is the user private key		
		FieldElement temp = e.compute(sk.getKey(), c.getU());
		
		//sigma = V xor hash(temp)
		byte [] hash = Util.hashToLength(temp.toByteArray(), c.V.length);
		
		byte[] sigma = Util.xorTwoByteArrays(c.V,hash);
		
		hash =Util.hashToLength(sigma, c.W.length);
		
		byte [] m = Util.xorTwoByteArrays(hash,c.W);
		
		//sigma||m
		byte [] toHash = new byte[sigma.length+m.length];
		System.arraycopy(sigma, 0, toHash, 0, sigma.length);
		System.arraycopy(m, 0, toHash, sigma.length, m.length);
		
		//hash(sigma||m) to biginteger r;
		Field field =e.getCurve2().getField();
		BigInt r =Util.hashToField(toHash, field);
		
		if(c.U.equals(e.getCurve2().multiply(msk.getP(), r)))
			return m;
		else
			return null;
		
		
		
	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		String ID ="Alice";
//		Random rnd =new Random();
//		KeyPair mst = BFCipher.setup(Predefined.ssTate(), rnd);
//		
//		KeyPair user = BFCipher.extract(mst, ID, rnd);
//		
//		byte [] m =new BigInt(2048,rnd).toByteArray();
//		BFCtext c =null;
//		Long startTime = System.nanoTime();
//		for (int i=0;i<1000;i++){
//		c = BFCipher.encrypt((BFUserPublicKey) user.getPublic(), m, rnd);
//		}
//		Long endTime = System.nanoTime()-startTime;
//		System.out.println("Encryption: "+endTime/1000000);
//		//BFCtext c = BFCipher.encrypt((BFUserPublicKey) user.getPublic(), m, rnd);
//		byte [] dec=null;
//
//		startTime = System.nanoTime();
//		for(int i=0;i<1000;i++){
//		dec =BFCipher.decrypt(c, (BFUserPrivateKey)user.getPrivate());
//		}
//		endTime = System.nanoTime()-startTime;
//		System.out.println("decryption: "+endTime/1000000);
//	//	byte [] dec =BFCipher.decrypt(c, (BFUserPrivateKey)user.getPrivate(), (BFMasterPublicKey) mst.getPublic());
//		System.out.println(new BigInt(m));
//		
//		System.out.println(new BigInt(dec));
//		
//
//	}
	


}
