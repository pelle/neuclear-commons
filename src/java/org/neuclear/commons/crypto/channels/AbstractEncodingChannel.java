package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;

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

$Id: AbstractEncodingChannel.java,v 1.1 2004/03/05 23:43:06 pelle Exp $
$Log: AbstractEncodingChannel.java,v $
Revision 1.1  2004/03/05 23:43:06  pelle
New Channels package with nio based channels for various crypto related tasks such as digests, signing, verifying and encoding.
DigestsChannel, SigningChannel and VerifyingChannel are complete, but not tested.
AbstractEncodingChannel will be used for a Base64/Base32 Channel as well as possibly an xml canonicalization channel in the xmlsig library.

*/

/**
 * This is an AbstractChannel for encodings such as base64 etc.
 * You write raw data to it and read the encoded data with read().
 * TODO This should probably be blocking.
 */
public abstract class AbstractEncodingChannel extends AbstractCryptoChannel implements ReadableByteChannel {
    protected AbstractEncodingChannel(int inChunkSize, int outChunkSize) {
        this.outChunkSize = outChunkSize;
        this.inChunkSize = inChunkSize;
        buf = ByteBuffer.allocate(outChunkSize);
    }

    public int write(ByteBuffer buffer) throws IOException {
        if (closed)
            throw new ClosedChannelException();
        return 0;
    }

    public int read(ByteBuffer buffer) throws IOException {
        if (closed)
            throw new ClosedChannelException();
        return 0;
    }

    protected final int outChunkSize;
    protected final int inChunkSize;
    protected final ByteBuffer buf;

}
