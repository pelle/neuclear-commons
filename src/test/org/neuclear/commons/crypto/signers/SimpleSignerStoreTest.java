/* $Id: SimpleSignerStoreTest.java,v 1.1 2003/11/12 18:54:42 pelle Exp $
 * $Log: SimpleSignerStoreTest.java,v $
 * Revision 1.1  2003/11/12 18:54:42  pelle
 * Updated SimpleSignerStoreTest to use a StoredPassPhraseAgent eliminating the popup during testing.
 * Created SigningBenchmark for running comparative performance benchmarks on various key algorithms.
 *
 * Revision 1.6  2003/11/11 21:18:46  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.5  2003/10/29 21:16:28  pelle
 * Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
 * To use it you pass a byte array and an alias. The sign method then returns the signature.
 * If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
 * This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
 * as SmartCards for end user applications.
 *
 * Revision 1.4  2003/10/28 23:56:04  pelle
 * Fixed the SimpleSigner unit test to verify the next functionality of the Signer interface.
 *
 * Revision 1.3  2003/10/21 22:31:15  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
 *
 * Revision 1.2  2003/09/22 19:24:03  pelle
 * More fixes throughout to problems caused by renaming.
 *
 * Revision 1.1.1.1  2003/09/19 14:42:03  pelle
 * First import into the neuclear project. This was originally under the SF neuclear
 * project. This marks a general major refactoring and renaming ahead.
 *
 * The new name for this code is NeuClear Identity and has the general package header of
 * org.neuclear.id
 * There are other areas within the current code which will be split out into other subprojects later on.
 * In particularly the signers will be completely seperated out as well as the contract types.
 *
 *
 * Revision 1.4  2003/02/18 00:06:15  pelle
 * Moved the Signer's into xml-sig
 *
 * Revision 1.3  2003/02/10 22:30:24  pelle
 * Got rid of even further dependencies. In Particular OSCore
 *
 * Revision 1.2  2003/02/09 00:15:56  pelle
 * Fixed things so they now compile with r_0.7 of XMLSig
 *
 * Revision 1.1  2003/01/16 19:16:09  pelle
 * Major Structural Changes.
 * We've split the test classes out of the normal source tree, to enable Maven's test support to work.
 * WARNING
 * for Development purposes the code does not as of this commit until otherwise notified actually verifysigs.
 * We are reworking the XMLSig library and need to continue work elsewhere for the time being.
 * DO NOT USE THIS FOR REAL APPS
 *
 * Revision 1.2  2002/10/06 00:39:26  pelle
 * I have now expanded support for different types of Signers.
 * There is now a JCESigner which uses a JCE KeyStore for signing.
 * I have refactored the SigningServlet a bit, eliminating most of the demo code.
 * This has been moved into DemoSigningServlet.
 * I have expanded the CommandLineSigner, so it now also has an option for specifying a default signing service.
 * The default web application now contains two signers.
 * - The Demo one is still at /Signer
 * - There is a new one at /personal/Signer this uses the testkeys.ks for
 * signing anything under neu://test
 * Note neu://test now has a default interactive signer running on localhost.
 * So to play with this you must install the webapp on your own local machine.
 *
 * Revision 1.1  2002/09/23 15:09:11  pelle
 * Got the SimpleSigner working properly.
 * I couldn't get SealedObjects working with BouncyCastle's Symmetric keys.
 * Don't know what I was doing, so I reimplemented it. Encrypting
 * and decrypting it my self.
 *
 */
package org.neuclear.commons.crypto.signers;

import junit.framework.TestCase;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.configuration.ConfigurationException;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.StoredPassphraseAgent;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


/**
 * @author pelleb
 * @version $Revision: 1.1 $
 */
public class SimpleSignerStoreTest extends TestCase {
    public SimpleSignerStoreTest(String name) throws GeneralSecurityException, NeuClearException, ConfigurationException {
        super(name);
        store = getSignerStoreInstance();
        generateKeys();
    }

    /**
     */
    public static SimpleSigner getSignerStoreInstance() throws NeuClearException, GeneralSecurityException, ConfigurationException {

        return new SimpleSigner("src/testdata/keys",
                new StoredPassphraseAgent("bob", "bob")
        );
    }


    protected static synchronized void generateKeys() throws GeneralSecurityException {
        if (kg == null) {
            System.out.println("Generating Test Keys");
            kg = KeyPairGenerator.getInstance("RSA");

            kg.initialize(1048, new SecureRandom("Bear it all with NeuDist".getBytes()));
            //       kp=kg.generateKeyPair();
            root = kg.generateKeyPair();
            bob = kg.generateKeyPair();
        }
    }


    public void testAddKey() throws NeuClearException, GeneralSecurityException, IOException {
        boolean success = false;
        try {
            store.addKey("root", "root".toCharArray(), root.getPrivate());
            success = true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue("Managed to add a key", success);
    }

    public void testSignData() throws NeuClearException, GeneralSecurityException, IOException, CryptoException {
        boolean success = false;
        byte data[] = null;
        try {
            store.addKey("bob", bob.getPrivate());
            data = store.sign("bob", "test".getBytes());
            assertTrue(CryptoTools.verify(bob.getPublic(), "test".getBytes(), data));
            success = true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue("Managed to add and sign some data", success);
        assertNotNull("Key wasn't null", data);
        //assertEquals("Gotten Key was the same as Stored Key", bob.getPrivate(), key);
    }


    private SimpleSigner store;
    private static KeyPairGenerator kg;
    protected static KeyPair root;
    protected static KeyPair bob;

}
