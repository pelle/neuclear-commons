package org.neuclear.commons.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

/**
 * 
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:53:18 PM
 */
public interface ConnectionSource {
    Connection getConnection() throws SQLException, IOException;
}
