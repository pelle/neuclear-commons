package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.crypto.passphraseagents.AgentMessages;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.NewPersonalityAction;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.OpenKeyStoreAction;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.SaveKeyStoreAction;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.DefaultSigner;
import org.neuclear.commons.crypto.signers.InvalidPassphraseException;
import org.neuclear.commons.crypto.signers.TestCaseSigner;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyStoreException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.prefs.Preferences;

/*
$Id: KeyStorePanel.java,v 1.1 2004/05/05 23:39:45 pelle Exp $
$Log: KeyStorePanel.java,v $
Revision 1.1  2004/05/05 23:39:45  pelle
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
public class KeyStorePanel extends JPanel {
    public KeyStorePanel() {
//        SwingTools.setLAF();
        prefs = Preferences.userNodeForPackage(DefaultSigner.class);

//        AgentMessages.updateLocale("es", "ES");
        caps = AgentMessages.getMessages();

        setLayout(new BorderLayout());
        toolbar = new JToolBar();
        toolbar.setFloatable(true);
        toolbar.setRollover(true);

        open = new JButton(new OpenKeyStoreAction());
        toolbar.add(open);
        save = new JButton(new SaveKeyStoreAction());
        toolbar.add(save);
        toolbar.addSeparator();

        newId = new JButton(new NewPersonalityAction());
        toolbar.add(newId);
        add(toolbar, BorderLayout.NORTH);

        list = new JList();
        list.setBorder(BorderFactory.createLoweredBevelBorder());
        add(list, BorderLayout.CENTER);
//        nad = new NewAliasDialog(this);


        newId.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {

//                SwingUtilities.invokeLater(nad);
            }
        });

        save.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(ActionEvent e) {
//                SaveKeyStore.saveAs(signer, message);
            }

        });


    }

    void updateList(String alias) {
        if (alias != null) {
            fillAliasList();
            list.setSelectedValue(alias, true);
//            message.info(alias + " added");
//            SaveKeyStore.save(signer, message);

        }
    }

    public Action[] getFileActions() {
        return new Action[]{};
    }

    public JLabel getLabel() {
        JLabel idlabel = new JLabel(caps.getString("identities"));
        idlabel.setIcon(IconTools.getPersonalities());
        idlabel.setLabelFor(list);
        return idlabel;
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

    public void addListSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    BrowsableSigner getSigner() {
        return signer;
    }

    public void setSigner(BrowsableSigner signer) {
        this.signer = signer;
        fillAliasList();
    }

    public String getSelectedValue() {
        return (String) list.getSelectedValue();
    }

    private BrowsableSigner signer;
    private String lastSelected;
    private final JButton newId;
    private final JButton save;
    private final JButton open;
    private final JList list;
    private final JToolBar toolbar;
//    private final NewAliasDialog nad;
    private final Preferences prefs;


    private static final String DEFAULT_ALIAS = "DEFAULT_ALIAS";
    private ResourceBundle caps;

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final KeyStorePanel panel = new KeyStorePanel();
        frame.getContentPane().add(panel);
        try {
            panel.setSigner(new TestCaseSigner());
        } catch (InvalidPassphraseException e) {
            e.printStackTrace();
            System.exit(1);
        }
        frame.pack();
        frame.show();
    }


}
