package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.Base64;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.DefaultSigner;
import org.neuclear.commons.crypto.signers.InvalidPassphraseException;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;

import javax.swing.*;
import java.io.File;
import java.security.PublicKey;

/*
$Id: SwingAgent.java,v 1.7 2004/04/14 00:10:52 pelle Exp $
$Log: SwingAgent.java,v $
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
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        ksd = new KeyStoreDialog();
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

    public static void main(final String[] args) {
        final InteractiveAgent dia = new SwingAgent();
        try {
            try {
//                System.out.println(dia.getPassPhrase("test"));
                final BrowsableSigner signer = new DefaultSigner(dia);
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

        } catch (InvalidPassphraseException e) {
            e.printStackTrace();
        }

        System.exit(0);
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

    class JKSFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File file) {
            String filename = file.getName();
            return filename.endsWith(".jks");
        }

        public String getDescription() {
            return "*.jks";
        }
    }

}
