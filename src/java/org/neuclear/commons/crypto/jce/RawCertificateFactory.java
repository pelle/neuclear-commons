package org.neuclear.commons.crypto.jce;

import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

$Id: RawCertificateFactory.java,v 1.1 2003/12/18 17:40:07 pelle Exp $
$Log: RawCertificateFactory.java,v $
Revision 1.1  2003/12/18 17:40:07  pelle
You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
IdentityCreator has been modified to allow creation of keys.
Note The actual Creation of Certificates still have a problem that will be resolved later today.

Revision 1.8  2003/12/17 12:45:57  pelle
NeuClear JCE Certificates now work with KeyStore.
We can now create JCE certificates based on NeuClear Identity's and store them in a keystore.

Revision 1.7  2003/11/21 04:45:11  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.6  2003/11/18 15:45:09  pelle
FileStoreTest now passes. FileStore works again.

Revision 1.5  2003/11/18 15:07:36  pelle
Changes to JCE Implementation
Working on getting all tests working including store tests

Revision 1.4  2003/11/11 21:18:42  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.3  2003/10/21 22:31:12  pelle
Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
Obviously had to perform many changes throughout the code to support these changes.

Revision 1.2  2003/10/01 17:05:37  pelle
Moved the NeuClearCertificate class to be an inner class of Identity.

Revision 1.1  2003/09/30 23:25:15  pelle
Added new JCE Provider and java Certificate implementation for NeuClear Identity.

*/

/**
 * This is the beginnings of integrating NeuClear into the JCE architecture allowing
 * NeuClear to be plugged in relatively easily for other types of applications such as
 * Code signing.
 * <p>
 * Currently the provider provides a CertificateFactory with the name NeuClear. This
 * can be instantiated using:<br>
 * <tt> CertificateFactory certfact=CertificateFactory.getInstance("NeuClear");</tt><p>
 *
 * User: pelleb
 * Date: Sep 30, 2003
 * Time: 4:39:08 PM
 */
public final class RawCertificateFactory extends CertificateFactorySpi {
    static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    static final String END_CERT = "-----END CERTIFICATE-----";

    final public Certificate engineGenerateCertificate(final InputStream inputStream) throws CertificateException {
        try {
            //Identity id=(Identity) VerifyingReader.getInstance().read(inputStream);
            final BufferedReader d = new BufferedReader(new InputStreamReader(inputStream));
            while(d.readLine().length()<2);//Skip any initial empty lines

            if (!d.readLine().startsWith(BEGIN_CERT))
                throw new CertificateException("Not a Valid Certificate");
            StringBuffer buf=new StringBuffer();
            String line=d.readLine();
            while(line.length()>2&&!line.startsWith(END_CERT)){
                buf.append(line);
                line=d.readLine();
            }
            PublicKey pub=CryptoTools.getPublicKeyFromBase64(buf.toString());
            return new RawCertificate(pub);
        } catch (IOException e) {
            throw new CertificateException("NeuClear: Problem reading Certificate:"+e.getMessage());
        } catch (CryptoException e) {
            throw new CertificateException("NeuClear: Problem reading Certificate:"+e.getMessage());
        }
    }

    final public Collection engineGenerateCertificates(final InputStream inputStream) throws CertificateException {
        final List list=new LinkedList();
        try {
            while(inputStream.available()>0) {
                list.add(engineGenerateCertificate(inputStream));
            }
        } catch (IOException e) {
            throw new CertificateException(e.getLocalizedMessage());
        }
        return list;
    }

    final public CRL engineGenerateCRL(final InputStream inputStream) throws CRLException {
        return null;
    }

    final public Collection engineGenerateCRLs(final InputStream inputStream) throws CRLException {
        return new ArrayList(0);
    }
}
