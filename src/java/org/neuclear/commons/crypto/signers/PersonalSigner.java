package org.neuclear.commons.crypto.signers;

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

import org.neuclear.commons.LowLevelException;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.passphraseagents.swing.SwingAgent;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.prefs.Preferences;

/**
 * User: pelleb
 * Date: May 13, 2004
 * Time: 9:12:42 AM
 */
public class PersonalSigner implements BrowsableSigner, ComboBoxModel {
    public PersonalSigner(JFrame frame) {
//        signer=null;
        this.frame = frame;
        this.listeners = new EventListenerList();
        this.list = new ArrayList();
        agent = new SwingAgent(this);
        this.dia = new OpenSignerDialog(frame, agent);
        prefs = Preferences.userNodeForPackage(PersonalSigner.class);

    }

    public final byte[] sign(final String name, final byte[] data) throws NonExistingSignerException, UserCancellationException {
        openIfNecessary();
        return signer.sign(name, data);
    }

    private void openIfNecessary() throws UserCancellationException {
        if (signer == null)
            open();
    }

    public void open() throws UserCancellationException {
        signer = dia.openSigner();
        updateList();
        fireListUpdated();
    }


    public final boolean canSignFor(final String name) {
        return signer.canSignFor(name);
    }

    public final int getKeyType(final String name) {
        return signer.getKeyType(name);
    }

    public final PublicKey generateKey(final String alias) throws UserCancellationException {
        openIfNecessary();
        return signer.generateKey(alias);
    }

    public PublicKey generateKey() throws UserCancellationException {
        return signer.generateKey();
    }

    public final PublicKey getPublicKey(final String name) throws NonExistingSignerException {
        return signer.getPublicKey(name);
    }

    public byte[] sign(byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        openIfNecessary();
        return ((InteractiveAgent) agent).sign(this, data, callback);
    }

    public byte[] sign(String name, char pass[], byte data[], SetPublicKeyCallBack callback) throws InvalidPassphraseException {
        return signer.sign(name, pass, data, callback);
    }

    public void createKeyPair(String alias, char passphrase[]) throws CryptoException {
        signer.createKeyPair(alias, passphrase);
        updateList();
        fireListUpdated();
        try {
            save(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UserCancellationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(false);
        } catch (IOException e) {
            throw new LowLevelException(e);
        } catch (UserCancellationException e) {
            throw new LowLevelException(e);
        }
    }

    public void save(final boolean force) throws IOException, UserCancellationException {
        if (signer != null)
            dia.save((JCESigner) signer, force);
    }

    public boolean isOpen() {
        return signer != null;
    }

    public Iterator iterator() throws KeyStoreException {
        if (signer == null)
            return new Iterator() {
                public void remove() {

                }

                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    return null;
                }

            };
        return signer.iterator();
    }

    private void updateList() {
        try {
            Iterator iter = iterator();
            list = new ArrayList();
            while (iter.hasNext()) {
                Object o = (Object) iter.next();
                list.add(o);
            }
        } catch (KeyStoreException e) {
            ;
        }
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index) {
        return list.get(index);
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    public void addListDataListener(ListDataListener l) {
        listeners.add(ListDataListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(ListDataListener.class, l);
    }

    protected void fireListUpdated() {
        // Guaranteed to return a non-null array
        Object[] listenerArray = listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ListDataEvent event = null;
        for (int i = listenerArray.length - 1; i >= 0; i--) {
            if (listenerArray[i] instanceof ListDataListener) {
                // Lazily create the event:
                if (event == null)
                    event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
                ((ListDataListener) listenerArray[i]).contentsChanged(event);
            }
        }
    }

    public Object getSelectedItem() {
        return selected;
    }

    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    private Object selected;

    private OpenSignerDialog dia;
    private BrowsableSigner signer;
    private JFrame frame;
    private ArrayList list;
    private EventListenerList listeners;
    private String filename;
    private final SwingAgent agent;
    private final Preferences prefs;
}
