package org.neuclear.commons.crypto.passphraseagents;

import org.neuclear.commons.LowLevelException;

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

$Id: StoredPassphraseAgent.java,v 1.5 2004/09/07 21:16:48 pelle Exp $
$Log: StoredPassphraseAgent.java,v $
Revision 1.5  2004/09/07 21:16:48  pelle
Gor rid of some of the benchmark code in the unit tests. There really is no need for a half an hour benchmark to run everytime I run the tests.
Also got rid of some of the annoying messages in AlwaysTheSamePassphraseAgent

Revision 1.4  2003/12/19 18:02:53  pelle
Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
- For most cases the main exception to worry about now is InvalidNamedObjectException.
- Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
  runtime exception.
- Source and Store patterns each now have their own exceptions that generalizes the various physical
  exceptions that can happen in that area.

Revision 1.3  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.2  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

Revision 1.1  2003/11/11 21:17:46  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.1  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

*/

/**
 * This agent contains one passphrase which is read at startup
 * from the configuration files.
 * This should never be used for any kind of production server usage.
 * User: pelleb
 * Date: Oct 30, 2003
 * Time: 5:01:14 PM
 */
public final class StoredPassphraseAgent implements PassPhraseAgent {

    public StoredPassphraseAgent(final String name, final String passphrase) {
        this.name = name;
        this.passphrase = passphrase;
//        System.out.println("StoredPassphraseAgent started.\nDO NOT USE FOR PRODUCTION SERVERS");
    }

    public final char[] getPassPhrase(final String name) {
        if (name.equals(this.name))
            return passphrase.toCharArray();
        else
            throw new LowLevelException(getClass().getName()+"\nCan not provide passphrase for: "+name);
    }

    public char[] getPassPhrase(String name, boolean incorrect) throws UserCancellationException {
        if (incorrect) {
            if (name.equals(this.name))
                throw new LowLevelException(getClass().getName()+"\nIncorrect passphrase set for:"+name);
            else
                throw new LowLevelException(getClass().getName()+"\nCan not provide passphrase for: "+name);
        }
        return getPassPhrase(name);
    }

    private final String name;
    private final String passphrase;
}
