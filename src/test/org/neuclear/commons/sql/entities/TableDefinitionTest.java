package org.neuclear.commons.sql.entities;

import junit.framework.TestCase;
import org.neuclear.commons.sql.ConnectionSource;
import org.neuclear.commons.sql.DefaultConnectionSource;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.time.TimeTools;

import javax.naming.NamingException;
import java.sql.*;
import java.io.IOException;
import java.util.Random;
import java.math.BigDecimal;

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

$Id: TableDefinitionTest.java,v 1.1 2003/12/24 00:25:41 pelle Exp $
$Log: TableDefinitionTest.java,v $
Revision 1.1  2003/12/24 00:25:41  pelle
Created a kind of poor man's version of ofbiz.org's EntityEngine. It doesnt use xml to configure it, but code.
Should mainly be used to create tables. Can also insert rows, but hasnt been thoroughly tested.
At some point I will improve that part and add some kind of smart querying engine to it. Similar to EntityEngine. But I dont
need that myself right now.
SQLLedger now uses this to create its tables. It is not fully working yet, but will be shortly.

*/

/**
 * User: pelleb
 * Date: Dec 23, 2003
 * Time: 3:26:17 PM
 */
public class TableDefinitionTest extends TestCase{
    public TableDefinitionTest(String string) throws SQLException, NamingException, IOException, NeuClearException {
        super(string);
    }
    public void testSimpleTable(){
        EntityModel table=new EntityModel("accounts", true);
        String correct="CREATE TABLE accounts (\n" +
                "id VARCHAR(50),\n" +
                "PRIMARY KEY(id)\n)";
        System.out.println(table.createDDL());
        assertEquals(correct,table.createDDL());
    }

    public void testComplexTable(){
        EntityModel table=new EntityModel("transaction", false);
        table.addTimeStamp();
        table.addURI("from");
        table.addURI("to");
        table.addMoney();
        table.addComment();
        String correct="CREATE TABLE transaction (\n" +
                "id BIGINT,\n" +
                "valuetime TIMESTAMP,\n" +
                "from VARCHAR(50),\n" +
                "to VARCHAR(50),\n" +
                "amount DECIMAL,\n" +
                "comment VARCHAR(100),\n" +
                "PRIMARY KEY(id)\n)";
        System.out.println(table.createDDL());
        assertEquals(correct,table.createDDL());
    }
    public void testRelationalTable(){
        EntityModel accounts=new EntityModel("accounts", true);
        EntityModel table=new EntityModel("transaction", false);
        table.addTimeStamp();
        table.addReference("sender",accounts);
        table.addReference("to",accounts);
        table.addMoney();
        table.addComment();
        String correct="CREATE TABLE transaction (\n" +
                "id BIGINT,\n" +
                "valuetime TIMESTAMP,\n" +
                "sender VARCHAR(50),\n" +
                "to VARCHAR(50),\n" +
                "amount DECIMAL,\n" +
                "comment VARCHAR(100),\n" +
                "FOREIGN KEY (sender) REFERENCES accounts(id) ON DELETE CASCADE,\n" +
//                "INDEX fromidx (from),\n" +
                "FOREIGN KEY (to) REFERENCES accounts(id) ON DELETE CASCADE,\n" +
//                "INDEX toidx (to),\n" +
                "PRIMARY KEY(id)\n)";
        System.out.println(table.createDDL());
        assertEquals(correct,table.createDDL());
    }

    public void testCreateTableStructure() throws SQLException, IOException, NamingException, NeuClearException {
        EntityModel accounts=new EntityModel("accounts", true);
        EntityModel table=new EntityModel("transaction", false);
        table.addTimeStamp();
        table.addReference("sender",accounts);
        table.addReference("recipient",accounts);
        table.addMoney();
        table.addComment();
        final Connection con = new DefaultConnectionSource().getConnection();
        table.create(con);
        Entity entity=table.insertEntity(con,new Object[] {
            "id",new Long(new Random().nextLong()),
            "valuetime",TimeTools.now(),
            "sender",new Object[]{
                "id","neu://bob@test"
            },
            "recipient",new Object[]{
                "id","neu://alice@test"
            },
            "amount",new Double(100),
            "comment","test comment"
        });
        assertNotNull(entity);

        con.commit();
        con.close();
    }
}
