package org.neuclear.commons.crypto.channels;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Pipe;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Mar 8, 2004
 * Time: 10:06:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class CipherChannel extends AbstractCryptoChannel{
    public CipherChannel(final Cipher cipher) throws IOException {
        this.cipher = cipher;
    }
    public int write(ByteBuffer buffer) throws IOException {
        if (closed)
            throw new ClosedChannelException();
        final int size = buffer.limit()-buffer.position();
        final int count;

        if (!buffer.isDirect()) {
            bytes=buffer.array();
            count=buffer.limit();
        } else {
            if (bytes==null)
                bytes=new byte[size];
            count=Math.min(size,bytes.length);
            buffer.get(bytes,0,count);
        }
        write(cipher.update(bytes,0,count));
        return count;
    }
    public void close() throws IOException {
        try {
            write(cipher.doFinal());
            super.close();
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e.getLocalizedMessage());
        } catch (BadPaddingException e) {
            throw new IOException(e.getLocalizedMessage());
        }

    }

    private final Cipher cipher;
    private byte[] bytes;
}
