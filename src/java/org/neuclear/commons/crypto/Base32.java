package org.neuclear.commons.crypto;

import java.math.BigInteger;

/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: Base32.java,v 1.1 2004/01/16 23:41:59 pelle Exp $
$Log: Base32.java,v $
Revision 1.1  2004/01/16 23:41:59  pelle
Added Base32 class. The Base32 encoding used wasnt following the standards.
Added user creatable Identity for Public Keys

*/

/**
 * Utility class for doing URL safe Base32 encodeings as specified in
 * <a href="http://www.waterken.com/dev/Enc/base32/">Tyler Close's Base32 page</a>
 */
public class Base32 {
    //Disallow Instantiation
    private Base32(){
    }
    /**
      * Encode in Base32 the given <code>{@link java.math.BigInteger}<code>.
      *
      * @param big
      * @return String with Base32 encoding
      */
     public static String encode(final BigInteger big) throws CryptoException {
//        System.out.println("JDK toByteArray(): "+encode(big.toByteArray()));
//        System.out.println("getBytes(): "+encode(getBytes(big)));
         return encode(big.toByteArray());
     }




     /**
      * Method decodeBase32Element
      *
      *
      * @param base32
      *
      * @return
      */
     public static byte[] decode(final byte[] base32) throws CryptoException {
         return null;
     }

     /**
      * <p>Decode a Base32-encoded string to a byte array</p>
      *
      * @param base32 <code>String</code> encoded string (single line only !!)
      * @return Decoded data in a byte array
      */
     public static byte[] decode(final String base32) throws CryptoException {
         return decode(base32.getBytes());

     }

     /**
      *
      * @param raw <code>byte[]<code> to be base32 encoded
      * @return the <code>String<code> with encoded data
      */
     public static String encode(final byte[] raw) throws CryptoException {
         return new String(encodeToByteArray(raw));
     }
     public static int getEncodedLength(int src){
         final int baselength = (src*8);
         final int mod=baselength%5;
         if (mod==0)
               return baselength/5;
         else
              return baselength/5+1;
     }
     public static byte[] encodeToByteArray(final byte[]raw) throws CryptoException {
         final int baselength = (raw.length*8);
         final int mod=baselength%5;
         final int length=getEncodedLength(raw.length);
         final byte encoded[]=new byte[length];
         final byte chunk[]=new byte[5];
         long chs=0;

         //ri= src index, ei= encoded index
         int ci=8;
         for (int ri=0,ei=0;ri<raw.length;ri=ri+5,ei=ei+8){
//             System.out.print(", "+ri);
             if (ri==(raw.length-1)&&mod>0) {
                 System.arraycopy(raw,ri,chunk,0,mod);
                 for(int j=mod;j<5;j++)
                    chunk[j]=0;
                 ci=(8*mod)/5+(((8*mod)%5==0)?0:1);
             } else
                System.arraycopy(raw,ri,chunk,0,5);
             chs=new BigInteger(chunk).longValue();
             for (int j=0;j<ci;j++){
                 encoded[ei+j]=encodeVal(getPart(chs,j));
             }
         }
         return encoded;
     }
     private static byte getPart(long chunk,int num){
         return (byte) ((chunk>>((7-num)*5))&31);
     }
     /**
      * Encode a String as base32
      *
      * @param raw <code>byte[]<code> to be base32 encoded
      * @return the <code>String<code> with encoded data
      */
     public static String encode(final String raw) throws CryptoException {
         return encode(raw.getBytes());
     }

    private static byte encodeVal(byte val) throws CryptoException {
        if (val>31)
            throw new CryptoException("Encode Overflow");
        return CROSS[val];
    }

    private static final byte[] CROSS = new byte[]{
        'a','b','c','d','e','f','g','h',
        'i','j','k','l','m','n','o','p',
        'q','r','s','t','u','v','w','x',
        'y','z','2','3','4','5','6','7'
    };


}
