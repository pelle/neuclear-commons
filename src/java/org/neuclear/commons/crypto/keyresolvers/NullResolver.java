package org.neuclear.commons.crypto.keyresolvers;

import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jan 14, 2004
 * Time: 11:17:37 AM
 * To change this template use Options | File Templates.
 */
public class NullResolver implements KeyResolver {
    public PublicKey resolve(String name) {
        return null;  //To change body of implemented methods use Options | File Templates.
    }
}
