package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.CryptoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
$Id: NewAliasDialog.java,v 1.4 2004/04/13 17:32:05 pelle Exp $
$Log: NewAliasDialog.java,v $
Revision 1.4  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

Revision 1.3  2004/04/12 15:00:29  pelle
Now have a slightly better way of handling the waiting for input using the WaitForInput class.
This will later be put into a command queue for execution.

Revision 1.2  2004/04/09 22:56:44  pelle
SwingAgent now manages key creation as well through the NewAliasDialog.
Many small uservalidation features have also been added.

Revision 1.1  2004/04/09 18:40:45  pelle
BrowsableSigner now inherits Signer and PublicKeySource, which means implementations only need to implement BrowsableSigner now.
Added NewAliasDialog, which isnt yet complete.

*/

/**
 * User: pelleb
 * Date: Apr 8, 2004
 * Time: 5:58:38 PM
 */
public class NewAliasDialog implements Runnable {
    public NewAliasDialog(KeyStoreDialog agent) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        this.agent = agent;
        ok = new JButton("Create");
        ok.setEnabled(false);
        cancel = new JButton("Cancel");
        cancel2 = new JButton("Cancel");
        alias = new JTextField();

        passphrase1 = new JPasswordField();
        passphrase2 = new JPasswordField();
        progress = new JProgressBar(0, 100);
        progress.setIndeterminate(true);
        progress.setVisible(true);
        dialog = new JDialog(agent.getDialog(), true);
        dialog.setTitle("NeuClear Signing Agent");
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dialog.hide();
        regular = buildPanel();
        process = buildProcessPanel();
        //setMainPanel();

        final ActionListener cl = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (alias) {
                    passphrase1.setText("");
                    passphrase2.setText("");
                    alias.setText("");
                    dialog.hide();
                }

            }
        };
        cancel.addActionListener(cl);
        cancel2.addActionListener(cl);

        final KeyListener validate = new KeyListener() {
            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                ok.setEnabled(validate());

            }

            public void keyTyped(KeyEvent e) {

            }
        };
        alias.addKeyListener(validate);
        passphrase1.addKeyListener(validate);
        passphrase2.addKeyListener(validate);

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (alias) {
                    if (validate()) {
                        createAlias();
                    }
                }

            }
        };

        ok.addActionListener(action);
        passphrase2.addActionListener(action);

    }

    private void setMainPanel() {
        dialog.getContentPane().removeAll();
        dialog.getContentPane().add(regular);
        dialog.pack();
        dialog.show();
    }

    private void setProcessPanel() {
        dialog.getContentPane().removeAll();
        dialog.getContentPane().add(process);
        dialog.pack();
        dialog.show();
    }

    private boolean validate() {
        if (alias.getText().length() == 0)
            return false;
        char[] p1 = passphrase1.getPassword();
        char[] p2 = passphrase2.getPassword();
        if (p1 == null || p2 == null)
            return false;
        if (p1.length != p2.length)
            return false;
        if (p1.length == 0)
            return false;
        return true;//new String(p1).equals(new String(p2));
    }

    private JPanel buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, 100dlu:grow ",
                "pref,3dlu,pref, 3dlu, pref, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.addSeparator("Create alias", cc.xyw(1, 1, 3));
        builder.addLabel("Alias:", cc.xy(1, 3));
        builder.add(alias, cc.xy(3, 3));
        builder.addLabel("Passphrase:", cc.xy(1, 5));
        builder.add(passphrase1, cc.xy(3, 5));
        builder.addLabel("(Repeat) Passphrase:", cc.xy(1, 7));
        builder.add(passphrase2, cc.xy(3, 7));
//        builder.add(progress,cc.xyw(1,9,3));
        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 9, 3));

        return builder.getPanel();

    }

    private JPanel buildProcessPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, 100dlu:grow ",
                "pref,3dlu,pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.addSeparator("Creating Keys", cc.xyw(1, 1, 3));
        builder.add(progress, cc.xyw(1, 3, 3));
        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
//        bb.addGridded(ok);
        bb.addGridded(cancel2);
        builder.add(bb.getPanel(), cc.xyw(1, 5, 3));

        return builder.getPanel();

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
        ok.setEnabled(false);
        cancel.setEnabled(true);
        passphrase1.setEnabled(true);
        alias.setEnabled(true);
        passphrase2.setEnabled(true);
        setMainPanel();
    }

    private void createAlias() {
        setProcessPanel();
        new Thread(new GenerateKeyTask(), "Generate Key").start();
    }

    private class GenerateKeyTask implements Runnable {
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
                System.out.println("Generating Key");
                agent.getSigner().createKeyPair(alias.getText(), passphrase1.getPassword());
                progress.setVisible(false);
                agent.updateList(alias.getText());
                dialog.hide();
            } catch (CryptoException e) {
                e.printStackTrace();
            }

        }

    }

    private KeyStoreDialog agent;
    private JDialog dialog;
    private JButton ok;
    private JButton cancel;
    private JButton cancel2;
    private JTextField alias;
    private JPasswordField passphrase1;
    private JPasswordField passphrase2;
    private JProgressBar progress;
    private JPanel regular;
    private JPanel process;

}
