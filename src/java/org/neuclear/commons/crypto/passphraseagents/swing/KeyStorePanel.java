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

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.NewPersonalityAction;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.OpenKeyStoreAction;
import org.neuclear.commons.crypto.passphraseagents.swing.actions.SaveKeyStoreAction;
import org.neuclear.commons.crypto.signers.*;
import org.neuclear.commons.swing.Messages;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.security.KeyStoreException;
import java.util.ArrayList;
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
    public KeyStorePanel(BrowsableSigner signer) {
//        SwingTools.setLAF();
        this.signer = signer;
        prefs = Preferences.userNodeForPackage(DefaultSigner.class);

//        AgentMessages.updateLocale("es", "ES");
        caps = Messages.getMessages();

        setLayout(new BorderLayout());
        toolbar = new JToolBar();
        toolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        toolbar.setFloatable(true);
        toolbar.setRollover(true);
        actions = new ArrayList(3);
        addAction(new OpenKeyStoreAction(signer), true);
        addAction(new SaveKeyStoreAction(signer), true);
        toolbar.addSeparator();
        addAction(new NewPersonalityAction(this), false);
        add(toolbar, BorderLayout.NORTH);

        list = new JList();
        if (signer instanceof PersonalSigner)
            list.setModel((ListModel) signer);
//        list.setBorder(BorderFactory.createLoweredBevelBorder());
        list.setCellRenderer(new KeyStoreListCellRenderer());
        add(new JScrollPane(list), BorderLayout.CENTER);
//        nad = new NewAliasDialog(this);
        fillAliasList();

    }

    public JButton addAction(Action action, boolean tiny) {
        JButton button = new JButton(action);
        if (tiny) {
            button.setText(null);
            button.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        }
        toolbar.add(button);
        actions.add(action);
        return button;
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
        return getActions();
    }

    public Action[] getActions() {
        Action array[] = new Action[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            array[i] = (Action) actions.get(i);

        }
        return array;
    }

    public JLabel getLabel() {
        JLabel idlabel = new JLabel(caps.getString("identities"));
        idlabel.setIcon(IconTools.getPersonalities());
        idlabel.setLabelFor(list);
        return idlabel;
    }

    private void fillAliasList() {
        try {
            if (signer == null || signer instanceof PersonalSigner)
                return;
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

    public BrowsableSigner getSigner() {
        return signer;
    }

    public String getSelectedValue() {
        return (String) list.getSelectedValue();
    }

    public JList getList() {
        return list;
    }

    final private BrowsableSigner signer;
    private String lastSelected;
    private final JList list;
    private final JToolBar toolbar;
    private final ArrayList actions;
//    private final NewAliasDialog nad;
    private final Preferences prefs;


    private static final String DEFAULT_ALIAS = "DEFAULT_ALIAS";
    private ResourceBundle caps;

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            final KeyStorePanel panel = new KeyStorePanel(new TestCaseSigner());
            frame.getContentPane().add(panel);
        } catch (InvalidPassphraseException e) {
            e.printStackTrace();
            System.exit(1);
        }
        frame.pack();
        frame.show();
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setSelectedIndex(int i) {
        list.setSelectedIndex(i);

    }

    public void setSelectedValue(String s, boolean b) {
        list.setSelectedValue(s, b);

    }

    public ListModel getModel() {
        return list.getModel();
    }

}
