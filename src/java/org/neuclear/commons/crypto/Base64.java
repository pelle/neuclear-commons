/* $Id: Base64.java,v 1.5 2004/02/19 15:29:10 pelle Exp $
 * $Log: Base64.java,v $
 * Revision 1.5  2004/02/19 15:29:10  pelle
 * Various cleanups and corrections
 *
 * Revision 1.4  2004/02/19 00:27:34  pelle
 * Discovered several incompatabilities with the xmlsig implementation. Have been working on getting it working.
 * Currently there is still a problem with enveloping signatures and it seems enveloped signatures done via signers.
 *
 * Revision 1.3  2004/01/10 00:01:40  pelle
 * Implemented new Schema for Transfer*
 * Working on it for Exchange*, so far all Receipts are implemented.
 * Added SignedNamedDocument which is a generic SignedNamedObject that works with all Signed XML.
 * Changed SignedNamedObject.getDigest() from byte array to String.
 * The whole malarchy in neuclear-pay does not build yet. The refactoring is a big job, but getting there.
 *
 * Revision 1.2  2003/11/21 04:43:41  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.1  2003/11/11 21:17:48  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/02/23 23:21:02  pelle
 * Yeah. We figured it out. We now have interop.
 * Granted not on all features as yet, but definitely on simple signatures.
 * I'm checking in Ramses' fix to QuickEmbeddedSignature and my fixes to the verification process.
 *
 * Revision 1.2  2003/02/22 16:54:10  pelle
 * Major structural changes in the whole processing framework.
 * Verification now supports Enveloping and detached signatures.
 * The reference element is a lot more important at the moment and handles much of the logic.
 * Replaced homegrown Base64 with Blackdowns.
 * Still experiencing problems with decoding foreign signatures. I reall dont understand it. I'm going to have
 * to reread the specs a lot more and study other implementations sourcecode.
 *
 * Revision 1.1  2003/02/11 14:47:02  pelle
 * Added benchmarking code.
 * DigestValue is now a required part.
 * If you pass a keypair when you sign, you get the PublicKey included as a KeyInfo block within the signature.
 *
 * Revision 1.2  2003/02/08 18:48:37  pelle
 * The Signature phase has been rewritten.
 * There now is a new Class called QuickEmbeddedSignature which is more in line with my original idea for this library.
 * It simply has a template of the xml and signs it in a standard way.
 * The original XMLSignature class is still used for verification and will in the future handle more thoroughly
 * all the various flavours of XMLSig.
 * XMLSecTools has got different flavours of canonicalize now. Including one where you can pass it a Canonicaliser to use.
 * Of the new Canonicalizer's are CanonicalizerWithComments, which I accidently left out of the last commit.
 * And CanonicalizerWithoutSignature which leaves out the Signature in the Canonicalization phase and is thus
 * a lot more efficient than the previous approach.
 *
 * Revision 1.1  2003/01/18 18:12:32  pelle
 * First Independent commit of the Independent XML-Signature API for NeuDist.
 *
 * Revision 1.2  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *
 */
package org.neuclear.commons.crypto;

import java.math.BigInteger;


/**
 * My modification of the Apache Groups XML-Security Base64 class.
 * Main changes are that it's modified to use Dom4J rather than DOM
 * <hr>
 * <b>Original Javadoc:</b><br>
 * Implementation of MIME's Base64 encoding and decoding conversions.
 * Optimized code. (raw version taken from oreilly.jonathan.util)
 *
 * @author pelleb
 * @author Anli Shundi
 * @author Christian Geuer-Pollmann
 * @see <A HREF="ftp://ftp.isi.edu/in-notes/rfc2045.txt">RFC 2045</A>
 */
public final class Base64 {

    /**
     * Field LINE_SEPARATOR
     */
    public static final String LINE_SEPARATOR = "\n";

    /**
     * Field BASE64DEFAULTLENGTH
     */
    public static final int BASE64DEFAULTLENGTH = 76;


    private Base64() {
        // we don't allow instantiation
    }


    /**
     * Method getBase64WrapLength
     *
     * @return
     */
    public static int getBase64WrapLength() {
        return BASE64DEFAULTLENGTH;
    }

