package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import java.security.PublicKey;

/*
 *  The NeuClear Project and it's libraries are
 *  (c) 2002-2004 Antilles Software Ventures SA
 *  For more information see: http://neuclear.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * A Signer which doesn't actually do anything. It is just intended as a placeholder when an App doesn't need to sign.
 */
public class DummySigner implements Signer {
    public byte[] sign(String name, byte data[]) throws UserCancellationException, NonExistingSignerException {
        throw new NonExistingSignerException(name);
    }

    public boolean canSignFor(String name) {
        return false;
    }

    public int getKeyType(String name) {
        return 0;
    }

    public PublicKey generateKey(String alias) throws UserCancellationException {
        throw new UserCancellationException(alias);
    }

    public PublicKey generateKey() throws UserCancellationException {
        throw new UserCancellationException("");
    }

    public void save() throws UserCancellationException {
        ;
    }
}
