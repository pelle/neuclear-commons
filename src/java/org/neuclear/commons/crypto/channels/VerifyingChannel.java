package org.neuclear.commons.crypto.channels;

import java.io.IOException;
import java.security.*;

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

$Id: VerifyingChannel.java,v 1.1 2004/03/05 23:43:06 pelle Exp $
$Log: VerifyingChannel.java,v $
Revision 1.1  2004/03/05 23:43:06  pelle
New Channels package with nio based channels for various crypto related tasks such as digests, signing, verifying and encoding.
DigestsChannel, SigningChannel and VerifyingChannel are complete, but not tested.
AbstractEncodingChannel will be used for a Base64/Base32 Channel as well as possibly an xml canonicalization channel in the xmlsig library.

*/

/**
 * Channel that reads data and verifies it against a public key
 */

public class VerifyingChannel extends AbstractSignatureChannel {
    public VerifyingChannel(Signature sig, PublicKey key) throws InvalidKeyException {
        super(sig);
        sig.initVerify(key);
    }

    public VerifyingChannel(String alg, PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        super(alg);
        sig.initVerify(key);
    }

    public VerifyingChannel(PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        sig.initVerify(key);
    }

    public boolean verify(byte signature[]) throws SignatureException, IOException {
        boolean verified = sig.verify(signature);
        close();
        return verified;
    }
}
