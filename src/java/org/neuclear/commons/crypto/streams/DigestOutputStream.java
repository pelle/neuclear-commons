package org.neuclear.commons.crypto.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
$Id: DigestOutputStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: DigestOutputStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * Handy Stream for calculating the Digest of data.
 */
public class DigestOutputStream extends OutputStream {
    public DigestOutputStream() throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance("sha1"));
    }

    public DigestOutputStream(MessageDigest dig) {
        this.dig = dig;
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p/>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws java.io.IOException if an I/O error occurs. In particular,
     *                             an <code>IOException</code> may be thrown if the
     *                             output stream has been closed.
     */
    public void write(int b) throws IOException {
        dig.update((byte) b);
    }

    public byte[] getDigest() {
        return dig.digest();
    }

    private final MessageDigest dig;
}
