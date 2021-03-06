package org.neuclear.commons.crypto.streams;

import org.neuclear.commons.crypto.CryptoTools;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/*
$Id: PasswordDecryptingInputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: PasswordDecryptingInputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 12:25:38 PM
 */
public class PasswordDecryptingInputStream extends CipherInputStream {
    public PasswordDecryptingInputStream(InputStream stream, char password[]) throws GeneralSecurityException {
        super(stream, CryptoTools.makePBECipher(Cipher.DECRYPT_MODE, password));
    }
}
