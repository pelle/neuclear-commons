package org.neuclear.commons.swing;

import org.neuclear.commons.crypto.passphraseagents.swing.actions.NeuClearAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

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

/**
 * User: pelleb
 * Date: Jun 6, 2004
 * Time: 12:06:15 PM
 */
public class SelectLanguageAction extends NeuClearAction {
    public SelectLanguageAction(JFrame parent, String name) {
        super(name, null);
        language = name;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        Messages.updateLocale(language);
        JOptionPane.showMessageDialog(parent, Messages.getText("langrestart"));
    }

    final String language;
    final JFrame parent;
}
