package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import com.l2fprod.common.swing.BannerPanel;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.swing.Messages;

import javax.swing.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/*
$Id: NewAliasDialog.java,v 1.15 2004/06/03 23:13:17 pelle Exp $
$Log: NewAliasDialog.java,v $
Revision 1.15  2004/06/03 23:13:17  pelle
Changes to Messages. Does not compile.

Revision 1.14  2004/05/21 19:24:04  pelle
Changed name of Neuclear Personal Signer to NeuClear Personal Trader
More changes from Personality to Account
Moved hibernates.properties out from the jar file and to the test directory and where ever it gets used, to avoid conflicts between multiple files.

Revision 1.13  2004/05/17 18:54:09  pelle
NewAliasDialog now uses BannerLabel
IdentityListModel is now stored using xstream

Revision 1.12  2004/05/16 00:04:00  pelle
Added SigningServer which encapsulates all the web serving functionality.
Added IdentityPanel which contains an IdentityTree of Identities.
Added AssetPanel
Save now works and Add Personality as well.

Revision 1.11  2004/05/14 23:47:01  pelle
Moved PersonalSigner and OpenSignerDialog to neuclear-commons where they belong.
The whole mechanism of opening keystores is pretty smooth right now.
Currently working on saving, which doesnt quite work yet. I have added a save method to OpenSignerDialog, which
should handle it.

Revision 1.10  2004/05/06 17:36:29  pelle
Further slight mods in the gui

Revision 1.9  2004/05/05 23:39:45  pelle
Starting to organize the swing parts of the passphrase agent a bit better.
I am creating actions, panels and dialogs.

Revision 1.8  2004/04/22 12:35:29  pelle
Added Icons and improved localisation

Revision 1.7  2004/04/21 23:10:13  pelle
Fixed mac look and feel

Revision 1.6  2004/04/15 15:34:41  pelle
Got rid of the looping InvalidPassphraseException in DefaultSigner.
Added initial focus for all dialogs.

Revision 1.5  2004/04/14 00:10:51  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

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

    public NewAliasDialog(KeyStorePanel agent) {
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
        this.ksPanel = agent;
        caps = Messages.getMessages();

        ok = new JButton(caps.getString("create"));
        ok.setIcon(IconTools.getOK());
        ok.setEnabled(false);
        cancel = new JButton(caps.getString("cancel"));
        cancel.setIcon(IconTools.getCancel());
        cancel2 = new JButton(caps.getString("cancel"));
        cancel2.setIcon(IconTools.getCancel());
        alias = new JTextField();

        passphrase1 = new JPasswordField();
        passphrase2 = new JPasswordField();

        banner = new com.l2fprod.common.swing.BannerPanel();
        banner.setIcon(IconTools.getLogo());
        banner.setTitle("Create Account");
        banner.setSubtitle("Choose a <b>name</b> and enter your <b>password</b> twice. The name will only be known to you.");

        message = new MessageLabel();

        progress = new JProgressBar(0, 100);
        progress.setIndeterminate(true);
        progress.setVisible(true);

        dialog = new JDialog();
        dialog.setTitle(caps.getString("signingagent"));
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
        ((JComponent) regular).registerKeyboardAction(cl,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        ((JComponent) process).registerKeyboardAction(cl,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                synchronized (alias) {
                    passphrase1.setText("");
                    passphrase2.setText("");
                    alias.setText("");
                    dialog.hide();
                }

            }
        });

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
        if (alias.getText().length() == 0) {
            message.invalid("Please enter your new Identity Name");
            return false;
        }
        if (ksPanel.getSigner().canSignFor(alias.getText())) {
            message.invalid(alias.getText() + " already exists");
            return false;
        }
        char[] p1 = passphrase1.getPassword();
        char[] p2 = passphrase2.getPassword();
        if (p1 == null || p2 == null || p1.length == 0 || p2.length == 0) {
            message.invalid("Please enter your new matching passphrases");
            return false;
        }
        if (p1.length != p2.length) {
            message.invalid("Both passphrases must be the same");
            return false;
        }
        message.clear();
        return true;//new String(p1).equals(new String(p2));
    }

    private JPanel buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, 100dlu:grow ",
                "pref,3dlu,pref, 3dlu, pref, 3dlu, pref,3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(banner, cc.xyw(1, 1, 3));
        final JLabel aliaslabel = builder.addLabel(caps.getString("name"), cc.xy(1, 3));
        aliaslabel.setIcon(IconTools.getPersonality());
        aliaslabel.setLabelFor(alias);
        builder.add(alias, cc.xy(3, 3));
        final JLabel ppl1 = builder.addLabel(caps.getString("passphrase"), cc.xy(1, 5));
        ppl1.setLabelFor(passphrase1);
        ppl1.setIcon(IconTools.getPassword());
        builder.add(passphrase1, cc.xy(3, 5));

        final JLabel ppl2 = builder.addLabel(caps.getString("repeatpassphrase"), cc.xy(1, 7));
        ppl2.setLabelFor(passphrase2);
        ppl2.setIcon(IconTools.getPassword());
        builder.add(passphrase2, cc.xy(3, 7));
        builder.add(message, cc.xyw(1, 9, 3));

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 11, 3));

        return builder.getPanel();

    }

    private JPanel buildProcessPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, 100dlu:grow ",
                "pref,3dlu,pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.addSeparator("Creating Account Keys", cc.xyw(1, 1, 3));
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
        alias.setText("");
        alias.setEnabled(true);
        passphrase1.setText("");
        passphrase2.setText("");
        alias.setEnabled(true);
        passphrase2.setEnabled(true);
        setMainPanel();
        alias.requestFocus();
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
                ksPanel.getSigner().createKeyPair(alias.getText(), passphrase1.getPassword());
                progress.setVisible(false);
//                ksPanel.updateList(alias.getText());
                dialog.hide();
            } catch (CryptoException e) {
                e.printStackTrace();
            }

        }

    }

    private KeyStorePanel ksPanel;
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
    private MessageLabel message;
    private ResourceBundle caps;
    private BannerPanel banner;

}
