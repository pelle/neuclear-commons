package org.neuclear.commons.crypto.signers;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.AlwaysTheSamePassphraseAgent;
import org.neuclear.commons.NeuClearException;

import java.io.FileNotFoundException;
import java.io.File;
import java.security.GeneralSecurityException;


/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 18, 2003
 * Time: 11:55:07 AM
 * To change this template use Options | File Templates.
 */
public class JCESignerTest extends TestCase{
    static final String FILENAME = "target/testdata/keystores/jcesignertest.jks";
    static final String BOB="neu://bob@test";
    static final String ALICE="neu://alice@test";

    public JCESignerTest(String string) {
        super(string);
        CryptoTools.ensureProvider();
    }
    public void testCreateBlank() throws FileNotFoundException, GeneralSecurityException, NeuClearException {
        File file=new File(FILENAME);
        if (file.exists())
            file.delete();// We want to clear out any existing stores
        assertFalse(file.exists());
        JCESigner signer=new JCESigner(FILENAME,"jks","SUN",new AlwaysTheSamePassphraseAgent("neuclear"));
        assertFalse(signer.canSignFor(BOB));
        assertFalse(signer.canSignFor(ALICE));
        signer.generateKey(BOB);
        signer.generateKey(ALICE);
        assertTrue(signer.canSignFor(BOB));
        assertTrue(signer.canSignFor(ALICE));
        signer.save();
        assertTrue(file.exists());
        JCESigner signer2=new JCESigner(FILENAME,"jks","SUN",new AlwaysTheSamePassphraseAgent("neuclear"));
        assertTrue(signer2.canSignFor(BOB));
        assertTrue(signer2.canSignFor(ALICE));
    }


}
