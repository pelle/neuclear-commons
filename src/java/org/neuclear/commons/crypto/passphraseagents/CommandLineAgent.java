package org.neuclear.commons.crypto.passphraseagents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

$Id: CommandLineAgent.java,v 1.2 2003/11/19 14:37:37 pelle Exp $
$Log: CommandLineAgent.java,v $
Revision 1.2  2003/11/19 14:37:37  pelle
CommandLineAgent now masks the passphrase input using the JLine library which is now a dependency.
And the beginnings of a KeyGeneratorApplet

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
 * User: pelleb
 * Date: Oct 29, 2003
 * Time: 11:53:29 AM
 */
public class CommandLineAgent implements InteractiveAgent {
    public char[] getPassPhrase(String name) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter passphrase for: " + name);
        System.out.print(": ");
        try {
            //TODO Figure out how to mask input
            return new jline.ConsoleReader().readLine(new Character((char)0)).toCharArray();
        } catch (IOException e) {
            System.err.println("Couldnt read line. Returning empty passphrase");
            return "".toCharArray();
        }
    }

    public static void main(String args[]) {
        PassPhraseAgent dia = new CommandLineAgent();
        System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));
        System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));

        System.exit(0);
    }

}
