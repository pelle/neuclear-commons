package org.neuclear.commons.crypto.passphraseagents.swing.actions;

import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.PersonalSigner;
import org.neuclear.commons.swing.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: SaveKeyStoreAction.java,v 1.5 2004/06/04 19:09:56 pelle Exp $
$Log: SaveKeyStoreAction.java,v $
Revision 1.5  2004/06/04 19:09:56  pelle
Updated the code now to use the new Messages class for localization support.

Revision 1.4  2004/05/16 00:03:59  pelle
Added SigningServer which encapsulates all the web serving functionality.
Added IdentityPanel which contains an IdentityTree of Identities.
Added AssetPanel
Save now works and Add Personality as well.

Revision 1.3  2004/05/06 21:40:29  pelle
More swing refactorings

Revision 1.2  2004/05/06 17:36:28  pelle
Further slight mods in the gui

Revision 1.1  2004/05/05 23:39:45  pelle
Starting to organize the swing parts of the passphrase agent a bit better.
I am creating actions, panels and dialogs.

*/

/**
 * User: pelleb
 * Date: May 5, 2004
 * Time: 11:16:19 PM
 */
public class SaveKeyStoreAction extends SignerAction implements Runnable {
    public SaveKeyStoreAction(BrowsableSigner signer) {
        super("savekeys", IconTools.getSaveAs(), signer);
        putValue(SHORT_DESCRIPTION, Messages.getText("savekeys"));
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));

    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (signer instanceof PersonalSigner)
            new Thread(this).start();
    }

    public void run() {
        ((PersonalSigner) signer).save();
    }

}
