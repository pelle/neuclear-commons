package org.neuclear.commons.sql;

import org.neuclear.commons.NeuClearException;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

/**
 * 
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public final class DefaultConnectionSource implements ConnectionSource {
    public DefaultConnectionSource() throws SQLException, NeuClearException, IOException, NamingException {
        final Properties props=SQLTools.loadProperties();
        cs = new XAConnectionSource(
                props.getProperty("jdbc.class"),
                props.getProperty("jdbc.url"),
                props.getProperty("jdbc.username"),
                props.getProperty("jdbc.password")
        );
    }

    public Connection getConnection() throws SQLException, IOException {
        return cs.getConnection();
    }

    private final ConnectionSource cs;

}
