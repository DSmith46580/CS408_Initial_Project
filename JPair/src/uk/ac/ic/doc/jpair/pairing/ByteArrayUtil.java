package uk.ac.ic.doc.jpair.pairing;

class ByteArrayUtil {
	private static final String HEXES = "0123456789ABCDEF";
	
	  public static String getHex( byte [] raw ) {
		    if ( raw == null ) {
		      return null;
		    }
		    final StringBuilder hex = new StringBuilder( 2 * raw.length );
		    for ( final byte b : raw ) {
		      hex.append(HEXES.charAt((b & 0xF0) >> 4))
		         .append(HEXES.charAt((b & 0x0F)));
		    }
		    return hex.toString();
		  }
	  
	    public static byte[] toBytesFromHex(String hex) {
	        byte rc[] = new byte[hex.length() / 2];
	        for (int i = 0; i < rc.length; i++) {
	            String h = hex.substring(i * 2, i * 2 + 2);
	            int x = Integer.parseInt(h, 16);
	            rc[i] = (byte) x;
	        }
	        return rc;
	    }
			  

	
    private static byte bitiIs1[] = {(byte) 0x80,0x40,0x20,0x10,0x8,0x04,0x02,0x01};
	public static int degreeOf(byte[] ba) {
		// TODO Auto-generated method stub
		for(int i=0;i<ba.length;i++){
			//first non-zero byte
			if (ba[i]!=0){
				return (ba.length-i)*8-numberOfLeadingZeroBits(ba[i])-1;
			}
		}
		//all zero.
		return -1;
	}
	
    /**
     * Returns the number of leading zero bits in a byte.
     */
	
	public static int numberOfLeadingZeroBits(byte b){
		int result;
		if(b<0)
			result= 0;
		else if(b>=64)
			result=1;
		else if (b>=32)
			result=2;
		else if(b>=16)
			result=3;
		else if(b>=8)
			result=4;
		else if(b>=4)
			result=5;
		else if(b>=2)
			result=6;
		else if(b==1)
			result=7;
		else
			result=8;
		return result;
		
	}
	
	

	
	

	public static int indexOfFirstNonZeroByte(byte[] r) {
		// TODO Auto-generated method stub
		int i;
		for(i=0;i<r.length;i++){
			if(r[i]!=0)
				return i;
		}
		return i;
	}
	
	public static void setTo1ByDegree(byte[] q, int degree) {
		// TODO Auto-generated method stub
		int nByte= degree/8;
		int byteIndex=q.length-1-nByte;
		int nbit=(degree)%8;
		int bitIndex =7-nbit;
		q[byteIndex]=(byte) (q[byteIndex]|(bitiIs1[bitIndex]));
		
	}
	
	
	
    /**
     * Returns a copy of the input array stripped of any leading zero bytes.
     */
    public static byte[] stripLeadingZeroBytes(byte[] val) {
        int byteLength = val.length;
	int keep;

	// Find first nonzero byte
	for (keep=0; keep<val.length && val[keep]==0; keep++)
	    ;
	
	//if the first byte is nonzero, return the input byte array
	if (keep==0){
		return (byte[])val.clone();
	}
	//if all zero, return a byte array represents 0;
	else if (keep==val.length){
		return new byte[1];
	}

	else{
	// otherwise, allocate new array and copy relevant part of input array
        int newLength = byteLength - keep;
        byte[] result = new byte[newLength];

        //for (int i=0;i<newLength;i++){
        //	result[i]=val[i+keep];
        //}
        System.arraycopy(val, keep, result, 0, newLength);
        return result;
	}
    }
    
	
    /**
     * Returns the bit as specified degree. if the input degree is out of range or negative;
     * return 0
     * @param  deg the degree
     * @return the bit as a boolean
     */

	
	public static boolean getBitByDegree(int deg, byte[] ba){
		int bits=ba.length*8;
		if(deg<0||deg>bits)
			return false;
		
		int index=bits-deg-1;
		int byteIndex=index/8;
		int bitIndex=index%8;
		
		byte b=ba[byteIndex];
		byte b2=(byte) (b<<bitIndex);
		
		return b2<0;

	}
}
