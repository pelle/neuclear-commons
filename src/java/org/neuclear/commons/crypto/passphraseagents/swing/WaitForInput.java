package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

/*
$Id: WaitForInput.java,v 1.2 2004/04/13 17:32:05 pelle Exp $
$Log: WaitForInput.java,v $
Revision 1.2  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

Revision 1.1  2004/04/12 15:00:29  pelle
Now have a slightly better way of handling the waiting for input using the WaitForInput class.
This will later be put into a command queue for execution.

*/

/**
 * User: pelleb
 * Date: Apr 10, 2004
 * Time: 3:13:10 PM
 */
public abstract class WaitForInput implements Runnable {

    public Object getResult() throws UserCancellationException {
        System.out.println(Thread.currentThread());

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                ;
            }
            if (cancelled)
                throw new UserCancellationException("User Cancelled");
            return result;
        }
    }

    protected void setResult(Object result) {
        this.result = result;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    protected void cancel() {
        cancelled = true;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    private Object result;
    private boolean cancelled = false;
    private final Object monitor = new Object();
}
