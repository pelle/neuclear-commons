package org.neuclear.commons;

import java.util.HashMap;

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

$Id: ThreadMap.java,v 1.1 2003/12/12 19:27:38 pelle Exp $
$Log: ThreadMap.java,v $
Revision 1.1  2003/12/12 19:27:38  pelle
All the Cactus tests now for signing servlet.
Added working AuthenticationFilterTest
Returned original functionality to DemoSigningServlet.
This is set up to use the test keys stored in neuclear-commons.
SigningServlet should now work for general use. It uses the default
keystore. Will add configurability later. It also uses the GUIDialogAgent.

*/

/**
 * A thread map stores an object to be shared amongst various methods in the same thread.
 */
public class ThreadMap {
    public ThreadMap() {
        this.map = new HashMap();
    }

    /**
     * Places an object in the ThreadMap
     * 
     * @param obj 
     */
    public final void put(Object obj) {
        map.put(Thread.currentThread().toString(), obj);
    }

    /**
     * Gets the object in the ThreadMap. If none exists returns null
     * 
     * @return 
     */
    public final Object get() {
        return map.get(Thread.currentThread().toString());
    }

    /**
     * Clears the threads current entry
     */
    public final void clear() {
        map.remove(Thread.currentThread().toString());
    }

    public final boolean hasEntry() {
        return map.containsKey(Thread.currentThread().toString());
    }

    final private HashMap map;
}
