package org.neuclear.commons.crypto.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Signature;
import java.security.SignatureException;

/*
$Id: AbstractSignatureStream.java,v 1.1 2004/03/31 18:48:24 pelle Exp $
$Log: AbstractSignatureStream.java,v $
Revision 1.1  2004/03/31 18:48:24  pelle
Added various Streams for simplified crypto operations.

*/

/**
 * User: pelleb
 * Date: Mar 31, 2004
 * Time: 11:41:18 AM
 */
public class AbstractSignatureStream extends OutputStream {
    public AbstractSignatureStream(Signature sig) {
        this.sig = sig;
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
    public final void write(int b) throws IOException {
        try {
            sig.update((byte) b);
        } catch (SignatureException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

    protected final Signature sig;
}
