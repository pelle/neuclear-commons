package org.neuclear.commons.crypto.jce;

import java.security.Provider;

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

$Id: RawJCEProvider.java,v 1.1 2003/12/18 17:40:07 pelle Exp $
$Log: RawJCEProvider.java,v $
Revision 1.1  2003/12/18 17:40:07  pelle
You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
IdentityCreator has been modified to allow creation of keys.
Note The actual Creation of Certificates still have a problem that will be resolved later today.

Revision 1.2  2003/10/01 17:05:37  pelle
Moved the NeuClearCertificate class to be an inner class of Identity.

Revision 1.1  2003/09/30 23:25:15  pelle
Added new JCE Provider and java Certificate implementation for NeuClear Identity.

*/

/**
 *   This is the beginnings of integrating NeuClear into the JCE architecture allowing
 * NeuClear to be plugged in relatively easily for other types of applications such as
 * Code signing.
 * <p>
 * Currently the provider provides a CertificateFactory with the name NeuClear. This
 * can be instantiated using:<br>
 * <tt> CertificateFactory certfact=CertificateFactory.getInstance("NeuClear");</tt><p>
 *
 * User: pelleb
 * Date: Sep 30, 2003
 * Time: 4:32:08 PM
 */
public final class RawJCEProvider extends Provider {
    public RawJCEProvider() {
        super("Raw", 0.5,"NeuClear Provider Implementing Certificates containing nothing but PublicKeys");
        put("CertificateFactory.Raw","org.neuclear.commons.crypto.jce.RawCertificateFactory");
    }

}
