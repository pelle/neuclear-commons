package org.neuclear.commons.crypto.signers;

import java.security.PublicKey;

/*
$Id: SetPublicKeyCallBack.java,v 1.1 2004/04/07 17:22:10 pelle Exp $
$Log: SetPublicKeyCallBack.java,v $
Revision 1.1  2004/04/07 17:22:10  pelle
Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
The XMLSig classes have also been updated to support this.

*/

/**
 * User: pelleb
 * Date: Apr 7, 2004
 * Time: 11:23:55 AM
 */
public interface SetPublicKeyCallBack {
    public void setPublicKey(PublicKey pub);
}
