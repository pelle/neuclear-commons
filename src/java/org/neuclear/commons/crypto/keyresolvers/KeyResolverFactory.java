package org.neuclear.commons.crypto.keyresolvers;

import org.neuclear.commons.Utility;

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

$Id: KeyResolverFactory.java,v 1.3 2004/02/19 15:29:09 pelle Exp $
$Log: KeyResolverFactory.java,v $
Revision 1.3  2004/02/19 15:29:09  pelle
Various cleanups and corrections

Revision 1.2  2004/01/14 16:34:07  pelle
New model of references and signatures now pretty much works.
I am still not 100% sure on the created enveloping signatures. I need to do more testing.

Revision 1.1  2004/01/07 23:11:27  pelle
XMLSig now has various added features:
-  KeyInfo supports X509v3 (untested)
-  KeyInfo supports KeyName
-  When creating a XMLSignature and signing it with a Signer, it adds the alias to the KeyName
Added KeyResolver interface and KeyResolverFactory Class. At the moment no implementations.

*/

/**
 * User: pelleb
 * Date: Jan 7, 2004
 * Time: 9:56:57 PM
 */
public class KeyResolverFactory {
    private KeyResolverFactory() {
    }

    public synchronized final static KeyResolver getInstance() {
        if (instance == null) {
            String impl = System.getProperty("org.neuclear.commons.crypto.keyresolvers.default");
            if (Utility.isEmpty(impl))
                impl = DEFAULT;
            try {
                instance = (KeyResolver) Class.forName(impl).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return instance;
    }

    public final static String DEFAULT = "org.neuclear.commons.crypto.keyresolvers.NullResolver";
    private static KeyResolver instance;
}
