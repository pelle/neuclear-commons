package org.neuclear.commons.crypto.passphraseagents.swing.actions;

import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.passphraseagents.swing.KeyStorePanel;
import org.neuclear.commons.crypto.passphraseagents.swing.NewAliasDialog;

import javax.swing.*;
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

$Id: NewPersonalityAction.java,v 1.4 2004/05/14 23:47:01 pelle Exp $
$Log: NewPersonalityAction.java,v $
Revision 1.4  2004/05/14 23:47:01  pelle
Moved PersonalSigner and OpenSignerDialog to neuclear-commons where they belong.
The whole mechanism of opening keystores is pretty smooth right now.
Currently working on saving, which doesnt quite work yet. I have added a save method to OpenSignerDialog, which
should handle it.

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
public class NewPersonalityAction extends SignerAction {
    public NewPersonalityAction(KeyStorePanel panel) {
        super("newid", IconTools.getAddPersonality(), panel.getSigner());
        putValue(SHORT_DESCRIPTION, caps.getString("newid"));
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
        nad = new NewAliasDialog(panel);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {

        SwingUtilities.invokeLater(nad);
    }

    private NewAliasDialog nad;
}
