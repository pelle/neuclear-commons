package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import java.security.KeyStoreException;
import java.security.PublicKey;
import java.util.Iterator;

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

$Id: BrowsableSigner.java,v 1.2 2004/04/07 17:22:10 pelle Exp $
$Log: BrowsableSigner.java,v $
Revision 1.2  2004/04/07 17:22:10  pelle
Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
The XMLSig classes have also been updated to support this.

Revision 1.1  2004/03/29 23:48:33  pelle
InteractiveAgent now has a new method which allows signers to ask for a passphrase without specifying alias.
The agents are passed a reference to the Signer, which they can use to browse aliases as well as create new key pairs.

The intention is to encapsulate most of the key management functionality within the InteractiveAgent.

*/

/**
 * Signer Stores with interactive user interfaces can implement this to
 * provide an iterator of the keys held within.
 */
public interface BrowsableSigner {
    Iterator iterator() throws KeyStoreException;

    PublicKey getPublicKey(String name) throws NonExistingSignerException;

    byte[] sign(byte data[], SetPublicKeyCallBack callback) throws UserCancellationException;

    byte[] sign(String alias, char passphrase[], byte data[], SetPublicKeyCallBack callback) throws InvalidPassphraseException;

}
