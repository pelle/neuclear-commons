package org.neuclear.commons.crypto.signers;

import org.bouncycastle.crypto.Digest;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

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

$Id: KeyCache.java,v 1.3 2004/03/22 20:09:05 pelle Exp $
$Log: KeyCache.java,v $
Revision 1.3  2004/03/22 20:09:05  pelle
Added simple ledger for unit testing and in memory use

Revision 1.2  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.1  2003/11/18 00:01:02  pelle
The simple signing web application for logging in and out is now working.
There had been an issue in the canonicalizer when dealing with the embedded object of the SignatureRequest object.

*/

/**
 * Keeps a Cache of PrivateKeys. The key to finding the keys in the Cache
 * is a SHA1 Digest of the alias and the passphrase
 */
public final class KeyCache {
    public KeyCache(final KeyStore ks) {
        this.cache = new HashMap();
        this.ks = ks;
    }

    public final Key getKey(final String alias, final char[] passphrase) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, NonExistingSignerException {
        final String hash = createHash(alias, passphrase);
        if (cache.containsKey(hash))
            return (Key) cache.get(hash);
        if (ks.containsAlias(alias)) {
            final Key key = ks.getKey(alias, passphrase);
            cache.put(hash, key);
            return key;
        }
        return null;
    }

    public final static String createHash(final String alias, final char[] passphrase) {
        final Digest dig = new org.bouncycastle.crypto.digests.SHA1Digest();
        final byte[] a = alias.getBytes();
        dig.update(a, 0, a.length);
        final byte[] p = new String(passphrase).getBytes();
        dig.update(p, 0, p.length);
        final byte[] hash = new byte[dig.getDigestSize()];
        dig.doFinal(hash, 0);
        return new String(hash);


    }

    private final KeyStore ks;
    private final Map cache;
}
