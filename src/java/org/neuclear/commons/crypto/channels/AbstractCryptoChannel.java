package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import java.nio.ByteBuffer;

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

$Id: AbstractCryptoChannel.java,v 1.2 2004/03/08 17:13:54 pelle Exp $
$Log: AbstractCryptoChannel.java,v $
Revision 1.2  2004/03/08 17:13:54  pelle
Added CipherChannel and the beginnings of a Base32EncodingChannel.
The AbstractCryptoChannel now is implemented with a pipe. You can get a readable channel with the source() method.
To pipe a ReadableByteChannel or another instance of AbstractCryptoChannel into the channel you can now use the pipe() methods.

Revision 1.1  2004/03/05 23:43:06  pelle
New Channels package with nio based channels for various crypto related tasks such as digests, signing, verifying and encoding.
DigestsChannel, SigningChannel and VerifyingChannel are complete, but not tested.
AbstractEncodingChannel will be used for a Base64/Base32 Channel as well as possibly an xml canonicalization channel in the xmlsig library.

*/

/**
 * An abstract Channel class used to build various crypto related Channels
 */
public abstract class AbstractCryptoChannel implements WritableByteChannel {
    protected AbstractCryptoChannel() throws IOException {
        pipe=Pipe.open();
    }

    public boolean isOpen() {
        return !closed;
    }

    public void close() throws IOException {
        closed = true;
        pipe.sink().close();
    }

    /**
     * Gets the Readable channel for reading the output of this channel.
     * @return
     */
    public Pipe.SourceChannel source(){
        return pipe.source();
    }

    /**
     * Used by sub classes to write byte arrays to the output.
     * @param data
     * @return
     * @throws IOException
     */
    protected int write(byte data[]) throws IOException {
        int count=0;
        int written=0;
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        while( (written=pipe.sink().write(buffer))>0) {count+=written ;};
        return count;
    }

    /**
     * Read and process all data from a given pipe.
     * This closes the input channel when end of stream is reached.
     * @param channel
     * @throws IOException
     */
    public void pipe(ReadableByteChannel channel) throws IOException {
        ByteBuffer buffer=ByteBuffer.allocate(128);
        while(channel.read(buffer)>=0){
            while(write(buffer)>0){};
        }
        channel.close();
        close();
    }

    /**
     * Convenience method for linking together multiple AbstractCryptoChannels into a Crypto pipeline.
     * @param channel
     * @throws IOException
     */
    public void pipe(AbstractCryptoChannel channel) throws IOException {
        pipe(channel.source());
    }
    protected boolean closed = false;
    private final Pipe pipe;

}
