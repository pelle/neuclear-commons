package org.neuclear.commons.crypto.passphraseagents;

import java.util.HashMap;
import java.util.Map;

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

$Id: AskAtStartupAgent.java,v 1.7 2004/01/19 17:53:14 pelle Exp $
$Log: AskAtStartupAgent.java,v $
Revision 1.7  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.6  2003/12/22 13:45:25  pelle
Added a naive benchmarking tool.
Fixed a bug in AskAtStartupAgent

Revision 1.5  2003/12/19 18:02:53  pelle
Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
- For most cases the main exception to worry about now is InvalidNamedObjectException.
- Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
  runtime exception.
- Source and Store patterns each now have their own exceptions that generalizes the various physical
  exceptions that can happen in that area.

Revision 1.4  2003/12/19 00:31:15  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.3  2003/12/16 23:16:40  pelle
Work done on the SigningServlet. The two phase web model is now only an option.
Allowing much quicker signing, using the GuiDialogueAgent.
The screen has also been cleaned up and displays the xml to be signed.
The GuiDialogueAgent now optionally remembers passphrases and has a checkbox to support this.
The PassPhraseAgent's now have a UserCancellationException, which allows the agent to tell the application if the user specifically
cancels the signing process.

Revision 1.2  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.1  2003/11/12 16:33:41  pelle
Idea missed checking this in during a refactoring.

Revision 1.1  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

*/

/**
 * AskAtStartupAgent encapsulates another passphraseagent, but  caches each request.
 * It is given an initial argument, which it asks at startup, thus not requiring it to ask the admin
 * for the passphrase at alls.
 */
public final class AskAtStartupAgent implements PassPhraseAgent {
    public AskAtStartupAgent(final InteractiveAgent agent, final String name) throws UserCancellationException {
        this.agent=agent;
        cache=new  HashMap();
        getPassPhrase(name);
    }

    /**
     * Retrieve the PassPhrase for a given name/alias
     * 
     * @param name 
     * @return 
     */
    public final char[] getPassPhrase(final String name) throws UserCancellationException {
        if (cache.containsKey(name))
            return (char[]) cache.get(name);
        else {
            char passphrase[] =agent.getPassPhrase(name);
            cache.put(name,passphrase);
            return passphrase;
        }
    }

    public char[] getPassPhrase(String name, boolean incorrect) throws UserCancellationException {
        if (incorrect) {
             char passphrase[] =agent.getPassPhrase(name,incorrect);
             cache.put(name,passphrase);
             return passphrase;
         }
        return getPassPhrase(name);
    }

    private final Map cache;
    private final PassPhraseAgent agent;

}
