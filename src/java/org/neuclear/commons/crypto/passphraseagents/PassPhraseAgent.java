package org.neuclear.commons.crypto.passphraseagents;


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

$Id: PassPhraseAgent.java,v 1.4 2003/12/19 18:02:53 pelle Exp $
$Log: PassPhraseAgent.java,v $
Revision 1.4  2003/12/19 18:02:53  pelle
Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
- For most cases the main exception to worry about now is InvalidNamedObjectException.
- Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
  runtime exception.
- Source and Store patterns each now have their own exceptions that generalizes the various physical
  exceptions that can happen in that area.

Revision 1.3  2003/12/19 00:31:15  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.2  2003/12/16 23:16:40  pelle
Work done on the SigningServlet. The two phase web model is now only an option.
Allowing much quicker signing, using the GuiDialogueAgent.
The screen has also been cleaned up and displays the xml to be signed.
The GuiDialogueAgent now optionally remembers passphrases and has a checkbox to support this.
The PassPhraseAgent's now have a UserCancellationException, which allows the agent to tell the application if the user specifically
cancels the signing process.

Revision 1.1  2003/11/11 21:17:46  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.2  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

Revision 1.1  2003/10/29 21:16:27  pelle
Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
To use it you pass a byte array and an alias. The sign method then returns the signature.
If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
as SmartCards for end user applications.

*/

/**
 * This interface is used to retrieve passphrases either interactively
 * or from a configuration file.
 * User: pelleb
 * Date: Oct 29, 2003
 * Time: 11:47:39 AM
 */
public interface PassPhraseAgent {

    /**
     * Retrieve the PassPhrase for a given name/alias
     * 
     * @param name 
     * @return 
     */
    char[] getPassPhrase(String name) throws UserCancellationException;

    char[] getPassPhrase(String name,boolean incorrect) throws UserCancellationException;
}
