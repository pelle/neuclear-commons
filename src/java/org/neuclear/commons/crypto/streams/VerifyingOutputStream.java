package org.neuclear.commons.crypto.streams;

import org.neuclear.commons.crypto.CryptoTools;

import java.security.*;

/*
$Id: VerifyingOutputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: VerifyingOutputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * Handy Stream for calculating the Digest of data.
 */
public class VerifyingOutputStream extends AbstractSignatureStream {
    public VerifyingOutputStream(KeyPair kp, byte signature[]) throws NoSuchAlgorithmException, InvalidKeyException {
        this(kp.getPublic(), signature);
    }

    public VerifyingOutputStream(PublicKey key, byte signature[]) throws NoSuchAlgorithmException, InvalidKeyException {
        this(CryptoTools.getSignatureCipher(key), signature);

    }

    public VerifyingOutputStream(Signature sig, byte signature[]) {
        super(sig);
        this.signature = signature;
    }

    /**
     * Verify that the Stream was signed by provided signature and PublicKey.
     *
     * @return
     * @throws SignatureException
     */
    public final boolean verify() throws SignatureException {
        return sig.verify(signature);
    }

    private final byte[] signature;
}
