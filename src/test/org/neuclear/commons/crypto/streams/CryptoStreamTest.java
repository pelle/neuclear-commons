package org.neuclear.commons.crypto.streams;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/*
$Id: CryptoStreamTest.java,v 1.1 2004/03/31 18:48:27 pelle Exp $
$Log: CryptoStreamTest.java,v $
Revision 1.1  2004/03/31 18:48:27  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 11:16:58 AM
 */
public class CryptoStreamTest extends TestCase {

    public CryptoStreamTest(String name) throws NoSuchAlgorithmException {
        super(name);
        rsa = CryptoTools.createTinyRSAKeyPair();
        dsa = CryptoTools.createTinyDSAKeyPair();
    }


    public void testStrings() throws GeneralSecurityException, IOException, CryptoException {
        assertEncryptDecrypt("");
        assertEncryptDecrypt("1");
        assertEncryptDecrypt("12");
        assertEncryptDecrypt("12345678890--");
        assertEncryptDecrypt("00000000000000000000000000000000000000000000000");
        assertEncryptDecrypt("the quick brown fox jumped over the lazy dog");

    }

    public void assertEncryptDecrypt(String test) throws GeneralSecurityException, IOException, CryptoException {
        final byte[] bytes = test.getBytes("UTF-8");
        final byte[] encrypted = encrypt(bytes);
        assertNotNull(encrypted);

        final byte[] decrypted = decrypt(encrypted);
        assertNotNull(decrypted);

        assertEquals(bytes, decrypted);

        assertBack2BackOutputStreams(bytes);
        assertBack2BackInputStreams(bytes);
    }

    public byte[] encrypt(byte data[]) throws GeneralSecurityException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PasswordEncryptingOutputStream sos = new PasswordEncryptingOutputStream(bos, "super secret squirrel".toCharArray());
        sos.write(data);
        sos.close();
        return bos.toByteArray();
    }

    public byte[] decrypt(byte data[]) throws GeneralSecurityException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PasswordDecryptingOutputStream sos = new PasswordDecryptingOutputStream(bos, "super secret squirrel".toCharArray());
        sos.write(data);
        sos.close();
        return bos.toByteArray();
    }

    public void assertBack2BackOutputStreams(byte data[]) throws GeneralSecurityException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PasswordDecryptingOutputStream eos = new PasswordDecryptingOutputStream(bos, "super secret squirrel".toCharArray());
        PasswordEncryptingOutputStream sos = new PasswordEncryptingOutputStream(eos, "super secret squirrel".toCharArray());
        sos.write(data);
        sos.close();
        assertEquals(data, bos.toByteArray());
    }

    public void assertBack2BackInputStreams(byte data[]) throws GeneralSecurityException, IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        PasswordEncryptingInputStream eos = new PasswordEncryptingInputStream(bis, "super secret squirrel".toCharArray());
        PasswordDecryptingInputStream sos = new PasswordDecryptingInputStream(eos, "super secret squirrel".toCharArray());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b = sos.read();
        while (b != -1) {
            bos.write(b);
            b = sos.read();
        }

        assertEquals(data, bos.toByteArray());
    }

    public void assertEquals(byte a[], byte b[]) {
        assertTrue((a == null) == (b == null));
        if (a == null) return;
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i]);
        }
    }

    private KeyPair rsa;
    private KeyPair dsa;
}
