package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;

/*
$Id: SaveKeyStore.java,v 1.1 2004/04/13 17:32:05 pelle Exp $
$Log: SaveKeyStore.java,v $
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
    public SaveKeyStore(BrowsableSigner signer) {
        this.signer = signer;
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
            signer.save();
        } catch (UserCancellationException e) {
            e.printStackTrace();
        }

    }

    public static void save(BrowsableSigner signer) {

    }

    private final BrowsableSigner signer;

}
