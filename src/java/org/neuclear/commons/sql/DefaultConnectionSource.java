package org.neuclear.commons.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public class DefaultConnectionSource extends ThreadMappedConnectionSource {

    public Connection createConnection() throws SQLException, IOException {
        return SQLTools.getConnection();
    }


}
