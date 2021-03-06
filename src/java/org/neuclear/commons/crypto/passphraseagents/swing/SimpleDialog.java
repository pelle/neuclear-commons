package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.swing.WaitForInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/*
$Id: SimpleDialog.java,v 1.8 2004/05/18 19:19:03 pelle Exp $
$Log: SimpleDialog.java,v $
Revision 1.8  2004/05/18 19:19:03  pelle
Added Swing package to commons.
NeuClearDialog is a standard Abstract Dialog Class for modal dialogs.
ProcessDialog is a standard Abstract Dialog Class for modal dialogs with a long running processing task.
Fixed serialization issues in Signer. It now loads and saves the IdentityListModel correctly.
AddIdentityDialog is a subclass of the above mentioned ProcessDialog.
Missing are:
- better error messages
- Populate and use categories combo

Revision 1.7  2004/04/22 23:59:51  pelle
Added various statistics to Ledger as well as AssetController
Improved look and feel in the web app.

Revision 1.6  2004/04/21 23:10:13  pelle
Fixed mac look and feel

Revision 1.5  2004/04/15 15:34:41  pelle
Got rid of the looping InvalidPassphraseException in DefaultSigner.
Added initial focus for all dialogs.

Revision 1.4  2004/04/14 00:10:52  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

Revision 1.3  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

Revision 1.2  2004/04/12 15:00:29  pelle
Now have a slightly better way of handling the waiting for input using the WaitForInput class.
This will later be put into a command queue for execution.

Revision 1.1  2004/04/09 22:56:44  pelle
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
public class SimpleDialog {
    public SimpleDialog() {
        try {
            if (UIManager.getSystemLookAndFeelClassName().equals("apple.laf.AquaLookAndFeel"))
                System.setProperty("com.apple.laf.useScreenMenuBar", "true");
            else {
                UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
                UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
            }
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        ok = new JButton("Open");
        ok.setIcon(IconTools.getOK());
        ok.setEnabled(false);
        cancel = new JButton("Cancel");
        cancel.setIcon(IconTools.getCancel());
        alias = new JLabel();
        passphrase = new JPasswordField();
        message = new MessageLabel();
        final URL imageurl = this.getClass().getClassLoader().getResource("org/neuclear/commons/crypto/passphraseagents/neuclear.png");
        if (imageurl != null)
            icon = new JLabel(new ImageIcon(imageurl));
        else
            icon = new JLabel("NeuClear");

        dialog = new JDialog();
        dialog.setTitle("NeuClear Signing Agent");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(buildPanel());
        dialog.pack();

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    passphrase.setText("");
                    runner.cancel();
                }

            }
        });

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    if (validate()) {
                        runner.execute();
                    }
                }

            }
        };
        ok.addActionListener(action);
        passphrase.addActionListener(action);
        final KeyListener validate = new KeyListener() {
            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                ok.setEnabled(validate());

            }

            public void keyTyped(KeyEvent e) {

            }
        };
        passphrase.addKeyListener(validate);

    }

    private Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "pref,3dlu,pref, 3dlu, fill:pref:grow, 3dlu, pref, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(icon, cc.xyw(1, 1, 1, CellConstraints.LEFT, CellConstraints.TOP));
        builder.addSeparator("Enter passphrase", cc.xyw(1, 3, 3));
        builder.addLabel("open:", cc.xy(1, 5)).setLabelFor(alias);
        builder.add(alias, cc.xy(3, 5));
        final JLabel label = builder.addLabel("Passphrase:", cc.xy(1, 7));
        label.setLabelFor(passphrase);
        label.setIcon(IconTools.getPassword());
        builder.add(passphrase, cc.xy(3, 7));
        builder.add(message, cc.xyw(1, 9, 3));


        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 11, 3));

        return builder.getPanel();
    }


    private boolean validate() {
        return (passphrase.getPassword().length > 0);
    }

    WaitForInput createGetPassphraseTask(final String name, final boolean incorrect) {
        return new DialogRunner(name, incorrect);
    }


    private final JButton ok;
    private final JButton cancel;
    private final JLabel alias;
    private final JPasswordField passphrase;
    private final JDialog dialog;
    private final JLabel icon;
    private final MessageLabel message;
    private WaitForInput runner;

    class DialogRunner extends WaitForInput {
        public DialogRunner(final String alias, final boolean incorrect) {
            this.req = alias;
            this.incorrect = incorrect;
        }

        public void run() {
            runner = this;
            alias.setText(req);
            if (incorrect)
                message.invalid("You entered an invalid passphrase. Try again...");
            dialog.pack();
            dialog.show();
            passphrase.requestFocus();
            System.out.println(Thread.currentThread());

        }

        public void execute() {
            dialog.hide();
            final char[] phrase = passphrase.getPassword();
//            if (remember.getState())
//                cache.put(name, phrase);
            passphrase.setText("");
            setResult(phrase);
        }

        private final String req;
        private final boolean incorrect;
    }

}
