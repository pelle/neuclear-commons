package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.crypto.passphraseagents.AlwaysTheSamePassphraseAgent;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;

import java.io.InputStream;
import java.net.URL;

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

$Id: TestCaseSigner.java,v 1.11 2004/03/22 20:09:05 pelle Exp $
$Log: TestCaseSigner.java,v $
Revision 1.11  2004/03/22 20:09:05  pelle
Added simple ledger for unit testing and in memory use

Revision 1.10  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.9  2003/12/19 00:31:16  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.8  2003/12/14 20:52:54  pelle
Added ServletPassPhraseAgent which uses ThreadLocal to transfer the passphrase to the signer.
Added ServletSignerFactory, which builds Signers for use within servlets based on parameters in the Servlets
Init parameters in web.xml
Updated SQLContext to use ThreadLocal
Added jakarta cactus unit tests to neuclear-commons to test the 2 new features above.
Added use of the new features in neuclear-commons to the servilets within neuclear-id and added
configuration parameters in web.xml

Revision 1.7  2003/11/22 00:22:52  pelle
All unit tests in commons, id and xmlsec now work.
AssetController now successfully processes payments in the unit test.
Payment Web App has working form that creates a TransferRequest presents it to the signer
and forwards it to AssetControlServlet. (Which throws an XML Parser Exception) I think the XMLReaderServlet is bust.

Revision 1.6  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.5  2003/11/18 23:34:55  pelle
Payment Web Application is getting there.

Revision 1.4  2003/11/18 15:07:18  pelle
Changes to JCE Implementation
Working on getting all tests working including store tests

Revision 1.3  2003/11/18 00:01:02  pelle
The simple signing web application for logging in and out is now working.
There had been an issue in the canonicalizer when dealing with the embedded object of the SignatureRequest object.

Revision 1.2  2003/11/13 23:26:17  pelle
The signing service and web authentication application is now almost working.

Revision 1.1  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

*/

/**
 * A signer for use in test cases. This id exclusively for use in test applications
 * and we cant say strongly enough to NEVER use this in any production code.
 * To make life simple the only rule for the keystores are that they
 * are in standard jks format and all the passphrases including the
 * keystore passphrase are the same.
 */
public final class TestCaseSigner extends JCESigner {

    /**
     * Creates a TestCaseSigner with the keystore in:<br>
     * <tt>src/testdata/keys/testkeys.jks</tt><br>
     * The password for all keys should be "neuclear"
     */
    public TestCaseSigner() throws InvalidPassphraseException {
        this(KEYSTORE, getKeyStore(), "neuclear");
    }

    public TestCaseSigner(final PassPhraseAgent agent) throws InvalidPassphraseException {
        this(KEYSTORE, getKeyStore(), agent);
    }

    /**
     * Creates a TestCaseSigner in the given location. The keystore must
     * be a SUN JKS format file and the passphrase for the keystore and all
     * keys must be the same.
     *
     * @param in         InputStream
     * @param passphrase The passphrase to use
     * @throws InvalidPassphraseException
     */
    public TestCaseSigner(final String name, final InputStream in, final String passphrase) throws InvalidPassphraseException {
        this(name, in, new AlwaysTheSamePassphraseAgent(passphrase));
    }

    public TestCaseSigner(final String name, final InputStream in, final PassPhraseAgent agent) throws InvalidPassphraseException {
        super(name,
                in,
                "jks", "SUN",
                agent,
                "neuclear".toCharArray());
    }

    private static InputStream getKeyStore() {
        final URL url = TestCaseSigner.class.getClassLoader().getResource(KEYSTORE);
//        System.out.println("loading keystore from: " + url.toString());
        return TestCaseSigner.class.getClassLoader().getResourceAsStream(KEYSTORE);


    }

    private static final String KEYSTORE = "org/neuclear/commons/crypto/signers/testkeys.jks";
}
