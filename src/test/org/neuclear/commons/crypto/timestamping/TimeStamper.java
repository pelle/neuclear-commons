package org.neuclear.commons.crypto.timestamping;

import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.keyresolvers.KeyResolverFactory;
import org.neuclear.commons.crypto.streams.VerifyingOutputStream;
import org.neuclear.commons.time.TimeTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.text.ParseException;
import java.util.Date;

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

$Id: TimeStamper.java,v 1.1 2004/04/01 23:16:37 pelle Exp $
$Log: TimeStamper.java,v $
Revision 1.1  2004/04/01 23:16:37  pelle
Add TimeStamper which is the first attempt at creating a remote timestamping utility.

*/

/**
 * User: pelleb
 * Date: Apr 1, 2004
 * Time: 10:39:50 PM
 */
public class TimeStamper {
    private TimeStamper() {
    };

    public static Date getTimeStamp(byte digest[]) throws IOException, CryptoException, ParseException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (digest == null || digest.length != 32)
            throw new CryptoException("Couldnt parse TimeServer data");

        final URL url = new URL(TSURL + new String(digest));
        InputStream is = url.openStream();


        // Read the Digest
        final byte tsdig[] = readDigest(is);
        // Verify the Digest is the Same
        for (int i = 0; i < 32; i++) {
            if (digest[i] != tsdig[i])
                throw new CryptoException("Server returned wrong digest");
        }

        // Read the Timestamp
        final byte tsdata[] = new byte[28];
        int read = is.read(tsdata);
        if (read < 28)
            throw new CryptoException("Couldnt parse TimeServer data");
        if (is.read() != '\n')
            throw new CryptoException("Couldnt parse TimeServer data");

        final Date ts = TimeTools.parseTimeStamp(new String(tsdata));
        // Read the PK digest of the TS server
        final byte tspk[] = readDigest(is);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte buffer[] = new byte[8];
        read = is.read(buffer);
        while (read != -1) {
            bos.write(buffer, 0, read);
            read = is.read(buffer);
        }
        final byte sig[] = bos.toByteArray();

        final PublicKey pub = KeyResolverFactory.getInstance().resolve(new String(tspk));

        VerifyingOutputStream vos = new VerifyingOutputStream(pub, sig);
        vos.write(tsdig);
        vos.write('\n');
        vos.write(tsdata);
        vos.write('\n');
        vos.write(tspk);
        if (!vos.verify())
            throw new CryptoException("TimeStamp signature didnt verify");
        return ts;
    }

    private static byte[] readDigest(InputStream is) throws CryptoException, IOException {
        final byte tsdig[] = new byte[32];

        int read = is.read(tsdig);
        if (read != 32)
            throw new CryptoException("Couldnt parse TimeServer data");
        if (is.read() != '\n')
            throw new CryptoException("Couldnt parse TimeServer data");
        return tsdig;
    }

    private static final String TSURL = "http://timecert.org/ts/";
}
