package org.neuclear.commons.crypto.passphraseagents;

import org.neuclear.commons.NeuClearException;

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

$Id: UserCancellationException.java,v 1.2 2004/01/19 17:53:14 pelle Exp $
$Log: UserCancellationException.java,v $
Revision 1.2  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.1  2003/12/19 19:56:00  pelle
IDEA missed it yet again

Revision 1.1  2003/12/16 23:16:40  pelle
Work done on the SigningServlet. The two phase web model is now only an option.
Allowing much quicker signing, using the GuiDialogueAgent.
The screen has also been cleaned up and displays the xml to be signed.
The GuiDialogueAgent now optionally remembers passphrases and has a checkbox to support this.
The PassPhraseAgent's now have a UserCancellationException, which allows the agent to tell the application if the user specifically
cancels the signing process.

*/

/**
 * User: pelleb
 * Date: Dec 16, 2003
 * Time: 4:28:58 PM
 */
public class UserCancellationException extends NeuClearException {
    public UserCancellationException(String name) {
        super("Cancellation by: "+name);
        this.name=name;
    }

    public String getName() {
        return name;
    }

    private final String name;
}
