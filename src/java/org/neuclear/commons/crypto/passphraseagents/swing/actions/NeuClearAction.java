package org.neuclear.commons.crypto.passphraseagents.swing.actions;

import org.neuclear.commons.swing.Messages;

import javax.swing.*;

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

$Id: NeuClearAction.java,v 1.4 2004/06/04 19:09:56 pelle Exp $
$Log: NeuClearAction.java,v $
Revision 1.4  2004/06/04 19:09:56  pelle
Updated the code now to use the new Messages class for localization support.

Revision 1.3  2004/06/03 23:13:16  pelle
Changes to Messages. Does not compile.

Revision 1.2  2004/05/27 19:51:26  pelle
The beginnings of the Create Account Page

Revision 1.1  2004/05/05 23:39:45  pelle
Starting to organize the swing parts of the passphrase agent a bit better.
I am creating actions, panels and dialogs.

*/

/**
 * User: pelleb
 * Date: May 5, 2004
 * Time: 11:28:05 PM
 */
public abstract class NeuClearAction extends AbstractAction {
    public NeuClearAction(String name, Icon icon) {
        super(Messages.getText(name), icon);
        this.name = name;
    }

    protected final String name;
}
