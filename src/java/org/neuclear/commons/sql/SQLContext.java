package org.neuclear.commons.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

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

$Id: SQLContext.java,v 1.2 2003/12/14 20:52:54 pelle Exp $
$Log: SQLContext.java,v $
Revision 1.2  2003/12/14 20:52:54  pelle
Added ServletPassPhraseAgent which uses ThreadLocal to transfer the passphrase to the signer.
Added ServletSignerFactory, which builds Signers for use within servlets based on parameters in the Servlets
Init parameters in web.xml
Updated SQLContext to use ThreadLocal
Added jakarta cactus unit tests to neuclear-commons to test the 2 new features above.
Added use of the new features in neuclear-commons to the servilets within neuclear-id and added
configuration parameters in web.xml

Revision 1.1  2003/12/01 15:44:53  pelle
Added XAConnectionSources and Transaction capability through jotm.

*/

/**
 * A thread mapped wrapper around a ConnectionSource. The same thread will always
 * receive the same connection source. A new Connection is created if none exist.
 * The threads connection can be specifically closed with close()
 */
public class SQLContext extends ThreadLocal implements ConnectionSource{
    public SQLContext(ConnectionSource source) {
        this.source = source;
    }

    /**
     * Gets the threads Connection or creates a new one if one doesnt exist.
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public Connection getConnection() throws SQLException, IOException {
        return (Connection)get();
    }
    /**
     * if the thread has an open connection it closes it.
     * @throws SQLException
     */
    public void close() throws SQLException, IOException {
        getConnection().close();
        set(null);
    }
    private final ConnectionSource source;

    protected Object initialValue() {
        try {
            return source.getConnection();    //To change body of overriden methods use Options | File Templates.
        } catch (SQLException e) {
            throw new org.neuclear.commons.LowLevelException(e);
        } catch (IOException e) {
            throw new org.neuclear.commons.LowLevelException(e);
        }
    }

}
