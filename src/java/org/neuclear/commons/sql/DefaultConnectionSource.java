package org.neuclear.commons.sql;

import org.neuclear.commons.NeuClearException;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public final class DefaultConnectionSource implements ConnectionSource {

    public Connection getConnection() throws SQLException, IOException {
        return SQLTools.getConnection();
    }

}
