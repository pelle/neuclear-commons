package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/*
$Id: SimpleDialog.java,v 1.1 2004/04/09 22:56:44 pelle Exp $
$Log: SimpleDialog.java,v $
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
    public SimpleDialog(SwingAgent agent) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        this.agent = agent;
        ok = new JButton("Open");
        ok.setEnabled(false);
        cancel = new JButton("Cancel");
        passphrase = new JPasswordField();
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
                    isCancel = true;
                    passphrase.notifyAll();
                }

            }
        });

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    if (validate()) {
                        isCancel = false;
                        passphrase.notifyAll();
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
                "pref,3dlu,pref, 3dlu, fill:pref:grow, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(icon, cc.xyw(1, 5, 1, CellConstraints.LEFT, CellConstraints.TOP));
        builder.addSeparator("Enter passphrase", cc.xyw(1, 3, 3));
        builder.addLabel("Passphrase:", cc.xy(1, 7));
        builder.add(passphrase, cc.xy(3, 7));

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 9, 3));

        return builder.getPanel();
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
        synchronized (passphrase) {//We dont want multiple agents popping up at the same time
            isCancel = true;
//            if (incorrect)
//                System.err.println("Incorrect passphrase");
//            incorrectLabel.setVisible(incorrect);
//            nameLabel.setVisible(true);
//
//            nameLabel.setText(name);
            dialog.pack();
            dialog.setVisible(true);
            try {
                passphrase.wait();
            } catch (InterruptedException e) {
                ;
            }
            dialog.setVisible(false);
            if (isCancel)
                throw new UserCancellationException(name);
            final String phrase = passphrase.getText();
//            if (remember.getState())
//                cache.put(name, phrase);
            passphrase.setText("");
            return phrase.toCharArray();
        }
    }

    BrowsableSigner getSigner() {
        return signer;
    }

    JDialog getDialog() {
        return dialog;
    }

    private boolean validate() {
        return (passphrase.getPassword().length > 0);
    }

    private BrowsableSigner signer;

    private final JButton ok;
    private final JButton cancel;
    private final JPasswordField passphrase;
    private final JDialog dialog;
    private boolean isCancel;
    private SwingAgent agent;
    private final JLabel icon;


}
