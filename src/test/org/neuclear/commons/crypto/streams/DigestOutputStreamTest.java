package org.neuclear.commons.crypto.streams;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/*
$Id: DigestOutputStreamTest.java,v 1.1 2004/03/31 18:48:28 pelle Exp $
$Log: DigestOutputStreamTest.java,v $
Revision 1.1  2004/03/31 18:48:28  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 11:16:58 AM
 */
public class DigestOutputStreamTest extends TestCase {

    public DigestOutputStreamTest(String name) {
        super(name);
    }


    public void testStrings() throws NoSuchAlgorithmException, IOException {
        assertDigestEquals("");
        assertDigestEquals("1");
        assertDigestEquals("12");
        assertDigestEquals("12345678890--");
        assertDigestEquals("00000000000000000000000000000000000000000000000");
        assertDigestEquals("the quick brown fox jumped over the lazy dog");

    }

    public void assertDigestEquals(String test) throws NoSuchAlgorithmException, IOException {
        assertEquals(new String(CryptoTools.digest(test)), getStreamDigest(test));

    }

    public String getStreamDigest(String data) throws NoSuchAlgorithmException, IOException {
        DigestOutputStream dig = new DigestOutputStream();
        dig.write(data.getBytes());
        return new String(dig.getDigest());
    }
}