    /**
     * Returns a byte-array representation of a <code>{@link java.math.BigInteger}<code>.
     * No sign-bit is outputed.
     * <p/>
     * <p><b>N.B.:</B> <code>{@link java.math.BigInteger}<code>'s toByteArray
     * retunrs eventually longer arrays because of the leading sign-bit.
     *
     * @param big <code>BigInteger<code> to be converted
     * @return a byte array with <code>bitlen</code> bits of <code>big</code>
     */
    public static byte[] getBytes(final BigInteger big) {

        int bitlen = big.bitLength();
        //round bitlen
        bitlen = ((bitlen + 7) >> 3) << 3;

        if (bitlen < big.bitLength()) {
            throw new IllegalArgumentException("Base64.IllegalBitlength");
        }

        final byte[] bigBytes = big.toByteArray();

        if (((big.bitLength() % 8) != 0)
                && (((big.bitLength() / 8) + 1) == (bitlen / 8))) {
            return bigBytes;
        } else {

            // some copying needed
            int startSrc = 0;    // no need to skip anything
            int bigLen = bigBytes.length;    //valid length of the string

            if ((big.bitLength() % 8) == 0) {    // correct values
                startSrc = 1;    // skip sign bit

                bigLen--;    // valid length of the string
            }

            final int startDst = bitlen / 8 - bigLen;    //pad with leading nulls
            final byte[] resizedBytes = new byte[bitlen / 8];

            System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, bigLen);

            return resizedBytes;
        }
    }

    /**
     * Encode in Base64 the given <code>{@link java.math.BigInteger}<code>.
     *
     * @param big
     * @return String with Base64 encoding
     */
    public static String encode(final BigInteger big) {
//        System.out.println("JDK toByteArray(): "+encode(big.toByteArray()));
//        System.out.println("getBytes(): "+encode(getBytes(big)));
        return encode(big.toByteArray());
    }


    public static String encodeClean(final byte[] bytes) {
        return LINE_SEPARATOR + encode(bytes, BASE64DEFAULTLENGTH) + LINE_SEPARATOR;
    }


    /**
     * Method decodeBase64Element
     *
     * @param base64
     * @return
     */
    public static byte[] decode(final byte[] base64) throws CryptoException {
        return org.bouncycastle.util.encoders.Base64.decode(base64);
    }

    /**
     * <p>Decode a Base64-encoded string to a byte array</p>
     *
     * @param base64 <code>String</code> encoded string (single line only !!)
     * @return Decoded data in a byte array
     */
    public static byte[] decode(final String base64) throws CryptoException {
        return org.bouncycastle.util.encoders.Base64.decode(base64);

    }

    /**
     * <p>Encode a byte array in Base64 format and return an optionally
     * wrapped line</p>
     *
     * @param raw  <code>byte[]</code> data to be encoded
     * @param wrap <code>int<code> length of wrapped lines; No wrapping if less than 4.
     * @return a <code>String</code> with encoded data
     */
    public static String encode(final byte[] raw, final int wrap) {
        final byte[] b64 = org.bouncycastle.util.encoders.Base64.encode(raw);

        //calculate length of encoded string
        final int encLen = b64.length;

        final int lines = (encLen / wrap);

        final byte[] encoded = new byte[encLen + lines];
        int sx = 0, dx = 0;
        for (sx = 0; sx < (lines * wrap); sx += wrap, dx += (wrap + 1)) {
            System.arraycopy(b64, sx, encoded, dx, wrap);
            encoded[dx + wrap] = '\n';
        }
        System.arraycopy(b64, sx, encoded, dx, encLen - sx);


        return new String(encoded);
    }

    /**
     * Encode a byte array and fold lines at the standard 76th character.
     *
     * @param raw <code>byte[]<code> to be base64 encoded
     * @return the <code>String<code> with encoded data
     */
    public static String encode(final byte[] raw) {
        return new String(org.bouncycastle.util.encoders.Base64.encode(raw));
//        return encode(raw, Base64.getBase64WrapLength());
    }

    /**
     * Encode a String as base64
     *
     * @param raw <code>byte[]<code> to be base64 encoded
     * @return the <code>String<code> with encoded data
     */
    public static String encode(final String raw) {
        return new String(org.bouncycastle.util.encoders.Base64.encode(raw.getBytes()));
//        return encode(raw, Base64.getBase64WrapLength());
    }

}
