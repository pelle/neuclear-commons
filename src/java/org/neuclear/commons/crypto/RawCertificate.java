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

$Id: RawCertificate.java,v 1.1 2003/11/19 23:32:51 pelle Exp $
$Log: RawCertificate.java,v $
Revision 1.1  2003/11/19 23:32:51  pelle
Signers now can generatekeys via the generateKey() method.
Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
SignedNamedObject now contains the full xml which is returned with getEncoded()
This means that it is now possible to further send on or process a SignedNamedObject, leaving
NamedObjectBuilder for its original purposes of purely generating new Contracts.
NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
Updated all major interfaces that used the old model to use the new model.

*/

/**
 * User: pelleb
 * Date: Nov 19, 2003
 * Time: 1:37:31 PM
 */
public class RawCertificate extends Certificate {
    public RawCertificate(PublicKey pub) {
        super("RAW");
        this.pub = pub;
    }

    public byte[] getEncoded() throws CertificateEncodingException {
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
    public void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
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
    public void verify(PublicKey publicKey, String string) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        ;
    }

    public String toString() {
        try {
            return CryptoTools.formatAsURLSafe(CryptoTools.digest(getEncoded()));
        } catch (Exception e) {
            return "error";
        }
    }

    public PublicKey getPublicKey() {
        return pub;
    }

    private final PublicKey pub;
}
