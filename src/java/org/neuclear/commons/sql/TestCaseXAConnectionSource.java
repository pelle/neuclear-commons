package org.neuclear.commons.sql;

import org.neuclear.commons.NeuClearException;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

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

$Id: TestCaseXAConnectionSource.java,v 1.1 2003/12/26 22:51:17 pelle Exp $
$Log: TestCaseXAConnectionSource.java,v $
Revision 1.1  2003/12/26 22:51:17  pelle
Mainly fixes to SQLLedger to support the schema generated by the new EntityModel

*/

/**
 * User: pelleb
 * Date: Dec 26, 2003
 * Time: 4:31:46 PM
 */
public class TestCaseXAConnectionSource extends XAConnectionSource{
    public TestCaseXAConnectionSource() throws SQLException, NeuClearException, NamingException {
        super("jdbc/NeuClearTest", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:target/testdata/db/ledger", "sa", "");
    }

}