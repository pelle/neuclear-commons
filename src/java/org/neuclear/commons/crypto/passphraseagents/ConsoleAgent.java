package org.neuclear.commons.crypto.passphraseagents;

import org.neuclear.commons.Utility;
import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

$Id: ConsoleAgent.java,v 1.7 2004/04/13 18:14:02 pelle Exp $
$Log: ConsoleAgent.java,v $
Revision 1.7  2004/04/13 18:14:02  pelle
added open dialog to swing agent and interactive agent

Revision 1.6  2004/04/13 17:32:06  pelle
Now has save dialog
Remembers passphrases

Revision 1.5  2004/04/07 17:22:09  pelle
Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
The XMLSig classes have also been updated to support this.

Revision 1.4  2004/03/31 23:14:03  pelle
misc changes

Revision 1.3  2004/03/31 18:48:23  pelle
Added various Streams for simplified crypto operations.

Revision 1.2  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.1  2003/12/19 00:31:15  pelle
Lots of usability changes through out all the passphrase agents and end user tools.

Revision 1.4  2003/12/16 23:16:40  pelle
Work done on the SigningServlet. The two phase web model is now only an option.
Allowing much quicker signing, using the GuiDialogueAgent.
The screen has also been cleaned up and displays the xml to be signed.
The GuiDialogueAgent now optionally remembers passphrases and has a checkbox to support this.
The PassPhraseAgent's now have a UserCancellationException, which allows the agent to tell the application if the user specifically
cancels the signing process.

Revision 1.3  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.2  2003/11/19 14:37:37  pelle
ConsoleAgent now masks the passphrase input using the JLine library which is now a dependency.
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
public final class ConsoleAgent implements InteractiveAgent {
    public ConsoleAgent() {
        this.cache = new HashMap();
    }

    public char[] getPassPhrase(String name) throws UserCancellationException {
        return getPassPhrase(name, false);  //To change body of implemented methods use Options | File Templates.
    }

    public final synchronized char[] getPassPhrase(final String name, boolean incorrect) throws UserCancellationException {
        if (!incorrect && cache.containsKey(name))
            return ((String) cache.get(name)).toCharArray();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (incorrect)
            System.out.println("entered passphrase was incorrect please try again");
        System.out.println("Please enter passphrase for: " + name + " ('q' to quit)");
        System.out.print(": ");
        try {
            final String line = new jline.ConsoleReader().readLine(new Character((char) '*'));
            if (line.equals("q"))
                throw new UserCancellationException(name);
            if (firstrun) {
                System.out.println("Do you wish to remember your entered passphrases for this sesson?");
                if (Utility.getAffirmative(false)) {
                    remember = true;
                }
                firstrun = false;
            }
            if (remember)
                cache.put(name, line);
            return line.toCharArray();
        } catch (IOException e) {
            System.err.println("Couldnt read line. Returning empty passphrase");
            return "".toCharArray();
        }
    }

    private final Map cache;
    private boolean remember = false;
    private boolean firstrun = true;

    public static void main(final String[] args) {
        final InteractiveAgent dia = new ConsoleAgent();
        try {
            System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));
            System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));
            System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));
            System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test", true)));
        } catch (UserCancellationException e) {
            System.out.println("user cancelled");
        }

        System.exit(0);
    }


    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws UserCancellationException
     */
    public char[] getPassPhrase(BrowsableSigner signer) throws UserCancellationException {
        return new char[0];
    }

    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws UserCancellationException
     */
    public byte[] sign(BrowsableSigner signer, byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        return new byte[0];
    }

    public File getSaveToFileName(String title, String def) throws UserCancellationException {
        return null;
    }

    public File getOpenFileName(String title, String def) throws UserCancellationException {
        return null;
    }

}
