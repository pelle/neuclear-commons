package org.neuclear.commons;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
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

$Id: Cache.java,v 1.1 2004/04/02 23:04:44 pelle Exp $
$Log: Cache.java,v $
Revision 1.1  2004/04/02 23:04:44  pelle
Got TransferOrder and Builder working with their test cases.
Working on TransferReceipt which is the first embedded receipt. This is causing some problems at the moment.

*/

/**
 * User: pelleb
 * Date: Apr 2, 2004
 * Time: 10:07:43 PM
 */
public abstract class Cache {
    protected Cache() {
        cache = Collections.synchronizedMap(new HashMap());
    }

    protected final Object lookup(String name) {
        final Reference ref = (Reference) cache.get(name);
        if (ref == null)
            return null;
        if (ref.isEnqueued()) {
            cache.remove(name);
            return null;
        }

        return ref.get();
    }

    protected Object cache(final String id, final Object obj) {
        if (!cache.containsKey(id) || ((Reference) cache.get(id)).isEnqueued()) {
            cache.put(id, new SoftReference(obj));
        }
        return obj;
    }

    private final Map cache;
}
