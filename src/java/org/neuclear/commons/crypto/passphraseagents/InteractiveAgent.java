package org.neuclear.commons.crypto.passphraseagents;

import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;

import java.io.File;

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

$Id: InteractiveAgent.java,v 1.8 2004/04/14 00:10:52 pelle Exp $
$Log: InteractiveAgent.java,v $
Revision 1.8  2004/04/14 00:10:52  pelle
Added a MessageLabel for handling errors, validation and info
Save works well now.
It's pretty much there I think.

Revision 1.7  2004/04/13 18:14:02  pelle
added open dialog to swing agent and interactive agent

Revision 1.6  2004/04/13 17:32:06  pelle
Now has save dialog
Remembers passphrases

Revision 1.5  2004/04/07 17:22:10  pelle
Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
The XMLSig classes have also been updated to support this.

Revision 1.4  2004/03/29 23:48:32  pelle
InteractiveAgent now has a new method which allows signers to ask for a passphrase without specifying alias.
The agents are passed a reference to the Signer, which they can use to browse aliases as well as create new key pairs.

The intention is to encapsulate most of the key management functionality within the InteractiveAgent.

Revision 1.3  2003/12/19 18:02:53  pelle
Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
- For most cases the main exception to worry about now is InvalidNamedObjectException.
- Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
  runtime exception.
- Source and Store patterns each now have their own exceptions that generalizes the various physical
  exceptions that can happen in that area.

Revision 1.2  2003/12/19 00:31:15  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.1  2003/11/11 21:17:46  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.1  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

*/

/**
 * Marker interface to mark interactive PassPhraseAgents.
 * User: pelleb
 * Date: Oct 30, 2003
 * Time: 5:13:49 PM
 */
public interface InteractiveAgent extends PassPhraseAgent {
    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws UserCancellationException
     */
    byte[] sign(final BrowsableSigner signer, byte data[], SetPublicKeyCallBack callback) throws UserCancellationException;

    File getSaveToFileName(String title, String def) throws UserCancellationException;

    File getOpenFileName(String title, String def) throws UserCancellationException;

    char[] getNewPassPhrase(String name) throws UserCancellationException;

}
