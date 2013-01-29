package uk.ac.ic.doc.jpair.pairing;

import java.util.Random;

import uk.ac.ic.doc.jpair.api.FieldElement;
import uk.ac.ic.doc.jpair.api.Pairing;
/**
 * Generate pairing given security parameters.
 * @author Changyu Dong
 * @version 1.0
 *
 */
public class PairingFactory {

	/**
	 * Given security parameters, return a Tate pairing constructed using the supersingular curve y^2=x^3+x over a random field.
	 * @param groupSize the bit length of group G1
	 * @param fieldSize the bit length of p which defines GFp
	 * @param rnd the source of randomness 
	 * @return a Tate pairing constructed using the random supersingular curve over a random field.
	 */
	public static Pairing ssTate(int groupSize, int fieldSize, Random rnd){
		if (groupSize>fieldSize)
			throw new IllegalArgumentException("The size of the field must greater than the size of the group");
		BigInt r = findSolinas(groupSize);
		if(r.isProbablePrime(200))
			;
		else{
			System.out.println("not correct");
			System.out.println(r.toString(2));
			System.exit(-1);
		}

		//size of the cofactor/4
		int hL = fieldSize-r.bitLength()-2;
		BigInt p;
		BigInt [] cof = new BigInt[1];
		do{
			p=findP(r,hL,rnd,cof);
		}while(!p.isProbablePrime(200));
		
		Fp field = new Fp(p);
		
		FieldElement a4 = field.getOne();
		FieldElement a6 = field.getZero();
		EllipticCurve ec = new EllipticCurve(field,field.getZero(),field.getZero(),field.getZero(),a4,a6);
		
		return new TatePairing(ec,r,cof[0]);
	}
	
	static BigInt findP(BigInt r,int hL,Random rnd,BigInt[] cof){
		
		BigInt h;
		do{
			h=new BigInt(hL,rnd);
		}while(h.bitLength()!=hL);
		//h (cofactor) is a multiple of 4
		h=h.shiftLeft(2);
		BigInt p= h.multiply(r);
		p=p.subtract(BigInt.ONE);
		cof[0]=h;
		return p;
	}
	static BigInt findSolinas(int groupSize){
		int byteL = ((groupSize-1)>>>3) +1;
		byte[] rbyte = new byte[byteL];
		ByteArrayUtil.setTo1ByDegree(rbyte, groupSize-1);
		BigInt r = new BigInt(1,rbyte);
		r=r.add(BigInt.ONE);
		//2^x+1
		boolean found=false;
		for(int i=groupSize-2;i>0;i--){
			r=r.flipBit(i);
			if(r.isProbablePrime(100)){
				found=true;
				break;
			}
			r=r.flipBit(i);
		}
		if(found)
			return r;
		else
			return findSolinas(groupSize+1);
		
	}

}
