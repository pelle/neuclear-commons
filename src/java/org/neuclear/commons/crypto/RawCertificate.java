package org.neuclear.commons.crypto;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

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

$Id: RawCertificate.java,v 1.4 2003/12/10 23:55:45 pelle Exp $
$Log: RawCertificate.java,v $
Revision 1.4  2003/12/10 23:55:45  pelle
Did some cleaning up in the builders
Fixed some stuff in IdentityCreator
New maven goal to create executable jarapp
We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
Will release shortly.

Revision 1.3  2003/12/06 00:16:35  pelle
Updated various areas in NSTools.
Updated URI Validation in particular to support new expanded format
Updated createUniqueID and friends to be a lot more unique and more efficient.
In CryptoTools updated getRandom() to finally use a SecureRandom.
Changed CryptoTools.getFormatURLSafe to getBase36 because that is what it really is.

Revision 1.2  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.1  2003/11/19 23:32:51  pelle
Signers now can generatekeys via the generateKey() method.
Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
SignedNamedObject now contains the full xml which is returned with getEncoded()
This means that it is now possible to further receive on or process a SignedNamedObject, leaving
NamedObjectBuilder for its original purposes of purely generating new Contracts.
NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
Updated all major interfaces that used the old model to use the new model.

*/

/**
 * User: pelleb
 * Date: Nov 19, 2003
 * Time: 1:37:31 PM
 */
public final class RawCertificate extends Certificate {
    public RawCertificate(final PublicKey pub) {
        super("RAW");
        this.pub = pub;
    }

    public final byte[] getEncoded() throws CertificateEncodingException {
        return pub.getEncoded();
    }

    /**
     * Not Implemented Pure Dummy
     * 
     * @param publicKey 
     * @throws CertificateException     
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException      
     * @throws NoSuchProviderException  
     * @throws SignatureException       
     */
    public final void verify(final PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        ;
    }

    /**
     * * Not Implemented Pure Dummy
     * 
     * @param publicKey 
     * @param string    
     * @throws CertificateException     
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException      
     * @throws NoSuchProviderException  
     * @throws SignatureException       
     */
    public final void verify(final PublicKey publicKey, final String string) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        ;
    }

    public final String toString() {
        try {
            return CryptoTools.formatAsBase36(CryptoTools.digest(getEncoded()));
        } catch (Exception e) {
            return "error";
        }
    }

    public final PublicKey getPublicKey() {
        return pub;
    }

    private final PublicKey pub;
}
