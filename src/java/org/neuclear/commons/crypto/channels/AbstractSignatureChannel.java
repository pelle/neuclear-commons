package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

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

$Id: AbstractSignatureChannel.java,v 1.3 2004/03/08 17:13:54 pelle Exp $
$Log: AbstractSignatureChannel.java,v $
Revision 1.3  2004/03/08 17:13:54  pelle
Added CipherChannel and the beginnings of a Base32EncodingChannel.
The AbstractCryptoChannel now is implemented with a pipe. You can get a readable channel with the source() method.
To pipe a ReadableByteChannel or another instance of AbstractCryptoChannel into the channel you can now use the pipe() methods.

Revision 1.2  2004/03/06 20:50:28  pelle
Added Unit tests for DigestChannel and SigningChannel.
The SigningChannel passes for Signing on straight signing of byte arrays as well as from Files
Currently the Verifying channel fails, need to investigate.
The DigestChannel passes for all types.

Revision 1.1  2004/03/05 23:43:06  pelle
New Channels package with nio based channels for various crypto related tasks such as digests, signing, verifying and encoding.
DigestsChannel, SigningChannel and VerifyingChannel are complete, but not tested.
AbstractEncodingChannel will be used for a Base64/Base32 Channel as well as possibly an xml canonicalization channel in the xmlsig library.

*/

/**
 * User: pelleb
 * Date: Mar 5, 2004
 * Time: 11:08:47 PM
 */
public abstract class AbstractSignatureChannel extends AbstractCryptoChannel {
    AbstractSignatureChannel(Signature sig) throws IOException {
        this.sig = sig;
    }

    AbstractSignatureChannel(String alg) throws NoSuchAlgorithmException, IOException {
        this(Signature.getInstance(alg));
    }

    AbstractSignatureChannel() throws NoSuchAlgorithmException, IOException {
        this("SHA1withRSA");
    }


    public int write(ByteBuffer buffer) throws IOException {
        if (closed)
            throw new ClosedChannelException();
        final int size = buffer.limit()-buffer.position();
        if (bytes ==null)
            bytes=new byte[size];
        final int count=Math.min(size,bytes.length);
        buffer.get(bytes,0,count);
        try {
            sig.update(bytes,0,count);
        } catch (SignatureException e) {
            throw new IOException(e.getLocalizedMessage());
        }
        return count;
    }

    protected final Signature sig;
    private byte[] bytes;
}
