package org.neuclear.commons.sql;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;

import org.neuclear.commons.sql.SQLTools;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jul 16, 2003
 * Time: 5:59:50 PM
 * To change this template use Options | File Templates.
 */
public class SQLToolsTest extends TestCase {
    public SQLToolsTest(String string) {
        super(string);
    }

    public void testGetConnection() {
/*
        try {
            Connection con=SQLTools.getConnection();
            assertTrue("No SQL Exception on getConnection()",true);
            assertTrue("No IO Exception",true);
            assertNotNull("Got Connection",con);
            PreparedStatement stmt=con.prepareStatement("select 1");
            assertTrue("No SQL Exception on statement preparation",true);

            assertNotNull("Got Statement",stmt);
            ResultSet rs=stmt.executeQuery();

            assertTrue("Returned a row",rs.next()) ;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
*/

    }
}
