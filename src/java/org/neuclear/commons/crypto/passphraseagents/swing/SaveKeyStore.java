package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.LowLevelException;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.PersonalSigner;

import java.io.IOException;

/*
$Id: SaveKeyStore.java,v 1.3 2004/05/14 23:47:01 pelle Exp $
$Log: SaveKeyStore.java,v $
Revision 1.3  2004/05/14 23:47:01  pelle
Moved PersonalSigner and OpenSignerDialog to neuclear-commons where they belong.
The whole mechanism of opening keystores is pretty smooth right now.
Currently working on saving, which doesnt quite work yet. I have added a save method to OpenSignerDialog, which
should handle it.

Revision 1.2  2004/04/14 00:10:52  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

Revision 1.1  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

*/

/**
 * User: pelleb
 * Date: Apr 13, 2004
 * Time: 11:55:54 AM
 */
public class SaveKeyStore implements Runnable {
    public SaveKeyStore(BrowsableSigner signer, MessageLabel message, boolean force) {
        this.signer = signer;
        this.message = message;
        this.force = force;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        try {
            if (signer instanceof PersonalSigner)
                ((PersonalSigner) signer).save(false);
            else
                signer.save();
            message.info("KeyStore saved");
        } catch (UserCancellationException e) {
            message.info("Save Cancelled");
        } catch (IOException e) {
            message.error("Problem Saving KeyStore: " + e.getLocalizedMessage());
        } catch (LowLevelException e) {
            message.error("Problem Saving KeyStore: " + e.getCause().getLocalizedMessage());
        } catch (Exception e) {
            message.error("Problem Saving KeyStore: " + e.getLocalizedMessage());
        }

    }

    public static void save(BrowsableSigner signer, MessageLabel message) {
        new Thread(new SaveKeyStore(signer, message, false)).start();
    }

    public static void saveAs(BrowsableSigner signer, MessageLabel message) {
        new Thread(new SaveKeyStore(signer, message, true)).start();
    }

    private final BrowsableSigner signer;
    private final MessageLabel message;
    private final boolean force;
}
