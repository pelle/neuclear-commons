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

$Id: AskAtStartupAgent.java,v 1.2 2003/11/21 04:43:41 pelle Exp $
$Log: AskAtStartupAgent.java,v $
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
 * User: pelleb
 * Date: Oct 30, 2003
 * Time: 5:09:36 PM
 */
public final class AskAtStartupAgent implements PassPhraseAgent {
    public AskAtStartupAgent(final InteractiveAgent agent, final String name) {
        this.name = name;
        this.passphrase = agent.getPassPhrase(name);
    }

    /**
     * Retrieve the PassPhrase for a given name/alias
     * 
     * @param name 
     * @return 
     */
    public final char[] getPassPhrase(final String name) {
        if (name.equals(this.name))
            return passphrase;
        else
            return new char[0];
    }

    private final String name;
    private final char[] passphrase;

}
