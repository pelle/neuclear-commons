package org.neuclear.commons;

import junit.framework.TestCase;

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

$Id: ThreadMapTest.java,v 1.1 2003/12/12 19:27:38 pelle Exp $
$Log: ThreadMapTest.java,v $
Revision 1.1  2003/12/12 19:27:38  pelle
All the Cactus tests now for signing servlet.
Added working AuthenticationFilterTest
Returned original functionality to DemoSigningServlet.
This is set up to use the test keys stored in neuclear-commons.
SigningServlet should now work for general use. It uses the default
keystore. Will add configurability later. It also uses the GUIDialogAgent.

*/

/**
 * User: pelleb
 * Date: Dec 12, 2003
 * Time: 11:56:32 AM
 */
public class ThreadMapTest extends TestCase {
    public ThreadMapTest(String string) {
        super(string);
        map = new ThreadMap();
    }

    public void setup() {
        map = new ThreadMap();
    }

    public void testWithinThread() {
        assertFalse(map.hasEntry());
        map.put("test");
        assertTrue(map.hasEntry());
        assertEquals("test", map.get());
        otherMethod();
        yetAnotherMethod();
        killerMethod();
        assertFalse(map.hasEntry());

    }

    public void otherMethod() {
        assertTrue(map.hasEntry());
        assertEquals("test", map.get());
        yetAnotherMethod();
    }

    public void yetAnotherMethod() {
        assertTrue(map.hasEntry());
        assertEquals("test", map.get());
    }

    public void killerMethod() {
        map.clear();
        assertFalse(map.hasEntry());
    }

    public void testWithinOtherThread() throws InterruptedException {
        assertFalse(map.hasEntry());
        map.put("test");
        assertTrue(map.hasEntry());
        assertEquals("test", map.get());
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                assertFalse(map.hasEntry());
            }
        });
        t2.start();
        assertTrue(map.hasEntry());

    }

    private ThreadMap map;
}
