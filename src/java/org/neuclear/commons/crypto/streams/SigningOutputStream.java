package org.neuclear.commons.crypto.streams;

import org.neuclear.commons.crypto.CryptoTools;

import java.security.*;

/*
$Id: SigningOutputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: SigningOutputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * Handy Stream for calculating the Digest of data.
 */
public class SigningOutputStream extends AbstractSignatureStream {
    public SigningOutputStream(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeyException {
        this(kp.getPrivate());
    }

    public SigningOutputStream(PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        this(CryptoTools.getSignatureCipher(key));
    }

    public SigningOutputStream(Signature sig) {
        super(sig);
    }

    public final byte[] sign() throws SignatureException {
        return sig.sign();
    }
}
