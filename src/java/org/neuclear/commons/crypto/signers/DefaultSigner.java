package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import java.security.KeyStoreException;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.prefs.Preferences;

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

$Id: DefaultSigner.java,v 1.7 2004/04/12 23:50:07 pelle Exp $
$Log: DefaultSigner.java,v $
Revision 1.7  2004/04/12 23:50:07  pelle
implemented the queue and improved the DefaultSigner

Revision 1.6  2004/04/12 15:37:01  pelle
Refactored DefaultSigner to delegate to a JCESigner and not inherit.

Revision 1.5  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.4  2003/12/19 00:31:15  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.3  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.2  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

Revision 1.1  2003/11/11 21:17:46  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.2  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

Revision 1.1  2003/10/29 21:16:28  pelle
Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
To use it you pass a byte array and an alias. The sign method then returns the signature.
If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
as SmartCards for end user applications.

*/

/**
 * Easy to use preconfigured Signer that uses the standard default JCE KeyStore
 * User: pelleb
 * Date: Oct 29, 2003
 * Time: 3:22:17 PM
 */
public final class DefaultSigner implements BrowsableSigner {
    public DefaultSigner(final PassPhraseAgent agent) throws UserCancellationException, InvalidPassphraseException {
        Preferences prefs = Preferences.userNodeForPackage(DefaultSigner.class);
        final String filename = prefs.get("KEYSTORE", CryptoTools.DEFAULT_KEYSTORE);
        signer = new JCESigner(filename, "jks", "SUN", agent);

    }

    public final byte[] sign(final String name, final byte[] data) throws NonExistingSignerException, UserCancellationException {
        return signer.sign(name, data);
    }

    public final byte[] sign(final String name, final byte[] data, boolean incorrect) throws UserCancellationException, NonExistingSignerException {
        return signer.sign(name, data, incorrect);
    }

    public final boolean canSignFor(final String name) {
        return signer.canSignFor(name);
    }

    public final int getKeyType(final String name) {
        return signer.getKeyType(name);
    }

    public final PublicKey generateKey(final String alias) throws UserCancellationException {
        return signer.generateKey(alias);
    }

    public final PublicKey getPublicKey(final String name) throws NonExistingSignerException {
        return signer.getPublicKey(name);
    }

    public byte[] sign(byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        return signer.sign(data, callback);
    }

    public byte[] sign(String name, char pass[], byte data[], SetPublicKeyCallBack callback) throws InvalidPassphraseException {
        return signer.sign(name, pass, data, callback);
    }

    public void createKeyPair(String alias, char passphrase[]) throws CryptoException {
        signer.createKeyPair(alias, passphrase);
    }

    public void save() {
        signer.save();
    }

    public Iterator iterator() throws KeyStoreException {
        return signer.iterator();
    }

    private final JCESigner signer;

}
