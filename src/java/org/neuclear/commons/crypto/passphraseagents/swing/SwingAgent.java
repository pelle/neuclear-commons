package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.neuclear.commons.crypto.Base64;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.InvalidPassphraseException;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;
import org.neuclear.commons.crypto.signers.TestCaseSigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/*
$Id: SwingAgent.java,v 1.1 2004/04/07 17:22:08 pelle Exp $
$Log: SwingAgent.java,v $
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
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        cache = new HashMap();
        sign = new JButton("Sign");
        cancel = new JButton("Cancel");
        newId = new JButton("New ...");
        list = new JList(new String[]{"bob", "carol", "alice"});
        list.setBorder(BorderFactory.createLoweredBevelBorder());
        passphrase = new JPasswordField();
        final URL imageurl = this.getClass().getClassLoader().getResource("org/neuclear/commons/crypto/passphraseagents/neuclear.png");
        if (imageurl != null)
            icon = new JLabel(new ImageIcon(imageurl));
        else
            icon = new JLabel("NeuClear");

        frame = new JFrame();
        frame.setTitle("NeuClear Signing Agent");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(buildPanel());
        frame.pack();

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
                    isCancel = false;
                    passphrase.notifyAll();
                }

            }
        };
        sign.addActionListener(action);
        passphrase.addActionListener(action);



//        frame.show();


    }

    private Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "pref,3dlu,pref, 3dlu, fill:pref:grow, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(icon, cc.xyw(1, 5, 1, CellConstraints.LEFT, CellConstraints.TOP));
        builder.addSeparator("Identities", cc.xyw(1, 3, 3));
        builder.add(list, cc.xyw(3, 5, 1));
        builder.addLabel("Passphrase:", cc.xy(1, 7));
        builder.add(passphrase, cc.xy(3, 7));

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGridded(newId);
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(sign);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 9, 3));

        return builder.getPanel();
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
        synchronized (passphrase) {//We dont want multiple agents popping up at the same time
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

//           if (cache.containsKey(name))
//               passphrase.setText((String) cache.get(name));
//           else
            passphrase.setText("");
            isCancel = true;
//           if (incorrect)
//               System.err.println("Incorrect passphrase");
//            incorrectLabel.setVisible(incorrect);
//            nameLabel.setVisible(true);
//
//            nameLabel.setText(name);
            frame.pack();
            frame.show();
//           frame.setVisible(true);
            try {
                passphrase.wait();
            } catch (InterruptedException e) {
                ;
            }
            frame.hide();
//           frame.setVisible(false);
            final String phrase = passphrase.getText();
//            if (remember.getState())
//                cache.put(name, phrase);
            passphrase.setText("");
            try {
                //Todo handle when an alias hasnt been selected
                return signer.sign(list.getSelectedValue().toString(), phrase.toCharArray(), data, callback);
            } catch (InvalidPassphraseException e) {
                return new byte[0];//TODO handle invalid passphrase
            }
        }
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
            if (cache.containsKey(name))
                passphrase.setText((String) cache.get(name));
            else
                passphrase.setText("");
            isCancel = true;
//            if (incorrect)
//                System.err.println("Incorrect passphrase");
//            incorrectLabel.setVisible(incorrect);
//            nameLabel.setVisible(true);
//
//            nameLabel.setText(name);
            frame.pack();
            frame.setVisible(true);
            try {
                passphrase.wait();
            } catch (InterruptedException e) {
                ;
            }
            frame.setVisible(false);
            if (isCancel)
                throw new UserCancellationException(name);
            final String phrase = passphrase.getText();
//            if (remember.getState())
//                cache.put(name, phrase);
            passphrase.setText("");
            return phrase.toCharArray();
        }
    }


    private final JButton sign;
    private final JButton cancel;
    private final JButton newId;
    private final JList list;
    private final JTextField passphrase;
    private final JFrame frame;
    private final Map cache;
    private boolean isCancel;
    private final JLabel icon;

    public static void main(final String[] args) {
        final InteractiveAgent dia = new SwingAgent();
        try {
            final BrowsableSigner signer = new TestCaseSigner(dia);
            try {
                byte sig[] = signer.sign("testdata".getBytes(), new SetPublicKeyCallBack() {
                    public void setPublicKey(PublicKey pub) {
                        System.out.println("PublicKey:");
                        System.out.println(pub);
                    }
                });
                System.out.println(Base64.encode(sig));
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


}
