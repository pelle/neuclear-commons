package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.passphraseagents.AgentMessages;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.DefaultSigner;
import org.neuclear.commons.crypto.signers.InvalidPassphraseException;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.security.KeyStoreException;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/*
$Id: KeyStoreDialog.java,v 1.8 2004/04/16 18:12:39 pelle Exp $
$Log: KeyStoreDialog.java,v $
Revision 1.8  2004/04/16 18:12:39  pelle
Added AgentMessages to support localization.
added us and spanish locales
Implemented localization for KeyStoreDialog

Revision 1.7  2004/04/15 20:03:51  pelle
Added license screen to Personal Signer.
Added Sign document menu to  Personal Signer.

Revision 1.6  2004/04/15 15:34:41  pelle
Got rid of the looping InvalidPassphraseException in DefaultSigner.
Added initial focus for all dialogs.

Revision 1.5  2004/04/14 23:39:57  pelle
Fixed a few things in the ServletSignerFactory
Added testkeys.jks where it should be.

Revision 1.4  2004/04/14 00:10:51  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

Revision 1.3  2004/04/13 17:32:05  pelle
Now has save dialog
Remembers passphrases

Revision 1.2  2004/04/12 23:50:07  pelle
implemented the queue and improved the DefaultSigner

Revision 1.1  2004/04/12 15:00:29  pelle
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
public class KeyStoreDialog {
    /**
     * @jira NEU-30 Allow Identity Objects to be described in XHTML
     */
    public KeyStoreDialog() {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        prefs = Preferences.userNodeForPackage(DefaultSigner.class);
        AgentMessages.updateLocale("es", "ES");
        caps = AgentMessages.getMessages();
        cache = new HashMap();
        sign = new JButton(caps.getString("sign"));
        sign.setEnabled(false);
        cancel = new JButton(caps.getString("cancel"));
        newId = new JButton(caps.getString("newid"));
        message = new MessageLabel();
        save = new JButton(caps.getString("savekeys"));
        remember = new JCheckBox(caps.getString("remember"), prefs.getBoolean(REMEMBER_PASSPHRASE, false));
        list = new JList();
        list.setBorder(BorderFactory.createLoweredBevelBorder());
        passphrase = new JPasswordField();
        frame = new JFrame();

        final URL imageurl = this.getClass().getClassLoader().getResource("org/neuclear/commons/crypto/passphraseagents/neuclear.png");
        if (imageurl != null) {
            final ImageIcon icon = new ImageIcon(imageurl);
            frame.setIconImage(icon.getImage());
            this.icon = new JLabel(icon);
        } else
            icon = new JLabel("NeuClear");

        frame.setTitle("NeuClear " + caps.getString("signingagent"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(buildPanel());
        frame.pack();
        nad = new NewAliasDialog(this);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    passphrase.setText("");
                    frame.hide();
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
        sign.addActionListener(action);
        passphrase.addActionListener(action);
        final KeyListener validate = new KeyListener() {
            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                sign.setEnabled(validate());

            }

            public void keyTyped(KeyEvent e) {

            }
        };
        passphrase.addKeyListener(validate);

        list.addListSelectionListener(new ListSelectionListener() {
            /**
             * Called whenever the value of the selection changes.
             *
             * @param e the event that characterizes the change.
             */
            public void valueChanged(ListSelectionEvent e) {
                if (list.getModel().getSize() == 0)
                    return;
                lastSelected = (String) list.getSelectedValue();
                if (remember.isSelected() && cache.containsKey(lastSelected))
                    passphrase.setText((String) cache.get(lastSelected));
                else
                    passphrase.setText("");
                sign.setEnabled(validate());

            }

        });

        newId.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {

                SwingUtilities.invokeLater(nad);
            }
        });

        save.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(ActionEvent e) {
                SaveKeyStore.saveAs(signer, message);
            }

        });
        remember.addChangeListener(new ChangeListener() {
            /**
             * Invoked when the target of the listener has changed its state.
             *
             * @param e a ChangeEvent object
             */
            public void stateChanged(ChangeEvent e) {
                cache.clear();

            }

        });
