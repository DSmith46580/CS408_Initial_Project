package uk.ac.ic.doc.jpair.pairing;

import uk.ac.ic.doc.jpair.api.FieldElement;
/**
 * This class implements the compressed Tate pairing as described in Michael Scott, Paulo S. L. M. Barreto: Compressed Pairings. CRYPTO 2004: 140-156.
 * The output value is reduced to half length. Instead of 
 * keeping the full a+bi value of the Tate pairing, it may be possible for cryptographic purposes to discard b
 * altogether, leaving the values defined only up to conjugation, which means one of the pairing arguments will
 * only be defined up to a sign. If the output of the Tate pairing e(P,Q) = a+bi, then the output of the 
 * compressed Tata pairing ce(P,Q) is only a. Note that when using compressed pairing ce(P,Q) =ce(-P,Q)=ce(-P,-Q)=ce(P,-Q),  
 * so only use the compressed Tate Pairing when the sign is not important.  
 * @author Changyu Dong
 * @version 1.0
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplex
 * @see uk.ac.ic.doc.jpair.pairing.HalfComplexField
 *
 */

public class CompressedTatePairing extends TatePairing {


	private static final long serialVersionUID = -4652529649730955560L;

	public CompressedTatePairing(EllipticCurve curve, BigInt groupOrder,
			BigInt coFactor) {
		super(curve, groupOrder, coFactor);
		// TODO Auto-generated constructor stub
		this.gt=new HalfComplexField((Fp) this.field);
	}

	/**
	 * Computes the compressed Tate Pairing.
	 * @return A HalfComplex whose value is tr(e(P, Q))
	 */
	@Override
	public FieldElement compute(Point P, Point Q) {
		// TODO Auto-generated method stub
		Complex f = computeF(P,Q);
		HalfComplex hf=f.toHalfComplex();
		return hf.pow(finalExponent);	
	}
	
	
	Complex computeF(Point P, Point Q){
		FieldElement f=new Complex((Fp)this.field,BigInt.ONE);
		JacobPoint V =this.ec.aToJ(P);
		BigInt n =this.order.subtract(BigInt.ONE);
		Point nP= ec.negate(P);
		ComplexField cf= new ComplexField((Fp) this.field);

		//byte[] ba =n.toByteArray();
		byte[] r =naf(this.order, (byte)2);
		//Point T;
		//BigInt [] lamda= new BigInt[1];;
		FieldElement u;
		for(int i=r.length-2;i>=0;i--){
			u=encDbl(V, Q);
			f=cf.multiply(cf.square(f),u);
			if(r[i]==1){
				u=encAdd(V, P, Q);
				f=cf.multiply(f,u);
			}
			if(r[i]==-1){
				u=encAdd(V, nP, Q);
				f=cf.multiply(f,u);
			}
		}

		return ((Complex) f).conjugate().divide((Complex )f);
		
		//return f.pow(this.finalExponent);
		//return f;	
	}

}
