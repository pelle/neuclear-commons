package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Mar 8, 2004
 * Time: 11:38:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class Base32EncodingChannel extends AbstractEncodingChannel {
    public Base32EncodingChannel() throws IOException {
        chunk=new byte[5];
    }

    int encode(ByteBuffer buffer) throws IOException {
        int size = buffer.limit()-buffer.position();
        int chunkSize= (size<5)?size:5;
        buffer.get(chunk,0,chunkSize);
        //TODO finish method
        return chunkSize;
    }
    final byte chunk[];


}