//        dialog.show();


    }

    void updateList(String alias) {
        if (alias != null) {
            fillAliasList();
            list.setSelectedValue(alias, true);
            message.info(alias + " added");
            frame.pack();
            SaveKeyStore.save(signer, message);

        }
    }

    private Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "pref,3dlu,pref, 3dlu, fill:pref:grow, 3dlu, pref, 3dlu, pref,3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(icon, cc.xyw(1, 5, 1, CellConstraints.LEFT, CellConstraints.TOP));
        icon.setLabelFor(list);
        builder.addSeparator(caps.getString("identities"), cc.xyw(1, 3, 3));
        builder.add(list, cc.xyw(3, 5, 1));
        builder.addLabel(caps.getString("passphrase"), cc.xy(1, 7)).setLabelFor(passphrase);
        builder.add(passphrase, cc.xy(3, 7));
        builder.add(remember, cc.xy(3, 9));
        builder.add(message, cc.xyw(1, 11, 3));

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGridded(newId);
        bb.addGridded(save);
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(sign);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 13, 3));

        return builder.getPanel();
    }

    private void fillAliasList() {
        try {

            Iterator iter = signer.iterator();
            Vector vector = new Vector();
            while (iter.hasNext()) {
                Object o = iter.next();
                vector.add(o);
            }
            list.setListData(vector);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    BrowsableSigner getSigner() {
        return signer;
    }

    JFrame getFrame() {
        return frame;
    }

    private boolean validate() {
        return (list.getSelectedIndex() >= 0 && passphrase.getPassword().length > 0);
    }

    WaitForInput createSigningTask(BrowsableSigner bs, byte data[], SetPublicKeyCallBack cb) {
        return new DialogRunner(bs, data, cb);
    }

    private BrowsableSigner signer;
    private String lastSelected;
    private final JButton sign;
    private final JButton cancel;
    private final JButton newId;
    private final JButton save;
    private final MessageLabel message;
    private final JCheckBox remember;
    private final JList list;
    private final JPasswordField passphrase;
    private final JFrame frame;
    private final NewAliasDialog nad;
    private final Map cache;
    private final JLabel icon;
    private final Preferences prefs;
    private DialogRunner runner;


    class DialogRunner extends WaitForInput {
        public DialogRunner(BrowsableSigner bs, byte data[], SetPublicKeyCallBack cb) {
            this.data = data;
            this.cb = cb;
            this.bs = bs;
            this.invalid = false;
        }

        public void run() {
            runner = this;
            signer = bs;
            fillAliasList();
            if (list.getModel().getSize() > 0) {
                list.setSelectedValue(prefs.get(DEFAULT_ALIAS, ""), true);
                if (list.getSelectedIndex() == -1)
                    list.setSelectedIndex(0);
                if (remember.isSelected() && (list.getSelectedIndex() != -1)
                        && cache.containsKey(list.getSelectedValue()))
                    passphrase.setText((String) cache.get(list.getSelectedValue()));
                else
                    passphrase.setText("");
            } else
                passphrase.setText("");
            if (invalid)
                message.invalidPassphrase();
            else
                message.clear();
            frame.pack();
            frame.show();
            frame.toFront();
            passphrase.requestFocus();
            sign.setEnabled(false);
        }

        public void execute() {
            if (remember.isSelected())
                cache.put(list.getSelectedValue().toString(), new String(passphrase.getPassword()));
            prefs.putBoolean(REMEMBER_PASSPHRASE, remember.isSelected());
            prefs.put(DEFAULT_ALIAS, list.getSelectedValue().toString());
            try {
                prefs.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }

            char phrase[] = passphrase.getPassword();
            passphrase.setText("");
            try {
                final byte[] sig = signer.sign(list.getSelectedValue().toString(), phrase, data, cb);
                invalid = false;
                frame.hide();
                setResult(sig);
            } catch (InvalidPassphraseException e) {
                invalid = true;
                run();
            } catch (Exception e) {
                message.error(e);
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

        }


        private final byte data[];
        private final SetPublicKeyCallBack cb;
        private final BrowsableSigner bs;
        private boolean invalid = false;

    }

    private static final String DEFAULT_ALIAS = "DEFAULT_ALIAS";
    private static final String REMEMBER_PASSPHRASE = "REMEMBER_PASSPHRASE";
    private ResourceBundle caps;

}
