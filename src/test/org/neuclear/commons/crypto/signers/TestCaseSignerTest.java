package org.neuclear.commons.crypto.signers;

import junit.framework.TestCase;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: TestCaseSignerTest.java,v 1.7 2004/02/19 15:29:12 pelle Exp $
$Log: TestCaseSignerTest.java,v $
Revision 1.7  2004/02/19 15:29:12  pelle
Various cleanups and corrections

Revision 1.6  2003/12/19 18:02:53  pelle
Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
- For most cases the main exception to worry about now is InvalidNamedObjectException.
- Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
  runtime exception.
- Source and Store patterns each now have their own exceptions that generalizes the various physical
  exceptions that can happen in that area.

Revision 1.5  2003/12/18 17:40:08  pelle
You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
IdentityCreator has been modified to allow creation of keys.
Note The actual Creation of Certificates still have a problem that will be resolved later today.

Revision 1.4  2003/12/10 23:55:45  pelle
Did some cleaning up in the builders
Fixed some stuff in IdentityCreator
New maven goal to create executable jarapp
We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
Will release shortly.

Revision 1.3  2003/11/21 04:43:42  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.2  2003/11/19 23:32:51  pelle
Signers now can generatekeys via the generateKey() method.
Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
SignedNamedObject now contains the full xml which is returned with getEncoded()
This means that it is now possible to further receive on or process a SignedNamedObject, leaving
NamedObjectBuilder for its original purposes of purely generating new Contracts.
NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
Updated all major interfaces that used the old model to use the new model.

Revision 1.1  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

*/

/**
 * User: pelleb
 * Date: Nov 12, 2003
 * Time: 2:48:23 PM
 */
public final class TestCaseSignerTest extends TestCase {
    public TestCaseSignerTest(final String string) throws GeneralSecurityException, NeuClearException {
        super(string);
        signer = new TestCaseSigner();
    }

    public final void testHasKeys() throws CryptoException {
        assertTrue(signer.canSignFor("neu://test"));
        assertTrue(signer.canSignFor("neu://test/bux"));
        assertTrue(signer.canSignFor("neu://bob@test"));
        assertTrue(signer.canSignFor("neu://alice@test"));
    }

    public final void testSignAndVerify() throws CryptoException, UserCancellationException {
        testKey("neu://test");
        testKey("neu://test/bux");
        testKey("neu://bob@test");
        testKey("neu://alice@test");
    }

    public final void testGenerateKey() throws CryptoException, UserCancellationException {
        final PublicKey pub = signer.generateKey(ALIASEVE);
        final byte[] data = "this is a test".getBytes();
        final byte[] sig = signer.sign(ALIASEVE, data);
        assertNotNull(sig);
        assertTrue(CryptoTools.verify(pub, data, sig));
        assertTrue(signer.canSignFor(ALIASEVE));
    }

    private void testKey(final String name) throws CryptoException, UserCancellationException {
        final byte[] sig = signer.sign(name, TESTDATA.getBytes());
        assertNotNull(sig);
        assertTrue(CryptoTools.verify(signer.getPublicKey(name), TESTDATA.getBytes(), sig));
    }


    private final TestCaseSigner signer;
    private static final String TESTDATA = "Here we go again";
    private static final String ALIASEVE = "neu://eve@test";

}
