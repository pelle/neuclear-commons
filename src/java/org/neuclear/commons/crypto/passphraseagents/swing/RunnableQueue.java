package org.neuclear.commons.crypto.passphraseagents.swing;

import java.util.LinkedList;

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

$Id: RunnableQueue.java,v 1.1 2004/04/12 23:50:07 pelle Exp $
$Log: RunnableQueue.java,v $
Revision 1.1  2004/04/12 23:50:07  pelle
implemented the queue and improved the DefaultSigner

*/

/**
 * User: pelleb
 * Date: Apr 12, 2004
 * Time: 10:16:55 PM
 */
public class RunnableQueue implements Runnable {
    public RunnableQueue() {
        queue = new LinkedList();
    }

    public void run() {

        System.out.println("Starting Crypto Agent Event Queue");
        while (true) {
            read().run();

        }
    }

    private Runnable read() {
        synchronized (monitor) {
            if (queue.size() > 0)
                return (Runnable) queue.removeFirst();

            try {
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return read();
        }
    }

    public void queue(Runnable run) {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        synchronized (monitor) {
            queue.add(run);
            monitor.notifyAll();
        }
    }

    private final LinkedList queue;
    private Thread thread;
    private final Object monitor = new Object();
}
