package org.neuclear.commons.crypto.streams;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

/*
$Id: SealedObjectOutputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: SealedObjectOutputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 1:42:28 PM
 */
public class SealedObjectOutputStream extends ObjectOutputStream {
    public SealedObjectOutputStream(OutputStream out, char password[]) throws IOException, GeneralSecurityException {
        super(new PasswordEncryptingOutputStream(out, password));
    }
}
