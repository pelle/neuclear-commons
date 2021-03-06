package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.crypto.Base64;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.DefaultSigner;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;
import org.neuclear.commons.swing.SwingTools;
import org.neuclear.commons.swing.WaitForInput;

import javax.swing.*;
import java.io.File;
import java.security.PublicKey;

/*
$Id: SwingAgent.java,v 1.16 2004/05/18 19:19:03 pelle Exp $
$Log: SwingAgent.java,v $
Revision 1.16  2004/05/18 19:19:03  pelle
Added Swing package to commons.
NeuClearDialog is a standard Abstract Dialog Class for modal dialogs.
ProcessDialog is a standard Abstract Dialog Class for modal dialogs with a long running processing task.
Fixed serialization issues in Signer. It now loads and saves the IdentityListModel correctly.
AddIdentityDialog is a subclass of the above mentioned ProcessDialog.
Missing are:
- better error messages
- Populate and use categories combo

Revision 1.15  2004/05/16 00:04:00  pelle
Added SigningServer which encapsulates all the web serving functionality.
Added IdentityPanel which contains an IdentityTree of Identities.
Added AssetPanel
Save now works and Add Personality as well.

Revision 1.14  2004/05/14 19:11:27  pelle
Added OpenSignerDialog, which has been integrated with PersonalSigner.

Revision 1.13  2004/05/11 15:38:04  pelle
Removed a few compilation errors

Revision 1.12  2004/05/06 21:40:30  pelle
More swing refactorings

Revision 1.11  2004/04/22 12:35:29  pelle
Added Icons and improved localisation

Revision 1.10  2004/04/21 23:10:13  pelle
Fixed mac look and feel

Revision 1.9  2004/04/15 20:03:52  pelle
Added license screen to Personal Signer.
Added Sign document menu to  Personal Signer.

Revision 1.8  2004/04/15 15:34:41  pelle
Got rid of the looping InvalidPassphraseException in DefaultSigner.
Added initial focus for all dialogs.

Revision 1.7  2004/04/14 00:10:52  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

Revision 1.6  2004/04/13 18:14:02  pelle
added open dialog to swing agent and interactive agent

Revision 1.5  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

Revision 1.4  2004/04/12 23:50:07  pelle
implemented the queue and improved the DefaultSigner

Revision 1.3  2004/04/12 15:00:29  pelle
Now have a slightly better way of handling the waiting for input using the WaitForInput class.
This will later be put into a command queue for execution.

Revision 1.2  2004/04/09 22:56:44  pelle
SwingAgent now manages key creation as well through the NewAliasDialog.
Many small uservalidation features have also been added.

Revision 1.1  2004/04/07 17:22:08  pelle
Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
The XMLSig classes have also been updated to support this.

*/

/**
 * User: pelleb
 * Date: Apr 7, 2004
 * Time: 9:55:37 AM
 */
public class SwingAgent implements InteractiveAgent {
    public SwingAgent() {
        this(null);
    }

    public SwingAgent(BrowsableSigner signer) {
        this.signer = signer;
        SwingTools.setLAF();
        ksd = new KeyStoreDialog(signer);
        simple = new SimpleDialog();
        np = new NewPassphraseDialog();
        queue = new RunnableQueue();
        fc = new JFileChooser();
        fc.setFileFilter(new JKSFilter());

    }

    private final SimpleDialog simple;
    private final NewPassphraseDialog np;
    private final KeyStoreDialog ksd;
    private final RunnableQueue queue;
    private final JFileChooser fc;
    private final BrowsableSigner signer;

    public static void main(final String[] args) {
        final SwingAgent dia = new SwingAgent();
        try {
            CryptoTools.ensureProvider();
//                System.out.println(dia.getPassPhrase("test"));
            final BrowsableSigner signer = new DefaultSigner(dia);
            dia.setSigner(signer);
            while (true) {
                byte sig[] = signer.sign("testdata".getBytes(), new SetPublicKeyCallBack() {
                    public void setPublicKey(PublicKey pub) {
                        System.out.println("PublicKey:");
                        System.out.println(pub);
                    }
                });
                System.out.println(Base64.encode(sig));
            }
//                System.out.println("Getting passphrase... " + new String(dia.getPassPhrase((BrowsableSigner) signer)));
//                System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test", true)));
        } catch (UserCancellationException e) {
            System.out.print("User Cancellation by: " + e.getName());
        }


        System.exit(0);
    }

    public void setSigner(BrowsableSigner signer) {
        ksd.setSigner(signer);
    }

    /**
     * Retrieve the PassPhrase for a given name/alias
     *
     * @param name
     * @return
     */
    public char[] getPassPhrase(String name) throws UserCancellationException {
        return getPassPhrase(name, false);
    }

    public char[] getPassPhrase(String name, boolean incorrect) throws UserCancellationException {
        WaitForInput waiter = simple.createGetPassphraseTask(name, incorrect);
        queue.queue(waiter);
        return (char[]) waiter.getResult();
    }

    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws org.neuclear.commons.crypto.passphraseagents.UserCancellationException
     *
     */
    public byte[] sign(BrowsableSigner signer, byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        WaitForInput waiter = ksd.createSigningTask(signer, data, callback);
        queue.queue(waiter);
        return (byte[]) waiter.getResult();
    }

    public File getSaveToFileName(String title, String def) throws UserCancellationException {
        prepFileChooser(def, title);
        int result = fc.showSaveDialog(ksd.getFrame());
        if (result == JFileChooser.CANCEL_OPTION)
            throw new UserCancellationException(title);
        return fc.getSelectedFile();
    }

    public File getOpenFileName(String title, String def) throws UserCancellationException {
        prepFileChooser(def, title);
        int result = fc.showOpenDialog(ksd.getFrame());
        if (result == JFileChooser.CANCEL_OPTION)
            throw new UserCancellationException(title);
        return fc.getSelectedFile();
    }

    public char[] getNewPassPhrase(String name) throws UserCancellationException {
        WaitForInput waiter = np.createGetNewPassphraseTask(name);
        queue.queue(waiter);
        return (char[]) waiter.getResult();
    }

    private void prepFileChooser(String def, String title) {
        File file = new File(def);
        fc.setCurrentDirectory(file.getParentFile());
        fc.setSelectedFile(file);
        fc.setDialogTitle(title);
    }


}
