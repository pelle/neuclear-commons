package org.neuclear.commons.crypto.keyresolvers;

import org.neuclear.commons.Cache;
import org.neuclear.commons.crypto.CryptoTools;

import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jan 14, 2004
 * Time: 11:17:37 AM
 * To change this template use Options | File Templates.
 */
public class PublicKeyCache extends Cache implements KeyResolver {


    public final PublicKey resolve(String name) {
        return (PublicKey) lookup(name);
    }

    public final PublicKey cache(final PublicKey pub) {
        final String keyid = CryptoTools.encodeBase32(CryptoTools.digest(pub.getEncoded()));
        return (PublicKey) cache(keyid, pub);
    }

    public synchronized static PublicKeyCache getInstance() {
        if (instance == null)
            instance = new PublicKeyCache();
        return instance;
    }

    public static void cachePublicKey(final PublicKey pub) {
        getInstance().cache(pub);
    }

    public static PublicKey resolvePublicKey(final String id) {
        return getInstance().resolve(id);
    }

    private static PublicKeyCache instance;
}
