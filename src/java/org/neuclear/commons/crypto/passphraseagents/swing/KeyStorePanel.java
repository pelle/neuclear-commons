package org.neuclear.commons.crypto.passphraseagents.swing;

/*
 *  The NeuClear Project and it's libraries are
 *  (c) 2002-2004 Antilles Software Ventures SA
 *  For more information see: http://neuclear.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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

/**
 * User: pelleb
 * Date: Apr 7, 2004
 * Time: 9:55:37 AM
 */
public class KeyStorePanel extends JPanel {
    public KeyStorePanel() {
        SwingTools.setLAF();
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
