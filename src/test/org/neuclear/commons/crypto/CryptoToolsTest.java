/*
  $Id: CryptoToolsTest.java,v 1.1 2003/11/11 21:17:52 pelle Exp $
  $Log: CryptoToolsTest.java,v $
  Revision 1.1  2003/11/11 21:17:52  pelle
  Further vital reshuffling.
  org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
  org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
  Did a bit of work on the Canonicalizer and changed a few other minor bits.

  Revision 1.5  2003/11/09 03:27:09  pelle
  More house keeping and shuffling about mainly pay

  Revision 1.4  2003/10/21 22:30:33  pelle
  Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
  Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
  Obviously had to perform many changes throughout the code to support these changes.

  Revision 1.3  2003/09/26 23:52:47  pelle
  Changes mainly in receiver and related fun.
  First real neuclear stuff in the payment package. Added TransferContract and AssetControllerReceiver.

  Revision 1.2  2003/02/11 14:50:24  pelle
  Trying onemore time. Added the benchmarking code.
  Now generates DigestValue and optionally adds KeyInfo to Signature.

  Revision 1.1  2003/02/08 20:55:08  pelle
  Some documentation changes.
  Major reorganization of code. The code is slowly being cleaned up in such a way that we can
  get rid of the org.neuclear.utils package and split out the org.neuclear.xml.soap package.
  Got rid of tons of unnecessary dependencies.

  Revision 1.1  2003/01/18 18:12:32  pelle
  First Independent commit of the Independent XML-Signature API for NeuDist.

  Revision 1.1  2003/01/16 19:16:09  pelle
  Major Structural Changes.
  We've split the test classes out of the normal source tree, to enable Maven's test support to work.
  WARNING
  for Development purposes the code does not as of this commit until otherwise notified actually verifysigs.
  We are reworking the XMLSig library and need to continue work elsewhere for the time being.
  DO NOT USE THIS FOR REAL APPS

  Revision 1.2  2002/09/21 23:11:16  pelle
  A bunch of clean ups. Got rid of as many hard coded URL's as I could.

  Revision 1.1.1.1  2002/09/18 10:55:55  pelle
  First release in new CVS structure.
  Also first public release.
  This implemnts simple named objects.
  - Identity Objects
  - NSAuth Objects

  Storage systems
  - In Memory Storage
  - Clear text file based storage
  - Encrypted File Storage (with SHA256 digested filenames)
  - CachedStorage
  - SoapStorage

  Simple SOAP client/server
  - Simple Single method call SOAP client, for arbitrary dom4j based requests
  - Simple Abstract SOAP Servlet for implementing http based SOAP Servers

  Simple XML-Signature Implementation
  - Based on dom4j
  - SHA-RSA only
  - Very simple (likely imperfect) highspeed canonicalizer
  - Zero support for X509 (We dont like that anyway)
  - Super Simple


  Revision 1.1.1.1  2002/05/29 10:02:22  pelle
  Lets try one more time. This is the first rev of the next gen of Neudist


  Revision 1.1.1.1  2002/03/20 00:46:52  pelleb
  no message



*/

package org.neuclear.commons.crypto;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.neuclear.commons.NeuClearException;


/**
 * @author Pelle Braendgaard
 */
public class CryptoToolsTest extends TestCase {
    public CryptoToolsTest() {
        super("CryptoToolsTest");
        setUp();
    }

    public CryptoToolsTest(String name) {
        super(name);
    }

    /**
     */
    protected void setUp() {
    }

    protected void tearDown() {
    }

    public static Test suite() {


        return new TestSuite(CryptoToolsTest.class);
    }

    public void testPadding() {
        byte src[] = new byte[20];
        for (int i = 0; i < src.length; i++)
            src[i] = (byte) ("a".getBytes()[0] + i);
        System.out.println("Source array='" + new String(src));
        byte padded[] = CryptoTools.pad(src, 14);
        System.out.println("Dest array='" + new String(padded));
        assertEquals(padded.length % 14, 0);
    }

    public void testSymmetricKeyEncryption() throws  CryptoException {
        String contentString = "<xml>Hello</xml>";
        byte password[] = "Three Brown Geese sledded down the hill".getBytes();
        byte contents[] = contentString.getBytes();
        byte encrypted[] = CryptoTools.encrypt(password, contents);
        String encryptString = new String(encrypted);
        byte decrypted[] = CryptoTools.decrypt(password, encrypted);
        byte depadded[] = new byte[contents.length];
        System.arraycopy(decrypted, 0, depadded, 0, depadded.length);
        String decryptString = new String(depadded);
        System.out.println("Original: " + contentString);
        System.out.println("Encrypted: " + encryptString);
        System.out.println("Decrypted: " + decryptString);

        assertEquals("Test Encryption/Decryption works", contentString, decryptString);
    }


}