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

$Id: SQLContext.java,v 1.1 2003/12/01 15:44:53 pelle Exp $
$Log: SQLContext.java,v $
Revision 1.1  2003/12/01 15:44:53  pelle
Added XAConnectionSources and Transaction capability through jotm.

*/

/**
 * A thread mapped wrapper around a ConnectionSource. The same thread will always
 * receive the same connection source. A new Connection is created if none exist.
 * The threads connection can be specifically closed with close()
 */
public class SQLContext implements ConnectionSource{
    public SQLContext(ConnectionSource source) {
        this.source = source;
        this.map=new HashMap();
    }

    /**
     * Gets the threads Connection or creates a new one if one doesnt exist.
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public Connection getConnection() throws SQLException, IOException {
        Connection con= (Connection)map.get(Thread.currentThread());
        if (con==null||con.isClosed()){
            con=source.getConnection();
            map.put(Thread.currentThread(),con);
        }
        return con;
    }
    /**
     * if the thread has an open connection it closes it.
     * @throws SQLException
     */
    public void close() throws SQLException {
        Connection con= (Connection)map.get(Thread.currentThread());
        if (con!=null||!con.isClosed())
            map.remove(Thread.currentThread());
        con.close();
    }
    private final ConnectionSource source;
    private final Map map;

}
