package org.neuclear.commons.sql;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

$Id: JNDIConnectionSource.java,v 1.5 2004/01/02 23:19:03 pelle Exp $
$Log: JNDIConnectionSource.java,v $
Revision 1.5  2004/01/02 23:19:03  pelle
Added StatementFactory pattern and refactored the ledger to use it.

Revision 1.4  2003/12/01 15:44:53  pelle
Added XAConnectionSources and Transaction capability through jotm.

Revision 1.3  2003/11/28 00:12:13  pelle
Getting the NeuClear web transactions working.

Revision 1.2  2003/11/21 04:43:42  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.1  2003/11/18 23:34:55  pelle
Payment Web Application is getting there.

*/

/**
 * The JNDIConnectionSource takes as a constructer the name of a JNDI connection source.
 * While not required, for most applications this should implement XA and there should also be
 * a JTA UserTransaction available via JNDI
 */
public final class JNDIConnectionSource implements ConnectionSource {
    public JNDIConnectionSource(final String name) throws NamingException {
        final InitialContext ctx = new InitialContext();
        this.ds = (DataSource) ctx.lookup("java:comp/env/" + name);

    }

    public final Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private final DataSource ds;
}
