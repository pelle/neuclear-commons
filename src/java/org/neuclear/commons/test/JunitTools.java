package org.neuclear.commons.test;

import org.neuclear.commons.Utility;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.CryptoException;

import java.security.*;
import java.security.cert.CertificateException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Feb 21, 2003
 * Time: 5:04:29 PM
 * $Id: JunitTools.java,v 1.1 2003/11/11 21:17:48 pelle Exp $
 * $Log: JunitTools.java,v $
 * Revision 1.1  2003/11/11 21:17:48  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.1  2003/02/21 22:48:18  pelle
 * New Test Infrastructure
 * Added test keys in src/testdata/keys
 * Modified tools to handle these keys
 *
 */
public final class JunitTools {
    private static void loadKeys() throws CryptoException {
        try {
            // TODO I think the keys are corrupt
            KeyStore ks=KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = "neuclear".toCharArray();
            ks.load(new FileInputStream(new File("src/testdata/keys/testkeys.jks")),password);
            rsakey=CryptoTools.getKeyPair(ks,"rsakey",password);
            dsakey=CryptoTools.getKeyPair(ks,"dsakey",password);

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
        if (rsakey==null)
            loadKeys();
        return rsakey;
    }

    public static KeyPair getTestDSAKey() throws CryptoException {
        if (dsakey==null)
            loadKeys();
        return dsakey;
    }

    private static KeyPair rsakey;
    private static KeyPair dsakey;

}
