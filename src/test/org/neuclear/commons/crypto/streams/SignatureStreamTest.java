package org.neuclear.commons.crypto.streams;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/*
$Id: SignatureStreamTest.java,v 1.1 2004/03/31 18:48:28 pelle Exp $
$Log: SignatureStreamTest.java,v $
Revision 1.1  2004/03/31 18:48:28  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 11:16:58 AM
 */
public class SignatureStreamTest extends TestCase {

    public SignatureStreamTest(String name) throws NoSuchAlgorithmException {
        super(name);
        rsa = CryptoTools.createTinyRSAKeyPair();
        dsa = CryptoTools.createTinyDSAKeyPair();
    }


    public void testStrings() throws NoSuchAlgorithmException, IOException, CryptoException, SignatureException, InvalidKeyException {
        assertSignatureEquals("");
        assertSignatureEquals("1");
        assertSignatureEquals("12");
        assertSignatureEquals("12345678890--");
        assertSignatureEquals("00000000000000000000000000000000000000000000000");
        assertSignatureEquals("the quick brown fox jumped over the lazy dog");

    }

    public void assertSignatureEquals(String test) throws NoSuchAlgorithmException, IOException, CryptoException, SignatureException, InvalidKeyException {
        final byte[] bytes = test.getBytes("UTF-8");
        assertSignature(rsa, bytes);
//        assertSignature(dsa,bytes); I have to disable this as CryptoTools converts the signature output for DSA in a way that is required of XMLSignature

    }

    private void assertSignature(KeyPair kp, final byte[] bytes) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, CryptoException {
        final byte[] ssig = getStreamSignature(kp, bytes);
        assertEquals(CryptoTools.sign(kp, bytes), ssig);
        assertEquals(CryptoTools.verify(kp.getPublic(), bytes, ssig), getStreamVerifier(kp, bytes, ssig));
        ssig[0] = (byte) (ssig[0] & 1); // tamper with signature
        assertFalse(getStreamVerifier(kp, bytes, ssig));


    }

    public byte[] getStreamSignature(KeyPair kp, byte data[]) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        SigningOutputStream sig = new SigningOutputStream(kp);
        sig.write(data);
        return sig.sign();
    }

    public boolean getStreamVerifier(KeyPair kp, byte data[], byte sig[]) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        VerifyingOutputStream ver = new VerifyingOutputStream(kp, sig);
        ver.write(data);
        return ver.verify();
    }

    public void assertEquals(byte a[], byte b[]) {
        assertTrue((a == null) == (b == null));
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i]);
        }
    }

    private KeyPair rsa;
    private KeyPair dsa;
}
