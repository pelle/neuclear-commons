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

$Id: JNDIConnectionSource.java,v 1.1 2003/11/18 23:34:55 pelle Exp $
$Log: JNDIConnectionSource.java,v $
Revision 1.1  2003/11/18 23:34:55  pelle
Payment Web Application is getting there.

*/

/**
 * User: pelleb
 * Date: Nov 18, 2003
 * Time: 6:09:37 PM
 */
public class JNDIConnectionSource implements ConnectionSource {
    public JNDIConnectionSource(String name) throws NamingException {
        InitialContext ctx = new InitialContext();
        this.ds = (DataSource) ctx.lookup(name);

    }

    public Connection getConnection() throws SQLException, IOException {
        return ds.getConnection();
    }

    private final DataSource ds;
}
