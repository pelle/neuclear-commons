package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

$Id: DigestChannel.java,v 1.2 2004/03/06 20:50:28 pelle Exp $
$Log: DigestChannel.java,v $
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
 * WritableByteChannel for producing SHA! Digests from ByteBuffers
 */
public class DigestChannel extends AbstractCryptoChannel {
    public DigestChannel() throws NoSuchAlgorithmException {
        this("SHA1");

    }

    public DigestChannel(MessageDigest digest) throws NoSuchAlgorithmException {
        this.digest = digest;
    }

    public DigestChannel(String alg) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(alg));
    }

    public int write(ByteBuffer buffer) throws IOException {
        if (closed)
            throw new ClosedChannelException();
        final int size = buffer.limit()-buffer.position();
        if (bytes ==null)
            bytes=new byte[size];
        final int count=Math.min(size,bytes.length);
        buffer.get(bytes,0,count);
        digest.update(bytes,0,count);
        return count;
    }

    /**
     * Call this to get the Digest
     *
     * @return
     */
    public byte[] getDigest() {
        return digest.digest();
    }

    private final MessageDigest digest;
    private byte[] bytes;
}
