package org.neuclear.commons.test;

import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Feb 21, 2003
 * Time: 5:04:29 PM
 * $Id: JunitTools.java,v 1.3 2003/11/21 04:43:42 pelle Exp $
 * $Log: JunitTools.java,v $
 * Revision 1.3  2003/11/21 04:43:42  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.2  2003/11/20 23:41:36  pelle
 * Getting all the tests to work in id
 * Removing usage of BC in CryptoTools as it was causing issues.
 * First version of EntityLedger that will use OFB's EntityEngine. This will allow us to support a vast amount databases without
 * writing SQL. (Yipee)
 *
 * Revision 1.1  2003/11/11 21:17:48  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 * <p/>
 * Revision 1.1  2003/02/21 22:48:18  pelle
 * New Test Infrastructure
 * Added test keys in src/testdata/keys
 * Modified tools to handle these keys
 */
public final class JunitTools {
    private static void loadKeys() throws CryptoException {
        try {

            final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            final char[] password = "neuclear".toCharArray();
            ks.load(new FileInputStream(new File("src/testdata/keys/testkeys.jks")), password);
            rsakey = CryptoTools.getKeyPair(ks, "rsakey", password);
            dsakey = CryptoTools.getKeyPair(ks, "dsakey", password);

        } catch (KeyStoreException e) {
            CryptoTools.rethrowException(e);
        } catch (IOException e) {
            CryptoTools.rethrowException(e);
        } catch (NoSuchAlgorithmException e) {
            CryptoTools.rethrowException(e);
        } catch (CertificateException e) {
            CryptoTools.rethrowException(e);
        }

    }


    public static KeyPair getTestRSAKey() throws CryptoException {
        if (rsakey == null)
            loadKeys();
        return rsakey;
    }

    public static KeyPair getTestDSAKey() throws CryptoException {
        if (dsakey == null)
            loadKeys();
        return dsakey;
    }

    private static KeyPair rsakey;
    private static KeyPair dsakey;

}
