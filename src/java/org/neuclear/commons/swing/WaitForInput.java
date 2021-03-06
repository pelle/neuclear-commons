package org.neuclear.commons.swing;

import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

/*
$Id: WaitForInput.java,v 1.1 2004/05/18 19:19:04 pelle Exp $
$Log: WaitForInput.java,v $
Revision 1.1  2004/05/18 19:19:04  pelle
Added Swing package to commons.
NeuClearDialog is a standard Abstract Dialog Class for modal dialogs.
ProcessDialog is a standard Abstract Dialog Class for modal dialogs with a long running processing task.
Fixed serialization issues in Signer. It now loads and saves the IdentityListModel correctly.
AddIdentityDialog is a subclass of the above mentioned ProcessDialog.
Missing are:
- better error messages
- Populate and use categories combo

Revision 1.6  2004/05/14 23:47:01  pelle
Moved PersonalSigner and OpenSignerDialog to neuclear-commons where they belong.
The whole mechanism of opening keystores is pretty smooth right now.
Currently working on saving, which doesnt quite work yet. I have added a save method to OpenSignerDialog, which
should handle it.

Revision 1.5  2004/05/14 19:11:27  pelle
Added OpenSignerDialog, which has been integrated with PersonalSigner.

Revision 1.4  2004/04/15 20:03:52  pelle
Added license screen to Personal Signer.
Added Sign document menu to  Personal Signer.

Revision 1.3  2004/04/14 00:10:52  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

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
//        System.out.println(Thread.currentThread());

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

    public void cancel() {
        cancelled = true;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    public abstract void execute();

    private Object result;
    private boolean cancelled = false;
    private final Object monitor = new Object();
}
