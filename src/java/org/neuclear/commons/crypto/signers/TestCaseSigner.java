package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.passphraseagents.AlwaysTheSamePassphraseAgent;

import java.io.InputStream;
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

$Id: TestCaseSigner.java,v 1.1 2003/11/12 23:47:50 pelle Exp $
$Log: TestCaseSigner.java,v $
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
public class TestCaseSigner extends JCESigner {

    /**
     * Creates a TestCaseSigner with the keystore in:<br>
     * <tt>src/testdata/keys/testkeys.jks</tt><br>
     * The password for all keys should be "neuclear"
     * 
     * @throws NeuClearException        
     * @throws GeneralSecurityException 
     */
    public TestCaseSigner() throws NeuClearException, GeneralSecurityException {
        this(KEYSTORE, TestCaseSigner.class.getClassLoader().getResourceAsStream(KEYSTORE), "neuclear");
    }

    /**
     * Creates a TestCaseSigner in the given location. The keystore must
     * be a SUN JKS format file and the passphrase for the keystore and all
     * keys must be the same.
     * 
     * @param in         InputStream
     * @param passphrase The passphrase to use
     * @throws NeuClearException        
     * @throws GeneralSecurityException 
     */
    public TestCaseSigner(String name, InputStream in, String passphrase) throws NeuClearException, GeneralSecurityException {
        super(name,
                in,
                "jks", "SUN",
                new AlwaysTheSamePassphraseAgent(passphrase)
        );
    }

    private static final String KEYSTORE = "org/neuclear/commons/crypto/signers/testkeys.jks";
}
