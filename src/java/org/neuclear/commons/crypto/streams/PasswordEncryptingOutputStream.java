package org.neuclear.commons.crypto.streams;

import org.neuclear.commons.crypto.CryptoTools;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

/*
$Id: PasswordEncryptingOutputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: PasswordEncryptingOutputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 12:25:38 PM
 */
public class PasswordEncryptingOutputStream extends CipherOutputStream {
    public PasswordEncryptingOutputStream(OutputStream stream, char password[]) throws GeneralSecurityException {
        super(stream, CryptoTools.makePBECipher(Cipher.ENCRYPT_MODE, password));
    }
}
