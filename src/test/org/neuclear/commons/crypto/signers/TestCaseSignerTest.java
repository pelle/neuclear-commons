package org.neuclear.commons.crypto.signers;

import junit.framework.TestCase;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;

import java.security.GeneralSecurityException;

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

$Id: TestCaseSignerTest.java,v 1.1 2003/11/12 23:47:50 pelle Exp $
$Log: TestCaseSignerTest.java,v $
Revision 1.1  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

*/

/**
 * User: pelleb
 * Date: Nov 12, 2003
 * Time: 2:48:23 PM
 */
public class TestCaseSignerTest extends TestCase {
    public TestCaseSignerTest(String string) throws GeneralSecurityException, NeuClearException {
        super(string);
        signer = new TestCaseSigner();
    }

    public void testHasKeys() throws CryptoException {
        assertTrue(signer.canSignFor("neu://test"));
        assertTrue(signer.canSignFor("neu://test/bux"));
        assertTrue(signer.canSignFor("neu://bob@test"));
        assertTrue(signer.canSignFor("neu://alice@test"));
    }

    public void testSignAndVerify() throws CryptoException {
        testKey("neu://test");
        testKey("neu://test/bux");
        testKey("neu://bob@test");
        testKey("neu://alice@test");
    }

    private void testKey(String name) throws CryptoException {
        byte sig[] = signer.sign(name, TESTDATA.getBytes());
        assertNotNull(sig);
        assertTrue(CryptoTools.verify(signer.getPublicKey(name), TESTDATA.getBytes(), sig));
    }


    private TestCaseSigner signer;
    private String TESTDATA = "Here we go again";

}
