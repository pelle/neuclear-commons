package org.neuclear.commons.crypto.passphraseagents.swing;

import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.swing.Messages;

import javax.swing.*;
import java.awt.*;

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

$Id: MessageLabel.java,v 1.3 2004/06/04 23:27:19 pelle Exp $
$Log: MessageLabel.java,v $
Revision 1.3  2004/06/04 23:27:19  pelle
Externalized loads of strings.
More localization to Spanish

Revision 1.2  2004/04/22 12:35:29  pelle
Added Icons and improved localisation

Revision 1.1  2004/04/14 00:10:51  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

*/

/**
 * User: pelleb
 * Date: Apr 13, 2004
 * Time: 9:34:50 PM
 */
public class MessageLabel extends JLabel {
    public MessageLabel() {
//        orig = getBackground();
        setText(" ");

//        setOpaque(false);
    }

    public void info(String message) {
//        setOpaque(true);
//        setBackground(INFO);
        setText(message);
        setIcon(IconTools.getInfo());
    }

    public void invalid(String message) {
//        setOpaque(true);
//        setBackground(INVALID);
        setText(message);
        setIcon(IconTools.getInvalid());
    }

    public void invalidPassphrase() {
        invalid(Messages.getText("invalidpassphrase"));
    }

    public void error(String message) {
//        setOpaque(true);
//        setBackground(ERROR);
        setText(message);
        setIcon(IconTools.getError());
    }

    public void error(Exception e) {
        if (e.getCause() == null)
            error(e.getLocalizedMessage());
        else
            error(e.getCause().getLocalizedMessage());
    }

    public void clear() {
//        setBackground(orig);
        setText(" ");
//        setOpaque(false);
        setIcon(null);
    }

//    private final Color orig;


    public static final Color INFO = new Color(200, 200, 200);
    public static final Color ERROR = Color.RED;
    public static final Color INVALID = Color.PINK;


}
