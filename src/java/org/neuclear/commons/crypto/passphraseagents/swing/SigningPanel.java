package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.neuclear.commons.crypto.passphraseagents.AgentMessages;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.signers.*;
import org.neuclear.commons.swing.SwingTools;
import org.neuclear.commons.swing.WaitForInput;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/*
$Id: SigningPanel.java,v 1.2 2004/05/18 19:19:03 pelle Exp $
$Log: SigningPanel.java,v $
Revision 1.2  2004/05/18 19:19:03  pelle
Added Swing package to commons.
NeuClearDialog is a standard Abstract Dialog Class for modal dialogs.
ProcessDialog is a standard Abstract Dialog Class for modal dialogs with a long running processing task.
Fixed serialization issues in Signer. It now loads and saves the IdentityListModel correctly.
AddIdentityDialog is a subclass of the above mentioned ProcessDialog.
Missing are:
- better error messages
- Populate and use categories combo

Revision 1.1  2004/05/06 21:40:30  pelle
More swing refactorings

Revision 1.11  2004/05/05 23:39:45  pelle
Starting to organize the swing parts of the passphrase agent a bit better.
I am creating actions, panels and dialogs.

Revision 1.10  2004/04/22 12:35:29  pelle
Added Icons and improved localisation

Revision 1.9  2004/04/21 23:10:13  pelle
Fixed mac look and feel

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
public class SigningPanel extends JPanel {
    public SigningPanel(BrowsableSigner signer) {
        SwingTools.setLAF();
        this.signer = signer;
        prefs = Preferences.userNodeForPackage(DefaultSigner.class);
//        AgentMessages.updateLocale("es", "ES");
        caps = AgentMessages.getMessages();
        setLayout(new BorderLayout());
        keys = new KeyStorePanel(signer);
        cache = new HashMap();
        sign = new JButton(caps.getString("sign"));
        sign.setIcon(IconTools.getSign());
        sign.setEnabled(false);
        cancel = new JButton(caps.getString("cancel"));
        cancel.setIcon(IconTools.getCancel());
        message = new MessageLabel();
        remember = new JCheckBox(caps.getString("remember"), prefs.getBoolean(REMEMBER_PASSPHRASE, false));
        passphrase = new JPasswordField();
        buildPanel();

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
                    if (isReady()) {
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
                sign.setEnabled(isReady());

            }

            public void keyTyped(KeyEvent e) {

            }
        };
        passphrase.addKeyListener(validate);


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

    private void buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "fill:pref:grow, 3dlu,pref,3dlu,pref, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(keys, cc.xyw(1, 1, 3));
        final JLabel pslabel = builder.addLabel(caps.getString("passphrase"), cc.xy(1, 3));
        pslabel.setLabelFor(passphrase);
        pslabel.setIcon(IconTools.getPassword());

        builder.add(passphrase, cc.xy(3, 3));
        builder.add(remember, cc.xy(3, 5));
        builder.add(message, cc.xyw(1, 7, 3));
        this.add(builder.getPanel(), BorderLayout.CENTER);

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(sign);
        bb.addGridded(cancel);
        this.add(bb.getPanel(), BorderLayout.SOUTH);
//        builder.add(bb.getPanel(), cc.xyw(1, 9, 3));
//        return builder.getPanel();
    }


    BrowsableSigner getSigner() {
        return signer;
    }


    private boolean isReady() {
        return (keys.getSelectedIndex() >= 0 && passphrase.getPassword().length > 0);
    }

    WaitForInput createSigningTask(BrowsableSigner bs, byte data[], SetPublicKeyCallBack cb) {
        return new DialogRunner(data, cb);
    }

    private final BrowsableSigner signer;
    private String lastSelected;
    private final JButton sign;
    private final JButton cancel;
    private final MessageLabel message;
    private final JCheckBox remember;
    private final KeyStorePanel keys;
    private final JPasswordField passphrase;
    private final Map cache;
    private final Preferences prefs;
    private DialogRunner runner;


    class DialogRunner extends WaitForInput {
        public DialogRunner(byte data[], SetPublicKeyCallBack cb) {
            this.data = data;
            this.cb = cb;
            this.invalid = false;
        }

        public void run() {
            runner = this;
            if (keys.getModel().getSize() > 0) {
                keys.setSelectedValue(prefs.get(DEFAULT_ALIAS, ""), true);
                if (keys.getSelectedIndex() == -1)
                    keys.setSelectedIndex(0);
                if (remember.isSelected() && (keys.getSelectedIndex() != -1)
                        && cache.containsKey(keys.getSelectedValue()))
                    passphrase.setText((String) cache.get(keys.getSelectedValue()));
                else
                    passphrase.setText("");
            } else
                passphrase.setText("");
            if (invalid)
                message.invalidPassphrase();
            else
                message.clear();
            sign.setEnabled(isReady());
            passphrase.requestFocus();
        }

        public void execute() {
            if (remember.isSelected())
                cache.put(keys.getSelectedValue().toString(), new String(passphrase.getPassword()));
            prefs.putBoolean(REMEMBER_PASSPHRASE, remember.isSelected());
            prefs.put(DEFAULT_ALIAS, keys.getSelectedValue().toString());
            try {
                prefs.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }

            char phrase[] = passphrase.getPassword();
            passphrase.setText("");
            try {
                final byte[] sig = signer.sign(keys.getSelectedValue().toString(), phrase, data, cb);
                invalid = false;
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
        private boolean invalid = false;

    }

    public static void main(String args[]) {
        try {
            final SigningPanel panel = new SigningPanel(new TestCaseSigner());
            JDialog dia = new JDialog();
            dia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dia.getContentPane().add(panel);
            dia.pack();
            dia.show();
        } catch (InvalidPassphraseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static final String DEFAULT_ALIAS = "DEFAULT_ALIAS";
    private static final String REMEMBER_PASSPHRASE = "REMEMBER_PASSPHRASE";
    private ResourceBundle caps;

}
