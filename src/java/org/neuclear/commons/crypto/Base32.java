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

$Id: Base32.java,v 1.4 2004/01/19 23:49:29 pelle Exp $
$Log: Base32.java,v $
Revision 1.4  2004/01/19 23:49:29  pelle
Unit testing uncovered further issues with Base32
NSTools is now uptodate as are many other classes. All transactional builders habe been updated.
Well on the way towards full "green" on Junit.

Revision 1.3  2004/01/19 17:53:13  pelle
Various clean ups

Revision 1.2  2004/01/18 21:20:20  pelle
Created Base32 encoder that now fully complies with Tyler's spec.

Revision 1.1  2004/01/16 23:41:59  pelle
Added Base32 class. The Base32 encoding used wasnt following the standards.
Added user creatable Identity for Public Keys

*/

/**
 * Utility class for doing URL safe Base32 encodeings as specified in
 * <a href="http://www.waterken.com/dev/Enc/base32/">Tyler Close's Base32 page</a>
 */
public final class Base32 {
    //Disallow Instantiation
    private Base32(){

    }
    /**
      * Encode in Base32 the given <code>{@link java.math.BigInteger}<code>.
      *
      * @param big
      * @return String with Base32 encoding
      */
     public static String encode(final BigInteger big) {
//        System.out.println("JDK toByteArray(): "+encode(big.toByteArray()));
//        System.out.println("getBytes(): "+encode(getBytes(big)));
         return encode(big.toByteArray());
     }



     /**
      * Method decodeBase32Element
      *
      *
      * @param raw
      *
      * @return
      */
     public static byte[] decode(final byte[] raw) throws CryptoException {
         final int baselength = (raw.length*5);
         final int mod=baselength%8;
         final int length=getDecodedLength(raw.length);
         final byte decoded[]=new byte[length];
         final byte chunk[]=new byte[5];
         long chs=0;

         for (int ri=0,di=0;ri<(raw.length);ri=ri+8,di=di+5){
             int rchsize=(raw.length-ri)>=8?8:(raw.length-ri);
             for (int j=0;j<rchsize;j++){
                 chs=(chs<<5)|decodeVal(raw[ri+j]);
             }
             switch(rchsize){
                 case 0:
                     break;
                 case 8:
                     break;
                 case 2:
                     if ((chs&0x03)!=0)
                        throw new CryptoException("Encoding Error");
                     chs>>=2;
                     break;
                 case 4:
                     if ((chs&0x0F)!=0)
                        throw new CryptoException("Encoding Error");
                     chs>>=4;
                     break;
                 case 5:
                     if ((chs&0x01)!=0)
                        throw new CryptoException("Encoding Error");
                     chs>>=1;
                     break;
                 case 7:
                     if ((chs&0x07)!=0)
                        throw new CryptoException("Encoding Error");
                     chs>>=3;
                     break;
                 default:
                        throw new CryptoException("Encoding Error");
             }
             int chsize=(length-di)>=5?5:(length-di);
             for (int j=0;j<chsize;j++){
                 decoded[di+(chsize-1-j)]=(byte) ((chs>>(8*j))&0xFF);
             }

         }
         return decoded;
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
     public static String encode(final byte[] raw)  {
         return new String(encodeToByteArray(raw));
     }
     public static int getEncodedLength(int src)  {
         final int baselength = src*8;
         final int mod=baselength%5;
         if (mod!=0)
               return baselength/5+1;
         return baselength/5;
     }
    public static int getDecodedLength(int src)  {
        final int baselength = src*5;
        return baselength/8;
    }

    public static byte[] encodeToByteArray(final byte[]raw)  {
         final int baselength = (raw.length*8);
         final int mod=baselength%5;
         final int length=getEncodedLength(raw.length);
         final byte encoded[]=new byte[length];
         final byte chunk[]=new byte[8];
         long chs=0;

         //ri= src index, ei= encoded index
         int ci=5;
         for (int ri=0,ei=0;ri<raw.length;ri=ri+5,ei=ei+8){
             chs=0;
             int rcsize=(raw.length-ri)>=5?5:(raw.length-ri);
             for (int j=ri;j<(ri+rcsize);j++){
                 chs|=raw[j];
                 chs<<=8;
             }
             chs>>=8;
             int ecsize=(length-ei)>=8?8:(length-ei);
             switch((ecsize)){
                 case 2:
                     chs<<=2;
                     break;
                 case 4:
                     chs<<=4;
                     break;
                 case 5:
                     chs<<=1;
                     break;
                 case 7:
                     chs<<=3;
                     break;
             }
             for (int j=0;j<ecsize;j++){
                 encoded[ei+ecsize-1-j]=encodeVal((byte) (chs&31));
                 chs>>>=5;
             }
         }
         return encoded;
     }
     private static byte getPart(long chunk,int num){
         return (byte) ((chunk>>>((7-num)*5))&31);
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

    private static byte encodeVal(byte val) {
        if (val>31)
            throw new RuntimeException("Base32 Encoding Overflow");
        return CROSS[val];
    }
    private static byte decodeVal(byte val) throws CryptoException {
        if(val >= 'a' && val <= 'z')
            return (byte) (val - 'a');
        else if(val >= '2' && val <= '7')
            return (byte) (val - '2' + 26);
        else if(val>= 'A' && val <= 'Z')
            return (byte)(val - 'A');
        else
            throw new CryptoException("Encode Overflow");
    }

    private static final byte[] CROSS = new byte[]{
        'a','b','c','d','e','f','g','h',
        'i','j','k','l','m','n','o','p',
        'q','r','s','t','u','v','w','x',
        'y','z','2','3','4','5','6','7'
    };


}
