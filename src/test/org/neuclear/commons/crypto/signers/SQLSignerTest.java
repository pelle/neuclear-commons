package org.neuclear.commons.crypto.signers;

import junit.framework.TestCase;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;


/**
 * Created by Eclipse.
 * User: deneb
 * Date: Dec 18, 2003
 * Time: 11:55:07 AM
 * To change this template use Options | File Templates.
 */
public class SQLSignerTest extends TestCase {
    static final String BOB = "neu://bob@test";
    static final String ALICE = "neu://alice@test";

    public SQLSignerTest(String string) {
        super(string);
        CryptoTools.ensureProvider();
    }

    public void testCreateBlank() throws FileNotFoundException, GeneralSecurityException, NeuClearException {
        SQLSigner signer = new SQLSigner("neuclear");
        assertFalse(signer.canSignFor(BOB));
        assertFalse(signer.canSignFor(ALICE));
        signer.generateKey(BOB);
        signer.generateKey(ALICE);
        assertTrue(signer.canSignFor(BOB));
        assertTrue(signer.canSignFor(ALICE));
        signer.save();
        SQLSigner signer2 = new SQLSigner("neuclear");
        assertTrue(signer2.canSignFor(BOB));
        assertTrue(signer2.canSignFor(ALICE));
    }


}
