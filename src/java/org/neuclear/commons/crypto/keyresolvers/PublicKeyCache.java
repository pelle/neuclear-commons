package org.neuclear.commons.crypto.keyresolvers;

import org.neuclear.commons.crypto.CryptoTools;

import java.lang.ref.WeakReference;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jan 14, 2004
 * Time: 11:17:37 AM
 * To change this template use Options | File Templates.
 */
public class PublicKeyCache implements KeyResolver {


    private PublicKeyCache() {
        this.cache = new HashMap();
    }

    public final PublicKey resolve(String name) {
        final WeakReference ref = (WeakReference) cache.get(name);
        if (ref == null)
            return null;
        return (PublicKey) ref.get();
    }

    public final void cache(final PublicKey pub) {
        final String keyid = CryptoTools.encodeBase32(CryptoTools.digest(pub.getEncoded()));
        if (!cache.containsKey(keyid) || ((WeakReference) cache.get(keyid)).isEnqueued()) {
            cache.put(keyid, new WeakReference(pub));
        }
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

    private final Map cache;
    private static PublicKeyCache instance;
}
