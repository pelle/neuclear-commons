package org.neuclear.commons.crypto.passphraseagents.swing.actions;

import org.neuclear.commons.crypto.passphraseagents.AgentMessages;

import javax.swing.*;
import java.util.ResourceBundle;

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

$Id: NeuClearAction.java,v 1.1 2004/05/05 23:39:45 pelle Exp $
$Log: NeuClearAction.java,v $
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
        super(caps.getString(name), icon);
    }

    protected final static ResourceBundle caps = AgentMessages.getMessages();
}
