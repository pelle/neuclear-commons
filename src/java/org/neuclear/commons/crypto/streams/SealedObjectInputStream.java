package org.neuclear.commons.crypto.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;

/*
$Id: SealedObjectInputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: SealedObjectInputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 1:42:28 PM
 */
public class SealedObjectInputStream extends ObjectInputStream {
    public SealedObjectInputStream(InputStream in, char password[]) throws IOException, GeneralSecurityException {
        super(new PasswordDecryptingInputStream(in, password));
    }
}
